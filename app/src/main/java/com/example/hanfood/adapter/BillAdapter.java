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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.HomeViewHolder> {
    private ArrayList<Bill> list;
    Context context;
    String state = "";

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
//        if (state.equalsIgnoreCase("Đã xác nhận")){
//        holder.tvStateOrder.setText(state.toUpperCase());
//            holder.tvStateOrder.setTextColor(R.color.teal_700);
//        }
        holder.tvStateOrder.setText(state);
        holder.tvIdBill.setText(String.valueOf(bill.getIdBill()));
        holder.tvQuantity.setText(String.valueOf(bill.getQuantity() + " món"));
        holder.tvPrice.setText("Tổng tiền: " + decimalFormat.format(bill.getPrice()) + " VNĐ");
        holder.tvTime.setText(bill.getCurrentDate() + " " + bill.getCurrentTime());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailBillActivity.class);
                i.putExtra("id", bill.getIdBill());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuantity, tvPrice, tvTime, tvStateOrder, tvIdBill;
        CardView cardView;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            tvIdBill = view.findViewById(R.id.tvIdBill);
            tvQuantity = view.findViewById(R.id.tvQuantity);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvTime = view.findViewById(R.id.tvTime);
            cardView = view.findViewById(R.id.card_view);
            tvStateOrder = view.findViewById(R.id.tvStateOrder);
        }
    }
}
