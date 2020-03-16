package com.maha.uds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maha.uds.Chat.ChatActivity;
import com.maha.uds.Chat.ChatKeys;
import com.maha.uds.Chat.FirebaseManager;
import com.maha.uds.Chat.MessageModel;
import com.maha.uds.Chat.MyNotificationManager;
import com.maha.uds.Chat.SharedPrefsKeys;
import com.maha.uds.Chat.SharedPrefsManager;

import java.util.List;
import com.maha.uds.Model.OrderModel;


public class MotherHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    TextView nameView;
    private String displayName;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    BottomNavigationView mNavigationView;
    private Button orderBtn;
    String status = "no orders";
    private OrderModel mOrderModel;
    static String mOrderKey;
    private Button paymentBtn;
    private Button reportBtn;
    private Button chatBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_home);

        setUIview();
        setupFirebaseListener();
        setupDisplayName();
        readChatNotification();
        getMyOrder();




        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("no order")||status.equals("finish")){
                    startActivity(new Intent(MotherHome.this, CreateOrder.class));
                }else{
                    Toast.makeText(MotherHome.this, "You already have an order", Toast.LENGTH_LONG).show();
                }

            }
        });


        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MotherHome.this,DailyReport.class));
            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!status.equals("no orders")){
                    if(!status.equals("pending")){
                        startActivity(new Intent(MotherHome.this,DailyReport.class));
                    }else{
                        Toast.makeText(MotherHome.this, "You don't have an active order", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MotherHome.this, "You don't have an order", Toast.LENGTH_SHORT).show();
                }
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MotherHome.this, ChatActivity.class));
            }
        });





    }
    public void setUIview(){
        nameView = findViewById(R.id.name_view);
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setOnNavigationItemSelectedListener(this);
        orderBtn = findViewById(R.id.createOrder_btn);
        paymentBtn = findViewById(R.id.payments_btn);
        reportBtn = findViewById(R.id.dailyReports_btn);
        chatBtn = findViewById(R.id.chat_btn);



    }

    private void readChatNotification() {
        String orderId="Test233";
        FirebaseManager.readChat(orderId, new FirebaseManager.OnMessagesRetrieved() {
            @Override
            public void DataIsLoaded(List<MessageModel> messageModels, List<String> keys) {
                String FirebaeChatID = keys.get(keys.size()-1);
                MessageModel mMessageModel = messageModels.get(messageModels.size()-1);
                String LocalChatID = SharedPrefsManager.getInstance().getString(SharedPrefsKeys.CHAT_ID,"Empty");
                if (!mMessageModel.getSenderID().equals(ChatKeys.USER_ID)){
                    if (!LocalChatID.equals(FirebaeChatID)){
                        if (mMessageModel.getMessageType().equals(ChatKeys.TEXT)){
                            MyNotificationManager.sendNotification("Person send you a message",mMessageModel.getMessage(),MotherHome.this);
                        }else {
                            MyNotificationManager.sendNotification("Person send you a Image","Image",MotherHome.this);
                        }
                        SharedPrefsManager.getInstance().setString(SharedPrefsKeys.CHAT_ID,FirebaeChatID);
                    }
                }

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        nameView.setText(displayName);
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    private void setupFirebaseListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    user.getUid();
                } else {
                    Intent intent = new Intent(MotherHome.this, SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    private void setupDisplayName() {
        SharedPreferences prefs = getSharedPreferences(MotherRegister.CHAT_PREFS, MODE_PRIVATE);
        displayName = prefs.getString(MotherRegister.DISPLAY_USER_NAME, null);
        if (displayName == null) {
            displayName = "Anonymous";
        }
    }
    public void pickImageFromGallery(){

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(MotherHome.this,MotherProfile.class));
                break;
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                break;
        }

        return false;
    }

    private void getMyOrder(){
        //show progress dialog
        mOrderModel = new OrderModel();
        mOrderKey = "";
        FirebaseDatabase.getInstance().getReference("orders")
                .orderByChild("motherID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //progress dialog dissmis
                        if(dataSnapshot.exists()){
                            for(DataSnapshot mSnapshot : dataSnapshot.getChildren()){
                                if(!mSnapshot.getValue(OrderModel.class).getOrderStatus().equals("finish")){
                                    mOrderModel = mSnapshot.getValue(OrderModel.class);
                                    mOrderKey = mSnapshot.getKey();
                                    status = mOrderModel.getOrderStatus();
                                    //filling the dashboard with a text shows that there is an active
                                }

                            }
                        }else{
                            Toast.makeText(MotherHome.this, "You don't have an active order", Toast.LENGTH_SHORT).show();
                            //we will fill the dashboard with a text shows that you don't have any order
                            status = "no order";
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });
    }


}
