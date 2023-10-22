package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminNewFoodActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView imgFood, back;
    EditText nameFood, price, percentSale, desFood, quantity;
    Button btAdd;
    String name, des, priceFood, priceFoodSale;
    String idCate, nameCate = "";

    private static final String TAG = "Food";
    private String saveCurDate, saveCurTime;
    private final static int galleryPick = 1;
    int sl;
    double percent;
    private String downloadImgUrl, randomKey;

    private Uri imageUri;
    private StorageReference imgRef;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_food);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminNewFoodActivity.this, AdminFoodActivity.class);
                i.putExtra("idCate", idCate);
                i.putExtra("nameCate", nameCate);
                finish();
            }
        });

        idCate = getIntent().getStringExtra("idCate");
        nameCate = getIntent().getStringExtra("nameCate");

        imgRef = FirebaseStorage.getInstance().getReference().child("ImageFood");
        myRef = FirebaseDatabase.getInstance().getReference().child("Food");

        imgFood.setOnClickListener(this);
        btAdd.setOnClickListener(this);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        nameFood = findViewById(R.id.nameFood);
        price = findViewById(R.id.price);
        percentSale = findViewById(R.id.percentSale);
        desFood = findViewById(R.id.desFood);
        imgFood = findViewById(R.id.imgFood);
        quantity = findViewById(R.id.quantity);
        back = findViewById(R.id.back);
        btAdd = findViewById(R.id.btAdd);

    }

    @Override
    public void onClick(View view) {

        if (view == imgFood) {
            openGallery();
        }
        if (view == btAdd) {
            validateData();
        }
    }

    private void validateData() {
        name = nameFood.getText().toString().trim();
        des = desFood.getText().toString().trim();
        priceFood = price.getText().toString().trim();
        percent = Double.parseDouble(percentSale.getText().toString().trim());
        sl = Integer.parseInt(quantity.getText().toString().trim());
        if (imageUri == null) {
            Toast.makeText(this, "Chọn ảnh", Toast.LENGTH_SHORT).show();
        } else if (name.isEmpty()) {
            Toast.makeText(this, "Nhập tên", Toast.LENGTH_SHORT).show();
        } else {
            storeCategory();
        }
    }

    private void storeCategory() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat curDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurDate = curDate.format(c.getTime());
        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
        saveCurTime = curTime.format(c.getTime());
        randomKey = saveCurDate + "-" + saveCurTime;
        StorageReference imageFolder = imgRef.child(imageUri.getLastPathSegment() + randomKey + ".jpg");
        final UploadTask uploadTask = imageFolder.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminNewFoodActivity.this,
                        "error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(AdminNewCategoryActivity.this, "up anh thanh cong!", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImgUrl = imageFolder.getDownloadUrl().toString();
                        return imageFolder.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downloadImgUrl = task.getResult().toString();
//                        Toast.makeText(AdminNewCategoryActivity.this, "Luu Url anh thanh cong!", Toast.LENGTH_SHORT).show();
                        saveFoodtoDatabas();
                    }
                });
            }
        });
    }

    private void saveFoodtoDatabas() {
        HashMap<String, Object> food = new HashMap<>();
        food.put("idFood", TAG + randomKey);
        food.put("idCate", idCate);
        food.put("nameFood", name);
        food.put("desFood", des);
        food.put("priceFood", priceFood);
        food.put("percentSale", percent);
        food.put("quantityFood", sl);
        food.put("imageFood", downloadImgUrl);
        myRef.child(TAG + randomKey).updateChildren(food)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AdminNewFoodActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AdminNewFoodActivity.this, AdminFoodActivity.class);
                            i.putExtra("idCate", idCate);
                            i.putExtra("nameCate", nameCate);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(AdminNewFoodActivity.this,
                                    "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imgFood.setImageURI(imageUri);
        }
    }
}