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
import com.maha.uds.Model.AccountModel;

import java.util.ArrayList;
import java.util.List;

public class ListOfBabysitter extends AppCompatActivity  {

    private RecyclerView mListView;
    private BabysitterListAdapter adapter;
    private DatabaseReference mReference;
    private List<AccountModel> babysitterList;
    private List<String> keys;

    FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_babysitter);


        mAuth = FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference("accounts");
        keys = new ArrayList<>();
        babysitterList = new ArrayList<>();
        bulidRecyclerView();
    }

    public void bulidRecyclerView(){
        mListView = findViewById(R.id.babysitter_list);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
       /*final String id = mAuth.getCurrentUser().getUid();
        DatabaseReference newRef=FirebaseDatabase.getInstance().getReference("accounts")
                .child(id).child("accountType");*/
       mReference.orderByChild("accountType").equalTo("babysitter").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               babysitterList.clear();
               for (DataSnapshot babysitterSnapshot : dataSnapshot.getChildren()) {
                   AccountModel babysitter = babysitterSnapshot.getValue(AccountModel.class);
                   if(babysitter.getStatus().equals("available")){
                       babysitterList.add(babysitter);
                       keys.add(babysitterSnapshot.getKey());
                       }
                   }

               adapter = new BabysitterListAdapter(babysitterList,keys , ListOfBabysitter.this);
               mListView.setAdapter(adapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    /*public void openAlertDialog () {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ListOfBabysitter.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.request_dialog_layout, null);

        Button request = view.findViewById(R.id.request_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);

        builder.setView(view).setTitle("New Order");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();}*/


        /*request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Uid = mAuth.getCurrentUser().getUid();
                final String[] babyID = new String[1];
                Query query = FirebaseDatabase.getInstance().getReference("babies").orderByChild("motherID").equalTo(Uid);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot babySnapshot : dataSnapshot.getChildren()){
                            BabyModel babyModel = babySnapshot.getValue(BabyModel.class);
                            babyID[0] = babySnapshot.getKey();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                String paymentID = null;
                String chatID = null;
                String dailyReportID = null;
                String orderStatus = "waiting";
                int totalHours = 0;
                double price = 0;
                List<ScheduleModel> scheduleList = null;

                OrderModel model = new OrderModel(Uid, babyID[0],keys.toString(),paymentID,chatID,dailyReportID,orderStatus,totalHours,price,scheduleList);
                FirebaseDatabase.getInstance().getReference();


            }
        });*/







}
