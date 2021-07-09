package com.example.flashlight.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/7/9
 */
public class myToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public myToast(Context context) {
        super(context);
    }

}
