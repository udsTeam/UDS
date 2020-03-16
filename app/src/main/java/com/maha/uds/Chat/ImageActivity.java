package com.maha.uds.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.maha.uds.R;

public class ImageActivity extends AppCompatActivity {

    private final String TAG = "Image Activity";

    private ImageView imageView;
    private String imageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        try {
            getSupportActionBar().setTitle("Image Viewer");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }

        imageView = findViewById(R.id.imageView);

        imageURL = getIntent().getStringExtra("ImageURL");

        Glide.with(this).load(imageURL).into(imageView);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    // API 5+ solution
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

}
