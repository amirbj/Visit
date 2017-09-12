package com.andc.slidingmenu.adapter;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andc.slidingmenu.Dialogs.MeterMaterial;
import com.andc.slidingmenu.Fragments.Cartable.Materials.CartableCollectMaterial;
import com.andc.slidingmenu.R;

import java.util.ArrayList;
import java.util.List;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.CollectMeterTbl;

/**
 * Created by SiaJam on 2/1/2017.
 */

public class CartableCollectToolsAdapter extends BaseAdapter implements View.OnClickListener {
    ArrayList<CollectMeterTbl> materialList;
    CartableCollectMaterial cartableCollectMaterial;

    public int editNumber = 8;
   // int position = 0;

    public CartableCollectToolsAdapter(CartableCollectMaterial cartableCollect){
        this.cartableCollectMaterial = cartableCollect;
       this.materialList = new ArrayList<CollectMeterTbl>();
    }

    public int getPosition(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return materialList.size();
    }

    @Override
    public CollectMeterTbl getItem(int position) {
        return materialList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_view_collect_tools, parent, false);


        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendtodialog(position);
            }
        });
        TextView description = (TextView) convertView.findViewById(R.id.cartable_material_desc);
        TextView type_contor = (TextView)convertView.findViewById(R.id.cartable_material_type_contor);
//        TextView unit = (TextView)convertView.findViewById(R.id.cartable_material_unit);
        TextView parvande = (TextView)convertView.findViewById(R.id.cartable_material_parvande);
        TextView badane = (TextView)convertView.findViewById(R.id.cartable_material_badane);
        //    TextView subscrip = (TextView)convertView.findViewById(R.id.cartable_material_subscrip);

       CollectMeterTbl requestedItem = getMaterialList().get(position);
       description.setText(String.valueOf( requestedItem.Desc));
        if(requestedItem.SubObjectcode != 0)
        type_contor.setText(( convertBaseToName(15,requestedItem.SubObjectcode)));
        parvande.setText(String.valueOf( requestedItem.BranchSrl ));
        badane.setText(String.valueOf( requestedItem.FabrikNumber));
   //     goods.setText(String.valueOf( requestedItem.goods));
//        subscrip.setText(String.valueOf( requestedItem.branchCode ));

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

    public ArrayList<CollectMeterTbl> getMaterialList() {
        return materialList;
    }

    @Override
    public void onClick(View v) {


    }

    private void sendtodialog(int position) {
        Bundle temp = new Bundle();
        temp.putString("position", String.valueOf(position));
        temp.putString("parvande", String.valueOf(this.getItem(position).BranchSrl));
        temp.putString("badane", this.getItem(position).FabrikNumber);
        temp.putString("desc", this.getItem(position).Desc);
        temp.putString("contor", String.valueOf(this.getItem(position).SubObjectcode));

        FragmentManager fragmentManager = cartableCollectMaterial.getFragmentManager();
        MeterMaterial meterMaterial = new MeterMaterial();
        meterMaterial.show(fragmentManager, "MeterMaterial");
        meterMaterial.setTargetFragment(cartableCollectMaterial, editNumber);
        meterMaterial.setArguments(temp);
    }

    public String convertBaseToName(int tabletcode, int value ){

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and TabletCode = ?", String.valueOf(tabletcode), String.valueOf(value));


        BaseMaterialTbl materialbase =  base.get(0);

        return materialbase.Description;

    }

}
