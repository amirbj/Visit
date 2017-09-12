package com.andc.slidingmenu.Main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.andc.slidingmenu.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by win on 5/9/2015.
 */
    public class Map extends SupportMapFragment implements LocationListener {

    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    public LocationManager locationManager;
    private GoogleMap map;
    public Double longitude;
    public Double latitude;
    public String fromPlace;
    public MarkerOptions markerOptions;


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of map
     * @return
     */
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);


        definition();
        // not requested from cartable
        if(fromPlace==null)
            showMapUsingGPS();
        else
            showMapWithLocation();

        return rootView;
    }

    private void showMapWithLocation() {
        map = this.getMap();
        final Location location = new Location("");
        location.setLatitude(this.longitude);
        location.setLongitude(this.latitude);
        onLocationChanged(location);

    }

    private void definition() {
        Bundle location = getArguments();
        this.fromPlace = location.getString("map");
        if(this.fromPlace!=null) {
            this.longitude = Double.valueOf(location.getString("long"));
            this.latitude = Double.valueOf(location.getString("lat"));
        }
    }

    /**
     * this method drive location manager and find location
     * if not,this shows a notification to user as a toast
     */
    private void showMapUsingGPS() {
        if (!isGooglePlayServicesAvailable()) {
            return;
        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if( isGPSNetworkOn() ) {
            map = this.getMap();
            if (map != null) {
                map.setMyLocationEnabled(true);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null)
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null)
                    onLocationChanged(location);
                if(location==null){
                    Toast toast = Toast.makeText(getActivity().getBaseContext()
                            , getResources().getString(R.string.google_map), Toast.LENGTH_SHORT);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(30);
                    toast.show();
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 10, this);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 100, this);
            }
        }
    }

    /**
     *
     * @param location
     * this method get location and long/lat of location to add marker on map then show on camera
     */
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        map.clear(); // remove previous marker
        map.addMarker(new MarkerOptions().position(latLng));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    /**
     * there is 2 flag for gps and wifi
     * this method check if both of them are off then alert user to turn them on
     * @return
     */
    private boolean isGPSNetworkOn() {
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(this.getActivity())
                    .setTitle("Attention!")
                    .setMessage("Sorry, location is not determined. Please enable location providers")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
            return false;
        }
        return true;
    }

    /**
     * check if device can connect to google play return true
     * if can not return false so map does not show to him
     * @return
     */
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getActivity().getBaseContext());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this.getActivity(), 0).show();
            return false;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        Map map;

        public GeocoderTask(Map map) {
            this.map = map;
        }

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(this.map.getActivity().getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                Toast.makeText(this.map.getActivity().getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            Map.this.map.clear();

            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){

                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                Map.this.map.addMarker(markerOptions);

                // Locate the first location
                if(i==0)
                    Map.this.map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

}
