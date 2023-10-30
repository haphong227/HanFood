package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanfood.adapter.FoodAdapter;
import com.example.hanfood.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFoodActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText eSearch;
    Button btSearch;
    ArrayList<Food> arrayListfood = new ArrayList<>();
    RecyclerView recyclerView;
    DatabaseReference myRef;
    FoodAdapter adapter;
    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);
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

        GridLayoutManager layoutManager = new GridLayoutManager(SearchFoodActivity.this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        myRef = FirebaseDatabase.getInstance().getReference("Food");
        btSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btSearch) {
            if (!eSearch.getText().toString().equals("")) {
                myRef.child("").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayListfood.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Food food = data.getValue(Food.class);
                            if (food.getNameFood().toLowerCase().contains(eSearch.getText().toString())) {
                                arrayListfood.add(food);
                            }
                        }
                        adapter = new FoodAdapter(arrayListfood, SearchFoodActivity.this);
                        recyclerView.setAdapter(adapter);
                        if (arrayListfood.size() == 0) {
                            Toast.makeText(SearchFoodActivity.this, "Không tìm thấy món ăn!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else {
                Toast.makeText(SearchFoodActivity.this, "Vui lòng nhập tên sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        eSearch = findViewById(R.id.eSearch);
        btSearch = findViewById(R.id.btSearch);
        recyclerView = findViewById(R.id.recycleView_search);
    }
}