package com.example.hanfood.adapter;

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

import com.example.hanfood.DetailFoodActivity;
import com.example.hanfood.R;
import com.example.hanfood.model.Food;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.HomeViewHolder> {
    List<Food> list;
    Context context;
    String price, priceFoodSale = "";

    public FoodAdapter(List<Food> list, Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        Food food = list.get(position);
        holder.txtName.setText(food.getNameFood());

        price = decimalFormat.format(Double.parseDouble(food.getPriceFood())) + " VNĐ";
        double priceFood = Double.parseDouble(food.getPriceFood());
        double priceSale = priceFood - food.getPercentSale() * priceFood / 100;
        priceFoodSale = decimalFormat.format(priceSale);

        if (food.getPercentSale() == 0) {
            holder.txtPrice.setVisibility(View.GONE);
            holder.percentSale.setVisibility(View.GONE);
            holder.txtpriceSale.setText(price);
        } else {
            holder.txtPrice.setText(price);
            holder.txtPrice.setPaintFlags(holder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); //gach ngang chu
            holder.txtpriceSale.setText(priceFoodSale);
            holder.percentSale.setText("Giảm " + decimalFormat.format(food.getPercentSale()) + "%");
        }

        Picasso.get().load(food.getImageFood())
                .into(holder.img);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailFoodActivity.class);
                i.putExtra("nameFood", food.getNameFood());
                i.putExtra("idFood", food.getIdFood());
                i.putExtra("desFood", food.getDesFood());
                i.putExtra("priceFood", food.getPriceFood());
                i.putExtra("priceFoodSale", priceSale);
                i.putExtra("percentSale", food.getPercentSale());
                i.putExtra("imageFood", food.getImageFood());
                i.putExtra("idCate", food.getIdCate());
                i.putExtra("quantityFood", food.getQuantityFood());
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
        TextView txtName, txtPrice, txtpriceSale, percentSale;
        CardView cardView;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.img);
            txtName = view.findViewById(R.id.txtName);
            txtPrice = view.findViewById(R.id.txtprice);
            txtpriceSale = view.findViewById(R.id.txtpriceSale);
            percentSale = view.findViewById(R.id.percentSale);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}
