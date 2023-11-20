package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanfood.adapter.FoodAdapter;
import com.example.hanfood.model.Food;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class DetailFoodActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView img_food, sub, add;
    TextView tvPrice, tvName, tvDes, tvSl, tvQuantity, tvPriceSale, tvPercentSale, tvQuantitySold, tvComment, tvRate;
    RecyclerView recyclerView_review;
    Button btAdd;
    FoodAdapter adapter;
    ArrayList<Food> dataFood = new ArrayList<>();
    DatabaseReference myFood;
    DatabaseReference myRef;
    private FirebaseUser auth;
    String name, idFood, des, img, idCate;
    int quantity = 0, quantitySold=0;
    double priceFoodSale = 0, percentSale = 0, price = 0;
    int totalQuantity = 0;
    double totalPrice = 0;
    float rate = 0;
    ArrayList<Food> dsfoodall = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailFoodActivity.this, MainActivity.class));
                finish();
            }
        });

        idFood = getIntent().getStringExtra("idFood");

        sub.setOnClickListener(this);
        add.setOnClickListener(this);
        btAdd.setOnClickListener(this);
        tvComment.setOnClickListener(this);

        getInforFood(); //hien thong tin chi tiet mon an
        displayListFood(); //hien listfood goi y
    }

    private void getInforFood() {
        final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat.applyPattern("#,###,###,###");
        final DecimalFormat decimalFormat2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        decimalFormat2.applyPattern("#.#");

        myFood = FirebaseDatabase.getInstance().getReference("Food");
        myFood.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    if (food.getIdFood().equalsIgnoreCase(idFood)) {
                        name = food.getNameFood();
                        price = food.getPriceFood();
                        percentSale = food.getPercentSale();
                        des = food.getDesFood();
                        img = food.getImageFood();
                        idCate = food.getIdCate();
                        quantity = food.getQuantityFood();
                        rate = food.getRate();
                        quantitySold = food.getQuantityFoodSold();
                    }
                    priceFoodSale = price - percentSale * price / 100;

                    tvName.setText(name);
                    if (percentSale == 0) {
                        tvPrice.setVisibility(View.GONE);
                        tvPercentSale.setVisibility(View.GONE);
                        tvPriceSale.setText(decimalFormat.format(price) + " VNĐ");
                    } else {
                        tvPercentSale.setText("Giảm " + decimalFormat.format(percentSale) + "%");
                        tvPrice.setText(decimalFormat.format(price) + " VNĐ");
                        tvPriceSale.setText(decimalFormat.format(priceFoodSale) + " VNĐ");
                        tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); //gach ngang chu

                    }
                    tvDes.setText(des);
                    if (quantity > 0) {
                        tvQuantity.setVisibility(View.GONE);
                    } else {
                        tvQuantity.setText("SOLD OUT!");
                        tvQuantity.setTextColor(Color.RED);
                    }
                    Picasso.get().load(img).into(img_food);
                    tvRate.setText(decimalFormat2.format(rate));
                    tvQuantitySold.setText(quantitySold + " đã bán");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailFoodActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayListFood() {
        LinearLayoutManager manager = new LinearLayoutManager(DetailFoodActivity.this, RecyclerView.HORIZONTAL, false);
        recyclerView_review.setLayoutManager(manager);
        adapter = new FoodAdapter(dataFood, DetailFoodActivity.this);

        myFood.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    dsfoodall.add(food);
                    if (food.getIdCate().equalsIgnoreCase(idCate) && !food.getIdFood().equalsIgnoreCase(idFood)) {
                        dataFood.add(food);
                    }
                }
                recyclerView_review.setAdapter(adapter);
                recyclerView_review.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailFoodActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == sub) {
            if (totalQuantity > 0) {
                totalQuantity--;
                tvSl.setText(String.valueOf(totalQuantity));
                if (percentSale == 0){
                    totalPrice = price * totalQuantity;
                }else {
                    totalPrice = priceFoodSale * totalQuantity;
                }
            }
        }
        if (view == add) {
            if (totalQuantity < 10) {
                totalQuantity++;
                tvSl.setText(String.valueOf(totalQuantity));
                if (percentSale == 0){
                    totalPrice = price * totalQuantity;
                }else {
                    totalPrice = priceFoodSale * totalQuantity;
                }
            }
        }
        if (view == btAdd) {
            if (totalQuantity > 0) {
                if (quantity != 0) addToCart();
                else Toast.makeText(this, "Sản phẩm đã hết!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Vui lòng chọn số lượng sản phẩm!", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == tvComment) {
            Intent i = new Intent(DetailFoodActivity.this, CommentActivity.class);
            i.putExtra("idFood", idFood);
            startActivity(i);
        }
    }

    private void addToCart() {

        final HashMap<String, Object> itemFood = new HashMap<>();
        itemFood.put("idFood", idFood);
        itemFood.put("productImg", img);
        itemFood.put("productName", name);
        itemFood.put("productPrice", price);
        itemFood.put("productPriceSale", priceFoodSale);
        itemFood.put("totalQuantity", Integer.parseInt(tvSl.getText().toString()));
        itemFood.put("totalPrice", totalPrice);
        itemFood.put("evaluate", false);
        myRef.child(name).updateChildren(itemFood)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(DetailFoodActivity.this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetailFoodActivity.this,
                                    "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvPercentSale = findViewById(R.id.tvPercentSale);
        img_food = findViewById(R.id.img_food);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvPriceSale = findViewById(R.id.tvPriceSale);
        tvDes = findViewById(R.id.tvDes);
        btAdd = findViewById(R.id.btAdd);
        sub = findViewById(R.id.sub);
        add = findViewById(R.id.add);
        tvSl = findViewById(R.id.tvSl);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvQuantitySold = findViewById(R.id.tvQuantitySold);
        tvComment = findViewById(R.id.tvComment);
        tvRate = findViewById(R.id.tvRate);
        recyclerView_review = findViewById(R.id.recycleView_review);

        auth = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("ItemFood/" + auth.getUid());

    }
}