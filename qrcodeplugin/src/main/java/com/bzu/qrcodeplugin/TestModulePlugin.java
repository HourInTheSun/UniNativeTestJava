package com.bzu.qrcodeplugin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

/**
 * author: 孟磊
 * date: 2021/3/15
 * desc: .
 */
public class TestModulePlugin extends WXModule {

    private final String TAG = "elitetyc===>>";

    @JSMethod(uiThread = true)
    public void calcNum(JSONObject options, JSCallback callback) {
        Log.e(TAG, "调用了call方法,两个数字相加");
        Integer num1 = options.getInteger("num1");
        Integer num2 = options.getInteger("num2");
        Integer res = num1 + num2;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("res", res);
        callback.invoke(jsonObject);
    }


    @JSMethod(uiThread = false)
    public void sleepCalcNum(JSONObject options, JSCallback callback) {
        Log.e(TAG, "调用了sleepCalcNum方法,睡眠3秒，两个数字相加");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Integer num1 = options.getInteger("num1");
        Integer num2 = options.getInteger("num2");
        Integer res = num1 + num2;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("res", res);
        callback.invoke(jsonObject);
    }

    private static final int REQUEST_CODE_CAMERA = 1001;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 2001;

    @JSMethod
    public void openCamera() {
        Activity activity = ((Activity) mWXSDKInstance.getContext()); //TODO
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //android 6.0及以上机型需要动态申请权限
            //1.检查权限
            int cameraPermissionResult = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
            if (cameraPermissionResult != PackageManager.PERMISSION_GRANTED) {
                //2-1.没有权限，申请权限
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION_CAMERA);
            } else {
                //2-2.权限通过，打开相机
                activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        } else {
            activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) { //检查相机权限结果
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }

}
