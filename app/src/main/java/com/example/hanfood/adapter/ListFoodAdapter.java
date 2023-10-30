package com.example.hanfood.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.R;
import com.example.hanfood.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListFoodAdapter extends RecyclerView.Adapter<ListFoodAdapter.HomeViewHolder> {
    private List<Cart> list;
    Context context;

    public ListFoodAdapter(List<Cart> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        Cart cart = list.get(position);
        holder.tvName.setText(cart.getProductName());
        holder.tvQuantity.setText(cart.getTotalQuantity() + "x   ");
//        holder.tvPrice.setText(decimalFormat.format(cart.getTotalPrice()) + " VNĐ");
//        holder.tvPriceSale.setText(decimalFormat.format(cart.getProductPriceSalse()) + " VNĐ");

        if (cart.getProductPriceSalse() < cart.getProductPrice()) {
            holder.tvPrice.setText(decimalFormat.format(cart.getProductPrice()) + " VNĐ");
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvPriceSale.setText(decimalFormat.format(cart.getProductPriceSalse()) + " VNĐ");

        } else {
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvPriceSale.setText(decimalFormat.format(cart.getProductPrice()) + " VNĐ");
        }

        Picasso.get().load(cart.getProductImg())
                .into(holder.imgFood);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvPrice, tvPriceSale;
        ImageView imgFood;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvQuantity = view.findViewById(R.id.tvQuantity);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvPriceSale = view.findViewById(R.id.tvPriceSale);
            imgFood = view.findViewById(R.id.imgFood);
        }

    }

}
