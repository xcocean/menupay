package com.njz.menupay.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.njz.menupay.R;
import com.njz.menupay.controller.UserController;
import com.njz.menupay.helper.MessageHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.Map;

public class UserSettingActivity extends AppCompatActivity {

    private EditText etPhone, etEmail, etName;
    private CheckBox[] checkBoxes = new CheckBox[6];
    private BootstrapButton btnUserUpdataInfo;
    private String photoName;
    private String name, phone, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        initView();
    }

    private void initView() {
        btnUserUpdataInfo = findViewById(R.id.btnUserUpdataInfo);
        checkBoxes[0] = findViewById(R.id.CheckBox1);
        checkBoxes[1] = findViewById(R.id.CheckBox2);
        checkBoxes[2] = findViewById(R.id.CheckBox3);
        checkBoxes[3] = findViewById(R.id.CheckBox4);
        checkBoxes[4] = findViewById(R.id.CheckBox5);
        checkBoxes[5] = findViewById(R.id.CheckBox6);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        name = SharedPreferencesHelper.getInstance().getString(this, "name", "未设置");
        phone = SharedPreferencesHelper.getInstance().getString(this, "phone", "未设置");
        email = SharedPreferencesHelper.getInstance().getString(this, "email", "未设置");
        etName.setText(name);
        etPhone.setText(phone);
        etEmail.setText(email);

        //点击修改时执行
        btnUserUpdataInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("UserSettingActivity", photoName);
                Map<String, String> map = new HashMap<>();
                UserController userController = new UserController();
                map.put("photo", photoName);
                if (etName.getText() == null || etEmail == null || etPhone == null) {
                    MessageHelper.getInstance().toastCenter(UserSettingActivity.this, "存在空值栏！");
                }
                map.put("name", etName.getText().toString());
                map.put("email", etEmail.getText().toString());
                map.put("phone", etPhone.getText().toString());
                userController.updateInfo(UserSettingActivity.this, btnUserUpdataInfo, map);
            }
        });
        setView();
    }

    private void setView() {
        String photo = SharedPreferencesHelper.getInstance().getString(this, "photo", "default");
        if (photo.equals("default")) {
            checkBoxes[0].setChecked(true);
            photoName = "default";
        } else if (photo.equals("one")) {
            checkBoxes[1].setChecked(true);
            photoName = "one";
        } else if (photo.equals("two")) {
            checkBoxes[2].setChecked(true);
            photoName = "two";
        } else if (photo.equals("three")) {
            checkBoxes[3].setChecked(true);
            photoName = "three";
        } else if (photo.equals("four")) {
            checkBoxes[4].setChecked(true);
            photoName = "four";
        } else if (photo.equals("five")) {
            checkBoxes[5].setChecked(true);
            photoName = "five";
        }
        initCheckBox();
    }

    private void initCheckBox() {
        checkBoxes[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    photoName = "default";
                    for (int i = 0; i < checkBoxes.length; i++) {
                        if (i != 0)
                            checkBoxes[i].setChecked(false);
                    }
                }
            }
        });
        checkBoxes[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    photoName = "one";
                    for (int i = 0; i < checkBoxes.length; i++) {
                        if (i != 1)
                            checkBoxes[i].setChecked(false);
                    }
                }
            }
        });
        checkBoxes[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    photoName = "two";
                    for (int i = 0; i < checkBoxes.length; i++) {
                        if (i != 2)
                            checkBoxes[i].setChecked(false);
                    }
                }
            }
        });
        checkBoxes[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    photoName = "three";
                    for (int i = 0; i < checkBoxes.length; i++) {
                        if (i != 3)
                            checkBoxes[i].setChecked(false);
                    }
                }
            }
        });
        checkBoxes[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    photoName = "four";
                    for (int i = 0; i < checkBoxes.length; i++) {
                        if (i != 4)
                            checkBoxes[i].setChecked(false);
                    }
                }
            }
        });
        checkBoxes[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    photoName = "five";
                    for (int i = 0; i < checkBoxes.length; i++) {
                        if (i != 5)
                            checkBoxes[i].setChecked(false);
                    }
                }
            }
        });

    }
}
