package com.maha.uds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
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

public class BabysitterHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView nameView;
    private String displayName;
    DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.babysitter_home);
        setupDisplayName();

        mDatabase = FirebaseDatabase.getInstance().getReference("babysitter");
        setupFirebaseListener();
        setUI();
        mNavigationView.setNavigationItemSelectedListener(this);



    }

    private void setUI(){
        nameView = (TextView)findViewById(R.id.nameView);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToolbar=findViewById(R.id.toolbar);
        mNavigationView= findViewById(R.id.navigationView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    @Override
    public void onStart() {
        super.onStart();
        //display name will appear in the home page
        nameView.setText(displayName);
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    private void setupFirebaseListener(){
        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null){
                    user.getUid();
                }else {
                    Intent intent = new Intent(BabysitterHome.this,MainActivity.class);
                    //user cannot go th the previous activity unless he sign out
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }
    //save user name and display it
    private void setupDisplayName(){
        SharedPreferences prefs = getSharedPreferences(BabysitterRegister.CHAT_PREFS,MODE_PRIVATE);
       displayName = prefs.getString(BabysitterRegister.DISPLAY_NAME_KEY,null);
       if(displayName == null){ displayName = "Anonymous";}
    }

    //interact with the menu items
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                Toast.makeText(BabysitterHome.this, "home selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.notification:
                Toast.makeText(BabysitterHome.this, "notification selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                Toast.makeText(BabysitterHome.this, "profile selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
        }

        return false;
    }
}
