package com.example.scantest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    FloatingActionButton fabAdd,fabCamera,fabGallary;
    Animation fabOpen,fabClose,rotateForward,rotateBackward;
    boolean isOpen=false;
    Bitmap bitmap;
    AlertDialog creditdialog,privacydialog,aboutappdialog;


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

        AlertDialog.Builder creditbuilder = new AlertDialog.Builder(Home.this);
        LayoutInflater inf = this.getLayoutInflater();
        final View dialogView = inf.inflate(aboutus,null);
        creditbuilder.setView(dialogView);
        creditbuilder.setCancelable(true);
        creditdialog = creditbuilder.create();

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
                        String sharebody="empty";
                        String sharesub="Text";
                        myIntent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
                        myIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
                        startActivity(Intent.createChooser(myIntent,"Share using"));
                        break;
                    case R.id.nav_AboutUs:
                        creditdialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                creditdialog.dismiss();
                            }
                        });
                    case R.id.nav_Help:
                        break;
                    case R.id.nav_RateThisApp:
                        startActivity( new Intent(Home.this,Rateus.class));
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
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
               // Toast.makeText(Home.this, "Camera Click", Toast.LENGTH_SHORT).show();
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

    }
    private void getPicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                ivProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e("hello",e.getMessage());
            }

        }
    }*/

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
            finish();
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

