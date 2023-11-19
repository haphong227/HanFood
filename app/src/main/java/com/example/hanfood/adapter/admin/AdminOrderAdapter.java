package com.example.hanfood.adapter.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.DetailBillActivity;
import com.example.hanfood.EvaluateActivity;
import com.example.hanfood.MainActivity;
import com.example.hanfood.PaymentActivity;
import com.example.hanfood.R;
import com.example.hanfood.model.Bill;
import com.example.hanfood.model.ItemFood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.HomeViewHolder> {
    private ArrayList<Bill> list;
    Context context;
    private String state = "", listFood = "";
    private DatabaseReference myRef, myFood;

    public AdminOrderAdapter(ArrayList<Bill> list, Context context) {
        this.list = list;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new HomeViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");

        Bill bill = list.get(position);
        holder.tvIdBill.setText(String.valueOf(bill.getIdBill()));
        holder.tvPrice.setText(decimalFormat.format(bill.getPrice()) + " VNĐ");
        holder.tvTime.setText(bill.getCurrentDate() + " " + bill.getCurrentTime());
        holder.tvEmail.setText(bill.getEmail());
        holder.tvName.setText(bill.getName());
        holder.tvPhone.setText(bill.getPhone());
        holder.tvAddress.setText(bill.getAddress());

        //hien thi danh sach mon an
        for (ItemFood itemFood : bill.getItemFoodArrayList()) {
            String name = itemFood.getProductName();
            double price = itemFood.getProductPriceSale();
            int quantity = itemFood.getTotalQuantity();
            listFood = listFood + name + " (" + decimalFormat.format(price) + " VNĐ)" + " - số lượng " + quantity + "\n";
        }
        holder.tvListFood.setText(listFood);
        listFood = "";

        state = bill.getStateOrder();
        holder.tvStateOrder.setText(state);

        //set trang thai cua checkbox
        boolean check = false;
        if (state.equalsIgnoreCase("Đã xác nhận")) {
            check = false;
            holder.checkbox.setChecked(check);
        }
        if (state.equalsIgnoreCase("Đã giao thành công")) {
            check = true;
            holder.checkbox.setChecked(check);
            holder.cardView.setBackgroundColor(0xFFECF2F8);
        }

        //bat su kien khi checkbox thay doi trang thai
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(compoundButton.getContext(), compoundButton.getText() + "|" + b, Toast.LENGTH_SHORT).show();
                if (b == true) {
                    holder.tvStateOrder.setText("Đã giao thành công");
                    holder.cardView.setBackgroundColor(0xFFECF2F8);
                    updateStateTrue(position);

                    //+ so luong san pham da ban
                    updateQuantity(position);
                } else {
                    holder.tvStateOrder.setText("Đã xác nhận");
                    holder.cardView.setBackgroundColor(0xFFFFFFFF);
                    updateStateFalse(position);

                    //- so luong san pham da ban
                    updateQuantitySub(position);
                }

            }
        });
    }

    private void updateQuantitySub(int position) {
        for (ItemFood itemFood : list.get(position).getItemFoodArrayList()) {
            String idFood = itemFood.getIdFood();
            myFood = FirebaseDatabase.getInstance().getReference().child("Food").child(idFood);
            myFood.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        int quantity = Integer.parseInt(snapshot.child("quantityFoodSold").getValue().toString());
                        int newQuantity = quantity - itemFood.getTotalQuantity();
                        Log.d("onDataChange: ", String.valueOf(newQuantity));
                        HashMap<String, Object> food = new HashMap<>();
                        food.put("quantityFoodSold", newQuantity);
                        myFood = FirebaseDatabase.getInstance().getReference().child("Food").child(idFood);
                        myFood.updateChildren(food).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    System.out.println("So san pham da bannn tru");
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void updateQuantity(int position) {
        for (ItemFood itemFood : list.get(position).getItemFoodArrayList()) {
            String idFood = itemFood.getIdFood();
            myFood = FirebaseDatabase.getInstance().getReference().child("Food").child(idFood);
            myFood.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        int quantity = Integer.parseInt(snapshot.child("quantityFoodSold").getValue().toString());
                        int newQuantity = itemFood.getTotalQuantity() + quantity;
                        HashMap<String, Object> food = new HashMap<>();
                        food.put("quantityFoodSold", newQuantity);
                        myFood = FirebaseDatabase.getInstance().getReference().child("Food").child(idFood);
                        myFood.updateChildren(food).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    System.out.println("So san pham da bannn");
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void updateStateFalse(int position) {
        HashMap<String, Object> bill1 = new HashMap<>();
        bill1.put("stateOrder", "Đã xác nhận");
        myRef = FirebaseDatabase.getInstance().getReference("Bill/" + list.get(position).getIdUser());
        myRef.child(list.get(position).getIdOrder()).updateChildren(bill1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("Update stateOrder0");
                }
            }
        });
    }

    private void updateStateTrue(int position) {
        HashMap<String, Object> bill1 = new HashMap<>();
        bill1.put("stateOrder", "Đã giao thành công");

        myRef = FirebaseDatabase.getInstance().getReference("Bill/" + list.get(position).getIdUser());
        myRef.child(list.get(position).getIdOrder()).updateChildren(bill1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("Update stateOrder1");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdBill, tvEmail, tvName, tvPhone, tvAddress, tvListFood, tvTime, tvPrice, tvStateOrder;
        CheckBox checkbox;
        CardView cardView;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            tvIdBill = view.findViewById(R.id.tvIdBill);
            tvEmail = view.findViewById(R.id.tvEmail);
            tvName = view.findViewById(R.id.tvName);
            tvPhone = view.findViewById(R.id.tvPhone);
            tvAddress = view.findViewById(R.id.tvAddress);
            tvListFood = view.findViewById(R.id.tvListFood);
            tvStateOrder = view.findViewById(R.id.tvStateOrder);
            tvTime = view.findViewById(R.id.tvTime);
            tvPrice = view.findViewById(R.id.tvPrice);
            checkbox = view.findViewById(R.id.checkbox);
            cardView = view.findViewById(R.id.card_view);
        }
    }
}
