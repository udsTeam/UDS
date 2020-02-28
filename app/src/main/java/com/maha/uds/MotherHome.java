package com.maha.uds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MotherHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView nameView;
    private String displayName;
    DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    ImageView mImageView;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_home);

        setUIview();
        mDatabase = FirebaseDatabase.getInstance().getReference("mother");
        setupFirebaseListener();
        setupDisplayName();


    }
    public void setUIview(){
        nameView = (TextView) findViewById(R.id.nameView);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToolbar = findViewById(R.id.toolbar);
        mNavigationView = findViewById(R.id.navigationView);
        mImageView = (ImageView)findViewById(R.id.profile_image);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

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
            case R.id.reports:
                Toast.makeText(MotherHome.this, "home selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                startActivity(new Intent(MotherHome.this,MotherProfile.class));
                break;
            case R.id.settings:
                Toast.makeText(MotherHome.this, "settings selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                break;
        }

        return false;
    }
}
