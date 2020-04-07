package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class BabysitterProfile extends AppCompatActivity {


    EditText Name;
    EditText Email;
    EditText phoneNumber;
    EditText age;
    EditText bio;
    Button cancelBtn;
    Button updateBtn;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.babysitter_profile);


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
                        String displayAge = accountModel.getAge();
                        String displayBio = accountModel.getBio();
                        String displayPhoneNumber = accountModel.getPhone();

                        Name.setHint(displayName);
                        Email.setHint(displayEmail);
                        phoneNumber.setHint(displayPhoneNumber);
                        age.setHint(displayAge);
                        bio.setHint(displayBio);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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
                float ratting = model.getRatting();
                AccountModel updateModel = new AccountModel(updatedEmail,accountType,updatedName,updatedBio,updatedPhoneNum,ratting,updatedAge,status);
                mDatabase.child(userID).setValue(updateModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BabysitterProfile.this, "Profile updated", Toast.LENGTH_LONG).show();

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





    public void setUpUI(){
        Name = findViewById(R.id.NameText);
        Email = findViewById(R.id.emailText);
        phoneNumber = findViewById(R.id.phoneNumberText);
        age = findViewById(R.id.ageText);
        bio = findViewById(R.id.bioText);
        cancelBtn = findViewById(R.id.cancel_btn);
        updateBtn= findViewById(R.id.Save_btn);
    }




    @Override
    protected void onStart () {
        super.onStart();



    }



}

