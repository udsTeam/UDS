package com.maha.uds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Chat.FirebaseManager;
import com.maha.uds.Model.OrderModel;
import com.maha.uds.Model.ScheduleModel;

import java.util.ArrayList;
import java.util.List;

public class BabyInformation extends AppCompatActivity {

    String name;
    String age;
    String notes;
    String gender;
    TextView nameView;
    TextView ageView;
    TextView notesView;
    TextView bio;
    String orderID;
    Button acceptBtn;
    Button rejectBtn;
    FirebaseAuth mAuth;
    List<ScheduleModel> mScheduleList;
    private ProgressDialog mProgressDialog;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_information);

        mProgressDialog = new ProgressDialog(this); // declare progress dialog
        mAuth = FirebaseAuth.getInstance(); // declare firebase Auth
        getIncomingIntents(); // calling method
        mScheduleList = new ArrayList<>(); // declare array list
    }

    // getting data from intent
    public void getIncomingIntents() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("age")
                && getIntent().hasExtra("notes")&& getIntent().hasExtra("gender") ) {
            name = getIntent().getStringExtra("name");
            age = getIntent().getStringExtra("age");
            notes = getIntent().getStringExtra("notes");
            gender = getIntent().getStringExtra("gender");
            orderID = getIntent().getStringExtra("OrderID");
            setUI(name,age,notes,gender);
        }
    }

    // setting user interface
    private void setUI(String name, String age, String notes,String gender) {
        //declare TextViews
        nameView= findViewById(R.id.nameView);
        ageView = findViewById(R.id.ageView);
        notesView = findViewById(R.id.notesView);
        bio = findViewById(R.id.bioView);
        //declare ListView
        mListView = findViewById(R.id.listView);
        //declare Buttons
        acceptBtn = findViewById(R.id.accept_btn);
        rejectBtn = findViewById(R.id.reject_btn);


        nameView.setText(name);// setting name in the name view
        ageView.setText(age);// setting age in the age view
        notesView.setText(notes);// setting notes in the notes view
        bio.setText(gender);// setting gender in the gender view

        //firebase listener to retrieve data from database and display them in a listView
        FirebaseDatabase.getInstance().getReference("orders").child(orderID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderModel order = dataSnapshot.getValue(OrderModel.class); // create an object from OrderModel class
                mScheduleList = order.getScheduleList(); // initialize schedule list "ArrayList"

                //create adapter and declare it "scheduleAdapter"
                ScheduleAdapter adapter = new ScheduleAdapter(BabyInformation.this, mScheduleList);
                // use the adapter to display data in a listView
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //set onClick listener to accept button
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setMessage("Accepting.."); //set message to the progress dialog
                mProgressDialog.show();
                //Declaring database reference to the account tree then current user Id
                DatabaseReference babysitterStatusRef = FirebaseDatabase.getInstance()
                        .getReference("accounts").child(mAuth.getCurrentUser().getUid());
                // update value for status for the current user
                babysitterStatusRef.child("status").setValue("pending");

                //Declaring database reference to the order tree then selected orderId
                DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference("orders").child(orderID);

                // update value for orderStatus for the selected order
                orderStatusRef.child("orderStatus").setValue("accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //if the operation succeed call method "rejectOtherOrders"
                        rejectOtherOrders();
                    }
                });
            }
        });

        //set onClick listener to reject button
        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setMessage("Rejecting..");
                mProgressDialog.show();
                DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference("orders").child(orderID);
                orderStatusRef.child("orderStatus").setValue("rejected"); // update value for orderStatus for the selected order
                orderStatusRef.child("babysitterID").setValue("");// update value for babysitterID for the selected order

                startActivity(new Intent(BabyInformation.this,OrdersList.class)); // using intent to go back to orders page

            }
        });mProgressDialog.dismiss(); // close progress dialog

    }



    // method to reject other orders
    private void rejectOtherOrders(){
       //firebase listener to update data
        FirebaseDatabase.getInstance().getReference("orders")
                .orderByChild("babysitterID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()) // to get current user
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //progress dialog dissmis
                        mProgressDialog.dismiss();
                        if(dataSnapshot.exists()){
                            //for loop to retrieve all orders for te current babysitter
                            for(DataSnapshot mSnapshot : dataSnapshot.getChildren()){
                                OrderModel mOrderModel = mSnapshot.getValue(OrderModel.class);
                                String key = mSnapshot.getKey(); // get order key
                                // if the order status equals pending
                                if(mOrderModel.getOrderStatus().equals("pending")){
                                    mOrderModel.setOrderStatus("rejected");//set orderStatus to rejected
                                    mOrderModel.setBabysitterID("");// set babysitterID to Empty string

                                    FirebaseDatabase.getInstance().getReference("orders")
                                            .child(key).setValue(mOrderModel); // write the new values to the database at this path
                                }
                            }
                        }

                        startActivity(new Intent(BabyInformation.this,BabysitterHome.class)); // go to home page

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });
    }

}
