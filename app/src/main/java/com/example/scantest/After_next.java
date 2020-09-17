package com.example.scantest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

public class After_next extends AppCompatActivity {

    ImageView imageView;
    Uri camera_Image_Uri, gallaryuri,uri_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_next);
        imageView = findViewById(R.id.imageView2);

        if (getIntent().getExtras() != null) {
            gallaryuri = Uri.parse(getIntent().getStringExtra("imageUri"));
            //Toast.makeText(getApplicationContext(), gallaryuri.toString(), Toast.LENGTH_SHORT).show();
            imageView.setImageURI(gallaryuri);
        }

        final Intent in= getIntent();
        camera_Image_Uri=in.getData();
        imageView.setImageURI(camera_Image_Uri);


        BottomNavigationView bottomNavigationView = findViewById(R.id.edit_nav);




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserItemSelected(item);
                return false;
            }

            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                    final Uri resultUri = UCrop.getOutput(data);
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
            }



            private void UserItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.crop:
                        CropImage.activity(camera_Image_Uri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(After_next.this);
                        break;
                    case R.id.rotation:
                       imageView.setRotation(imageView.getRotation() + 90);
                       break;

                    case R.id.filter:
                        break;

                    case R.id.color:
                        break;

                    case R.id.trash:
                        imageView.setImageDrawable(null);
                       startActivity(new Intent(After_next.this,Home.class));
                        finish();

                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                imageView.setImageURI(result.getUri());
                Toast.makeText(getApplicationContext(), "Crop Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
