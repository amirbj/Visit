package com.andc.slidingmenu.Main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.andc.slidingmenu.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Random;

import slidingmenu.andc.com.dataaccess.CartableTbl;


public class MapActivity extends AppCompatActivity {

    public static String EXTRA_COMMAND = "view_code";

    private GoogleMap mMap;
    private static final String myTAG = "LocationTest";
    private static final String myTAG2 = "GPSFeed";
    public String mViewCode;
    LocationManager locationManager;
    LocationListener locationListener;
    Location myLocation;
    ArrayList<CartableTbl> mCartableList;

    //Marker Arguments
    private double mLatitude = 0;
    private double mLongitude = 0;
    private String mTitle;
    private String mSnippet;

    @TargetApi(21)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_map);

        if (getIntent().getExtras() != null)
            mViewCode = getIntent().getExtras().getString(EXTRA_COMMAND);

        //Toolbar toolbar = (Toolbar) getLayoutInflater().inflate(R.layout.toolbar_map, null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initiateMap();
        setLocationService();
        setUpMap(null);
    }

    public void onResume() {
        super.onResume();

        //Register a Listener to request updates from GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListener);
        Log.d(myTAG2, "Started Requesting Location");
    }

    public void onPause() {
        super.onPause();
        //Stop Requesting Updates from GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);
        Log.d(myTAG2, "Stoped Requesting Location");
    }

    public void setLocationService(){
        locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        //GPS prompt
        if(!(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) || !(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("To Use This Feature,\n Please Turn On GPS ");
            dialog.setPositiveButton("Turn on GPS", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });

            dialog.setNegativeButton("No, Thanks", new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });
            dialog.show();
        }
    }

    public void initiateMap(){
        if (mMap == null) {
            FragmentManager myFM =getSupportFragmentManager();
            final SupportMapFragment myMAPF = (SupportMapFragment) myFM
                    .findFragmentById(R.id.mapall);
            mMap=myMAPF.getMap();
        }
    }

    private void setUpMap(Location location) {
        //Return if the map isn't Available
        if (mMap == null)
            return;

        //Clear Map to Draw new Markers
        mMap.clear();

        //When GPS is down use a default location until an accurate location is received
        if(location == null){
            myLocation = new Location("myLocation");
            myLocation.setLatitude(35.741329);
            myLocation.setLongitude(51.337025);
        } else{
            myLocation = location;

            //Add Marker Only if MyLocation is Available
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()))
                    .title("My Location")
                    .snippet("You Are Here!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
        }

        //Get List of Cartable
        mCartableList = new ArrayList<CartableTbl>();
        mCartableList.addAll(CartableTbl.listAll(CartableTbl.class));

        //Add a marker to map for each Cartable
        for(int j=0; j<mCartableList.size(); j++)
        {
            CartableTbl mCartableItem = mCartableList.get(j);

           // mTitle = mCartableItem.subscriptionName;
        //    mSnippet = mCartableItem.requestNumber;
            //mLatitude = Double.parseDouble(mCartableItem.latitude);
            //mLongitude = Double.parseDouble(mCartableItem.longitude);

            //Use Mock Locations for DEMO
            double minLat = 35.627313;
            double maxLat = 35.770065;

            double minLong = 51.254126;
            double maxLong = 51.488959;

            Random r = new Random();
            mLatitude = minLat + ((maxLat - minLat) * r.nextDouble());
            mLongitude = minLong + ((maxLong - minLong) * r.nextDouble());

            //Log.d(myTAG, "Marker " + j + ": Latitude = " + mLatitude + " | Longitude= " + mLongitude);


            //Add the Cartable Location To map if location is provided
            if(mLatitude!=0 && mLongitude!=0 )
            {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mLatitude, mLongitude))
                        .title("\u200e" + mTitle)
                        .snippet(mSnippet));
                        //.snippet("\u200e" + mSnippet + ":شماره درخواست" ));
            }
        }

        //Enter the Cartable of Selected Pin
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String mRequestNumber = marker.getSnippet();
                Intent intent = new Intent(getApplicationContext(),CartableItemActivity.class);
                intent.putExtra("requestnumber", mRequestNumber);
                startActivity(intent);
            }
        });


        //Set up Camera
        setUpCamera();
        Log.d(myTAG2, "Map Updated!");
    }

    public void setUpCamera(){
        if(mViewCode==null||mViewCode=="") {

        LatLngBounds center = new LatLngBounds(
                new LatLng(myLocation.getLatitude(), myLocation.getLongitude())
                ,new LatLng(myLocation.getLatitude(), myLocation.getLongitude())
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center.getCenter(), 13));

        } else{

        //    CartableListDB mViewedCartable = CartableListDB.find(CartableListDB.class, "request_Number = ?", String.valueOf(mViewCode)).get(0);

            //Double mLatitude = Double.parseDouble(mViewedCartable.latitude);
            //Double mLongitude = Double.parseDouble(mViewedCartable.longitude);

            LatLngBounds center = new LatLngBounds(
                    new LatLng(mLatitude, mLongitude)
                    ,new LatLng(mLatitude, mLongitude)
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center.getCenter(), 16));

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class MyLocationListener implements LocationListener{

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(myTAG2, "Provider Enabled!");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(myTAG2, "Provider Disabled!");
        }

        @Override
        public void onLocationChanged(Location location) {
            //Toast.makeText(getApplicationContext(), "Location Updated to = ("
            //		+ location.getLatitude() + " , " + location.getLongitude()
            //		+ ") Provider= " + location.getProvider() , Toast.LENGTH_LONG).show();

            //Update Map Markers Near User based on new Location
            Log.d(myTAG2, "Updating Map Using New Location ("
                    + location.getLatitude() + ", " + location.getLongitude()
                    + ") Provider= " + location.getProvider());
            setUpMap(location) ;
        }
    }

}
