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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.andc.slidingmenu.R;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;

/**
 * Created by SiaJam on 2/8/2017.
 */

public class CollectMaterial extends DialogFragment {

    public int deleteMaterial = 9;
    public int newMaterial = 11;
    public int editMaterial = 8;

    Spinner collectmaterial;
    Spinner materialtype;
    EditText number;
    EditText description;
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
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
       View rootview = inflater.inflate(R.layout.create_collect_material, container, false);
        getDialog().setTitle("اضافه کردن کالای جمع آوری");





        definition(rootview);
        setAdapter();
        int req = this.getTargetRequestCode();
        if(req ==11){
            delete.setVisibility(View.GONE);
        }
        Delete(this);
        Register(this);
        return rootview;
    }

   private void setAdapter() {

        ArrayAdapter goodsadapter = new ArrayAdapter(getActivity(), R.layout.spinner_item , getActivity().getResources().getStringArray(R.array.goods_list));
       collectmaterial.setAdapter(goodsadapter);
       collectmaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String Item = collectmaterial.getSelectedItem().toString();
               ArrayAdapter typeadapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, convertCodeTolist(convertBaseToCode(-1, Item)));
               materialtype.setAdapter(typeadapter);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });




   /*    MaterialDB ma = new MaterialDB(getActivity());
        ArrayList list = new ArrayList();
        for (int i=1; i<ma.getSize();i++) {
            if(ma.getItem(i)!=null)
            list.add(ma.getItem(i));

        }
        ArrayAdapter<String> goodadapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, list);
        goodadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        collectmaterial.setAdapter(goodadapter);
*/
    }

    private void definition(View rootView) {

         collectmaterial = (Spinner) rootView.findViewById(R.id.cartable_collect_material_goods);
         materialtype = (Spinner) rootView.findViewById(R.id.cartable_collect_material_type);
         number  = (EditText) rootView.findViewById(R.id.cartable_collect_material_estimated);
         description  = (EditText) rootView.findViewById(R.id.cartable_collect_material_description);
         this.register = (Button) rootView.findViewById(R.id.create_all_new_material);
         this.delete = (Button) rootView.findViewById(R.id.delete_all_new_material);
    }

 private void Delete(final CollectMaterial collectmaterial){
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

                            collectmaterial.getTargetFragment().onActivityResult(deleteMaterial, deleteMaterial, null);
                            collectmaterial.dismiss();
                         }
                     }).create();

             dialog.show();


         }
     });

 }

    private void Register(final CollectMaterial collect){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int req = collect.getTargetRequestCode();
                String goodstext = collectmaterial.getSelectedItem().toString();
                String typegoods = materialtype.getSelectedItem().toString();
                String num = number.getText().toString();
                String desc =description.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("good", goodstext);
                intent.putExtra("type", typegoods);
                intent.putExtra("number", num);
                intent.putExtra("desc", desc);
                collect.getTargetFragment().onActivityResult(req,req, intent);
                collect.dismiss();

            }
        });


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
