package com.example.flashlight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.weeboos.permissionlib.PermissionRequest;

import java.util.ArrayList;

/*
 * 简单实现
 * */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ToggleButton lightSwitch;
    private CameraManager mManager;
    private Camera mCamera;
    private android.hardware.Camera.Parameters parameters;
    MainActivity instance;
    private Button btn_getPermission;
    private static final String[] list_permission = new String[]{
            Manifest.permission.CAMERA
    };
    private String[] get_toast_text = new String[]{
            "获取成功", "获取失败", "不再询问"
    };
    private String getString;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mySimpleToast("打开应用测试成功");
        initBind();

        mManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        // mPermissionHelper = new PermissionHelper(this);
        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.switch_light:
                        if (buttonView.isChecked()) {
                            new Thread(new TurnOn()).start();
                        } else {
                            try {
                                parameters = mCamera.getParameters();
                                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                                mCamera.setParameters(parameters);
                                mCamera.release();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                        break;
                    default:
                        break;
                }

            }
        });
        btn_getPermission.setOnClickListener(this);
    }

    public void openCamera(View view) {
        startCamera();
    }

    private class TurnOn implements Runnable {

        @Override
        public void run() {
            mCamera = Camera.open();
            parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);

        }
    }

    private void initBind() {
        lightSwitch = findViewById(R.id.switch_light);
        btn_getPermission = findViewById(R.id.getPermission);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getPermission:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    mySimpleToast("已开启权限");
                    //打开箱机
                    //startCamera();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
                }
                break;
            default:
                break;
        }
    }

    private void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (permissions[0].equals(Manifest.permission.CAMERA)) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        mySimpleToast("已打开相机权限");

                    } else {
                        startAlertDialog();
                    }
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                mySimpleToast("感谢您的同意");
            }

        }
    }

    public void startAlertDialog() {
        /*
         * 引导用户打开权限
         * */
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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
                mySimpleToast("当您再次点击时，我们会再提醒");
            }
        });
        alertDialog.create().show();
    }

    public void startSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void mySimpleToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void changeFlashLight(boolean isChecked) {
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

                    }
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
}