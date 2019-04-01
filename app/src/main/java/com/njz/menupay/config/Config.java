package com.njz.menupay.config;

public class Config {

    //服务器ip+端口
    public static final String URL_SERVER = "http://47.107.40.136:8089";//阿里服务器ip
    public static final String URL_MENU="/get/getMenu";
    public static final String URL_REGISTER="/register/user";
    public static final String URL_LOGIN="/login/user";
    public static final String URL_UPDATE_INFO="/user/updateInfo";
    public static final String URL_GET_ORDER="/user/getOrder";
    public static final String URL_ADD_ORDER="/user/addOrder";
    public static final String URL_USER_ORDER="/user/getBill";
    public static final String URL_UPDATE_PASSWORD="/user/updatePassword";


    //状态码---采用枚举也可以
    public static final String STATUS="status";
    public static final String STATUS_SUCCESS="success";
    public static final String STATUS_ERROR="error";


    //结果
    public static final String RESULT="result";

    //存放服务器图片的本地路径---启动时初始化
    public static String PATH_MENU_PICTURE="";

}
