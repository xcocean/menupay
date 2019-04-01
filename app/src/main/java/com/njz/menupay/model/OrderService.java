package com.njz.menupay.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.njz.menupay.config.Config;
import com.njz.menupay.helper.DataBaseHelper;
import com.njz.menupay.helper.MessageHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;
import com.njz.menupay.network.NetOkHttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderService {
    private Gson gson = new Gson();

    public List<Map<String, String>> getOrder(Context context, String username, String token) throws Exception {
        String res = NetOkHttp.getHttp(Config.URL_GET_ORDER + "/?username=" + username + "&token=" + token);
        if (res != null) {
            if (res.equals("")) {
                return null;
            }
        }
        List<Map<String, String>> orders = new ArrayList<>();
        orders = gson.fromJson(res, new TypeToken<List<Map<String, String>>>() {
        }.getType());//json数据转化为listMap
        //将数据写入数据库
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from user_order ");//删除表的数据
        for (int i = 0; i < orders.size(); i++) {
            db.execSQL("insert into user_order(id,seat,create_time,price,order_number,name,handle_status,menu_id,username,status,describe,handle_time,complete_time)" +
                    " values(?,?,?,?,?,?,?,?,?,?,?,?,?)", new String[]{orders.get(i).get("id"),
                    orders.get(i).get("seat"), orders.get(i).get("create_time"), orders.get(i).get("price"),
                    orders.get(i).get("order_number"), orders.get(i).get("name"), orders.get(i).get("handle_status"),
                    orders.get(i).get("menu_id"), orders.get(i).get("username"), orders.get(i).get("status"),
                    orders.get(i).get("describe"), orders.get(i).get("handle_time"), orders.get(i).get("complete_time")});
        }
        return orders;
    }


}
