package com.l024.smilegirl.okgo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import com.l024.smilegirl.R;
import com.l024.smilegirl.constant.ConstantUtil;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自己服务器的数据解析（把result对应的数据回调到requestSucess里面）
 */
public abstract class StringOwnCallback extends StringCallback {
    //弹出菜单
    private ProgressDialog dialog;
    private Activity mActivity;

    public StringOwnCallback(Activity activity) {
        initDialog(activity);
    }
    public StringOwnCallback() {
    }

    private void initDialog(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            this.mActivity = activity;
            dialog = new ProgressDialog(activity);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.dialog_progress_style));
            dialog.setMessage("正在加载...");
        }
    }


    /** 请求网络开始前，UI线程 */
    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        //网络请求前显示对话框
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
//        String token = SPUtils.getInstance(ConstantUtil.ACCOUNT_TABLE).getString(ConstantUtil.TOKEN,"");
//        if (!StringEmpty.isEmpty(token)) {
//            request.getHeaders().put("x-auth-token","Bearer "+token);
//        }
    }

    /** 对返回数据进行操作的回调， UI线程 */
    @Override
    public void onSuccess(Response<String> response) {
       //判断返回值中是否有errorcode
        String body = response.body().toString();
        if(body!=null){
//            try{
//                if(body.contains("errorCode")){
//                    JSONObject jsonObject = new JSONObject(body);
//                    int errcode = jsonObject.optInt("errorCode");
//                    if (errcode==0){
//                        requestSucess(true, "", jsonObject.optString("message"));
//                    } else if(errcode==401){
//                        if (dialog != null && dialog.isShowing()&& mActivity != null && !mActivity.isFinishing()) {
//                            dialog.dismiss();
//                        }
////                        //返回到登录页面 未经授权
////                        SPUtils.getInstance(ConstantUtil.ACCOUNT_TABLE).clear(true);
////                        mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
////                        mActivity.finish();
//                    }else if(errcode==40016){
//                        requestSucess(false, "", jsonObject.optString("message"));
//                    }else{
////                        String token = SPUtils.getInstance(ConstantUtil.ACCOUNT_TABLE).getString(ConstantUtil.TOKEN,"");
////                        if("".equals(token)||token==null){
////                            if (dialog != null && dialog.isShowing()&& mActivity != null && !mActivity.isFinishing()) {
////                                dialog.dismiss();
////                            }
////                            //返回到登录页面 码表数据缺失
////                            SPUtils.getInstance(ConstantUtil.ACCOUNT_TABLE).clear(true);
////                            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
////                            mActivity.finish();
//                        }else{
//                            requestSucess(false, "", jsonObject.optString("message"));
//                        }
//                    }
//                }else {
//                    if(body.equals("")||body.length()==0||body.equals("[]")||body.equals("null")||body==null){
//                        requestSucess(true, "", "");
//                    }else {
//                        requestSucess(true, body, "");
//                    }
//                }
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
        }
    }

    /** 缓存成功的回调,UI线程 */
    @Override
    public void onCacheSuccess(Response<String> response) {
        super.onCacheSuccess(response);
        onSuccess(response);
    }

    /** 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程 */
    @Override
    public void onError(Response<String> response) {
        super.onError(response);
        //网络请求结束后关闭对话框
        //TODO 部分机型存在IllegalArgumentException
        if (dialog != null && dialog.isShowing()&& mActivity != null && !mActivity.isFinishing()) {
            dialog.dismiss();
        }
    }

    /** 请求网络结束后，UI线程 */
    @Override
    public void onFinish() {
        super.onFinish();
        //网络请求结束后关闭对话框
        //TODO 部分机型存在IllegalArgumentException
        if (dialog != null && dialog.isShowing()&& mActivity != null && !mActivity.isFinishing()) {
            dialog.dismiss();
        }
    }

    /** 上传过程中的进度回调，get请求不回调，UI线程 */
    @Override
    public void uploadProgress(Progress progress) {
        super.uploadProgress(progress);
    }

    /** 下载过程中的进度回调，get请求不回调，UI线程 */
    @Override
    public void downloadProgress(Progress progress) {
        super.downloadProgress(progress);
    }

    public abstract void requestSucess(boolean isSucess, String data, String msg);
}

