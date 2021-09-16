package com.example.shakemaps;

import android.content.Intent;
import android.hardware.Sensor;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel
{
    private static final int SHAKE_THRESHOLD = 600;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private MutableLiveData<String> agiteMutable;
    private MutableLiveData<String> statusSensor;

    public LiveData<String> getAgiteMutable()
    {
        if (agiteMutable == null)
            agiteMutable = new MutableLiveData<>();
        return agiteMutable;
    }

    public LiveData<String> getStatusSensorMutable()
    {
        if (statusSensor == null)
            statusSensor = new MutableLiveData<>();
        return statusSensor;
    }

    public void status(Sensor sensor)
    {
        if(sensor == null)
        {
            statusSensor.setValue("finalizar");
        }
    }

    public void sensorAgite(float x, float y, float z, long curTime)
    {
        if ((curTime - lastUpdate) > 100)
        {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float move = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

            if (move > SHAKE_THRESHOLD)
            {
                agiteMutable.setValue("ok");
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }
}
