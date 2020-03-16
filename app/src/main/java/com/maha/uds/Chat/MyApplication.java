package com.maha.uds.Chat;

import android.app.Application;


/** Class that fires up when app starting before all activities **/
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /** Initializing Shared Preference only one time in here **/
        SharedPrefsManager.initialize(this);

        /** Calling method from notification class **/
        MyNotificationManager.createChannelAndHandleNotifications(this);
    }

}
