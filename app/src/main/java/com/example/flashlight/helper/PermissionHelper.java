package com.example.flashlight.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.flashlight.MainActivity;
import com.example.weeboos.permissionlib.PermissionRequest;

import java.util.ArrayList;

/**
 * @author hello word
 * @desc 动态权限获取
 * @date 2021/7/14
 */
public class PermissionHelper {
    private Activity activity;
    private Context context;
    private CameraManager mManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PermissionHelper(Activity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        mManager = (CameraManager) context.getSystemService(context.CAMERA_SERVICE);
    }
    public void startAlertDialog() {
        /*
         * 引导用户打开权限
         * */
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("说明");
        alertDialog.setMessage("需要打开相机权限,才能开启闪光灯");
        alertDialog.setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startSetting();   //打开手机的设置界面
            }
        });
        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mySimpleToast("当您再次点击时，我们会再提醒");
                Toast.makeText(context,"当您再次点击时，我们会再提醒",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.create().show();
    }

    public void startSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, 10);
    }
    public void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, 0);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void changeFlashLight(boolean isChecked) {
        //针对安卓7.0以上实现打开闪光灯
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String[] ids = mManager.getCameraIdList();
                //获取当前手机所有摄像头的id
                for (String id : ids) {
                    CameraCharacteristics c = mManager.getCameraCharacteristics(id);
                    Boolean flashAvailable = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                    Integer lenFacing = c.get(CameraCharacteristics.LENS_FACING);
                    if (flashAvailable != null && flashAvailable && lenFacing != null && lenFacing == CameraCharacteristics.LENS_FACING_BACK){
                        //此摄像头打开闪光灯
                        mManager.setTorchMode(id,isChecked);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
