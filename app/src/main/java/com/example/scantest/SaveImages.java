package com.example.scantest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SaveImages extends AppCompatActivity {
    Gson gson = new Gson();
    ArrayList<Bitmap> imageslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_images);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String gsonst=sharedPreferences.getString("images","");
        imageslist=gson.fromJson(gsonst,new TypeToken<ArrayList<Bitmap>>(){}.getType());
        if(imageslist==null){
            imageslist=new ArrayList<>();
        }
        ImageAdaptar imageAdaptar = new ImageAdaptar(this,imageslist);
        ViewGroup root = findViewById(R.id.allpictures);
        root.removeAllViews();
        View imageView = LayoutInflater.from(SaveImages.this).inflate(R.layout.showallpictures,null);
        GridView imagegridview = imageView.findViewById(R.id.GridViewimage);
        imagegridview.setAdapter(imageAdaptar);
        root.addView(imageView);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saveimagemenu,menu);
        return (true);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                PdfDocument pdfDocument=new PdfDocument();
                PdfDocument.PageInfo pi= new PdfDocument.PageInfo.Builder(400,400,imageslist.size()).create();
                PdfDocument.Page page=pdfDocument.startPage(pi);
                Canvas canvas=page.getCanvas();
                for(int i=0;i<imageslist.size();i++) {
                    canvas.drawBitmap(imageslist.get(i), 0, 0, null);
                }
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
                    Toast.makeText(getApplicationContext(), "Pdf is save", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SaveImages.this, Home.class));
                }catch(IOException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                pdfDocument.close();

                break;

        }
        return super.onOptionsItemSelected(item);
    }
}