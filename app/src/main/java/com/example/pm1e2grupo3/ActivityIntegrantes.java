package com.example.pm1e2grupo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityIntegrantes extends AppCompatActivity {
    private ListView listview;

    private ArrayList<String>nombres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrantes);
        listview = (ListView) findViewById(R.id.ListIntegrantes);

        nombres = new ArrayList<String>();
        nombres.add("Mirian Fatima Ordoñez Amador");
        nombres.add("Katherin Nicole Amador Maradiaga");
        nombres.add("Christian Isaac Calona Cruz");
        nombres.add("Elvin Joel Molina Alvarez");
        nombres.add("Sharon Vanessa Espinoza Calderon");
        nombres.add("Gleen Alexis Pineda Moreno");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);
        listview.setAdapter(adapter);
        findViewById(R.id.btnRegresar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(in);
            }
        });
    }

}