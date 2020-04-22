package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Model.AccountModel;

public class MotherProfile extends AppCompatActivity {



    EditText motherName;
    EditText motherEmail;
    EditText phoneNumber;
    Button cancelBtn;
    Button updateBtn;
    Button history;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_profile);
        setUpUI();
        mDatabase = FirebaseDatabase.getInstance().getReference("accounts");
        mAuth = FirebaseAuth.getInstance();


        FirebaseDatabase.getInstance().getReference("accounts").child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AccountModel accountModel = dataSnapshot.getValue(AccountModel.class);
                        String displayName = accountModel.getName();
                        String displayEmail = accountModel.getEmail();
                        String displayPhoneNumber = accountModel.getPhone();

                        motherName.setHint(displayName);
                        motherEmail.setHint(displayEmail);
                        phoneNumber.setHint(displayPhoneNumber);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MotherProfile.this,Previous_orders.class));
            }
        });

    }



    public void setUpUI(){
        motherName = findViewById(R.id.motherNameText);
        motherEmail = findViewById(R.id.emailText);
        phoneNumber = findViewById(R.id.phoneNumberText);
        cancelBtn = findViewById(R.id.cancel_btn);
        updateBtn= findViewById(R.id.Save_btn);
        history = findViewById(R.id.previous_btn);
    }




        @Override
        protected void onStart () {
            super.onStart();


        }





    }




