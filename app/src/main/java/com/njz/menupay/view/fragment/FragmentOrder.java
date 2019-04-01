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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.njz.menupay.R;
import com.njz.menupay.controller.OrderController;
import com.njz.menupay.view.activity.OrderDetailedActivity;


public class FragmentOrder extends Fragment {

    private ListView listOrder;
    private SwipeRefreshLayout swipeOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("FragmentUser", "创建订单界面");

        listOrder = getActivity().findViewById(R.id.listOrder);
        swipeOrder = getActivity().findViewById(R.id.swipeOrder);

        OrderController orderController = new OrderController();
        orderController.initOrder(getActivity(), listOrder);

        //下拉刷新
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        swipeOrder.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLACK, Color.YELLOW, Color.BLUE);
        //设置触发刷新的距离
        swipeOrder.setDistanceToTriggerSync(200);
        swipeOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                orderController.initOrder(getActivity(), listOrder);
                //停止刷新的圆圈
                swipeOrder.setRefreshing(false);
            }
        });

        listOrder.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                boolean enable = false;
                if (listOrder != null && listOrder.getChildCount() > 0) {
                    // 检查listView第一个item是否可见
                    boolean firstItemVisible = listOrder.getFirstVisiblePosition() == 0;
                    // 检查第一个item的顶部是否可见
                    boolean topOfFirstItemVisible = listOrder.getChildAt(0).getTop() == 0;
                    // 启用或者禁用SwipeRefreshLayout刷新标识
                    enable = firstItemVisible && topOfFirstItemVisible;
                } else if (listOrder != null && listOrder.getChildCount() == 0) {
                    // 没有数据的时候允许刷新
                    enable = true;
                }
                // 把标识传给swipeRefreshLayout
                swipeOrder.setEnabled(enable);
            }
        });

        //点击订单列表查看详情
        listOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), OrderDetailedActivity.class));
            }
        });
    }
}
