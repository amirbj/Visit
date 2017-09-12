package com.andc.slidingmenu.Dialogs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.andc.slidingmenu.R;
import com.andc.slidingmenu.Utility.DBHelper;

import java.util.ArrayList;

import slidingmenu.andc.com.dataaccess.UnitDB;

/**
 * Created by win on 5/10/2015.
 */
public class UpdateFragment extends DialogFragment {
    public Spinner material;
    public Spinner template;
    public Spinner resposible;
    public Spinner unit;
    public Button update;
    public AlertDialog mDialog;
    UpdateFragment updateFragment = this;

    /**
     * @param savedInstanceState prepare layout of update
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_update, null);
        mDialog = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setTitle(getActivity().getResources().getString(R.string.dialog_update_title))
                .create();


        definition(rootView);
        setAdapter();
        updateHandler(update);

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
     * @param rootView find any views on cartable view and just define variables
     */
    private void definition(View rootView) {
        this.material = ((Spinner) (rootView.findViewById(R.id.materialDB)));
        this.template = ((Spinner) (rootView.findViewById(R.id.templateDB)));
        this.resposible = ((Spinner) (rootView.findViewById(R.id.responsibilityDB)));
        this.unit = ((Spinner) (rootView.findViewById(R.id.unitDB)));
        this.update = (Button) rootView.findViewById(R.id.preferences_update);
    }

    /**
     * there are 4 spinner in app that we make table for them
     * we can update them in app manually by calling web services
     * array of elements in material template, array of elements of materials
     * array of responsible person for collecting branch
     * array of materials unit
     */
    private void setAdapter() {
        /*MaterialTemplateDB templateDB = new MaterialTemplateDB(this.getActivity());
        ArrayList<String> templateArray = new ArrayList<String>();
        for (int i = 1; i <= templateDB.getSize(); i++)
            templateArray.add(templateDB.getItem(i));
*/
      //  ArrayAdapter<String> templateAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, templateArray);
      //  templateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
      //  template.setAdapter(templateAdapter);

       // MaterialDB mattDB = new MaterialDB(this.getActivity());
        ArrayList<String> mattArray = new ArrayList<String>();
       // for (int i = 1; i <= mattDB.getSize(); i++)
          //  mattArray.add(mattDB.getItem(i));

        ArrayAdapter<String> mattAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, mattArray);
        mattAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        material.setAdapter(mattAdapter);

       // ResposibilityDB responseDB = new ResposibilityDB(this.getActivity());
       // ArrayList<String> responseArray = new ArrayList<String>();
      //  for (int i = 1; i <= responseDB.getSize(); i++)
        //    responseArray.add(responseDB.getItem(i));

       // ArrayAdapter<String> responseAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, responseArray);
      //  responseAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
      //  resposible.setAdapter(responseAdapter);

        UnitDB unitDB = new UnitDB(this.getActivity());
        ArrayList<String> unitArray = new ArrayList<String>();
        for (int i = 1; i <= unitDB.getSize(); i++)
            unitArray.add(unitDB.getItem(i));

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, unitArray);
        unitAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        unit.setAdapter(unitAdapter);

    }

    /**
     * @param update when we click on update button, app should get values of these 4 spinners from the server
     */
    private void updateHandler(Button update) {
        update.requestFocus();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetObservationsInitData getObservations = new GetObservationsInitData();
                getObservations.execute();
                mDialog.dismiss();
            }
        });
    }


    class GetObservationsInitData extends AsyncTask<Long, String, String> {
        private ProgressDialog pd = new ProgressDialog(getActivity());

        @Override
        protected void onPostExecute(String msg) {
            pd.dismiss();
        }

        @Override
        protected void onPreExecute() {
            pd.setIndeterminate(true);
            pd.setMessage(getActivity().getString(R.string.Downloading_data));
            pd.show();
        }

        @Override
        protected String doInBackground(Long... longs) {
            String rv = "";
            try {
                rv = DBHelper.InitObservesFromServer();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return rv;
        }
    }
}