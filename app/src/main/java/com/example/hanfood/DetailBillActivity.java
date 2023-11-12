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
import android.widget.ListView;
import android.widget.TextView;

import com.example.hanfood.adapter.EvaluateAdapter;
import com.example.hanfood.adapter.ListFoodAdapter;
import com.example.hanfood.model.Bill;
import com.example.hanfood.model.ItemFood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DetailBillActivity extends AppCompatActivity{
    Toolbar toolbar;
    TextView tvAddress, tvidBill, tvDate, tvPrice, tvName, tvPhone, tvTmpPrice, tvShip, tvQuantityFood, tvNote;
    ImageView imgNote;
    RecyclerView recyclerView;
    EvaluateAdapter evaluateAdapter;
    DatabaseReference myRef;
    FirebaseUser auth;
    String address, idBill, date, name, phone, note;
    ArrayList<ItemFood> itemFoodArrayList = new ArrayList<>();
    boolean evaluate;
    double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bill);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailBillActivity.this, HistoryOrderActivity.class));
            }
        });

        idBill = getIntent().getStringExtra("idBill");

        displayDetailBill();
    }

    private void displayDetailBill() {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        LinearLayoutManager manager = new LinearLayoutManager(DetailBillActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Bill/" + auth.getUid());
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Bill bill = data.getValue(Bill.class);
                    if (bill.getIdBill().equalsIgnoreCase(idBill)) {
                        date = bill.getCurrentTime() + " " + bill.getCurrentDate();
                        address = bill.getAddress();
                        price = bill.getPrice();
                        name = bill.getName();
                        phone = bill.getPhone();
                        note = bill.getNote();
                        evaluate = bill.isEvaluate();
                        itemFoodArrayList = bill.getItemFoodArrayList();
                    }
                }
                evaluateAdapter = new EvaluateAdapter(itemFoodArrayList, DetailBillActivity.this);
                recyclerView.setAdapter(evaluateAdapter);
                recyclerView.setHasFixedSize(true);

                tvDate.setText(date);
                tvName.setText(name);
                tvPhone.setText(phone);
                tvAddress.setText(address);
                tvPrice.setText(decimalFormat.format(price) + " VNĐ");
                tvTmpPrice.setText(decimalFormat.format(price) + " VNĐ");
                tvidBill.setText(idBill);
                tvQuantityFood.setText("Tạm tính (" + itemFoodArrayList.size() + " món)");

                if (note.equalsIgnoreCase("")) {
                    tvNote.setVisibility(View.GONE);
                    imgNote.setVisibility(View.GONE);
                } else {
                    tvNote.setText(note);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvAddress = findViewById(R.id.tvAddress);
        tvPrice = findViewById(R.id.tvPrice);
        tvPrice = findViewById(R.id.tvPrice);
        tvidBill = findViewById(R.id.tvidBill);
        tvDate = findViewById(R.id.tvDate);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvQuantityFood = findViewById(R.id.tvQuantityFood);
        tvShip = findViewById(R.id.tvShip);
        tvTmpPrice = findViewById(R.id.tvTmpPrice);
        recyclerView = findViewById(R.id.recycleView_food);
        tvNote = findViewById(R.id.tvNote);
        imgNote = findViewById(R.id.note);
    }


}