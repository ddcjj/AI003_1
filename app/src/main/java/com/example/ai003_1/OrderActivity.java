package com.example.ai003_1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends Activity {
    private List<ShopProduct> datas;
    private OrderListAdapter adapter;
    private ListView orderView;
    private String result; // 儲存資料用的字串
    private int orderId;
    private String userName;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order);

        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getInt("Order_ID");
        userName = bundle.getString(CDictionary.USER_NAME);
        password = bundle.getString(CDictionary.USER_PASSWORD);


        Thread thread = new Thread(mutiThread);
        thread.start(); // 開始執行
        try{
            thread.join();
        }
        catch (InterruptedException e){
            System.out.println(e);
        }

        adapter = new OrderListAdapter(datas,this);
        orderView = findViewById(R.id.orderView);

        orderView.setAdapter(adapter);

/*
        // 模擬數據
        Bundle bundle_pay = getIntent().getExtras();
        ShopProduct shopProduct = null;
        if(bundle_pay != null) {
            ShopProduct getShopProduct = bundle_pay.getParcelable("Parcelable");
            shopProduct = new ShopProduct();
            shopProduct.setName(getShopProduct.getName());
            shopProduct.setNum(getShopProduct.getNum());
            shopProduct.setUnitprice(getShopProduct.getUnitprice());
            shopProduct.setPrice(getShopProduct.getPrice());
            datas.add(shopProduct);
        }

        adapter = new OrderListAdapter(datas,this);
        orderView.setAdapter(adapter);

 */
    }
    private Runnable mutiThread = new Runnable(){
        public void run()
        {
            try {
                URL url = new URL("http://40.84.151.37/getOrderByUser.php");
                // 開始宣告 HTTP 連線需要的物件，這邊通常都是一綑的
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 建立 Google 比較挺的 HttpURLConnection 物件
                connection.setRequestMethod("POST");

                OutputStream os = null;

                // 設定連線方式為 POST
                connection.setDoOutput(true); // 允許輸出
                connection.setDoInput(true); // 允許讀入
                connection.setUseCaches(false); // 不使用快取

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cAccount",userName);
                jsonObject.put("cPassword",password);

                String message = jsonObject.toString();

                connection.setReadTimeout(10000 /*milliseconds*/);
                connection.setConnectTimeout(15000 /* milliseconds */);
                connection.setFixedLengthStreamingMode(message.getBytes().length);

                //setup send
                os = new BufferedOutputStream(connection.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();

                connection.connect(); // 開始連線

                int responseCode = connection.getResponseCode();
                // 建立取得回應的物件
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    // 如果 HTTP 回傳狀態是 OK ，而不是 Error
                    InputStream inputStream = connection.getInputStream();
                    // 取得輸入串流
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream,
                            "utf-8"), 8);
                    // 讀取輸入串流的資料
                    String box = ""; // 宣告存放用字串
                    String line = null; // 宣告讀取用的字串

                    while((line = bufReader.readLine()) != null) {
                        box += line + "\n";
                        // 每當讀取出一列，就加到存放字串後面
                    }
                    inputStream.close(); // 關閉輸入串流
                    result = box; // 把存放用字串放到全域變數

                    datas = new ArrayList<ShopProduct>();
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShopProduct shopProduct = new ShopProduct();
                        JSONObject json_shopProduct = jsonArray.getJSONObject(i);

                        if( orderId == json_shopProduct.getInt("OrderID")) {
                            shopProduct.setName(json_shopProduct.getString("PNAME"));
                            shopProduct.setNum(json_shopProduct.getInt("QTY"));
                            shopProduct.setUnitprice(json_shopProduct.getInt("PPRICE"));
                            shopProduct.setPrice(json_shopProduct.getInt("PPRICE") * json_shopProduct.getInt("QTY"));
                            shopProduct.setImageUrl(getImageBitmap(json_shopProduct.getString("IMAGE")));
                            datas.add(shopProduct);
                        }

                    }

                }
                // 讀取輸入串流並存到字串的部分
                // 取得資料後想用不同的格式
                // 例如 Json 等等，都是在這一段做處理

            } catch(Exception e) {
                result = e.toString(); // 如果出事，回傳錯誤訊息
            }
            System.out.println(result);

        }
    };

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return bm;
    }

}
