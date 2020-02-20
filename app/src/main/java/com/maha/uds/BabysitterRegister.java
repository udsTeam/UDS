package com.maha.uds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class BabysitterRegister extends AppCompatActivity {

     static final String CHAT_PREFS = "chatPrefs";
     static final String DISPLAY_NAME_KEY = "name";


    private EditText username_text;
    private EditText email_text;
    private EditText password_text;
    private EditText confirmPassword_text;
    private TextView signIn;
    private Button register;
    private FirebaseAuth mFirebaseAuth;
    private ProgressDialog mProgressDialog;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.babysitter_register);
        // firebase authentication !!
        mFirebaseAuth = FirebaseAuth.getInstance();
        // progress dialog for the wait time
        mProgressDialog = new ProgressDialog(this);
        // connect all the views from the XML layout
        setUIview();
        // set the actoion for the two buttons

            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(BabysitterRegister.this, SignIn.class));
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isValidate()) {
                        // get the data form the fields
                        final String userName = username_text.getText().toString().trim();
                        final String userEmail = email_text.getText().toString().trim();
                        final String userPassword = password_text.getText().toString().trim();

                        mProgressDialog.setMessage("Singing Up");
                        mProgressDialog.show();
                        mFirebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mProgressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    //adding the user information to the realtiem database
                                    UID = mFirebaseAuth.getCurrentUser().getUid();
                                    Babysitter babysitter = new Babysitter(userEmail, userName,UID);
                                    FirebaseDatabase.getInstance().getReference("babysitter").child(UID).setValue(babysitter);
                                    saveDisplayName();
                                    sendEmailVer();
                                } else {
                                    Toast.makeText(BabysitterRegister.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }



    private void setUIview() {

        username_text = (EditText) findViewById(R.id.username_text);
        email_text = (EditText) findViewById(R.id.email_text);
        password_text = (EditText) findViewById(R.id.password_text);
        confirmPassword_text = (EditText) findViewById(R.id.confirm_password_text);
        signIn = (TextView) findViewById(R.id.signIn_btn);
        register = (Button) findViewById(R.id.register_btn);
    }

    private boolean isValidate() {
        boolean cheack = false;
        // reading data from text field
        final String username = username_text.getText().toString();
        final String email = email_text.getText().toString();
        final String password = password_text.getText().toString();
        final String passCong = confirmPassword_text.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passCong)) {
            Toast.makeText(BabysitterRegister.this, "plz fill out all fields ", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(BabysitterRegister.this, "the password should be at least 6 char !!! ", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(passCong)) {
            Toast.makeText(BabysitterRegister.this, "the two password dose not match", Toast.LENGTH_SHORT).show();
        } else {
            cheack = true;
        }
        return cheack;
    }

    private void sendEmailVer() {
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(BabysitterRegister.this, "تم تسجيلك بنجاح الرجاء تفعيل الايميل", Toast.LENGTH_SHORT).show();
                        mFirebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(BabysitterRegister.this, SignIn.class));
                    } else {
                        Toast.makeText(BabysitterRegister.this, "فشل التسجيل ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
     public void saveDisplayName(){
        String displayName = username_text.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, MODE_PRIVATE);
        prefs.edit().putString(DISPLAY_NAME_KEY, displayName).apply();

    }


}

