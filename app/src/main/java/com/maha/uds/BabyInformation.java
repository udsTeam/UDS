package com.maha.uds;

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
    FirebaseAuth mAuth;
    List<ScheduleModel> mScheduleList;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baby_information);

        mAuth = FirebaseAuth.getInstance();
        getIncomingIntents();

    }

    public void getIncomingIntents() {
        if (getIntent().hasExtra("name") && getIntent().hasExtra("age")
                && getIntent().hasExtra("notes")&& getIntent().hasExtra("gender") ) {
            name = getIntent().getStringExtra("name");
            age = getIntent().getStringExtra("age");
            notes = getIntent().getStringExtra("notes");
            gender = getIntent().getStringExtra("gender");
            orderID = getIntent().getStringExtra("OrderID");
            mScheduleList = new ArrayList<>();
            setUI(name,age,notes,gender);
        }
    }

    private void setUI(String name, String age, String notes,String gender) {
        nameView= findViewById(R.id.nameView);
        ageView = findViewById(R.id.ageView);
        notesView = findViewById(R.id.notesView);
        bio = findViewById(R.id.bioView);
        mListView = findViewById(R.id.listView);
        acceptBtn = findViewById(R.id.accept_btn);
        nameView.setText(name);
        ageView.setText(age);
        notesView.setText(notes);
        bio.setText(gender);

        FirebaseDatabase.getInstance().getReference("orders").child(orderID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                OrderModel order = dataSnapshot.getValue(OrderModel.class);
                mScheduleList = order.getScheduleList();

                ScheduleAdapter adapter = new ScheduleAdapter(BabyInformation.this, mScheduleList);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference babysitterStatusRef = FirebaseDatabase.getInstance()
                        .getReference("accounts").child(mAuth.getCurrentUser().getUid());
                babysitterStatusRef.child("status").setValue("pending");


                DatabaseReference orderStatusRef = FirebaseDatabase.getInstance().getReference("orders").child(orderID);

                orderStatusRef.child("orderStatus").setValue("accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        rejectOtherOrders();
                    }
                });
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    private void rejectOtherOrders(){
        //show progress dialog
        FirebaseDatabase.getInstance().getReference("orders")
                .orderByChild("babysitterID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //progress dialog dissmis

                        if(dataSnapshot.exists()){
                            for(DataSnapshot mSnapshot : dataSnapshot.getChildren()){
                                OrderModel mOrderModel = mSnapshot.getValue(OrderModel.class);
                                String key = mSnapshot.getKey();
                                if(!mOrderModel.getOrderStatus().equals("accepted")){
                                    mOrderModel.setOrderStatus("rejected");
                                    mOrderModel.setBabysitterID("");
                                    FirebaseDatabase.getInstance().getReference("orders")
                                            .child(key).setValue(mOrderModel);
                                }
                            }
                        }

                        startActivity(new Intent(BabyInformation.this,BabysitterHome.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });
    }

}
