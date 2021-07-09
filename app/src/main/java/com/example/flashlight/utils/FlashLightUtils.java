package com.example.flashlight.utils;

import android.content.Context;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;
import android.os.CountDownTimer;

/**
 * @author hello word
 * @desc 手电筒工具类
 * @date 2021/7/9
 */
public class FlashLightUtils {
    private static Context mContext;
    private static CountDownTimer timer;
    private boolean isOpen = false;
    private static Camera camera;
    private static CameraManager cameraManager;
}
