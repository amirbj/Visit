package com.andc.slidingmenu.adapter;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andc.slidingmenu.Fragments.Cartable.Materials.CartableCollectMaterial;
import com.andc.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.CollectMeterTbl;
import slidingmenu.andc.com.dataaccess.CollectionTbl;

/**
 * Created by Bijarchian on 3/13/2017.
 */

public class CartableOtherMaterialsAdapter extends BaseAdapter {
    ArrayList<CollectionTbl> materialList;
    CartableCollectMaterial cartableCollectMaterial;

    public CartableOtherMaterialsAdapter(CartableCollectMaterial collectMaterial){

     this.cartableCollectMaterial = collectMaterial;
        this.materialList = new ArrayList<CollectionTbl>();

    }
    @Override
    public int getCount() {
        return materialList.size();
    }

    @Override
    public CollectionTbl getItem(int position) {
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
            convertView = inflater.inflate(R.layout.list_view_collect_other_materials, parent, false);

        }

        TextView goods = (TextView) convertView.findViewById(R.id.cartable_material_other_goods);
        TextView type = (TextView) convertView.findViewById(R.id.cartable_material_other_type);
        TextView number = (TextView) convertView.findViewById(R.id.cartable_material_other_number);
        TextView desc = (TextView) convertView.findViewById(R.id.cartable_material_other_desc);

        CollectionTbl collectionTbl = getMaterialList().get(position);
        goods.setText(convertBaseToName(-1 ,collectionTbl.MainObjectcode));
        type.setText(convertBaseToName(collectionTbl.MainObjectcode,collectionTbl.SubObjectcode));
        number.setText(String.valueOf(collectionTbl.Quantity));
        desc.setText(collectionTbl.Desc);



        measurmentPainting(this.cartableCollectMaterial, position, convertView);
        return convertView;
    }

    private void measurmentPainting(CartableCollectMaterial cartableCollectMaterial, int position, View convertView) {
        if(position%2==0) {
            ColorDrawable cd = new ColorDrawable( cartableCollectMaterial.getActivity().getResources().getColor(R.color.dark_gray) );
            convertView.setBackground(cd);
        }
        else if(position%2==1) {
            ColorDrawable cd = new ColorDrawable( cartableCollectMaterial.getActivity().getResources().getColor(R.color.cream) );
            convertView.setBackground(cd);
        }
    }

    public ArrayList<CollectionTbl> getMaterialList() {
        return materialList;
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
}
