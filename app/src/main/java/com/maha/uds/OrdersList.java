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

        mAuth = FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();
        babyList = new ArrayList<>();
        orderKeyList = new ArrayList<>();
        bulidRecyclerView();


    }

    public void bulidRecyclerView(){
        orderListView = findViewById(R.id.order_list);
        orderListView.setHasFixedSize(true);
        orderListView.setLayoutManager(new LinearLayoutManager(this));

    }
    @Override
    protected void onStart() {
        super.onStart();
       /*final String id = mAuth.getCurrentUser().getUid();
        DatabaseReference newRef=FirebaseDatabase.getInstance().getReference("accounts")
                .child(id).child("accountType");*/
       mReference.child("orders").orderByChild("babysitterID").equalTo(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            OrderModel order = orderSnapshot.getValue(OrderModel.class);
                                key = order.getBabyID();
                                babyInfo(key);
                                orderKeyList.add(orderSnapshot.getKey());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public void babyInfo(String key){
        mReference.child("babies").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BabyModel baby = dataSnapshot.getValue(BabyModel.class);
                babyList.add(baby);

                adapter = new BabyListAdapter(babyList,orderKeyList,OrdersList.this);
                orderListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })  ;
    }
}
