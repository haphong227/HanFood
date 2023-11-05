package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hanfood.adapter.AddressAdapter;
import com.example.hanfood.model.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    TextView newAddress;
    RecyclerView rcv_address;
    DatabaseReference myRef;
    ArrayList<Address> addressArrayList = new ArrayList<>();;
    AddressAdapter adapter;
    FirebaseUser auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this, PaymentActivity.class));
                finish();
            }
        });

        listAddress();

        newAddress.setOnClickListener(this);
    }

    private void listAddress() {
        LinearLayoutManager manager = new LinearLayoutManager(AddressActivity.this, LinearLayoutManager.VERTICAL, false);
        rcv_address.setLayoutManager(manager);

        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()){
                    Address address = data.getValue(Address.class);
                    addressArrayList.add(address);
                }
                adapter = new AddressAdapter(addressArrayList, AddressActivity.this);
                rcv_address.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        newAddress = findViewById(R.id.newAddress);
        rcv_address = findViewById(R.id.rcv_address);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Address/" + auth.getUid());
    }

    @Override
    public void onClick(View view) {
        if (view == newAddress){
            startActivity(new Intent(AddressActivity.this, NewAddressActivity.class));
        }

    }
}