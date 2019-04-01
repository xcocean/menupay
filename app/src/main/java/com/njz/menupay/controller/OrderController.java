package com.njz.menupay.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.njz.menupay.R;
import com.njz.menupay.helper.DataBaseHelper;
import com.njz.menupay.helper.MessageHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;
import com.njz.menupay.model.OrderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController {

    private Context context;
    private Gson gson = new Gson();
    private ListView listView;

    public void initOrder(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
        if (SharedPreferencesHelper.getInstance().getBoolean(context, "loginStatus", false)){
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
                            orders = lo;
                            handler.sendEmptyMessage(201);
                        }else {
                            handler.sendEmptyMessage(205);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(203);
                    }

                }
            }).start();
        }else {
            handler.sendEmptyMessage(204);
        }
    }

    public static List<Map<String, String>> orders = new ArrayList<>();

    private void loadOrder(Context context, ListView listOrder, boolean def) {
        this.listView = listOrder;
        this.context = context;
        if (def) {
            DataBaseHelper helper = new DataBaseHelper(context);
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery("select * from user_order", null);
            List<Map<String, String>> orders = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Map<String, String> m = new HashMap<>();
                    m.put("id", String.valueOf(cursor.getInt(cursor.getColumnIndex("id"))));
                    m.put("handle_status", cursor.getString(cursor.getColumnIndex("handle_status")));
                    m.put("name", cursor.getString(cursor.getColumnIndex("name")));
                    m.put("price", cursor.getString(cursor.getColumnIndex("price")));
                    m.put("seat", cursor.getString(cursor.getColumnIndex("seat")));
                    m.put("create_time", cursor.getString(cursor.getColumnIndex("create_time")));
                    orders.add(m);
                } while (cursor.moveToNext());
            }
            this.orders = orders;
        }
        LoadOrderList loadOrderList = new LoadOrderList();
        listOrder.setAdapter(loadOrderList);
    }

    /**
     * 加载本地的
     */
    private void loadLocal(Context context, ListView listOrder) {
        this.listView = listView;
        this.context = context;
        handler.sendEmptyMessage(202);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 201) {
                loadOrder(context, listView, false);
            } else if (msg.what == 202) {
                loadOrder(context, listView, true);
            } else if (msg.what == 203) {
                loadLocal(context, listView);
            } else if (msg.what == 204) {
                MessageHelper.getInstance().toast(context,"您还没有登陆");
            }else if(msg.what==205){
                MessageHelper.getInstance().toast(context,"您的身份已经过期，请重新登录");
                SharedPreferencesHelper.getInstance().putBoolean(context, "loginStatus", false);
            }
        }
    };

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
//            Log.d("OrderController-ItemId", String.valueOf(position));
            SharedPreferencesHelper.getInstance().putString(context, "orderClickId", orders.get(position).get("id"));
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View itemRootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_order, null);
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
            tvSeat.setText(seat);

            TextView tvTime = itemRootView.findViewById(R.id.tvTime);
            String t = orders.get(i).get("create_time");
            t = t.replace("T", " ").replace(".000+0000", "");
            tvTime.setText(t);
            return itemRootView;
        }
    }

}
