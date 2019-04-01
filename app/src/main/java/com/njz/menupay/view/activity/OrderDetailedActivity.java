package com.njz.menupay.view.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.njz.menupay.R;
import com.njz.menupay.helper.DataBaseHelper;
import com.njz.menupay.helper.SharedPreferencesHelper;

import java.util.HashMap;
import java.util.Map;

public class OrderDetailedActivity extends AppCompatActivity {

    private TextView tvMenuDetailedName;
    private TextView tvMenuDetailedDescribe;
    private TextView tvMenuDetailedPrice;
    private TextView tvHandleStatus;
    private TextView tvCreateTime;
    private TextView tvHandleTime;
    private TextView tvAcceptanceTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detailed);

        initView();
    }

    private void initView() {
        String id = SharedPreferencesHelper.getInstance().getString(this, "orderClickId", "0");
        tvMenuDetailedName = findViewById(R.id.tvMenuDetailedName);
        tvMenuDetailedDescribe = findViewById(R.id.tvMenuDetailedDescribe);
        tvMenuDetailedPrice = findViewById(R.id.tvMenuDetailedPrice);
        tvHandleStatus = findViewById(R.id.tvHandleStatus);
        tvCreateTime = findViewById(R.id.tvCreateTime);
        tvHandleTime = findViewById(R.id.tvHandleTime);
        tvAcceptanceTime = findViewById(R.id.tvAcceptanceTime);

        Map<String, String> map = new HashMap<>();
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user_order where id=?", new String[]{id});
        if (cursor.moveToFirst()) {
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("handle_status", cursor.getString(cursor.getColumnIndex("handle_status")));
            map.put("name", cursor.getString(cursor.getColumnIndex("name")));
            map.put("price", cursor.getString(cursor.getColumnIndex("price")));
            map.put("seat", cursor.getString(cursor.getColumnIndex("seat")));
            map.put("create_time", cursor.getString(cursor.getColumnIndex("create_time")));
            map.put("describe", cursor.getString(cursor.getColumnIndex("describe")));
            map.put("handle_time", cursor.getString(cursor.getColumnIndex("handle_time")));
            map.put("complete_time", cursor.getString(cursor.getColumnIndex("complete_time")));
        }

        tvMenuDetailedName.setText(map.get("name"));
        tvMenuDetailedDescribe.setText(map.get("describe"));
        tvMenuDetailedPrice.setText(map.get("price") + " ￥");
        tvHandleStatus.setText("订单状态：" + map.get("handle_status"));
        tvCreateTime.setText("创建时间：" + map.get("create_time").replace("T", " ").replace(".000+0000", ""));
        String handle_time = map.get("handle_time");
        if (handle_time != null)
            tvAcceptanceTime.setText("受理时间：" + handle_time.replace("T", " ").replace(".000+0000", ""));
        String complete_time = map.get("complete_time");
        if (complete_time != null)
            tvHandleTime.setText("完成时间：" + complete_time.replace("T", " ").replace(".000+0000", ""));
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();//销毁当前，释放内存
    }
}
