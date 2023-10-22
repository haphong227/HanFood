package com.example.hanfood.adapter.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.R;
import com.example.hanfood.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.HomeViewHolder> {
    private ArrayList<Category> list;

    public SliderAdapter(ArrayList<Category> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Category category = list.get(position);
        holder.tvDeal.setText(category.getNameCate());
        Picasso.get().load(category.getImageCate())
                .into(holder.imgFood);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgFood;
        private TextView tvDeal;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood=itemView.findViewById(R.id.imgFood);
            tvDeal=itemView.findViewById(R.id.tvDeal);
        }
    }
}
