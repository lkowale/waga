package com.waga;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by pandejo on 28.02.2018.
 */

public class GPSLocator {

    LocationManager locationManager;
    Location lastLocation,punktPoczatkowy;
    float total_distance,distance;
    boolean updated=false;
    boolean mierzy=false;

    static long  MIN_TIME_BW_UPDATES = 1000;
    static float MIN_DISTANCE_CHANGE_FOR_UPDATES=0;
    TextView tvLong,tvLang;
    long lastTime;
    int speed,szerokosc_siewu;
    int wagaPoczatkowa,roznica_wagi;
    float odleglosc;

    public GPSLocator(Context mContext, final TextView tlong, final TextView tlati, final TextView tdistance,int szer_siewu)
    {
        tvLong=tlong;
        tvLang=tlati;
        szerokosc_siewu=szer_siewu;
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
//                makeUseOfNewLocation(location);
                tlong.setText("Dokładność: "+String.valueOf(Math.round(location.getAccuracy()))+"m");
                tlati.setText("Satelit: "+String.valueOf(location.getExtras().getInt("satellites")));

                if(lastLocation!=null) {
                    distance=location.distanceTo(lastLocation);
                    total_distance +=distance;
                    updated=true;
                    long millis = System.currentTimeMillis();
                    speed=Math.round(3600*distance/(millis-lastTime));//km/h
                    lastTime= millis;
                }

                lastLocation=location;
                lastTime=System.currentTimeMillis();

                tdistance.setText("Droga: "+String.valueOf(Math.round(total_distance)));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);


//        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        Log.d("GPS Enabled", "GPS Enabled");

    }

    public int getSpeed()
    {
        if(updated)
            updated=false;
        else
            speed=0;
            return speed;
    }
    public void rozpocznijPomiar(int waga){
        wagaPoczatkowa=waga;
        punktPoczatkowy=lastLocation;
        mierzy=true;
    }

    public int zakonczPomiar(int waga) {
        mierzy = false;
        if (lastLocation != null && punktPoczatkowy != null) {
            odleglosc = lastLocation.distanceTo(punktPoczatkowy);
            roznica_wagi=wagaPoczatkowa - waga;
            if (odleglosc > 0)
                return Math.round(roznica_wagi * 10000 / (odleglosc * szerokosc_siewu));
        }

        return 0;
    }
}
