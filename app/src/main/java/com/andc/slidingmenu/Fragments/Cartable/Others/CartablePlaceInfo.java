package com.andc.slidingmenu.Fragments.Cartable.Others;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;

import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

import java.util.List;


import com.andc.slidingmenu.Calendar.Solar;
import com.andc.slidingmenu.Main.Map;
import com.andc.slidingmenu.Main.CartableItemActivity;
import com.andc.slidingmenu.Main.MapActivity;
import com.andc.slidingmenu.Utility.ConvertorNumberToPersian;
import com.andc.slidingmenu.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import ir.smartlab.persindatepicker.PersianDatePicker;
import ir.smartlab.persindatepicker.util.PersianCalendar;
import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;

import slidingmenu.andc.com.dataaccess.CartableTbl;

import slidingmenu.andc.com.dataaccess.CityTbl;
import slidingmenu.andc.com.dataaccess.LocationTbl;
import slidingmenu.andc.com.dataaccess.VillageTbl;


/**
 * Created by win on 4/21/2015.
 */
public class CartablePlaceInfo extends Fragment implements CartableItemActivity.Callbacks , AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    ConvertorNumberToPersian convertorNumberToPersian;
    public int shoingMap = 10;
    public TextView visitDate;
    public long requestNumber;
    public EditText floorCount;
    public EditText unitCount;
    public EditText totalArea;
    public EditText baseArea, familyNo, nextLeft, nextRight;
    public CheckBox isVillage;
    public Spinner citiesSpinner, IndustrialCities, villagesSpinner;
    public TextView name;
    public TextView date;
    public LocationTbl thisItem;
    public CartableTbl thisPerson;
    public EditText longitude;
    public EditText latitude;
    public ArrayList<String> cityName;
    public Button showMap;
    public RadioGroup radio , radioVaziat, radiomahdude;
    public RadioButton dakhelmahdude, kharejmahdude, shomali, jonubi;
    public RadioButton faghedbana, darsakht, jadidsakht, ghadimi;



    @Override
    public boolean isValid(){

        return true;
    }



    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of place info
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visit_place_info, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        definition(rootView);
        //spinnerHandling();
        loadData();
        setListeners(rootView);
        visitDateHandling(visitDate, inflater, container);



        return rootView;
    }



    /**
     * when you click on show map button
     * app shows map useing long and lat
     * @param cartablePlaceInfo
     */
    private void mapHandling(final CartablePlaceInfo cartablePlaceInfo) {
        this.showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isGooglePlayServicesAvailable()) {
                    return;
                }

                Fragment fragment = new Map();
                Bundle info = new Bundle();
                info.putLong("long",thisItem.LastX);
                info.putLong("lat",thisItem.LastY);
                info.putString("map","true");
                fragment.setArguments(info);
                fragment.setTargetFragment(cartablePlaceInfo, shoingMap);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().add(R.id.frame_container, fragment).addToBackStack(null).commit();

            }
        });
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

    /**
     * @param rootView
     * find any views on cartable view by rootView, get bundles from previous fragment and just define variables
     */
    private void definition(View rootView) {
        convertorNumberToPersian = new ConvertorNumberToPersian();

        this.visitDate = (TextView)rootView.findViewById(R.id.place_info_visit_date);
        this.floorCount = (EditText) rootView.findViewById(R.id.place_info_floor_count);
        this.unitCount = (EditText) rootView.findViewById(R.id.place_info_unit_count);
        this.radio = (RadioGroup) rootView.findViewById(R.id.place_info_shomali_jonubi);
        this.radioVaziat = (RadioGroup) rootView.findViewById(R.id.place_info_vaziat_sakht);
        this.totalArea = (EditText) rootView.findViewById(R.id.place_info_total_area);
        this.baseArea = (EditText) rootView.findViewById(R.id.place_info_zirbana);
        this.familyNo = (EditText) rootView.findViewById(R.id.place_info_familNo);
        this.nextLeft = (EditText) rootView.findViewById(R.id.place_info_eshterak_chap);
        this.nextRight = (EditText) rootView.findViewById(R.id.place_info_eshterak_rast);

        this.radiomahdude = (RadioGroup) rootView.findViewById(R.id.place_info_vaziat_mahdude);
        this.dakhelmahdude = (RadioButton) rootView.findViewById(R.id.place_info_radio_dakhelmahdude);
        this.kharejmahdude = (RadioButton) rootView.findViewById(R.id.place_info_radio_kharejmahdude);
        this.citiesSpinner = (Spinner)rootView.findViewById(R.id.place_info_cities);
        this.villagesSpinner = (Spinner) rootView.findViewById(R.id.place_info_village_list);
        this.IndustrialCities = (Spinner) rootView.findViewById(R.id.place_info_sharak_sanati);
        this.shomali = (RadioButton) rootView.findViewById(R.id.place_info_shomali);
        this.jonubi = (RadioButton) rootView.findViewById(R.id.place_info_jonubi);
        this.faghedbana = (RadioButton) rootView.findViewById(R.id.place_info_radio_faghed_bana);
        this.darsakht = (RadioButton) rootView.findViewById(R.id.place_info_radio_darhalesakht);
        this.jadidsakht = (RadioButton) rootView.findViewById(R.id.place_info_radio_new);
        this.ghadimi = (RadioButton) rootView.findViewById(R.id.place_info_radio_old);


        this.isVillage = (CheckBox)rootView.findViewById(R.id.place_info_village_box);

        this.name = (TextView)rootView.findViewById(R.id.place_info_name);
        this.date = (TextView)rootView.findViewById(R.id.place_info_visit_date);
        this.longitude = (EditText)rootView.findViewById(R.id.place_info_long);
        this.latitude = (EditText)rootView.findViewById(R.id.place_info_lat);


        showMap = (Button) rootView.findViewById(R.id.place_info_show_map);
        this.showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MapActivity.class);
                intent.putExtra(MapActivity.EXTRA_COMMAND, requestNumber);
                startActivityForResult(intent,0);
            }
        });

        Bundle info = getArguments();
        this.requestNumber = info.getLong("requestnumber");
    }

    /**
     * cartable in db which is related to this request number received from DB
     * load receiving data and show to user
     */
    private void loadData() {
        List<LocationTbl> locationList = LocationTbl.find(LocationTbl.class, "Request_Code = ?", String.valueOf(this.requestNumber));
        List<CartableTbl> cartableTbls = CartableTbl.find(CartableTbl.class, "Request_Code = ?", String.valueOf(this.requestNumber));
        List<BaseMaterialTbl> shahrakList = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ?" , "5");
        List<BaseMaterialTbl> buildStatList = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ?" , "6");
        this.thisItem = (LocationTbl)locationList.get(0);
        this.thisPerson = cartableTbls.get(0);

        List<CityTbl> cityList = CityTbl.find(CityTbl.class, "Rgn_Code = ?", String.valueOf(thisItem.RgnCode));
        List<VillageTbl> villageList = VillageTbl.find(VillageTbl.class,"Rgn_Code = ?", String.valueOf(thisItem.RgnCode));
        this.name.setText(thisPerson.Name);
       // this.thisItem.
        this.floorCount.setText(thisItem.FloorCount);
        this.unitCount.setText(thisItem.UnitCount);
        this.totalArea.setText(String.valueOf(thisItem.TotalArea));
        this.baseArea.setText(String.valueOf(thisItem.BaseArea));
        this.longitude.setText(String.valueOf(thisItem.LastY));
        this.latitude.setText(String.valueOf(thisItem.LastX));
        this.familyNo.setText(String.valueOf(thisItem.FamilyCount));
        this.nextLeft.setText(String.valueOf(thisItem.NextLeftFileNo));
        this.nextRight.setText(String.valueOf(thisItem.NextRightFileNo));

        if(thisItem.IsVillage)
            this.isVillage.setChecked(true);
        else
            this.isVillage.setChecked(false);
        if(thisItem.BuildingStatus == 320)
            this.faghedbana.setChecked(true);
        if(thisItem.BuildingStatus == 321)
            this.darsakht.setChecked(true);
        if(thisItem.BuildingStatus == 322)
            this.jadidsakht.setChecked(true);
        if(thisItem.BuildingStatus == 323)
            this.ghadimi.setChecked(true);

        if(thisItem.AreaStatus == 230){
            dakhelmahdude.setChecked(true);
        }
        if(thisItem.AreaStatus == 231){
            kharejmahdude.setChecked(true);
        }

        ArrayList shahraks = new ArrayList();
        for(BaseMaterialTbl city: shahrakList){
            shahraks.add(city.Description);
        }

        ArrayAdapter Sharakadapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, shahraks);
        IndustrialCities.setAdapter(Sharakadapter);
        ArrayAdapter myAdap = (ArrayAdapter<CharSequence>) IndustrialCities.getAdapter();
        if(thisItem.Industrialcity!= 0) {
            int spinnerPosition = myAdap.getPosition(convertBaseToName(5, thisItem.Industrialcity));
            IndustrialCities.setSelection(spinnerPosition);
        }

        ArrayList cities = new ArrayList();
        for(CityTbl cityTbl: cityList){
            cities.add(cityTbl.CityName);
        }

        ArrayAdapter cityAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, cities);
        citiesSpinner.setAdapter(cityAdapter);
        ArrayAdapter myapp = (ArrayAdapter) citiesSpinner.getAdapter();
        if(thisItem.CityCode != 0){
            int position = myapp.getPosition(getCityNamebyCityCode(thisItem.CityCode));
            citiesSpinner.setSelection(position);

        }


        ArrayList villages = new ArrayList();
        for(VillageTbl villageTbl: villageList){
            villages.add(villageTbl.VillageName);
        }

        ArrayAdapter villageAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, villages);
        villagesSpinner.setAdapter(villageAdapter);
        ArrayAdapter ada = (ArrayAdapter) villagesSpinner.getAdapter();
        if(thisItem.VillageCode != 0){
            int position = ada.getPosition(getVillageNameByCode(thisItem.VillageCode));
            villagesSpinner.setSelection(position);

        }

