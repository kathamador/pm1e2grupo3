package com.example.pm1e2grupo3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

public class ActivityActualizar extends AppCompatActivity {
    ImageView imageView;
    EditText nombre, numero;
    TextView tvlatitud, tvlongitud,mensaje;
    static final int RESULT_GALLERY_IMG = 100;
    Bitmap photo = null;
    public static String latitud = "";
    public static String longitud = "";
    public static String id;
    public static String nombre1;
    public static String url;
    public static String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar);

        imageView =findViewById(R.id.ivactu);
        nombre = findViewById(R.id.txtNombre);
        numero = findViewById(R.id.txtNumero);
        tvlatitud = findViewById(R.id.txtlatitud);
        tvlongitud = findViewById(R.id.txtlongitud);

        Picasso.with(getApplicationContext()).load(url).into(imageView);
        nombre.setText(nombre1+"");
        numero.setText(telefono);
        tvlatitud.setText(latitud);
        tvlongitud.setText(longitud);

        findViewById(R.id.btnsalvar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaciones();
            }
        });
    }
    public void validaciones()
    {
            if (nombre.getText().toString().trim().length() == 0) {
                nombre.setError("CAMPO OBLIGATORIO");

            } else {
                nombre.setError(null);
                if (numero.getText().toString().trim().length() == 0) {
                    numero.setError("CAMPO OBLIGATORIO");
                } else {
                    numero.setError(null);
                    if (numero.getText().toString().length() >= 11) {
                        numero.setError("MAXIMO 11 CARACTERES");
                    } else {
                        numero.setError(null);
                        Actualizar();
                    }
                }

        }
    }
    private void Actualizar()
    {
        String url = "http://192.168.1.34/actualizar_usuario.php";

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("IdPerson", id);
        params.put("Name", nombre.getText().toString().toLowerCase());
        params.put("Phone", numero.getText().toString());
        params.put("Longitud", longitud);
        params.put("Latitud", latitud);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"Se ha Modificado exitosamente",Toast.LENGTH_SHORT).show();

                        //Toast.makeText(getApplicationContext(),"Se ha Ingresado Exitosamente",Toast.LENGTH_SHORT).show();
                        Intent list = new Intent(getApplicationContext(),Listado.class);
                        startActivity(list);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error", "Error: " + error.getMessage());
                AlertaDialogo("Error al modificar "+error.getMessage(),"Error");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

        private void GaleriaImagenes()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoPickerIntent, RESULT_GALLERY_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri;

        if (resultCode == RESULT_OK && requestCode == RESULT_GALLERY_IMG)
        {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

            try
            {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            }
            catch (Exception ex)
            {}
        }
    }


    private void AlertaDialogo(String mensaje, String title){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog titulo = alerta.create();
        titulo.setTitle(title);
        titulo.show();

    }
    public static String getLatitud() {
        return latitud;
    }

    public static void setLatitud(String latitud) {
        ActivityActualizar.latitud = latitud;
    }

    public static String getLongitud() {
        return longitud;
    }

    public static void setLongitud(String longitud) {
        ActivityActualizar.longitud = longitud;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        ActivityActualizar.id = id;
    }

    public static String getNombre1() {
        return nombre1;
    }

    public static void setNombre1(String nombre1) {
        ActivityActualizar.nombre1 = nombre1;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ActivityActualizar.url = url;
    }

    public static String getTelefono() {
        return telefono;
    }

    public static void setTelefono(String telefono) {
        ActivityActualizar.telefono = telefono;
    }


}