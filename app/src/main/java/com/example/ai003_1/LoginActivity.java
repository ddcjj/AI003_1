package com.example.ai003_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText ed_id;
    EditText ed_password;
    String id;
    String password;
    Button btn_login;
    Button btn_cancel;
    private Button btn_registered;
    private Button btn_visitor;
    private CheckBox cb_autoLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitialComponent();
        Log.i("testMain","123");

    }

    private void InitialComponent(){
        ed_id = findViewById(R.id.ed_id);
        id = ed_id.getText().toString();
        ed_password = findViewById(R.id.ed_password);
        password = ed_password.getText().toString();

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(btn_login_click);
        //btn_cancel = findViewById(R.id.btn_cancel);
        //btn_cancel.setOnClickListener(btn_cancel_click);
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

            String id = ed_id.getText().toString();
            String password = ed_password.getText().toString();

            if(id.equals("jack") && password.equals("1234")){
                String userName = "jack";

                Bundle bundle = new Bundle();
                bundle.putString("name",userName);
                Intent intent = new Intent();
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
                            .putString(CDictionary.ID,id)
                            .apply();
                }

                setResult(CDictionary.RESULT_LOGIN,intent);
                finish();
            }else{
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("登入失敗")
                        .setMessage("帳號密碼錯誤")
                        .setPositiveButton("確定",null)
                        .show();
            }
        }
    };

//    private View.OnClickListener btn_cancel_click = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            finish();
//        }
//    };

    private View.OnClickListener btn_visitor_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            setResult(CDictionary.RESULT_LOGIN_VISITER,intent);
            finish();
        }
    };
    private View.OnClickListener btn_registered_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this,RegisteredActivity.class);
            startActivity(intent);
        }
    };
}
