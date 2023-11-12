package com.example.hanfood.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.DetailBillActivity;
import com.example.hanfood.R;
import com.example.hanfood.model.Bill;
import com.example.hanfood.model.ItemFood;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.HomeViewHolder> {
    private ArrayList<Bill> list;
    Context context;
    private String state = "", listFood = "";


    public BillAdapter(ArrayList<Bill> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new HomeViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        Bill bill = list.get(position);
        state = bill.getStateOrder();
        holder.tvStateOrder.setText(state);
        holder.tvIdBill.setText(String.valueOf(bill.getIdBill()));
        holder.tvPrice.setText(decimalFormat.format(bill.getPrice()) + " VNĐ");
        holder.tvTime.setText(bill.getCurrentDate() + " " + bill.getCurrentTime());
        holder.tvName.setText(bill.getName());
        holder.tvPhone.setText(bill.getPhone());
        holder.tvAddress.setText(bill.getAddress());

        //hien thi danh sach mon an
        for (ItemFood itemFood : bill.getItemFoodArrayList()) {
            String name = itemFood.getProductName();
            double price = itemFood.getProductPriceSalse();
            int quantity = itemFood.getTotalQuantity();
            listFood = listFood + name + " (" + decimalFormat.format(price) + " VNĐ)" + " - số lượng " + quantity + "\n";
        }
        holder.tvListFood.setText(listFood);
        listFood = "";

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailBillActivity.class);
                i.putExtra("idBill", bill.getIdBill());
                context.startActivity(i);
            }
        });

        if (bill.isEvaluate()==true){
            holder.cardView.setBackgroundColor(0xFFECF2F8);
        }else {
            holder.cardView.setBackgroundColor(0xFFFFFFFF);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrice, tvTime, tvStateOrder, tvIdBill, tvName, tvPhone, tvAddress, tvListFood;
        CardView cardView;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            tvIdBill = view.findViewById(R.id.tvIdBill);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvTime = view.findViewById(R.id.tvTime);
            cardView = view.findViewById(R.id.card_view);
            tvStateOrder = view.findViewById(R.id.tvStateOrder);
            tvName = view.findViewById(R.id.tvName);
            tvPhone = view.findViewById(R.id.tvPhone);
            tvAddress = view.findViewById(R.id.tvAddress);
            tvListFood = view.findViewById(R.id.tvListFood);
        }
    }
}
