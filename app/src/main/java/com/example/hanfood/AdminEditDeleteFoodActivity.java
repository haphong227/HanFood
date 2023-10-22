package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hanfood.model.Food;
import com.example.hanfood.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminEditDeleteFoodActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView imgFood, back;
    EditText nameFood, price, percentSale, desFood, quantity;
    Button btUpdate, btDelete;
    private String idFood = "";
    private DatabaseReference myRef;
    String idCate, nameCate = "";
    String name, des, priceFood, percent, sl;
    String nameF, desF, priceF, imageF, quantityF, percentF;

    private Uri imageUri;
    StorageReference imgRef;
    FirebaseStorage storage;
    private String saveCurDate, saveCurTime;
    private String randomKey;
    private final int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_delete_food);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminEditDeleteFoodActivity.this, AdminFoodActivity.class);
                i.putExtra("idCate", idCate);
                finish();
            }
        });

        idCate = getIntent().getStringExtra("idCate");
        idFood = getIntent().getStringExtra("idFood");
        myRef = FirebaseDatabase.getInstance().getReference().child("Food").child(idFood);
        displayFood();

        btUpdate.setOnClickListener(this);
        btDelete.setOnClickListener(this);
    }

    private void displayFood() {
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    nameF = snapshot.child("nameFood").getValue().toString();
                    desF = snapshot.child("desFood").getValue().toString();
                    priceF = snapshot.child("priceFood").getValue().toString();
                    percentF = snapshot.child("percentSale").getValue().toString();
                    quantityF = snapshot.child("quantityFood").getValue().toString();
                    imageF = snapshot.child("imageFood").getValue().toString();
                    System.out.println(name + "+" + des + "+" + priceFood + "+" + imageF + "q" + quantityF);

                    nameFood.setText(nameF);
                    desFood.setText(desF);
                    price.setText(priceF);
                    percentSale.setText(String.valueOf(percentF));
                    quantity.setText(quantityF);
                    Picasso.get().load(imageF).into(imgFood);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == btUpdate) {
            updateFood();
        }
        if (view == btDelete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Xóa món ăn");
            builder.setMessage("Bạn có chắc muốn xóa?");
            builder.setIcon(R.drawable.remove);
            builder.setNegativeButton("Bỏ qua", null);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteFood();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (view == imgFood) {
            selectImage();
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void deleteFood() {
        myRef.child("").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminEditDeleteFoodActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AdminEditDeleteFoodActivity.this, AdminFoodActivity.class);
                i.putExtra("idCate", idCate);
                i.putExtra("nameCate", nameCate);
                startActivity(i);
                finish();
            }
        });
    }

    private void updateFood() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat curDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurDate = curDate.format(c.getTime());
        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
        saveCurTime = curTime.format(c.getTime());
        randomKey = saveCurDate + "-" + saveCurTime;

        name = nameFood.getText().toString();
        des = desFood.getText().toString();
        priceFood = price.getText().toString();
        percent = percentSale.getText().toString();
        sl = quantity.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Không được để trống tên!", Toast.LENGTH_SHORT).show();
        } else if (des.isEmpty()) {
            Toast.makeText(this, "Không được để trống miêu tả!", Toast.LENGTH_SHORT).show();
        } else if (priceFood.isEmpty()) {
            Toast.makeText(this, "Không được để trống giá!", Toast.LENGTH_SHORT).show();
        } else if (percent.isEmpty()) {
            Toast.makeText(this, "Không được để trống % sale!", Toast.LENGTH_SHORT).show();
        } else if (sl.isEmpty()) {
            Toast.makeText(this, "Không được để trống số lượng!", Toast.LENGTH_SHORT).show();
        } else if (imageUri != null) {
            change();
        } else {
            HashMap<String, Object> food = new HashMap<>();
            food.put("nameFood", name);
            food.put("imageFood", imageF);
            food.put("desFood", des);
            food.put("priceFood", priceFood);
            food.put("percentSale", Double.parseDouble(percent));
            food.put("quantityFood", Integer.parseInt(sl));
            myRef.child("").updateChildren(food).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminEditDeleteFoodActivity.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AdminEditDeleteFoodActivity.this, AdminFoodActivity.class);
                        i.putExtra("idCate", idCate);
                        i.putExtra("nameCate", nameCate);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }


    }

    private void change() {
        StorageReference imageFolder = imgRef.child("ImageFood/" + imageUri.getLastPathSegment() + randomKey + ".jpg");
        imageFolder.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        HashMap<String, Object> food = new HashMap<>();
                        food.put("nameFood", name);
                        food.put("imageFood", url);
                        food.put("desFood", des);
                        food.put("priceFood", priceFood);
                        food.put("percentSale", Double.parseDouble(percent));
                        food.put("quantityFood", Integer.parseInt(sl));
                        myRef.child("").updateChildren(food).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AdminEditDeleteFoodActivity.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(AdminEditDeleteFoodActivity.this, AdminFoodActivity.class);
                                    i.putExtra("idCate", idCate);
                                    i.putExtra("nameCate", nameCate);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminEditDeleteFoodActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imgFood.setImageURI(imageUri);
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        nameFood = findViewById(R.id.nameFood);
        price = findViewById(R.id.price);
        percentSale = findViewById(R.id.percentSale);
        desFood = findViewById(R.id.desFood);
        imgFood = findViewById(R.id.imgFood);
        quantity = findViewById(R.id.quantity);
        btUpdate = findViewById(R.id.btUpdate);
        btDelete = findViewById(R.id.btDelete);

        storage = FirebaseStorage.getInstance();
        imgRef = storage.getReference();
    }
}