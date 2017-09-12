package com.andc.slidingmenu.Dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.andc.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;

/**
 * Created by Bijarchian on 3/7/2017.
 */

public class MeterMaterial extends DialogFragment {

    public int deleteMaterial = 9;
    public int editMaterial = 8;

    Spinner contorType;
    EditText desc;
    EditText badane;
    EditText parvande;
    public Button register;
    public Button delete;


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog!= null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.create_collect_contor, container, false);
        getDialog().setTitle("اضافه کردن کالای جمع آوری");


       definition(rootview);
       setAdapter();

        Delete(this);
        Register(this);
        return rootview;
    }

    private void setAdapter() {

        ArrayAdapter typeAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, convertCodeTolist(15));
        contorType.setAdapter(typeAdapter);
    }

    private void definition(View rootview) {
        Bundle info = getArguments();


        badane = (EditText) rootview.findViewById(R.id.cartable_collect_meter_badane);
        parvande = (EditText) rootview.findViewById(R.id.cartable_collect_meter_parvande);
        desc = (EditText) rootview.findViewById(R.id.cartable_collect_meter_description);
        contorType = (Spinner) rootview.findViewById(R.id.cartable_collect_meter_type);
        register = (Button) rootview.findViewById(R.id.create_contor_edit);
        delete = (Button) rootview.findViewById(R.id.delete_contor);

        badane.setText(info.getString("badane"));
        parvande.setText(info.getString("parvande"));
        desc.setText(info.getString("desc"));

    }

    private void Delete(final MeterMaterial metermaterial){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new AlertDialog.Builder(getActivity()).
                        setTitle("حذف کالای جمع آوری")
                        .setMessage("آیا از حذف کالای مورد نظر اطمینان دارید")
                        .setNegativeButton("خیر", null)
                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.putExtra("position", getArguments().getString("position"));

                                metermaterial.getTargetFragment().onActivityResult(deleteMaterial, deleteMaterial,intent);
                                metermaterial.dismiss();
                            }
                        }).create();

                dialog.show();


            }
        });

    }

    private void Register(final MeterMaterial collect){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int req = collect.getTargetRequestCode();
                String badaneText = badane.getText().toString();
                String parvandeText = parvande.getText().toString();
                String descText= desc.getText().toString();
                String contorText = contorType.getSelectedItem().toString();
                int contorCode = convertBaseToCode(15, contorText);

                Intent intent = new Intent();
                intent.putExtra("position", getArguments().getString("position"));
                intent.putExtra("badane", badaneText);
                intent.putExtra("parvande",parvandeText);
                intent.putExtra("desc", descText);
                intent.putExtra("contorcode", contorCode);
                collect.getTargetFragment().onActivityResult(req,req, intent);
                collect.dismiss();

            }
        });


    }

    public List<?> convertCodeTolist(int tabletcode){
        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? ", String.valueOf(tabletcode));
        List<String> baselist = new ArrayList<>();
        for(BaseMaterialTbl material:base){
            baselist.add(material.Description);
        }
        return baselist;
    }
    public int convertBaseToCode(int tabletcode , String name){

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and Description = ?", String.valueOf(tabletcode) , name);

        BaseMaterialTbl materialbase = base.get(0);
        return  materialbase.Tablet_Code;
    }
}
