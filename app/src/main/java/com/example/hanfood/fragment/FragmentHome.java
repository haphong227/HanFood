package com.example.hanfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hanfood.R;
import com.example.hanfood.SearchFoodActivity;
import com.example.hanfood.adapter.CategoryAdapter;
import com.example.hanfood.adapter.FoodAdapter;
import com.example.hanfood.adapter.SliderAdapter;
import com.example.hanfood.model.Category;
import com.example.hanfood.model.Food;
import com.example.hanfood.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment implements View.OnClickListener {
    ViewPager2 viewPager;
    SliderAdapter sliderAdapter;
    EditText searchView;
    TextView tvNameUser;
    RecyclerView recyclerView_category, recyclerView_food;
    CategoryAdapter categoryAdapter;
    FoodAdapter foodAdapter;
    ArrayList<Category> dataCategory;
    ArrayList<Food> dataFood;
    ArrayList<Food> dataSlide;
    DatabaseReference myCate, myFood;

    //    Slider truot ve anh dau khi o slide cuoi
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = viewPager.getCurrentItem();
            if (currentPosition == dataCategory.size() - 1) {
                viewPager.setCurrentItem(0, true); // Set true để có hiệu ứng chuyển đổi mượt mà
            } else {
                viewPager.setCurrentItem(currentPosition + 1, true);
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);


        searchView.setOnClickListener(this);
        displaySlider();
        displayListCategory();
        displayListFood();
        displayNameUser();
    }

    private void displayListCategory() {

        GridLayoutManager manager_category = new GridLayoutManager(getContext(), 4);
        recyclerView_category.setLayoutManager(manager_category);

        myCate = FirebaseDatabase.getInstance().getReference("Category");
        myCate.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataCategory = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Category category = data.getValue(Category.class);
                    dataCategory.add(category);
//                    System.out.println(category.getName()+ ","+category.getImage()+"\n");
                }
                categoryAdapter = new CategoryAdapter(dataCategory, getContext());
                recyclerView_category.setAdapter(categoryAdapter);
                recyclerView_category.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displayListFood() {
        GridLayoutManager manager_food = new GridLayoutManager(getContext(), 2);
        recyclerView_food.setLayoutManager(manager_food);

        myFood = FirebaseDatabase.getInstance().getReference("Food");
        myFood.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataFood = new ArrayList<>();
                dataSlide = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    dataFood.add(food);
                    if (food.getPercentSale() != 0) {
                        dataSlide.add(food);
                    }

//                    System.out.println(food.getName()+ ","+food.getPrice()+","+food.getImage()+"\n");
                }
                foodAdapter = new FoodAdapter(dataFood, getContext());
                recyclerView_food.setAdapter(foodAdapter);
                recyclerView_food.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void displaySlider() {
//              3 slide
        viewPager.setOffscreenPageLimit(3);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            public void transformPage(View page, float positon) {
                float r = 1 - Math.abs(positon);
                page.setScaleY(0.65f + r * 0.1f);
            }

        });
        viewPager.setPageTransformer(compositePageTransformer);
        List<String> imageUrls = getSampleImageUrls();
//        set adapter cho slider
        sliderAdapter = new SliderAdapter(imageUrls);
        viewPager.setAdapter(sliderAdapter);
//                tu chuyen slide sau 3s
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000);
                // Kiểm tra nếu đang ở vị trí cuối cùng, quay lại slide đầu
                if (position == sliderAdapter.getItemCount() - 1) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem(0);
                        }
                    }, 3000); // Đặt lại sau 3 giây
                }
            }
        });

    }

    private List<String> getSampleImageUrls() {
        List<String> imageUrls = new ArrayList<>();
        // Thêm đường dẫn ảnh của bạn vào đây
        imageUrls.add("https://i.pinimg.com/564x/85/0f/a2/850fa21a46ac10029572017242e1b380.jpg");
        imageUrls.add("https://i.pinimg.com/564x/dc/6c/bd/dc6cbd2bc4684f8a9e7f111068b41fed.jpg");
        imageUrls.add("https://i.pinimg.com/564x/76/87/57/76875796327ca3810a00f9fb83c096dd.jpg");
        imageUrls.add("https://i.pinimg.com/564x/6b/f7/42/6bf742ccd13c694c57d383f4e920aca2.jpg");
        imageUrls.add("https://i.pinimg.com/564x/3f/18/b3/3f18b365ad965b4f432968e0a071e5b6.jpg");
        imageUrls.add("https://i.pinimg.com/564x/bd/dd/7f/bddd7ffc2b306c8008a3762aa3c1568b.jpg");
        imageUrls.add("https://i.pinimg.com/564x/c1/87/4f/c1874fc52efbbc91fb9074a6758ef6f7.jpg");
        return imageUrls;
    }

    private void displayNameUser() {
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(auth.getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    String userName = user.getName();
                    Log.d("UserName", userName);
                    tvNameUser.setText("Chào " + userName + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi truy vấn
                Log.e("FirebaseError", "Error fetching user data", databaseError.toException());
            }
        });
    }

    private void initView(View view) {
        searchView = view.findViewById(R.id.eSearch);
        recyclerView_category = view.findViewById(R.id.recycleView_category);
        recyclerView_food = view.findViewById(R.id.recycleView_food);
        viewPager = view.findViewById(R.id.viewPager);
        tvNameUser = view.findViewById(R.id.tvNameUser);
    }

    @Override
    public void onClick(View view) {
        if (view == searchView) {
            startActivity(new Intent(getActivity(), SearchFoodActivity.class));
        }
    }
}
