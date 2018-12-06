package com.mydeerlet.im;

import android.content.Context;

public class Constant {


    public static boolean DEBUG_MODE = true;  //调试debug_mode
    public static boolean DEBUG_MODE_LOG = true; //debugLog
//    public static String BASE_URL="http://wthrcdn.etouch.cn/";   //Retrofit 设置的 baseURL
    public static String BASE_URL="http://192.168.1.240:8080/IM/app/";   //Retrofit 设置的 baseURL


    private Context context;
    private static Constant constant;

    public static Constant getInstance(){
        if (constant == null){
            synchronized (Constant.class){
                if (constant == null){
                    constant = new Constant();
                }
            }
        }
        return constant;
    }

    public void init(Context context){
        this.context = context;
    }

    public Context getContext(){
        return context;
    }
}
