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
import com.andc.slidingmenu.Fragments.Cartable.DemandedBranch.CartableChangeBranch;
import com.andc.slidingmenu.R;

import slidingmenu.andc.com.dataaccess.ChangeBranchTbl;

/**
 * Created by win on 4/26/2015.
 */
public class CartableChangeBranchAdapter extends BaseAdapter {

    CartableChangeBranch cartableChangeBranch;
    ArrayList<ChangeBranchTbl> changeBranchList;
    TextView tariff;
    TextView power;
    TextView amper;
    TextView phase;
    TextView docNumber;
    TextView fabrikNumber;
    TextView status;

    TextView newTariff;
    TextView newFabrikNumber;
    TextView newPower;
    TextView newAmper;
    TextView newPhase;
    TextView newDocNumber;
    TextView newStatus;
    CheckBox moveCheck;
    String phsName, oldphsName;
    int ampValue, oldampValue;

    public CartableChangeBranchAdapter(CartableChangeBranch cartableChangeBranch) {
        this.cartableChangeBranch = cartableChangeBranch;
        changeBranchList = new ArrayList<ChangeBranchTbl>();
    }

    public ArrayList<ChangeBranchTbl> getChangeBranchList() {
        return changeBranchList;
    }

    @Override
    public int getCount() {
        return changeBranchList.size();
    }

    @Override
    public ChangeBranchTbl getItem(int position) {
        return changeBranchList.get(position);
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
            convertView = inflater.inflate(R.layout.list_view_change_branch, parent, false);
        }
         moveCheck = (CheckBox) convertView.findViewById(R.id.new_change_branch_move);
      tariff = (TextView) convertView.findViewById(R.id.change_branch_tariffe);
      //  fabrikNumber = (TextView) convertView.findViewById(R.id.change_branch_fabrik);
      //  power = (TextView) convertView.findViewById(R.id.change_branch_power);
//        amper = (TextView) convertView.findViewById(R.id.change_branch_amper);
        phase = (TextView) convertView.findViewById(R.id.change_branch_phase);
        docNumber = (TextView) convertView.findViewById(R.id.change_branch_document_number);
        status = (TextView) convertView.findViewById(R.id.change_branch_status);

        newTariff = (TextView) convertView.findViewById(R.id.new_change_branch_tariffe);
//        newFabrikNumber = (TextView) convertView.findViewById(R.id.new_change_branch_fabrik);
    //    newPower = (TextView) convertView.findViewById(R.id.new_change_branch_power);
//        newAmper = (TextView) convertView.findViewById(R.id.new_change_branch_amper);
        newPhase = (TextView) convertView.findViewById(R.id.new_change_branch_phase);
        newDocNumber = (TextView) convertView.findViewById(R.id.new_change_branch_document_number);
        newStatus = (TextView) convertView.findViewById(R.id.new_change_branch_status);

//        tariff.setGravity(Gravity.CENTER);
//        fabrikNumber.setGravity(Gravity.CENTER);
//        power.setGravity(Gravity.CENTER);
//        amper.setGravity(Gravity.CENTER);
        phase.setGravity(Gravity.CENTER);
        docNumber.setGravity(Gravity.CENTER);
        status.setGravity(Gravity.CENTER);

//        newTariff.setGravity(Gravity.CENTER);
     //   newFabrikNumber.setGravity(Gravity.CENTER);
 //       newPower.setGravity(Gravity.CENTER);
//        newAmper.setGravity(Gravity.CENTER);
        newPhase.setGravity(Gravity.CENTER);
        newDocNumber.setGravity(Gravity.CENTER);
        newStatus.setGravity(Gravity.CENTER);
        moveCheck.setGravity(Gravity.CENTER);
        if(this.getChangeBranchList().get(position).OldtrfHcode == 290)
            tariff.setText("خانگی");
        if(this.getChangeBranchList().get(position).OldtrfHcode == 291)
            tariff.setText("عمومی");
        if(this.getChangeBranchList().get(position).OldtrfHcode == 292)
            tariff.setText("کشاورزی");
        if(this.getChangeBranchList().get(position).OldtrfHcode == 293)
            tariff.setText("صنعتی");
        if(this.getChangeBranchList().get(position).OldtrfHcode == 294)
            tariff.setText("سایر مصارف");
//        tariff.setText( this.getChangeBranchList().get(position).tariff );
   //     fabrikNumber.setText(this.getChangeBranchList().get(position).fabrikNumber);
      // power.setText( String.valueOf(this.getChangeBranchList().get(position).OldPwrcnt));
//        amper.setText( this.getChangeBranchList().get(position).amper );

