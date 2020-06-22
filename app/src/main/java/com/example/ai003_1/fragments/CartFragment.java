package com.example.ai003_1.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ai003_1.R;
import com.example.ai003_1.ShopAdapter;
import com.example.ai003_1.ShopProduct;

import org.json.JSONArray;
import org.json.JSONException;
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

public class CartFragment extends Fragment implements View.OnClickListener {

    private List<ShopProduct> datas;
    private ShopAdapter adapter;
    private ListView listView;
    private List<Boolean> listShow;    // 這個用來記錄哪幾個 item 是被打勾的
    private TextView check_total_sum;
    private TextView check_total_sum_RM;
    private CheckedTextView check_select;
    private Button btn_delete;
    private Button btn_pay;
    private String result; // 儲存資料用的字串
    private String result_member;
    int total_sum = 0;
    private final View.OnClickListener btn_pay_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog();
        }

    };

    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); //創建訊息方塊

        builder.setMessage("確定要購買？");

        builder.setTitle("購買");

        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss(); //dismiss為關閉dialog,Activity還會保留dialog的狀態

                Thread thread_pay = new Thread(mutiThread_pay);
                Thread thread_sendEmail = new Thread(mutiThread_sendEmail);
                thread_pay.start(); // 開始執行
                thread_sendEmail.start();
                try {
                    thread_pay.join();
                    thread_sendEmail.join();
                }
                catch (Exception e){
                    System.out.println(e);
                }

                check_total_sum.setText(Integer.toString(total_sum));
                check_total_sum_RM.setText(Integer.toString((int) Math.ceil(total_sum/4.05)));
                adapter.notifyDataSetChanged();
            }

        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }

        });

        builder.create().show();

    }

    private View.OnClickListener btn_delete_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int count = listView.getCount();
            int total_sub = 0;
            if (count == 0) {

            } else {
                for (int item = count - 1; item >= 0; item--) {
                    if (listShow.get(item)) {
                        total_sub += datas.get(item).getPrice();
                        datas.remove(item);
                        listShow.remove(item);
                    }
                }
                total_sum -= total_sub;
                check_total_sum.setText(Integer.toString(total_sum));
                check_total_sum_RM.setText(Integer.toString((int) Math.ceil(total_sum/4.05)));
                adapter.notifyDataSetChanged();
            }


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
        btn_delete.setOnClickListener(btn_delete_click);
        btn_pay = view.findViewById(R.id.pay);
        btn_pay.setOnClickListener(btn_pay_click);
        check_total_sum = view.findViewById(R.id.check_total_sum);
        check_total_sum_RM = view.findViewById(R.id.check_total_sum_RM);
        listView = view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView chkItem = view.findViewById(R.id.check_select);
                chkItem.setChecked(!chkItem.isChecked());
                listShow.set(position, chkItem.isChecked());
                if (chkItem.isChecked()) {
                    total_sum += datas.get(position).getPrice();
                } else {
                    total_sum -= datas.get(position).getPrice();
                }
                check_total_sum.setText(Integer.toString(total_sum));
                check_total_sum_RM.setText(Integer.toString((int) Math.ceil(total_sum/4.05)));
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Thread thread = new Thread(mutiThread);
        thread.start(); // 開始執行
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        adapter = new ShopAdapter(datas, getContext());
        check_total_sum.setText(Integer.toString(total_sum));
        check_total_sum_RM.setText(Integer.toString((int) Math.ceil(total_sum/4.05)));
        listView.setAdapter(adapter);
        adapter.setOnAddNum(this);
        adapter.setOnSubNum(this);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        int total_sum = Integer.parseInt((String) check_total_sum.getText());
        switch (view.getId()) {
            case R.id.item_btn_add: //點擊添加數量按鈕，執行相應的處理
                // 獲取 Adapter 中設置的 Tag
                if (tag != null && tag instanceof Integer) { //解決問題：如何知道你點擊的按鈕是哪一個列表項中的，通過Tag的position
                    int position = (Integer) tag;
                    //更改集合的數據
                    int num = datas.get(position).getNum();
                    int unitprice = datas.get(position).getUnitprice();
                    num++;
                    datas.get(position).setNum(num); //修改集合中商品數量
                    datas.get(position).setPrice(unitprice * num); //修改集合中該商品總價 數量*單價
                    total_sum += datas.get(position).getUnitprice();
                    check_total_sum.setText(Integer.toString(total_sum));
                    check_total_sum_RM.setText(Integer.toString((int) Math.ceil(total_sum/4.05)));
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
                    int unitprice = datas.get(position).getUnitprice();
                    if (num > 0) {
                        num--;
                        datas.get(position).setNum(num); //修改集合中商品數量
                        datas.get(position).setPrice(unitprice * num); //修改集合中該商品總價 數量*單價
                        total_sum -= datas.get(position).getUnitprice();
                        check_total_sum.setText(Integer.toString(total_sum));
                        check_total_sum_RM.setText(Integer.toString((int) Math.ceil(total_sum/4.05)));
                        if (num == 0)
                            datas.get(position).setPrice(0);
                        adapter.notifyDataSetChanged();
                    }
                }

                break;
        }
    }

    // 建立一個執行緒執行的事件取得網路資料
    // Android 有規定，連線網際網路的動作都不能再主線程做執行
    // 畢竟如果使用者連上網路結果等太久整個系統流程就卡死了
    private Runnable mutiThread = new Runnable() {
        public void run() {
            try {
                URL url_order = new URL("http://40.84.151.37/getOrderByUser.php");
                //URL url_member = new URL("http://10.0.2.2/project/memberManage_j.php");
                // 開始宣告 HTTP 連線需要的物件，這邊通常都是一綑的
                HttpURLConnection connection = (HttpURLConnection) url_order.openConnection();
                //HttpURLConnection connection_member = (HttpURLConnection) url_member.openConnection();
                // 建立 Google 比較挺的 HttpURLConnection 物件
                connection.setRequestMethod("POST");
                //connection_member.setRequestMethod("POST");
                // 設定連線方式為 POST

                OutputStream os = null;

                connection.setDoOutput(true); // 允許輸出
                connection.setDoInput(true); // 允許讀入
                connection.setUseCaches(false); // 不使用快取

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
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 如果 HTTP 回傳狀態是 OK ，而不是 Error
                    InputStream inputStream = connection.getInputStream();

                    // 取得輸入串流
                    BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream,
                            "utf-8"), 8);

                    // 讀取輸入串流的資料
                    String box = ""; // 宣告存放用字串
                    String line = null; // 宣告讀取用的字串


                    while ((line = bufReader.readLine()) != null) {
                        box += line + "\n";
                        // 每當讀取出一列，就加到存放字串後面
                    }

                    inputStream.close(); // 關閉輸入串流
                    result = box; // 把存放用字串放到全域變數


                    datas = new ArrayList<ShopProduct>();
                    JSONArray jsonArray = new JSONArray(result);
                    listShow = new ArrayList<Boolean>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShopProduct shopProduct = new ShopProduct();
                        JSONObject json_shopProduct = jsonArray.getJSONObject(i);
                        shopProduct.setName(json_shopProduct.getString("PNAME"));
                        shopProduct.setNum(json_shopProduct.getInt("QTY"));
                        shopProduct.setUnitprice(json_shopProduct.getInt("PPRICE"));
                        shopProduct.setPrice(json_shopProduct.getInt("PPRICE") * json_shopProduct.getInt("QTY"));
                        shopProduct.setCID(json_shopProduct.getString("CID"));
                        shopProduct.setOrderID(json_shopProduct.getString("OrderID"));
                        shopProduct.setImageUrl(getImageBitmap(json_shopProduct.getString("IMAGE")));


                        datas.add(shopProduct);
                        listShow.add(true);
                        total_sum += shopProduct.getPrice();
                    }

                }
                // 讀取輸入串流並存到字串的部分
                // 取得資料後想用不同的格式
                // 例如 Json 等等，都是在這一段做處理

            } catch (Exception e) {
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

    private Runnable mutiThread_pay = new Runnable() {
        @Override
        public void run() {
            OutputStream os = null;
            InputStream is = null;
            HttpURLConnection conn = null;
            try {
                //constants
                URL url = new URL("http://40.84.151.37/verifyOrderByUser.php");
                JSONObject jsonObject = new JSONObject();
                int count = listView.getCount();
                //給OrderID QTY
                for (int i = count - 1; i >= 0; i--) {
                    if (listShow.get(i)) {
                        try {
                            jsonObject.put("OrderID", datas.get(i).getOrderID());
                            jsonObject.put("QTY", datas.get(i).getNum());
                            jsonObject.put("CCHECK", "Y");
                            total_sum -= datas.get(i).getPrice();


                            String message = jsonObject.toString();

                            conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(10000 /*milliseconds*/);
                            conn.setConnectTimeout(15000 /* milliseconds */);
                            conn.setRequestMethod("POST");
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setFixedLengthStreamingMode(message.getBytes().length);

                            //make some HTTP header nicety
                            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                            //open
                            conn.connect();

                            //setup send
                            os = new BufferedOutputStream(conn.getOutputStream());
                            os.write(message.getBytes());
                            //clean up
                            os.flush();

                            //do somehting with response
                            is = conn.getInputStream();
                            //String contentAsString = readIt(is,len);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        datas.remove(i);
                        listShow.remove(i);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                //clean up
                try {
                    os.close();
                    is.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                conn.disconnect();
            }
        }
    };
    private Runnable mutiThread_sendEmail = new Runnable(){
        public void run()
        {
            try {
                URL url = new URL("http://40.84.151.37/confirmedMail.php");
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
                jsonObject.put("CID",datas.get(0).getCID());
                jsonObject.put("eMail","aw123456e@gmail.com");
                jsonObject.put("TotalCash",check_total_sum.getText());

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
            } catch(Exception e) {
                result = e.toString(); // 如果出事，回傳錯誤訊息
            }
            System.out.println(result);
        }
    };
}