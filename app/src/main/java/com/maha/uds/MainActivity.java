package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth mFirebaseAuth;
    DatabaseReference mReference;
    Button signIn;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        signIn = findViewById(R.id.signIn_btn);
        signUp = findViewById(R.id.signUp_btn);

        //setup listener to sign in button
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                finish();
                startActivity(intent);
            }
        });
        //setup listener to sign in button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MotherOrBabysitter.class);
                finish();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        String userId = mFirebaseAuth.getCurrentUser().getUid();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        Query mQuery2 = FirebaseDatabase.getInstance().getReference("mother")
                .orderByChild("userId").equalTo(userId);
        if (firebaseUser != null) {
            mQuery2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //go to mother page
                        startActivity(new Intent(MainActivity.this, MotherHome.class));
                    } else {
                        // go baby setter page
                        startActivity(new Intent(MainActivity.this, BabysitterHome.class));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}












