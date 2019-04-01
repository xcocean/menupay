package com.njz.menupay.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.njz.menupay.R;
import com.njz.menupay.controller.UserController;
import com.njz.menupay.helper.MessageHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView tvRegister;
    private EditText etUsername;
    private EditText etPassword;
    private static String username, password;
    public static LoginActivity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = this;//用来控制登陆成功销毁这个页面

        initView();//后来想了一下，记住密码那些我懒得做了，只做记住账号
    }

    private void initView() {
        //获取页面上的控件对象
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        //判断是否登陆过
        username = SharedPreferencesHelper.getInstance().getString(this, "username", null);
        if (username != null && !username.equals("")) {
            etUsername.setText(username);//赋值到用户名
        }


        UserController userController = new UserController();
        //点击登陆时
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                if (inspectInput(LoginActivity.this, username, password)) {
                    userController.login(LoginActivity.this, username, password, btnLogin);
                }
            }
        });

        //点击注册时
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


    /**
     * 检查输入情况
     */
    public boolean inspectInput(Context context, String username, String passwprd) {
        if (username == null || username.equals("")) {
            MessageHelper.getInstance().toastCenter(context, "用户名不能为空！");
            return false;
        } else if (passwprd == null || passwprd.equals("")) {
            MessageHelper.getInstance().toastCenter(context, "密码不能为空！");
            return false;
        }
        return true;
    }
}
