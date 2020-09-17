package com.example.scantest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
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
    AlertDialog creditdialog,privacydialog,aboutappdialog;
    Uri camareImageUri,gallaryImageUri;



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
                        break;

                    case R.id.nav_Help:
                        break;
                    case R.id.nav_RateThisApp:
                      //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/")));
                        break;
                    case R.id.nav_Signin:
                        break;
                    case R.id.nav_Signout:
                        break;

                }

            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });





        fabGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                getPicture();

                // Toast.makeText(Home.this, "Gallary Click", Toast.LENGTH_SHORT).show();
            }
        });

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA)==
                            PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                            PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,PERMISSION_CODE);
                    }
                    else{
                        openCamera();
                    }


                }
                else{
                    openCamera();
                }
            }
        });
    }



    private void openCamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"new image");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        camareImageUri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,camareImageUri);
        startActivityForResult(cameraIntent,CAMERA_IMAGE_CAPTURE_CODE);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST) {
            openCamera();
        }

    }*/



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_IMAGE_CAPTURE_CODE ){

            Intent i =new Intent(Home.this,After_next.class);
            i.setData(camareImageUri);
            i.putExtra("check","cam");

            startActivity(i);
        }
        else if(requestCode == GALLARY_IMAGE_CAPTURE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            gallaryImageUri = data.getData();
            Intent intent = new Intent(Home.this,After_next.class);
            intent.putExtra("imageUri",gallaryImageUri.toString());
            //Toast.makeText(getApplicationContext(), gallaryImageUri.toString(), Toast.LENGTH_LONG).show();
            startActivity(intent);

        }
    }

    private void getPicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),GALLARY_IMAGE_CAPTURE_CODE);
    }


    private void animateFab(){
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
    }


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