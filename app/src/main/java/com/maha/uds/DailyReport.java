package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Model.DailyReportModel;

public class DailyReport extends AppCompatActivity {

    Button update , back;
    EditText arrive,leave,nap, mealReport,notes;
    DatabaseReference mReference;
    private FirebaseAuth mAuth;
    String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_report);

        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().getReference("orders")
                .orderByChild("babysitterID").equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    orderID = snapshot.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mReference = FirebaseDatabase.getInstance().getReference("orders") ;
        setUI();





    }

    public void setUI(){
        update = findViewById(R.id.updateBtn);
        back = findViewById(R.id.backBtn);
        arrive = findViewById(R.id.arriveTimeText);
        leave = findViewById(R.id.LeaveTimeText);
        nap = findViewById(R.id.napTimeText);
        mealReport = findViewById(R.id.mealText);
        notes = findViewById(R.id.notesText);


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String type = "babysitter";

        FirebaseDatabase.getInstance().getReference("accounts").child(userId).child("accountType")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().equals(type)){

                    arrive.setEnabled(true);
                    leave.setEnabled(true);
                    nap.setEnabled(true);
                    mealReport.setEnabled(true);
                    notes.setEnabled(true);

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            updateReportInfo();
                        }
                    });
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(DailyReport.this,BabysitterHome.class));
                        }
                    });
               }else {

                    arrive.setEnabled(false);
                    leave.setEnabled(false);
                    nap.setEnabled(false);
                    mealReport.setEnabled(false);
                    notes.setEnabled(false);

                    displayInfo();
                    update.setVisibility(View.GONE);

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(DailyReport.this,MotherHome.class));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateReportInfo(){
        String arriveTime = arrive.getText().toString();
        String leaveTime = leave.getText().toString();
        String napTime = nap.getText().toString();
        String foodReport = mealReport.getText().toString();
        String unusualNotes = notes.getText().toString();


        DailyReportModel report = new DailyReportModel(arriveTime,leaveTime,napTime,foodReport,unusualNotes);
        mReference.child(orderID).child("dailyReport").setValue(report);
    }


    public void displayInfo(){

        mReference.child(MotherHome.mOrderKey).child("dailyReport").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()) {
                        DailyReportModel model = dataSnapshot.getValue(DailyReportModel.class);
                        Log.d("DailyReportModel", model.toString());
                        arrive.setHint(model.getArriveTime());
                        leave.setHint(model.getLeavingTime());
                        nap.setHint(model.getNapTime());
                        mealReport.setHint(model.getMealReport());
                        notes.setHint(model.getUnusualNotes());

                    }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
