package com.example.ai003_1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private EditText ed_id;
    private EditText ed_password;
    private String userName;
    private String password;
    private Button btn_login;
    private Button btn_registered;
    private Button btn_visitor;
    private CheckBox cb_autoLogin;
    private String response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitialComponent();
    }

    private void InitialComponent(){
        ed_id = findViewById(R.id.ed_id);
        userName = ed_id.getText().toString();
        ed_password = findViewById(R.id.ed_password);
        password = ed_password.getText().toString();

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(btn_login_click);
        btn_visitor = findViewById(R.id.btn_visitor);
        btn_visitor.setOnClickListener(btn_visitor_click);
        btn_registered = findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(btn_registered_click);

        cb_autoLogin = findViewById(R.id.cb_autoLogin);
        cb_autoLogin.setChecked(getSharedPreferences(CDictionary.LOGIN,MODE_PRIVATE)
                    .getBoolean(CDictionary.AUTO_LOGIN,false));
        cb_autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getSharedPreferences(CDictionary.LOGIN,MODE_PRIVATE)
                        .edit()
                        .putBoolean(CDictionary.AUTO_LOGIN,isChecked)
                        .apply();
            }
        });
    }

    private View.OnClickListener btn_login_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            userName = ed_id.getText().toString();
            password = ed_password.getText().toString();

            RetrieveFeedTask task = new RetrieveFeedTask();
            task.execute();
        }
    };

    private View.OnClickListener btn_visitor_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(LoginActivity.this,MainActivity.class),CDictionary.REQUEST_LOGIN_VISITOR);
//            Intent intent = new Intent();
//            setResult(CDictionary.RESULT_LOGIN_VISITER,intent);
//            finish();
        }
    };
    private View.OnClickListener btn_registered_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this,RegisteredActivity.class);
            startActivity(intent);
        }
    };
    public void GetText() throws UnsupportedEncodingException {


        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
//            URL url = new URL("http://140.116.180.101/checkUser.php");
            URL url = new URL("http://40.84.151.37/checkUser.php");

            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("cAccount", userName);
            jsonParam.put("cPassword", password);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(jsonParam.toString());
            wr.flush();
//            Log.d(TAG, "json is " + jsonParam);

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            Log.d(TAG,"reder"+reader);
            StringBuilder sb = new StringBuilder();
            String line = null;

//             Read Server Response
            while ((line = reader.readLine()) != null) {

                // Append server response in string
                sb.append(line + "\n");

            }
//            Log.d(TAG, "sb is " + sb.toString());
            JSONObject jsonObj = new JSONObject(sb.toString());
//            Log.d(TAG, "answers is " + jsonObj.getJSONArray("posts"));
            response = jsonObj.getJSONArray("posts").toString();
//            Log.d(TAG, response);
//            Log.d(TAG, "boolean: " + response.contains("成功"));
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

            if(response.contains("成功")){

                Bundle bundle = new Bundle();
                bundle.putString(CDictionary.USER_NAME, userName);
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtras(bundle);
//                intent.putExtra("name",userName);

                //自動登入儲存帳號密碼
                Boolean remember = getSharedPreferences(CDictionary.LOGIN,MODE_PRIVATE)
                        .getBoolean(CDictionary.AUTO_LOGIN,false);
                if(remember){
                    getSharedPreferences(CDictionary.LOGIN,MODE_PRIVATE)
                            .edit()
                            .putBoolean(CDictionary.LOGON,true)
                            .commit();

                    getSharedPreferences(CDictionary.LOGIN,MODE_PRIVATE)
                            .edit()
                            .putString(CDictionary.ID, userName)
                            .apply();
                }
                Log.d(TAG, "Login name: " + userName);
                startActivity(intent);
//                startActivityForResult(intent,CDictionary.REQUEST_LOGIN);
//                setResult(CDictionary.RESULT_LOGIN,intent);
//                finish();
            }else{
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("登入失敗")
                        .setMessage("帳號密碼錯誤")
                        .setPositiveButton("確定",null)
                        .show();
            }
        }

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
