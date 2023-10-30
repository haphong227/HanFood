package com.example.hanfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.hanfood.ChangePasswordActivity;
import com.example.hanfood.EditProfileActivity;
import com.example.hanfood.HistoryOrderActivity;
import com.example.hanfood.LoginActivity;
import com.example.hanfood.R;
import com.example.hanfood.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentUser extends Fragment implements View.OnClickListener {
    FirebaseUser firebaseUser;
    DatabaseReference mRef;
    CircleImageView img_profile;
    CardView tvEditprofile;
    TextView tvUsername, tvEmail, tvChangepassword, tvHistory, tvLogout;
    String name, email, img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initView(view);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            displayUser();
        }

        tvEditprofile.setOnClickListener(this);
        tvChangepassword.setOnClickListener(this);
        tvHistory.setOnClickListener(this);
        tvLogout.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        if (view == tvEditprofile) {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        }
        if (view == tvChangepassword) {
            startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
        }
        if (view == tvHistory) {
            startActivity(new Intent(getActivity(), HistoryOrderActivity.class));
        }
        if (view == tvLogout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            Toast.makeText(getActivity(), "See you later", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUser() {
        mRef = FirebaseDatabase.getInstance().getReference("User");
        mRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user.getIdUser().equalsIgnoreCase(firebaseUser.getUid())) {
                        name = user.getName();
                        email = user.getEmail();
                        img = user.getImage();
                    }
                }
                tvEmail.setText(email);
                tvUsername.setText(name);
                if (img == null) {
                    Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(img_profile);
                } else if (img != null) {
                    Picasso.get().load(img).into(img_profile);
                }

//                if (snapshot.exists()) {
//                    name = snapshot.child("name").getValue().toString();
//                    email = snapshot.child("email").getValue().toString();
//                    img = snapshot.child("image").getValue().toString();
//
//                    tvEmail.setText(email);
//                    tvUsername.setText(name);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView(View view) {
        img_profile = view.findViewById(R.id.img_profile);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvEditprofile = view.findViewById(R.id.tvEditprofile);
        tvChangepassword = view.findViewById(R.id.tvChangepassword);
        tvHistory = view.findViewById(R.id.tvHistory);
        tvLogout = view.findViewById(R.id.tvLogout);
    }

}
