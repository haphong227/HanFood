package com.example.hanfood.fragment.admin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hanfood.ChangePasswordActivity;
import com.example.hanfood.EditProfileActivity;
import com.example.hanfood.HistoryOrderActivity;
import com.example.hanfood.LoginActivity;
import com.example.hanfood.R;
import com.example.hanfood.StatisticsActivity;
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

public class FragmentProfile extends Fragment implements View.OnClickListener {
    CardView tvEditprofile;
    FirebaseUser firebaseUser;
    DatabaseReference mRef;
    CircleImageView img_profile;
    TextView tvUsername, tvEmail, tvChangepassword, tvStatistics, tvLogout;
    String name, email, img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            displayUser();
        }

        tvEditprofile.setOnClickListener(this);
        tvChangepassword.setOnClickListener(this);
//        tvStatistics.setOnClickListener(this);
        tvLogout.setOnClickListener(this);

        return view;
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == tvEditprofile) {
            startActivity(new Intent(getActivity(), EditProfileActivity.class));
        }
        if (view == tvChangepassword) {
            startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
        }
//        if (view == tvStatistics) {
//            startActivity(new Intent(getActivity(), StatisticsActivity.class));
//        }
        if (view == tvLogout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            Toast.makeText(getActivity(), "See you later", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView(View view) {
        img_profile = view.findViewById(R.id.img_profile);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvEditprofile = view.findViewById(R.id.tvEditprofile);
        tvChangepassword = view.findViewById(R.id.tvChangepassword);
//        tvStatistics = view.findViewById(R.id.tvStatistics);
        tvLogout = view.findViewById(R.id.tvLogout);
    }
}
