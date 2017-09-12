package com.andc.slidingmenu.Main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.andc.slidingmenu.Fragments.Cartable.DemandedBranch.CartableDemandedBranch;
import com.andc.slidingmenu.Fragments.Cartable.Materials.CartableMaterials;
import com.andc.slidingmenu.Fragments.Cartable.Others.CartableElectricSupply;
import com.andc.slidingmenu.Fragments.Cartable.Others.CartableObservations;
import com.andc.slidingmenu.Fragments.Cartable.Others.CartablePlaceInfo;
import com.andc.slidingmenu.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import slidingmenu.andc.com.dataaccess.CartableTbl;

/**
 * Created by win on 10/11/2015.
 */
public class CartableItemActivity extends AppCompatActivity  {

    //Tags
    final String TAG_STEP1 = "Info";
    final String TAG_STEP2 = "Demand";
    final String TAG_STEP3 = "Supply";
    final String TAG_STEP4 = "Observation";
    final String TAG_STEP5 = "Tools";

    //Flags
    private boolean collpaseDemoed = false;

    //ViewPager Vars
    private MyViewPagerAdaptor mAdaptor;
    public ViewPager mViewPager;


    //Control Buttons
    //private Button mNextButton;
    //private Button mPreviousButton;
    private FloatingActionButton mFinishButton;

    //Steps Buttons
    private Button mStep1;
    private Button mStep2;
    private Button mStep3;
    private Button mStep4;
    private Button mStep5;

    //Global Variables
    private long mRequestNumber;
    List<CartableTbl> mCartableList;
    private CartableTbl mCartable;
    public Callbacks mCallbacks;
    AppBarLayout appBarLayout;

    //Map Variable
    private GoogleMap mMap;
    private static final String myTAG = "LocationTest";
    private static final String myTAG2 = "GPSFeed";
    public long  mViewCode;
    LocationManager locationManager;
    LocationListener locationListener;
    Location myLocation;

    //Marker Variables
    private double mLatitude = 0;
    private double mLongitude = 0;
    private String mTitle;
    private String mSnippet;

    /**
     * Required interface for hosting activities.
     * Use mCallbacks = (Callbacks)fragment; to get Desired Callback
     * and set mCallbacks = null; after you are Done
     */
    public interface Callbacks {
        public boolean isValid();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.andc.slidingmenu.R.layout.activity_cartable_item);


        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get Request Number and Related Cartable
        mRequestNumber = getIntent().getExtras().getLong("requestnumber");
        mCartableList = CartableTbl.find(CartableTbl.class, "Request_Code = ?", String.valueOf(mRequestNumber));
        mCartable = mCartableList.get(0);
        mViewCode = mRequestNumber;

        //Set up Map Toolbar
        //initiateMap();
        setLocationService();
        setUpMap(null);

        //Set up the Rest
        defineButtons();
        setUpViewPager();

