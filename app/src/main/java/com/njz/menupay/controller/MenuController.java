package com.njz.menupay.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.njz.menupay.config.Config;
import com.njz.menupay.entity.Menu;
import com.njz.menupay.helper.MessageHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;
import com.njz.menupay.model.MenuSerivce;
import com.njz.menupay.view.activity.DetailedActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuController {

    private List<Menu> menuList = new ArrayList<>();
    private ProgressBar bar;
    private MenuSerivce serivce;
    private ListView listView;
    private Context context;
    private String message;


    public void initMenuResouce(Context context, ProgressBar bar, ListView listView) {
        this.bar = bar;
        this.listView = listView;
        this.context = context;
        handler.sendEmptyMessage(151);//显示加载
        new Thread(new Runnable() {
            @Override
            public void run() {
                serivce = new MenuSerivce();
                menuList = serivce.getMenu(context, true);
                if (menuList != null) {
                    //把服务器的数据下载到本地和加入到数据库
                    serivce.insertMenu(context, menuList);
                    serivce.localServer(context, menuList, false);
                    handler.sendEmptyMessage(101);
                } else {
                    //加载本地数据
                    handler.sendEmptyMessage(181);
                    menuList = serivce.getMenu(context, false);
                    serivce.insertMenu(context, menuList);
                    handler.sendEmptyMessage(101);
                }
                handler.sendEmptyMessage(152);
            }
        }).start();
    }

    /**
     * 下拉刷新事件
     */
    public boolean onRefreshListener(Context context, ListView listView) {
        this.bar = bar;
        this.listView = listView;
        this.context = context;
        handler.sendEmptyMessage(151);//显示加载
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(151);
                serivce = new MenuSerivce();
                menuList = serivce.getMenu(context, true);
                if (menuList != null) {
                    //把服务器的数据下载到本地和加入到数据库
                    serivce.insertMenu(context, menuList);
                    serivce.localServer(context, menuList, false);
                    handler.sendEmptyMessage(101);
                } else {
                    handler.sendEmptyMessage(181);
                }
                try {
                    Thread.sleep(1000);//线程延迟
                } catch (InterruptedException e) {
                }
                handler.sendEmptyMessage(152);
            }
        }).start();
        return true;
    }

    public void pay(Context context, String id, String number) {
        this.context = context;
        MenuSerivce menuSerivce = new MenuSerivce();
        String username = SharedPreferencesHelper.getInstance().getString(context, "username", "username");
        String token = SharedPreferencesHelper.getInstance().getString(context, "token", "token");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String res = menuSerivce.pay(username, token, id, number);
                    Map<String, String> map = new HashMap<>();
                    Gson gson = new Gson();
                    map = gson.fromJson(res, Map.class);
                    if (res == null) {
                        handler.sendEmptyMessage(131);
                    } else {
                        if (map.get(Config.STATUS).equals(Config.STATUS_SUCCESS)) {
                            handler.sendEmptyMessage(133);
                        } else {
                            handler.sendEmptyMessage(132);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("handler", String.valueOf(msg.what));
            if (msg.what == 101) {
                List<Menu> menus = new ArrayList<>();
                serivce.loadLocalMenuList(listView, context);
            } else if (msg.what == 151) {
                //显示加载圈圈
                bar.setVisibility(View.VISIBLE);
            } else if (msg.what == 152) {
                //隐藏加载圈圈
                bar.setVisibility(View.INVISIBLE);
            } else if (msg.what == 181) {
                MessageHelper.getInstance().toastCenter(context, "网络连接超时，请检查您的网络,将加载本地数据");
            }
            if (msg.what == 131) {
                MessageHelper.getInstance().toastCenter(context, "网络连接超时，请检查您的网络");
            } else if (msg.what == 132) {
                MessageHelper.getInstance().toast(context, "订单处理错误！-500");
            } else if (msg.what == 133) {
                DetailedActivity.detailedActivity.finish();//销毁页面
                MessageHelper.getInstance().toast(context, "支付成功！正在处理订单！");
            }
        }
    };

}
