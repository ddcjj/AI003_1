package com.example.ai003_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class AccountActivity extends AppCompatActivity {

    private TextView text_account_account;
    private EditText ed_account_password;
    private EditText ed_account_name;
    private EditText ed_account_birthday;
    private EditText ed_account_email;
    private EditText ed_account_address;
    private EditText ed_account_phone;
    private EditText ed_account_wechat;
    private EditText ed_account_taobao;
    private EditText ed_account_checkPassword;
    private Button btn_account_confirm;
    private Button btn_account_cancel;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        InitialComponent();


        btn_account_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ed_account_password.getText().toString().equals(ed_account_checkPassword.getText().toString())){
                    new AlertDialog.Builder(AccountActivity.this)
                            .setMessage("密碼不相同")
                            .setNegativeButton("確定",null)
                            .show();
                    return;
                }
                RetrieveFeedTask task = new RetrieveFeedTask();
                task.execute();

                Toast.makeText(AccountActivity.this,response,Toast.LENGTH_SHORT).show();
            }
        });

        btn_account_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void InitialComponent() {
        text_account_account = findViewById(R.id.text_account_account);
        text_account_account.setText(getSharedPreferences(CDictionary.LOGIN,MODE_PRIVATE)
                            .getString(CDictionary.ID,""));
        ed_account_name = findViewById(R.id.ed_account_name);
        ed_account_birthday = findViewById(R.id.ed_account_birthday);
        ed_account_email = findViewById(R.id.ed_account_email);
        ed_account_password = findViewById(R.id.ed_account_password);
        ed_account_checkPassword = findViewById(R.id.ed_account_checkPassword);
        ed_account_address = findViewById(R.id.ed_account_address);
        ed_account_phone = findViewById(R.id.ed_account_phone);
        ed_account_wechat = findViewById(R.id.ed_account_wechat);
        ed_account_taobao = findViewById(R.id.ed_account_taobao);
        btn_account_confirm = findViewById(R.id.btn_account_confirm);

        btn_account_cancel = findViewById(R.id.btn_account_cancel);
    }

    public void GetText() throws UnsupportedEncodingException {


        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("http://140.116.180.101/CustomerInput_app_rec.php");

            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
//            jsonParam.put("cAccount", ed_account_account.getText().toString());
            jsonParam.put("cName",ed_account_name.getText().toString());
            jsonParam.put("Birthday",ed_account_birthday.getText().toString());
            jsonParam.put("cEmail",ed_account_email.getText().toString());
            jsonParam.put("cPassword",ed_account_password.getText().toString());
            jsonParam.put("recPassword",ed_account_checkPassword.getText().toString());
            jsonParam.put("cAddress",ed_account_address.getText().toString());
            jsonParam.put("cPhone",ed_account_phone.getText().toString());
            jsonParam.put("cWechatID",ed_account_wechat.getText().toString());
            jsonParam.put("cTaobaoID",ed_account_taobao.getText().toString());

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            //wr.write(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            wr.write(jsonParam.toString());
            wr.flush();
            Log.d("xiang", "json is " + jsonParam);

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Log.d("xiang","reder"+reader);
            StringBuilder sb = new StringBuilder();
            String line = null;

//             Read Server Response
            while ((line = reader.readLine()) != null) {

                // Append server response in string
                sb.append(line + "\n");

            }
            Log.d("xiang", "sb is " + sb.toString());
            JSONObject jsonObj = new JSONObject(sb.toString());
//            Log.d("xiang", "answers is " + jsonObj.getJSONArray("posts"));
//            Log.d("xiang", "0 is " + jsonObj.getJSONArray("answers").getJSONObject(0));
//            Log.d("xiang", "answer is " + jsonObj.getJSONArray("answers").getJSONObject(0).getString("answer"));
//            response = jsonObj.getJSONArray("answers").getJSONObject(0).getString("posts");
            response = jsonObj.getJSONArray("posts").toString();
            Log.d("xiang",response);
            //txtView.setText(response+"\n");


        } catch (Exception ex) {

            Log.d("xiang", "exception at last " + ex);

        } finally {

            try {

                reader.close();

            } catch (Exception ex) {

            }
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                Log.d("xiang", "called");
                GetText();
                Log.d("xiang", "after called");

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
                Log.d("xiang", "Exception occurred " + e);

            }

            return null;
        }

    }
}
