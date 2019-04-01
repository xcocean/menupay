package com.njz.menupay.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.njz.menupay.R;
import com.njz.menupay.config.Config;
import com.njz.menupay.controller.MenuController;
import com.njz.menupay.entity.Menu;
import com.njz.menupay.helper.BitmapHelper;
import com.njz.menupay.helper.DataBaseHelper;
import com.njz.menupay.helper.MessageHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;
import com.njz.menupay.utils.Utils;

/**
 * 菜的详细信息
 */
public class DetailedActivity extends AppCompatActivity {

    private Menu menu = new Menu();

    private ImageView ivMenuDetailedPicture;
    private TextView tvMenuDetailedName;
    private TextView tvMenuDetailedDescribe;
    private TextView tvMenuDetailedSalesNumber;
    private TextView tvMenuDetailedPrice;
    private Button btnPay;
    public static DetailedActivity detailedActivity;

    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        detailedActivity = this;

        int id = getIntent().getIntExtra("id", 0);
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from menu limit ?,1", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            menu.setId(cursor.getInt(cursor.getColumnIndex("id")));
            menu.setName(cursor.getString(cursor.getColumnIndex("name")));
            menu.setPrice(cursor.getString(cursor.getColumnIndex("price")));
            menu.setDescribe(cursor.getString(cursor.getColumnIndex("describe")));
            menu.setPicture(cursor.getString(cursor.getColumnIndex("picture")));
            menu.setSalesNumber(cursor.getString(cursor.getColumnIndex("salesNumber")));
        }
        initView();
    }

    private void initView() {
        ivMenuDetailedPicture = findViewById(R.id.ivMenuDetailedPicture);
        tvMenuDetailedName = findViewById(R.id.tvMenuDetailedName);
        tvMenuDetailedDescribe = findViewById(R.id.tvMenuDetailedDescribe);
        tvMenuDetailedSalesNumber = findViewById(R.id.tvMenuDetailedSalesNumber);
        tvMenuDetailedPrice = findViewById(R.id.tvMenuDetailedPrice);
        btnPay = findViewById(R.id.btnPay);

        tvMenuDetailedName.setText(menu.getName());
        tvMenuDetailedDescribe.setText(menu.getDescribe());
        tvMenuDetailedSalesNumber.setText("已售：" + menu.getSalesNumber());
        tvMenuDetailedPrice.setText(menu.getPrice() + " ￥");
        Bitmap bmp = BitmapHelper.getLoacalBitmap(Config.PATH_MENU_PICTURE + "/" + menu.getPicture());
        ivMenuDetailedPicture.setImageBitmap(bmp);

        //点击购买事件
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!SharedPreferencesHelper.getInstance().getBoolean(DetailedActivity.this,"loginStatus",false)){
                    MessageHelper.getInstance().toast(DetailedActivity.this, "您还没有登陆！");
                    return ;
                }
                EditText et = new EditText(DetailedActivity.this);
                AlertDialog.Builder numbe = new AlertDialog.Builder(DetailedActivity.this);
                numbe.setTitle("请输入您的座位号：")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                number = et.getText().toString();
                                if (number != null) {
                                    if (Utils.isNumeric(number)) {
                                        if (Integer.parseInt(number) < 0 || Integer.parseInt(number) > 20) {
                                            MessageHelper.getInstance().toast(DetailedActivity.this, "请输入座位号：1~20");
                                            return;
                                        }
                                        payShow();
                                        return;
                                    }
                                }
                                MessageHelper.getInstance().toast(DetailedActivity.this, "请输入座位号：1~20");
                                return;
                            }
                        }).setNegativeButton("取消", null);
                numbe.show();
            }
        });
    }

    private void payShow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(DetailedActivity.this);
        dialog.setTitle("确认");
        dialog.setMessage("模拟调用三方支付：您将要支付" + menu.getPrice() + "元");
        // 设置“确定”按钮,使用DialogInterface.OnClickListener接口参数
        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MenuController menuController = new MenuController();
                        menuController.pay(DetailedActivity.this, String.valueOf(menu.getId()), number);
                    }
                });
        // 设置“取消”按钮,使用DialogInterface.OnClickListener接口参数
        dialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MessageHelper.getInstance().toast(DetailedActivity.this, "已经取消订单！");
                    }
                });
        dialog.show();
    }
}