        mViewPager.setCurrentItem(4);
    }

    //--------------------CallBacks--------------------
    @Override
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

    @Override
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


    @Override
    @TargetApi(17)
    public void onBackPressed() {

        //Give an Alert to Register Cartable if it is Unregistered
        if(mCartable.Status == 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    //.setView(mDialogView)
                    .setTitle(getResources().getString(R.string.dialog_back_title))
                    .setMessage(getResources().getString(R.string.dialog_back_body))
                    .setNegativeButton(getResources().getString(R.string.dialog_button_return), null)
                    .setPositiveButton(getResources().getString(R.string.dialog_button_exit), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            CartableItemActivity.super.onBackPressed();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

            //Set Title Gravity
            final int alertTitle = getResources().getIdentifier("alertTitle", "id", "android");
            TextView messageText = (TextView) dialog.findViewById(alertTitle);
            //messageText.setGravity(Gravity.Right);
            messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        } else if(mCartable.Status == 9) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    //.setView(mDialogView)
                    .setTitle(getResources().getString(R.string.dialog_back_title))
                    .setMessage("اطالاعات به صورت ناقص ذخیره شده است")
                    .setNegativeButton(getResources().getString(R.string.dialog_button_return), null)
                    .setPositiveButton(getResources().getString(R.string.dialog_button_exit), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            CartableItemActivity.super.onBackPressed();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

            //Set Title Gravity
            final int alertTitle = getResources().getIdentifier("alertTitle", "id", "android");
            TextView messageText = (TextView) dialog.findViewById(alertTitle);
            //messageText.setGravity(Gravity.Right);
            messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        else {
            finishHandler();
        }
    }

    //--------------------Activity Setup--------------------
    @TargetApi(17)
    public void defineButtons(){
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
/*

        mNextButton = (Button) findViewById(R.id.cartable_button_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mCurrentStep = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(mCurrentStep - 1);
            }
        });

        mPreviousButton = (Button) findViewById(R.id.cartable_button_previous);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mCurrentStep = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(mCurrentStep + 1);
            }
        });
*/

        final CartableItemActivity mActivity = this;
        mFinishButton = (FloatingActionButton) findViewById(R.id.cartable_button_finish);
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                        .setTitle(mActivity.getResources().getText(R.string.dialog_cartable_title))
                        .setMessage(mActivity.getResources().getText(R.string.dialog_cartable_body))
                        .setNegativeButton(mActivity.getResources().getText(R.string.dialog_button_return), null)
                        .setPositiveButton(mActivity.getResources().getText(R.string.dialog_button_confirm), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                finishHandler();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

                //Set Title Gravity
                final int alertTitle = mActivity.getResources().getIdentifier("alertTitle", "id", "android");
                TextView messageText = (TextView) dialog.findViewById(alertTitle);
                messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        });

        mStep1 = (Button) findViewById(R.id.cartable_step_1);
        mStep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(4);
            }
        });
        mStep2 = (Button) findViewById(R.id.cartable_step_2);
        mStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(3);
            }
        });

        mStep3 = (Button) findViewById(R.id.cartable_step_3);
        mStep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });

        mStep4 = (Button) findViewById(R.id.cartable_step_4);
        mStep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });

        mStep5 = (Button) findViewById(R.id.cartable_step_5);
        mStep5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
    }

    public void managePreviousSteps(int currentStep){

        //Reset All Steps to Default
        mStep1.setEnabled(false);
        mStep2.setEnabled(false);
        mStep3.setEnabled(false);
        mStep4.setEnabled(false);
        mStep5.setEnabled(false);

        mStep1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_next), null, null);
        mStep2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_next), null, null);
        mStep3.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_next), null, null);
        mStep4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_next), null, null);
        mStep5.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_next), null, null);

        mStep1.setTextColor(getResources().getColor(android.R.color.darker_gray));
        mStep2.setTextColor(getResources().getColor(android.R.color.darker_gray));
        mStep3.setTextColor(getResources().getColor(android.R.color.darker_gray));
        mStep4.setTextColor(getResources().getColor(android.R.color.darker_gray));
        mStep5.setTextColor(getResources().getColor(android.R.color.darker_gray));


        //Steps Variables
        Drawable mIcon;
        int mColor;
        List<Fragment> mFragments = getSupportFragmentManager().getFragments();
        int previousSteps = ++currentStep;
        switch(previousSteps){
            case 0:
                //No Not Need to prepare this Step As Step 5 is the Last Step
                mCallbacks = (Callbacks)(getSupportFragmentManager().getFragments().get(5));
                mIcon = (mCallbacks.isValid() ? getResources().getDrawable(R.drawable.step_completed) : getResources().getDrawable(R.drawable.step_error));
                mColor = (mCallbacks.isValid() ? getResources().getColor(android.R.color.holo_green_dark) : getResources().getColor(android.R.color.holo_orange_dark));

                mStep5.setEnabled(true);
                mStep5.setTextColor(mColor);
                mStep5.setCompoundDrawablesWithIntrinsicBounds(null, mIcon, null, null);

            case 1:
                mCallbacks = (Callbacks)(getSupportFragmentManager().getFragments().get(4));
                mIcon = (mCallbacks.isValid() ? getResources().getDrawable(R.drawable.step_completed) : getResources().getDrawable(R.drawable.step_error));
                mColor = (mCallbacks.isValid() ? getResources().getColor(android.R.color.holo_green_dark) : getResources().getColor(android.R.color.holo_orange_dark));

                mStep4.setEnabled(true);
                mStep4.setTextColor(mColor);
                mStep4.setCompoundDrawablesWithIntrinsicBounds(null, mIcon, null, null);

            case 2:
                mCallbacks = (Callbacks)(getSupportFragmentManager().getFragments().get(3));
                mIcon = (mCallbacks.isValid() ? getResources().getDrawable(R.drawable.step_completed) : getResources().getDrawable(R.drawable.step_error));
                mColor = (mCallbacks.isValid() ? getResources().getColor(android.R.color.holo_green_dark) : getResources().getColor(android.R.color.holo_orange_dark));

                mStep3.setEnabled(true);
                mStep3.setTextColor(mColor);
                mStep3.setCompoundDrawablesWithIntrinsicBounds(null, mIcon, null, null);

            case 3:
                mCallbacks = (Callbacks)(getSupportFragmentManager().getFragments().get(2));
                mIcon = (mCallbacks.isValid() ? getResources().getDrawable(R.drawable.step_completed) : getResources().getDrawable(R.drawable.step_error));
                mColor = (mCallbacks.isValid() ? getResources().getColor(android.R.color.holo_green_dark) : getResources().getColor(android.R.color.holo_orange_dark));

                mStep2.setEnabled(true);
                mStep2.setTextColor(mColor);
                mStep2.setCompoundDrawablesWithIntrinsicBounds(null, mIcon, null, null);

            case 4:
                mCallbacks = (Callbacks)(getSupportFragmentManager().getFragments().get(1));
                mIcon = (true ? getResources().getDrawable(R.drawable.step_completed) : getResources().getDrawable(R.drawable.step_error));
                mColor = (true ? getResources().getColor(android.R.color.holo_green_dark) : getResources().getColor(android.R.color.holo_orange_dark));

                mStep1.setEnabled(true);
                mStep1.setTextColor(mColor);
                mStep1.setCompoundDrawablesWithIntrinsicBounds(null, mIcon, null, null);
                break;

            default:
                //No Prep Needed!
                break;
        }

    }

    public void setUpViewPager(){
        //Set View Pager
        mAdaptor = new MyViewPagerAdaptor(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(com.andc.slidingmenu.R.id.pager);
        mViewPager.setAdapter(mAdaptor);

        //Set Page Limit to 4 so All fragments remain Attach During Activity Life Cycle
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                managePreviousSteps((position));

                //Fade out Animation
                Animation anim = null;
                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_out);
                anim.setDuration(300);


                switch (position) {

                    //Step1
                    case 4:
                        //mNextButton.setVisibility(View.VISIBLE);
                        //mPreviousButton.startAnimation(anim);
                        //mPreviousButton.setVisibility(View.GONE);
                        mFinishButton.hide();

                        mStep1.setEnabled(true);
                        mStep1.setTextColor(getResources().getColor(android.R.color.black));
                        mStep1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_current), null, null);
                        break;

                    //Step2
                    case 3:
                   /*     if(!collpaseDemoed) {
                            appBarLayout.setExpanded(false,true);
                            collpaseDemoed = true;
                        }*/

                        //mNextButton.setVisibility(View.VISIBLE);
                        //mPreviousButton.setVisibility(View.VISIBLE);
                        mFinishButton.hide();

                        mStep2.setEnabled(true);
                        mStep2.setTextColor(getResources().getColor(android.R.color.black));
                        mStep2.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_current), null, null);
                        break;

                    //Step3
                    case 2:
                        //mNextButton.setVisibility(View.VISIBLE);
                        //mPreviousButton.setVisibility(View.VISIBLE);

                        mFinishButton.hide();
                        mStep3.setEnabled(true);
                        mStep3.setTextColor(getResources().getColor(android.R.color.black));
                        mStep3.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_current), null, null);
                        break;

                    //Step4
                    case 1:
                        //mNextButton.setVisibility(View.VISIBLE);
                        //mPreviousButton.setVisibility(View.VISIBLE);
                        mFinishButton.hide();

                        mStep4.setEnabled(true);
                        mStep4.setTextColor(getResources().getColor(android.R.color.black));
                        mStep4.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_current), null, null);
                        break;

                    //Step5
                    case 0:
                        //mNextButton.startAnimation(anim);
                        //mNextButton.setVisibility(View.GONE);
                        //mPreviousButton.setVisibility(View.VISIBLE);
                        mFinishButton.show();

                        mStep5.setEnabled(true);
                        mStep5.setTextColor(getResources().getColor(android.R.color.black));
                        mStep5.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_current), null, null);
                        break;

                    default:
                        //mPreviousButton.startAnimation(anim);
                        //mPreviousButton.setVisibility(View.GONE);
                        mFinishButton.hide();

                        mStep1.setEnabled(true);
                        mStep1.setTextColor(getResources().getColor(android.R.color.black));
                        mStep1.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.step_current), null, null);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * Set the Cartable Status
     * And Finish the Activity
     */
    public void finishHandler() {
        /**
         * In Order to save Cartable State
         * Get Fresh Cartable
         * to Avoid Overwriting
         */
        mCartableList = CartableTbl.find(CartableTbl.class, "Request_Code = ?", String.valueOf(mRequestNumber));
        mCartable = mCartableList.get(0);

        //Leave Send Cartable Unchanged
        if (mCartable.Status == 2) {
            this.finish();
        } else {
            //Validate Visited or Faulty Cartable for Changes
            if (isCartableValid()) {
                mCartable.Status = 1;
            } else {
                mCartable.Status = 9;
            }

            mCartable.save();
        }
        this.finish();
    }

    /**
     * Check to see if any step of cartable contains Error
     * Return False on Error
     */
    public boolean isCartableValid(){
        for(int step = 1; step <= 4 ; step++){
            mCallbacks = (Callbacks)(getSupportFragmentManager().getFragments().get(step));
            if(!mCallbacks.isValid())
                return false;
        }
        return true;
    }

    //--------------------Map--------------------
    public void setLocationService(){
        locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        //GPS prompt
        if(!(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) || !(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("To Use This Feature,\n Please Turn On GPS ");
            dialog.setPositiveButton("Turn on GPS", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });

            dialog.setNegativeButton("No, Thanks", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                }
            });
            dialog.show();
        }
    }

  /* public void initiateMap(){
        if (mMap == null) {
            FragmentManager myFM =getSupportFragmentManager();
            final SupportMapFragment myMAPF = (SupportMapFragment) myFM
                    .findFragmentById(R.id.map);
            mMap=myMAPF.getMap();
        }
    }*/

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

            mTitle = mCartableItem.Name;
            mSnippet = String.valueOf(mCartableItem.RequestCode);
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
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
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
        if(mViewCode== 0||String.valueOf(mViewCode).equals("")) {

            LatLngBounds center = new LatLngBounds(
                    new LatLng(myLocation.getLatitude(), myLocation.getLongitude())
                    ,new LatLng(myLocation.getLatitude(), myLocation.getLongitude())
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center.getCenter(), 13));

        } else{

            CartableTbl mViewedCartable = CartableTbl.find(CartableTbl.class, "Request_Code = ?", String.valueOf(mViewCode)).get(0);

            //Double mLatitude = Double.parseDouble(mViewedCartable.latitude);
            //Double mLongitude = Double.parseDouble(mViewedCartable.longitude);

            LatLngBounds center = new LatLngBounds(
                    new LatLng(mLatitude, mLongitude)
                    ,new LatLng(mLatitude, mLongitude)
            );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center.getCenter(), 16));

        }
    }


    //--------------------Inner Classes--------------------

    public class MyViewPagerAdaptor extends FragmentPagerAdapter{
        public MyViewPagerAdaptor(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount(){
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            Bundle args = new Bundle();
            args.putLong("requestnumber", mRequestNumber);

            switch(position){
                //Step1
                case 4:
                    fragment = new CartablePlaceInfo();
                    fragment.setArguments(args);
                    return fragment;

                //Step2
               case 3:
                    fragment = new CartableDemandedBranch();
                    fragment.setArguments(args);
                    return fragment;

                //Step3
                 case 2:
                    fragment = new CartableElectricSupply();
                    fragment.setArguments(args);
                    return fragment;

                //Step4
                case 1:
                    fragment = new CartableObservations();
                    fragment.setArguments(args);
                    return fragment;

                //Step5
                case 0:
                    fragment = new CartableMaterials();
                    fragment.setArguments(args);
                    return fragment;


                default:
                    fragment = new CartablePlaceInfo();
                    fragment.setArguments(args);
                    return fragment;

            }
        }

        public Fragment getFragment(int position){
            Fragment myFragment =  getSupportFragmentManager().getFragments().get(position);
            return myFragment;
        }
    }

    public class MyLocationListener implements LocationListener{

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