        if(this.getChangeBranchList().get(position).OldPhs == 250)
            oldphsName = "تک فاز";
        if(this.getChangeBranchList().get(position).OldPhs == 251)
            oldphsName = "سه فاز";
        if(this.getChangeBranchList().get(position).OldAmp == 261)
            oldampValue = 5;
        if(this.getChangeBranchList().get(position).OldAmp == 262)
            oldampValue = 10;
        if(this.getChangeBranchList().get(position).OldAmp == 263)
            oldampValue = 15;
        if(this.getChangeBranchList().get(position).OldAmp == 264)
            oldampValue = 25;
        if(this.getChangeBranchList().get(position).OldAmp == 265)
            oldampValue = 32;

        phase.setText(this.getChangeBranchList().get(position).OldPwrcnt + " / " + oldampValue + " / " + oldphsName );
        docNumber.setText(String.valueOf(this.getChangeBranchList().get(position).FabrikNumber));
        if(this.getChangeBranchList().get(position).ActionType == 2)
            newStatus.setText("افزایش");

        if(this.getChangeBranchList().get(position).ActionType == 3)
        newStatus.setText("کاهش");

        if(this.getChangeBranchList().get(position).TrfHcode == 290)
            newTariff.setText("خانگی");
        if(this.getChangeBranchList().get(position).TrfHcode == 291)
            newTariff.setText("عمومی");
        if(this.getChangeBranchList().get(position).TrfHcode == 292)
            newTariff.setText("کشاورزی");
        if(this.getChangeBranchList().get(position).TrfHcode == 293)
            newTariff.setText("صنعتی");
        if(this.getChangeBranchList().get(position).TrfHcode == 294)
            newTariff.setText("سایر مصارف");

//        newTariff.setText( this.getChangeBranchList().get(position).newTariff );
 //       newFabrikNumber.setText(this.getChangeBranchList().get(position).fabrikNumber);
  //      newPower.setText(String.valueOf(this.getChangeBranchList().get(position).Pwrcnt));
//        newAmper.setText( this.getChangeBranchList().get(position).newAmper );

        if(this.getChangeBranchList().get(position).Phs == 250)
            phsName= "تک فاز";
        if(this.getChangeBranchList().get(position).Phs == 251)
            phsName = "سه فاز";
        if(this.getChangeBranchList().get(position).Amp == 261)
            ampValue = 5;
        if(this.getChangeBranchList().get(position).Amp == 262)
            ampValue = 10;
        if(this.getChangeBranchList().get(position).Amp == 263)
            ampValue = 15;
        if(this.getChangeBranchList().get(position).Amp == 264)
            ampValue = 25;
        if(this.getChangeBranchList().get(position).Amp == 265)
            ampValue = 32;
        newPhase.setText(ampValue + " / " +  this.getChangeBranchList().get(position).Pwrcnt +  " / " + phsName);
    //    newDocNumber.setText( this.getChangeBranchList().get(position).newSLRNumber );
        status.setText("موجود");
    if(this.getChangeBranchList().get(position).HaveChangePlace){
        moveCheck.setChecked(true);
    }else{
        moveCheck.setChecked(false);
    }
        changePainting(cartableChangeBranch, position, convertView);

        return convertView;
    }

    /**
     *     we paint cartable list background. odds are cream, evens are dark_gray
     */
    private void changePainting(CartableChangeBranch cartableChangeBranch, int position, View convertView) {
        if(position%2==0) {
            ColorDrawable cd = new ColorDrawable( cartableChangeBranch.getActivity().getResources().getColor(R.color.dark_gray) );
            convertView.setBackground(cd);
        }
        else if(position%2==1) {
            ColorDrawable cd = new ColorDrawable( cartableChangeBranch.getActivity().getResources().getColor(R.color.cream) );
            convertView.setBackground(cd);
        }
    }
}
