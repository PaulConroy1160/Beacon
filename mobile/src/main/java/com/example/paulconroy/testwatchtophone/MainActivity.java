package com.example.paulconroy.testwatchtophone;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mLocationClient;
    GoogleMap mMap;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    Button getLocationBTN;
    Button scan;
    Location myLocation;
    ImageButton refresh;


    TextView logo;
    TextView status;
    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //getLocationBTN = (Button) findViewById(R.id.location);

        typeFace = Typeface.createFromAsset(getAssets(), "fonts/muli.ttf");

        refresh = (ImageButton) findViewById(R.id.refresh);
        status = (TextView) findViewById(R.id.status);
        status.setTypeface(typeFace);
        scan = (Button) findViewById(R.id.scan);
        scan.setTypeface(typeFace);
        scan.setVisibility(View.GONE);
        logo = (TextView) findViewById(R.id.logoFont);
        logo.setTypeface(typeFace);

        status.setText("Calibrating...");

        if (serviceIsOK()) {
            initMap();

            mLocationClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            mLocationClient.connect();
        }


        //Toast.makeText(this,"Finding location...",Toast.LENGTH_LONG).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                getCurrentLocation();

            }
        }, 1000);

    }

    public boolean serviceIsOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect", Toast.LENGTH_LONG).show();
        }

        return false;
    }

    private void initMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFragment.getMap();
            mMap.getUiSettings().setAllGesturesEnabled(false);
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        //Toast.makeText(this,"connected",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void getCurrentLocation() {
        Location currentLocation = LocationServices.FusedLocationApi
                .getLastLocation(mLocationClient);
        if (currentLocation == null) {
            Toast.makeText(this, "can't connect to find location", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this,"Location found",Toast.LENGTH_LONG).show();
            LatLng latLng = new LatLng(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude()
            );
            //15 recommended
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(
                    latLng, 15
            );


            mMap.animateCamera(update);
            myLocation = currentLocation;

            enableScan();
            Log.d("Lat is:", " " + myLocation.getLatitude());
            Log.d("Long is:", " " + myLocation.getLongitude());

        }
    }

    public void addMarker(View v) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                .title("Beacon")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markericon)));
    }

    public void timeLineStart(View v) {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }

    public void enableScan() {
        scan.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_left);
        scan.startAnimation(animation);


    }

    public void disableScan() {

        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.slide_out_left);
        scan.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scan.setVisibility(View.GONE);
                statusAnimate();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void refresh(View v) {
        disableScan();
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.rotate);
        refresh.startAnimation(animation);


    }

    public void statusAnimate() {

        status.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.slide_in_left);
        status.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                status.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
