package com.andc.slidingmenu.Fragments.Cartable.DemandedBranch;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.andc.slidingmenu.Main.CartableItemActivity;
import com.andc.slidingmenu.R;

import java.util.List;

import slidingmenu.andc.com.dataaccess.ChangeBranchTbl;
import slidingmenu.andc.com.dataaccess.CollectBranchTbl;
import slidingmenu.andc.com.dataaccess.NewBranchTbl;


/**
 * Created by win on 4/26/2015.
 */
public class CartableDemandedBranch extends Fragment implements CartableItemActivity.Callbacks{

    private ViewGroup rootView;
    public FragmentTabHost host;
    private int width;
    private int height;
    public long requestNumber;

    @Override
    public boolean isValid(){
        return true;
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of demanded branch
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_visit_place_demanded_branch, container, false);

        definition(rootView);
        getScreenSize();
        tabHostPrepare(host);
        return rootView;
    }

    /**
     *
     * @param rootView
     * find any views on cartable view by rootView, get bundles from previous fragment and just define variables
     */
    private void definition(ViewGroup rootView) {
        host = (FragmentTabHost) rootView.findViewById(R.id.demanded_branch_tabs);

        Bundle info = getArguments();
        this.requestNumber = info.getLong("requestnumber");
    }

    /**
     * save devices screen size x,y
     */
    private void getScreenSize() {
        WindowManager wm = (WindowManager) getActivity().getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
    }

    /**
     *
     * @param host
     * host is used for hosting 3 tabs in this section
     * 1. CartableCollectorBranch class
     * 2. CartableChangeBranch class
     * 3. CartableNewBranch class
     * create a bundle named info, put it in every fragment tab
     */
    private void tabHostPrepare(FragmentTabHost host) {
        host.setup(this.getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        Bundle info = new Bundle();
        info.putLong("requestnumber",requestNumber);

        //Set Up Collect Branch Tab
        TabHost.TabSpec mAddBranch = host.newTabSpec(getResources().getString(R.string.cartable_tab_branch_collector));
        mAddBranch.setIndicator(initiateTabIndicator2(R.string.cartable_tab_branch_collector));
        //        initiateTabIndicator(R.string.cartable_tab_branch_collector,R.drawable.ic_demandbranch_collectbranch));
        host.addTab(mAddBranch, CartableCollectorBranch.class, info);

        //Set Up Edit Branch Tab
        TabHost.TabSpec mEditBranch = host.newTabSpec(getResources().getString(R.string.cartable_tab_inc_dec));
       mEditBranch.setIndicator(initiateTabIndicator2(R.string.cartable_tab_inc_dec));
            //    initiateTabIndicator(R.string.cartable_tab_inc_dec,R.drawable.ic_demandbranch_changebranch));
        host.addTab(mEditBranch, CartableChangeBranch.class, info);

        //Set Up New Branch Tab
        TabHost.TabSpec mRemoveBranch = host.newTabSpec(getResources().getString(R.string.cartable_tab_new_branch));
      mRemoveBranch.setIndicator(initiateTabIndicator2(R.string.cartable_tab_new_branch));
   //            initiateTabIndicator(R.string.cartable_tab_new_branch,R.drawable.ic_demandbranch_newbranch));
        host.addTab(mRemoveBranch, CartableNewBranch.class, info);

        host.setCurrentTab( host.getChildCount() + 1);

        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            final TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            if (tv != null)
                tv.setTextSize(this.getResources().getInteger(R.integer.demandedFragmentTabFontSize));
        }
    }
    public View initiateTabIndicator2(int tabTitle){
        View mTabIndicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_demand_branch, null);

        TextView mTabTitle = (TextView)mTabIndicator.findViewById(R.id.tab_demandbranch_title);
        mTabTitle.setText(getResources().getString(tabTitle));



        return mTabIndicator;
    }

    public View initiateTabIndicator(int tabTitle, int tabIcon){
        View mTabIndicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab_demand_branch, null);

        TextView mTabTitle = (TextView)mTabIndicator.findViewById(R.id.tab_demandbranch_title);
        mTabTitle.setText(getResources().getString(tabTitle));

       // ImageView mTabIcon = (ImageView)mTabIndicator.findViewById(R.id.tab_demandbranch_icon);
       // mTabIcon.setImageDrawable(getResources().getDrawable(tabIcon));

        return mTabIndicator;
    }

    public boolean validateDemandBranch(final CartableDemandedBranch cartableDemandedBranch){

        List<NewBranchTbl> newBranch = NewBranchTbl.find(NewBranchTbl.class, "Request_Code = ?", String.valueOf(requestNumber));
        List<ChangeBranchTbl> changeBranch = ChangeBranchTbl.find(ChangeBranchTbl.class, "Request_Code = ?", String.valueOf(requestNumber));
        List<CollectBranchTbl> collectBranch = CollectBranchTbl.find(CollectBranchTbl.class, "Request_Code = ?", String.valueOf(requestNumber));
        int size = newBranch.size() + changeBranch.size() + collectBranch.size();

        //*************************************

        if(size<0){
            Toast toast = Toast.makeText(cartableDemandedBranch.getActivity().getBaseContext()
                    , getResources().getString(R.string.cartable_branch_not_set), Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toast.show();
            return false;
        }

        //*************************************

        boolean mergeCollectNotSet=true;
        for(ChangeBranchTbl changeBrch:changeBranch){
            if(changeBrch.RequestActionType == 2){
                mergeCollectNotSet=false;
                for(CollectBranchTbl collectBrch:collectBranch){
                    if(collectBrch.RequestActionType == 1){
                        mergeCollectNotSet = true;
                        break;
                    }
                    else
                        mergeCollectNotSet = false;
                }
                if(mergeCollectNotSet==false) {
                    Toast toast = Toast.makeText(cartableDemandedBranch.getActivity().getBaseContext()
                            , getResources().getString(R.string.cartable_branch_merge_collect_not_set), Toast.LENGTH_SHORT);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(30);
                    toast.show();
                    break;
                }
            }
        }

        if(mergeCollectNotSet==false) {
            return false;
        }

        //*************************************

        boolean separationNewBranchNotSet=true;
        for(ChangeBranchTbl changeBrch:changeBranch){
            if(changeBrch.RequestActionType == 1){
                separationNewBranchNotSet=false;
                for(NewBranchTbl newBrch:newBranch){
                    if(newBrch.Request_Code == 13) {
                        separationNewBranchNotSet = true;
                        break;
                    }
                    else
                        separationNewBranchNotSet = false;
                }
                if(separationNewBranchNotSet==false) {
                    Toast toast = Toast.makeText(cartableDemandedBranch.getActivity().getBaseContext()
                            , getResources().getString(R.string.cartable_branch_sep_newbranch_not_set), Toast.LENGTH_SHORT);
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
                    toastTV.setTextSize(30);
                    toast.show();
                    break;
                }
            }
        }

        if(separationNewBranchNotSet==false)
            return false;

        //*************************************

        boolean duplicatedCollectorChange=false;
        for(ChangeBranchTbl changeBrch:changeBranch){
            for(CollectBranchTbl collectBrch:collectBranch){
                if(changeBrch.BranchCode == collectBrch.BranchCode) {
                    duplicatedCollectorChange = true;
                    break;
                }
            }
            if(duplicatedCollectorChange==true)
                break;
        }

        if(duplicatedCollectorChange==true) {
            Toast toast = Toast.makeText(cartableDemandedBranch.getActivity().getBaseContext()
                    , getResources().getString(R.string.cartable_branch_change_in_collect), Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toast.show();
            return false;
        }

        //*************************************

        boolean changeBranchset=false;
        for(CollectBranchTbl collectBrch:collectBranch){
            if(collectBrch.RequestActionType == 1) {
                for (ChangeBranchTbl changeBrch : changeBranch) {
                    if (changeBrch.RequestActionType == 2) {
                        changeBranchset = true;
                        break;
                    }
                }
                if (changeBranchset ==true)
                    break;
            }
        }

//                if(changeBranchset==false) {
//                    Toast toast = Toast.makeText(cartableDemandedBranch.getActivity().getBaseContext()
//                            , getResources().getString(R.string.cartable_branch_collect_not_in_change), Toast.LENGTH_SHORT);
//                    LinearLayout toastLayout = (LinearLayout) toast.getView();
//                    TextView toastTV = (TextView) toastLayout.getChildAt(0);
//                    toastTV.setTextSize(30);
//                    toast.show();
//                    return;
//                }

        //*************************************

        return true;
    }
}
