package com.maha.uds;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Model.ScheduleModel;

import java.util.ArrayList;
import java.util.List;

public class WorkSchedule extends AppCompatActivity {

    List<ScheduleModel> mList;
    ListView mListView;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_schedule);

        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("orders")
                .child(BabysitterHome.mOrderKey).child("scheduleList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()){
                    ScheduleModel workSchedule = scheduleSnapshot.getValue(ScheduleModel.class);
                    mList.add(workSchedule);

                }
                ScheduleAdapter adapter = new ScheduleAdapter(WorkSchedule.this,mList);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
