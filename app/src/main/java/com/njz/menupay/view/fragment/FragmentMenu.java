package com.njz.menupay.view.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.njz.menupay.R;
import com.njz.menupay.entity.Menu;
import com.njz.menupay.controller.MenuController;
import com.njz.menupay.helper.MessageHelper;
import com.njz.menupay.view.activity.DetailedActivity;

import java.util.List;


public class FragmentMenu extends Fragment {

    private List<Menu> listMenu = null;
    private ListView listView;
    private ProgressBar progressMenu;
    private SwipeRefreshLayout swipeMenu;

    private MenuController menuController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //获取list对象
        listView = getActivity().findViewById(R.id.listMenu);
        progressMenu = getActivity().findViewById(R.id.progressMenu);//加载圆圈
        swipeMenu = getActivity().findViewById(R.id.swipeMenu);//下拉刷新

        Log.d("FragmentUser", "创建菜单界面");

        menuController = new MenuController();
        menuController.initMenuResouce(getActivity(), progressMenu, listView);//初始化


        initView();
    }

    private void initView() {
        Intent intent = new Intent(getActivity(), DetailedActivity.class);
        //列表点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent.putExtra("id",i);
                startActivity(intent);
            }
        });

        //下拉刷新事件
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        swipeMenu.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLACK, Color.YELLOW, Color.BLUE);
        //设置触发刷新的距离
        swipeMenu.setDistanceToTriggerSync(200);
        swipeMenu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boolean b= menuController.onRefreshListener(getActivity(),listView);//下拉刷新初始化
                if(b){
                    //停止刷新的圆圈
                    swipeMenu.setRefreshing(false);
                    MessageHelper.getInstance().toastCenter(getActivity(),"刷新成功");
                }else {
                    //停止刷新的圆圈
                    swipeMenu.setRefreshing(false);
                    MessageHelper.getInstance().toastCenter(getActivity(),"刷新失败");
                }
            }
        });

    }
}
