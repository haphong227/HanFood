package com.example.hanfood.adapter.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.AdminEditDeleteCategoryActivity;
import com.example.hanfood.AdminFoodActivity;
import com.example.hanfood.R;
import com.example.hanfood.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.HomeViewHolder> {
    private List<Category> list;
    Context context;

    public AdminCategoryAdapter(List<Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_admin, parent, false);
        return new HomeViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Category category = list.get(position);
        holder.tvName.setText(category.getNameCate());
        Picasso.get().load(category.getImageCate())
                .into(holder.img);
        holder.tvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AdminFoodActivity.class);
                i.putExtra("idCate", category.getIdCate());
                i.putExtra("nameCate", category.getNameCate());
                context.startActivity(i);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AdminEditDeleteCategoryActivity.class);
                i.putExtra("idCate", category.getIdCate());
                i.putExtra("nameCate", category.getNameCate());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView img, edit;
        TextView tvName, tvList;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img);
            edit = view.findViewById(R.id.edit);
            tvName = view.findViewById(R.id.tvName);
            tvList = view.findViewById(R.id.tvList);
        }

    }

}
