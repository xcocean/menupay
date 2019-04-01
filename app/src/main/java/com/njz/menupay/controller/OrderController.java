package com.njz.menupay.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.njz.menupay.R;
import com.njz.menupay.config.Config;
import com.njz.menupay.helper.BitmapHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;
import com.njz.menupay.model.OrderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderController {

    private Context context;
    private Gson gson = new Gson();
    private List<Map<String, String>> orders = new ArrayList<>();

    public void initOrder(Context context, ListView listView) {
        this.context = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                String username = SharedPreferencesHelper.getInstance().getString(context, "username", "username");
                String token = SharedPreferencesHelper.getInstance().getString(context, "token", "token");
                OrderService orderService = new OrderService();
                try {
                    OrderService orderService1 = new OrderService();
                    List<Map<String, String>> lo = orderService.getOrder(context, username, token);
                    if (lo != null) {
                        loadOrder(listView, lo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 201) {

            } else if (msg.what == 202) {

            } else if (msg.what == 203) {

            } else if (msg.what == 204) {

            }
        }
    };


    private void loadOrder(ListView listOrder, List<Map<String, String>> orders) {
        this.orders = orders;
        LoadOrderList loadOrderList = new LoadOrderList();
        listOrder.setAdapter(loadOrderList);
    }

    class LoadOrderList extends BaseAdapter {
        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object getItem(int position) {
            return orders.get(position);
        }

        @Override
        public long getItemId(int position) {
            Log.d("MenuSerivce-getItemId", String.valueOf(position));
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View itemRootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_menu, null);
            TextView tvId = itemRootView.findViewById(R.id.tvId);
            tvId.setText(orders.get(i).get("id"));

            TextView tvStatus = itemRootView.findViewById(R.id.tvStatus);
            tvStatus.setText(orders.get(i).get("handle_status"));

            TextView tvMenuName = itemRootView.findViewById(R.id.tvMenuName);
            String name = orders.get(i).get("name");
            if (name != null)
                if (name.length() > 7) {
                    name = name.substring(0, 7) + "...";
                }
            tvMenuName.setText(name);

            TextView tvPrice = itemRootView.findViewById(R.id.tvPrice);
            String price = orders.get(i).get("price");
            price = price + " ￥";
            tvPrice.setText(price);

            TextView tvSeat = itemRootView.findViewById(R.id.tvSeat);
            String seat = orders.get(i).get("seat");
            seat = "座位: " + seat;
            tvPrice.setText(price);

            TextView tvTime = itemRootView.findViewById(R.id.tvTime);
            String t = orders.get(i).get("create_time");
            tvTime.setText(t);
            return itemRootView;
        }
    }

}
