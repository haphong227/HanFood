package com.example.hanfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.PaymentActivity;
import com.example.hanfood.R;
import com.example.hanfood.adapter.ItemFoodCartAdapter;
import com.example.hanfood.model.ItemFood;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class FragmentCart extends Fragment implements View.OnClickListener {
    TextView tvTong;
    RecyclerView recyclerView_cart;
    Button btBuy;
    ItemFoodCartAdapter cartAdapter;
    ArrayList<ItemFood> cartArrayList;
    private FirebaseUser auth;
    private DatabaseReference myRef;
    double total = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_cart.setLayoutManager(linearLayoutManager);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        if (auth != null) {
            displayCart();
        }

        btBuy.setOnClickListener(this);
    }

    private void displayCart() {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        myRef = FirebaseDatabase.getInstance().getReference("ItemFood/" + auth.getUid());
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalAmount = 0;
                cartArrayList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    ItemFood itemFood = data.getValue(ItemFood.class);
                    cartArrayList.add(itemFood);
                    totalAmount += itemFood.getTotalPrice();
                }
                total = totalAmount;
                cartAdapter = new ItemFoodCartAdapter(cartArrayList, getContext());
                recyclerView_cart.setAdapter(cartAdapter);
                recyclerView_cart.setHasFixedSize(true);
                tvTong.setText("Tổng tiền: " + decimalFormat.format(totalAmount) + " VNĐ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btBuy) {
            String TAG = "Order";
            String saveCurrentTime, savecurrentDate, randomKey;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = curTime.format(c.getTime());
            SimpleDateFormat curDate = new SimpleDateFormat("yyyy-MM-dd");
            savecurrentDate = curDate.format(c.getTime());
            randomKey = savecurrentDate + "-" + saveCurrentTime;

            if (total > 0) {
                HashMap<String, Object> itemFood = new HashMap<>();
                itemFood.put("idOrder", TAG + randomKey);
                for (ItemFood food : cartArrayList) {
                    myRef = FirebaseDatabase.getInstance().getReference("ItemFood/" + auth.getUid());
                    myRef.child(food.getIdFood()).updateChildren(itemFood).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent i = new Intent(getContext(), PaymentActivity.class);
                            startActivity(i);
                        }
                    });
                }
            } else {
                Toast.makeText(getContext(), "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initView(View view) {
        recyclerView_cart = view.findViewById(R.id.recycleView_cart);
        tvTong = view.findViewById(R.id.tvTong);
        btBuy = view.findViewById(R.id.btBuy);
    }

}
