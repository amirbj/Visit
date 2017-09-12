package com.andc.slidingmenu.adapter;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andc.slidingmenu.Fragments.Cartable.Materials.CartableCollectMaterial;
import com.andc.slidingmenu.Fragments.Cartable.Materials.CartableInstallMaterial;
import com.andc.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.CollectionTbl;
import slidingmenu.andc.com.dataaccess.InstalationTbl;

/**
 * Created by Bijarchian on 3/14/2017.
 */

public class CartableOtherInstalationAdapter extends BaseAdapter {
    ArrayList<InstalationTbl> materialList;
    CartableInstallMaterial cartableInstallMaterial;

    public CartableOtherInstalationAdapter(CartableInstallMaterial cartableInstallMaterial){
        this.cartableInstallMaterial = cartableInstallMaterial;
        this.materialList = new ArrayList<InstalationTbl>();

    }
    @Override
    public int getCount() {
        return materialList.size();
    }

    @Override
    public InstalationTbl getItem(int position) {
        return materialList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_view_install_other_materials, parent, false);

        }

        TextView goods = (TextView) convertView.findViewById(R.id.cartable_install_material_other_goods);
        TextView type = (TextView) convertView.findViewById(R.id.cartable_install_material_other_type);
        TextView number = (TextView) convertView.findViewById(R.id.cartable_install_material_other_number);
        TextView resp = (TextView) convertView.findViewById(R.id.cartable_install_material_other_responsible);

        InstalationTbl instalationTbl = materialList.get(position);
       goods.setText(convertBaseToName(-1, instalationTbl.MainObjectcode));
        type.setText(convertBaseToName(instalationTbl.MainObjectcode, instalationTbl.SubObjectcode));
        number.setText(String.valueOf(instalationTbl.Quantity));
        if(instalationTbl.Promiser == 1){
            resp.setText("متقاضی");
        }
        else{
            resp.setText("شرکت");
        }
        measurmentPainting(this.cartableInstallMaterial, position, convertView);
        return convertView;
    }
    private void measurmentPainting(CartableInstallMaterial cartableInstallMaterial, int position, View convertView) {
        if(position%2==0) {
            ColorDrawable cd = new ColorDrawable( cartableInstallMaterial.getActivity().getResources().getColor(R.color.dark_gray) );
            convertView.setBackground(cd);
        }
        else if(position%2==1) {
            ColorDrawable cd = new ColorDrawable( cartableInstallMaterial.getActivity().getResources().getColor(R.color.cream) );
            convertView.setBackground(cd);
        }
    }

    public ArrayList<InstalationTbl> getMaterialList() {
        return materialList;
    }

    public String convertBaseToName(int tabletcode, int value ){

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and TabletCode = ?", String.valueOf(tabletcode), String.valueOf(value));


        BaseMaterialTbl materialbase =  base.get(0);

        return materialbase.Description;

    }
}
