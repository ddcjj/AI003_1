package com.example.ai003_1;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private List<OrderProduct> orderProducts;
    private Context context;

    public OrderAdapter(List<OrderProduct> orderProducts, Context context) {
        this.orderProducts = orderProducts;
        this.context = context;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (orderProducts != null) {
            ret = orderProducts.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return orderProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = null;
        if (view != null) {
            v = view;
        }
        else {
            v = LayoutInflater.from(context).inflate(R.layout.item_order,parent,false);
        }

        OrderAdapter.ViewHolder holder = (OrderAdapter.ViewHolder) v.getTag();
        if (holder == null) {

            holder = new OrderAdapter.ViewHolder();
            holder.item_order_id = v.findViewById(R.id.item_order_id);
            holder.item_order_state = v.findViewById(R.id.item_order_state);

        }

        holder.item_order_id.setText(orderProducts.get(position).getId());
        holder.item_order_state.setText(orderProducts.get(position).getState());

        v.setTag(holder);
        return v;
    }

    private static class ViewHolder{
        public TextView item_order_state;
        private TextView item_order_id;
    }
}
