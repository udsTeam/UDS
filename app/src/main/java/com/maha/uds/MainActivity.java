package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 3000;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        if (mFirebaseAuth.getCurrentUser()!=null) {
            Usertype();
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this,SignIn.class);
                    startActivity(intent);
                    finish();
                }

            },SPLASH_TIME_OUT);
        }


    }

    public void Usertype(){
        String userId = mFirebaseAuth.getCurrentUser().getUid();
        mReference = FirebaseDatabase.getInstance().getReference("accounts").child(userId).child("accountType");
        final String mother = "mother";
        final String babysitter = "babysitter";

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;

                if(dataSnapshot.getValue().equals(mother)){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this,MotherHome.class);
                            startActivity(intent);
                            finish();
                        }

                    },SPLASH_TIME_OUT);

                }else if (dataSnapshot.getValue().equals(babysitter)){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this,BabysitterHome.class);
                            startActivity(intent);
                            finish();
                        }

                    },SPLASH_TIME_OUT);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}












