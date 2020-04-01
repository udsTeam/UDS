package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maha.uds.Model.BabyModel;

public class CreateOrder extends AppCompatActivity {

    EditText name;
    EditText age;
    TextInputEditText notes;
    RadioGroup gender;
    int id;
    RadioButton boy;
    RadioButton girl;
    Button next;
    public static String babyKey = "";
    FirebaseAuth mAuth;
    DatabaseReference mReference;

    TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            buttonEnable();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_order);

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        setUI();
        name.addTextChangedListener(mWatcher);
        age.addTextChangedListener(mWatcher);
        notes.addTextChangedListener(mWatcher);

        buttonEnable();



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String motherId = mAuth.getCurrentUser().getUid();
                String childName = name.getText().toString();
                String childAge = age.getText().toString();
                String childNotes = notes.getText().toString();
                babyKey = mReference.child("babies").push().getKey();
                String childGender = boy.isChecked()?"boy": girl.isChecked()?"girl":"";
                BabyModel baby = new BabyModel(childName,childGender,childAge,childNotes,motherId);
                mReference.child("babies").child(babyKey).setValue(baby);
                startActivity(new Intent(CreateOrder.this,ListOfBabysitter.class));
            }
        });

    }




    private void buttonEnable() {
        String childName = name.getText().toString();
        String childAge = age.getText().toString();
        String childNotes = notes.getText().toString();
        int id = gender.getCheckedRadioButtonId();

        if(childName.isEmpty()||childAge.isEmpty()||childNotes.isEmpty()||id==-1){
            next.setEnabled(false);
        }else {
            next.setEnabled(true);
        }
    }


    private void setUI() {
        name = findViewById(R.id.childName);
        age = findViewById(R.id.childAge);
        notes = findViewById(R.id.childNotes);
        gender = findViewById(R.id.gender);
        boy = findViewById(R.id.boyBtn);
        girl = findViewById(R.id.girlBtn);
        next = findViewById(R.id.nextBtn);

    }


}
