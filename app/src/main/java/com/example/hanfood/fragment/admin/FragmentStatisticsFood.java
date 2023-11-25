package com.example.hanfood.fragment.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hanfood.R;
import com.example.hanfood.model.Food;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentStatisticsFood extends Fragment {
    DatabaseReference myRef;
    BarChart barChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        statisticByFood();
    }

    private void statisticByFood() {
        myRef = FirebaseDatabase.getInstance().getReference("Food");
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> foodList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    foodList.add(food);
                }
                drawBarChart(foodList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawBarChart(List<Food> foodList) {
        Description description = new Description();
        description.setText("Thống kê số lượng");
        barChart.setDescription(description);

        List<BarEntry> barEntriesSold = new ArrayList<>();
        List<BarEntry> barEntriesRemaining = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < foodList.size(); i++) {
            Food food = foodList.get(i);
            barEntriesSold.add(new BarEntry(i, food.getQuantityFoodSold()));
            barEntriesRemaining.add(new BarEntry(i, food.getQuantityFood()));
            labels.add(food.getNameFood());
        }

        BarDataSet barDataSetSold = new BarDataSet(barEntriesSold, "Số sản phẩm đã bán");
        barDataSetSold.setColor(Color.rgb(255, 102, 0));

        BarDataSet barDataSetRemaining = new BarDataSet(barEntriesRemaining, "Số lượng món ăn còn lại");
        barDataSetRemaining.setColor(Color.rgb(76, 165, 237));

        BarData barData = new BarData(barDataSetRemaining, barDataSetSold);

        barChart.setData(barData);
// Cấu hình trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(90f); // Đặt góc quay nhãn thành 0 để nhãn thẳng hàng với cột

// Cấu hình trục Y
        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setGranularity(1f);

// Tính toán khoảng trống giữa các nhóm và cột trong mỗi nhóm
        int numberOfGroups = foodList.size(); // Số nhóm
        int numberOfColumns = 2; // Số cột trong mỗi nhóm
        float groupSpace = 0.2f; // Khoảng trống giữa các nhóm
        float barSpace = 0f; // Khoảng trống giữa các cột trong một nhóm
        float barWidth = (1f - groupSpace) / numberOfColumns - barSpace; // Độ rộng của cột

        barData.setBarWidth(barWidth);
        barChart.groupBars(-0.5f, groupSpace, barSpace);

        barChart.invalidate();
    }

    private void initView(View view) {
        barChart = view.findViewById(R.id.barChart);
    }
}
