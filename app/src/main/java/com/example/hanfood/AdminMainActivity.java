package com.example.hanfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.hanfood.fragment.admin.FragmentCategory;
import com.example.hanfood.fragment.admin.FragmentOrder;
import com.example.hanfood.fragment.admin.FragmentProfile;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class AdminMainActivity extends AppCompatActivity implements ChipNavigationBar.OnItemSelectedListener{
    Toolbar toolbar;
    TextView tvTitle;
    public static String emailuser = "";

    private ChipNavigationBar nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        initView();

        tvTitle.setText("Danh mục");
        Intent intent = getIntent();
        emailuser = intent.getStringExtra("email");

        nav.setItemSelected(R.id.mCategory, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new FragmentCategory()).commit();

        nav.setOnItemSelectedListener(this);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolBar);
        nav = findViewById(R.id.nav);
        tvTitle = findViewById(R.id.tvTitle);
    }

    @Override
    public void onItemSelected(int i) {
        Fragment fragment = null;
        switch (i) {
            case R.id.mCategory:
                toolbar.setVisibility(View.VISIBLE);
                tvTitle.setText("Danh mục");
                fragment = new FragmentCategory();
                break;
            case R.id.mFood:
                toolbar.setVisibility(View.VISIBLE);
                tvTitle.setText("Đơn hàng");
                fragment = new FragmentOrder();
                break;
//                    case R.id.mNotification:
//                        toolbar.setVisibility(View.VISIBLE);
//                        tvTitle.setText("Notification");
//                        fragment = new FragmentNotification();
//                        break;
            case R.id.mProfile:
                toolbar.setVisibility(View.GONE);
                fragment = new FragmentProfile();
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).commit();
    }
}