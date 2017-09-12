package com.andc.slidingmenu.Fragments.Cartable.DemandedBranch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.andc.slidingmenu.Dialogs.ChangeBranch;
import com.andc.slidingmenu.R;
import com.andc.slidingmenu.adapter.CartableChangeBranchAdapter;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.ChangeBranchTbl;

/**
 * Created by win on 4/26/2015.
 */
public class CartableChangeBranch extends Fragment {

    public int newNumber = 4; // new
    public int editNumber = 5; // edit
    public int deleteNumber = 6;
    public int cancelNumber = 7;

    public ListView listView;
    public CartableChangeBranchAdapter adapter;
    public long requestNumber;
    int phaseVal, amperVal, voltageVal, tariffVal;
    int newphaseVal, newamperVal, newvoltageVal, newtarifVal;
    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of change branch
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_visit_place_change_branch, container, false);

        definition(rootView);
        return rootView;
    }

    /**
     * user maybe wants to create a change branch or edit change branch added before
     * handling this by putting edit number and new number in setTargetFragment function
     * these two number will use in onActivityResult function later response
     * in temp bundle we need to put old value and new value
     */
    private void setListClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                HeaderViewListAdapter mHeaderAdapter = (HeaderViewListAdapter)listView.getAdapter();
                CartableChangeBranchAdapter mChangeAdapter = (CartableChangeBranchAdapter)mHeaderAdapter.getWrappedAdapter();
                Bundle temp = new Bundle();
                temp.putString("fabrik", mChangeAdapter.getChangeBranchList().get(position).FabrikNumber);
                temp.putString("branchCode", String.valueOf(mChangeAdapter.getChangeBranchList().get(position).BranchCode));
               // temp.putString("isFromServer", mChangeAdapter.getChangeBranchList().get(position).);
                temp.putString("newdocument", String.valueOf(mChangeAdapter.getChangeBranchList().get(position).BranchSrl));
                temp.putInt("type", mChangeAdapter.getChangeBranchList().get(position).OldBranchTypeCode);
                temp.putInt("phase", mChangeAdapter.getChangeBranchList().get(position).OldPhs);
                temp.putInt("amper", mChangeAdapter.getChangeBranchList().get(position).OldAmp);
                temp.putInt("power", mChangeAdapter.getChangeBranchList().get(position).OldPwrcnt);
                temp.putInt("tariff", mChangeAdapter.getChangeBranchList().get(position).OldtrfHcode);
                temp.putInt("voltage", mChangeAdapter.getChangeBranchList().get(position).OldVoltcode);
                temp.putString("status", String.valueOf(mChangeAdapter.getChangeBranchList().get(position).ActionType));
                temp.putInt("trftype", mChangeAdapter.getChangeBranchList().get(position).OldTrfType);

                temp.putInt("newtype", mChangeAdapter.getChangeBranchList().get(position).ActionType);
                temp.putInt("newphase", mChangeAdapter.getChangeBranchList().get(position).Phs);
                temp.putInt("newamper", mChangeAdapter.getChangeBranchList().get(position).Amp);
                temp.putInt("newpower", mChangeAdapter.getChangeBranchList().get(position).Pwrcnt);
                temp.putInt("newtariff", mChangeAdapter.getChangeBranchList().get(position).TrfHcode);
                temp.putInt("newvoltage", mChangeAdapter.getChangeBranchList().get(position).VoltCode);
                temp.putInt("newstatus", mChangeAdapter.getChangeBranchList().get(position).BranchTypeCode);
                temp.putInt("requestaction", mChangeAdapter.getChangeBranchList().get(position).RequestActionType);
                temp.putString("moveneeded", String.valueOf(mChangeAdapter.getChangeBranchList().get(position).HaveChangePlace));
                temp.putString("changeneeded", String.valueOf(mChangeAdapter.getChangeBranchList().get(position).HaveChangeMeter));
                temp.putString("position", String.valueOf( position ));

              //  temp.putString("increase", mChangeAdapter.getChangeBranchList().get(position).increase);
             //   temp.putString("decrease", mChangeAdapter.getChangeBranchList().get(position).decrease);
               // temp.putString("merge", mChangeAdapter.getChangeBranchList().get(position).merge);
              //  temp.putString("separation", mChangeAdapter.getChangeBranchList().get(position).separation);

                FragmentManager fm = getFragmentManager();
                ChangeBranch changeBranch = new ChangeBranch();
                changeBranch.show(fm, "edit new branch");
                changeBranch.setTargetFragment(CartableChangeBranch.this, editNumber);
                changeBranch.setArguments(temp);
            }
        });
    }

    /**
     * @param rootView
     * find any views on cartable view by rootView, get bundles from previous fragment and just define variables
     * with request number we can search in DB and get related list items
     */
    private void definition(View rootView) {
        //Set Up ListView
        listView = (ListView) rootView.findViewById(R.id.visit_place_list);
        listView.setClickable(true);
        setListClickListener();

        //Set Up Adapter
        adapter = new CartableChangeBranchAdapter(this);
        Bundle info = getArguments();
        this.requestNumber = info.getLong("requestnumber");
        List<ChangeBranchTbl> changeBranch = ChangeBranchTbl.find(ChangeBranchTbl.class, "Request_Code = ?", String.valueOf(this.requestNumber));
        for(ChangeBranchTbl branch:changeBranch) {
          //  if(branch.isCancel.equalsIgnoreCase("false"))
                adapter.getChangeBranchList().add(branch);
        }
        adapter.notifyDataSetChanged();

        //Set Footer Button to List View
        LinearLayout mAddButton = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.list_view_add_button, null, false);
        TextView mAddText = (TextView)mAddButton.findViewById(R.id.list_item_branch_add_button);
        mAddText.setText(R.string.list_item_branch_edit_button);
        mAddButton.setEnabled(false);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();

                ChangeBranch changeBranch = new ChangeBranch();
                changeBranch.show(fm, "create new branch");
                changeBranch.setTargetFragment(CartableChangeBranch.this, newNumber);

            }
        });
        listView.addFooterView(mAddButton);

        listView.setAdapter(adapter);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param result
     * result contains information is inserted by user which should be saved in DB
     * if request code = delete number, it means user wants to delete this item
     * if request code = new number, it means new material inserted and should be save in DB and add to list
     * if request code = edit number, it means old material changed and should be save in DB and replace in list so we need to know
     * the position of it
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {



        if( requestCode == deleteNumber ) {
            int position = Integer.valueOf( result.getStringExtra("position") );
            ChangeBranchTbl item = adapter.getChangeBranchList().get(position);
            item.delete();
            adapter.getChangeBranchList().remove(position);
            adapter.notifyDataSetChanged();
        }
        else if( requestCode == cancelNumber ) {
            int position = Integer.valueOf( result.getStringExtra("position") );
            ChangeBranchTbl item = adapter.getChangeBranchList().get(position);
            //item.isCancel = "true";
            item.save();
            adapter.getChangeBranchList().remove(position);
            adapter.notifyDataSetChanged();
        }
        else if( requestCode == newNumber ) {
            amperVal = convertBaseToCode(10,result.getStringExtra("amper"));
            phaseVal = convertBaseToCode(9,result.getStringExtra("phase"));
            tariffVal = convertBaseToCode(11,result.getStringExtra("tariff"));
            voltageVal = convertBaseToCode(13,result.getStringExtra("voltage"));
            newtarifVal = convertBaseToCode(11,result.getStringExtra("newtariff"));
            newvoltageVal = convertBaseToCode(13,result.getStringExtra("newvoltage"));
            ChangeBranchTbl changeBranchDB = new ChangeBranchTbl();
            changeBranchDB.RequestCode = this.requestNumber;
            changeBranchDB.ActionType = 4;
            changeBranchDB.FabrikNumber= result.getStringExtra("fabrik");
            changeBranchDB.BranchCode = Long.parseLong(result.getStringExtra("branchCode"));
           // changeBranchDB.address = result.getStringExtra("Address");
           // changeBranchDB.fullName = result.getStringExtra("FulName");

            changeBranchDB.BranchSrl = Long.parseLong(result.getStringExtra("document"));
          //  changeBranchDB.isFromServer = "false";
            changeBranchDB.OldBranchTypeCode = Integer.parseInt(result.getStringExtra("type"));
            changeBranchDB.OldPhs =phaseVal ;
            changeBranchDB.OldAmp = amperVal ;
            changeBranchDB.OldPwrcnt = Integer.parseInt(result.getStringExtra("power"));
            changeBranchDB.OldtrfHcode = tariffVal;
            changeBranchDB.OldVoltcode = voltageVal;

         //   changeBranchDB.newSLRNumber = result.getStringExtra("newdocument");
            changeBranchDB.BranchTypeCode = Integer.parseInt(result.getStringExtra("newtype"));
            changeBranchDB.Phs = newphaseVal;
            changeBranchDB.Amp = newamperVal;
            changeBranchDB.Pwrcnt= Integer.parseInt(result.getStringExtra("newpower"));
            changeBranchDB.TrfHcode = newtarifVal;
            changeBranchDB.VoltCode = newvoltageVal;
            changeBranchDB.ActionType = Integer.parseInt(result.getStringExtra("newstatus"));
            changeBranchDB.HaveChangeMeter = Boolean.parseBoolean(result.getStringExtra("changeneeded"));
            changeBranchDB.HaveChangePlace = Boolean.parseBoolean(result.getStringExtra("moveneeded"));

           // changeBranchDB.increase = result.getStringExtra("increase");
           // changeBranchDB.decrease = result.getStringExtra("decrease");
         //   changeBranchDB.merge = result.getStringExtra("merge");
          //  changeBranchDB.separation = result.getStringExtra("separation");
          //  changeBranchDB.isCancel = "false";

            adapter.getChangeBranchList().add(changeBranchDB);
            adapter.notifyDataSetChanged();
            changeBranchDB.save();
        }

        else if( requestCode == editNumber ) {
            amperVal = convertBaseToCode(10,result.getStringExtra("amper"));
            phaseVal = convertBaseToCode(9,result.getStringExtra("phase"));
            tariffVal = convertBaseToCode(11,result.getStringExtra("tariff"));
            voltageVal = convertBaseToCode(13,result.getStringExtra("voltage"));
            newtarifVal = convertBaseToCode(11,result.getStringExtra("newtariff"));
            newvoltageVal = convertBaseToCode(13,result.getStringExtra("newvoltage"));
            int position = Integer.valueOf( result.getStringExtra("position") );
            ChangeBranchTbl changeBranchDB = adapter.getChangeBranchList().get(position);
            changeBranchDB.RequestCode = this.requestNumber;
            changeBranchDB.FabrikNumber = result.getStringExtra("fabrik");
            changeBranchDB.BranchCode= Long.parseLong(result.getStringExtra("branchCode"));
          //  changeBranchDB.isFromServer = result.getStringExtra("isFromServer");
            changeBranchDB.BranchSrl = Long.parseLong(result.getStringExtra("newdocument"));
         //   if(!result.getStringExtra("type").isEmpty())
       //     changeBranchDB.OldBranchTypeCode = Integer.parseInt(result.getStringExtra("type"));

            changeBranchDB.OldPhs = phaseVal;
            changeBranchDB.OldAmp= amperVal;
            if(result.getStringExtra("power")!= null)
            changeBranchDB.OldPwrcnt = Integer.parseInt(result.getStringExtra("power"));
            changeBranchDB.OldtrfHcode = tariffVal;
            changeBranchDB.OldVoltcode = voltageVal;
            changeBranchDB.ActionType = Integer.parseInt(result.getStringExtra("status"));
            //changeBranchDB.address = result.getStringExtra("Address");
           // changeBranchDB.fullName = result.getStringExtra("FulName");

           // changeBranchDB.newSLRNumber = result.getStringExtra("newdocument");
            if(!result.getStringExtra("newtype").isEmpty())
            changeBranchDB.BranchTypeCode = Integer.parseInt(result.getStringExtra("newtype"));

            changeBranchDB.Phs = Integer.parseInt(result.getStringExtra("newphase"));

            changeBranchDB.Amp = Integer.parseInt(result.getStringExtra("newamper"));
           // if(!result.getStringExtra("newpower").isEmpty())
          //  changeBranchDB.Pwrcnt = Integer.parseInt(result.getStringExtra("newpower"));
            changeBranchDB.HaveChangeMeter = Boolean.parseBoolean(result.getStringExtra("changeneeded"));
            changeBranchDB.HaveChangePlace = Boolean.parseBoolean(result.getStringExtra("moveneeded"));

            changeBranchDB.TrfHcode = newtarifVal;

            changeBranchDB.VoltCode = newvoltageVal;


           // changeBranchDB.increase = result.getStringExtra("increase");
           // changeBranchDB.decrease = result.getStringExtra("decrease");
           // changeBranchDB.merge = result.getStringExtra("merge");
           // changeBranchDB.separation = result.getStringExtra("separation");
           // changeBranchDB.isCancel = "false";

            changeBranchDB.save();
            adapter.notifyDataSetChanged();
        }

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



