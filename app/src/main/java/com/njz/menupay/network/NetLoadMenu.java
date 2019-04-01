package com.njz.menupay.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njz.menupay.config.Config;
import com.njz.menupay.entity.Menu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class NetLoadMenu {
    private ImageView loadImg;
    private String imgUrl;
    private byte[] picByte;
    public List<Menu> listMenu = null;
    private ListView listView;
    public List<Map<String, String>> listMap = null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (picByte != null) {
                if (msg.what == 0x123) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(picByte, 0, picByte.length);
                    loadImg.setImageBitmap(bitmap);
                }
            }
        }
    };


    public void httpLoad(ListView listView) {
        this.listView = listView;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Gson gson = new Gson();
                    String result = null;
                    result = NetOkHttp.getHttp(Config.URL_MENU);
                    listMap = gson.fromJson(result, new TypeToken<List<Map<String, String>>>() {
                    }.getType());//json数据转化为listMap
                    /*for (int i = 0; i < listMap.size(); i++) {
                        Menu menu = new Menu();
                        menu.setId(Integer.valueOf(listMap.get(i).get("id")));
                        menu.setName(listMap.get(i).get("name"));
                        menu.setPrice(listMap.get(i).get("price"));
                        menu.setDescribe(listMap.get(i).get("describe"));
                        menu.setPicture(listMap.get(i).get("picture"));
                        menu.setServerPicture(listMap.get(i).get("serverPicture"));
                        menu.setSalesNumber(listMap.get(i).get("salesNumber"));
                        Log.d("NetLoadMenu:", menu.toString());
                        listMenu.add(i, menu);
                    }*/

                } catch (Exception e) {
                    Log.d("NetLoadMenu:", e.toString());
                }
                handler.sendEmptyMessage(0x101);
            }
        }).start();
    }

    public void load(ImageView loadImg, String imgUrl) {
        this.loadImg = loadImg;
        this.imgUrl = imgUrl;
        Drawable drawable = loadImg.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
        new Thread(runnable).start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(imgUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(10000);
                if (conn.getResponseCode() == 200) {
                    InputStream in = conn.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while ((length = in.read(bytes)) != -1) {
                        out.write(bytes, 0, length);
                    }
                    picByte = out.toByteArray();
                    in.close();
                    out.close();
                    handler.sendEmptyMessage(0x123);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


}
