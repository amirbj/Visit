package com.andc.slidingmenu.adapter;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.andc.slidingmenu.Fragments.Cartable.DemandedBranch.CartableNewBranch;
import com.andc.slidingmenu.R;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.NewBranchTbl;

/**
 * Created by win on 4/18/2015.
 */
public class CartableNewBranchAdapter extends BaseAdapter {

    CartableNewBranch cartableNewBranch;
    ArrayList<NewBranchTbl> newBranchList;
    TextView phase;
    TextView amper;
    TextView power;
    TextView tariff;
    TextView count;
    CheckBox tafkik;

    public CartableNewBranchAdapter(CartableNewBranch cartableNewBranch) {
        this.cartableNewBranch = cartableNewBranch;
        newBranchList = new ArrayList<NewBranchTbl>();
    }

    public ArrayList<NewBranchTbl> getNewBranchList() {

        return newBranchList;
    }

    @Override
    public int getCount() {
        return newBranchList.size();
    }

    @Override
    public NewBranchTbl getItem(int position) {
        return newBranchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_view_new_branch, parent, false);
        }

        phase = (TextView) convertView.findViewById(R.id.new_branch_phase);
        amper = (TextView) convertView.findViewById(R.id.new_branch_amper);
        power = (TextView) convertView.findViewById(R.id.new_branch_power);
        tariff = (TextView) convertView.findViewById(R.id.new_branch_tariff);
        count = (TextView) convertView.findViewById(R.id.new_branch_count);
        tafkik = (CheckBox) convertView.findViewById(R.id.new_branch_tafkik);

        power.setGravity(Gravity.CENTER);
        amper.setGravity(Gravity.CENTER);
        power.setGravity(Gravity.CENTER);
        tariff.setGravity(Gravity.CENTER);
        count.setGravity(Gravity.CENTER);
        tafkik.setGravity(Gravity.CENTER);

       // phase.setText(String.valueOf(this.getNewBranchList().get(position).Phs));
        //amper.setText(String.valueOf(this.getNewBranchList().get(position).Amp));
        power.setText(String.valueOf(this.getNewBranchList().get(position).Pwrcnt));


            if(this.getNewBranchList().get(position).TrfHcode == 290)
                tariff.setText("خانگی");
            if(this.getNewBranchList().get(position).TrfHcode == 291)
                tariff.setText("عمومی");
            if(this.getNewBranchList().get(position).TrfHcode == 292)
                tariff.setText("کشاورزی");
            if(this.getNewBranchList().get(position).TrfHcode == 293)
                tariff.setText("صنعتی");
            if(this.getNewBranchList().get(position).TrfHcode == 294)
                tariff.setText("سایر مصارف");

        if(this.getNewBranchList().get(position).Phs == 250)
            phase.setText("تک فاز");
        if(this.getNewBranchList().get(position).Phs == 251)
            phase.setText("سه فاز");

        if(this.getNewBranchList().get(position).Amp == 261)
            amper.setText(String.valueOf(5));
        if(this.getNewBranchList().get(position).Amp == 262)
            amper.setText(String.valueOf(10));
        if(this.getNewBranchList().get(position).Amp == 263)
            amper.setText(String.valueOf(15));
        if(this.getNewBranchList().get(position).Amp == 264)
            amper.setText(String.valueOf(25));
        if(this.getNewBranchList().get(position).Amp == 265)
            amper.setText(String.valueOf(32));
        /*
        number 13 is tafkik
         */
            if(this.getNewBranchList().get(position).RequestActionType == 13 && this.getNewBranchList().get(position).ActionType == 1) {
                tafkik.setChecked(true);
            }
            else{
                tafkik.setChecked(false);
            }



        count.setText(String.valueOf(this.getNewBranchList().get(position).Count));


        newBranchPainting(cartableNewBranch, position, convertView);

        return convertView;
    }

    private void newBranchPainting(CartableNewBranch cartableNewBranch, int position, View convertView) {
        if(position%2==0) {
            ColorDrawable cd = new ColorDrawable( cartableNewBranch.getActivity().getResources().getColor(R.color.dark_gray) );
            convertView.setBackground(cd);
        }
        else if(position%2==1) {
            ColorDrawable cd = new ColorDrawable( cartableNewBranch.getActivity().getResources().getColor(R.color.cream) );
            convertView.setBackground(cd);
        }
    }

}
