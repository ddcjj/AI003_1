package com.example.ai003_1;

public class ShopProduct {
    //商品名稱
    private String name;
    // 商品數量
    private int num;
    // 該商品總價
    private int price;

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", num=" + num +
                ", price=" + price +
                '}';
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    public int getPrice() {
        return price;
    }

}
