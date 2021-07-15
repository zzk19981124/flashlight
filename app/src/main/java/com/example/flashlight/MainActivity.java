package com.example.flashlight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.flashlight.helper.PermissionHelper;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ToggleButton lightSwitch;
    private Camera mCamera;
    private Button btn_getPermission,startCamera;
    private PermissionHelper mPermissionHelper = new PermissionHelper(this);
    private Context mContext;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBind();

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.switch_light:
                        mPermissionHelper.changeFlashLight(isChecked);
                        break;
                    default:
                        break;
                }
            }
        });
        btn_getPermission.setOnClickListener(this);
        startCamera.setOnClickListener(this);
    }
    private void initBind() {
        lightSwitch = findViewById(R.id.switch_light);
        btn_getPermission = findViewById(R.id.getPermission);
        startCamera = findViewById(R.id.open_camera);
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
            case R.id.open_camera:
                mPermissionHelper.startCamera();
                break;
            default:
                break;
        }
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
                        mPermissionHelper.startAlertDialog();
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
}