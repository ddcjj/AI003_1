package com.example.ai003_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ai003_1.fragments.AccountFragment;
import com.example.ai003_1.fragments.CartFragment;
import com.example.ai003_1.fragments.HomeFragment;
import com.example.ai003_1.fragments.ListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private boolean logon;

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logon = getSharedPreferences(CDictionary.LOGIN,MODE_PRIVATE)
                .getBoolean(CDictionary.LOGON,false);

        if(!logon) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, CDictionary.REQUEST_LOGIN);
        }

        bottomNavigation = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=CDictionary.RESULT_LOGIN && resultCode!=CDictionary.RESULT_LOGIN_VISITER){
            finish();
        }
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment fragment=null;
                    switch (item.getItemId()){
                        case R.id.navigation_home:
                            fragment=new HomeFragment();
                            break;
                        case R.id.navigation_cart:
                            fragment=new CartFragment();
                            break;
                        case R.id.navigation_list:
                            fragment=new ListFragment();
                            break;
                        case R.id.navigation_account:
                            fragment=new AccountFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();


                    return true;
                }
            };
}
