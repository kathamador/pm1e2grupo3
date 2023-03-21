package com.example.pm1e2grupo3;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ClaseListAdapter extends  ArrayAdapter<Bitmap> {
    private List<Personas> mLista;
    private Context context;
    private int resourceLayout;
    ImageView imageView;
    private ArrayList<Bitmap> imagenes;


    public ClaseListAdapter(@NonNull Context context, int resource,ArrayList<Bitmap> imagenes, @NonNull List<Personas> objects) {
        super(context,  R.layout.item_person, imagenes);
        this.context = context;
        this.mLista = objects;
        this.resourceLayout = resource;
        this.imagenes = imagenes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view==null){
            view = LayoutInflater.from(context).inflate(resourceLayout,null);
        }
        Personas person = mLista.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_person, parent, false);

        ImageView imageView = view.findViewById(R.id.imagen);
        imageView.setImageBitmap(imagenes.get(position));

        TextView id = view.findViewById(R.id.txtId);
        id.setText(person.getId());
        TextView ruta = view.findViewById(R.id.txtRuta);
        ruta.setText(person.getNombre());
        return view;
    }
}
