package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class AdminEditDeleteCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView imgCategory;
    EditText nameCategory;
    Button btUpdate, btDelete;
    private final int PICK_IMAGE_REQUEST = 22;
    private String idCate = "";
    String name, image;

    private DatabaseReference myRef;

    private Uri imageUri;
    StorageReference imgRef;
    FirebaseStorage storage;
    private String saveCurDate, saveCurTime;
    private String randomKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_delete_category);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminEditDeleteCategoryActivity.this, AdminMainActivity.class));
                finish();
            }
        });

        idCate = getIntent().getStringExtra("idCate");
        myRef = FirebaseDatabase.getInstance().getReference().
                child("Category").child(idCate);
        displayCategory();

        btUpdate.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        imgCategory.setOnClickListener(this);
    }

    private void displayCategory() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("nameCate").getValue().toString();
                    image = snapshot.child("imageCate").getValue().toString();
                    System.out.println(name + "+" + image);
                    nameCategory.setText(name);
                    Picasso.get().load(image).into(imgCategory);
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
            updateCategory();
        }
        if (view == btDelete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Xóa danh mục");
            builder.setMessage("Bạn chắc chắn muốn xóa?");
            builder.setIcon(R.drawable.remove);
            builder.setNegativeButton("Bỏ qua", null);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteCategory();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        if (view == imgCategory) {
            selectImage();
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void deleteCategory() {
        myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AdminEditDeleteCategoryActivity.this,
                        "Xóa thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminEditDeleteCategoryActivity.this,
                        AdminMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateCategory() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat curDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurDate = curDate.format(c.getTime());
        SimpleDateFormat curTime = new SimpleDateFormat("HH:mm:ss");
        saveCurTime = curTime.format(c.getTime());
        randomKey = saveCurDate + "-" + saveCurTime;

        name = nameCategory.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Không được để trống tên!", Toast.LENGTH_SHORT).show();
        } else if (imageUri != null) {
            change();
        } else {
            HashMap<String, Object> category = new HashMap<>();
            category.put("nameCate", name);
            category.put("imageCate", image);
            myRef.child("").updateChildren(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminEditDeleteCategoryActivity.this,
                                "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        System.out.println("urllllllll" + image);
                        Intent intent = new Intent(AdminEditDeleteCategoryActivity.this, AdminMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void change() {
        StorageReference imageFolder = imgRef.child("ImageCategory/" + imageUri.getLastPathSegment() + randomKey + ".jpg");

        imageFolder.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        HashMap<String, Object> category = new HashMap<>();
                        category.put("nameCate", name);
                        category.put("imageCate", url);
                        myRef.child("").updateChildren(category).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AdminEditDeleteCategoryActivity.this,
                                            "Sửa thành công!", Toast.LENGTH_SHORT).show();
                                    System.out.println("urllllllll" + url);
                                    Intent intent = new Intent(AdminEditDeleteCategoryActivity.this, AdminMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminEditDeleteCategoryActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            imgCategory.setImageURI(imageUri);
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        nameCategory = findViewById(R.id.nameCategory);
        imgCategory = findViewById(R.id.imgCategory);
        btUpdate = findViewById(R.id.btUpdate);
        btDelete = findViewById(R.id.btDelete);

        storage = FirebaseStorage.getInstance();
        imgRef = storage.getReference();
    }
}