package com.maha.uds;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Model.BabyModel;
import com.maha.uds.Model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrdersList extends AppCompatActivity {

    private RecyclerView orderListView;
    private BabyListAdapter adapter;
    private DatabaseReference mReference;
    private List<BabyModel> babyList;
    static  List<String> orderKeyList;
    String key ;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_list);


        mAuth = FirebaseAuth.getInstance();//declare firebase Auth
        mReference= FirebaseDatabase.getInstance().getReference(); //declare database reference
        babyList = new ArrayList<>(); //declare arrayList
        orderKeyList = new ArrayList<>(); //declare arrayList
        bulidRecyclerView(); //call method


    }

    // method to set recyclerView
    public void bulidRecyclerView(){
        orderListView = findViewById(R.id.order_list); // declare
        orderListView.setHasFixedSize(true);
        orderListView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    protected void onStart() {
        super.onStart();

        // firebase listener to get babyID for all orders for the current babysitter
       mReference.child("orders").orderByChild("babysitterID").equalTo(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // loop to retrieve all orders
                        for(DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            OrderModel order = orderSnapshot.getValue(OrderModel.class); // create object
                                key = order.getBabyID(); // key variable to hold babyID
                                babyInfo(key); //call method
                                orderKeyList.add(orderSnapshot.getKey()); // get all orderIDs and add them to ArrayList
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //Method to display baby Information
    public void babyInfo(String key){
        // firebase listener to retrieve data from database
        mReference.child("babies").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                BabyModel baby = dataSnapshot.getValue(BabyModel.class); //create object
                babyList.add(baby); // add object to ArrayList

                adapter = new BabyListAdapter(babyList,orderKeyList,OrdersList.this);// declare adapter
                orderListView.setAdapter(adapter); // set adapter to recyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })  ;
    }
}
