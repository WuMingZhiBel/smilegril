package com.l024.smilegirl.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.l024.smilegirl.MainActivity;
import com.l024.smilegirl.R;
import com.l024.smilegirl.constant.ConstantUtil;
import com.l024.smilegirl.presenter.CameraPreview;
import com.l024.smilegirl.ui.view.FaceDetectRoundView;
import com.l024.smilegirl.ui.view.MySurfaceView;
import com.l024.smilegirl.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LaunchActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    private CameraPreview cameraPreview;
    private FaceDetectRoundView mFaceView;

    private Camera camera;
    private MySurfaceView surfaceView;
    private  SurfaceHolder cameraSurfaceHolder;
    private boolean isPreview = false;

    //    权限
    private String[] permission = {ConstantUtil.WRITE_EXTERNAL_STORAGE, ConstantUtil.READ_EXTERNAL_STORAGE,
            ConstantUtil.READ_PHONE_STATE,ConstantUtil.CAMERA};
    private static final int REQUECT_CODE_SDCARD = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.btn_take_picture)
    public void onClick() {
//        //得到照相机的参数
//        Camera.Parameters parameters = cameraPreview.mCamera.getParameters();
//        //图片的格式
//        parameters.setPictureFormat(ImageFormat.JPEG);
//        //预览的大小是多少
//        parameters.setPreviewSize(800, 400);
//        //设置对焦模式，自动对焦
//        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
//        //对焦成功后，自动拍照
//        cameraPreview.mCamera.autoFocus(new Camera.AutoFocusCallback() {
//            @Override
//            public void onAutoFocus(boolean success, Camera camera) {
//                if (success) {
//                    //获取照片
//                    cameraPreview.mCamera.takePicture(null, null, mPictureCallback);
//                }
//            }
//        });

        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
    }

    @AfterPermissionGranted(REQUECT_CODE_SDCARD)
    private void getPermiss() {
        if (EasyPermissions.hasPermissions(this, permission)) {
            initSurfaceView();
        } else {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.permissonTxt), REQUECT_CODE_SDCARD, permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (permission.length == perms.size()) {
            initSurfaceView();
        } else {
            ToastUtils.showShort(R.string.perminssions_content);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtils.showShort(R.string.perminssions_content);
    }
    //初始化数据
    private void initData(){
        //检测并访问相机资源 检查手机是否存在相机资源，如果存在，请求访问相机资源。
        boolean isCamear = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if(isCamear){
            getPermiss();
        }else{
            ToastUtils.showLong("哇,不是叭，居然没检测到的手机的相机耶！");
        }
    }

    //初始化
    private void initSurfaceView(){
        surfaceView = (MySurfaceView)findViewById(R.id.camera_mysurfaceview);
        cameraSurfaceHolder = surfaceView.getHolder();
        cameraSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    initView();
                    camera.setPreviewDisplay(holder);
                    isPreview = true;
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cameraSurfaceHolder = holder;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                cameraSurfaceHolder = holder;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCamera();
            }
        });
        // This method was deprecated in API level 11. this is ignored, this value is set automatically when needed.
        cameraSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initView(){
        // 初始化摄像头  ，设置为前置相机
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        Camera.Parameters parameters = camera.getParameters();
        // 每秒30帧
        parameters.setPreviewFrameRate(30);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
    }
    /**
     * 释放摄像头资源
     */
    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.lock();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
