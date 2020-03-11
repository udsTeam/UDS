package com.maha.uds;

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
import com.maha.uds.Model.OrderModel;
import com.maha.uds.Model.ScheduleModel;

import java.util.ArrayList;
import java.util.List;

public class ChildSchedule extends AppCompatActivity {

    Button addSchedule;
    Button next;
    EditText dayText;
    Spinner timeText;
    EditText dateText;
    ListView scheduleListView;
    List<ScheduleModel> ScheduleList;
    ScheduleModel schedule;
    ScheduleAdapter mAdapter;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    int totalHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_schedule);



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
                int hours = Integer.parseInt(time);
                for(int i = 0; i<= ScheduleList.size(); i++){
                    hours += hours;
                    totalHours = hours;
                }


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderModel orderModel = new OrderModel();
                String motherID = mAuth.getCurrentUser().getUid();
                String babyID = CreateOrder.babyKey;
                String babysitterID = Gallery.key;

                orderModel.setMotherID(motherID);
                orderModel.setBabyID(babyID);
                orderModel.setBabysitterID(babysitterID);
                orderModel.setScheduleList(ScheduleList);
                orderModel.setChatID("");
                orderModel.setDailyReportID("");
                orderModel.setOrderStatus("pending");
                orderModel.setPaymentID("");
                orderModel.setPrice(0);
                orderModel.setTotalHours(totalHours);
                String orderKey = mReference.child("orders").push().getKey();
                mReference.child("orders").child(orderKey).setValue(orderModel);
            }
        });
    }

    public void setUI(){
        mReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        addSchedule = findViewById(R.id.add_btn);
        next = findViewById(R.id.next_btn);
        scheduleListView = findViewById(R.id.Schedule_list);
        ScheduleList = new ArrayList<>();
        dayText = findViewById(R.id.dayEditText);
        timeText = findViewById(R.id.timeSpinner);
        dateText = findViewById(R.id.dateEditText);

    }


    }








