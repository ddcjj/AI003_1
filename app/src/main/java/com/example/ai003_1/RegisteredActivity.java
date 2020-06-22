package com.example.ai003_1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ai003_1.fragments.HomeFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class RegisteredActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private EditText ed_registered_account;
    private EditText ed_registered_password;
    private EditText ed_registered_name;
    private EditText ed_registered_birthday;
    private EditText ed_registered_email;
    private EditText ed_registered_address;
    private EditText ed_registered_phone;
    private EditText ed_registered_wechat;
    private EditText ed_registered_taobao;
    private EditText ed_registered_checkPassword;
    private EditText ed_registered_checkEmail;
    private Button btn_registered_email;
    private Button btn_registered_confirm;
    private Button btn_registered_cancel;
    private String response;
    private String responseMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        InitialComponent();

        btn_registered_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrieveFeedTask mail = new RetrieveFeedTask("http://40.84.151.37/authMail.php",true);
                mail.execute();
            }
        });

        btn_registered_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!ed_registered_password.getText().toString().equals(ed_registered_checkPassword.getText().toString())){
                    new AlertDialog.Builder(RegisteredActivity.this)
                            .setMessage("密碼不相同")
                            .setNegativeButton("確定",null)
                            .show();
                    Log.d(TAG, "password: ");
                    return;
                }

                RetrieveFeedTask task = new RetrieveFeedTask("http://40.84.151.37/addUser.php",false);
                task.execute();

//                if(!responseMail.equals(ed_registered_checkEmail.getText().toString())){
//                    new AlertDialog.Builder(RegisteredActivity.this)
//                            .setMessage("認證碼有誤")
//                            .setNegativeButton("確定",null)
//                            .show();
//                    Log.d(TAG, "mail: ");
////                    return;
//                }

//                if(response.equals("successful")){
//                    Log.d(TAG, "successful: ");
//                    Toast.makeText(RegisteredActivity.this,response,Toast.LENGTH_SHORT).show();
//                }
            }
        });

        btn_registered_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void InitialComponent() {
        ed_registered_account = findViewById(R.id.ed_registered_account);
        ed_registered_name = findViewById(R.id.ed_registered_name);
        ed_registered_birthday = findViewById(R.id.ed_registered_birthday);
        ed_registered_email = findViewById(R.id.ed_registered_email);
        btn_registered_email = findViewById(R.id.btn_registered_email);
        ed_registered_checkEmail = findViewById(R.id.ed_registered_checkEmail);
        ed_registered_password = findViewById(R.id.ed_registered_password);
        ed_registered_checkPassword = findViewById(R.id.ed_registered_checkPassword);
        ed_registered_address = findViewById(R.id.ed_registered_address);
        ed_registered_phone = findViewById(R.id.ed_registered_phone);
        ed_registered_wechat = findViewById(R.id.ed_registered_wechat);
        ed_registered_taobao = findViewById(R.id.ed_registered_taobao);
        btn_registered_confirm = findViewById(R.id.btn_registered_confirm);
        btn_registered_cancel = findViewById(R.id.btn_registered_cancel);
    }
    public void GetText(String urlStr, boolean isMail) throws UnsupportedEncodingException {


        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
//            URL url = new URL("http://140.116.180.101/CustomerInput_app_rec.php");
            URL url = new URL(urlStr);

            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();

            if(isMail){
                jsonParam.put("eMail",ed_registered_email.getText().toString());
            }else {
                jsonParam.put("cAccount", ed_registered_account.getText().toString());
                jsonParam.put("cPassword", ed_registered_password.getText().toString());
                jsonParam.put("cName", ed_registered_name.getText().toString());
                jsonParam.put("Birthday", ed_registered_birthday.getText().toString());
                jsonParam.put("cEmail", ed_registered_email.getText().toString());
//            jsonParam.put("CheckNumber",ed_registered_checkEmail.getText().toString());
                jsonParam.put("cAddress", ed_registered_address.getText().toString());
                jsonParam.put("cPhone", ed_registered_phone.getText().toString());
                jsonParam.put("cWechatID", ed_registered_wechat.getText().toString());
                jsonParam.put("cTaobaoID", ed_registered_taobao.getText().toString());
            }

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            //wr.write(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            wr.write(jsonParam.toString());
            wr.flush();
            Log.d(TAG, "json is " + jsonParam);

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Log.d(TAG,"reder"+reader);
            StringBuilder sb = new StringBuilder();
            String line = null;

//             Read Server Response
            while ((line = reader.readLine()) != null) {

                // Append server response in string
                sb.append(line + "\n");

            }
            Log.d(TAG, "sb is " + sb.toString());
            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d(TAG, "GetText: test");
                JSONObject jsonObjectarray = jsonArray.getJSONObject(i);
                Log.d(TAG, "GetText: test2");
                if(isMail){
//                response = jsonObj.getJSONArray("Captcha").toString();
                    Log.d(TAG, "30678: "+ jsonObjectarray.getInt("Captcha"));
                    responseMail = ""+jsonObjectarray.getInt("Captcha");
                }else {
//                    response = jsonObj.getJSONArray("posts").toString();
                    response = jsonObjectarray.getString("status");
//                    response = jsonObj.getJSONArray("posts").toString();
                }
            }

//            JSONObject jsonObj = new JSONObject(sb.toString());
//            Log.d(TAG, "answers is " + jsonObj.getJSONArray("posts"));
//            Log.d(TAG, "0 is " + jsonObj.getJSONArray("answers").getJSONObject(0));
//            Log.d(TAG, "answer is " + jsonObj.getJSONArray("answers").getJSONObject(0).getString("answer"));
//            response = jsonObj.getJSONArray("answers").getJSONObject(0).getString("posts");
//            response = jsonObj.getJSONArray("posts").toString();
            Log.d(TAG,"response is " + response);
            //txtView.setText(response+"\n");
        } catch (Exception ex) {

            Log.d(TAG, "exception at last " + ex);

        } finally {

            try {

                reader.close();

            } catch (Exception ex) {

            }
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!isMail) {
                if (!responseMail.equals(ed_registered_checkEmail.getText().toString())) {
                    new AlertDialog.Builder(RegisteredActivity.this)
                            .setMessage("認證碼有誤")
                            .setNegativeButton("確定", null)
                            .show();
                    Log.d(TAG, "mail: ");
                    return;
                }

                if (response.equals("successful")) {
                    Log.d(TAG, "successful: ");
                    Toast.makeText(RegisteredActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RegisteredActivity.this, "註冊失敗", Toast.LENGTH_SHORT).show();
                }
            }
        }

        String url;
        boolean isMail;

        public RetrieveFeedTask(String url,boolean isMail) {
        this.url = url;
        this.isMail = isMail;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                Log.d(TAG, "called");
                GetText(url,isMail);
                Log.d(TAG, "after called");

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
                Log.d(TAG, "Exception occurred " + e);

            }

            return null;
        }

    }
}
