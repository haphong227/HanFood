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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanfood.adapter.admin.AdminFoodAdapter;
import com.example.hanfood.model.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminFoodActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    TextView toolbar_title;
    RecyclerView recyclerView;
    FloatingActionButton fabtnFood;
    AdminFoodAdapter foodAdapter;
    ArrayList<Food> dataFood;
    DatabaseReference myFood;
    String idCate = "";

    public String nameCate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_food);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminFoodActivity.this, AdminMainActivity.class));
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(AdminFoodActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        idCate = getIntent().getStringExtra("idCate");
        nameCate = getIntent().getStringExtra("nameCate");
        toolbar_title.setText(nameCate);

        dataFood = new ArrayList<>();

        foodAdapter = new AdminFoodAdapter(dataFood, AdminFoodActivity.this);
        recyclerView.setAdapter(foodAdapter);
        recyclerView.setHasFixedSize(true);

        myFood = FirebaseDatabase.getInstance().getReference("Food/");
        myFood.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dataFood.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Food food = data.getValue(Food.class);
                        if (food.getIdCate().equalsIgnoreCase(idCate)) {
                            dataFood.add(food);
                        }
                    }
                    foodAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminFoodActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });

        fabtnFood.setOnClickListener(this);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewFood);
        fabtnFood = findViewById(R.id.fabtnFood);
        toolbar_title = findViewById(R.id.toolbar_title);
    }

    @Override
    public void onClick(View view) {
        if (view == fabtnFood) {
            Intent i = new Intent(AdminFoodActivity.this, AdminNewFoodActivity.class);
            i.putExtra("idCate", idCate);
            i.putExtra("nameCate", nameCate);
            startActivity(i);
        }
    }
}