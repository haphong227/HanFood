package com.example.hanfood.fragment.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.AdminNewCategoryActivity;
import com.example.hanfood.R;
import com.example.hanfood.adapter.admin.AdminCategoryAdapter;
import com.example.hanfood.model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentCategory extends Fragment implements View.OnClickListener{
    RecyclerView recyclerViewCate;
    FloatingActionButton fabtnCate;
    ArrayList<Category> dataCategory;
    AdminCategoryAdapter categoryAdapter;
    DatabaseReference myCate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        fabtnCate.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerViewCate.setLayoutManager(manager);

        myCate = FirebaseDatabase.getInstance().getReference("Category");
        myCate.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataCategory = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    dataCategory.add(category);
                }
                categoryAdapter = new AdminCategoryAdapter(dataCategory, getContext());
                recyclerViewCate.setAdapter(categoryAdapter);
                recyclerViewCate.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        recyclerViewCate = view.findViewById(R.id.recyclerViewCate);
        fabtnCate = view.findViewById(R.id.fabtnCate);
    }

    @Override
    public void onClick(View view) {
        if (view == fabtnCate){
            startActivity(new Intent(getActivity(), AdminNewCategoryActivity.class));
        }
    }
}
