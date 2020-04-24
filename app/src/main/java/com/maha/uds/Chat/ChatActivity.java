package com.maha.uds.Chat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.maha.uds.R;

import java.util.List;


public class ChatActivity extends AppCompatActivity {

    private final String TAG = "Chat Activity";
    private static final int PICK_IMAGE_REQUEST_CODE = 534;
    private static final int CAPTURE_IMAGE_REQUEST_CODE = 353;
    public static final int CAMERA_PERMISSION_CODE = 654;
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 675;

    private RecyclerView recyclerView;
    private Uri filePath;
    private ImageButton sendButton, addAttachment, cameraButton;
    private String orderKey;
    private EditText chatEditText;
    private ChatAdapter chatAdapter;

    private ContentValues values;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        try {
            String title = "Chatting with " + getIntent().getStringExtra("Name");
            getSupportActionBar().setTitle(title);

            //Show back arrow in toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            orderKey = getIntent().getStringExtra("OrderKey");
            sendButton = findViewById(R.id.sendBtn);
            chatEditText = findViewById(R.id.messageETxt);
            addAttachment = findViewById(R.id.addAttachmentButton);
            cameraButton = findViewById(R.id.CameraButton);
            recyclerView = findViewById(R.id.recyclerView);

            requestAccessToCameraPermission();

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }

        readChat();



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Calling add message method and clearing edit text after that
                    FirebaseManager.addMessage(orderKey, chatEditText.getText().toString(),ChatKeys.TEXT);
                    chatEditText.getText().clear();

                } catch (Exception ex) {
                    Log.e(TAG, "Error: " + ex.getMessage());
                }
            }
        });

        addAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Calling choose Image method
                    chooseImage();

                } catch (Exception ex) {
                    Log.e(TAG, "Error: " + ex.getMessage());
                }

            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Calling capture Photo method
                    capturePhoto();

                } catch (Exception ex) {
                    Log.e(TAG, "Error: " + ex.getMessage());
                }

            }
        });

    }

    //Activating onBackPressed method for back arrow in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
        return super.onOptionsItemSelected(item);

    }


    private void readChat() {
        //Calling read Chat method and then setting the chat adapter
        FirebaseManager.readChat(orderKey, new FirebaseManager.OnMessagesRetrieved() {
            @Override
            public void DataIsLoaded(List<MessageModel> messageModels, List<String> keys) {
                chatAdapter = new ChatAdapter(ChatActivity.this, messageModels,keys);
                recyclerView.setLayoutManager(new GridLayoutManager(ChatActivity.this,1));
                recyclerView.setAdapter(chatAdapter);
                scrollMyListViewToBottom();

            }
        });

        //Method Scroll to last position when layout changes
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (chatAdapter!=null)
                                recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
                        }
                    }, 100);
                }
            }
        });
    }

    //Method Scroll to last position when data changed
    private void scrollMyListViewToBottom() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        });
    }


    //Method to choose image from gallery
    private void chooseImage() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_CODE);

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }

    }

    //Method to open camera and capture photo
    private void capturePhoto() {
        try {
            values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Your Camera");
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST_CODE);

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }

    }

    //Method to request Access To Camera Permission
    private void requestAccessToCameraPermission() {

        try {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(ChatActivity.this, new String[]{
                        Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);

            }

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }

    }

    //Method to request Write External Storage Permission
    private void requestWriteExternalStoragePermission() {

        try {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(ChatActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);

            }

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }


    }


    //The result after request dialog disappear
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        try {
            switch (RC) {

                case CAMERA_PERMISSION_CODE:
                    if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                        requestWriteExternalStoragePermission();

                    } else {
                        requestWriteExternalStoragePermission();

                    }

                    break;
                case WRITE_EXTERNAL_STORAGE_CODE:

                    if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    } else {

                    }
            }

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }

    }

    //The result after getting back from camera or gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    if (filePath != null) {
                        FirebaseManager.uploadImage(filePath, orderKey, this, ChatKeys.USER_ID);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK
            ) {
                try {
                    try {
                        if (imageUri != null) {
                            FirebaseManager.uploadImage(imageUri, orderKey, this, ChatKeys.USER_ID);
                            Log.d("Tst", imageUri.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }

    }


}

