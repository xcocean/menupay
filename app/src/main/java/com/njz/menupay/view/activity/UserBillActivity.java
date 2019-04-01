package com.njz.menupay.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.njz.menupay.R;
import com.njz.menupay.controller.UserController;

public class UserBillActivity extends AppCompatActivity {

    private TextView tvSumPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bill);

        tvSumPrice = findViewById(R.id.tvSumPrice);
        UserController userController = new UserController();
        userController.getBill(this, tvSumPrice);
    }
}
