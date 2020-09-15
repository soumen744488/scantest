package com.example.scantest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class After_next extends AppCompatActivity {

    ImageView imageView;
    Uri camera_Image_Uri, gallary_Image_Uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_next);
        imageView = findViewById(R.id.imageView2);
        //Bundle extras = getIntent().getExtras();
       // if (extras.containsKey("camareImage")) {
           // camera_Image_Uri = Uri.parse(extras.getString("cameraImage"));
           // imageView.setImageURI(camera_Image_Uri);

            // gallary_Image_Uri =in.getData();
            //imageView.setImageURI(gallary_Image_Uri);
        Intent in= getIntent();
        camera_Image_Uri=in.getData();
        imageView.setImageURI(camera_Image_Uri);


        //}
    }
}
