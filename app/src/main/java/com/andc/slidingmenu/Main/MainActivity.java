package com.andc.slidingmenu.Main;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.TextView;

import com.andc.slidingmenu.Dialogs.PreferencesFragment;
import com.andc.slidingmenu.Dialogs.UpdateFragment;
import com.andc.slidingmenu.Fragments.Cartable.Others.CartableFragment;
import com.andc.slidingmenu.Fragments.NavigationFragments.AboutFragment;

import com.andc.slidingmenu.Fragments.NavigationFragments.HelpFragment;
import com.andc.slidingmenu.Fragments.NavigationFragments.InternetBalance;
import com.andc.slidingmenu.Modals.NavDrawerItem;
import com.andc.slidingmenu.R;
import com.andc.slidingmenu.adapter.NavDrawerListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.Request;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.CartableTbl;
import slidingmenu.andc.com.dataaccess.CityTbl;
import slidingmenu.andc.com.dataaccess.ObservationListTbl;
import slidingmenu.andc.com.dataaccess.ObserveGroupTbl;
import slidingmenu.andc.com.dataaccess.RegionTbl;
import slidingmenu.andc.com.dataaccess.VillageTbl;




public class MainActivity extends AppCompatActivity {

    public static Context context;

    //User Preferences
    public String username;
    public String password;
    public String token;

    //Drawer Variables
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    public NavDrawerListAdapter mDrawerAdapter;
    public SharedPreferences preferences;
    public ProgressDialog builder;
    public AlertDialog.Builder dialog;

    //Search Variables
    CartableFragment mCartableFragment;
    InternetBalance mBalance;
    AboutFragment aboutFragment;
    HelpFragment helpFragment;
    ArrayList<CartableTbl> mCartableList;
    final String Table_Base = "BASE_MATERIAL_TBL";
    final String Table_City = "CITY_TBL";
    final String Table_Region = "REGION_TBL";
    final String Table_Village = "VILLAGE_TBL";
    final String Table_observeGroup = "OBSERVE_GROUP_TBL";
    enum WhichFragment {
        //HomeFragment,
        CartableFragment,
        //ReportsFragment,
        //RequestFragment,
        PreferencesDialog,
        MapActivity,
        UpdateDialog,
        getBaseInfo,
        InternetBalance,
        HelpFragment,
        AboutFragment,
        SignOut,
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Show Current Date
        //View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //TextView dateView = (TextView)rootView.findViewById(R.id.main_date);
        //SolarCalendar solarCalendar = new SolarCalendar();
        //dateView.setText(solarCalendar.getDescribedDateFormat());
         builder = new ProgressDialog(MainActivity.this);
        dialog = new AlertDialog.Builder(MainActivity.this);
        //Get UserName and Password + Token
        context = getApplicationContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.username = preferences.getString("username", "");
        this.password = preferences.getString("password", "");
        this.token = preferences.getString("token", "");
        Log.e("user = ", preferences.getString("username", ""));
        Log.e("token = ", preferences.getString("token", ""));


        //Set up Drawer Data
        mTitle = mDrawerTitle = getTitle();
        navMenuTitles = getResources().getStringArray(com.andc.slidingmenu.R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(com.andc.slidingmenu.R.array.nav_drawer_icons);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        addDrawerItems();
        navMenuIcons.recycle();
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        // toolbar.setTitle("نرم افزار بازدید از محل انشعاب");
        toolbar.setSubtitle("نسخه 1.1");

        //Set Up Drawer View and Listener
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_view);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                if(item!= null && item.getItemId()== android.R.id.home)

