package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maha.uds.Model.DailyReportModel;
import com.maha.uds.Model.OrderModel;
import com.maha.uds.Model.ScheduleModel;

import java.util.ArrayList;
import java.util.List;

public class ChildSchedule extends AppCompatActivity {

    Button addSchedule;
    Button finishOrder;
    EditText dayText;
    Spinner timeText;
    EditText dateText;
    ListView scheduleListView;
    String babysitterKey;
    List<ScheduleModel> ScheduleList;
    ScheduleModel schedule;
    static String orderKey = "";
    ScheduleAdapter mAdapter;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    int totalHours;
    int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_schedule);



        setIntent();
        setUI();
        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = dayText.getText().toString();
                String time = timeText.getSelectedItem().toString();
                String date = dateText.getText().toString();

                if (TextUtils.isEmpty(day) || TextUtils.isEmpty(date)) {
                    Toast.makeText(ChildSchedule.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    schedule = new ScheduleModel(day, date, time);
                    ScheduleList.add(schedule);
                    mAdapter = new ScheduleAdapter(ChildSchedule.this, ScheduleList);
                    scheduleListView.setAdapter(mAdapter);
                }

                int hours = 0;
                for(ScheduleModel scheduleTime: ScheduleList){
                    hours = hours + Integer.parseInt(scheduleTime.getTime());
                    totalHours= hours;
                    totalPrice = totalHours*20;
            }


            }
        });

        finishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderModel orderModel = new OrderModel();
                String motherID = mAuth.getCurrentUser().getUid();
                String babyID = CreateOrder.babyKey;
                orderModel.setMotherID(motherID);
                orderModel.setBabyID(babyID);
                orderModel.setBabysitterID(babysitterKey);
                orderModel.setScheduleList(ScheduleList);
                orderModel.setChatID("");
                String arriveTime = "";
                String leaveTime = "";
                String napTime = "";
                String mealReport = "";
                String notes = "";
                DailyReportModel report = new DailyReportModel(arriveTime,leaveTime,napTime,mealReport,notes);
                orderModel.setDailyReport(report);
                orderModel.setOrderStatus("pending");
                orderModel.setPaymentStatus("not Paid");
                orderModel.setPrice(totalPrice);
                orderModel.setTotalHours(totalHours);
                orderKey = mReference.child("orders").push().getKey();
                mReference.child("orders").child(orderKey).setValue(orderModel);
                mReference.child("accounts").child(babysitterKey).child("status").setValue("pending");
                Toast.makeText(ChildSchedule.this, "Order Created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChildSchedule.this,MotherHome.class);
                intent.putExtra("orderKey",orderKey);
                startActivity(intent);

            }
        });
    }
    public void setIntent(){
        if(getIntent().hasExtra("babysitterKey")){
        babysitterKey = getIntent().getStringExtra("babysitterKey");}
    }

    public void setUI(){
        mReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        addSchedule = findViewById(R.id.add_btn);
        finishOrder = findViewById(R.id.finish_btn);
        scheduleListView = findViewById(R.id.Schedule_list);
        ScheduleList = new ArrayList<>();
        dayText = findViewById(R.id.dayEditText);
        timeText = findViewById(R.id.timeSpinner);
        dateText = findViewById(R.id.dateEditText);

    }


    }








