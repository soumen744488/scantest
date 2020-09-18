package com.example.scantest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Home extends AppCompatActivity {
    public static final int CAMERA_REQUEST = 1888;
    public static final int CAMERA_IMAGE_CAPTURE_CODE = 1000;
    public static final int GALLARY_IMAGE_CAPTURE_CODE = 1001;
    public static final int PERMISSION_CODE = 1111;


    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    FloatingActionButton fabAdd,fabCamera,fabGallary;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    boolean isOpen=false;
    Bitmap bitmap,gallaryImagebitmap;

    Uri camareImageUri,uri;
    AlertDialog aboutappdialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(Home.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //For Floating Action Button
        fabAdd = findViewById(R.id.fabAdd);
        fabCamera = findViewById(R.id.fabCamera);
        fabGallary = findViewById(R.id.fabGallary);
        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_bacward);

        AlertDialog.Builder aboutbuilder = new AlertDialog.Builder(Home.this);
        LayoutInflater infab = this.getLayoutInflater();
        final View aboutdialogView = infab.inflate();
        aboutbuilder.setView(aboutdialogView);
        aboutappdialog = aboutbuilder.create();

        NavigationView navigationView = findViewById(R.id.navigationView);
        View view = navigationView.inflateHeaderView(R.layout.navigation_header);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserItemSelected(item);
                return false;
            }


            private void UserItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_ShareThisApp:
                        Intent myIntent= new Intent(Intent.ACTION_SEND);
                        myIntent.setType("text/plain");
                        String sharebody="https://drive.google.com/drive/folders/1EUb55LVNpDJZqR9tJPxZPm3EnrCDzPxT?usp=sharing";
                        String sharesub="Text";
                        myIntent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
                        myIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
                        startActivity(Intent.createChooser(myIntent,"Share using"));
                        break;
                    case R.id.nav_AboutUs:
                        startActivity(new Intent(Home.this,AboutUs.class));
                        break;

                    case R.id.info:
                        aboutappdialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                aboutappdialog.dismiss();
                            }
                        });
                        aboutappdialog.show();
                        aboutappdialog.setCancelable(false);
                        break;

                    case R.id.nav_RateThisApp:
                      //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/")));
                        break;


                }

            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // animateFab();
                CropImage.startPickImageActivity(Home.this);

            }
        });





      /*  fabGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(Home.this);
            }
        });

       fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(Home.this);
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this,data);
            //if(CropImage.isReadExternalStoragePermissionsRequired(this,imageuri)){
                uri=imageuri;
              //  Toast.makeText(getApplicationContext(), imageuri.toString(), Toast.LENGTH_SHORT).show();
              //  requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},0);
           // }else{
                CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);
            //}
        }
        if(requestCode== CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                Intent intent = new Intent(Home.this,After_next.class);
                intent.putExtra("imageUri",result.getUri().toString());
                startActivity(intent);
            }
        }
    }

   /* private void animateFab(){
        if(isOpen){
            fabAdd.startAnimation(rotateForward);
            fabCamera.startAnimation(fabClose);
            fabGallary.startAnimation(fabClose);
            fabCamera.setClickable(false);
            fabGallary.setClickable(false);
            isOpen=false;
        }
        else {
            fabAdd.startAnimation(rotateBackward);
            fabCamera.startAnimation(fabOpen);
            fabGallary.startAnimation(fabOpen);
            fabCamera.setClickable(true);
            fabGallary.setClickable(true);
            isOpen=true;
        }
    }*/


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == R.id.share) {

        } else if (item.getItemId() == R.id.sortbyname) {

        } else if (item.getItemId() == R.id.sortbydate) {

        } else if (item.getItemId() == R.id.trash) {

        } else if (item.getItemId() == R.id.exit) {
           // finish();
            AlertDialog.Builder build = new AlertDialog.Builder(this);

            build.setTitle(" Exit ")
                    .setMessage("Do you Want to exit from this application ?")
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setCancelable(false);

            AlertDialog alert = build.create();
            alert.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();

        }
    }
}