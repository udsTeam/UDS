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

public class MotherProfile extends AppCompatActivity {



    EditText motherName;
    EditText motherEmail;
    EditText phoneNumber;
    Button cancelBtn;
    Button updateBtn;
    String displayName;
    String displayEmail;
    String displayPhone;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    SharedPreferences prefs;
    SharedPreferences.Editor mEditor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_profile);
        setupDisplayName();
        setUpUI();
        mDatabase = FirebaseDatabase.getInstance().getReference("accounts");
        mAuth = FirebaseAuth.getInstance();



        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = mAuth.getCurrentUser().getUid();
                String updatedName = motherName.getText().toString();
                String updatedEmail = motherEmail.getText().toString();
                String updatedPhoneNum = phoneNumber.getText().toString();
                String accountType = "mother";
                String bio = null;
                String age = null;
                String status = null;
                int ratting = 0;
                AccountModel model = new AccountModel(updatedEmail,accountType,updatedName,bio,updatedPhoneNum,ratting,age,status);
                mDatabase.child(userID).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MotherProfile.this, "Profile updated", Toast.LENGTH_LONG).show();
                        updateInfo();
                    }
                });

            }
        });


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MotherProfile.this,MotherHome.class));
            }
        });

    }

    public void updateInfo(){
        mEditor = prefs.edit();
        String updatedName = motherName.getText().toString();
        String updatedEmail = motherEmail.getText().toString();
        String updatedPhoneNum = phoneNumber.getText().toString();

        mEditor.putString(MotherRegister.DISPLAY_USER_NAME,updatedName).apply();
        mEditor.putString(MotherRegister.DISPLAY_EMAIL,updatedEmail).apply();
        mEditor.putString(MotherRegister.DISPLAY_PHONE,updatedPhoneNum).apply();
        mEditor.commit();

    }

    public void setUpUI(){
        motherName = findViewById(R.id.motherNameText);
        motherEmail = findViewById(R.id.emailText);
        phoneNumber = findViewById(R.id.phoneNumberText);
        cancelBtn = findViewById(R.id.cancel_btn);
        updateBtn= findViewById(R.id.Save_btn);
    }


    public void setupDisplayName() {
        prefs = getSharedPreferences(MotherRegister.CHAT_PREFS, MODE_PRIVATE);
        displayName = prefs.getString(MotherRegister.DISPLAY_USER_NAME, null);
        displayEmail = prefs.getString(MotherRegister.DISPLAY_EMAIL,null);
        displayPhone = prefs.getString(MotherRegister.DISPLAY_PHONE,null);
        if (displayName == null) {
            displayName = "Anonymous";
        }
    }

        @Override
        protected void onStart () {
            super.onStart();
            motherName.setText(displayName);
            motherEmail.setText(displayEmail);
            phoneNumber.setText(displayPhone);

        }






    }




