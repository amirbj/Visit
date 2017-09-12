package com.andc.slidingmenu.Dialogs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andc.slidingmenu.ExceptionViewer.ShowExceptions;
import com.andc.slidingmenu.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.ChangeBranchTbl;

/**
 * Created by win on 4/18/2015.
 */
public class ChangeBranch extends DialogFragment {

    public int cancelBranch = 7;
    public int deleteBranch = 6;
    public int newBranch = 4;
    public int editBranch = 5;

    public LinearLayout amperLayout;
    public LinearLayout powerLayout;

    // view
    public Spinner type;
    public EditText fabrikNumber;
    public EditText docNumber;
    public Spinner phase;
    public Spinner amper;
    public EditText power;
    public Spinner tariff;
    public Spinner voltage;
    public String position;
    public TextView branchCodeTextView;
   // public TextView addressTextView;
    public TextView nameTextView;
    public TextView oldPowerTextView;
    public TextView oldPhaseTextView;
    public TextView oldAmperTextView, oldtarrifTextView, oldvoltTextview, oldbranchTypeTextView;

    // old
    public String statusText;
    public String branchCodeText;
    public String fabrikNumberText;
    public String docNumberText;
    public String typeText;
    public String phaseText;
    public String amperText;
    public String powerText;
    public String tariffText;
    public String voltageText;
    public String nameText;
    public String trfType;
    public String addressText;

    // web service
    public ShowExceptions e = new ShowExceptions();
    public String newFabrikNumber;
    public String userName;
    public String userID = "325";
    public String token;

    public String branchSLRValue;
    public String branchCodeValue;
    public String powerValue;
    public String phaseValue;
    public String amperValue;
    public String typeValue;
    public String tariffValue;
    public String voltageValue;
    public String nameValue;
    public String addressValue;
    public String actionType;
    public String requestActionType;

    // new
    public String newStatusText;
    public String newDocNumber;
    public String newTypeText;
    public String newPhaseText;
    public String newAmperText;
    public String newPowerText;
    public String newTariffText;
    public String newVoltageText;

    // button , checkbox
    public Button register;
    public Button delete;
    public Button receive;
    public Button cancel;
    private CheckBox changeBranchMerge;
    private CheckBox changeBranchSeparation;
    private CheckBox moveNeeded, changeNeeded;
    private String moveneededText, changeneededText;
//    private CheckBox changeBranchIncrease;
//    private CheckBox changeBranchDecrease;

