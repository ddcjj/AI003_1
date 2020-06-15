package com.example.ai003_1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ai003_1.PayActivity;
import com.example.ai003_1.R;
import com.example.ai003_1.ShopAdapter;
import com.example.ai003_1.ShopProduct;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements View.OnClickListener{

    private List<ShopProduct> datas;
    private ShopAdapter adapter;
    private ListView listView;
    private List<Boolean> listShow;    // 這個用來記錄哪幾個 item 是被打勾的
    private TextView check_total_sum;
    private Button btn_delete;
    private Button btn_pay;
    private final View.OnClickListener btn_pay_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getContext(), PayActivity.class);
            startActivity(intent);
        }
    };

    public CartFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_cart, container, false);
        final View v = inflater.inflate(R.layout.item_cart, container, false);
        btn_delete = view.findViewById(R.id.delete);
        //btn_delete.setOnClickListener(btn_delete_click);
        btn_pay = view.findViewById(R.id.pay);
        btn_pay.setOnClickListener(btn_pay_click);
        check_total_sum = view.findViewById(R.id.check_total_sum);
        listView = view.findViewById(R.id.listView);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 模擬數據
        datas = new ArrayList<ShopProduct>();
        ShopProduct shopProduct = null;
        int total_sum = 0;

        for (int i = 0; i < 10; i++) {
            shopProduct = new ShopProduct();
            shopProduct.setName("商品："+i+":單價:"+i);
            shopProduct.setNum(1);
            shopProduct.setPrice(i);
            datas.add(shopProduct);
            total_sum += i;
        }
        adapter = new ShopAdapter(datas,getContext());
        check_total_sum.setText(Integer.toString(total_sum));
        listView.setAdapter(adapter);
        adapter.setOnAddNum(this);
        adapter.setOnSubNum(this);

    }

    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        switch (view.getId()){
            case R.id.item_btn_add: //點擊添加數量按鈕，執行相應的處理
                // 獲取 Adapter 中設置的 Tag
                if (tag != null && tag instanceof Integer) { //解決問題：如何知道你點擊的按鈕是哪一個列表項中的，通過Tag的position
                    int position = (Integer) tag;
                    //更改集合的數據
                    int num = datas.get(position).getNum();
                    num++;
                    datas.get(position).setNum(num); //修改集合中商品數量
                    datas.get(position).setPrice(position*num); //修改集合中該商品總價 數量*單價
                    //解決問題：點擊某個按鈕的時候，如果列表項所需的數據改變了，如何更新UI
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.item_btn_sub: //點擊減少數量按鈕 ，執行相應的處理
                // 獲取 Adapter 中設置的 Tag
                if (tag != null && tag instanceof Integer) {
                    int position = (Integer) tag;
                    //更改集合的數據
                    int num = datas.get(position).getNum();
                    if (num>0) {
                        num--;
                        datas.get(position).setNum(num); //修改集合中商品數量
                        datas.get(position).setPrice(position * num); //修改集合中該商品總價 數量*單價
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }
}
