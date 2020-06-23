package com.example.ai003_1.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class OrderFragment extends Fragment {

    private ListView orderView;
    private List<OrderProduct> datas;
    private OrderAdapter adapter;
    private String result; // 儲存資料用的字串
    private final AdapterView.OnItemClickListener orderView_click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle = new Bundle();
            bundle.putInt("Order_ID",Integer.parseInt(datas.get(position).getId()));
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

        Thread thread = new Thread(mutiThread);
        thread.start(); // 開始執行
        try{
            thread.join();
        }
        catch (InterruptedException e){
            System.out.println(e);
        }


        adapter = new OrderAdapter(datas,getContext());
        orderView.setAdapter(adapter);

    }

    private Runnable mutiThread = new Runnable(){
        public void run()
        {
            try {
                URL url = new URL("http://40.84.151.37/getShipByUser.php");
                // 開始宣告 HTTP 連線需要的物件，這邊通常都是一綑的
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 建立 Google 比較挺的 HttpURLConnection 物件
                connection.setRequestMethod("POST");
                // 設定連線方式為 POST
                connection.setDoOutput(true); // 允許輸出
                connection.setDoInput(true); // 允許讀入
                connection.setUseCaches(false); // 不使用快取

                OutputStream os = null;

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cAccount","0001");
                jsonObject.put("cPassword","1234");

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

                    datas = new ArrayList<OrderProduct>();
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json_order = jsonArray.getJSONObject(i);
                        if(json_order.getString("CCHECK").equals("Y")) {
                            OrderProduct orderProduct = new OrderProduct();
                            orderProduct.setId(json_order.getString("OrderID"));

                            if(json_order.getString("SCHECK").equals("Y")){
                                orderProduct.setState("已發貨");
                            }
                            else {
                                orderProduct.setState("備貨中");
                            }


                            datas.add(orderProduct);
                        }
                    }

                }
                // 讀取輸入串流並存到字串的部分
                // 取得資料後想用不同的格式
                // 例如 Json 等等，都是在這一段做處理

            } catch(Exception e) {
                result = e.toString(); // 如果出事，回傳錯誤訊息
            }

        }
    };
}
