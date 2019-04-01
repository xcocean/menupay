package com.njz.menupay.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.Gson;
import com.njz.menupay.config.Config;
import com.njz.menupay.helper.MessageHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;
import com.njz.menupay.model.UserSerivce;
import com.njz.menupay.view.activity.LoginActivity;
import com.njz.menupay.view.activity.RegisterActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制类
 */
public class UserController {
    private Context context;
    private String message;
    private Button button;

    public void register(Context context, Button button, String username, String password, String email) {
        this.context = context;
        this.button = button;
        handler.sendEmptyMessage(306);
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("email", email);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UserSerivce userSerivce = new UserSerivce();
                    message = userSerivce.register(map);
                    if (message == null) {
                        handler.sendEmptyMessage(300);
                    } else if (message.equals("success")) {//成功！
                        handler.sendEmptyMessage(301);
                    } else {//失败！

                        handler.sendEmptyMessage(302);
                    }
                } catch (Exception e) {
                }
                handler.sendEmptyMessage(307);
            }
        }).start();
    }

    /**
     * 用户登陆
     */
    public void login(Context context, String username, String password, Button button) {
        this.button = button;
        this.context = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(341);
                UserSerivce userSerivce = new UserSerivce();
                Map<String, String> result = new HashMap<>();
                result = userSerivce.login(context, username, password);
                if (!result.isEmpty()) {
                    if (result.get(Config.STATUS).equals(Config.STATUS_SUCCESS)) {
                        //登陆成功
                        SharedPreferencesHelper.getInstance().putString(context, "userCallback", "success");//登陆回调
                        LoginActivity.loginActivity.finish();//销毁登陆页面
                    } else {
                        //登陆失败--弹出结果
                        message = result.get(Config.RESULT);
                        handler.sendEmptyMessage(351);
                    }
                } else {
                    //连接超时
                    handler.sendEmptyMessage(352);
                }
                handler.sendEmptyMessage(342);
            }
        }).start();
    }


    private BootstrapButton bootstrapButton;

    public void updateInfo(Context context, BootstrapButton bootstrapButton, Map<String, String> map) {
        this.bootstrapButton = bootstrapButton;
        this.context = context;

        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(376);
                try {
                    Gson gson = new Gson();
                    UserSerivce userSerivce = new UserSerivce();
                    String result = userSerivce.userUpdateInfo(context, map);
                    Map<String, String> maps = new HashMap<>();
                    maps = gson.fromJson(result, Map.class);
                    if (maps.get(Config.STATUS).equals(Config.STATUS_SUCCESS)) {
                        SharedPreferencesHelper.getInstance().putString(context, "name", map.get("name"));
                        SharedPreferencesHelper.getInstance().putString(context, "photo", map.get("photo"));
                        SharedPreferencesHelper.getInstance().putString(context, "email", map.get("email"));
                        SharedPreferencesHelper.getInstance().putString(context, "phone", map.get("phone"));
                        SharedPreferencesHelper.getInstance().putString(context,"userCallback","success");//修改回调
                        handler.sendEmptyMessage(371);
                    } else {
                        String t = maps.get(Config.RESULT);
                        if (t.equals("tokenInvalid")) {
                            //表示令牌过期
                            handler.sendEmptyMessage(378);
                        } else {
                            message = t;
                            handler.sendEmptyMessage(372);
                        }
                    }
                } catch (Exception e) {

                }
                handler.sendEmptyMessage(377);
            }
        }).start();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 301) {//注册成功！
                RegisterActivity.registerActivity.finish();
                MessageHelper.getInstance().toastCenter(context, "注册成功！");
            } else if (msg.what == 302) {
                MessageHelper.getInstance().toastCenter(context, message);
            } else if (msg.what == 300) {
                MessageHelper.getInstance().toastCenter(context, "网络连接超时-请检查网络");
            } else if (msg.what == 341) {
                button.setEnabled(false);//锁定按钮
                button.setText("登陆中...");
            } else if (msg.what == 342) {
                button.setText("登 陆");
                button.setEnabled(true);//释放按钮
            } else if (msg.what == 351) {
                MessageHelper.getInstance().toastCenter(context, message);
            } else if (msg.what == 352) {
                MessageHelper.getInstance().toastCenter(context, "连接超时-请检查网络");
            } else if (msg.what == 371) {
                MessageHelper.getInstance().toastCenter(context, "修改成功！");
            } else if (msg.what == 372) {
                MessageHelper.getInstance().toastCenter(context, message);
            }
            if (msg.what == 378) {
                MessageHelper.getInstance().toastCenter(context, "您的身份已经过期！请重新登陆。");
            }
            if (msg.what == 376) {
                bootstrapButton.setEnabled(false);
                bootstrapButton.setText("修改中...");
            } else if (msg.what == 377) {
                bootstrapButton.setEnabled(true);
                bootstrapButton.setText("修  改");
            }
            if (msg.what == 306) {
                button.setEnabled(false);//锁定按钮
                button.setText("注册中...");
            } else if (msg.what == 307) {
                button.setText("注 册");
                button.setEnabled(true);//释放按钮
            }
        }
    };
}
