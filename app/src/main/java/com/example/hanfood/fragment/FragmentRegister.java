package com.example.hanfood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hanfood.AdminNewCategoryActivity;
import com.example.hanfood.R;
import com.example.hanfood.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FragmentRegister extends Fragment {
    DatabaseReference databaseReference;
    DatabaseReference myRef;
    EditText eEmail, ePassword, eName;
    Button btRegister;
    FirebaseAuth auth;
    String email, password, confirmPassword, name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        return view;
    }

    private void register() {
        email = eEmail.getText().toString().trim();
        password = ePassword.getText().toString().trim();
        name = email.substring(0, email.length() - 10);

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
        } else if (!email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
            Toast.makeText(getActivity(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(getActivity(), "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
        } else {

            //create user
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    } else {
                        addUser();
                        addAddress();
                    }
                }
            });
        }
    }

    private void addAddress() {
        HashMap<String, Object> address = new HashMap<>();
        address.put("idUser", auth.getUid());
        address.put("nameUser", name);
        address.put("phoneNumber", null);
        address.put("address", null);
        myRef.child(auth.getUid()).updateChildren(address).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    System.out.println("Thêm địa chỉ thành công");
                }
            }
        });
    }

    private void addUser() {
        HashMap<String, Object> user = new HashMap<>();
        user.put("address", null);
        user.put("email", email);
        user.put("password", password);
        user.put("name", null);
        user.put("phone", null);
        user.put("idUser", auth.getUid());
        user.put("image", null);
        databaseReference.child(auth.getUid()).updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView(View view) {
        eEmail = view.findViewById(R.id.eEmail);
        ePassword = view.findViewById(R.id.ePassword);
        eName = view.findViewById(R.id.eName);
        btRegister = view.findViewById(R.id.btRegister);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        myRef = FirebaseDatabase.getInstance().getReference("Address");
    }
}
