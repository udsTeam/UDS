package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Model.AccountModel;

public class Gallery extends AppCompatActivity {

     String babysitterID;
    FirebaseAuth mAuth;
    DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_layout);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        setIntent();

        FirebaseDatabase.getInstance().getReference("accounts").child(babysitterID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AccountModel accountModel = dataSnapshot.getValue(AccountModel.class);
                        String displayName = accountModel.getName();
                        String displayAge = accountModel.getAge();
                        String displayBio = accountModel.getBio();
                        float displayRating = accountModel.getRatting();
                        setUI(displayName,displayAge,displayBio,displayRating);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void setIntent(){
        if(getIntent().hasExtra("babysitterKey")){
            babysitterID = getIntent().getStringExtra("babysitterKey");}
    }



    private void setUI(final String name, String age, String bio, final float rating){
        TextView nameV = findViewById(R.id.nameView);
        nameV.setText(name);
        TextView ageV = findViewById(R.id.ageView);
        ageV.setText(age);
        TextView bioV = findViewById(R.id.bioView);
        bioV.setText(bio);
        RatingBar mRatingBar = findViewById(R.id.ratingBar);
        mRatingBar.setRating(rating);
        Button request = findViewById(R.id.request_btn);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Gallery.this,ChildSchedule.class);
                intent.putExtra("babysitterKey", babysitterID);
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

