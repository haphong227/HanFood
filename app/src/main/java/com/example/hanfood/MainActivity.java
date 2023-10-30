package com.example.hanfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.hanfood.fragment.FragmentCart;
import com.example.hanfood.fragment.FragmentHome;
import com.example.hanfood.fragment.FragmentUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvTitle;
    public static String emailuser = "";
    public static int cart_count = 0;

    private ChipNavigationBar nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        tvTitle.setText("Trang chủ");
        Intent intent = getIntent();
        emailuser = intent.getStringExtra("email");

        nav.setItemSelected(R.id.mFood, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FragmentHome()).commit();
        nav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.mFood:
                        toolbar.setVisibility(View.VISIBLE);
                        tvTitle.setText("Trang chủ");
                        fragment = new FragmentHome();
                        break;
                    case R.id.mCart:
                        toolbar.setVisibility(View.VISIBLE);
                        tvTitle.setText("Giỏ hàng");
                        fragment = new FragmentCart();
                        break;
//                    case R.id.mNotification:
//                        toolbar.setVisibility(View.VISIBLE);
//                        tvTitle.setText("Notification");
//                        fragment = new FragmentNotification();
//                        break;
                    case R.id.mUser:
                        toolbar.setVisibility(View.GONE);
                        fragment = new FragmentUser();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolBar);
        nav = findViewById(R.id.nav);
        tvTitle = findViewById(R.id.tvTitle);
    }
}