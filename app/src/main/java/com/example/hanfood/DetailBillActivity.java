package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hanfood.adapter.ListFoodAdapter;
import com.example.hanfood.model.Bill;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DetailBillActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvAddress, tvidBill, tvDate, tvPrice, tvName, tvPhone, tvTmpPrice, tvShip, tvQuantityFood, tvNote;
    ImageView imgNote;
    RecyclerView recyclerView;
    ListFoodAdapter cartAdapter;
    DatabaseReference myRef;
    FirebaseUser auth;
    String address, idBill, idOrder, date, name, phone, note;
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
                finish();
            }
        });
        idBill = getIntent().getStringExtra("id");
        idOrder = getIntent().getStringExtra("idOrder");
        address = getIntent().getStringExtra("address");
        price = getIntent().getDoubleExtra("price", 22);

        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        tvAddress.setText(address);
        tvPrice.setText(decimalFormat.format(price) + " VNĐ");
        tvTmpPrice.setText(decimalFormat.format(price) + " VNĐ");
        tvidBill.setText(idBill);

        LinearLayoutManager manager = new LinearLayoutManager(DetailBillActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Bill/" + auth.getUid());
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Bill bill = data.getValue(Bill.class);
                    if (bill.getIdOrder().equalsIgnoreCase(idOrder)) {
                        date = bill.getCurrentDate() + " " + bill.getCurrentTime();
                        name = bill.getName();
                        phone = bill.getPhone();
                        note = bill.getNote();
                        cartAdapter = new ListFoodAdapter(bill.getCartArrayList(), DetailBillActivity.this);
                        tvQuantityFood.setText("Tạm tính (" + bill.getCartArrayList().size() + " món)");
                    }
                }
                tvDate.setText(date);
                tvName.setText(name);
                tvPhone.setText(phone);

                if (note.equalsIgnoreCase("")){
                    tvNote.setVisibility(View.GONE);
                    imgNote.setVisibility(View.GONE);
                }else {
                    tvNote.setText(note);
                }
                recyclerView.setAdapter(cartAdapter);
                recyclerView.setHasFixedSize(true);
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