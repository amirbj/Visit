package com.andc.slidingmenu.Dialogs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
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

import java.util.concurrent.ExecutionException;

/**
 * Created by win on 4/28/2015.
 */
public class CollectBranch extends DialogFragment {

    public int cancelBranch = 9;
    public int deleteBranch = 8;
    public int newBranch = 6;
    public int editBranch = 7;

    public LinearLayout amperLayout;
    public LinearLayout powerLayout;

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
    public String branchStatusTypeValue;
    public String actionType;
    public String requestActionType;

    public Spinner type;
    public TextView docNumber;
    public EditText fabrikNumber;
    public Spinner phase;
    public Spinner amper;
    public EditText power;
    public Spinner tariff;
    public Spinner voltage;
    public Spinner branchStatusType;
    public String position;
    public CheckBox collectBranchMerge;

    public String docNumberText;
    public String fabrikNumberText;
    public String typeText;
    public String phaseText;
    public String amperText;
    public String powerText;
    public String tariffText;
    public String voltageText;
    public String branchStatusTypeText;

    public Button register;
    public Button delete;
    public Button receive;
    public Button cancel;

    private static final String NAMESPACE = "http://tempuri.org/";
    private static String URL;
    private static final String SOAP_ACTION = "http://tempuri.org/GetNewBranchRequestByFabrikNumber";
    private static final String METHOD_NAME = "GetNewBranchRequestByFabrikNumber";

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of collect branch
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_collect_branch, container);
        Bundle bundle = getArguments();
        getDialog().setTitle(getResources().getString(R.string.collect_branch));

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
        changeHandler();
        getDataFromFubrikNumber();

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
        fabrikNumber = (EditText) rootView.findViewById(R.id.create_collect_branch_fabrik_number);
        docNumber = (TextView) rootView.findViewById(R.id.create_collect_branch_document_number);
        type = (Spinner) rootView.findViewById(R.id.create_collect_branch_type);
        phase = (Spinner) rootView.findViewById(R.id.create_collect_branch_phase);
        amper = (Spinner) rootView.findViewById(R.id.create_collect_branch_amper);
        power = (EditText) rootView.findViewById(R.id.create_collect_branch_power);
        tariff = (Spinner) rootView.findViewById(R.id.create_collect_branch_tariff);
        voltage = (Spinner) rootView.findViewById(R.id.create_collect_branch_voltage);
        branchStatusType = (Spinner) rootView.findViewById(R.id.create_collect_branch_temp_permanent);
        collectBranchMerge = (CheckBox) rootView.findViewById(R.id.collect_branch_merge);
        type.requestFocus();
        register = (Button) rootView.findViewById(R.id.create_all_collect_branch);
        delete = (Button) rootView.findViewById(R.id.delete_all_collect_branch);
        receive = (Button) rootView.findViewById(R.id.receive_all_collect_branch);
        cancel = (Button) rootView.findViewById(R.id.cancel_all_collect_branch);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences( getActivity() );
        this.userName = preferences.getString("username","");
        this.token = preferences.getString("token","");
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

        fabrikNumber.setText(bundle.getString("fabrik"));
        branchCodeValue = bundle.getString("branchCode");

        docNumber.setText(bundle.getString("document"));

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

        spinnerPosition = Integer.valueOf( bundle.getString("BranchStatusType") );
        if(spinnerPosition==4)
            spinnerPosition = 1;
        else
            spinnerPosition = 0;

        branchStatusType.setSelection(spinnerPosition);

        position = bundle.getString("position");

        if(bundle.getString("merge")!=null) {
            if (bundle.getString("merge").equalsIgnoreCase("true"))
                collectBranchMerge.setChecked(true);
            else
                collectBranchMerge.setChecked(false);
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
     * @param collectBranch
     * when user wants to delete item, confirm dialog appears
     * position of item send to caller fragment to remove from the main list
     */
    @TargetApi(17)
    private void delete(final CollectBranch collectBranch) {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(collectBranch.getActivity())
                        .setTitle(getResources().getString(R.string.dialog_delete_title))
                        .setMessage(getResources().getString(R.string.dialog_delete_body))
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent result = new Intent();
                                result.putExtra("position", position);
                                collectBranch.getTargetFragment().onActivityResult(deleteBranch, deleteBranch, result);
                                collectBranch.dismiss();
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
     * @param collectBranch
     * when user wants to delete item, confirm dialog appears
     * position of item send to caller fragment to remove from the main list
     */
    @TargetApi(17)
    private void cancel(final CollectBranch collectBranch) {
        if(cancel!=null && cancel.getVisibility()==View.VISIBLE) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(collectBranch.getActivity())
                            .setTitle(getResources().getString(R.string.dialog_cancel_title))
                            .setMessage(getResources().getString(R.string.dialog_cancel_body))
                            .setNegativeButton(getResources().getString(R.string.no), null)
                            .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Intent result = new Intent();
                                    result.putExtra("position", position);
                                    collectBranch.getTargetFragment().onActivityResult(cancelBranch, cancelBranch, result);
                                    collectBranch.dismiss();
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
    }

    /**
     *
     * @param collectBranch
     * read data from list and dialog box and save them in a bundle and return to caller fragment
     */
    private void register(final CollectBranch collectBranch) {
        final int req = collectBranch.getTargetRequestCode();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean done = true;
                typeText = type.getSelectedItem().toString();
                fabrikNumberText = fabrikNumber.getText().toString();
                docNumberText = docNumber.getText().toString();
                typeText = type.getSelectedItem().toString();
                phaseText = phase.getSelectedItem().toString();
                amperText = amper.getSelectedItem().toString();
                powerText = power.getText().toString();
                tariffText = tariff.getSelectedItem().toString();
                voltageText = voltage.getSelectedItem().toString();
                branchStatusTypeText = branchStatusType.getSelectedItem().toString();
                if(branchStatusTypeText.equalsIgnoreCase("موقت"))
                    branchStatusTypeText = "4";
                else
                    branchStatusTypeText = "3";

                // calculate power value if user does not insert
                if(powerLayout.getVisibility()==View.GONE)
                    powerText = String.valueOf(Math.round((Double.valueOf(phaseText) * Double.valueOf(amperText)) / 5.0));
                else if(amperLayout.getVisibility()==View.GONE)
                    amperText = String.valueOf(Math.round((5.0 * Double.valueOf(powerText)) / Double.valueOf(phaseText)));

                // these to field should be inserted
                if (fabrikNumberText.isEmpty()) {
                    Toast toast = Toast.makeText(collectBranch.getTargetFragment().getActivity().getBaseContext()
                            , getResources().getString(R.string.empty_fabrik), Toast.LENGTH_SHORT);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(30);
                    toast.show();
                    done = false;
                }

                // if branch type is demandi, power field should be filled
                if(powerLayout.getVisibility()==View.VISIBLE) {
                    if (powerText.isEmpty()) {
                        Toast toast = Toast.makeText(collectBranch.getTargetFragment().getActivity().getBaseContext()
                                , getResources().getString(R.string.wrong_power), Toast.LENGTH_SHORT);
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
                    result.putExtra("document", docNumberText);
                    result.putExtra("branchCode", branchCodeValue);
                    result.putExtra("type", typeText);
                    result.putExtra("phase", phaseText);
                    result.putExtra("amper", amperText);
                    result.putExtra("power", powerText);
                    result.putExtra("tariff", tariffText);
                    result.putExtra("voltage", voltageText);
                    result.putExtra("BranchStatusType", branchStatusTypeText);

                    if(collectBranchMerge.isChecked())
                        result.putExtra("merge", "true");
                    else
                        result.putExtra("merge", "false");

                    result.putExtra("position", position);
                    collectBranch.getTargetFragment().onActivityResult(req, req, result);

                    collectBranch.dismiss();
                }
            }
        });
    }

    /**
     * set adapter and values list for spinners
     */
    private void setAdapter() {

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.new_branch_type_array, R.layout.spinner_item);
        typeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        type.setAdapter(typeAdapter);


        ArrayAdapter<CharSequence> phaseAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.new_branch_phase_array, R.layout.spinner_item);
        phaseAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        phase.setAdapter(phaseAdapter);

        ArrayAdapter<CharSequence> amperAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.new_branch_amper_array, R.layout.spinner_item);
        amperAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        amper.setAdapter(amperAdapter);

        ArrayAdapter<CharSequence> tariffAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.new_branch_tariff_array, R.layout.spinner_item);
        tariffAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        tariff.setAdapter(tariffAdapter);

        ArrayAdapter<CharSequence> voltageAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.new_branch_voltage_array, R.layout.spinner_item);
        voltageAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        voltage.setAdapter(voltageAdapter);

        ArrayAdapter<CharSequence> branchStatusAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.collect_branch_branch_status_array, R.layout.spinner_item);
        branchStatusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        branchStatusType.setAdapter(branchStatusAdapter);

        power.setGravity(Gravity.CENTER);
        docNumber.setGravity(Gravity.CENTER);
        fabrikNumber.setGravity(Gravity.CENTER);
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

    /**
     * load Data with fabrik number
     */
    private void viewDataInLayout() {
        docNumber.setText( branchSLRValue );
        power.setText( powerValue );

        ArrayAdapter<CharSequence> myAdap = (ArrayAdapter<CharSequence>) phase.getAdapter();
        int spinnerPosition = myAdap.getPosition( phaseValue );
        phase.setSelection(spinnerPosition);

        myAdap = (ArrayAdapter<CharSequence>) amper.getAdapter();
        spinnerPosition = myAdap.getPosition( amperValue );
        amper.setSelection(spinnerPosition);

        myAdap = (ArrayAdapter<CharSequence>) type.getAdapter();
        if(typeValue!=null) {
            if (typeValue.equalsIgnoreCase("1"))
                spinnerPosition = myAdap.getPosition("دیماندی");

            else if (typeValue.equalsIgnoreCase("2"))
                spinnerPosition = myAdap.getPosition("خانگی");

            type.setSelection(spinnerPosition);
        }

        if(tariffValue!=null) {
            myAdap = (ArrayAdapter<CharSequence>) tariff.getAdapter();
            if (tariffValue.equalsIgnoreCase("1"))
                spinnerPosition = myAdap.getPosition("خانگی");

            else if (tariffValue.equalsIgnoreCase("2"))
                spinnerPosition = myAdap.getPosition("عمومی");

            else if (tariffValue.equalsIgnoreCase("3"))
                spinnerPosition = myAdap.getPosition("کشاورزی");

            else if (tariffValue.equalsIgnoreCase("4"))
                spinnerPosition = myAdap.getPosition("صنعتی");

            else if (tariffValue.equalsIgnoreCase("5"))
                spinnerPosition = myAdap.getPosition("سایر");

            tariff.setSelection(spinnerPosition);
        }

        if(voltageValue!=null) {
            myAdap = (ArrayAdapter<CharSequence>) voltage.getAdapter();
            if (voltageValue.equalsIgnoreCase("1"))
                voltageValue = "اولیه";

            else if (voltageValue.equalsIgnoreCase("2"))
                voltageValue = "ثانویه";

            spinnerPosition = myAdap.getPosition(voltageValue);
            voltage.setSelection(spinnerPosition);
        }

        if(requestActionType!=null) {
            if (requestActionType.equalsIgnoreCase("14"))
                collectBranchMerge.setChecked(true);
        }
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
                actionType = jsonObject.get("ActionType").toString();
                requestActionType = jsonObject.get("RequestActionType").toString();

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}