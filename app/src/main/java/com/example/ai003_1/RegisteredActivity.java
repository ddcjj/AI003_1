package com.example.ai003_1;

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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class RegisteredActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        InitialComponent();

        btn_registered_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    return;
                }
                RetrieveFeedTask task = new RetrieveFeedTask();
                task.execute();

                Toast.makeText(RegisteredActivity.this,response,Toast.LENGTH_SHORT).show();

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
    public void GetText() throws UnsupportedEncodingException {


        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
//            URL url = new URL("http://140.116.180.101/CustomerInput_app_rec.php");
            URL url = new URL("http://40.84.151.37/CustomerInput_app_rec.php");

            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("cAccount", ed_registered_account.getText().toString());
            jsonParam.put("cName",ed_registered_name.getText().toString());
            jsonParam.put("Birthday",ed_registered_birthday.getText().toString());
            jsonParam.put("cEmail",ed_registered_email.getText().toString());
//            jsonParam.put("CheckNumber",ed_registered_checkEmail.getText().toString());
            jsonParam.put("cPassword",ed_registered_password.getText().toString());
            jsonParam.put("recPassword",ed_registered_checkPassword.getText().toString());
            jsonParam.put("cAddress",ed_registered_address.getText().toString());
            jsonParam.put("cPhone",ed_registered_phone.getText().toString());
            jsonParam.put("cWechatID",ed_registered_wechat.getText().toString());
            jsonParam.put("cTaobaoID",ed_registered_taobao.getText().toString());

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
