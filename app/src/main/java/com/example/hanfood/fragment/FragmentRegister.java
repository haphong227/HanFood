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

import com.example.hanfood.R;
import com.example.hanfood.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentRegister extends Fragment {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
    EditText eEmail, ePassword, eConfirmPassword, eName;
    Button btRegister;
    FirebaseAuth auth;

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
        String email = eEmail.getText().toString().trim();
        String password = ePassword.getText().toString().trim();
        String confirmPassword = eConfirmPassword.getText().toString().trim();
        String name = eName.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
        } else {
            if (!email.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
                Toast.makeText(getActivity(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getActivity(), "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            } else if (!(password.matches(confirmPassword))) {
                eConfirmPassword.setError("Mật mật không trùng khớp!");
            } else {

                //create user
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                        } else {
                            User user = new User(null, email, password, name, null, auth.getUid(), null);
                            String key = databaseReference.push().getKey();
                            databaseReference.child(key).setValue(user);
                            Toast.makeText(getActivity(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void initView(View view) {
        eEmail = view.findViewById(R.id.eEmail);
        ePassword = view.findViewById(R.id.ePassword);
        eConfirmPassword = view.findViewById(R.id.eConfirmPassword);
        eName = view.findViewById(R.id.eName);
        btRegister = view.findViewById(R.id.btRegister);
        auth = FirebaseAuth.getInstance();
    }
}
