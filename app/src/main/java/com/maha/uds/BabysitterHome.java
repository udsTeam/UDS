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
import com.maha.uds.Chat.ChatActivity;
import com.maha.uds.Chat.ChatKeys;
import com.maha.uds.Chat.FirebaseManager;
import com.maha.uds.Chat.MessageModel;
import com.maha.uds.Chat.MyNotificationManager;
import com.maha.uds.Chat.SharedPrefsKeys;
import com.maha.uds.Chat.SharedPrefsManager;

import java.util.List;

public class BabysitterHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    TextView nameView;
    private String displayName;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    BottomNavigationView mNavigationView;
    private Button orderBtn;
    private Button trackBtn;
    private Button paymentBtn;
    private Button reportBtn;
    private Button chatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.babysitter_home);
        setUIview();
        setupFirebaseListener();
        setupDisplayName();
        readChatNotification();

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BabysitterHome.this, "1", Toast.LENGTH_SHORT).show();
            }
        });

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BabysitterHome.this, "2", Toast.LENGTH_SHORT).show();
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BabysitterHome.this, "3", Toast.LENGTH_SHORT).show();
            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(BabysitterHome.this,DailyReport.class));
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BabysitterHome.this, ChatActivity.class));
            }
        });




    }
    public void setUIview(){
        nameView = findViewById(R.id.name_view);
        mNavigationView = findViewById(R.id.navigation_view);
        mNavigationView.setOnNavigationItemSelectedListener(this);
        orderBtn = findViewById(R.id.createOrder_btn);
        trackBtn = findViewById(R.id.trackOrder_btn);
        paymentBtn = findViewById(R.id.payments_btn);
        reportBtn = findViewById(R.id.dailyReports_btn);
        chatBtn = findViewById(R.id.chat_btn);



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
                    Intent intent = new Intent(BabysitterHome.this, SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    private void setupDisplayName() {
        SharedPreferences prefs = getSharedPreferences(BabysitterRegister.CHAT_PREFS, MODE_PRIVATE);
        displayName = prefs.getString(BabysitterRegister.DISPLAY_NAME, null);
        if (displayName == null) {
            displayName = "Anonymous";
        }
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
                            MyNotificationManager.sendNotification("Person send you a message",mMessageModel.getMessage(),BabysitterHome.this);
                        }else {
                            MyNotificationManager.sendNotification("Person send you a Image","Image",BabysitterHome.this);
                        }
                        SharedPrefsManager.getInstance().setString(SharedPrefsKeys.CHAT_ID,FirebaeChatID);
                    }
                }

            }
        });
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(BabysitterHome.this,BabysitterProfile.class));
                break;
            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                break;
        }

        return false;
    }
}
