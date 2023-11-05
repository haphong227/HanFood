package com.example.hanfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.DetailBillActivity;
import com.example.hanfood.EditAddressActivity;
import com.example.hanfood.NewAddressActivity;
import com.example.hanfood.PaymentActivity;
import com.example.hanfood.R;
import com.example.hanfood.model.Address;
import com.example.hanfood.model.Bill;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.HomeViewHolder> {
    private ArrayList<Address> list;
    Context context;

    public AddressAdapter(ArrayList<Address> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Address address = list.get(position);
        holder.tvUserName.setText(address.getNameUser()+ " | ");
        holder.tvPhone.setText(address.getPhoneNumber());
        holder.tvAddress.setText(address.getAddress());

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditAddressActivity.class);
                i.putExtra("idAddress", address.getIdAddress());
                System.out.println(address.getIdAddress());
                context.startActivity(i);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PaymentActivity.class);
                i.putExtra("idAddress", address.getIdUser());
                i.putExtra("nameUser", address.getNameUser());
                i.putExtra("phoneNumber", address.getPhoneNumber());
                i.putExtra("address", address.getAddress());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvPhone, tvEdit, tvAddress;

        public HomeViewHolder(@NonNull View view) {
            super(view);
            tvUserName = view.findViewById(R.id.tvUserName);
            tvPhone = view.findViewById(R.id.tvPhone);
            tvEdit = view.findViewById(R.id.tvEdit);
            tvAddress = view.findViewById(R.id.tvAddress);
        }
    }
}
