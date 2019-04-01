package com.njz.menupay.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.njz.menupay.R;
import com.njz.menupay.controller.OrderController;


public class FragmentOrder extends Fragment {

    private ListView listOrder;

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

        OrderController orderController = new OrderController();
        orderController.initOrder(getActivity(),listOrder);
    }
}
