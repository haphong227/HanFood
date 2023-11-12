package com.example.hanfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.R;
import com.example.hanfood.model.Comment;
import com.example.hanfood.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.HomeViewHolder> {
    private ArrayList<Comment> list;
    Context context;
    private DatabaseReference myRef;
    private FirebaseUser auth;
    private String name, img;
    private double rate;

    public CommentAdapter(ArrayList<Comment> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Comment comment = list.get(position);
        holder.tvDateTime.setText(comment.getCurrentTime() + " " + comment.getCurrentDate());
        holder.tvContent.setText(comment.getContent());

        myRef = FirebaseDatabase.getInstance().getReference("User");
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user.getIdUser().equalsIgnoreCase(comment.getIdUser())) {
                        name = user.getName();
                        img = user.getImage();

                    }
                }
//                System.out.println("user nameeeee" + name + "imgggg" + img);

                if (img == null) {
                    Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(holder.imgUser);
                } else if (img != null) {
                    Picasso.get().load(img).into(holder.imgUser);
                }
                holder.tvNameUser.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.ratingBar.setRating((float) comment.getRate());

//
//        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                Toast.makeText(context, String.valueOf(rating), Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameUser, tvContent, tvDateTime;
        ImageView imgUser;
        RatingBar ratingBar;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            imgUser = view.findViewById(R.id.imgUser);
            tvNameUser = view.findViewById(R.id.tvNameUser);
            tvContent = view.findViewById(R.id.tvContent);
            tvDateTime = view.findViewById(R.id.tvDateTime);
            ratingBar = view.findViewById(R.id.ratingBar);
        }

    }

}