if(thisItem.GeoDir != null) {
    if (thisItem.GeoDir.equals("240")) {
        shomali.setChecked(true);
    }

    if (thisItem.GeoDir.equals("241")) {
        jonubi.setChecked(true);
    }
}

       // if(buildStatList.get(0).Tablet_Code == 0)


  //      List<CitiesListCartableRequestDB> cities = CitiesListCartableRequestDB.find(CitiesListCartableRequestDB.class, "city_Code = ?", String.valueOf(thisItem.CityCode));
//        CitiesListCartableRequestDB city = cities.get(0);

  //     ArrayAdapter<CharSequence> cityAdapter = (ArrayAdapter<CharSequence>) citiesSpinner.getAdapter();
  //      int spinnerPosition = cityAdapter.getPosition(city.cityName);
   //     if (spinnerPosition<citiesSpinner.getAdapter().getCount())
   //         citiesSpinner.setSelection(spinnerPosition);

      //  spinnerPosition = Integer.valueOf( thisItem.placeArea )-1;
        java.text.DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date date = null;
        try {
            date = format.parse(thisPerson.RequestDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Solar solar = new Solar();
            solar.solar(date);

        visitDate.setText(convertorNumberToPersian.toPersianNumber( solar.year +"/"+ solar.month +"/"+ solar.date));









/*
        if(thisItem.VisitDate!=null){
            thisItem.VisitDate = convertorNumberToPersian.toPersianNumber(thisItem.VisitDate);
            this.date.setText(thisItem.VisitDate);
        } */
    }

    private void setListeners(final View root) {

        visitDate.addTextChangedListener(new myTextWatcher(root));
        unitCount.addTextChangedListener(new myTextWatcher(root));
        floorCount.addTextChangedListener(new myTextWatcher(root));
         baseArea.addTextChangedListener(new myTextWatcher(root));
        totalArea.addTextChangedListener(new myTextWatcher(root));
        nextLeft.addTextChangedListener(new myTextWatcher(root));
        nextRight.addTextChangedListener(new myTextWatcher(root));


        familyNo.addTextChangedListener(new myTextWatcher(root));


       longitude.addTextChangedListener(new myTextWatcher(root));
       latitude.addTextChangedListener(new myTextWatcher(root));


        //latitude.setOnEditorActionListener();

        IndustrialCities.setOnItemSelectedListener(this);
        villagesSpinner.setOnItemSelectedListener(this);
        citiesSpinner.setOnItemSelectedListener(this);

        shomali.setOnCheckedChangeListener(this);
        jonubi.setOnCheckedChangeListener(this);
        isVillage.setOnCheckedChangeListener(this);

        dakhelmahdude.setOnCheckedChangeListener(this);
        kharejmahdude.setOnCheckedChangeListener(this);

        faghedbana.setOnCheckedChangeListener(this);
        darsakht.setOnCheckedChangeListener(this);
        jadidsakht.setOnCheckedChangeListener(this);
        ghadimi.setOnCheckedChangeListener(this);
    }
    /**
     * when user inserts data in step 1 of wizard procedure and wants to go over step 2
     * this function is called to save data in database
     */
    private void saveItem() {
try {
    thisPerson.Status = 9;
    thisItem.VisitDate = this.visitDate.getText().toString();
    java.text.DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    thisItem.VisitDateEn = dateFormat.format(date).toString();
    thisItem.FloorCount = this.floorCount.getText().toString();
    thisItem.UnitCount = this.unitCount.getText().toString();
    if (!totalArea.getText().toString().isEmpty())
        thisItem.TotalArea = Integer.parseInt(this.totalArea.getText().toString());
    if (!baseArea.getText().toString().isEmpty())
        thisItem.BaseArea = Integer.parseInt(this.baseArea.getText().toString());
    if (!nextRight.getText().toString().isEmpty())
        thisItem.NextRightFileNo = Integer.parseInt(this.nextRight.getText().toString());
    if (!nextLeft.getText().toString().isEmpty())
        thisItem.NextLeftFileNo = Integer.parseInt(this.nextLeft.getText().toString());
    if (!familyNo.getText().toString().isEmpty())
        thisItem.FamilyCount = Integer.parseInt(this.familyNo.getText().toString());

    if (!longitude.getText().toString().isEmpty())
        thisItem.LastY = Integer.valueOf(this.longitude.getText().toString());
    if (!latitude.getText().toString().isEmpty())
        thisItem.LastX = Integer.valueOf(this.latitude.getText().toString());

    thisItem.VisitDate = this.date.getText().toString();


} catch (NumberFormatException e){
    e.printStackTrace();
}




   /*     if(this.dakhelmahdude.getId() == id ) {
            thisItem.placeArea = String.valueOf("داخل محدوده");
            Log.e("id of radio","dakhel");
        }
       if(this.kharejmahdude.getId()==id){
            thisItem.placeArea = String.valueOf("خارج محدوده");
            Log.e("id of radio","kharej");
        }*/
        thisPerson.save();
        thisItem.save();
    }

    /**
     * define the spinner for geographic area
     */
  /*  private void spinnerHandling() {
        List<CityTbl> cities = CityTbl.find(CityTbl.class, "RequestCode  = ?", String.valueOf(this.requestNumber));
        cityName = new ArrayList<String>();
        for(CityTbl city:cities)
            cityName.add(city.CityName);

        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, cityName);
        citiesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        citiesSpinner.setAdapter(citiesAdapter);

       // ArrayAdapter<CharSequence> geoAdapter = ArrayAdapter.createFromResource(getActivity(),
              //  R.array.new_branch_geo_array, R.layout.spinner_item);
       // geoAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
       // geographic.setAdapter(geoAdapter);
    }
*/

    public String convertBaseToName(int tabletcode, int value ){

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and TabletCode = ?", String.valueOf(tabletcode), String.valueOf(value));


        BaseMaterialTbl materialbase =  base.get(0);

        return materialbase.Description;

    }
    public int convertBaseToCode(int tabletcode , String name){

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and Description = ?", String.valueOf(tabletcode) , name);

        BaseMaterialTbl materialbase = base.get(0);
        return  materialbase.Tablet_Code;
    }

    public List<?> convertCodeTolist(int tabletcode){
        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? ", String.valueOf(tabletcode));
        List<String> baselist = new ArrayList<>();
        for(BaseMaterialTbl material:base){
            baselist.add(material.Description);
        }
        return baselist;
    }
    public String getCityNamebyCityCode(int code){

        List<CityTbl> cityList = CityTbl.find(CityTbl.class, "City_Code = ?", String.valueOf(code) );
        CityTbl city = cityList.get(0);

        return city.CityName;

    }
    public int getCityCodebycityName(String name){

        List<CityTbl> cityList = CityTbl.find(CityTbl.class, "City_Name = ?", name );
        CityTbl city = cityList.get(0);

        return city.CityCode;

    }

    public int getVillageCodebyName(String name){

        List<VillageTbl> vilagelist = VillageTbl.find(VillageTbl.class, "village_Name = ?", name);
        VillageTbl village = vilagelist.get(0);

        return village.VillageCode;

    }
    public String getVillageNameByCode(int code){
        List<VillageTbl> vilagelist = VillageTbl.find(VillageTbl.class, "village_Code = ?", String.valueOf(code));
        VillageTbl vilagename = vilagelist.get(0);

        return vilagename.VillageName;


    }
    /**
     *
     * @param visitDate
     * @param inflater
     * @param container
     * user get persian date view and findout day, month, year, and finally the date is picked by user show to him
     * date picker dialog show as a alert dialog
     * icon, view, title is settled, then date shows in persianDate textview
     */
    @TargetApi(17)
    private void visitDateHandling(final TextView visitDate, final LayoutInflater inflater, final ViewGroup container) {
        View persiandate = inflater.inflate(R.layout.show_persian_date_dialog_fragment, container, false);
        PersianDatePicker persianDatePicker = (PersianDatePicker) persiandate.findViewById(R.id.persianDate);
        PersianCalendar persianCalendar = persianDatePicker.getDisplayPersianDate();
        int day = persianCalendar.getPersianDay();
        int month = persianCalendar.getPersianMonth();
        int year = persianCalendar.getPersianYear();
        if(visitDate.getText().toString().isEmpty()) {
            visitDate.setText(convertorNumberToPersian.toPersianNumber(String.valueOf(year)) + "/" + convertorNumberToPersian.toPersianNumber(String.valueOf(month))
                    + "/" + convertorNumberToPersian.toPersianNumber(String.valueOf(day)));
        }

        visitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View persiandate = inflater.inflate(R.layout.show_persian_date_dialog_fragment, container, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setView(persiandate);
                builder.setTitle(getResources().getText(R.string.datePicker));
                builder.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        PersianDatePicker persianDatePicker = (PersianDatePicker) persiandate.findViewById(R.id.persianDate);
                        PersianCalendar persianCalendar = persianDatePicker.getDisplayPersianDate();
                        int day = persianCalendar.getPersianDay();
                        int month = persianCalendar.getPersianMonth();
                        int year = persianCalendar.getPersianYear();
                        visitDate.setText( convertorNumberToPersian.toPersianNumber(String.valueOf(year)) + "/" + convertorNumberToPersian.toPersianNumber(String.valueOf(month))
                                + "/" +  convertorNumberToPersian.toPersianNumber(String.valueOf(day)) );
                    }
                });
                builder.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //DO TASK
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                //Set Title Gravity
                final int alertTitle = getActivity().getResources().getIdentifier("alertTitle", "id", "android");
                TextView messageText = (TextView) dialog.findViewById(alertTitle);
                messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.place_info_sharak_sanati :

                String shahrak = parent.getItemAtPosition(position).toString();
                thisItem.Industrialcity = convertBaseToCode(5, shahrak);
                break;


            case R.id.place_info_cities:

                String city = parent.getItemAtPosition(position).toString();
                thisItem.CityCode = getCityCodebycityName(city);
                break;

            case R.id.place_info_village_list:

                String village = parent.getItemAtPosition(position).toString();
                thisItem.VillageCode = getVillageCodebyName(village);
                break;

        }

        thisItem.save();
        thisPerson.Status = 9;
        thisPerson.save();




    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch ( buttonView.getId()){

            case R.id.place_info_village_box:

                if(isChecked){
                    thisItem.IsVillage = true;
                }
                else{
                    thisItem.IsVillage = false;
                }
                break;

            case  R.id.place_info_shomali:
                if(isChecked)
                    thisItem.GeoDir = "240";
                break;

            case R.id.place_info_jonubi:
                if(isChecked)
                    thisItem.GeoDir = "241";

                break;

            case R.id.place_info_radio_kharejmahdude:
                if(isChecked)
                    thisItem.AreaStatus = 231;

                break;

            case R.id.place_info_radio_dakhelmahdude:
                if(isChecked)
                    thisItem.AreaStatus = 230;
                break;

            case R.id.place_info_radio_faghed_bana:
                if(isChecked)
                    thisItem.BuildingStatus = 320;
                break;

            case R.id.place_info_radio_darhalesakht:
                if(isChecked)
                    thisItem.BuildingStatus = 321;
                break;
            case R.id.place_info_radio_new:
                if(isChecked)
                    thisItem.BuildingStatus = 322;
                break;
            case R.id.place_info_radio_old:
                if(isChecked)
                    thisItem.BuildingStatus = 323;
                break;
        }

        thisItem.save();
        thisPerson.Status = 9;
        thisPerson.save();

    }


    public class myTextWatcher implements TextWatcher{
        View root;
        public myTextWatcher(View root) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

           saveItem();
        }
    }
}
