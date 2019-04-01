package com.njz.menupay;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.njz.menupay.config.Config;
import com.njz.menupay.helper.SharedPreferencesHelper;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MenuPayApplication extends Application {

    private List<Map<String, String>> listMapserver = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //读取手机本地的data/data/...../menu/picture地址
        Config.PATH_MENU_PICTURE = getApplicationContext().getFilesDir().getAbsolutePath() + "/menu/picture";
        File f = new File(Config.PATH_MENU_PICTURE);
        //文件夹不存在时
        if (!f.exists()) {
            //递归创建文件夹
            f.mkdirs();
            //打印日志
            Log.d("启动软件时候，不存在文件夹：", f.getPath());
        } else {
            //打印日志
            Log.d("启动软件时候，存在文件夹：", f.getPath());
        }

        //初始化Bootstrap UI框架  这是引用Bootstrap框架的，用户的圆形头像就是使用这个框架
        TypefaceProvider.registerDefaultIconSets();

        //初始化设备名--整个手机的名称
        SharedPreferencesHelper.getInstance().putString(getApplicationContext(), "device", Build.PRODUCT);
    }
}
