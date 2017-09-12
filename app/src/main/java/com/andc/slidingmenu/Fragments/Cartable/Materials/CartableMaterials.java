package com.andc.slidingmenu.Fragments.Cartable.Materials;

/**
 * Created by SiaJam on 1/31/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.andc.slidingmenu.Main.CartableItemActivity;
import com.andc.slidingmenu.R;

import java.util.ArrayList;

import slidingmenu.andc.com.dataaccess.CartableTbl;

public class CartableMaterials extends Fragment implements CartableItemActivity.Callbacks{


    public ListView requirmentListView;
    //   public ImageView add;
//   public Spinner chooseItems;
    public Button newTools;

    public int newNumber = 7;
    public int editNumber = 8;
    public int deleteNumber = 9;
    public long requestNumber;
    public CartableTbl thisItem;
    public FragmentTabHost tabHost;

    @Override
    public boolean isValid(){
        return true;
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * prepare layout of materials
     * @return
     */
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = (View) inflater.inflate(R.layout.fragment_visit_place_tab_material, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        definition(rootView);
        //setAdapterAdapter();
        tabHostPrepare(tabHost);
        return rootView;
    }

    /**
     *
     * @param rootView
     * find any views on cartable view by rootView, get bundles from previous fragment and just define variables
     */
    private void definition(View rootView) {
        //   this.add = (ImageView) rootView.findViewById(R.id.cartable_measurment_add);
        //   this.chooseItems = (Spinner) rootView.findViewById(R.id.cartable_measurment_template);
        tabHost = (FragmentTabHost) rootView.findViewById(R.id.material_tabs);
     //   requirmentListView = (ListView) rootView.findViewById(R.id.requirement_tools_list);
     //   requirmentListView.setClickable(true);
      //  setListClickHandler();
        Bundle info = getArguments();
        this.requestNumber = info.getLong("requestnumber");


    }

    /**
     *
     * adding tab for install matrial and collect material
     */
    private void tabHostPrepare(FragmentTabHost host) {
        host.setup(this.getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        Bundle info = new Bundle();
        info.putLong("requestnumber",requestNumber);


        //Set Up Edit Branch Tab
        TabHost.TabSpec mCollect = host.newTabSpec("کالاهای جمع آوری");
        mCollect.setIndicator("کالاهای جمع آوری");
        host.addTab(mCollect, CartableCollectMaterial.class, info);

        //Set Up Collect Branch Tab
        TabHost.TabSpec mInstall = host.newTabSpec("کالاهای نصب");
        mInstall.setIndicator("کالاهای نصب");
        host.addTab(mInstall, CartableInstallMaterial.class, info);


        host.setCurrentTab( host.getChildCount() );

        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            final TextView tv = (TextView) host.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            if (tv != null)
                tv.setTextSize(this.getResources().getInteger(R.integer.demandedFragmentTabFontSize));
        }
    }

    /**
     * list for material template spinner is loading here
     */
 /*   private void setAdapterAdapter() {
        MaterialTemplateDB mat = new MaterialTemplateDB(this.getActivity());
        ArrayList<String> templateArray = new ArrayList<String>();
        for(int i=1; i<=mat.getSize(); i++)
            templateArray.add(mat.getItem(i));

        ArrayAdapter<String> mattAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, templateArray);
        mattAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //chooseItems.setAdapter(mattAdapter);
    }
*/




}
