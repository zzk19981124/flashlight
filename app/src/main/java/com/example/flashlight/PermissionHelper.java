package com.example.flashlight;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

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
    public PermissionHelper(Activity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }
    //PermissionRequest request = new PermissionRequest(activity);
    PermissionRequest request = new PermissionRequest((AppCompatActivity) activity);

    /*public void getPermission(){
        request.requestPermission(new PermissionRequest.PermissionListener() {
            @Override
            public void permissionGranted() {
                Toast.makeText(activity,"获取成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void permissionDenied(ArrayList<String> permissions) {

            }

            @Override
            public void permissionNeverAsk(ArrayList<String> permissions) {

            }
        });
    }*/
}
