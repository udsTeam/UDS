package com.maha.uds.Chat;

import com.google.firebase.auth.FirebaseAuth;

public class ChatKeys {

    public static final String USER_ID = FirebaseAuth.getInstance().getUid();
    public static final String CHAT_REFERENCE = "Chat";
    public static final String TEXT = "Text";
    public static final String IMAGE = "Image";

}
