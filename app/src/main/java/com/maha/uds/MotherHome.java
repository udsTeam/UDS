package com.maha.uds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MotherHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    TextView nameView;
    private String displayName;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    BottomNavigationView mNavigationView;
    private Button orderBtn;
    private Button trackBtn;
    private Button paymentBtn;
    private Button reportBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_home);

        setUIview();
        setupFirebaseListener();
        setupDisplayName();

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MotherHome.this,CreateOrder.class));
            }
        });

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MotherHome.this,ListOfBabysitter.class));            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MotherHome.this, "3", Toast.LENGTH_SHORT).show();
            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MotherHome.this, "4", Toast.LENGTH_SHORT).show();
            }
        });





    }
    public void setUIview(){
        nameView = findViewById(R.id.name_view);
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setOnNavigationItemSelectedListener(this);
        orderBtn = findViewById(R.id.createOrder_btn);
        trackBtn = findViewById(R.id.trackOrder_btn);
        paymentBtn = findViewById(R.id.payments_btn);
        reportBtn = findViewById(R.id.dailyReports_btn);



    }

    @Override
    public void onStart() {
        super.onStart();
        nameView.setText(displayName);
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    private void setupFirebaseListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    user.getUid();
                } else {
                    Intent intent = new Intent(MotherHome.this, SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    private void setupDisplayName() {
        SharedPreferences prefs = getSharedPreferences(MotherRegister.CHAT_PREFS, MODE_PRIVATE);
        displayName = prefs.getString(MotherRegister.DISPLAY_USER_NAME, null);
        if (displayName == null) {
            displayName = "Anonymous";
        }
    }
    public void pickImageFromGallery(){

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(MotherHome.this,MotherProfile.class));
                break;
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                break;
        }

        return false;
    }
}
