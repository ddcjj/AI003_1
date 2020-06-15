package com.example.ai003_1;

import androidx.annotation.NonNull;

public class OrderProduct {
    //訂單編號
    private String id;
    //訂單狀態
    private String state;

    @NonNull
    @Override
    public String toString() {
        return "OrderID{" +
                "Id='" + id + '\'' +
                ", state=" + state +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(String state) { this.state = state; }

    public String getId() { return id; }

    public String getState() { return state; }


}
