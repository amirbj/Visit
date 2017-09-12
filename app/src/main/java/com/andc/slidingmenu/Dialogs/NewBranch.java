package com.andc.slidingmenu.Dialogs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andc.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;

/**
 * Created by win on 4/18/2015.
 */
public class NewBranch extends DialogFragment {


    public int newBranchNumber = 2;
    public int editBranchNumber = 3;
    public int deleteBranch = 4;
    public int cancelBranch = 5;

    public LinearLayout amperLayout;
    public LinearLayout powerLayout;

    public Spinner type;
    public Spinner phase;
    public Spinner amper;
    public EditText power;
    public Spinner tariff;
    public Spinner voltage;
    public Spinner TrfType;
    public EditText count;
    public String position;

    public String typeText;
    public String branchCode;
    public String phaseText;
    public String amperText;
    public String powerText;
    public String tariffText;
    public String voltageText;
    public String TrfTypeText;
    public String countText;

    public Button register;
    public Button delete;
    public Button cancel;
    public CheckBox newBranchSeparation;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of new branch
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_new_branch, container);
        Bundle bundle = getArguments();
        getDialog().setTitle(getResources().getString(R.string.new_branch));

        definition(rootView);

        setAdapter();
        int req = this.getTargetRequestCode();
        if(req==newBranchNumber)
            delete.setVisibility(View.GONE);
        if(req==editBranchNumber)
            initialize(bundle);
        delete(this);
        register(this);
        cancel(this);
        changeHandler();

        return rootView;
    }

    /**
     *
     * @param rootView
     * find any views on cartable view and just define variables
     */
    private void definition(View rootView) {
        amperLayout = (LinearLayout) rootView.findViewById(R.id.amper_line);
        powerLayout = (LinearLayout) rootView.findViewById(R.id.power_line);
        type = (Spinner) rootView.findViewById(R.id.create_new_branch_type);
        phase = (Spinner) rootView.findViewById(R.id.create_new_branch_phase);
        amper = (Spinner) rootView.findViewById(R.id.create_new_branch_amper);
        power = (EditText) rootView.findViewById(R.id.create_new_branch_power);
        tariff = (Spinner) rootView.findViewById(R.id.create_new_branch_tariff);
        voltage = (Spinner) rootView.findViewById(R.id.create_new_branch_voltage);
        TrfType = (Spinner) rootView.findViewById(R.id.create_new_branch_trf_type);
        count = (EditText) rootView.findViewById(R.id.create_new_branch_count);
        type.requestFocus();
        register = (Button) rootView.findViewById(R.id.create_all_new_branch);
        delete = (Button) rootView.findViewById(R.id.delete_all_new_branch);
        cancel = (Button) rootView.findViewById(R.id.cancel_all_new_branch);
        newBranchSeparation = (CheckBox) rootView.findViewById(R.id.new_branch_separation);
    }

    /**
     *
     * @param newBranch
     * when user wants to delete item, confirm dialog appears
     * position of item send to caller fragment to remove from the main list
     */
    @TargetApi(17)
    private void delete(final NewBranch newBranch) {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(newBranch.getActivity())
                        .setTitle(getResources().getString(R.string.dialog_delete_title))
                        .setMessage(getResources().getString(R.string.dialog_delete_body))
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent result = new Intent();
                                result.putExtra("position", position);
                                newBranch.getTargetFragment().onActivityResult(deleteBranch, deleteBranch, result);
                                newBranch.dismiss();
                            }
                        }).create();

                dialog.show();

                //Set Title Gravity
                final int alertTitle = getActivity().getResources().getIdentifier( "alertTitle", "id", "android" );
                TextView messageText = (TextView)dialog.findViewById(alertTitle);
                //messageText.setGravity(Gravity.Right);
                messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        });
    }


    /**
     *
     * @param bundle
     * get initial data from bundle of caller fragment and insert them to their related fields
     */
    private void initialize(Bundle bundle) {
        if(bundle.getString("isFromServer")!=null && bundle.getString("isFromServer").equalsIgnoreCase("true")) {
            cancel.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
        }

        ArrayAdapter<CharSequence> myAdap = (ArrayAdapter<CharSequence>) type.getAdapter();
        int spinnerPosition = myAdap.getPosition(bundle.getString("type"));
        type.setSelection(spinnerPosition);

        myAdap = (ArrayAdapter<CharSequence>) phase.getAdapter();
        spinnerPosition = myAdap.getPosition(bundle.getString("phase"));
        phase.setSelection(spinnerPosition);

        myAdap = (ArrayAdapter<CharSequence>) amper.getAdapter();
        spinnerPosition = myAdap.getPosition(bundle.getString("amper"));
        amper.setSelection(spinnerPosition);

        power.setText(bundle.getString("power"));

        myAdap = (ArrayAdapter<CharSequence>) tariff.getAdapter();
        spinnerPosition = myAdap.getPosition(bundle.getString("tariff"));
        tariff.setSelection(spinnerPosition);

        myAdap = (ArrayAdapter<CharSequence>) voltage.getAdapter();
        spinnerPosition = myAdap.getPosition(bundle.getString("voltage"));
        voltage.setSelection(spinnerPosition);

        spinnerPosition = Integer.valueOf(bundle.getString("TrfType"));
        TrfType.setSelection(spinnerPosition);

        count.setText(bundle.getString("count"));
        position = bundle.getString("position");

        branchCode = bundle.getString("branchCode");

        if(bundle.getString("separation")!=null) {
            if (bundle.getString("separation").equalsIgnoreCase("true"))
                newBranchSeparation.setChecked(true);
            else
                newBranchSeparation.setChecked(false);
        }
    }

    private void changeHandler() {
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(type.getSelectedItem().toString().equalsIgnoreCase("عادی")){
                    amperLayout.setVisibility(View.VISIBLE);
                    powerLayout.setVisibility(View.GONE);

                    ArrayAdapter myAdap = (ArrayAdapter<CharSequence>) phase.getAdapter();
                    int spinnerPosition = myAdap.getPosition("1");
                    phase.setSelection(spinnerPosition);

                    myAdap = (ArrayAdapter<CharSequence>) amper.getAdapter();
                    spinnerPosition = myAdap.getPosition("32");
                    amper.setSelection(spinnerPosition);

                    myAdap = (ArrayAdapter<CharSequence>) tariff.getAdapter();
                    spinnerPosition = myAdap.getPosition("خانگی");
                    tariff.setSelection(spinnerPosition);

                    myAdap = (ArrayAdapter<CharSequence>) voltage.getAdapter();
                    spinnerPosition = myAdap.getPosition("ثانویه");
                    voltage.setSelection(spinnerPosition);
                }
                else if(type.getSelectedItem().toString().equalsIgnoreCase("دیماندی")){
                    powerLayout.setVisibility(View.VISIBLE);
                    amperLayout.setVisibility(View.GONE);

                    ArrayAdapter myAdap = (ArrayAdapter<CharSequence>) phase.getAdapter();
                    int spinnerPosition = myAdap.getPosition("3");
                    phase.setSelection(spinnerPosition);

                    power.setTextSize(getResources().getInteger(R.integer.demandedFragmentEditTextSize));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     *
     * @param newBranch
     * read data from list and dialog box and save them in a bundle and return to caller fragment
     */
    private void register(final NewBranch newBranch) {
        final int req = newBranch.getTargetRequestCode();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean done = true;
                typeText = type.getSelectedItem().toString();
                phaseText = phase.getSelectedItem().toString();
                if(amperLayout.getVisibility()==View.VISIBLE)
                    amperText = amper.getSelectedItem().toString();
                if(powerLayout.getVisibility()==View.VISIBLE)
                    powerText = power.getText().toString();
                tariffText = tariff.getSelectedItem().toString();
                voltageText = voltage.getSelectedItem().toString();
                TrfTypeText = TrfType.getSelectedItem().toString();
                if(TrfTypeText.equalsIgnoreCase("ازاد"))
                    TrfTypeText = "1";
                else
                    TrfTypeText = "0";

                countText = count.getText().toString();

                if(powerLayout.getVisibility()==View.GONE)
                    powerText = String.valueOf(Math.round((Double.valueOf(phaseText) * Double.valueOf(amperText)) / 5.0));
                else if(amperLayout.getVisibility()==View.GONE && !powerText.equals(null) && !powerText.equalsIgnoreCase(""))
                    amperText = String.valueOf(Math.round((5.0 * Double.valueOf(powerText)) / Double.valueOf(phaseText)));

                if(powerLayout.getVisibility()==View.VISIBLE) {
                    if (powerText.isEmpty() || countText.isEmpty()) {
                        Toast toast = Toast.makeText(newBranch.getTargetFragment().getActivity().getBaseContext()
                                , getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(30);
                        toast.show();
                        done = false;
                    }
                }

                if(amperLayout.getVisibility()==View.VISIBLE) {
                    if (amperText.isEmpty() || countText.isEmpty()) {
                        Toast toast = Toast.makeText(newBranch.getTargetFragment().getActivity().getBaseContext()
                                , getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(30);
                        toast.show();
                        done = false;
                    }
                }

                if(done==true) {
                    Intent result = new Intent();
                    result.putExtra("type", typeText);
                    result.putExtra("phase", phaseText);
                    result.putExtra("amper", amperText);
                    result.putExtra("power", powerText);
                    result.putExtra("tariff", tariffText);
                    result.putExtra("voltage", voltageText);
                    result.putExtra("TrfType", TrfTypeText);
                    result.putExtra("count", countText);

                    if(req==newBranchNumber)
                        result.putExtra("branchCode", "-1");
                    else
                        result.putExtra("branchCode", branchCode);

                    if(newBranchSeparation.isChecked())
                        result.putExtra("separation", "true");
                    else
                        result.putExtra("separation", "false");

                    result.putExtra("position", position);
                    newBranch.getTargetFragment().onActivityResult(req, req, result);

                    newBranch.dismiss();
                }
            }
        });
    }

    @TargetApi(17)
    private void cancel(final NewBranch newBranch) {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(newBranch.getActivity())
                        .setTitle(getResources().getString(R.string.dialog_cancel_title))
                        .setMessage(getResources().getString(R.string.dialog_cancel_body))
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent result = new Intent();
                                result.putExtra("position", position);
                                newBranch.getTargetFragment().onActivityResult(cancelBranch, cancelBranch, result);
                                newBranch.dismiss();
                            }
                        }).create();

                dialog.show();

                //Set Title Gravity
                final int alertTitle = getActivity().getResources().getIdentifier( "alertTitle", "id", "android" );
                TextView messageText = (TextView)dialog.findViewById(alertTitle);
                //messageText.setGravity(Gravity.Right);
                messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        });
    }

    /**
     * set adapter and values list for spinners
     */
    private void setAdapter() {
        ArrayAdapter phaseAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, convertCodeTolist(9));
        phaseAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        phase.setAdapter(phaseAdapter);

        ArrayAdapter amperAdapter = new ArrayAdapter(getActivity(),
                R.layout.spinner_dropdown_item, convertCodeTolist(10));
        amperAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        amper.setAdapter(amperAdapter);

        ArrayAdapter tariffAdapter = new ArrayAdapter(getActivity(),
                R.layout.spinner_dropdown_item, convertCodeTolist(11));
        tariffAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        tariff.setAdapter(tariffAdapter);

        ArrayAdapter voltageAdapter = new ArrayAdapter(getActivity(),
                R.layout.spinner_dropdown_item, convertCodeTolist(13));
        voltageAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        voltage.setAdapter(voltageAdapter);


        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.new_branch_type_array, R.layout.spinner_item);
        typeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        type.setAdapter(typeAdapter);


        ArrayAdapter<CharSequence> trfTypeAdapter = new ArrayAdapter(getActivity(),
                R.layout.spinner_dropdown_item , convertCodeTolist(12));
        trfTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        TrfType.setAdapter(trfTypeAdapter);

        count.setGravity(Gravity.CENTER);
    }

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
}