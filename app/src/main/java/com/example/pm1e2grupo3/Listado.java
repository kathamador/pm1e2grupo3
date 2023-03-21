package com.example.pm1e2grupo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Listado extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<Personas> mLista = new ArrayList<>();
    private ListView mlistView;
    List<Personas> usuarioList;
    ClaseListAdapter mAdapter;
    EditText txtBuscador;
    public static int resultado = 1;
    ArrayList<String> arrayUsuario;
    private static ClaseListAdapter adaptercustom;
    private ListView listView;
    private ArrayList<Bitmap> imagenes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        mlistView = (ListView) findViewById(R.id.mlistView);
        txtBuscador = (EditText) findViewById(R.id.txtBuscador);

        //getSupportActionBar().hide();
        mlistView.setOnItemClickListener(this);
        listView = findViewById(R.id.mlistView);
        imagenes = new ArrayList<>();

        Busqueda("");
        txtBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
               if (s.toString().trim().length() == 0) {
                    Busqueda("");
                } else {
                    Busqueda(s.toString());
                }
            }
        });

        // Realizar la solicitud HTTP para obtener las imágenes desde el servidor
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.1.34/viewimg.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            // Iterar sobre los objetos JSON en la respuesta
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String imagenBase64 = jsonObject.getString("imagen");

                                // Decodificar la imagen desde la cadena Base64
                                byte[] imagenBytes = Base64.decode(imagenBase64, Base64.DEFAULT);
                                Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);

                                // Agregar la imagen al ArrayList
                                imagenes.add(imagenBitmap);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Agregar la solicitud a la cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        findViewById(R.id.main_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
            });

    }
    private void Busqueda(String dato) {
        String url = "http://192.168.1.34/Mostrar.php?texto=" + dato + "";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                mLista = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Personas personas = new Personas();
                        personas.setId(jsonObject.getString("IdPerson"));
                        personas.setNombre(jsonObject.getString("Name"));
                        personas.setTelefono(jsonObject.getString("Phone"));
                        personas.setUrl(jsonObject.getString("Image"));
                        personas.setLongitud(jsonObject.getString("Longitud"));
                        personas.setLatitud(jsonObject.getString("Latitud"));

                        mLista.add(personas);
                        mAdapter = new ClaseListAdapter(getApplicationContext(), R.layout.item_person,imagenes, mLista);
                        mlistView.setAdapter(mAdapter);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexion al buscar", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(getApplicationContext(), ActivityAcciones.class);
        intent.putExtra("IdPerson", mLista.get(position).getId());
        intent.putExtra("nombre", mLista.get(position).getNombre());
        intent.putExtra("telefono", mLista.get(position).getTelefono());
        intent.putExtra("longitud", mLista.get(position).getLongitud());
        intent.putExtra("latitud", mLista.get(position).getLatitud());
        startActivityForResult(intent, resultado);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultado && requestCode == RESULT_OK) {
            Busqueda("");
            txtBuscador.setText("");
        }
    }
}