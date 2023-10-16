package com.example.hanfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.hanfood.adapter.TabLayoutAdapter;
import com.example.hanfood.fragment.FragmentLogin;
import com.example.hanfood.fragment.FragmentRegister;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    TabLayout tableLayout;

    private TabLayoutAdapter adapter;
    public static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        adapter = new TabLayoutAdapter(this.getSupportFragmentManager());
        adapter.addFragment(new FragmentLogin(), "Login");
        adapter.addFragment(new FragmentRegister(), "Register");
        viewPager.setAdapter(adapter);
        tableLayout.setupWithViewPager(viewPager);

        getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.blue));

    }

    private void initView() {
        tableLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }
}