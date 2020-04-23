package com.maha.uds;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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


public class MotherHome extends AppCompatActivity {

    TextView nameView;
    String displayName;
    Intent mIntent;
    String babyitterID;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Button orderBtn;
    private Button profile;
    private Button logout;
    private TextView statusView;
    String status = "no orders";
    private OrderModel mOrderModel;
    static String mOrderKey;
    private Button paymentBtn;
    private Button reportBtn;
    private Button chatBtn;
    private FirebaseAuth mAuth;
    float RatingValue = 0;
    private String babysitterName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mother_home);

        setUIview();
        setupFirebaseListener();

        getMyOrder();
        mAuth = FirebaseAuth.getInstance();

        mIntent = new Intent(MotherHome.this, MotherProfile.class);

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
                startActivity(new Intent(MotherHome.this, PaymentPage.class));
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MotherHome.this, ChatActivity.class);
                mIntent.putExtra("OrderKey", mOrderKey);
                mIntent.putExtra("Name", babysitterName);
                startActivity(mIntent);
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


    public void setUIview() {
        nameView = findViewById(R.id.name_view);
        orderBtn = findViewById(R.id.viewOrder_btn);
        orderBtn.setVisibility(View.GONE);
        paymentBtn = findViewById(R.id.payment_btn);
        paymentBtn.setVisibility(View.GONE);
        reportBtn = findViewById(R.id.dailyReports_btn);
        orderBtn.setVisibility(View.GONE);
        chatBtn = findViewById(R.id.chat_btn);
        orderBtn.setVisibility(View.GONE);
        profile = findViewById(R.id.profile_btn);
        profile.setVisibility(View.VISIBLE);
        logout = findViewById(R.id.logOut_btn);
        logout.setVisibility(View.VISIBLE);
        statusView = findViewById(R.id.orderStatusView);

    }


    private void readChatNotification() {
        FirebaseManager.readChat(mOrderKey, new FirebaseManager.OnMessagesRetrieved() {
            @Override
            public void DataIsLoaded(List<MessageModel> messageModels, List<String> keys) {
                String FirebaeChatID = keys.get(keys.size() - 1);
                MessageModel mMessageModel = messageModels.get(messageModels.size() - 1);
                String LocalChatID = SharedPrefsManager.getInstance().getString(SharedPrefsKeys.CHAT_ID, "Empty");
                if (!mMessageModel.getSenderID().equals(ChatKeys.USER_ID)) {
                    if (!LocalChatID.equals(FirebaeChatID)) {
                        if (mMessageModel.getMessageType().equals(ChatKeys.TEXT)) {
                            MyNotificationManager.sendNotification(babysitterName + " send you a message", mMessageModel.getMessage(), MotherHome.this);
                        } else {
                            MyNotificationManager.sendNotification(babysitterName + " send you a Image", "Image", MotherHome.this);
                        }
                        SharedPrefsManager.getInstance().setString(SharedPrefsKeys.CHAT_ID, FirebaeChatID);
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


    private void getBabySitterName() {
        FirebaseDatabase.getInstance().getReference("accounts").child(babyitterID).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    babysitterName = dataSnapshot.getValue(String.class);
                chatBtn.setVisibility(View.VISIBLE);
                readChatNotification();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getMyOrder() {
        //show progress dialog
        mOrderModel = new OrderModel();
        mOrderKey = "";
        FirebaseDatabase.getInstance().getReference("orders")
                .orderByChild("motherID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //progress dialog dissmis
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot mSnapshot : dataSnapshot.getChildren()) {
                                mOrderModel = mSnapshot.getValue(OrderModel.class);
                                mOrderKey = mSnapshot.getKey();
                                babyitterID = mOrderModel.getBabysitterID();
                                if (mOrderModel.getOrderStatus().equals("active")) {
                                    statusView.setText("Your order is active");
                                    getBabySitterName();
                                    orderBtn.setVisibility(View.GONE);
                                    reportBtn.setVisibility(View.VISIBLE);
                                    paymentBtn.setVisibility(View.GONE);
                                    profile.setVisibility(View.VISIBLE);
                                    logout.setVisibility(View.VISIBLE);

                                }
                                if (mOrderModel.getOrderStatus().equals("ratting")) {
                                    orderBtn.setVisibility(View.VISIBLE);
                                    reportBtn.setVisibility(View.GONE);
                                    chatBtn.setVisibility(View.GONE);
                                    paymentBtn.setVisibility(View.GONE);
                                    profile.setVisibility(View.VISIBLE);
                                    logout.setVisibility(View.VISIBLE);
                                    rattingDialog();
                                } else if (mOrderModel.getOrderStatus().equals("pending")) {
                                    statusView.setText("Your order still pending");
                                    orderBtn.setVisibility(View.GONE);
                                    reportBtn.setVisibility(View.GONE);
                                    chatBtn.setVisibility(View.GONE);
                                    paymentBtn.setVisibility(View.GONE);
                                    profile.setVisibility(View.VISIBLE);
                                    logout.setVisibility(View.VISIBLE);

                                } else if (mOrderModel.getOrderStatus().equals("finished")
                                        || mOrderModel.getOrderStatus().equals("rejected")) {
                                    statusView.setText("You don't have an order");
                                    orderBtn.setVisibility(View.VISIBLE);
                                    reportBtn.setVisibility(View.GONE);
                                    chatBtn.setVisibility(View.GONE);
                                    paymentBtn.setVisibility(View.GONE);
                                    profile.setVisibility(View.VISIBLE);
                                    logout.setVisibility(View.VISIBLE);

                                } else if(mOrderModel.getOrderStatus().equals("accepted")) {

                                    statusView.setText("You should Pay First");
                                        orderBtn.setVisibility(View.GONE);
                                        reportBtn.setVisibility(View.GONE);
                                        chatBtn.setVisibility(View.GONE);
                                        paymentBtn.setVisibility(View.VISIBLE);
                                        profile.setVisibility(View.VISIBLE);
                                        logout.setVisibility(View.VISIBLE);

//                                    if (mOrderModel.getPaymentStatus().equals("paid")) {
//                                        status = mOrderModel.getOrderStatus();
//                                        statusView.setText("Your Order is active");
//                                        orderBtn.setVisibility(View.GONE);
//                                        reportBtn.setVisibility(View.VISIBLE);
//                                        chatBtn.setVisibility(View.VISIBLE);
//                                        paymentBtn.setVisibility(View.GONE);
//                                        profile.setVisibility(View.VISIBLE);
//                                        logout.setVisibility(View.VISIBLE);
//                                        //filling the dashboard with a text shows that there is an active
//                                    } else {
//                                        statusView.setText("You should Pay First");
//                                        orderBtn.setVisibility(View.GONE);
//                                        reportBtn.setVisibility(View.GONE);
//                                        chatBtn.setVisibility(View.GONE);
//                                        paymentBtn.setVisibility(View.VISIBLE);
//                                        profile.setVisibility(View.VISIBLE);
//                                        logout.setVisibility(View.VISIBLE);
                                    }
                                }

                            }
                         else {

                            //we will fill the dashboard with a text shows that you don't have any order
                            status = "no order";
                            statusView.setText("You don't have an order");
                            reportBtn.setVisibility(View.GONE);
                            paymentBtn.setVisibility(View.GONE);
                            chatBtn.setVisibility(View.GONE);
                            orderBtn.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {


                    }
                });
    }

    public void rattingDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MotherHome.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.ratting_dialog, null);

        Button done = view.findViewById(R.id.doneBtn);
        final RatingBar ratting = view.findViewById(R.id.ratingBar);

        builder.setView(view).setTitle("Ratting");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RatingValue = ratting.getRating();
                FirebaseDatabase.getInstance().getReference("accounts")
                        .child(babyitterID).child("ratting").setValue(RatingValue);
                FirebaseDatabase.getInstance().getReference("accounts")
                        .child(babyitterID).child("status").setValue("available");
                FirebaseDatabase.getInstance().getReference("orders")
                        .child(mOrderKey).child("orderStatus").setValue("finished");
                FirebaseDatabase.getInstance().getReference("orders")
                        .child(mOrderKey).child("babysitterID").setValue("");
                alertDialog.dismiss();
            }

        });
    }


}
