package com.example.pm1e2grupo3;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageListAdapter extends ArrayAdapter<Bitmap> {

    private Context context;
    private ArrayList<Bitmap> imagenes;

    public ImageListAdapter(Context context, ArrayList<Bitmap> imagenes) {
        super(context, R.layout.item_person, imagenes);
        this.context = context;
        this.imagenes = imagenes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_person, parent, false);

        ImageView imageView = view.findViewById(R.id.imagen);
        imageView.setImageBitmap(imagenes.get(position));

        return view;
    }
}
