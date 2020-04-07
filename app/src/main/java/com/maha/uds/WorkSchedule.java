package com.maha.uds;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Model.ScheduleModel;

import java.util.ArrayList;
import java.util.List;

public class WorkSchedule extends AppCompatActivity {

    List<ScheduleModel> mList;
    List<String> keyList;
    private RecyclerView mListView;
    private WorkScheduleAdapter adapter;
    FirebaseAuth mAuth;
    Button finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_schedule);

        finish = findViewById(R.id.finishBtn);
        mListView = findViewById(R.id.listView);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        keyList= new ArrayList<>();
        fillListView();



        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlertDialog();
            }
        });
    }




    public void fillListView(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance().getReference("orders")
                .child(BabysitterHome.mOrderKey).child("scheduleList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()){
                    ScheduleModel workSchedule = scheduleSnapshot.getValue(ScheduleModel.class);
                    mList.add(workSchedule);
                    keyList.add(scheduleSnapshot.getKey());

                }
                adapter = new WorkScheduleAdapter(mList,keyList,WorkSchedule.this);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteAlertDialog (){

        final AlertDialog.Builder builder = new AlertDialog.Builder(WorkSchedule.this);
        LayoutInflater inflater = getLayoutInflater();
        View AlertView = inflater.inflate(R.layout.delete_dialog_layout, null);
        Button yes = AlertView.findViewById(R.id.yesBtn);
        Button no = AlertView.findViewById(R.id.noBtn);

        final DatabaseReference mRef = FirebaseDatabase
                .getInstance().getReference("orders").child(BabysitterHome.mOrderKey);

        builder.setView(AlertView).setTitle("Terminate order");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.child("orderStatus").setValue("ratting");
                alertDialog.dismiss();

            }
        });
      no.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              alertDialog.dismiss();
          }
      });

    }
}


