package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanfood.adapter.FoodAdapter;
import com.example.hanfood.model.Category;
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
    Spinner spSearch;
    ArrayList<Food> arrayListfood = new ArrayList<>();
    RecyclerView recyclerView;
    DatabaseReference myRef, myCate;
    FoodAdapter adapter;
    String search;
    ArrayList<String> spinnerList;
    ArrayAdapter<String> spinnerAdapter;

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

        btSearch.setOnClickListener(this);

        spinnerList = new ArrayList<>();
        myCate = FirebaseDatabase.getInstance().getReference("Category");
        myCate.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    spinnerList.add(category.getNameCate());
                }
                System.out.println("spinner" + spinnerList);
                spinnerAdapter = new ArrayAdapter<String>(SearchFoodActivity.this,
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerList);
                spSearch.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        spSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                search = spSearch.getSelectedItem().toString();

//                Toast.makeText(SearchFoodActivity.this, search, Toast.LENGTH_SHORT).show();
                myCate = FirebaseDatabase.getInstance().getReference("Category");
                myCate.child("").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Category category = data.getValue(Category.class);
                            if (category.getNameCate().equalsIgnoreCase(search)) {
                                filterCate(category.getIdCate());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btSearch) {
            if (!eSearch.getText().toString().equals("")) {
                filterName(eSearch.getText().toString());
            } else {
                Toast.makeText(SearchFoodActivity.this, "Vui lòng nhập tên sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void filterName(String searchname) {
        myRef = FirebaseDatabase.getInstance().getReference("Food");
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListfood.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    if (food.getNameFood().toLowerCase().contains(searchname)) {
                        arrayListfood.add(food);
                    }
                }
                adapter = new FoodAdapter(arrayListfood, SearchFoodActivity.this);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                if (arrayListfood.size() == 0) {
                    Toast.makeText(SearchFoodActivity.this, "Không tìm thấy món ăn!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void filterCate(String searchname) {
        myRef = FirebaseDatabase.getInstance().getReference("Food");
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListfood.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    if (food.getIdCate().equalsIgnoreCase(searchname)) {
                        arrayListfood.add(food);
                    }
                }
                adapter = new FoodAdapter(arrayListfood, SearchFoodActivity.this);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                if (arrayListfood.size() == 0) {
                    Toast.makeText(SearchFoodActivity.this, "Không tìm thấy món ăn!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        eSearch = findViewById(R.id.eSearch);
        btSearch = findViewById(R.id.btSearch);
        recyclerView = findViewById(R.id.recycleView_search);
        spSearch = findViewById(R.id.spSearch);
    }
}