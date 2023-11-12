package com.example.hanfood.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanfood.R;
import com.example.hanfood.adapter.admin.AdminOrderAdapter;
import com.example.hanfood.model.Bill;
import com.example.hanfood.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class FragmentOrder extends Fragment {

    RecyclerView recyclerView_bill;
    AdminOrderAdapter orderAdapter;
    ArrayList<Bill> billArrayList = new ArrayList<>();
    DatabaseReference myRef, myUser;
    Query query;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView_bill = view.findViewById(R.id.recyclerView_bill);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView_bill.setLayoutManager(manager);

        getIdUser();

    }

    private void getIdUser() {
        myUser = FirebaseDatabase.getInstance().getReference("User");
        myUser.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    String idUser = user.getIdUser();
                    addBill(idUser); //lay danh sach bill theo user
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addBill(String idUser) {

        myRef = FirebaseDatabase.getInstance().getReference("Bill/" + idUser);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Bill bill = data.getValue(Bill.class);
                    billArrayList.add(bill);
                }
                //sortBill theo IdBill
                Collections.sort(billArrayList, Bill.BillIdCompare);
                orderAdapter = new AdminOrderAdapter(billArrayList, getContext());
                recyclerView_bill.setAdapter(orderAdapter);
                recyclerView_bill.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
