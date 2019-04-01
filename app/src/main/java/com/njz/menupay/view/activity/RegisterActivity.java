package com.njz.menupay.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.njz.menupay.R;
import com.njz.menupay.controller.UserController;
import com.njz.menupay.model.UserSerivce;
import com.njz.menupay.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText etRegister1, etRegister2, etRegister3, etRegister4;
    public static RegisterActivity registerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerActivity = this;

        initView();
    }

    private void initView() {
        btnRegister = findViewById(R.id.btnRegister);
        etRegister1 = findViewById(R.id.etRegister1);
        etRegister2 = findViewById(R.id.etRegister2);
        etRegister3 = findViewById(R.id.etRegister3);
        etRegister4 = findViewById(R.id.etRegister4);

        //注册按钮监听
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inspentValues();
            }
        });
    }

    private void inspentValues() {
        //获取输入框的值转化为string类型
        String username = etRegister1.getText().toString();
        String password1 = etRegister2.getText().toString();
        String password2 = etRegister3.getText().toString();
        String email = etRegister4.getText().toString();

        //使用正则表达式检查输入的格式！
        if (!Utils.isUserName(username)) {
            Toast.makeText(RegisterActivity.this, "账号4~12位只能字母或数字组成", Toast.LENGTH_SHORT).show();
            btnRegister.setClickable(true);//解锁
            return;
        } else if (!password1.equals(password2)) {
            Toast.makeText(RegisterActivity.this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
            btnRegister.setClickable(true);//解锁
            return;
        } else if (!Utils.isPassword(password1)) {
            Toast.makeText(RegisterActivity.this, "密码 4~12位只能字母或数字组成", Toast.LENGTH_SHORT).show();
            btnRegister.setClickable(true);//解锁
            return;
        } else if (!Utils.isEmail(email)) {
            Toast.makeText(RegisterActivity.this, "邮箱格式有误！", Toast.LENGTH_SHORT).show();
            btnRegister.setClickable(true);//解锁
            return;
        }
        UserController userController=new UserController();
        userController.register(RegisterActivity.this,btnRegister,username,password1,email);
    }
}
