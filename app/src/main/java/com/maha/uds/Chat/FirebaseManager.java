package com.maha.uds.Chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


public class FirebaseManager {

    public static final String TAG = "Firebase Controller";
    public static List<MessageModel> mMessageList = new ArrayList<>();
    public static List<String> mKeys = new ArrayList<>();


    //Interface for onDataChange block
    public interface OnMessagesRetrieved {
        void DataIsLoaded(List<MessageModel> messageModels, List<String> keys);
    }


    //Method to Add message to firebase
    public static void addMessage(String orderID, String message , String messageType) {
        try {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            long timeInMill = Calendar.getInstance().getTimeInMillis();

            MessageModel messageModel = new MessageModel();
            messageModel.setCreationDate(timeInMill);
            messageModel.setSenderID(ChatKeys.USER_ID);
            messageModel.setMessageType(messageType);
            messageModel.setMessage(message);
            String newID = db.getReference().push().getKey();
            db.getReference(ChatKeys.CHAT_REFERENCE).child(orderID).child(newID).setValue(messageModel);

        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }
    }


    //Method to retrieve messages from Firebase
    public static void readChat(String orderID, final OnMessagesRetrieved onMessagesRetrieved) {
        try {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            db.getReference(ChatKeys.CHAT_REFERENCE).child(orderID).orderByChild("creationDate").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        mMessageList.clear();
                        mKeys.clear();
                        for(DataSnapshot keyNode:dataSnapshot.getChildren()){
                            mKeys.add(keyNode.getKey());
                            mMessageList.add(keyNode.getValue(MessageModel.class));
                        }

                        onMessagesRetrieved.DataIsLoaded(mMessageList,mKeys);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e.getMessage());
        }

    }

    //Method to upload images to Firebase Storage
    public static void uploadImage(Uri filePath, final String OrderID, final Context context, final String userID) {
        try {
            if (filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] data = baos.toByteArray();

                FirebaseStorage.getInstance().getReference(ChatKeys.CHAT_REFERENCE)
                        .child(OrderID).child(UUID.randomUUID().toString() + ".jpg")
                        .putBytes(data)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    taskSnapshot.getStorage().getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Log.e("UrlImage", o.toString());
                                                    addMessage( OrderID,o.toString(),"Image");
                                                }

                                            });
                                    progressDialog.dismiss();

                                    Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();

                                } catch (Exception ex) {
                                    Log.e(TAG, "Error: " + ex.getMessage());
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                try {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();

                                } catch (Exception ex) {
                                    Log.e(TAG, "Error: " + ex.getMessage());
                                }

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                            .getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");

                                } catch (Exception ex) {
                                    Log.e(TAG, "Error: " + ex.getMessage());
                                }
                            }
                        });

            }

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
}
