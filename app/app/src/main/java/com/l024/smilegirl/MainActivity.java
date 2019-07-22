package com.l024.smilegirl;

import android.annotation.TargetApi;
import android.database.Observable;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.l024.smilegirl.constant.ConstantUtil;
import com.l024.smilegirl.ui.view.MySurfaceView;
import com.l024.smilegirl.ui.view.MySurfaceView2;
import com.l024.smilegirl.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    //摄像机类0002
  private Camera camera;
    //自定义
    private MySurfaceView2 surfaceView;
    private SurfaceHolder cameraSurfaceHolder;

    int  cameraType =1;
    byte[] mPreBuffer = new byte[400];
    //判断是否可以录像
    private boolean isRecording = true;
    private MediaRecorder mediaRecorder;     //录制视频类
    private File mRecAudioFile;   //录制视频文件
    private File mRecVideoPath; //视频文件

    //    权限
    private String[] permission = {ConstantUtil.WRITE_EXTERNAL_STORAGE, ConstantUtil.READ_EXTERNAL_STORAGE,
            ConstantUtil.READ_PHONE_STATE,ConstantUtil.CAMERA};
    private static final int REQUECT_CODE_SDCARD = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
    }

    //检1测权限
    @AfterPermissionGranted(REQUECT_CODE_SDCARD)
    public void checkPermission(){
        if (EasyPermissions.hasPermissions(this, permission)) {
            init();
        } else {
            EasyPermissions.requestPermissions(this, "checkPermission", REQUECT_CODE_SDCARD, permission);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (permission.length == perms.size()) {
            init();
        } else {
            ToastUtils.showShort("onPermissionsGranted");
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        ToastUtils.showShort("onPermissionsDenied"+R.string.perminssions_content);
    }

    //初始化
    @TargetApi(17)
    public void init(){
        mRecVideoPath = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/CameraDemo/video/ErrorVideo/");
        Log.i("MainActivity","..."+mRecVideoPath.getAbsolutePath());
        if (!mRecVideoPath.exists()) {
            mRecVideoPath.mkdirs();
            Log.i("MainActivity","创建文件");
        }
        //获取自定义
        surfaceView = (MySurfaceView2) findViewById(R.id.my_surface_view);
        cameraSurfaceHolder = surfaceView.getHolder();
        cameraSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                cameraSurfaceHolder = holder;
                initView();
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
        //设置点击事件
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLong(" 开始");
                //判断是否可以录像
                if(isRecording){
                    //关闭预览
                    camera.stopPreview();
                    //清除内存
                    camera.release();
                    camera = null;
                    //视频类
                    if (null == mediaRecorder) {
                        mediaRecorder = new MediaRecorder();
                    } else {
                        mediaRecorder.reset();
                    }
                    //判断类型开启前后置相机
                    if(cameraType==1){
                        camera = Camera.open(1);
                    }
                    else {
                        camera = Camera.open(0);
                    }
                    //
                    camera.lock();
                    //获取摄像头参数
                    Camera.Parameters parameters = camera.getParameters();
                    if(cameraType==0){
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    }
                    camera.setDisplayOrientation(90);
                    //快门音关闭
                    camera.enableShutterSound(false);
                    parameters.setPreviewFrameRate(25);
                    camera.setParameters(parameters);
                    camera.unlock();
                    mediaRecorder.setCamera(camera);
                    mediaRecorder.setOrientationHint(270);
                    mediaRecorder.setPreviewDisplay(cameraSurfaceHolder.getSurface());
                    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                    mediaRecorder.setVideoSize(640, 480);
                    mediaRecorder.setVideoEncodingBitRate(8*1024*1024);
                    Log.i("MainActivity", "mediaRecorder set sucess");
                    try {
                        mRecAudioFile = File.createTempFile("Vedio", ".avi", mRecVideoPath);
                        Log.i("MainActivity","11111111");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("MainActivity", "..." + mRecAudioFile.getAbsolutePath());
                    mediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isRecording = !isRecording;
                    Log.i("MainActivity", "=====开始录制视频=====");

                }
            }
        });
    }

    //初始化视图
    public void  initView(){
        // 初始化摄像头  ，设置为前置相机
        try {
            if(cameraType==1){
                camera = Camera.open(1);
            }
            else {
                camera = Camera.open(0);
            }
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewFrameRate(30);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);
            camera.addCallbackBuffer(mPreBuffer);
            camera.setPreviewDisplay(cameraSurfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
