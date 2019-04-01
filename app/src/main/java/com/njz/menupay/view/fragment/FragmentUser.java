package com.njz.menupay.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njz.menupay.R;
import com.njz.menupay.helper.MessageHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;
import com.njz.menupay.view.activity.LoginActivity;
import com.njz.menupay.view.activity.UserBillActivity;
import com.njz.menupay.view.activity.UserInfoActivity;

public class FragmentUser extends Fragment {

    private RelativeLayout llAbout, llBill, llUser;
    private LinearLayout llUserTop;
    private boolean loginStatus;
    private ImageView ivPhoto;
    private TextView tvName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FragmentUser", "创建用户界面");
        //获取登陆状态
        loginStatus = SharedPreferencesHelper.getInstance().getBoolean(getActivity(), "loginStatus", false);
        initView();
    }

    private void initView() {
        llAbout = getActivity().findViewById(R.id.llAbout);
        llUser = getActivity().findViewById(R.id.llUser);
        llBill = getActivity().findViewById(R.id.llBill);
        llUserTop = getActivity().findViewById(R.id.llUserTop);

        tvName = getActivity().findViewById(R.id.tvName);
        ivPhoto = getActivity().findViewById(R.id.ivPhoto);

        //未登录初始化页面
        if (!loginStatus) {
            ivPhoto.setImageResource(R.mipmap.ic_user_default);
            tvName.setText("未登录");
            setPhoto(ivPhoto, "default");
        } else {
            String photo = SharedPreferencesHelper.getInstance().getString(getActivity(), "photo", "default");
            setPhoto(ivPhoto, photo);
        }

        //点击关于
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageHelper.getInstance().toastCenter(getActivity(), "当前版本1.0.1，最新版本1.0.1");
            }
        });
        //点击账单
        llBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPreferencesHelper.getInstance().getBoolean(getActivity(), "loginStatus", false))
                    startActivity(new Intent(getActivity(), UserBillActivity.class));
                else
                    MessageHelper.getInstance().toast(getActivity(), "您还没有登陆");
            }
        });

        llUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userOnclick();
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userOnclick();
            }
        });
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userOnclick();
            }
        });
        llUserTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userOnclick();
            }
        });
    }

    private void userOnclick() {
        //首先判断是否登陆了
        //如果已经登陆了
        if (loginStatus) {
            //前往个人页面
            startActivity(new Intent(getActivity(), UserInfoActivity.class));
        } else {
            //否则前往登陆页面
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }

    }


    /**
     * 回调
     */
    @Override
    public void onResume() {
        super.onResume();
//        Log.d("FragmentUser:", "onResume");
//        登陆后的回调
        String callback = SharedPreferencesHelper.getInstance().getString(getActivity(), "userCallback", "null");
        if (callback.equals("null")) {

        } else {
            //更新头像
            SharedPreferencesHelper.getInstance().putString(getActivity(), "userCallback", "null");
            String photo = SharedPreferencesHelper.getInstance().getString(getActivity(), "photo", "default");
            setPhoto(ivPhoto, photo);
        }

        loginStatus = SharedPreferencesHelper.getInstance().getBoolean(getActivity(), "loginStatus", false);
        if (loginStatus) {
            String name = SharedPreferencesHelper.getInstance().getString(getActivity(), "name", "昵称");
            tvName.setText(name);
        } else {
            tvName.setText("未登录");
            setPhoto(ivPhoto, "default");
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
