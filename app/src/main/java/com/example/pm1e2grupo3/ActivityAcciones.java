package com.example.pm1e2grupo3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;

public class ActivityAcciones extends AppCompatActivity implements LocationListener, OnMapReadyCallback {
    String id ;
    String nombre;
    String foto ;
    String latitud = "" ;
    String longitud = "";
    String telefono;
    ImageView img;
    private LocationManager locationManager;
    private GoogleMap mMap;
    double lat;
    double lon ;

    public static int resultado = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        img = (ImageView) findViewById(R.id.imageView2);
        TextView caja = (TextView) findViewById(R.id.caja);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("IdPerson");
        nombre = intent.getStringExtra("nombre");
        telefono = intent.getStringExtra("telefono");
        longitud = intent.getStringExtra("longitud");
        latitud = intent.getStringExtra("latitud");

        String texto = "Informacion personal\n\n"+
                "ID: "+id + "\n"+
                "Nombre: "+nombre + "\n"+
                "Telefono: "+telefono + "\n"+
                "Longitud: "+longitud + "\n"+
                "Latitud: "+latitud + "\n";

        caja.setText(texto);

        findViewById(R.id.btnEliminar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Eliminar();
            }
        });
        findViewById(R.id.btnActu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityActualizar.class);
                ActivityActualizar.setId(id);
                ActivityActualizar.setLatitud(latitud);
                ActivityActualizar.setLongitud(longitud);
                ActivityActualizar.setNombre1(nombre);
                ActivityActualizar.setTelefono(telefono);
                startActivityForResult(intent,resultado);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String geoUri = "http://maps.google.com/maps?q=loc:" + latitud + "," + longitud + " (" + "Destino" + ")";
                Uri location = Uri.parse(geoUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });
        findViewById(R.id.btnRegresarl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), Listado.class);
                startActivity(in);
            }
        });

    }

    private void Eliminar()
    {
        String url = "http://192.168.1.34/Eliminarp.php";
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("IdPerson", id);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"Se ha Eliminado exitosamente",Toast.LENGTH_SHORT).show();
                        Intent list = new Intent(getApplicationContext(),Listado.class);
                        startActivity(list);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultado && requestCode == RESULT_OK) {
        }
    }
    ///mapi
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lat = Double.parseDouble(latitud);
        lon = Double.parseDouble(longitud);
        LatLng bogota = new LatLng(lat, lon);
        googleMap.addMarker(new MarkerOptions().position(bogota).title("Ubicacion de "+nombre));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(bogota));
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
    }
}