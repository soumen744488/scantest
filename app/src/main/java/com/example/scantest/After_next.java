package com.example.scantest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.DateTimePatternGenerator;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mukesh.image_processing.ImageProcessor;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import java.io.IOException;

public class After_next extends AppCompatActivity {

    ImageView imageView;
    Uri gallaryuri;
    AlertDialog adjustdialog;
    View adjustdialogView;
    Bitmap imagebitmap,setbitmap;
    ImageProcessor imageProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_next);
        //adjustdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        imageView = findViewById(R.id.imageView2);

        gallaryuri = Uri.parse(getIntent().getStringExtra("imageUri"));
        imageView.setImageURI(gallaryuri);
        try {
            imagebitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),gallaryuri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageProcessor = new ImageProcessor();


        AlertDialog.Builder builderadjust = new AlertDialog.Builder(After_next.this);
        LayoutInflater infab = this.getLayoutInflater();
        adjustdialogView = infab.inflate(R.layout.filters,null);
        builderadjust.setView(adjustdialogView);
        adjustdialog=builderadjust.create();


        adjustdialogbutton();

        BottomNavigationView bottomNavigationView = findViewById(R.id.edit_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserItemSelected(item);
                return false;
            }
            private void UserItemSelected(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.filter:
                        WindowManager.LayoutParams wlp = adjustdialog.getWindow().getAttributes();
                        wlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        wlp.gravity= Gravity.BOTTOM ;
                        adjustdialog.show();
                        adjustdialog.setCancelable(false);
                        break;
                    case R.id.adjust:
                        break;

                    case R.id.trash:
                        imageView.setImageDrawable(null);
                       startActivity(new Intent(After_next.this,Home.class));
                        finish();

                }
            }

        });
    }


    private void adjustdialogbutton() {
        Button b1,b2,b3,b4,b5,b6,b7,b8,savedismis;
        b1=adjustdialogView.findViewById(R.id.blackfilter);
        b2=adjustdialogView.findViewById(R.id.engrave);
        b3=adjustdialogView.findViewById(R.id.GaussianBlur);
        savedismis=adjustdialogView.findViewById(R.id.dismissave);

        savedismis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustdialog.dismiss();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setbitmap=imageProcessor.applyBlackFilter(imagebitmap);
               imageView.setImageBitmap(setbitmap);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbitmap=imageProcessor.engrave(imagebitmap);
                imageView.setImageBitmap(setbitmap);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbitmap=imageProcessor.applyGaussianBlur(imagebitmap);
                imageView.setImageBitmap(setbitmap);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.after_next_option_menu,menu);
        return (true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
