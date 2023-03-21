package com.example.pm1e2grupo3;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ActivityMapa  extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    public static Double latitud_map=0.00,longitud_map=0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static Double getLatitud_map() {
        return latitud_map;
    }

    public static void setLatitud_map(Double latitud_map) {
        ActivityMapa.latitud_map = latitud_map;
    }

    public static Double getLongitud_map() {
        return longitud_map;
    }

    public static void setLongitud_map(Double longitud_map) {
        ActivityMapa.longitud_map = longitud_map;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney;
        sydney = new LatLng(latitud_map, longitud_map);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}