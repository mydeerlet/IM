package com.mydeerlet.im;

import android.app.Application;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Constant.getInstance().init(this);
    }
}
