package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanfood.model.Bill;
import com.example.hanfood.model.Comment;
import com.example.hanfood.model.Food;
import com.example.hanfood.model.ItemFood;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class EvaluateActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;

    TextView tvName;
    EditText edComment;
    ImageView imgFood;
    RatingBar ratingBar;
    Button btSend;
    ArrayList<Comment> commentArrayList;
    FirebaseUser auth;
    DatabaseReference myRef, myCmt;
    String idOrder = "", idFood = "";
    String img, name, content;
    String TAG = "Comment";
    float rate = 0, avgRate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        initView();

        idOrder = getIntent().getStringExtra("idOrder");
        idFood = getIntent().getStringExtra("idFood");

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(EvaluateActivity.this, DetailBillActivity.class);
//                i.putExtra("idBill", idBill);
//                startActivity(i);
                finish();
            }
        });

        displayFood();

        btSend.setOnClickListener(this);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float r, boolean b) {
                rate = r;
//                Toast.makeText(EvaluateActivity.this, "rate " + r, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayFood() {
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    if (food.getIdFood().equalsIgnoreCase(idFood)) {
                        name = food.getNameFood();
                        img = food.getImageFood();
                    }
                }
                Picasso.get().load(img).into(imgFood);
                tvName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == btSend) {
            if (rate == 0 || edComment.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(this, "Vui lòng điền đủ thông tin để đánh giá!", Toast.LENGTH_SHORT).show();
            } else {
                String saveCurrentTime, savecurrentDate, savecurrentDate2, randomKey;
                Calendar c = Calendar.getInstance();
                SimpleDateFormat curDate = new SimpleDateFormat("dd-MM-yyyy");
                savecurrentDate = curDate.format(c.getTime());
                SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
                saveCurrentTime = curTime.format(c.getTime());
                SimpleDateFormat curDate2 = new SimpleDateFormat("yyyy-MM-dd");
                savecurrentDate2 = curDate2.format(c.getTime());
                randomKey = savecurrentDate2 + "-" + saveCurrentTime;

                HashMap<String, Object> commnent = new HashMap<>();
                commnent.put("idComment", TAG + randomKey);
                commnent.put("idUser", auth.getUid());
                commnent.put("idFood", idFood);
                commnent.put("content", edComment.getText().toString());
                commnent.put("currentTime", saveCurrentTime);
                commnent.put("currentDate", savecurrentDate);
                commnent.put("rate", rate);
                myCmt = FirebaseDatabase.getInstance().getReference("Comment/" + idFood);
                myCmt.child(TAG + randomKey).updateChildren(commnent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EvaluateActivity.this, "Bạn đã đánh giá món ăn!", Toast.LENGTH_SHORT).show();
                        updateRateFood();
                        updateEvaluate();
                        finish();
                    }
                });
            }
        }
    }

    //cap nhat so sao cua mon an sau khi danh gia
    private void updateRateFood() {
        myCmt = FirebaseDatabase.getInstance().getReference("Comment/" + idFood);
        myCmt.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentArrayList = new ArrayList<>();
                float r = 0;
                for (DataSnapshot data : snapshot.getChildren()) {
                    Comment comment = data.getValue(Comment.class);
                    if (comment.getIdFood().equalsIgnoreCase(idFood)) {
                        commentArrayList.add(comment);
                        r += comment.getRate();
                    }
                }
                avgRate = r / commentArrayList.size();
//                System.out.println("rateeee tb " + avgRate);
//                System.out.println("sizeee " + commentArrayList.size());


                HashMap<String, Object> food = new HashMap<>();
                food.put("rate", avgRate);
                myRef = FirebaseDatabase.getInstance().getReference("Food/" + idFood);
                myRef.child("").updateChildren(food).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Bạn đã update Rate!");
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //cap nhat trang thai cho san pham trong bill khi da danh gia
    private void updateEvaluate() {
        myRef = FirebaseDatabase.getInstance().getReference("Bill/" + auth.getUid());
        myRef.child("").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Bill bill = data.getValue(Bill.class);
                    if (bill.getIdOrder().equalsIgnoreCase(idOrder)) {
                        for (ItemFood itemFood : bill.getItemFoodArrayList()) {
                            //danh gia thanh cong -> setEvaluate true
                            if (itemFood.getIdFood().equalsIgnoreCase(idFood)) {
                                itemFood.setEvaluate(true);
                                myRef.child(idOrder).setValue(bill);
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        imgFood = findViewById(R.id.imgFood);
        tvName = findViewById(R.id.tvName);
        edComment = findViewById(R.id.edComment);
        ratingBar = findViewById(R.id.ratingBar);
        btSend = findViewById(R.id.btSend);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Food/" + idFood);

    }
}
