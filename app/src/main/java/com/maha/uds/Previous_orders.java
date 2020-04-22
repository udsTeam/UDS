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
import com.maha.uds.Model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class Previous_orders extends AppCompatActivity {

    private RecyclerView orderListView;
    private orderListAdapter adapter;
    private DatabaseReference mReference;
    private List<OrderModel> orderList;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previous_orders);

        mAuth = FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference("orders");
        orderList = new ArrayList<>();
        bulidRecyclerView();


    }

    private void bulidRecyclerView() {
        orderListView = findViewById(R.id.recyclerView);
        orderListView.setHasFixedSize(true);
        orderListView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mReference.orderByChild("motherID").equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    OrderModel orderModel = orderSnapshot.getValue(OrderModel.class);
                        orderList.add(orderModel);
                }

                adapter = new orderListAdapter(orderList , Previous_orders.this);
                orderListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
