package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NewAddressActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText eName, ePhone, eAddress;
    Button btAdd;
    String TAG = "Address", randomKey = "";
    String name, phone, addr;
    FirebaseUser auth;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewAddressActivity.this, AddressActivity.class));
                finish();
            }
        });

        eAddress.setOnClickListener(this);
        btAdd.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == eAddress) {
            startActivity(new Intent(NewAddressActivity.this, MapsActivity.class));
        }
        if (view == btAdd) {
            newAddress();
        }
    }

    private void newAddress() {
        String saveCurrentTime, savecurrentDate;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat curDate = new SimpleDateFormat("yyyy-MM-dd");
        savecurrentDate = curDate.format(c.getTime());
        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = curTime.format(c.getTime());
        randomKey = savecurrentDate + "-" + saveCurrentTime;

        name = eName.getText().toString();
        phone = ePhone.getText().toString();
        addr = eAddress.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || addr.isEmpty()) {
            Toast.makeText(NewAddressActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        } else if (phone.length() < 10 || phone.length() > 12) {
            Toast.makeText(NewAddressActivity.this, "Vui lòng nhập đúng định dạng số điện thoại!", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> address = new HashMap<>();
            address.put("idUser", auth.getUid());
            address.put("idAddress", TAG + randomKey);
            address.put("nameUser", name);
            address.put("phoneNumber", phone);
            address.put("address", addr);
            myRef.child(TAG + randomKey).updateChildren(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        System.out.println("Thêm địa chỉ thành công");
                        startActivity(new Intent(NewAddressActivity.this, AddressActivity.class));
                    }
                }
            });
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        eName = findViewById(R.id.eName);
        ePhone = findViewById(R.id.ePhone);
        eAddress = findViewById(R.id.eAddress);
        btAdd = findViewById(R.id.btAdd);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Address/" + auth.getUid());
    }
}