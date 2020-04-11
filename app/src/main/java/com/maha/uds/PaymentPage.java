package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Model.OrderModel;

public class PaymentPage extends AppCompatActivity {

    EditText name;
    EditText cardNumber;
    EditText month;
    EditText year;
    EditText cvc;
    RadioGroup method;
    RadioButton paymentMethod;
    RadioButton cash;
    RadioButton visa;
    String paymentStatus = "";
    TextView totalPriceView;
    TextView msgView;
    String totalPrice;
    int id;
    Button submitBtn;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_page);

        setUI();
        mDatabase = FirebaseDatabase.getInstance().getReference("orders").child(MotherHome.mOrderKey);

        mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        OrderModel orderModel = dataSnapshot.getValue(OrderModel.class);
                        if(orderModel.getOrderStatus().equals("active")) {
                            totalPrice = String.valueOf(orderModel.getPrice());
                            paymentStatus = orderModel.getPaymentStatus();

                            if (orderModel.getPaymentStatus().equals("pending")) {
                                submitBtn.setVisibility(View.GONE);
                                method.setEnabled(false);
                            } else if (orderModel.getPaymentStatus().equals("paid")) {
                                totalPriceView.setText("0");
                                method.setEnabled(false);
                                submitBtn.setVisibility(View.GONE);
                            } else {
                                submitBtn.setVisibility(View.VISIBLE);
                                totalPriceView.setText(totalPrice);
                            }
                        }else{
                            submitBtn.setVisibility(View.GONE);
                            msgView.setText("Your request still pending");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




    }

    public void visaClicked(View view){

           msgView.setVisibility(View.GONE);
            name.setEnabled(true);
            cardNumber.setEnabled(true);
            month.setEnabled(true);
            year.setEnabled(true);
            cvc.setEnabled(true);

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatabase.child("paymentStatus").setValue("paid");
                    startActivity(new Intent(PaymentPage.this,MotherHome.class));
                }
            });
    }

    public void cashClicked(View view){
        msgView.setVisibility(View.VISIBLE);
             name.setEnabled(false);
             cardNumber.setEnabled(false);
             month.setEnabled(false);
             year.setEnabled(false);
             cvc.setEnabled(false);
             submitBtn.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     mDatabase.child("paymentStatus").setValue("pending");
                     startActivity(new Intent(PaymentPage.this,MotherHome.class));
                 }
             });
    }

    public void setUI(){
        name = findViewById(R.id.nameText);
        cardNumber = findViewById(R.id.cardNumberText);
        month = findViewById(R.id.monthText);
        year = findViewById(R.id.yearText);
        cvc = findViewById(R.id.cvcText);
        method = findViewById(R.id.method);
        cash = findViewById(R.id.cash_btn);
        visa = findViewById(R.id.visa_btn);
        submitBtn = findViewById(R.id.submit);
        totalPriceView = findViewById(R.id.totalPrice);
        msgView = findViewById(R.id.msg);
        msgView.setVisibility(View.VISIBLE);
        name.setEnabled(false);
        cardNumber.setEnabled(false);
        month.setEnabled(false);
        year.setEnabled(false);
        cvc.setEnabled(false);

    }
}
