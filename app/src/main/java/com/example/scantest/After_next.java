package com.example.scantest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    Uri gallaryuri;
    AlertDialog adjustdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_next);
        imageView = findViewById(R.id.imageView2);
        
        gallaryuri = Uri.parse(getIntent().getStringExtra("imageUri"));
        imageView.setImageURI(gallaryuri);

        AlertDialog.Builder builderadjust = new AlertDialog.Builder(After_next.this);
        LayoutInflater infab = this.getLayoutInflater();
        View aboutdialogView = infab.inflate(R.layout.filters,null);
        builderadjust.setView(aboutdialogView);
        adjustdialog=builderadjust.create();


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
                        adjustdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        WindowManager.LayoutParams wlp = adjustdialog.getWindow().getAttributes();
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
