package com.example.scantest;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdaptar extends BaseAdapter {
    Context context;
    List<Bitmap> imageArray;

    ImageView picturesView;
    LayoutInflater layoutInflater;

    ImageAdaptar(Context context, ArrayList<Bitmap> ima){
        this.context=context;
        imageArray = ima;

    }
    @Override
    public int getCount() {
        return imageArray.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
           view = layoutInflater.inflate(R.layout.gridimages,null);

        }

        picturesView = view.findViewById(R.id.picture);
        TextView tv = view.findViewById(R.id.textView4);
        picturesView.setImageBitmap(imageArray.get(i));
        tv.setText((i+1)+"");
        return view;

    }
}
