package com.example.shakemaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class PositionMaps extends FragmentActivity
{
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_maps);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);

        getMyLocation();
    }

    private void getMyLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            return;
        }

        Task<Location> tarea = client.getLastLocation();
        tarea.addOnSuccessListener(new OnSuccessListener<Location>()
        {
            @Override
            public void onSuccess(Location location)
            {
                    supportMapFragment.getMapAsync(new OnMapReadyCallback()
                    {
                        @Override
                        public void onMapReady(GoogleMap googleMap)
                        {
                            //inicia lat y log
                            LatLng latLng = new LatLng(location.getLatitude()
                                    , location.getLongitude());

                            //opciones de marcas
                            MarkerOptions options = new MarkerOptions()
                                    .position(latLng)
                                    .title("Aqui estoy!");

                            //zoom mapa & tipo
                            CameraPosition camPos = new CameraPosition.Builder()
                                    .target(latLng)
                                    .zoom(18.0f)
                                    .bearing(45)
                                    .tilt(70)
                                    .build();

                            CameraUpdate camUp = CameraUpdateFactory.newCameraPosition(camPos);
                            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            googleMap.animateCamera(camUp);

                            //agregar marca al mapa
                            googleMap.addMarker(options);
                        }
                    });
            }
        });
    }

}