package com.andc.slidingmenu.Fragments.Cartable.DemandedBranch;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;





import com.andc.slidingmenu.Dialogs.NewBranch;
import com.andc.slidingmenu.R;
import com.andc.slidingmenu.adapter.CartableNewBranchAdapter;

import java.util.List;

import slidingmenu.andc.com.dataaccess.NewBranchTbl;

/**
 * Created by win on 4/11/2015.
 */
public class CartableNewBranch extends Fragment  {

    public int newNumber = 2; // create new branch
    public int editNumber = 3; // edit branch
    public int deleteNumber = 4;
    public int cancelNumber = 5;


    public ListView listView;
    public CartableNewBranchAdapter adapter;
    public long requestNumber;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of new branch
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_visit_place_new_branch, container, false);

        definition(rootView);
        return rootView;
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
        //setListClickListener();

        //Set Up Adapter
        adapter = new CartableNewBranchAdapter(this);
        Bundle info = getArguments();
        this.requestNumber = info.getLong("requestnumber");
        List<NewBranchTbl> newBranch = NewBranchTbl.find(NewBranchTbl.class, "RequestCode = ?", String.valueOf(this.requestNumber));
        for(NewBranchTbl branch:newBranch) {
         //   if(branch.isCancel.equalsIgnoreCase("false"))
                adapter.getNewBranchList().add(branch);
        }
        adapter.notifyDataSetChanged();

        //Set Footer Button to List View
        LinearLayout mAddButton = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.list_view_add_button, null, false);
        TextView mAddText = (TextView)mAddButton.findViewById(R.id.list_item_branch_add_button);
        mAddText.setText(R.string.list_item_branch_add_button);
        mAddButton.setEnabled(false);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                NewBranch newBranch = new NewBranch();
                newBranch.show(fm, "create new branch");
                newBranch.setTargetFragment(CartableNewBranch.this, newNumber);
            }
        });
        listView.addFooterView(mAddButton);

        //Set Adapter
        listView.setAdapter(adapter);
    }

    /**
     * user maybe wants to create a new branch or edit new branch added before
     * handling this by putting edit number and new number in setTargetFragment function
     * these two number will use in onActivityResult function later response
     */
  /*  private void setListClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HeaderViewListAdapter mHeaderAdapter = (HeaderViewListAdapter)listView.getAdapter();
                CartableNewBranchAdapter mNewAdapter = (CartableNewBranchAdapter)mHeaderAdapter.getWrappedAdapter();

                Bundle temp = new Bundle();
                temp.putLong("branchCode", mNewAdapter.getNewBranchList().get(position).Branch_Code);
                temp.putString("type", mNewAdapter.getItem(position).type);
                temp.putString("phase", mNewAdapter.getItem(position).phase);
                temp.putString("amper", mNewAdapter.getItem(position).amper);
                temp.putString("power", mNewAdapter.getItem(position).power);
                temp.putString("tariff", mNewAdapter.getItem(position).tariff);
                temp.putString("voltage", mNewAdapter.getItem(position).voltage);
                temp.putString("TrfType", mNewAdapter.getItem(position).TrfType);
                temp.putString("count", mNewAdapter.getItem(position).count);
                temp.putString("separation", mNewAdapter.getItem(position).separation);
                temp.putString("isFromServer", mNewAdapter.getItem(position).isFromServer);
                temp.putString("position", String.valueOf(position));

                FragmentManager fm = getFragmentManager();
                NewBranch newBranch = new NewBranch();
                newBranch.show(fm, "edit new branch");
                newBranch.setTargetFragment(CartableNewBranch.this, editNumber);
                newBranch.setArguments(temp);
            }
        });
    }
*/
    /**
     *
     * @param requestCode
     * @param resultCode
     * @param result
     * result contains information is inserted by user which should be saved in DB
     * if request code = 100, it means user wants to delete this item
     * if request code = new number, it means new material inserted and should be save in DB and add to list
     * if request code = edit number, it means old material changed and should be save in DB and replace in list so we need to know
     * the position of it
     */
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if( requestCode == deleteNumber ) {
            int position = Integer.valueOf( result.getStringExtra("position") );
            NewBranchTbl item = adapter.getNewBranchList().get(position);
            item.delete();
            adapter.getNewBranchList().remove(position);
            adapter.notifyDataSetChanged();
        }
        else if( requestCode == cancelNumber ) {
            int position = Integer.valueOf( result.getStringExtra("position") );
            NewBranchTbl item = adapter.getNewBranchList().get(position);
            item.isCancel = "true";
            item.save();
            adapter.getNewBranchList().remove(position);
            adapter.notifyDataSetChanged();
        }
        else if( requestCode == newNumber ) {
            NewBranchDB newBranchRecord = new NewBranchDB();
            newBranchRecord.requestNumber = this.requestNumber;
            newBranchRecord.branchCode = result.getStringExtra("branchCode");
            newBranchRecord.isFromServer = "false";
            newBranchRecord.type = result.getStringExtra("type");
            newBranchRecord.phase = result.getStringExtra("phase");
            newBranchRecord.amper = result.getStringExtra("amper");
            newBranchRecord.power = result.getStringExtra("power");
            newBranchRecord.tariff = result.getStringExtra("tariff");
            newBranchRecord.voltage = result.getStringExtra("voltage");
            newBranchRecord.TrfType = result.getStringExtra("TrfType");
            newBranchRecord.count = result.getStringExtra("count");
            newBranchRecord.separation = result.getStringExtra("separation");
            newBranchRecord.isCancel = "false";
            adapter.getNewBranchList().add(newBranchRecord);
            adapter.notifyDataSetChanged();
            newBranchRecord.save();

        }
        else if( requestCode == editNumber ) {
            int position = Integer.valueOf( result.getStringExtra("position") );
            NewBranchDB newBranchRecords = adapter.getNewBranchList().get(position);
            newBranchRecords.branchCode = result.getStringExtra("branchCode");
            newBranchRecords.requestNumber = this.requestNumber;
            newBranchRecords.isFromServer = result.getStringExtra("isFromServer");
            newBranchRecords.type = result.getStringExtra("type");
            newBranchRecords.phase = result.getStringExtra("phase");
            newBranchRecords.amper = result.getStringExtra("amper");
            newBranchRecords.power = result.getStringExtra("power");
            newBranchRecords.tariff = result.getStringExtra("tariff");
            newBranchRecords.voltage = result.getStringExtra("voltage");
            newBranchRecords.TrfType = result.getStringExtra("TrfType");
            newBranchRecords.count = result.getStringExtra("count");
            newBranchRecords.separation = result.getStringExtra("separation");
            newBranchRecords.isCancel = "false";
            newBranchRecords.save();
            adapter.notifyDataSetChanged();
        }
    }
    */
}
