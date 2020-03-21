package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Gallery extends AppCompatActivity {

     String key;
    FirebaseAuth mAuth;
    DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        getIncomingIntents();
    }

    public void getIncomingIntents(){
        if(getIntent().hasExtra("name") && getIntent().hasExtra("age") && getIntent().hasExtra("bio")
                ||getIntent().hasExtra("babysitterKey")){
            String name = getIntent().getStringExtra("name");
            String age = getIntent().getStringExtra("age");
            String bio = getIntent().getStringExtra("bio");
            key = getIntent().getStringExtra("babysitterKey");
            setUI(name,age,bio,key);

        }

    }

    private void setUI(final String name, String age, String bio, final String key){
        TextView nameV = findViewById(R.id.nameView);
        nameV.setText(name);
        TextView ageV = findViewById(R.id.ageView);
        ageV.setText(age);
        TextView bioV = findViewById(R.id.bioView);
        bioV.setText(bio);
        Button request = findViewById(R.id.request_btn);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Gallery.this,ChildSchedule.class);
                intent.putExtra("babysitterKey", key);
                startActivity(intent);

                /*String motherId = mAuth.getCurrentUser().getUid();
                final String id;
                Query childId = mReference.child("babies").orderByChild("motherID").equalTo(motherId);
                childId.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot babySnapshot : dataSnapshot.getChildren()){
                             key = babySnapshot.getKey();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                OrderModel model = new OrderModel();
                model.setBabyID(CreateOrder.babyKey);
                List<ScheduleModel> scheduleList =new ArrayList<>();
                scheduleList.add(new ScheduleModel("54","43","43"));
                scheduleList.add(new ScheduleModel("54","43","43"));
                scheduleList.add(new ScheduleModel("54","43","43"));

                String i = "5";
                int j = Integer.parseInt(i);

                String s= String.valueOf(j);

                model.setScheduleList(scheduleList);

            }*/
            }
        });


    }



}

