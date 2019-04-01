package com.njz.menupay.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.njz.menupay.R;
import com.njz.menupay.controller.UserController;
import com.njz.menupay.helper.MessageHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;
import com.njz.menupay.utils.Utils;

public class UserInfoActivity extends AppCompatActivity {

    private ImageView ivPhoto;
    private TextView tvName, tPhone, tEmail;
    private BootstrapButton btnSet, btnLogout, btnUpdatePassword;
    public static UserInfoActivity userInfoActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userInfoActivity = this;

        initView();
    }

    private void initView() {
        tvName = findViewById(R.id.tvName);
        tPhone = findViewById(R.id.tPhone);
        tEmail = findViewById(R.id.tEmail);
        ivPhoto = findViewById(R.id.ivPhoto);
        btnSet = findViewById(R.id.btnSet);
        btnLogout = findViewById(R.id.btnLogout);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        //初始化页面内容
        String photo = SharedPreferencesHelper.getInstance().getString(this, "photo", "default");
        String name = SharedPreferencesHelper.getInstance().getString(this, "name", "昵称");
        String email = SharedPreferencesHelper.getInstance().getString(this, "email", "未设置");
        String phone = SharedPreferencesHelper.getInstance().getString(this, "phone", "未设置");
        tvName.setText(name);
        tEmail.setText(email);
        tPhone.setText(phone);
        setPhoto(ivPhoto, photo);


        //点击修改密码
        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = new EditText(UserInfoActivity.this);
                et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//密码样式
                AlertDialog.Builder numbe = new AlertDialog.Builder(UserInfoActivity.this);
                numbe.setTitle("请输入新密码：")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件
                                if (et == null) {
                                    MessageHelper.getInstance().toastCenter(UserInfoActivity.this, "密码不能为空");
                                } else {
                                    String password = et.getText().toString();
                                    if (password.length() < 4) {
                                        MessageHelper.getInstance().toastCenter(UserInfoActivity.this, "密码长度至少4位");
                                        return;
                                    } else if (!Utils.isPassword(password)) {
                                        MessageHelper.getInstance().toastCenter(UserInfoActivity.this, "密码格式有误！数字、字母");
                                        return ;
                                    }
                                    UserController userController = new UserController();
                                    userController.updatePassword(UserInfoActivity.this, password);
                                }
                                return;
                            }
                        }).setNegativeButton("取消", null);
                numbe.show();
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //打开设置页面
                startActivity(new Intent(UserInfoActivity.this, UserSettingActivity.class));
            }
        });

        //注销登陆
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //修改登陆状态
                SharedPreferencesHelper.getInstance().putBoolean(UserInfoActivity.this, "loginStatus", false);
                SharedPreferencesHelper.getInstance().putString(UserInfoActivity.this, "userCallback", "success");
                //清除token
                SharedPreferencesHelper.getInstance().putString(UserInfoActivity.this, "token", "token");
                SharedPreferencesHelper.getInstance().putString(UserInfoActivity.this, "photo", "default");
                MessageHelper.getInstance().toast(UserInfoActivity.this, "注销成功");
                finish();//销毁当前页面
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("UserInfoActivity", "onPause");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("UserInfoActivity", "onPostResume");
        String callback = SharedPreferencesHelper.getInstance().getString(this, "userCallback", "null");
        if (callback.equals("null")) {

        } else {
            //更新头像昵称、、、
            String photo = SharedPreferencesHelper.getInstance().getString(this, "photo", "default");
            String name = SharedPreferencesHelper.getInstance().getString(this, "name", "昵称");
            String email = SharedPreferencesHelper.getInstance().getString(this, "email", "未设置");
            String phone = SharedPreferencesHelper.getInstance().getString(this, "phone", "未设置");
            tvName.setText(name);
            tEmail.setText(email);
            tPhone.setText(phone);
            setPhoto(ivPhoto, photo);
        }
    }

    /**
     * 头像设置
     */
    private void setPhoto(ImageView photo, String value) {
        switch (value) {
            case "one":
                photo.setImageResource(R.mipmap.ic_user_one);
                break;
            case "two":
                photo.setImageResource(R.mipmap.ic_user_two);
                break;
            case "three":
                photo.setImageResource(R.mipmap.ic_user_three);
                break;
            case "four":
                photo.setImageResource(R.mipmap.ic_user_four);
                break;
            case "five":
                photo.setImageResource(R.mipmap.ic_user_five);
                break;
            default:
                photo.setImageResource(R.mipmap.ic_user_default);
        }
    }
}
