package com.example.hanfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hanfood.model.Bill;
import com.example.hanfood.model.Food;
import com.example.hanfood.model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class StatisticsActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    TextView titlePage;
    TextView toDate, fromDate;
    Button btStatistics;
    DatabaseReference myRef, myBill, myUser;
    CombinedChart combinedChart;
    String toD = "", fromD = "";
    Date to, from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        initView();

        setSupportActionBar(toolbar);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawable);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StatisticsActivity.this, AdminMainActivity.class));
                finish();
            }
        });

        statistis();
        toDate.setOnClickListener(this);
        fromDate.setOnClickListener(this);
        btStatistics.setOnClickListener(this);
    }

    private void statistisByDate() {
        myUser = FirebaseDatabase.getInstance().getReference("User");
        myUser.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    String idUser = user.getIdUser();
                    try {
                        addBill(idUser); //lay danh sach bill theo user
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void statistis() {
        myUser = FirebaseDatabase.getInstance().getReference("User");
        myUser.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    String idUser = user.getIdUser();
                    addAllBill(idUser); //lay danh sach bill theo user
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void addBill(String idUser) throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        to = dateFormat.parse(toD);
//        from = dateFormat.parse(fromD);
//
//        System.out.println(to);
//        System.out.println(from);

        myBill = FirebaseDatabase.getInstance().getReference("Bill/" + idUser);
        myBill.orderByChild("currentDate").startAt(toD).endAt(fromD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Bill> billList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Bill bill = data.getValue(Bill.class);
                    billList.add(bill);
                }
                billChart(billList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAllBill(String idUser) {

        myBill = FirebaseDatabase.getInstance().getReference("Bill/" + idUser);
        myBill.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Bill> billList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Bill bill = data.getValue(Bill.class);
                    billList.add(bill);
                }
                billChart(billList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void billChart(List<Bill> billList) {
        Description description = new Description();
        description.setText("Thống kê doanh thu theo ngày");
        combinedChart.setDescription(description);

        List<Entry> lineEntries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < billList.size(); i++) {
            Bill bill = billList.get(i);
            lineEntries.add(new Entry(i, (float) bill.getPrice()));
            labels.add(bill.getCurrentDate());
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Doanh thu theo ngày");
        lineDataSet.setLineWidth(2.0f);

        LineData lineData = new LineData(lineDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45f);

        combinedChart.setData(combinedData);
        combinedChart.setBorderColor(R.color.blue_100);
        combinedChart.invalidate();
    }

    private void statistisByFood() {
        myRef = FirebaseDatabase.getInstance().getReference("Food");
        myRef.child("").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> foodList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Food food = data.getValue(Food.class);
                    foodList.add(food);
                }
                drawCombinedChart(foodList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StatisticsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void drawCombinedChart(List<Food> foodList) {
        Description description = new Description();
        description.setText("Thống kê số lượng");
        combinedChart.setDescription(description);

        List<Entry> barEntries = new ArrayList<>();
        List<Entry> lineEntries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < foodList.size(); i++) {
            Food food = foodList.get(i);
            barEntries.add(new Entry(i, food.getQuantityFood()));
            lineEntries.add(new Entry(i, food.getQuantityFoodSold()));
            labels.add(food.getNameFood());
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Số sản phẩm đã bán");
        lineDataSet.setLineWidth(2.0f);

        LineDataSet barDataSet = new LineDataSet(barEntries, "Số sản phẩm còn lại");
        barDataSet.setLineWidth(2.0f);
        barDataSet.setColor(Color.GREEN);

        LineData lineData = new LineData(lineDataSet, barDataSet);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45f);

        combinedChart.setData(combinedData);
        combinedChart.setBorderColor(R.color.blue_100);
        combinedChart.invalidate();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        titlePage = findViewById(R.id.toolbar_title);
        combinedChart = findViewById(R.id.barChart);
        toDate = findViewById(R.id.toDate);
        fromDate = findViewById(R.id.fromDate);
        btStatistics = findViewById(R.id.btStatistics);
    }

    @Override
    public void onClick(View view) {
        if (view == btStatistics) {
            toD = toDate.getText().toString().trim();
            fromD = fromDate.getText().toString().trim();

            if (toD.isEmpty() || fromD.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ngày thống kê!", Toast.LENGTH_SHORT).show();
            } else {
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//                // Chuyển đổi chuỗi ngày thành đối tượng Date
//                Date date1 = null;
//                try {
//                    date1 = sdf.parse(toD);
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//                Date date2 = null;
//                try {
//                    date2 = sdf.parse(fromD);
//                } catch (ParseException e) {
//                    throw new RuntimeException(e);
//                }
//                // Tính khoảng cách giữa hai đối tượng Date
//                long diffInMilliseconds = Math.abs(date2.getTime() - date1.getTime());
//                if (TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS) > 30) {
//                    Toast.makeText(this, "2 ngày chỉ có thể cách nhau 30 ngày!", Toast.LENGTH_SHORT).show();
//                } else {
////                            statistisByFood();
                statistisByDate();
//                }
            }
        }
        if (view == toDate) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(StatisticsActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = "";
                    if (m > 8) {
                        if (d > 9) {
                            date = d + "-" + (m + 1) + "-" + y;
                        } else date = "0" + d + "-" + (m + 1) + "-" + y;
                    } else if (d > 9) {
                        date = d + "-0" + (m + 1) + "-" + y;
                    } else date = "0" + d + "-0" + (m + 1) + "-" + y;
                    toDate.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }
        if (view == fromDate) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(StatisticsActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = "";
                    if (m > 8) {
                        if (d > 9) {
                            date = d + "-" + (m + 1) + "-" + y;
                        } else date = "0" + d + "-" + (m + 1) + "-" + y;
                    } else if (d > 9) {
                        date = d + "-0" + (m + 1) + "-" + y;
                    } else date = "0" + d + "-0" + (m + 1) + "-" + y;
                    fromDate.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }
    }
}