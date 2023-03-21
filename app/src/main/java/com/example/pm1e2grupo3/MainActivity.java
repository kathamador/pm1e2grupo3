package com.example.pm1e2grupo3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Button btnCamara, btnGuardar, btnListaPersonas;
    ImageView imagen;
    TextView txtNombre, txtTelefono;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    TextView tvlatitud, tvlongitud;
    public static String latitud = "";
    public static String longitud = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();
        btnCamara = findViewById(R.id.btnCamara);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnListaPersonas = findViewById(R.id.btnListaPersonas);
        imagen = findViewById(R.id.imageView2);

        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        tvlatitud = findViewById(R.id.txtlatitud);
        tvlongitud = findViewById(R.id.txtlongitud);

        txtNombre.requestFocus();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCamara();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validaciones();
            }
        });

        btnListaPersonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Listado.class);
                startActivity(i);
            }
        });
        findViewById(R.id.main_add2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivityIntegrantes.class);
                startActivity(i);
         }});
    }
 public void validaciones()
 {
     if (imagen.getDrawable() != null) {
         if (txtNombre.getText().toString().trim().length() == 0) {
             txtNombre.setError("CAMPO OBLIGATORIO");

         } else {
             txtNombre.setError(null);
             if (txtTelefono.getText().toString().trim().length() == 0) {
                 txtTelefono.setError("CAMPO OBLIGATORIO");
             } else {
                 txtTelefono.setError(null);
                 if (txtTelefono.getText().toString().length() >= 11) {
                     txtTelefono.setError("MAXIMO 11 CARACTERES");
                 } else {
                     txtTelefono.setError(null);
                     uploadData();
                 }
             }
         }
     } else {
         if (txtNombre.getText().toString().trim().length() == 0) {
             txtNombre.setError("CAMPO OBLIGATORIO");
         } else {
             txtNombre.setError(null);
         }
         if (txtTelefono.getText().toString().trim().length() == 0) {
             txtTelefono.setError("CAMPO OBLIGATORIO");
         } else {
             txtTelefono.setError(null);
         }
         if (txtTelefono.getText().toString().length()  >=  11 ) {
             txtTelefono.setError("MAXIMO 8 CARACTERES");
         } else {
             txtTelefono.setError(null);
         }
         Toast.makeText(this, "SELECCIONE LA IMAGEN", Toast.LENGTH_SHORT).show();
     }
 }
    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagen.setImageBitmap(imageBitmap);
        }
    }
    private void uploadData() {
        // Obtener los datos del formulario
        String nombre = txtNombre.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String imagenBase64 = getImagenBase64();

        // Realizar la solicitud al servidor
        String url = "http://192.168.1.34/Persons.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta del servidor
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            int success = jsonResponse.getInt("success");
                            String message = jsonResponse.getString("message");
                            if (success == 1) {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                // Reiniciar el formulario
                                txtNombre.setText("");
                                txtTelefono.setText("");
                                imagen.setImageDrawable(null);

                            } else {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error al conectarse al servidor"+error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("telefono", telefono);
                params.put("base64", imagenBase64);
                params.put("longitud", longitud);
                params.put("latitud", latitud);
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String getImagenBase64() {
        // Obtener la imagen del ImageView como Bitmap
        Bitmap bitmap = ((BitmapDrawable) imagen.getDrawable()).getBitmap();
        // Convertir el Bitmap a Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imagenBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return imagenBase64;
    }

    ///ubicacion
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            String Text = "Mi ubicacion actual es: " + "\n Lat = "
                    + loc.getLatitude() + "\n Long = " + loc.getLongitude();

            MainActivity.setLatitud(loc.getLatitude()+"");
            MainActivity.setLongitud(loc.getLongitude()+"");
            tvlatitud.setText(loc.getLatitude()+"");
            tvlongitud.setText(loc.getLongitude()+"");
            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
    public static void setLatitud(String latitud) {
        MainActivity.latitud = latitud;
    }
    public static void setLongitud(String longitud) {
        MainActivity.longitud = longitud;
    }
}