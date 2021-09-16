package com.example.shakemaps;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private MainActivityViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validaPermisos();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        model = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MainActivityViewModel.class);

        model.getStatusSensorMutable().observe(this, new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                finish();
            }
        });

        model.getAgiteMutable().observe(this, new Observer<String>()
        {
            @Override
            public void onChanged(String s)
            {
                Intent intent = new Intent(MainActivity.this, PositionMaps.class);
                startActivity(intent);
            }
        });

        model.status(sensor);

        sensorEventListener = new SensorEventListener()
        {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent)
            {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                long curTime = System.currentTimeMillis();
                model.sensorAgite(x,y,z,curTime);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy)
            {

            }
        };
        
        iniciar();
    }



    private void validaPermisos()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, 100);

        }
    }

    protected void iniciar()
    {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void parar()
    {
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onResume()
    {
        iniciar();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        parar();
        super.onPause();
    }
}