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

public class AdminNewCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView imgCategory;
    EditText nameCategory;
    Button btAdd;
    String name;
    private Uri imageUri;
    private StorageReference imgRef;
    private DatabaseReference myRef;
    private static final String TAG = "Cate";
    private String saveCurDate, saveCurTime;
    private final static int galleryPick = 1;
    private String downloadImgUrl, randomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_category);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminNewCategoryActivity.this, AdminMainActivity.class));
                finish();
            }
        });

        imgCategory.setOnClickListener(this);
        btAdd.setOnClickListener(this);

    }

    private void initView() {
        nameCategory = findViewById(R.id.nameCategory);
        imgCategory = findViewById(R.id.imgCategory);
        toolbar = findViewById(R.id.toolbar);
        btAdd = findViewById(R.id.btAdd);
        imgRef = FirebaseStorage.getInstance().getReference().child("ImageCategory");
        myRef = FirebaseDatabase.getInstance().getReference().child("Category");
    }

    @Override
    public void onClick(View view) {
        if (view == imgCategory) {
            openGallery();
        }
        if (view == btAdd) {
            validateData();
        }

    }

    private void validateData() {
        name = nameCategory.getText().toString().trim();
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
                Toast.makeText(AdminNewCategoryActivity.this,
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
                        saveCategorytoDatabas();
                    }
                });
            }
        });
    }

    private void saveCategorytoDatabas() {
        HashMap<String, Object> category = new HashMap<>();
        category.put("idCate", TAG + randomKey);
        category.put("nameCate", name);
        category.put("imageCate", downloadImgUrl);
        myRef.child(TAG + randomKey).updateChildren(category)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

//                            Intent intent1 = new Intent(AdminNewCategoryActivity.this, AdminCategoryActivity.class);
//                            startActivity(intent1);
                            finish();
                            Toast.makeText(AdminNewCategoryActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AdminNewCategoryActivity.this,
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
            imgCategory.setImageURI(imageUri);
        }
    }
}