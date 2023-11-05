package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hanfood.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;

    EditText eName, ePhone, eEmail, eSex, eBirthday;
    Button btUpdateProfile;
    CircleImageView imgprofile;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    String email, name, phone, img, pass, uID, birth;
    String emailU, nameU, phoneU, birthU, sexU;
    DatabaseReference mRef, myAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
                finish();
            }
        });

        displayUser();
        imgprofile.setOnClickListener(this);
        btUpdateProfile.setOnClickListener(this);
        eBirthday.setOnClickListener(this);
    }

    private void displayUser() {
        mRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    if (user.getIdUser().equalsIgnoreCase(uID)) {
                        System.out.println(firebaseUser.getUid() + "\nhihihihihihihihi111111111 \n");
                        name = user.getName();
                        birth = user.getBirthday();
                        email = user.getEmail();
                        phone = user.getPhone();
                        pass = user.getPassword();
                        img = user.getImage();
                        System.out.println("ảnh:     " + user.getImage());
                    }
                }
                eBirthday.setText(birth);
                eEmail.setText(email);
                eName.setText(name);
                ePhone.setText(phone);
                if (img == null) {
                    Picasso.get().load("https://vnn-imgs-a1.vgcloud.vn/image1.ictnews.vn/_Files/2020/03/17/trend-avatar-1.jpg").into(imgprofile);
                } else if (img != null) {
                    Picasso.get().load(img).into(imgprofile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == eBirthday){
            final Calendar c=Calendar.getInstance();
            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog=new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date="";
                    if (m>8){
                        date=d+"/"+(m+1)+"/"+y;
                    }else {
                        date=d+"/0"+(m+1)+"/"+y;
                    }
                    eBirthday.setText(date);
                }
            },year,month,day);
            dialog.show();
        }
        if (view == imgprofile) {
            selectImage();
        }
        if (view == btUpdateProfile) {
            emailU = eEmail.getText().toString().trim();
            phoneU = ePhone.getText().toString().trim();
            nameU = eName.getText().toString().trim();
            birthU = eBirthday.getText().toString().trim();
            if (emailU.isEmpty() || phoneU.isEmpty() || nameU.isEmpty() || birthU.isEmpty()) {
                Toast.makeText(EditProfileActivity.this, "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
            } else if (!emailU.matches("^[a-zA-Z][a-z0-9_\\.]{4,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
                Toast.makeText(EditProfileActivity.this, "Email Không Hợp Lệ", Toast.LENGTH_SHORT).show();
            } else if (phoneU.length() < 10 || phoneU.length() > 12) {
                Toast.makeText(EditProfileActivity.this, "Vui lòng nhập đúng số điện thoại!", Toast.LENGTH_SHORT).show();
            } else if (filePath != null) {
                change();
            } else {
                HashMap<String, Object> user = new HashMap<>();
                user.put("name", nameU);
                user.put("birthday", birthU);
                user.put("phone", phoneU);
                user.put("email", emailU);
                user.put("password", pass);
                user.put("image", img);
                mRef.child(uID).updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        }

    }



    private void change() {
        StorageReference imageFolder = storageReference.child("ImageProfile/" + uID + ".jpg");
        imageFolder.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        HashMap<String, Object> user = new HashMap<>();
                        user.put("name", nameU);
                        user.put("birthday", birthU);
                        user.put("phone", phoneU);
                        user.put("email", emailU);
                        user.put("password", pass);
                        user.put("image", url);
                        mRef.child(uID).updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getBaseContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
            imgprofile.setImageURI(filePath);
        }
    }


    private void initView() {
        eName = findViewById(R.id.eName);
        ePhone = findViewById(R.id.ePhone);
        eEmail = findViewById(R.id.eEmail);
//        eSex = findViewById(R.id.eSex);
        eBirthday = findViewById(R.id.eBirthday);
        imgprofile = findViewById(R.id.img);
        btUpdateProfile = findViewById(R.id.btUpdateProfile);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        toolbar = findViewById(R.id.toolbar);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uID = firebaseUser.getUid();
        mRef = FirebaseDatabase.getInstance().getReference("User");
        myAddress = FirebaseDatabase.getInstance().getReference("Address/" + uID);
    }
}