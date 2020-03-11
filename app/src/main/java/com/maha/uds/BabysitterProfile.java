package com.maha.uds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maha.uds.Model.AccountModel;

public class BabysitterProfile extends AppCompatActivity {


    EditText Name;
    EditText Email;
    EditText phoneNumber;
    EditText age;
    EditText bio;
    Button cancelBtn;
    Button updateBtn;
    String displayName;
    String displayEmail;
    String displayPhone;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    SharedPreferences prefs;
    SharedPreferences.Editor mEditor;
     String displayAge;
     String displayBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.babysitter_profile);

        setupDisplayName();
        setUpUI();
        mDatabase = FirebaseDatabase.getInstance().getReference("accounts");
        mAuth = FirebaseAuth.getInstance();



        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = mAuth.getCurrentUser().getUid();
                AccountModel model = new AccountModel();
                String updatedName = Name.getText().toString();
                String updatedEmail = Email.getText().toString();
                String updatedPhoneNum = phoneNumber.getText().toString();
                String accountType = "babysitter";
                String updatedBio = bio.getText().toString();
                String updatedAge = age.getText().toString();
                String status = model.getStatus();
                int ratting = model.getRatting();
                AccountModel updateModel = new AccountModel(updatedEmail,accountType,updatedName,updatedBio,updatedPhoneNum,ratting,updatedAge,status);
                mDatabase.child(userID).setValue(updateModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BabysitterProfile.this, "Profile updated", Toast.LENGTH_LONG).show();
                        updateInfo();
                    }
                });

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BabysitterProfile.this,BabysitterHome.class));
            }
        });

    }

    public void updateInfo(){
        mEditor = prefs.edit();
        String updatedName = Name.getText().toString();
        String updatedEmail = Email.getText().toString();
        String updatedPhoneNum = phoneNumber.getText().toString();
        String updatedAge = age.getText().toString();
        String updatedBio = bio.getText().toString();

        mEditor.putString(BabysitterRegister.DISPLAY_NAME,updatedName).apply();
        mEditor.putString(BabysitterRegister.DISPLAY_EMAIL,updatedEmail).apply();
        mEditor.putString(BabysitterRegister.DISPLAY_PHONE,updatedPhoneNum).apply();
        mEditor.putString(BabysitterRegister.DISPLAY_AGE,updatedAge).apply();
        mEditor.putString(BabysitterRegister.DISPLAY_BIO,updatedBio).apply();

        mEditor.commit();

    }

    public void setUpUI(){
        Name = findViewById(R.id.NameText);
        Email = findViewById(R.id.emailText);
        phoneNumber = findViewById(R.id.phoneNumberText);
        age = findViewById(R.id.ageText);
        bio = findViewById(R.id.bioText);
        cancelBtn = findViewById(R.id.cancel_btn);
        updateBtn= findViewById(R.id.Save_btn);
    }


    public void setupDisplayName() {
        prefs = getSharedPreferences(BabysitterRegister.CHAT_PREFS, MODE_PRIVATE);
        displayName = prefs.getString(BabysitterRegister.DISPLAY_NAME, null);
        displayEmail = prefs.getString(BabysitterRegister.DISPLAY_EMAIL,null);
        displayPhone = prefs.getString(BabysitterRegister.DISPLAY_PHONE,null);
        displayAge = prefs.getString(BabysitterRegister.DISPLAY_AGE, null);
        displayBio = prefs.getString(BabysitterRegister.DISPLAY_BIO, null);
        if (displayName == null) {
            displayName = "Anonymous";
        }
    }

    @Override
    protected void onStart () {
        super.onStart();
        Name.setText(displayName);
        Email.setText(displayEmail);
        phoneNumber.setText(displayPhone);
        bio.setText(displayBio);
        age.setText(displayAge);


    }



}

