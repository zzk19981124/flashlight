package com.example.flashlight;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

/*
* 简单实现
* */
public class MainActivity extends AppCompatActivity {
    private ToggleButton lightSwitch;
    private CameraManager mManager;
    private Camera mCamera = null;
    private android.hardware.Camera.Parameters mParameters = null;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightSwitch = findViewById(R.id.switch_light);
        mManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}