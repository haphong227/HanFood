package com.example.hanfood.adapter.admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.AdminEditDeleteFoodActivity;
import com.example.hanfood.R;
import com.example.hanfood.model.Food;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminFoodAdapter extends RecyclerView.Adapter<AdminFoodAdapter.HomeViewHolder> {
    List<Food> list;
    Context context;

    public AdminFoodAdapter(List<Food> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<Food> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_admin, parent, false);
        return new HomeViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        Food food = list.get(position);
        holder.txtName.setText(food.getNameFood());
        holder.txtDes.setText(food.getDesFood());
        holder.txtQuantity.setText("Số lượng: " + String.valueOf(food.getQuantityFood()));

        String price = decimalFormat.format(Double.parseDouble(food.getPriceFood()))+ " VNĐ";
        double priceFood = Double.parseDouble(food.getPriceFood());
        double priceSale = priceFood - food.getPercentSale()*priceFood/100;
        String priceFoodSale = decimalFormat.format(priceSale)+ " VNĐ";

        if (food.getPercentSale()==0) {
            holder.txtPrice.setVisibility(View.GONE);
            holder.percentSale.setVisibility(View.GONE);
            holder.txtpriceSale.setText(price);
        }else {
            holder.txtPrice.setText(price);
            holder.txtPrice.setPaintFlags(holder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtpriceSale.setText(priceFoodSale);
            holder.percentSale.setText("Giảm " + decimalFormat.format(food.getPercentSale()) +"%");
        }

        Picasso.get().load(food.getImageFood())
                .into(holder.img);
        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, AdminEditDeleteFoodActivity.class);
                i.putExtra("idFood", food.getIdFood());
                i.putExtra("idCate", food.getIdCate());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtName, txtPrice, txtDes, txtQuantity, txtpriceSale, percentSale;
        CardView cardView;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img);
            txtName = view.findViewById(R.id.txtName);
            txtPrice = view.findViewById(R.id.txtprice);
            txtpriceSale = view.findViewById(R.id.txtpriceSale);
            txtDes = view.findViewById(R.id.txtdes);
            txtQuantity = view.findViewById(R.id.txtQuantity);
            percentSale = view.findViewById(R.id.percentSale);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}
