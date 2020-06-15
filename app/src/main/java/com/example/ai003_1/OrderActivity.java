package com.example.ai003_1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends Activity {
    private List<ShopProduct> datas;
    private OrderListAdapter adapter;
    private ListView orderView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);

        Bundle bundle = getIntent().getExtras();
        String orderId = bundle.getString("Order_ID");

        orderView = findViewById(R.id.orderView);

        // 模擬數據
        datas = new ArrayList<ShopProduct>();
        ShopProduct shopProduct = null;

        for (int i = 1; i <= 10; i++) {
            shopProduct = new ShopProduct();
            shopProduct.setName("商品："+i+":單價:"+i);
            shopProduct.setNum(1);
            shopProduct.setPrice(i);
            datas.add(shopProduct);
        }
        adapter = new OrderListAdapter(datas,this);
        orderView.setAdapter(adapter);
    }
}
