package com.maha.uds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MotherProfile extends AppCompatActivity {


    ImageButton add_btn;
    ListView scheduleList;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    List<Schedule> mSchedules;
    String displayName;
    TextView name;
    TextView deleteDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_profile);

        add_btn = findViewById(R.id.add_btn);
        scheduleList = findViewById(R.id.scheduleList);
        mReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mSchedules = new ArrayList<>();
        deleteDate = findViewById(R.id.delete_btn);

        name = findViewById(R.id.childName_text);
        setupDisplayName();

        scheduleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteAlertDialog();
                return false;
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MotherProfile.this,ChildForm.class));
            }
        });
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlertDialog();
            }
        });


    }


    private void setupDisplayName() {
        SharedPreferences prefs = getSharedPreferences(MotherRegister.CHAT_PREFS, MODE_PRIVATE);
        displayName = prefs.getString(MotherRegister.DISPLAY_USER_NAME, null);
        if (displayName == null) {
            displayName = "Anonymous";
        }
    }

        @Override
        protected void onStart () {
            super.onStart();
            name.setText(displayName);
            String userId = mAuth.getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference("schedule").child(userId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mSchedules.clear();
                            for (DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
                                Schedule schedule = scheduleSnapshot.getValue(Schedule.class);

                                mSchedules.add(schedule);

                            }
                            ScheduleAdapter adapter = new ScheduleAdapter(MotherProfile.this, mSchedules);
                            scheduleList.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

        }

        public void deleteAlertDialog (){


            final AlertDialog.Builder builder = new AlertDialog.Builder(MotherProfile.this);
            LayoutInflater inflater = getLayoutInflater();
            View AlertView = inflater.inflate(R.layout.delete_dialog_layout, null);
            final String userId = mAuth.getCurrentUser().getUid();
            final EditText date = AlertView.findViewById(R.id.dateEditText);
            Button delete = AlertView.findViewById(R.id.delete_btn);

            final DatabaseReference mRef = FirebaseDatabase
                    .getInstance().getReference("schedule").child(userId);

            builder.setView(AlertView).setTitle("Delete!!");
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String id = mRef.push().getKey();
                    mRef.child(id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    alertDialog.dismiss();

                }
            });
        }


        public void openAlertDialog () {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MotherProfile.this);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.mother_add_schedule, null);

            Button add = view.findViewById(R.id.add_btn);
            final EditText dayText = view.findViewById(R.id.dayEditText);
            final EditText timeText = view.findViewById(R.id.timeEditText);
            final EditText dateText = view.findViewById(R.id.dateEditText);
            builder.setView(view).setTitle("New Schedule");
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();


            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String day = dayText.getText().toString();
                    String time = timeText.getText().toString();
                    String date = dateText.getText().toString();
                    String userId = mAuth.getCurrentUser().getUid();
                    if (day.isEmpty() || time.isEmpty() || date.isEmpty()) {
                        Toast.makeText(MotherProfile.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                    } else {
                        Schedule schedule = new Schedule(day, date, time, userId);
                        mReference.child("schedule").child(userId).push().setValue(schedule);
                        dayText.setText(null);
                        timeText.setText(null);
                        dateText.setText(null);

                        alertDialog.dismiss();
                    }
                }
            });
        }
    }