    private static final String NAMESPACE = "http://tempuri.org/";
    private static String URL;
    private static final String SOAP_ACTION = "http://tempuri.org/GetNewBranchRequestByFabrikNumber";
    private static final String METHOD_NAME = "GetNewBranchRequestByFabrikNumber";
    View rootView;
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog!= null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of change branch
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        rootView = inflater.inflate(R.layout.create_change_branch, container);

        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault);
        getDialog().setTitle(getResources().getString(R.string.change_branch));

        definition(rootView);
        setAdapter();
        int req = this.getTargetRequestCode();
        if(req==newBranch)
            delete.setVisibility(View.GONE);
        if(req==editBranch)
            initialize(bundle);

        delete(this);
        cancel(this);
        register(this);
      //  changeHandler();
        getDataFromFubrikNumber();

        return rootView;
    }

    /**
     *
     * @param rootView
     * find any views on cartable view and just define variables
     */
    private void definition(View rootView) {
        branchCodeTextView = (TextView) rootView.findViewById(R.id.create_change_branch_branch_code);
      //  addressTextView = (TextView) rootView.findViewById(R.id.create_change_branch_address);
        nameTextView = (TextView) rootView.findViewById(R.id.create_change_branch_name);
        oldPowerTextView = (TextView) rootView.findViewById(R.id.create_change_branch_old_power);
        oldPhaseTextView = (TextView) rootView.findViewById(R.id.create_change_branch_old_phase);
        oldAmperTextView = (TextView) rootView.findViewById(R.id.create_change_branch_old_amper);
        oldtarrifTextView = (TextView) rootView.findViewById(R.id.create_change_branch_name);
        oldbranchTypeTextView = (TextView) rootView.findViewById(R.id.create_change_branch_type_);
        oldvoltTextview = (TextView) rootView.findViewById(R.id.create_change_branch_type_voltage);
        amperLayout = (LinearLayout) rootView.findViewById(R.id.amper_line);
        powerLayout = (LinearLayout) rootView.findViewById(R.id.power_line);
        fabrikNumber = (EditText) rootView.findViewById(R.id.create_change_branch_fabrik_number);
        docNumber = (EditText) rootView.findViewById(R.id.create_change_branch_document_number);
        type = (Spinner) rootView.findViewById(R.id.create_change_branch_type);
        phase = (Spinner) rootView.findViewById(R.id.create_change_branch_phase);
        amper = (Spinner) rootView.findViewById(R.id.create_change_branch_amp);
        power = (EditText) rootView.findViewById(R.id.create_change_branch_power);
        tariff = (Spinner) rootView.findViewById(R.id.create_change_branch_tariff);
        voltage = (Spinner) rootView.findViewById(R.id.create_change_branch_voltage);
        changeBranchMerge = (CheckBox) rootView.findViewById(R.id.change_branch_merge);
        changeBranchSeparation = (CheckBox) rootView.findViewById(R.id.change_branch_separation);
        changeNeeded = (CheckBox) rootView.findViewById(R.id.changeneeded);
        moveNeeded = (CheckBox) rootView.findViewById(R.id.moveneeded);
        type.requestFocus();
        register = (Button) rootView.findViewById(R.id.create_all_change_branch);
        delete = (Button) rootView.findViewById(R.id.delete_all_change_branch);
        receive = (Button) rootView.findViewById(R.id.receive_all_change_branch);
        cancel = (Button) rootView.findViewById(R.id.cancel_all_change_branch);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( getActivity() );
        this.userName = preferences.getString("username","");
        this.token = preferences.getString("token","");
    }

    /**
     * set adapter and values list for spinners
     */
    private void setAdapter() {
        List<?> list = convertCodeTolist(14);
        ArrayAdapter typeAdapter = new  ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, list);
        type.setAdapter(typeAdapter);



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

        power.setGravity(Gravity.CENTER);
        /*docNumber.setGravity(Gravity.CENTER);
        fabrikNumber.setGravity(Gravity.CENTER);
        addressTextView.setGravity(Gravity.CENTER);
        nameTextView.setGravity(Gravity.CENTER);
        oldPowerTextView.setGravity(Gravity.CENTER);
        oldAmperTextView.setGravity(Gravity.CENTER);
        oldPhaseTextView.setGravity(Gravity.CENTER);*/
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
        else{
            receive.setVisibility(View.GONE);
        }

        fabrikNumber.setText(bundle.getString("fabrik"));
        docNumber.setText(bundle.getString("newdocument"));

        ArrayAdapter myAdap = (ArrayAdapter<CharSequence>) type.getAdapter();
        int spinnerPosition = myAdap.getPosition(bundle.getInt("newtype"));
        type.setSelection(spinnerPosition);

        myAdap = (ArrayAdapter) phase.getAdapter();
        spinnerPosition = myAdap.getPosition(convertBaseToName(9,bundle.getInt("newphase")));
        phase.setSelection(spinnerPosition);

        myAdap = (ArrayAdapter<CharSequence>) amper.getAdapter();
        spinnerPosition = myAdap.getPosition(convertBaseToName(10,bundle.getInt("newamper")));
        amper.setSelection(spinnerPosition);

        power.setText(String.valueOf(bundle.getInt("newpower")));

        myAdap = (ArrayAdapter<CharSequence>) tariff.getAdapter();
        spinnerPosition = myAdap.getPosition(convertBaseToName(11 ,bundle.getInt("newtariff")));
        tariff.setSelection(spinnerPosition);

        myAdap = (ArrayAdapter<CharSequence>) voltage.getAdapter();
        spinnerPosition = myAdap.getPosition(convertBaseToName(13,bundle.getInt("newvoltage")));
        voltage.setSelection(spinnerPosition);

        position = bundle.getString("position");

        docNumberText = bundle.getString("document");

        typeText =  bundle.getString("type");
        statusText = bundle.getString("status");
        phaseText = convertBaseToName(9 ,bundle.getInt("phase"));
        amperText = convertBaseToName(10,bundle.getInt("amper"));
        powerText = bundle.getString("power");
        voltageText = convertBaseToName(13,bundle.getInt("voltage"));
        tariffText = convertBaseToName(11,bundle.getInt("tariff"));
        trfType = convertBaseToName(12, bundle.getInt("trftype"));

        branchCodeText = bundle.getString("branchCode");
        addressText = bundle.getString("Address");
        nameText = bundle.getString("FulName");


        newStatusText = bundle.getString("newstatus");


            if (bundle.getInt("requestaction") == 13 )
                changeBranchSeparation.setChecked(true);
            else
                changeBranchSeparation.setChecked(false);


            if (bundle.getInt("requestaction") == 14 )
                changeBranchMerge.setChecked(true);
            else
                changeBranchMerge.setChecked(false);

        if(bundle.getString("changeneeded").equals("true")) {
            changeNeeded.setChecked(true);
        }else {
            changeNeeded.setChecked(false);
        }

        if(bundle.getString("moveneeded").equals("true")){
            moveNeeded.setChecked(true);
        }
        else{
            moveNeeded.setChecked(false);
        }


        // old value
        branchCodeTextView.setText(branchCodeText);
      //  addressTextView.setText(addressText);
        nameTextView.setText(nameText);
        oldPowerTextView.setText(powerText);
        oldPhaseTextView.setText(phaseText);
        oldAmperTextView.setText(amperText);
        oldbranchTypeTextView.setText(trfType);
        oldvoltTextview.setText(voltageText);
        oldtarrifTextView.setText(tariffText);
    }

    /**
     *
     * @param changeBranch
     * when user wants to delete item, confirm dialog appears
     * position of item send to caller fragment to remove from the main list
     */
    @TargetApi(17)
    private void delete(final ChangeBranch changeBranch) {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(changeBranch.getActivity())
                        .setTitle(getResources().getString(R.string.dialog_delete_title))
                        .setMessage(getResources().getString(R.string.dialog_delete_body))
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent result = new Intent();
                                result.putExtra("position", position);
                                changeBranch.getTargetFragment().onActivityResult(deleteBranch, deleteBranch, result);
                                changeBranch.dismiss();
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

    @TargetApi(17)
    private void cancel(final ChangeBranch changeBranch) {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(changeBranch.getActivity())
                        .setTitle(getResources().getString(R.string.dialog_cancel_title))
                        .setMessage(getResources().getString(R.string.dialog_cancel_body))
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent result = new Intent();
                                result.putExtra("position", position);
                                changeBranch.getTargetFragment().onActivityResult(cancelBranch, cancelBranch, result);
                                changeBranch.dismiss();
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
     * @param changeBranch
     * read data from list and dialog box and save them in a bundle and return to caller fragment
     */
    private void register(final ChangeBranch changeBranch) {
        final int req = changeBranch.getTargetRequestCode();
      // register.setEnabled(false);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean done = true;

                newTypeText = String.valueOf(convertBaseToCode(14,type.getSelectedItem().toString()));
                fabrikNumberText = fabrikNumber.getText().toString();
         //       addressText = addressTextView.getText().toString();
                nameText = nameTextView.getText().toString();
                newDocNumber = docNumber.getText().toString();
                newPhaseText = String.valueOf(convertBaseToCode(9,phase.getSelectedItem().toString()));
                if(amperLayout.getVisibility()==View.VISIBLE)
                    newAmperText = String.valueOf(convertBaseToCode(10 , amper.getSelectedItem().toString()));
               // if(powerLayout.getVisibility()==View.VISIBLE)
                  //  newPowerText = power.getText().toString();
                newTariffText =  tariff.getSelectedItem().toString();
                newVoltageText = voltage.getSelectedItem().toString();
                if(changeNeeded.isChecked())
                    changeneededText = "true";
                else
                    changeneededText = "false";

                if(moveNeeded.isChecked())
                    moveneededText = "true";
               else
                    moveneededText = "false";

                if(powerLayout.getVisibility()==View.GONE)
                    newPowerText = String.valueOf((Math.round(Double.valueOf(newPhaseText) * Double.valueOf(newAmperText)) / 5.0));
                else if(amperLayout.getVisibility()==View.GONE && !powerText.equals(null) && !powerText.equalsIgnoreCase(""))
                    newAmperText = String.valueOf(Math.round((5.0 * Double.valueOf(newPowerText)) / Double.valueOf(newPhaseText)));

                // this item should be inserted
                if (newDocNumber.isEmpty()) {
                    Toast toast = Toast.makeText(changeBranch.getTargetFragment().getActivity().getBaseContext()
                            , getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(30);
                    toast.show();
                    done = false;
                }

                if(powerLayout.getVisibility()==View.VISIBLE) {
                    // this item should be inserted
                    if (newPowerText.isEmpty()) {
                        Toast toast = Toast.makeText(changeBranch.getTargetFragment().getActivity().getBaseContext()
                                , getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(30);
                        toast.show();
                        done = false;
                    }
                }

                if(powerValue!=null) {
                    if (newPowerText.equalsIgnoreCase(powerValue)) {
                        Toast toast = Toast.makeText(changeBranch.getTargetFragment().getActivity().getBaseContext()
                                , getResources().getString(R.string.equal_power), Toast.LENGTH_SHORT);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(30);
                        toast.show();
                        done = false;
                    }
                }

                if(amperLayout.getVisibility()==View.VISIBLE) {
                    // this item should be inserted
                    if (newAmperText.isEmpty()) {
                        Toast toast = Toast.makeText(changeBranch.getTargetFragment().getActivity().getBaseContext()
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

                    result.putExtra("fabrik", fabrikNumberText);
                    result.putExtra("branchCode", branchCodeText);
                    result.putExtra("document", docNumberText);
                    result.putExtra("Address", addressText);
                    result.putExtra("FulName", nameText);
                    result.putExtra("type", newTypeText);
                    result.putExtra("phase", phaseText);
                    result.putExtra("amper", amperText);
                    result.putExtra("power", powerText);
                    result.putExtra("tariff", tariffText);
                    result.putExtra("voltage", voltageText);
                    result.putExtra("status", statusText);

                    result.putExtra("newdocument", newDocNumber);
                    result.putExtra("newtype", newTypeText);
                    result.putExtra("newphase",newPhaseText);
                    result.putExtra("newamper",newAmperText );
                  //  result.putExtra("newpower", newPowerText);
                    result.putExtra("newtariff", newTariffText);
                    result.putExtra("newvoltage", newVoltageText);
                    result.putExtra("moveneeded" , moveneededText);
                    result.putExtra("changeneeded", changeneededText);



                 /*   if(powerText!=null && Double.valueOf(newPowerText) > Double.valueOf(powerText)) {
                        result.putExtra("increase", "true");
                        result.putExtra("newstatus", "افزایش");
                        result.putExtra("decrease", "false");
                    }
                    else{
                        result.putExtra("decrease", "true");
                        result.putExtra("newstatus", "کاهش");
                        result.putExtra("increase", "false");
                    }
*/

                    if(changeBranchSeparation.isChecked())
                        result.putExtra("separation", "true");
                    else
                        result.putExtra("separation", "false");


                    if(changeBranchMerge.isChecked())
                        result.putExtra("merge", "true");
                    else
                        result.putExtra("merge", "false");

                    result.putExtra("position", position);
                    changeBranch.getTargetFragment().onActivityResult(req, req, result);

                    changeBranch.dismiss();
                }
            }
        });
    }

    /**
     * if something happen to view like change type from addi to demandi
     * it's handled bye this method like ...
     * addi has phase 1
     * demandi has phase 3
     */
    private void changeHandler() {
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(type.getSelectedItem().toString().equalsIgnoreCase("عادی")){
                    amperLayout.setVisibility(View.VISIBLE);
                    powerLayout.setVisibility(View.GONE);

                   // ChangeBranchTbl.find(ChangeBranchTbl.class , "Request_Code = ?", request_)
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

                    if(power.getText().toString().isEmpty())
                        power.setTextSize(getResources().getInteger(R.integer.demandedFragmentEditTextSize));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
/*        changeBranchIncrease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    changeBranchDecrease.setChecked(false);
            }
        });
        changeBranchDecrease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    changeBranchIncrease.setChecked(false);
            }
        });*/
        changeBranchMerge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    changeBranchSeparation.setChecked(false);
            }
        });
        changeBranchSeparation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    changeBranchMerge.setChecked(false);
            }
        });
    }

    /**
     * receive old information about counter by server callling fabrik number
     */
    private void getDataFromFubrikNumber() {
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFabrikNumber = fabrikNumber.getText().toString();
                if (makeURLReady()) {
                    try {
                        new callSoapWebService().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    viewDataInLayout();
                }
            }
        });

    }

    /**
     * load Data with fabrik number
     */
    private void viewDataInLayout() {
        docNumber.setText( branchSLRValue );
        docNumberText = branchSLRValue;

        branchCodeTextView.setText(branchCodeValue);
        branchCodeText = branchCodeValue;

        oldPowerTextView.setText( powerValue );
        powerText = powerValue;

        oldPhaseTextView.setText(phaseValue);
        phaseText = phaseValue;

        oldAmperTextView.setText(amperValue);
        amperText = amperValue;

        nameTextView.setText(nameValue);
        nameText = nameValue;

        //addressTextView.setText(addressValue);
        addressText = addressValue;
    }

    /**
     * this method try to get src address and port from application preferences
     * if it is not settled one request Toast show to user that he/she should set them in preferences fragment
     * if every thing does right a true value returned
     * @return boolean value
     */
    private Boolean makeURLReady() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String webServicesAddress = preferences.getString("webServicesAddress", "");
        if(webServicesAddress.isEmpty()){
            Toast toast = Toast.makeText(getActivity().getBaseContext()
                    , getResources().getString(R.string.src_invalid), Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toast.show();
            return false;
        }

        String port = preferences.getString("port","");
        if(port.isEmpty()){
            Toast toast = Toast.makeText(getActivity().getBaseContext()
                    , getResources().getString(R.string.port_invalid), Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toast.show();
            return false;
        }

        if(!port.equalsIgnoreCase("") && !webServicesAddress.equalsIgnoreCase("") ){
            URL = String.format("http://%s/SearchService.asmx",webServicesAddress+":"+port);
        }
        return true;
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

    /**
     * web service call is instance of async class
     * there is a progress dialog that you can use it or not
     * progress dialog is used to show a dialog progress to user when receiving data process is completing
     */
    class callSoapWebService extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog = new ProgressDialog(getActivity());

        /**
         * any thing you need to be done before service call running
         */
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getResources().getString(R.string.Downloading_data));
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    callSoapWebService.this.cancel(true);
                }
            });
        }

        /**
         * any thing you need to be done after service call running , such as canceling progress dialog
         */
        @Override
        protected void onPostExecute(String temp) {
            this.progressDialog.cancel();
        }

        /**
         *
         * @param urls
         * first we need to create Soap Object with namespace and method name that we created before
         * every properties that server need you can add to this object
         * then you should serialize your request and make it ready to send through http call
         * response is a soap object and need to convert any property of that to soap primitive
         * soap primitive is a jason object you need
         *
         * @return
         */
        @Override
        protected String doInBackground(String... urls) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("userName", userName);
            request.addProperty("token", token);
            request.addProperty("userId", userID);
            request.addProperty("fabrikNumber", newFabrikNumber);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);

                SoapPrimitive sp = (SoapPrimitive) ((SoapObject) envelope.bodyIn).getProperty(0);
                JSONObject jo = new JSONObject(sp.toString());
                joParser(jo);
                return sp.toString();
            } catch (Exception execption) {
                e = new ShowExceptions(execption.getMessage());
            }
            return null;
        }

        /**
         *
         * @param jsonObject
         * jo is a jason object contains informations about any cartable row
         * CartableListDB is a table in database to storing data receive from server
         * only cartable data stored to DB that its request number is not stored before
         * the "isThere" boolean flag use to handle this
         */
        private void joParser(JSONObject jsonObject) {
            try {
                branchSLRValue = jsonObject.get("BranchSrl").toString();
                branchCodeValue = jsonObject.get("BranchCode").toString();
                powerValue = jsonObject.get("OldPwrCnt").toString();
                phaseValue = jsonObject.get("OldPhs").toString();
                amperValue = jsonObject.get("OldAmp").toString();
                typeValue = jsonObject.get("OldBranchTypeCode").toString();
                tariffValue = jsonObject.get("TrfHCode").toString();
                voltageValue = jsonObject.get("VoltCode").toString();
                nameValue = jsonObject.get("FullName").toString();
                addressValue = jsonObject.get("Address").toString();
                actionType = jsonObject.get("ActionType").toString();
                requestActionType = jsonObject.get("RequestActionType").toString();

                if(typeValue.equalsIgnoreCase("1"))
                    amperValue = String.valueOf(Math.round((5.0 * Double.valueOf(powerValue)) / Double.valueOf(phaseValue)));
                else
                    powerValue = String.valueOf((Math.round(Double.valueOf(phaseValue) * Double.valueOf(amperValue)) / 5.0));

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

}