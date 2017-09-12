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

import java.util.List;

import com.andc.slidingmenu.Dialogs.CollectBranch;
import com.andc.slidingmenu.R;

import com.andc.slidingmenu.adapter.CartableCollectorBranchAdapter;

import slidingmenu.andc.com.dataaccess.CollectBranchTbl;

/**
 * Created by win on 4/27/2015.
 */
public class CartableCollectorBranch extends Fragment {

    public int newNumber = 6; // new
    public int editNumber = 7; // edit
    public int cancelNumber = 9;
    public int deleteNumber = 8;

    public ListView listView;
    public CartableCollectorBranchAdapter adapter;
    public long requestNumber;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of collect branch
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_visit_place_collect_branch, container, false);

        definition(rootView);
        return rootView;
    }

    /**
     * user maybe wants to create a collect branch or edit collect branch added before
     * handling this by putting edit number and new number in setTargetFragment function
     * these two number will use in onActivityResult function later response
     */
    private void setListClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HeaderViewListAdapter mHeaderAdapter = (HeaderViewListAdapter)listView.getAdapter();
                CartableCollectorBranchAdapter mCollectAdapter = (CartableCollectorBranchAdapter)mHeaderAdapter.getWrappedAdapter();

                Bundle temp = new Bundle();
                temp.putString("position", String.valueOf(position) );
                temp.putString("fabrik", mCollectAdapter.getCollectorBranchList().get(position).FabrikNumber);
                temp.putString("document", String.valueOf(mCollectAdapter.getCollectorBranchList().get(position).BranchSrl));
                temp.putString("branchCode", String.valueOf(mCollectAdapter.getCollectorBranchList().get(position).BranchCode));
               // temp.putString("isFromServer", mCollectAdapter.getCollectorBranchList().get(position).isFromServer);
                temp.putString("type", String.valueOf(mCollectAdapter.getCollectorBranchList().get(position).BranchTypeCode));
                temp.putString("phase", String.valueOf(mCollectAdapter.getCollectorBranchList().get(position).Phs));
                temp.putString("amper", String.valueOf(mCollectAdapter.getCollectorBranchList().get(position).Amp));
                temp.putString("power", String.valueOf(mCollectAdapter.getCollectorBranchList().get(position).Pwrcnt));
                temp.putString("tariff", String.valueOf(mCollectAdapter.getCollectorBranchList().get(position).TrfHcode));
                temp.putString("voltage", String.valueOf(mCollectAdapter.getCollectorBranchList().get(position).VoltCode));
               // temp.putString("BranchStatusType", mCollectAdapter.getCollectorBranchList().get(position).BranchStatusType);
              //  temp.putString("merge", mCollectAdapter.getCollectorBranchList().get(position).isMerge);

                FragmentManager fm = getFragmentManager();
                CollectBranch collectBranch = new CollectBranch();
                collectBranch.show(fm, "edit new branch");
                collectBranch.setTargetFragment(CartableCollectorBranch.this, editNumber);
                collectBranch.setArguments(temp);
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
        adapter = new CartableCollectorBranchAdapter(this);
        Bundle info = getArguments();
        this.requestNumber = info.getLong("requestnumber");
        List<CollectBranchTbl> collectBranch = CollectBranchTbl.find(CollectBranchTbl.class, "Request_Code = ?", String.valueOf(this.requestNumber));

        for(CollectBranchTbl branch:collectBranch) {
         //   if(branch.isCancel.equalsIgnoreCase("false"))
                adapter.getCollectorBranchList().add(branch);
        }
        adapter.notifyDataSetChanged();

        //Set Footer Button to List View
        LinearLayout mAddButton = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.list_view_add_button, null, false);
        TextView mAddText = (TextView)mAddButton.findViewById(R.id.list_item_branch_add_button);
        mAddText.setText(R.string.list_item_branch_collect_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                CollectBranch collectBranch = new CollectBranch();
                collectBranch.show(fm, "create new branch");
                collectBranch.setTargetFragment(CartableCollectorBranch.this, newNumber);
            }
        });
        listView.addFooterView(mAddButton);

        listView.setAdapter(adapter);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param result
     * result contains information is inserted by user which should be saved in DB
     * if request code = delete number, it means user wants to delete this item
     * if request code = new number, it means new material inserted and should be save in DB and add to list
     * if request code = edit number, it means old material changed and should be save in DB and replace in list so we need to know
     * the position of it
     */
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if( requestCode == deleteNumber ) {
            int position = Integer.valueOf( result.getStringExtra("position") );
            CollectBranchTbl item = adapter.getCollectorBranchList().get(position);
            item.delete();
            adapter.getCollectorBranchList().remove(position);
            adapter.notifyDataSetChanged();
        }
        else if( requestCode == cancelNumber ) {
            int position = Integer.valueOf( result.getStringExtra("position") );
            CollectBranchTbl item = adapter.getCollectorBranchList().get(position);
           // item.isCancel = "true";
            item.save();
            adapter.getCollectorBranchList().remove(position);
            adapter.notifyDataSetChanged();
        }
        else if( requestCode == newNumber ) {
            CollectBranchTbl collectBranchDB = new CollectBranchTbl();
            collectBranchDB.RequestCode = this.requestNumber;
            collectBranchDB.FabrikNumber = result.getStringExtra("fabrik");
            collectBranchDB.BranchSrl = Long.parseLong(result.getStringExtra("document"));
            collectBranchDB.BranchCode = Long.parseLong(result.getStringExtra("branchCode"));
            collectBranchDB.BranchTypeCode = Integer.parseInt(result.getStringExtra("type"));
            collectBranchDB.Phs = Integer.parseInt(result.getStringExtra("phase"));
            collectBranchDB.Amp = Integer.parseInt(result.getStringExtra("amper"));
            collectBranchDB.Pwrcnt= Integer.parseInt(result.getStringExtra("power"));
            collectBranchDB.TrfHcode = Integer.parseInt(result.getStringExtra("tariff"));
            collectBranchDB.VoltCode = Integer.parseInt(result.getStringExtra("voltage"));
          //  collectBranchDB.BranchStatusType = result.getStringExtra("BranchStatusType");
          //  collectBranchDB.isMerge = result.getStringExtra("merge");
           // collectBranchDB.isFromServer = "false";
          //  collectBranchDB.isCancel = "false";
            adapter.getCollectorBranchList().add(collectBranchDB);
            adapter.notifyDataSetChanged();
            collectBranchDB.save();
        }

        else if( requestCode == editNumber ) {
            int position = Integer.valueOf( result.getStringExtra("position") );
            CollectBranchTbl collectBranch = adapter.getCollectorBranchList().get(position);
            collectBranch.RequestCode = this.requestNumber;
            collectBranch.FabrikNumber = result.getStringExtra("fabrik");
            collectBranch.BranchCode= Long.parseLong(result.getStringExtra("branchCode"));
            collectBranch.BranchSrl= Long.parseLong(result.getStringExtra("document"));
            collectBranch.BranchTypeCode = Integer.parseInt(result.getStringExtra("type"));
            collectBranch.Phs = Integer.parseInt(result.getStringExtra("phase"));
            collectBranch.Amp = Integer.parseInt(result.getStringExtra("amper"));
            collectBranch.Pwrcnt = Integer.parseInt(result.getStringExtra("power"));
            collectBranch.TrfHcode= Integer.parseInt(result.getStringExtra("tariff"));
            collectBranch.VoltCode = Integer.parseInt(result.getStringExtra("voltage"));
          //  collectBranch.BranchStatusType = result.getStringExtra("BranchStatusType");
          //  collectBranch.isMerge = result.getStringExtra("merge");
          //  collectBranch.isFromServer = result.getStringExtra("isFromServer");
          //  collectBranch.isCancel = "false";
            collectBranch.save();
            adapter.notifyDataSetChanged();
        }

    }

}