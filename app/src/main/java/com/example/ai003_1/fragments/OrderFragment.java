package com.example.ai003_1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ai003_1.OrderActivity;
import com.example.ai003_1.OrderAdapter;
import com.example.ai003_1.OrderProduct;
import com.example.ai003_1.R;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private ListView orderView;
    private List<OrderProduct> datas;
    private OrderAdapter adapter;
    private final AdapterView.OnItemClickListener orderView_click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            bundle.putString("Order_ID",datas.get(position).getId());
            Intent intent = new Intent(getContext(), OrderActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_order, container, false);

        orderView = view.findViewById(R.id.orderView);
        orderView.setOnItemClickListener(orderView_click);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 模擬數據
        datas = new ArrayList<OrderProduct>();
        OrderProduct orderProduct = null;

        for (int i = 1; i <= 10; i++) {
            orderProduct = new OrderProduct();
            orderProduct.setId("訂單編號"+i);
            orderProduct.setState("準備中");
            datas.add(orderProduct);
        }

        adapter = new OrderAdapter(datas,getContext());
        orderView.setAdapter(adapter);

    }
}
