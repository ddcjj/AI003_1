package com.example.ai003_1;

import android.graphics.Bitmap;


public class ShopProduct{
    // CustomerID
    private String CID;
    //OrderID
    private String orderID;
    //商品名稱
    private String name;
    // 商品數量
    private int num;
    //商品單價
    private int unitprice;
    //商品單價(NT)
    private int unitprice_NT;
    // 該商品總價
    private int price;
    //商品總價(NT)
    private int price_NT;
    // 該商品圖片
    private Bitmap imageUrl;



    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", num=" + num +
                ", unitprice=" + unitprice +
                ", price=" + price +
                '}';
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setUnitprice(int unitprice) {
        this.unitprice = unitprice;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCID(String CID) { this.CID = CID; }

    public void setOrderID(String orderID) { this.orderID = orderID; }

    public void setImageUrl(Bitmap imageUrl) { this.imageUrl = imageUrl; }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    public int getUnitprice() {
        return unitprice;
    }

    public int getPrice() {
        return price;
    }

    public String getCID() { return CID; }

    public String getOrderID() { return orderID; }

    public Bitmap getImageUrl() { return imageUrl; }

}
