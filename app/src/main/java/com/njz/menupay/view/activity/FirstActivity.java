package com.njz.menupay.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.njz.menupay.R;
import com.njz.menupay.helper.SharedPreferencesHelper;

/**
 * 第一次启动App
 */
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        //判断是否是第一次在这个手机上运行app
        boolean isFirst = SharedPreferencesHelper.getInstance().getBoolean(this, "isFirstRun", false);
        Log.d("FirstActivity", String.valueOf(isFirst));
        if (!isFirst) {
            SharedPreferencesHelper.getInstance().putBoolean(this, "isFirstRun", true);
        } else {
            //进入主页
            startActivity(new Intent(FirstActivity.this, MainActivity.class));
            finish();//销毁当前页面
        }
        initView();
    }

    private void initView() {
        //按钮点击事件，点击后进入主页
        findViewById(R.id.btnFirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入主页
                startActivity(new Intent(FirstActivity.this, MainActivity.class));
                finish();//销毁当前页面
            }
        });
    }
}
