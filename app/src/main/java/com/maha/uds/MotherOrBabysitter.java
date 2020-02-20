package com.maha.uds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MotherOrBabysitter extends AppCompatActivity implements View.OnClickListener {


    Button mother;
    Button babysitter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_or_babysitter);

        mother = (Button)findViewById(R.id.mother_btn);
        babysitter = (Button)findViewById(R.id.babysitter_btn);

        mother.setOnClickListener(this);
        babysitter.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        if(v == mother){
            Intent intent = new Intent(MotherOrBabysitter.this,MotherRegister.class);
            finish();
            startActivity(intent);
        }else if(v == babysitter){
            Intent intent = new Intent(MotherOrBabysitter.this,BabysitterRegister.class);
            finish();
            startActivity(intent);
        }
    }
}
