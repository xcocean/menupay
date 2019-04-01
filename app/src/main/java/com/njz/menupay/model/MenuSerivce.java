package com.njz.menupay.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njz.menupay.R;
import com.njz.menupay.config.Config;
import com.njz.menupay.entity.Menu;
import com.njz.menupay.helper.BitmapHelper;
import com.njz.menupay.helper.DataBaseHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;
import com.njz.menupay.network.NetOkHttp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuSerivce {

    public List<Menu> getMenu(Context context, boolean def) {
        if (def) {
            try {
                List<Menu> menus = new ArrayList<>();
                Gson gson = new Gson();
                List<Map<String, String>> listMap = null;
                String result = null;
                result = NetOkHttp.getHttp(Config.URL_MENU);
                listMap = gson.fromJson(result, new TypeToken<List<Map<String, String>>>() {
                }.getType());//json数据转化为listMap
                for (int i = 0; i < listMap.size(); i++) {
                    Menu menu = new Menu();
                    menu.setId(Integer.valueOf(listMap.get(i).get("id")));
                    menu.setName(listMap.get(i).get("name"));
                    menu.setDescribe(listMap.get(i).get("describe"));
                    menu.setPrice(listMap.get(i).get("price"));
                    menu.setPicture(listMap.get(i).get("picture"));
                    menu.setServerPicture(Config.URL_SERVER + "/" + listMap.get(i).get("serverPicture"));
                    menu.setSalesNumber(listMap.get(i).get("salesNumber"));
                    menus.add(menu);
                }
                return menus;
            } catch (Exception e) {
                Log.e("getMenu", e.toString());
            }
        } else {//采用本地数据
            DataBaseHelper helper = new DataBaseHelper(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from menu", null);
            if (cursor.moveToFirst()) {
                do {
                    Menu m = new Menu();
                    m.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    m.setName(cursor.getString(cursor.getColumnIndex("name")));
                    m.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                    m.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
                    m.setPicture(cursor.getString(cursor.getColumnIndex("picture")));
                    m.setServerPicture(cursor.getString(cursor.getColumnIndex("serverPicture")));
                    m.setSalesNumber(cursor.getString(cursor.getColumnIndex("salesNumber")));
                    menus.add(m);
                } while (cursor.moveToNext());
            }
            return menus;
        }
        return null;
    }

    public boolean insertMenu(Context context, List<Menu> menus) {
        if (menus == null) {
            return false;
        }
        //将数据写入数据库
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from menu");//删除表的数据
        for (int i = 0; i < menus.size(); i++) {
            db.execSQL("insert into menu(id,name,price,describe,picture,serverPicture,salesNumber) values(?,?,?,?,?,?,?)", new String[]{menus.get(i).getId().toString(),
                    menus.get(i).getName(), menus.get(i).getPrice(), menus.get(i).getDescribe(),
                    menus.get(i).getPicture(), menus.get(i).getServerPicture(), menus.get(i).getSalesNumber()});
        }
        return true;
    }

    public boolean localServer(Context context, List<Menu> menus, boolean re) {
        //获取本地文件夹下的图片名字
        String picturePath = Config.PATH_MENU_PICTURE;
        File localPictures = new File(picturePath);
        File[] tempList = localPictures.listFiles();
        if (re)
            for (int i = 0; i < menus.size(); i++) {
                //逐个遍历
                for (int j = 0; j < tempList.length; j++) {
                    if (menus.get(i).getPicture().equals(tempList[j].getName())) {
                        menus.remove(i);//存在文件了，移除
                    }
                }
            }
        try {
            //执行下载
            FileOutputStream fos = null;
            for (int i = 0; i < menus.size(); i++) {
                File f = new File(picturePath + "/" + menus.get(i).getPicture());
                fos = new FileOutputStream(f);
                Bitmap bmp = NetOkHttp.getURLimage(menus.get(i).getServerPicture());
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
            if (fos != null) {
                fos.close();//关闭流
            }
            return true;
        } catch (IOException e) {
            Log.e("MenuSerivce-localServer", e.toString());
        }
        return false;
    }


    public List<Menu> menus = new ArrayList<>();

    /**
     * 加载本地的
     */
    public void loadLocalMenuList(ListView listView, Context context) {
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from menu", null);
        if (cursor.moveToFirst()) {
            do {
                Menu m = new Menu();
                m.setId(cursor.getInt(cursor.getColumnIndex("id")));
                m.setName(cursor.getString(cursor.getColumnIndex("name")));
                m.setPrice(cursor.getString(cursor.getColumnIndex("price")));
                m.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
                m.setPicture(cursor.getString(cursor.getColumnIndex("picture")));
                m.setSalesNumber(cursor.getString(cursor.getColumnIndex("salesNumber")));
                menus.add(m);
            } while (cursor.moveToNext());
        }
        LoadMenuList loadMenuList = new LoadMenuList();
        listView.setAdapter(loadMenuList);
    }

    class LoadMenuList extends BaseAdapter {
        @Override
        public int getCount() {
            return menus.size();
        }

        @Override
        public Object getItem(int position) {
            return menus.get(position);
        }

        @Override
        public long getItemId(int position) {
            Log.d("MenuSerivce-getItemId", String.valueOf(position));
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View itemRootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_menu, null);
            TextView tvId = itemRootView.findViewById(R.id.tvId);
            tvId.setText(menus.get(i).getId().toString());

            TextView tvMenuName = itemRootView.findViewById(R.id.tvMenuName);
            tvMenuName.setText(menus.get(i).getName());

            TextView tvDesc = itemRootView.findViewById(R.id.tvDesc);
            String desc = menus.get(i).getDescribe();
            if (desc != null)
                if (desc.length() > 23) {
                    desc = desc.substring(0, 23) + "...";
                }
            tvDesc.setText(desc);

            TextView tvPrice = itemRootView.findViewById(R.id.tvPrice);
            String price = menus.get(i).getPrice();
            price = price + " ￥";
            tvPrice.setText(price);

            TextView tvSales = itemRootView.findViewById(R.id.tvSales);
            String sa = "已售 " + menus.get(i).getSalesNumber();
            tvSales.setText(sa);

            //图片-本地的
            ImageView iv = itemRootView.findViewById(R.id.ivPicture);
            Bitmap bmp = BitmapHelper.getLoacalBitmap(Config.PATH_MENU_PICTURE + "/" + menus.get(i).getPicture());
            if (bmp != null) {
                iv.setImageBitmap(bmp);
//                Log.d("MenuSerivce-BaseAdapter", "读取本地图片");
            }
            return itemRootView;
        }
    }


    //提交订单
    public String pay(String username, String token, String id, String number) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("seat", number);//座位号
        map.put("username", username);
        map.put("token", token);
        map.put("id", id);
        return NetOkHttp.postHttp(Config.URL_LOGIN, map);
    }
}
