package com.example.scantest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.icu.text.DateTimePatternGenerator;
import android.net.Uri;
import android.opengl.Matrix;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mukesh.image_processing.ImageProcessor;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class After_next extends AppCompatActivity {

    ImageView imageView;
    Uri gallaryuri;
    AlertDialog filterdialog,adjustdialog;
    View adjustdialogView,filterdialogView;
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
        adjustdialogView = infab.inflate(R.layout.adjust,null);
        builderadjust.setView(adjustdialogView);
        adjustdialog=builderadjust.create();

        AlertDialog.Builder builderfilter = new AlertDialog.Builder(After_next.this);
        LayoutInflater infil = this.getLayoutInflater();
        filterdialogView = infil.inflate(R.layout.filters,null);
        builderfilter.setView(filterdialogView);
        filterdialog=builderfilter.create();


        filteroperation();
        adjustoperation();


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
                        WindowManager.LayoutParams wlp = filterdialog.getWindow().getAttributes();
                        wlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        wlp.gravity= Gravity.BOTTOM ;
                        filterdialog.show();
                        filterdialog.setCancelable(false);
                        break;
                    case R.id.adjust:
                        WindowManager.LayoutParams wlp0 = adjustdialog.getWindow().getAttributes();
                        wlp0.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        wlp0.gravity= Gravity.BOTTOM ;
                        adjustdialog.show();
                        adjustdialog.setCancelable(false);
                        break;

                    case R.id.trash:
                        imageView.setImageDrawable(null);
                        startActivity(new Intent(After_next.this,Home.class));
                        finish();
                        break;

                    case R.id.save:
                        PdfDocument pdfDocument=new PdfDocument();
                        PdfDocument.PageInfo pi= new PdfDocument.PageInfo.Builder(imagebitmap.getWidth(),imagebitmap.getHeight(),1).create();
                        PdfDocument.Page page=pdfDocument.startPage(pi);
                        Canvas canvas=page.getCanvas();
                        canvas.drawBitmap(imagebitmap,0,0,null);
                        pdfDocument.finishPage(page);
                        //save the bitmap image
                        File root= new File(Environment.getExternalStorageDirectory(),"SCAN BY OWN DOC");
                        if(!root.exists()){
                            root.mkdir();
                        }
                        File file= new File(root,"picture.pdf");
                        try{
                            // FileOutputStream fileOutputStream= new FileOutputStream(file);
                            pdfDocument.writeTo(new FileOutputStream(file));
                            Toast.makeText(After_next.this, "Pdf is save", Toast.LENGTH_SHORT).show();
                        }catch(IOException e){
                            e.printStackTrace();
                            Toast.makeText(After_next.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        pdfDocument.close();
                    }


                }


        });

    }


    private void adjustoperation() {
        SeekBar brightness,sharpness,smooth;
        brightness = adjustdialogView.findViewById(R.id.brightnessseekbar);
        sharpness = adjustdialogView.findViewById(R.id.sharpnessseekbar);
        smooth = adjustdialogView.findViewById(R.id.smoothseekBar);

        Button okad = adjustdialogView.findViewById(R.id.okadjust);
        okad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustdialog.dismiss();
            }
        });

        brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setbitmap=imageProcessor.doBrightness(imagebitmap,progress*10);
                imageView.setImageBitmap(setbitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sharpness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setbitmap=imageProcessor.sharpen(imagebitmap,progress*10);
                imageView.setImageBitmap(setbitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        smooth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setbitmap=imageProcessor.smooth(imagebitmap,progress*10);
                imageView.setImageBitmap(setbitmap);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    private void filteroperation() {
        Button b1,b2,b3,b4,b5,b6,b7,savedismis;
        b1=filterdialogView.findViewById(R.id.blackfilter);
        b2=filterdialogView.findViewById(R.id.engrave);
        b3=filterdialogView.findViewById(R.id.invert);
        b4=filterdialogView.findViewById(R.id.color4);
        b5=filterdialogView.findViewById(R.id.color5);
        b6=filterdialogView.findViewById(R.id.color6);
        b7=filterdialogView.findViewById(R.id.color7);

        savedismis=filterdialogView.findViewById(R.id.dismissave);

        savedismis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterdialog.dismiss();
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
                setbitmap=imageProcessor.doInvert(imagebitmap);
                imageView.setImageBitmap(setbitmap);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbitmap=imageProcessor.doGreyScale(imagebitmap);
                imageView.setImageBitmap(setbitmap);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbitmap=imageProcessor.applyReflection(imagebitmap);
                imageView.setImageBitmap(setbitmap);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbitmap=imageProcessor.applyHueFilter(imagebitmap,2);
                imageView.setImageBitmap(setbitmap);
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setbitmap=imageProcessor.applyFleaEffect(imagebitmap);
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
