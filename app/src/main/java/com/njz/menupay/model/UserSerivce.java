package com.njz.menupay.model;

import android.content.Context;

import com.google.gson.Gson;
import com.njz.menupay.config.Config;
import com.njz.menupay.helper.SharedPreferencesHelper;
import com.njz.menupay.network.NetOkHttp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserSerivce {


    /**
     * 注册
     */
    public String register(Map<String, String> map) {
        try {
            String result = NetOkHttp.postHttp(Config.URL_REGISTER, map);
            Gson gson = new Gson();
            map = gson.fromJson(result, Map.class);
            if (map.get("status").equals(Config.STATUS_SUCCESS)) {
                return Config.STATUS_SUCCESS;
            } else {
                return map.get(Config.RESULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, String> login(Context context, String username, String password) {

        Map<String, String> map = new HashMap<>();
        String device = SharedPreferencesHelper.getInstance().getString(context, "device", "无法识别该设备");
        try {
            map.put("username", username);
            map.put("device", device);
            map.put("password", password);
            String res = NetOkHttp.postHttp(Config.URL_LOGIN, map);
            //解析结果
            Gson gson = new Gson();
            map = gson.fromJson(res, Map.class);
            if (map.get(Config.STATUS).equals(Config.STATUS_SUCCESS)) {
                //表示登陆成功！---把令牌、用户名、昵称、电话头像等、、保存到本地
                SharedPreferencesHelper.getInstance().putBoolean(context, "loginStatus", true);
                SharedPreferencesHelper.getInstance().putString(context, "token", map.get("token"));
                SharedPreferencesHelper.getInstance().putString(context, "username", username);
                //其他信息
                SharedPreferencesHelper.getInstance().putString(context, "name", map.get("name"));
                SharedPreferencesHelper.getInstance().putString(context, "email", map.get("email"));
                SharedPreferencesHelper.getInstance().putString(context, "photo", map.get("photo"));
                SharedPreferencesHelper.getInstance().putString(context, "sex", map.get("sex"));
                SharedPreferencesHelper.getInstance().putString(context, "phone", map.get("phone"));
                SharedPreferencesHelper.getInstance().putString(context, "city", map.get("city"));
            }
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    public String userUpdateInfo(Context context, Map<String, String> map) throws IOException {
        map.put("token", SharedPreferencesHelper.getInstance().getString(context, "token", "token"));
        map.put("username", SharedPreferencesHelper.getInstance().getString(context, "username", "username"));
        String result = NetOkHttp.postHttp(Config.URL_UPDATE_INFO, map);
        return result;
    }
}