                {
                    if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);

                    }
                    else{
                        mDrawerLayout.openDrawer(Gravity.RIGHT);

                    }

                }
                return false;
            }
            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        //Set up Drawer List
        mDrawerList = (ListView) mDrawerLayout.findViewById(R.id.list_slidermenu);
        mDrawerAdapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());


        if (savedInstanceState == null)
            displayView(0);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mDrawerAdapter.notifyDataSetChanged();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * add item to sliding menu
     * titles and icons of items retreive from resources
     */
    private void addDrawerItems() {
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));

    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }
    }

    /**
     * @param position position show which item is selected by user from the drawer screen
     *                 switch case use Enumeration values, every one of them is mapped to one fragment
     *                 this activity passes 2 argument to every fragment which is created
     */
    private void displayView(int position) {
        Fragment fragment = null;
        WhichFragment whichFragment = WhichFragment.values()[position];
        switch (whichFragment) {

            case CartableFragment:
                fragment = new CartableFragment();
                mCartableFragment = (CartableFragment) fragment;
                break;

            case PreferencesDialog:
                fragment = new PreferencesFragment();
                ((PreferencesFragment) fragment).show(getSupportFragmentManager(), "Preferences");
                fragment = null;
                setTitle(navMenuTitles[position]);
                break;

            case MapActivity:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, 0);
                setTitle(navMenuTitles[position]);
                break;

            case UpdateDialog:
                fragment = new UpdateFragment();
                ((UpdateFragment) fragment).show(getSupportFragmentManager(), "Update");
                fragment = null;
                setTitle(navMenuTitles[position]);
                break;
            case getBaseInfo:
                getBaseInfo();
                setTitle(navMenuTitles[position]);
                break;


            case InternetBalance:
                fragment = new InternetBalance();
                mBalance = (InternetBalance) fragment;
                break;

            case HelpFragment:
                fragment = new HelpFragment();
                helpFragment = (HelpFragment) fragment;
                break;

            case AboutFragment:
                fragment = new AboutFragment();
                aboutFragment = (AboutFragment) fragment;
                break;


            case SignOut:
                SignOut();
                setTitle(navMenuTitles[position]);
                break;

            default:
                break;
        }

        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString("username", this.username);
            bundle.putString("password", this.password);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(com.andc.slidingmenu.R.id.frame_container, fragment).addToBackStack("fragback").commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    /**
     * when user wants to sign out user,pass,token and loggedin flag remove from preferences
     */
    @TargetApi(17)
    public void SignOut() {
        getResources().getDrawable(R.drawable.ic_drawer);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dialog_exit_title))
                .setMessage(getResources().getString(com.andc.slidingmenu.R.string.dialog_exit_body))
                .setNegativeButton(R.string.dialog_button_return, null)
                .setPositiveButton(R.string.dialog_button_exit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("isLoggedIn", "false");
                        editor.putString("isTimerCreated", "false");
                        editor.remove("username");
                        editor.remove("password");
                        editor.remove("token");
                        editor.apply();
                        MainActivity.this.finish();
                        Intent intent = new Intent();
                        setResult(1, intent);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

        //Set Title Gravity
        final int alertTitle = this.getResources().getIdentifier("alertTitle", "id", "android");
        TextView messageText = (TextView) dialog.findViewById(alertTitle);
        messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        //getSupportActionBar().setTitle(mTitle);
        toolbar.setTitle(mTitle);
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * @param item open and close drawer items
     * @return
     */
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null && item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        }

        if (item.getItemId() == R.id.menu_search) {
            //When User Wants To Search Get CartableList From DB
            mCartableList = new ArrayList<CartableTbl>();
            mCartableList.addAll(CartableTbl.listAll(CartableTbl.class));
        }
        return true;
    }
