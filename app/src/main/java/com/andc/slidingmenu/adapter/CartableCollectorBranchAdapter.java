package com.andc.slidingmenu.adapter;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import com.andc.slidingmenu.Fragments.Cartable.DemandedBranch.CartableCollectorBranch;
import com.andc.slidingmenu.R;

import slidingmenu.andc.com.dataaccess.CollectBranchTbl;

/**
 * Created by win on 4/27/2015.
 */
public class CartableCollectorBranchAdapter extends BaseAdapter {

    CartableCollectorBranch cartableCollectorBranch;
    ArrayList<CollectBranchTbl> collectorBranchList;
    TextView tariff;
    TextView fabrikNumber;
    TextView power;
    TextView amper;
    TextView phase;
    TextView docNumber;
    CheckBox edgham;

    public CartableCollectorBranchAdapter(CartableCollectorBranch cartableCollectorBranch) {
        this.cartableCollectorBranch = cartableCollectorBranch;
        collectorBranchList = new ArrayList<CollectBranchTbl>();
    }

    public ArrayList<CollectBranchTbl> getCollectorBranchList() {
        return collectorBranchList;
    }

    @Override
    public int getCount() {
        return collectorBranchList.size();
    }

    @Override
    public CollectBranchTbl getItem(int position) {
        return collectorBranchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *      This is the most important method.
     *      This method will be called to get the View for each row.
     *      This is the method where we can use our custom listitem and bind it with the data.
     *      The first argument passed to getView is the listview item position ie row number.
     *      The second parameter is recycled view reference(as we know listview recycles a view, you can confirm through this parameter).
     *      Third parameter is the parent to which this view will get attached to.
     */
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_view_collect_branch, parent, false);
        }

//        tariff = (TextView) convertView.findViewById(R.id.collect_branch_tariff);
        fabrikNumber = (TextView) convertView.findViewById(R.id.collect_branch_fabrik);
       // power = (TextView) convertView.findViewById(R.id.collect_branch_power);
//        amper = (TextView) convertView.findViewById(R.id.collect_branch_amper);
        phase = (TextView) convertView.findViewById(R.id.collect_branch_phase);
        docNumber = (TextView) convertView.findViewById(R.id.collect_branch_document_number);
        edgham = (CheckBox) convertView.findViewById(R.id.collect_branch_edgham);


//        tariff.setGravity(Gravity.CENTER);
        fabrikNumber.setGravity(Gravity.CENTER);
//        power.setGravity(Gravity.CENTER);
//        amper.setGravity(Gravity.CENTER);
        phase.setGravity(Gravity.CENTER);
        docNumber.setGravity(Gravity.CENTER);

//        tariff.setText( this.getCollectorBranchList().get(position).tariff);

        fabrikNumber.setText(this.getCollectorBranchList().get(position).FabrikNumber);

//        power.setText(String.valueOf(this.getCollectorBranchList().get(position).Pwrcnt));
//        amper.setText( this.getCollectorBranchList().get(position).amper );
        phase.setText(this.getCollectorBranchList().get(position).Phs + " / " + this.getCollectorBranchList().get(position).Amp + " / " + this.getCollectorBranchList().get(position).Pwrcnt);
        docNumber.setText(String.valueOf(this.getCollectorBranchList().get(position).BranchSrl));
        if(this.getCollectorBranchList().get(position).RequestActionType ==14){
            edgham.setChecked(true);
        }
        else{
            edgham.setChecked(false);
        }
        collectorPainting(cartableCollectorBranch, position, convertView);

        return convertView;
    }

    /**
     *     we paint cartable list background. odds are cream, evens are dark_gray
     */
    private void collectorPainting(CartableCollectorBranch cartableCollectorBranch, int position, View convertView) {
        if(position%2==0) {
            ColorDrawable cd = new ColorDrawable( cartableCollectorBranch.getActivity().getResources().getColor(R.color.dark_gray) );
            convertView.setBackground(cd);
        }
        else if(position%2==1) {
            ColorDrawable cd = new ColorDrawable( cartableCollectorBranch.getActivity().getResources().getColor(R.color.cream) );
            convertView.setBackground(cd);
        }
    }

}
