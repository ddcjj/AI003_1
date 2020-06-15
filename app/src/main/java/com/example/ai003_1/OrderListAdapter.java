package com.example.ai003_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderListAdapter extends BaseAdapter {
    private List<ShopProduct> orderListProduct;
    private Context context;

    public OrderListAdapter(List<ShopProduct> orderListProducts, Context context) {
        this.orderListProduct = orderListProducts;
        this.context = context;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (orderListProduct != null) {
            ret = orderListProduct.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int i) {
        return orderListProduct.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = null;
        if (view != null) {
            v = view;
        }
        else {
            v = LayoutInflater.from(context).inflate(R.layout.item_orderlist,viewGroup,false);
        }

        OrderListAdapter.ViewHolder holder = (OrderListAdapter.ViewHolder) v.getTag();
        if (holder == null) {

            holder = new OrderListAdapter.ViewHolder();
            holder.item_order_id = v.findViewById(R.id.item_product_name);
            holder.item_product_num = v.findViewById(R.id.item_product_num);
            holder.item_product_price = v.findViewById(R.id.item_product_price);

        }

        holder.item_order_id.setText(orderListProduct.get(i).getName());
        holder.item_product_num.setText(orderListProduct.get(i).getNum()+"");
        holder.item_product_price.setText(orderListProduct.get(i).getPrice() + "");

        v.setTag(holder);
        return v;
    }


    private static class ViewHolder{
        private TextView item_order_id;
        private TextView item_product_num;
        private TextView item_product_price;
    }

}