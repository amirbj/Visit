package com.andc.slidingmenu.Fragments.Cartable.Others;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import com.andc.slidingmenu.Main.CartableItemActivity;
import com.andc.slidingmenu.R;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.CartableTbl;
import slidingmenu.andc.com.dataaccess.SupplyPowerTbl;


/**
 * Created by win on 4/29/2015.
 */
public class CartableElectricSupply extends Fragment implements CartableItemActivity.Callbacks, AdapterView.OnItemSelectedListener {

    public Spinner dueTo;
    public String cause;

    public RadioGroup radioGroup;
    public RadioButton availNetwork;
    public RadioButton createNetwork;
    public RadioButton notPossible;

    public LinearLayout dueToText;
    public LinearLayout dueToSpinner;

    public long  requestNumber;
    public SupplyPowerTbl thisItem;

    @Override
    public boolean isValid(){
        if(radioGroup.getCheckedRadioButtonId()!=-1){
            return true;
        }
        return false;
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of electric supply
     * @return
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_visit_place_electric_supply, container, false);

        definition(rootView);

        changeHandling();
        setAdapterAdapter();
        return rootView;
    }

    /**
     * @param rootView
     * find any views on cartable view, get bundles from previous fragment and just define variables
     * cartable in db which is related to this request number received from DB
     */
    private void definition(ViewGroup rootView) {
        //Spinner
        dueToText = (LinearLayout) rootView.findViewById(R.id.due_to_text);
        dueToSpinner = (LinearLayout) rootView.findViewById(R.id.due_to_spinner);
        dueTo = (Spinner) rootView.findViewById(R.id.electric_supply_not_possible_due_to);


        //Radio Group
        radioGroup = (RadioGroup) rootView.findViewById(R.id.electric_supply);
        availNetwork = (RadioButton) rootView.findViewById(R.id.electric_supply_avail_network);
        createNetwork = (RadioButton) rootView.findViewById(R.id.electric_supply_create_network);
        notPossible = (RadioButton) rootView.findViewById(R.id.electric_supply_not_possible);

        Bundle info = getArguments();
        this.requestNumber = info.getLong("requestnumber");

        List<SupplyPowerTbl> cartable = SupplyPowerTbl.find(SupplyPowerTbl.class, "Request_Code = ?", String.valueOf(this.requestNumber));
        if(!cartable.isEmpty())
        this.thisItem = cartable.get(0);
        dueTo.setOnItemSelectedListener(this);
    }

    /**
     * radio button for handling electric supplement is created
     * when user wants to register an chosen approach, ID of chosen approach compare with thisItem.method
     * values for thisItem.method is 1,2,3
     * 1: not possible to create a network
     * 2: network is available
     * 3: needs to create a new network
     */
    private void changeHandling() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                //Control Visibility of Spinner
                if(checkedId==notPossible.getId() ){

                    dueToSpinner.setVisibility(View.VISIBLE);
                    dueToText.setVisibility(View.VISIBLE);
                } else {
                    dueToSpinner.setVisibility(View.INVISIBLE);
                    dueToText.setVisibility(View.INVISIBLE);
                }
                //Bold the Selected Button's Text
                //availNetwork.setTypeface(availNetwork.isChecked() ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                //createNetwork.setTypeface(createNetwork.isChecked() ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
                //notPossible.setTypeface(notPossible.isChecked() ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);




                //Save Items
           if (checkedId == notPossible.getId()) {
                  thisItem.Supplymethod =1;
                  thisItem.ReasonnotSupply = convertBaseToCode(24 , dueTo.getSelectedItem().toString());
             } else if (checkedId == availNetwork.getId()) {
                 thisItem.Supplymethod= 2;
                 thisItem.ReasonnotSupply = convertBaseToCode(24, dueTo.getSelectedItem().toString());
               } else if (checkedId == createNetwork.getId()) {
                  thisItem.Supplymethod = 3;
                  thisItem.ReasonnotSupply = convertBaseToCode(24, dueTo.getSelectedItem().toString());
                }
               thisItem.save();
           }
        });
    }

   public void saveItem(){
        if( radioGroup.getCheckedRadioButtonId()==notPossible.getId() ){
            thisItem.Supplymethod = 1;

            thisItem.ReasonnotSupply = convertBaseToCode(24, dueTo.getSelectedItem().toString());
        }
        else if(radioGroup.getCheckedRadioButtonId()==availNetwork.getId()) {
            thisItem.Supplymethod  = 2;
            thisItem.ReasonnotSupply = convertBaseToCode(24, dueTo.getSelectedItem().toString());
        }
        else if(radioGroup.getCheckedRadioButtonId()==createNetwork.getId()) {
            thisItem.Supplymethod = 3;
            thisItem.ReasonnotSupply = convertBaseToCode(24, dueTo.getSelectedItem().toString());
        }
        thisItem.save();
    }

    /**
     * set adapter and values for cause spinner
     * check which one should be checked
     */
    private void setAdapterAdapter() {
        List<BaseMaterialTbl> baselist= BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "24");
        List<String> items = new ArrayList<>();

        for(BaseMaterialTbl base: baselist ){
            items.add(base.Description);
        }

        ArrayAdapter dueAdapter =new  ArrayAdapter(getActivity(),
                 R.layout.spinner_item, items);
        dueAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dueTo.setAdapter(dueAdapter);
        if(thisItem.ReasonnotSupply == 530){
            dueTo.setSelection(getAdapterPosion(dueTo, 24, 530));
        }
              if(thisItem.ReasonnotSupply == 531){
            dueTo.setSelection(getAdapterPosion(dueTo, 24, 531));
        }
        if(thisItem.ReasonnotSupply == 532){
            dueTo.setSelection(getAdapterPosion(dueTo, 24, 532));
        }

        if(thisItem.Supplymethod!= 0) {
            if (thisItem.Supplymethod == 2)
                availNetwork.setChecked(true);
            else if (thisItem.Supplymethod == 3)
                createNetwork.setChecked(true);
            else if (thisItem.Supplymethod == 1) {             // if number 3 is selected cause spinner shows
                dueToSpinner.setVisibility(View.VISIBLE);
                dueToText.setVisibility(View.VISIBLE);
                notPossible.setChecked(true);
                if (thisItem.ReasonnotSupply != 0) {
                    int spinnerPosition = dueAdapter.getPosition(thisItem.ReasonnotSupply);
                    dueTo.setSelection(spinnerPosition);
                }
            }
        }
    }

    public int convertBaseToCode(int tabletcode, String name) {

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and Description = ?", String.valueOf(tabletcode), name);

        BaseMaterialTbl materialbase = base.get(0);
        return materialbase.Tablet_Code;
    }

    public String convertBaseToName(int tabletcode, int value ){

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and TabletCode = ?", String.valueOf(tabletcode), String.valueOf(value));


        BaseMaterialTbl materialbase =  base.get(0);

        return materialbase.Description;

    }
    public int getAdapterPosion(Spinner s, int code, int subcode){
        ArrayAdapter myapp = (ArrayAdapter) s.getAdapter();
        int position =0;
        if(subcode != 0) {
            position = myapp.getPosition(convertBaseToName(code, subcode));
        }
        return position;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        saveItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}


