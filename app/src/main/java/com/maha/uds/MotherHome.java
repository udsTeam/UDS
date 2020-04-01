package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.maha.uds.Model.AccountModel;
import com.maha.uds.Model.OrderModel;

import java.util.List;


public class MotherHome extends AppCompatActivity  {

    TextView nameView;
    String displayName;
    String displayAge;
    String displayEmail;
    String displayBio;
    String displayPhoneNumber;
    Intent mIntent;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Button orderBtn;
    private Button profile ;
    private Button logout;
    String status = "no orders";
    private OrderModel mOrderModel;
    static String mOrderKey;
    private Button paymentBtn;
    private Button reportBtn;
    private Button chatBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_home);

        setUIview();
        setupFirebaseListener();

        readChatNotification();
        getMyOrder();
        mAuth = FirebaseAuth.getInstance();

        mIntent = new Intent(MotherHome.this,MotherProfile.class);

        FirebaseDatabase.getInstance().getReference("accounts").child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AccountModel accountModel = dataSnapshot.getValue(AccountModel.class);
                        displayName = accountModel.getName();
                        nameView.setText(displayName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            reportBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MotherHome.this, DailyReport.class));
                }
            });
            paymentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MotherHome.this,PaymentPage.class));
                }
            });

            chatBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MotherHome.this, ChatActivity.class));
                }
            });

            orderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MotherHome.this, CreateOrder.class));
                }
            });

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(mIntent);
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                }
            });

        }


    public void setUIview(){
        nameView = findViewById(R.id.name_view);
        orderBtn = findViewById(R.id.createOrder_btn);
        paymentBtn = findViewById(R.id.payment_btn);
        reportBtn = findViewById(R.id.dailyReports_btn);
        chatBtn = findViewById(R.id.chat_btn);
        profile = findViewById(R.id.profile_btn);
        logout = findViewById(R.id.logOut_btn);

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
                                if(!mSnapshot.getValue(OrderModel.class).getOrderStatus().equals("no order")){
                                    mOrderModel = mSnapshot.getValue(OrderModel.class);
                                    mOrderKey = mSnapshot.getKey();
                                    status = mOrderModel.getOrderStatus();
                                    orderBtn.setVisibility(View.GONE);
                                    //filling the dashboard with a text shows that there is an active
                                }

                            }
                        }else{
                            Toast.makeText(MotherHome.this, "You don't have an active order", Toast.LENGTH_SHORT).show();
                            //we will fill the dashboard with a text shows that you don't have any order
                            status = "no order";
                            reportBtn.setVisibility(View.GONE);
                            paymentBtn.setVisibility(View.GONE);
                            chatBtn.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });
    }


}
