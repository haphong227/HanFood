package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hanfood.model.Address;
import com.example.hanfood.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class EditAddressActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText eName, ePhone, eAddress;
    Button btUpdate, btDelete;
    String idAddress, phone, name, addr;
    FirebaseUser auth;
    DatabaseReference myRef;
    private String saveCurDate, saveCurTime;
    private String randomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditAddressActivity.this, AddressActivity.class);
                finish();
            }
        });

        idAddress = getIntent().getStringExtra("idAddress");
        System.out.println(idAddress);

        displayAddress();

        btUpdate.setOnClickListener(this);
        btDelete.setOnClickListener(this);

    }

    private void displayAddress() {
        myRef.child(idAddress).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String phoneNumber = snapshot.child("phoneNumber").getValue().toString();
                String nameUser = snapshot.child("nameUser").getValue().toString();
                String address = snapshot.child("address").getValue().toString();

                eName.setText(nameUser);
                ePhone.setText(phoneNumber);
                eAddress.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btDelete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Xóa địa chỉ");
            builder.setMessage("Bạn có chắc muốn xóa?");
            builder.setIcon(R.drawable.remove);
            builder.setNegativeButton("Bỏ qua", null);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteAddress(idAddress);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (view == btUpdate) {
            updateAddress();
        }

    }

    private void updateAddress() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat curDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurDate = curDate.format(c.getTime());
        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
        saveCurTime = curTime.format(c.getTime());
        randomKey = saveCurDate + "-" + saveCurTime;

        name = eName.getText().toString();
        phone = ePhone.getText().toString();
        addr = eAddress.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Không được để trống tên!", Toast.LENGTH_SHORT).show();
        } else if (phone.isEmpty()) {
            Toast.makeText(this, "Không được để trống số điện thoại!", Toast.LENGTH_SHORT).show();
        } else if (addr.isEmpty()) {
            Toast.makeText(this, "Không được để trống địa chỉ!", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> address = new HashMap<>();
            address.put("nameUser", name);
            address.put("phoneNumber", phone);
            address.put("address", addr);
            myRef.child(idAddress).updateChildren(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        System.out.println("Cập nhật địa chỉ thành công");
                        startActivity(new Intent(EditAddressActivity.this, AddressActivity.class));
                    }
                }
            });
        }
    }

    private void deleteAddress(String idAddress) {
        myRef.child(idAddress).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditAddressActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditAddressActivity.this, AddressActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        eName = findViewById(R.id.eName);
        ePhone = findViewById(R.id.ePhone);
        eAddress = findViewById(R.id.eAddress);
        btUpdate = findViewById(R.id.btUpdate);
        btDelete = findViewById(R.id.btDelete);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Address/" + auth.getUid());
    }


}