package com.example.hanfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.EditEvaluateActivity;
import com.example.hanfood.EvaluateActivity;
import com.example.hanfood.R;
import com.example.hanfood.model.ItemFood;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class EvaluateAdapter extends RecyclerView.Adapter<EvaluateAdapter.HomeViewHolder> {
    private List<ItemFood> list;
    Context context;
    private float rate = 0;

    public EvaluateAdapter(List<ItemFood> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluate, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        ItemFood itemFood = list.get(position);
        holder.tvName.setText(itemFood.getProductName());
        holder.tvQuantity.setText(itemFood.getTotalQuantity() + "x   ");

        if (itemFood.getProductPriceSale() < itemFood.getProductPrice()) {
            holder.tvPrice.setText(decimalFormat.format(itemFood.getProductPrice()) + " VNĐ");
            holder.tvPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvPriceSale.setText(decimalFormat.format(itemFood.getProductPriceSale()) + " VNĐ");

        } else {
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvPriceSale.setText(decimalFormat.format(itemFood.getProductPrice()) + " VNĐ");
        }

        Picasso.get().load(itemFood.getProductImg())
                .into(holder.imgFood);

        if (itemFood.isEvaluate()==false){
            holder.btRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, EvaluateActivity.class);
                    i.putExtra("idFood", itemFood.getIdFood());
                    i.putExtra("idOrder", itemFood.getIdOrder());
                    i.putExtra("position", position);
                    context.startActivity(i);
                }
            });
        }
        else holder.btRate.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvPrice, tvPriceSale;
        ImageView imgFood;
        Button btRate;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvQuantity = view.findViewById(R.id.tvQuantity);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvPriceSale = view.findViewById(R.id.tvPriceSale);
            imgFood = view.findViewById(R.id.imgFood);
            btRate = view.findViewById(R.id.btRate);
        }
    }

}
