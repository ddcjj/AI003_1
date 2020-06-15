package com.example.ai003_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ai003_1.fragments.CartFragment;

import java.util.List;

public class ShopAdapter extends BaseAdapter {
    private List<ShopProduct> shopProducts;
    private Context context;
    private CartFragment onAddNum;
    private CartFragment onSubNum;

    public void setOnAddNum(CartFragment onAddNum){
        this.onAddNum = onAddNum;
    }
    public void setOnSubNum(CartFragment onSubNum) {
        this.onSubNum = onSubNum;
    }
    public ShopAdapter(List<ShopProduct> shopProducts, Context context) {
        this.shopProducts = shopProducts;
        this.context = context;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (shopProducts != null) {
            ret = shopProducts.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int i) {
        return shopProducts.get(i);
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
            v = LayoutInflater.from(context).inflate(R.layout.item_cart,viewGroup,false);
        }

        ViewHolder holder = (ViewHolder) v.getTag();
        if (holder == null) {

            holder = new ViewHolder();
            holder.item_order_id = v.findViewById(R.id.item_product_name);
            holder.item_product_num = v.findViewById(R.id.item_product_num);
            holder.item_product_price = v.findViewById(R.id.item_product_price);
            holder.check_all = v.findViewById(R.id.checkAll);

            holder.item_btn_add = v.findViewById(R.id.item_btn_add);
            holder.item_btn_add.setOnClickListener(onAddNum);

            holder.item_btn_sub = v.findViewById(R.id.item_btn_sub);
            holder.item_btn_sub.setOnClickListener(onSubNum);

        }

        holder.item_order_id.setText(shopProducts.get(i).getName());
        holder.item_product_num.setText(shopProducts.get(i).getNum()+"");
        holder.item_product_price.setText(shopProducts.get(i).getPrice() + "");
/*
        holder.check_all.setTag(i);
        holder.check_all.setChecked(mCheckStates.get(i, false));
        holder.check_all.setOnCheckedChangeListener(this);

 */
        holder.item_btn_add.setTag(i);
        holder.item_btn_sub.setTag(i);

        v.setTag(holder);
        return v;
    }


    private static class ViewHolder{
        private TextView item_order_id;
        private TextView item_product_num;
        private TextView item_product_price;
        private CheckBox check_all;
        private ImageButton item_btn_add;
        private ImageButton item_btn_sub;
    }

}
