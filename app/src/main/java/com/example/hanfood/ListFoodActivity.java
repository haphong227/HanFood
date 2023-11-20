package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanfood.adapter.FoodAdapter;
import com.example.hanfood.adapter.admin.AdminFoodAdapter;
import com.example.hanfood.model.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    FoodAdapter foodAdapter;
    ArrayList<Food> dataFood;
    DatabaseReference myFood;
    String idCate = "";

    public String nameCate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListFoodActivity.this, MainActivity.class));
            }
        });


        idCate = getIntent().getStringExtra("idCate");
        nameCate = getIntent().getStringExtra("nameCate");

        displayListFood();
    }

    private void displayListFood() {
        GridLayoutManager manager = new GridLayoutManager(ListFoodActivity.this, 2);
        recyclerView.setLayoutManager(manager);

        myFood = FirebaseDatabase.getInstance().getReference("Food/");
        myFood.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataFood = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    if (food.getIdCate().equalsIgnoreCase(idCate)) {
                        dataFood.add(food);
                    }
                }
                foodAdapter = new FoodAdapter(dataFood, ListFoodActivity.this);
                recyclerView.setAdapter(foodAdapter);
                recyclerView.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListFoodActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerViewFood);
    }
}