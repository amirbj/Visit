package com.andc.slidingmenu.Dialogs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andc.slidingmenu.R;

/**
 * Created by win on 4/7/2015.
 */
public class PreferencesFragment extends DialogFragment {
    public String webServicesAddress = new String();
    public String port = new String();
    public String smsBody = new String();
    public Button register;
    public AlertDialog mDialog;

    /**
     *
     * @param savedInstanceState
     * prepare layout of preferences
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_preferences, null);
        mDialog = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setTitle(getActivity().getResources().getString(R.string.dialog_preferences_title))
                .create();

        getPreferences();
        definition(rootView);
        registerHandler(this, rootView);

        return mDialog;
    }

    @TargetApi(17)
    @Override
    public void onStart(){
        super.onStart();

        //Set Title Gravity
        final int alertTitle = this.getResources().getIdentifier( "alertTitle", "id", "android" );
        TextView messageText = (TextView)mDialog.findViewById(alertTitle);
        //messageText.setGravity(Gravity.Right);
        messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }

    /**
     * get address, port and sms body from app preferences
     */
    private void getPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(webServicesAddress.isEmpty())
            webServicesAddress = preferences.getString("webServicesAddress","");
        if(port.isEmpty())
            port = preferences.getString("port","");
        if(smsBody.isEmpty())
            smsBody = preferences.getString("smsbody","");
    }

    /**
     *
     * @param rootView
     * find any views on cartable view and just define variables
     */
    private void definition(View rootView) {
        register = (Button)rootView.findViewById(R.id.preferences_register);
        ((EditText)(rootView.findViewById(R.id.webServicesAddress))).setText(webServicesAddress);
        ((EditText)(rootView.findViewById(R.id.port))).setText(port);
        ((EditText)(rootView.findViewById(R.id.sms_body))).setText(smsBody);
        register.requestFocus();
    }


    /**
     *
     * @param preferencesFragment
     * @param rootView
     * this section enable user to insert server address port and sms body
     * sms body will use in cartable fragment section
     */
    private void registerHandler(final PreferencesFragment preferencesFragment, final View rootView) {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServicesAddress = ((EditText)(rootView.findViewById(R.id.webServicesAddress))).getText().toString();
                port = ((EditText)(rootView.findViewById(R.id.port))).getText().toString();
                smsBody = ((EditText)(rootView.findViewById(R.id.sms_body))).getText().toString();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("webServicesAddress",webServicesAddress);
                editor.putString("port",port);
                editor.putString("smsbody",smsBody);
                editor.apply();

                Toast toast = Toast.makeText(getActivity().getBaseContext()
                        , getResources().getString(R.string.preferences_saved), Toast.LENGTH_SHORT);
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTV = (TextView) toastLayout.getChildAt(0);
                toastTV.setTextSize(20);
                toast.show();

                //getFragmentManager().popBackStack();
                mDialog.dismiss();
            }
        });
    }

}