*/
    /**
     * back button handler
     * if there is a fragment in stack pop it
     * and if there is none ask a question from user to exit
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawers();

        }
            int count = getFragmentManager().getBackStackEntryCount();

            if (count == 0) {
                finish();
                //additional code
            } else {
                getFragmentManager().popBackStack();
            }

     /*       new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(com.andc.slidingmenu.R.string.exit))
                    .setMessage(getResources().getString(com.andc.slidingmenu.R.string.exit_confirm))
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            Log.e("user = ", preferences.getString("username", ""));
                            MainActivity.this.finish();
                        }
                    }).create().show();

*/
        }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == event.KEYCODE_BACK){
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawers();
            }
            else if(getSupportFragmentManager().getBackStackEntryCount() > 1) {
               getSupportFragmentManager().popBackStack(1,1);
                setTitle(navMenuTitles[0]);

            }else{
                this.finish();
            }
            return true;

        }
        return super.onKeyDown(keyCode , event);
    }


    /*
        This method is Getting Base Info from server
 */

    public void getBaseInfo() {

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String serverAddress = preferences.getString("webServicesAddress", "");
        String port = preferences.getString("port", "");
        String URL = String.format("http://" + serverAddress + ":" + port);

        restConn res = new restConn();
        res.execute(URL);
       /* final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("درخواست ورود");
        progressDialog.setMessage("لطفا چند لحظه صبر کنید...");
        progressDialog.show();

       Retrofit retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();


        ApiInterface apiService = retrofit.create(ApiInterface.class);
        Call<List<AndroidParameterNewFinalList>> call = apiService.getBsse(username, token);
        call.enqueue(new Callback<List<AndroidParameterNewFinalList>>() {
            @Override
            public void onResponse(Call<List<AndroidParameterNewFinalList>> call, Response<List<AndroidParameterNewFinalList>> response) {
                if (response.code() == 200 || response.code() == 201) {
                    Log.e("city code", "" + response.code());

                    for (AndroidParameterNewFinalList rs : response.body()) {
                        BaseInfoModel b = rs.getAndroidParameterNewFinalList().get(0);
                        Log.e("city code", "" + b.getDescription() + response.code());
                    }


                }
            }


            @Override
            public void onFailure(Call<List<AndroidParameterNewFinalList>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }



            //   Toast.makeText(MainActivity.this, b.getCityCode(), Toast.LENGTH_SHORT).show();


        }); */
    }
    public void inserttoObservegroup(){
        ObserveGroupTbl.deleteAll(ObserveGroupTbl.class);
        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Datatype = ?", "-1");
        for(BaseMaterialTbl observe: base) {
            ObserveGroupTbl observeGroupTbl = new ObserveGroupTbl();
            observeGroupTbl.GroupId = observe.Tablet_Code;
            observeGroupTbl.Title = observe.Description;
            observeGroupTbl.save();

        }

    }

    public  void insertObserveList(){
        ObservationListTbl.deleteAll(ObservationListTbl.class);
        ObserveGroupTbl.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_observeGroup + "'");
        List<ObserveGroupTbl> ob = ObserveGroupTbl.listAll(ObserveGroupTbl.class);
        for(int i = 0; i < ob.size(); i++) {
            int id = Integer.valueOf(ob.get(i).GroupId);
            List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "TabletMain_Code = ?", String.valueOf(id));
            for (int j=0; j<base.size(); j++) {
                ObservationListTbl list = new ObservationListTbl();
                list.GroupId = id;
                list.Observecode = base.get(j).Tablet_Code;
                list.DataType = base.get(j).Data_Type;
                list.Title = base.get(j).Description;
                list.TemplateType = base.get(j).Template_Type;
                list.save();



            }
        }

    }



    public String saveBase(String data){
        BaseMaterialTbl base1 = new BaseMaterialTbl();
        List<BaseMaterialTbl> material = base1.listAll(BaseMaterialTbl.class);
        if(material.size() >0) {
            base1.deleteAll(BaseMaterialTbl.class);
            base1.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_Base + "'");
        }

        RegionTbl region = new RegionTbl();
        List<RegionTbl> regionList = region.listAll(RegionTbl.class);
        if(regionList.size()>0){
            region.deleteAll(RegionTbl.class);
            region.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_Region + "'");

        }

        CityTbl city = new CityTbl();
        List<CityTbl> cityList = city.listAll(CityTbl.class);
        if(cityList.size()>0){
            city.deleteAll(CityTbl.class);
            city.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_City + "'");
        }

        VillageTbl village = new VillageTbl();
        List<VillageTbl> villageList = village.listAll(VillageTbl.class);
        if(villageList.size()>0){

            village.deleteAll(VillageTbl.class);
            VillageTbl.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_Village + "'");
        }

        try {
            JSONObject object = new JSONObject(data);
            JSONArray Androidparam = object.getJSONArray("AndroidParameterNewFinalList");
            JSONArray RegionList = object.getJSONArray("RegionList");
            JSONArray CityList = object.getJSONArray("CityList");
            JSONArray VillageList = object.getJSONArray("VillageList");

            for(int i=0; i<Androidparam.length();i++) {
                BaseMaterialTbl tbl = new BaseMaterialTbl();
                JSONObject obj = Androidparam.getJSONObject(i);
                tbl.Tablet_Code = obj.optInt("TabletCode");
                tbl.Data_Type = obj.optInt("DataType");
                tbl.Template_Type = obj.optInt("TemplateType");
                tbl.Description = obj.optString("Description");
                tbl.Tablet_MainCode = obj.optInt("TabletMainCode");
                tbl.save();

            }
            for(int i=0; i<RegionList.length();i++){
                RegionTbl regionTbl = new RegionTbl();
                JSONObject obj = RegionList.getJSONObject(i);
                regionTbl.RgnCode = obj.optInt("RegionCode");
                regionTbl.RgnName = obj.optString("RegionName");
                regionTbl.save();

            }
            for(int i=0; i<CityList.length();i++){
                CityTbl cityTbl = new CityTbl();
                JSONObject obj = CityList.getJSONObject(i);
                cityTbl.RgnCode = obj.optInt("RegionCode");
                cityTbl.CityCode = obj.optInt("CityCode");
                cityTbl.CityName = obj.optString("CityName");
                cityTbl.save();


            }
            for(int i=0; i<VillageList.length();i++){
                VillageTbl villageTbl = new VillageTbl();
                JSONObject obj = VillageList.getJSONObject(i);
                villageTbl.RgnCode = obj.optInt("RgnCode");
                villageTbl.CityCode = obj.optInt("CityCode");
                villageTbl.VillageCode = obj.optInt("VillageCode");
                villageTbl.VillageName = obj.optString("VillageName");
                villageTbl.save();



            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<BaseMaterialTbl> materiallist = base1.listAll(BaseMaterialTbl.class);
        List<RegionTbl> regionList1 = region.listAll(RegionTbl.class);
        List<CityTbl> cityList1 = city.listAll(CityTbl.class);
        List<VillageTbl> villageList1 = village.listAll(VillageTbl.class);
        String msg = " اطلاعات پایه = " + String.valueOf(materiallist.size())+"رکورد"
                + " \n  اطلاعات مناطق = " + String.valueOf(regionList1.size())+"رکورد"
                +" \n  اطلاعات شهرها = " + String.valueOf(cityList1.size()) +"رکورد"
                + "\n اطلاعات روستاها = " + String.valueOf(villageList1.size())+ "رکورددریافت شد";
        builder.dismiss();
        return  msg;

    }

    public class restConn extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {

            builder.setIndeterminate(true);
            builder.setMessage("درحال گرفتن اطلاعات پایه از سرور...");
            builder.setCancelable(false);
            builder.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            String s = null;
            final Request request = new Request.Builder()
                    .url(params[0] +"/api/BaseService/GetVisitParameters")
                    // .addHeader("Accept", "application/json")
                    .addHeader("UserName", username)
                    .addHeader("Token", token)

                    .build();

            try {
               OkHttpClient client = new OkHttpClient();
                okhttp3.Response res = client.newCall(request).execute();
                s= res.body().string();

                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
            }


            if(s != null){

               final String message = saveBase(s);
                inserttoObservegroup();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setTitle("اطلاعات پایه");
                        dialog.setMessage(message);
                        dialog.show();

                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            insertObserveList();


        }
    }

}


