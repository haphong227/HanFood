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
import android.widget.ArrayAdapter;

import com.example.hanfood.adapter.CommentAdapter;
import com.example.hanfood.adapter.EvaluateAdapter;
import com.example.hanfood.adapter.ItemFoodCartAdapter;
import com.example.hanfood.model.Comment;
import com.example.hanfood.model.ItemFood;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rcv_comment;

    String idFood="";
    CommentAdapter evaluateAdapter;

    DatabaseReference myRef;

    ArrayList<Comment> commentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CommentActivity.this, DetailFoodActivity.class);
                i.putExtra("idFood", idFood);
                startActivity(i);
            }
        });

        idFood = getIntent().getStringExtra("idFood");
//        System.out.println(idFood);

        displayComment();
    }

    private void displayComment() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentActivity.this, LinearLayoutManager.VERTICAL, false);
        rcv_comment.setLayoutManager(linearLayoutManager);

        myRef.child(idFood).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentArrayList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Comment comment = data.getValue(Comment.class);
                    commentArrayList.add(comment);
                }
//                System.out.println(commentArrayList.size());

                evaluateAdapter = new CommentAdapter(commentArrayList, CommentActivity.this);
                rcv_comment.setAdapter(evaluateAdapter);
                rcv_comment.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        rcv_comment = findViewById(R.id.rcv_comment);
        myRef = FirebaseDatabase.getInstance().getReference("Comment");
    }
}