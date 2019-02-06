package com.example.user.stillwalk.helperclasses;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.user.stillwalk.classes.SmsPage;


public class GPS implements PermissionCheck{

    private static boolean checkForPermission = false;
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location my_location;
    private String my_provider;

    public GPS(Context context) {
        this.context = context;

        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            checkForPermission = true;
        }
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                my_location = location;
                for (Button b : SmsPage.sosButtons){
                    b.setVisibility(View.VISIBLE);
                }
                SmsPage.progressBar.setVisibility(View.GONE);
                SmsPage.loadingText.setVisibility(View.GONE);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                my_provider = provider;
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    public Location getLocation(){

        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates("gps",0,0,locationListener);
        }

        return my_location;
    }
    @Override
    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(context,permission);
        return check == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCheckForPermission() {
        return checkForPermission;
    }

    public String getMy_provider() {
        return my_provider;
    }
}
