package com.andc.slidingmenu.Dialogs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.andc.slidingmenu.R;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.UnitDB;

/**
 * Created by win on 4/18/2015.
 */
public class NewMaterial extends DialogFragment {

    public int deleteMaterial = 9;
    public int newMaterial = 7;
    public int editMaterial = 8;

    //  public EditText branchCode;
    public Spinner goods, type;
    public EditText estimated;
    //  public Spinner dataUnit;
    // public Spinner responsible;
    //  public Spinner status;
    public String position;
    public String branchCodeText;
    public String goodsText, typeTrxt;
    public String goodsCodeText;
    public String estimatedText;
    public String unitText;
    public String responsibleText;
    public String statusText;
    public Button register;
    public Button delete;
    public RadioGroup responsible;
    public RadioButton sherakt, moteghazi;

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState prepare layout of new material
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.create_new_material, container);
        Bundle bundle = getArguments();
        getDialog().setTitle(getResources().getString(R.string.cartable_measurment));

        definition(rootView);

        setAdapter();
        int req = this.getTargetRequestCode();
        if (req == newMaterial)
            delete.setVisibility(View.GONE);
        if (req == editMaterial)
            initialize(bundle);
        delete(this);
        register(this);

        return rootView;
    }

    /**
     * @param rootView find any views on cartable view and just define variables
     */
    private void definition(View rootView) {
        //  this.branchCode = (EditText) rootView.findViewById(R.id.cartable_new_material_subscrip);
        this.goods = (Spinner) rootView.findViewById(R.id.cartable_new_material_goods);
        this.type = (Spinner) rootView.findViewById(R.id.cartable_new_material_type);
        this.estimated = (EditText) rootView.findViewById(R.id.cartable_new_material_estimated);
        this.sherakt = (RadioButton) rootView.findViewById(R.id.cartable_new_material_sherkat);
        this.moteghazi = (RadioButton) rootView.findViewById(R.id.cartable_new_material_moteghazi);

        this.responsible = (RadioGroup) rootView.findViewById(R.id.cartable_new_material_moteahed);
        this.register = (Button) rootView.findViewById(R.id.create_all_new_material);
        this.delete = (Button) rootView.findViewById(R.id.delete_all_new_material);
    }

    /**
     * @param newMaterial when user wants to delete item, confirm dialog appears
     *                    position of item send to caller fragment to remove from the main list
     */
    @TargetApi(17)
    private void delete(final NewMaterial newMaterial) {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(newMaterial.getActivity())
                        .setTitle("حذف کالای نصب")
                        .setMessage("آیا از حذف کالای مورد نظر اطمینان دارید")
                        .setNegativeButton(getResources().getString(R.string.no), null)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent result = new Intent();
                                result.putExtra("position", position);
                                newMaterial.getTargetFragment().onActivityResult(deleteMaterial, deleteMaterial, result);
                                newMaterial.dismiss();
                            }
                        }).create();

                dialog.show();

                final int alertTitle = getActivity().getResources().getIdentifier("alertTitle", "id", "android");
                TextView messageText = (TextView) dialog.findViewById(alertTitle);
                messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            }
        });
    }

    /**
     * @param bundle get initial data from bundle of caller fragment and insert them to their related fields
     */
    private void initialize(Bundle bundle) {

        ArrayAdapter<CharSequence> myAdap = (ArrayAdapter<CharSequence>) goods.getAdapter();
        int spinnerPosition = myAdap.getPosition(bundle.getString("goods"));
        goods.setSelection(spinnerPosition);

        // myAdap = (ArrayAdapter<CharSequence>) dataUnit.getAdapter();
        // spinnerPosition = myAdap.getPosition(bundle.getString("unit"));
        // dataUnit.setSelection(spinnerPosition);

        //  myAdap = (ArrayAdapter<CharSequence>) status.getAdapter();
        // spinnerPosition = myAdap.getPosition(bundle.getString("status"));
        //  status.setSelection(spinnerPosition);

        // myAdap = (ArrayAdapter<CharSequence>) responsible.getAdapter();
        // spinnerPosition = myAdap.getPosition(bundle.getString("resposible"));
        //responsible.setSelection(spinnerPosition);

        //branchCode.setText(bundle.getString("subs"));
        estimated.setText(bundle.getString("estimated"));
        position = bundle.getString("position");

    }

    /**
     * @param newMaterial read data from list and dialog box and save them in a bundle and return to caller fragment
     */
    private void register(final NewMaterial newMaterial) {
        final int req = newMaterial.getTargetRequestCode();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean done = true;
                goodsText = goods.getSelectedItem().toString();
                typeTrxt = type.getSelectedItem().toString();
                goodsCodeText = String.valueOf(goods.getSelectedItemPosition() + 1);
                if (moteghazi.isChecked()) ;
                {
                    responsibleText = "متقاضی";
                }
                if (sherakt.isChecked()) {
                    responsibleText = "شرکت";
                }
                // unitText = dataUnit.getSelectedItem().toString();
                //  statusText = status.getSelectedItem().toString();
                //  responsibleText = responsible.getSelectedItem().toString();
                //branchCodeText = branchCode.getText().toString();
                estimatedText = estimated.getText().toString();

                if (estimatedText.isEmpty()) {
                    Toast toast = Toast.makeText(newMaterial.getTargetFragment().getActivity().getBaseContext()
                            , getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(30);
                    toast.show();
                    done = false;
                }

                if (done == true) {
                    Intent result = new Intent();

                    result.putExtra("type", typeTrxt);
                    result.putExtra("goods", goodsText);
                    result.putExtra("goodsCode", goodsCodeText);
                    result.putExtra("estimated", estimatedText);
                    result.putExtra("resposible", responsibleText);
                    result.putExtra("position", position);
                    newMaterial.getTargetFragment().onActivityResult(req, req, result);

                    newMaterial.dismiss();
                }
            }
        });
    }

    /**
     * set adapter and values list for spinners
     * some spinner have value in DB so we have to read them from DB first
     * these spinner have updatable value
     */
    private void setAdapter() {
        ArrayAdapter goodsadapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, getActivity().getResources().getStringArray(R.array.goods_list));
        goods.setAdapter(goodsadapter);
        goods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Item = goods.getSelectedItem().toString();
                ArrayAdapter typeadapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, convertCodeTolist(convertBaseToCode(-1, Item)));
                type.setAdapter(typeadapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*MaterialDB mat = new MaterialDB(this.getActivity());
        ArrayList<String> mattArray = new ArrayList<String>();
        for(int i=1; i<=mat.getSize(); i++) {
            if(mat.getItem(i)!=null)
                mattArray.add(mat.getItem(i));
        }
        ArrayAdapter<String> goodAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, mattArray);
        goodAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        goods.setAdapter(goodAdapter);

        UnitDB unit = new UnitDB(this.getActivity());
        ArrayList<String> unitArray = new ArrayList<String>();
        for(int i=1; i<=unit.getSize(); i++) {
            if(unit.getItem(i)!=null)
                unitArray.add(unit.getItem(i));
        }
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, unitArray);
        unitAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //dataUnit.setAdapter(unitAdapter);

        ResposibilityDB response = new ResposibilityDB(this.getActivity());
        ArrayList<String> responseArray = new ArrayList<String>();
        for(int i=1; i<=response.getSize(); i++) {
            if(response.getItem(i)!=null)
                responseArray.add(response.getItem(i));
        }
        ArrayAdapter<String> responsibleAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, responseArray);
        responsibleAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //responsible.setAdapter(responsibleAdapter);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource
                (getActivity(), R.array.new_material_status_array, R.layout.spinner_item);
        statusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //status.setAdapter(statusAdapter);

        //branchCode.setGravity(Gravity.CENTER);

        estimated.setGravity(Gravity.CENTER);

    }*/
    }

    public String convertBaseToName(int tabletcode, int value) {

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and TabletCode = ?", String.valueOf(tabletcode), String.valueOf(value));


        BaseMaterialTbl materialbase = base.get(0);

        return materialbase.Description;

    }

    public int convertBaseToCode(int tabletcode, String name) {

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and Description = ?", String.valueOf(tabletcode), name);

        BaseMaterialTbl materialbase = base.get(0);
        return materialbase.Tablet_Code;
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