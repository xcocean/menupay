package com.njz.menupay.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String databaseName = "mp";//数据库的名字
    private static final String CREATE_MENU = "create table menu(" +
            "id integer primary key," +
            "name text," +
            "price real," +
            "describe text," +
            "picture text," +
            "serverPicture text," +
            "salesNumber text)";
    private static final String CREATE_ORDER = "create table user_order(" +
            "id integer primary key," +
            "seat text," +
            "price real," +
            "status text," +
            "username text," +
            "menu_id text," +
            "describe text," +
            "handle_status text," +
            "name text," +
            "order_number text," +
            "create_time text)";

    public DataBaseHelper(Context context) {
        super(context, databaseName, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MENU);
        db.execSQL(CREATE_ORDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //升级时存在删除
        db.execSQL("drop table if exists menu");
        db.execSQL("drop table if exists user_order");
    }
}
