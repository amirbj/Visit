package com.andc.slidingmenu.Fragments.Cartable.Materials;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.andc.slidingmenu.Dialogs.CollectMaterial;
import com.andc.slidingmenu.R;

import com.andc.slidingmenu.adapter.CartableCollectToolsAdapter;
import com.andc.slidingmenu.adapter.CartableOtherMaterialsAdapter;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Handler;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.CartableTbl;
import slidingmenu.andc.com.dataaccess.ChangeBranchTbl;
import slidingmenu.andc.com.dataaccess.CollectBranchTbl;
import slidingmenu.andc.com.dataaccess.CollectMeterTbl;
import slidingmenu.andc.com.dataaccess.CollectionTbl;
import slidingmenu.andc.com.dataaccess.InstalationTbl;


/**
 * Created by SiaJam on 1/31/2017.
 */

public class CartableCollectMaterial extends Fragment implements AdapterView.OnItemClickListener {

    public ListView  collectListView;
    public CartableCollectToolsAdapter adapter;
    public CartableOtherMaterialsAdapter otherAdapter;
    public int newNumber = 7;
    public int editNumber = 8;
    public int deleteNumber = 9;
    public long requestNumber;
    public CartableTbl thisItem;
    public Spinner cableType, vinchType, selfClampType, branchBoxType, takhtekontorType, branchHandleType, phiozType;
    public EditText noCableEt, noVinchEt, noselfClampEt, noBranchBoxEt, noTakhteContorEt, noBranchHandleEt, noPhiozEt;
    public EditText cableEt, vinchEt, selfEt, branchboxEt, takhteEt, branchhandleEt, phiozEt;
    private int width;
    private int height;
    public ListView collecttools;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_visit_place_collect_tools, container, false);
         definition(rootview);
        setUpSpinner();
      //  setListClickHandler();
        getScreenSize();
        loadData();
        return rootview;
    }
    private void definition(View rootview) {

     //   CollectMeterTbl.deleteAll(CollectMeterTbl.class, "RequestCode = ?", String.valueOf(this.requestNumber) );
        insertfromChangetbl();
        insertfromCollectbranch();
        collectListView = (ListView) rootview.findViewById(R.id.collect_tools_list);
        collecttools = (ListView) rootview.findViewById(R.id.collect_material_tools_list);
        collectListView.setOnItemClickListener(this);


      //  setListClickHandler();
        //Set Up Adapter
        adapter = new CartableCollectToolsAdapter(CartableCollectMaterial.this);
        otherAdapter = new CartableOtherMaterialsAdapter(CartableCollectMaterial.this);


        Bundle info = getArguments();
        this.requestNumber = info.getLong("requestnumber");

        List<CartableTbl> cartable = CartableTbl.find(CartableTbl.class, "Request_Code = ?", String.valueOf(this.requestNumber));
        this.thisItem = cartable.get(0);


                List < CollectionTbl > measurmentMaterial = CollectionTbl.find(CollectionTbl.class, "Request_Code = ? and Othermaterials = ?", String.valueOf(this.requestNumber), "1");
                for(CollectionTbl collection:measurmentMaterial)
                    otherAdapter.getMaterialList().add(collection);


        List<CollectMeterTbl> meterList = CollectMeterTbl.find(CollectMeterTbl.class, "RequestCode = ?", String.valueOf(this.requestNumber));

        for(CollectMeterTbl material:meterList)
            adapter.getMaterialList().add(material);


        //Set Header Button to List View
       // LinearLayout mAddButton = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.list_view_add_button, null, false);

        LinearLayout headerView = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.list_view_collect_tools_footer, null, false);

        ImageButton imageButton = (ImageButton) rootview.findViewById(R.id.collect_add_button);
       // mAddButton.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
  //      TextView mAddText = (TextView)mAddButton.findViewById(R.id.list_item_branch_add_button);
     //   mAddText.setText(R.string.cartable_measurment_create);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                CollectMaterial newMaterial = new CollectMaterial();
                newMaterial.show(fm, "collectMaterial");
                newMaterial.setTargetFragment(CartableCollectMaterial.this, 11);
            }
        });

     //   collectListView.addFooterView(headerView);
      //  collectListView.addFooterView(footerView);

        collecttools.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        collectListView.setAdapter(otherAdapter);
        otherAdapter.notifyDataSetChanged();

        /*
        edit text for description of collection
         */
      cableEt = (EditText) rootview.findViewById(R.id.cartable_material_desc_kabl);
        vinchEt = (EditText) rootview.findViewById(R.id.cartable_material_desc_vinch);
        selfEt = (EditText) rootview.findViewById(R.id.cartable_material_desc_clamp_self);
        branchboxEt = (EditText) rootview.findViewById(R.id.cartable_material_desc_branch_box);
        takhteEt = (EditText) rootview.findViewById(R.id.cartable_material_desc_takhte_kontor);
        branchhandleEt = (EditText) rootview.findViewById(R.id.cartable_material_desc_branch_handle);
        phiozEt = (EditText) rootview.findViewById(R.id.cartable_material_desc_fioz);

        /*
        Edit text for number of collection

         */

        noCableEt = (EditText) rootview.findViewById(R.id.numberofkabl);
        noVinchEt = (EditText) rootview.findViewById(R.id.numberofvinch);
        noselfClampEt = (EditText) rootview.findViewById(R.id.numberofclampself);
        noBranchBoxEt = (EditText) rootview.findViewById(R.id.numberof_brancgbox);
        noTakhteContorEt = (EditText) rootview.findViewById(R.id.numberof_takhte_kontor);
        noBranchHandleEt = (EditText) rootview.findViewById(R.id.numberof_branch_handle);
        noPhiozEt = (EditText) rootview.findViewById(R.id.numberof_fioz);

        /*
        spinners for collection type
         */

        cableType = (Spinner) rootview.findViewById(R.id.cartable_material_kabl);
        vinchType = (Spinner) rootview.findViewById(R.id.cartable_material_vinch);
        selfClampType = (Spinner) rootview.findViewById(R.id.cartable_material_clamp_self);
        branchBoxType = (Spinner) rootview.findViewById(R.id.cartable_material_branch_box);
        takhtekontorType = (Spinner) rootview.findViewById(R.id.cartable_material_takhte_kontor);
        branchHandleType = (Spinner) rootview.findViewById(R.id.cartable_material_brnach_handle);
        phiozType = (Spinner) rootview.findViewById(R.id.cartable_material_fioz);

        ImageButton done = (ImageButton) rootview.findViewById(R.id.material_collect_add);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionTbl.deleteAll(CollectionTbl.class, "Request_Code = ? and Othermaterials = ?", String.valueOf(requestNumber), "0");
                save();
                Toast.makeText(getActivity(), "تغییرات با موفقیت ذخیره گردید", Toast.LENGTH_SHORT).show();


            }

        });
    }


    public void setUpSpinner(){
       List<BaseMaterialTbl> kableList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "16");
        List<String> cables = new ArrayList();
        for(BaseMaterialTbl base: kableList){
            cables.add(base.Description);
        }
        ArrayAdapter kableAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, cables);
        cableType.setAdapter(kableAdapter);

        List<BaseMaterialTbl> VinchList = BaseMaterialTbl.find(BaseMaterialTbl.class, "TabletMain_Code = ?","17");
        List<String> vinchs = new ArrayList<>();
        for(BaseMaterialTbl base: VinchList){
            vinchs.add(base.Description);
        }
        ArrayAdapter VinchAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, vinchs);
        vinchType.setAdapter(VinchAdapter);

        List<BaseMaterialTbl> clampselfList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "18");
        List<String> clampselfs = new ArrayList();
        for(BaseMaterialTbl base: clampselfList){
            clampselfs.add(base.Description);
        }
        ArrayAdapter clampSelfAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, clampselfs);
        selfClampType.setAdapter(clampSelfAdapter);


        List<BaseMaterialTbl> branhboxList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "19");
        List<String> branchbox = new ArrayList();
        for(BaseMaterialTbl base: branhboxList){
            branchbox.add(base.Description);
        }
        ArrayAdapter branchboxAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, branchbox);
        branchBoxType.setAdapter(branchboxAdapter);


        List<BaseMaterialTbl> takhtekontorList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "20");
        List<String> takhtekontor = new ArrayList();
        for(BaseMaterialTbl base: takhtekontorList){
            takhtekontor.add(base.Description);
        }

        ArrayAdapter takhtekontorAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item , takhtekontor);
        takhtekontorType.setAdapter(takhtekontorAdapter);


        List<BaseMaterialTbl> branchhandleList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "21");
        List<String> branchhandle = new ArrayList();
        for(BaseMaterialTbl base: branchhandleList){
            branchhandle.add(base.Description);
        }

        ArrayAdapter branchhandleAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, branchhandle);
        branchHandleType.setAdapter(branchhandleAdapter);


        List<BaseMaterialTbl> phiozList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "18");
        List<String> phiozes = new ArrayList();
        for(BaseMaterialTbl base: phiozList){
            phiozes.add(base.Description);
        }

        ArrayAdapter phiozAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, phiozes);
        phiozType.setAdapter(phiozAdapter);
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
    public void  insertfromCollectbranch(){

        List<CollectBranchTbl> tavizilist = CollectBranchTbl.find(CollectBranchTbl.class , "Request_Code = ?", String.valueOf(requestNumber));
        for(CollectBranchTbl collectBranchTbl: tavizilist) {
            CollectMeterTbl collectMeterTbl = new CollectMeterTbl();
            collectMeterTbl.Request_Code = collectBranchTbl.RequestCode;
            collectMeterTbl.FabrikNumber = collectBranchTbl.FabrikNumber;
            collectMeterTbl.BranchCode = collectBranchTbl.BranchCode;
            collectMeterTbl.MainObjectcode = 15;
            collectMeterTbl.BranchSrl = collectBranchTbl.BranchSrl;

            if (collectBranchTbl.FabrikNumber != null) {
                List<CollectMeterTbl> listmeter = CollectMeterTbl.find(CollectMeterTbl.class, "RequestCode = ? and Fabrik_Number = ?", String.valueOf(this.requestNumber), collectBranchTbl.FabrikNumber);
                boolean isThere = false;
                for (CollectMeterTbl collect : listmeter) {
                    if (collect.FabrikNumber != null)
                        isThere = true;
                }
                if (!isThere)
                    collectMeterTbl.save();


            }
        }

    }

    public void insertfromChangetbl(){
        List<ChangeBranchTbl> tavizMaterialList = ChangeBranchTbl.find(ChangeBranchTbl.class, "Request_Code = ? and Have_Change_Meter = ?", String.valueOf(requestNumber), "1");
        for(ChangeBranchTbl tavizi: tavizMaterialList){
            CollectMeterTbl collectMeterTbl = new CollectMeterTbl();
            collectMeterTbl.Request_Code = tavizi.RequestCode;
            collectMeterTbl.FabrikNumber = tavizi.FabrikNumber;
            collectMeterTbl.MainObjectcode = 15;
            collectMeterTbl.BranchCode = tavizi.BranchCode;
            collectMeterTbl.BranchSrl = tavizi.BranchSrl;

            List<CollectMeterTbl> listmeter = CollectMeterTbl.find(CollectMeterTbl.class, "RequestCode = ? and Fabrik_Number = ?", String.valueOf(this.requestNumber), tavizi.FabrikNumber);
            boolean isThere = false;
            for(CollectMeterTbl collect : listmeter){
                if(collect.FabrikNumber != null)
                    isThere = true;
            }
            if(!isThere)
            collectMeterTbl.save();
        }
    }
    public String convertBaseToName(int tabletcode, int value ){

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and TabletCode = ?", String.valueOf(tabletcode), String.valueOf(value));


        BaseMaterialTbl materialbase =  base.get(0);

        return materialbase.Description;

    }

    public int getAdapterPosion(Spinner s, int code, int subcode){
        ArrayAdapter myapp = (ArrayAdapter) s.getAdapter();
        int position =0;
        if(subcode != 0) {
            position = myapp.getPosition(convertBaseToName(code, subcode));
        }
        return position;

    }
    private void loadData(){
        List<CollectionTbl> collectionTblList = CollectionTbl.find(CollectionTbl.class, "Request_Code = ?", String.valueOf(requestNumber));

      for(CollectionTbl collectionTbl: collectionTblList) {

          if (collectionTbl.MainObjectcode == 16 && collectionTbl.Quantity != 0 && collectionTbl.Othermaterials == 0) {
              noCableEt.setText(String.valueOf(collectionTbl.Quantity));
              cableEt.setText(collectionTbl.Desc);
              cableType.setSelection(getAdapterPosion(cableType, collectionTbl.MainObjectcode, collectionTbl.SubObjectcode));
          }
          if (collectionTbl.MainObjectcode == 17 && collectionTbl.Quantity != 0 && collectionTbl.Othermaterials == 0) {
              noVinchEt.setText(String.valueOf(collectionTbl.Quantity));
              vinchEt.setText(collectionTbl.Desc);
              vinchType.setSelection(getAdapterPosion(vinchType, collectionTbl.MainObjectcode, collectionTbl.SubObjectcode));
          }
          if (collectionTbl.MainObjectcode == 18 && collectionTbl.Quantity != 0 && collectionTbl.Othermaterials == 0) {
              noselfClampEt.setText(String.valueOf(collectionTbl.Quantity));
              selfEt.setText(collectionTbl.Desc);
              selfClampType.setSelection(getAdapterPosion(selfClampType, collectionTbl.MainObjectcode, collectionTbl.SubObjectcode));
          }
          if (collectionTbl.MainObjectcode == 19 && collectionTbl.Quantity != 0 && collectionTbl.Othermaterials == 0) {
              noBranchBoxEt.setText(String.valueOf(collectionTbl.Quantity));
              branchboxEt.setText(collectionTbl.Desc);
              branchBoxType.setSelection(getAdapterPosion(branchBoxType, collectionTbl.MainObjectcode, collectionTbl.SubObjectcode));

          }
          if (collectionTbl.MainObjectcode == 20 && collectionTbl.Quantity != 0 && collectionTbl.Othermaterials == 0) {
              noTakhteContorEt.setText(String.valueOf(collectionTbl.Quantity));
              takhteEt.setText(collectionTbl.Desc);
             takhtekontorType.setSelection(getAdapterPosion(takhtekontorType, collectionTbl.MainObjectcode, collectionTbl.SubObjectcode));

          }
          if (collectionTbl.MainObjectcode == 21 && collectionTbl.Quantity != 0 && collectionTbl.Othermaterials == 0) {
              noBranchHandleEt.setText(String.valueOf(collectionTbl.Quantity));
              branchhandleEt.setText(collectionTbl.Desc);
              branchHandleType.setSelection(getAdapterPosion(branchHandleType, collectionTbl.MainObjectcode, collectionTbl.SubObjectcode));

          }
          if (collectionTbl.MainObjectcode == 22 && collectionTbl.Quantity != 0 && collectionTbl.Othermaterials == 0) {
              noPhiozEt.setText(String.valueOf(collectionTbl.Quantity));
              phiozEt.setText(collectionTbl.Desc);
              phiozType.setSelection(getAdapterPosion(phiozType, collectionTbl.MainObjectcode, collectionTbl.SubObjectcode));
          }

      }
    }

    public void save() {
        //  installmat = InstallMaterials.get(j);


        String cable_type = cableType.getSelectedItem().toString();


        if (!cable_type.isEmpty() && !noCableEt.getText().toString().isEmpty()) {
            CollectionTbl collectionTbl = new CollectionTbl();

            collectionTbl.MainObjectcode = 16;
            collectionTbl.SubObjectcode = convertBaseToCode(16, cable_type);
            collectionTbl.RequestCode = this.requestNumber;
            collectionTbl.Desc = cableEt.getText().toString();

            collectionTbl.Quantity = Integer.parseInt(noCableEt.getText().toString());

            collectionTbl.save();


        }

        String vinch_clamp = vinchType.getSelectedItem().toString();
        if (!vinch_clamp.isEmpty() && !noVinchEt.getText().toString().isEmpty()) {

            CollectionTbl collectionTbl = new CollectionTbl();
            collectionTbl.MainObjectcode = 17;
            collectionTbl.SubObjectcode = convertBaseToCode(17, vinch_clamp);
            collectionTbl.RequestCode = this.requestNumber;
            collectionTbl.Desc = vinchEt.getText().toString();
            collectionTbl.Quantity = Integer.parseInt(noVinchEt.getText().toString());


            collectionTbl.save();

        }

        String clamp_self = selfClampType.getSelectedItem().toString();
        if (!clamp_self.isEmpty() && !noselfClampEt.getText().toString().isEmpty()) {
            CollectionTbl collectionTbl = new CollectionTbl();
            collectionTbl.MainObjectcode = 18;
            collectionTbl.SubObjectcode = convertBaseToCode(18, clamp_self);
            collectionTbl.RequestCode = this.requestNumber;
            collectionTbl.Desc = selfEt.getText().toString();
            collectionTbl.Quantity = Integer.parseInt(noselfClampEt.getText().toString());


            collectionTbl.save();


        }


        String branch_box = branchBoxType.getSelectedItem().toString();
        if (!branch_box.isEmpty() && !noBranchBoxEt.getText().toString().isEmpty()) {
            CollectionTbl collectionTbl = new CollectionTbl();
            collectionTbl.MainObjectcode = 19;
            collectionTbl.SubObjectcode = convertBaseToCode(19, branch_box);
            collectionTbl.RequestCode = this.requestNumber;
            collectionTbl.Desc = branchboxEt.getText().toString();
            collectionTbl.Quantity = Integer.parseInt(noBranchBoxEt.getText().toString());

            collectionTbl.save();


        }

        String takhte_kontor = takhtekontorType.getSelectedItem().toString();
        if (!takhte_kontor.isEmpty() && !noTakhteContorEt.getText().toString().isEmpty()) {
            CollectionTbl collectionTbl = new CollectionTbl();
            collectionTbl.MainObjectcode = 20;
            collectionTbl.SubObjectcode = convertBaseToCode(20, takhte_kontor);
            collectionTbl.RequestCode = this.requestNumber;
            collectionTbl.Desc = takhteEt.getText().toString();
            collectionTbl.Quantity = Integer.parseInt(noTakhteContorEt.getText().toString());

            collectionTbl.save();


        }


        String branch_handle = branchHandleType.getSelectedItem().toString();
        if (!branch_handle.isEmpty() && !noBranchHandleEt.getText().toString().isEmpty()) {
            CollectionTbl collectionTbl = new CollectionTbl();
            collectionTbl.MainObjectcode = 21;
            collectionTbl.SubObjectcode = convertBaseToCode(21, branch_handle);
            collectionTbl.RequestCode = this.requestNumber;
            collectionTbl.Desc = branchhandleEt.getText().toString();
            collectionTbl.Quantity = Integer.parseInt(noBranchHandleEt.getText().toString());

            collectionTbl.save();

        }


        String fioz_type = phiozType.getSelectedItem().toString();
        if (!fioz_type.isEmpty() && !noPhiozEt.getText().toString().isEmpty()) {
            CollectionTbl collectionTbl = new CollectionTbl();
            collectionTbl.MainObjectcode = 22;
            collectionTbl.SubObjectcode = convertBaseToCode(22, fioz_type);
            collectionTbl.RequestCode = this.requestNumber;
            collectionTbl.Desc = phiozEt.getText().toString();
            collectionTbl.Quantity = Integer.parseInt(noPhiozEt.getText().toString());

            collectionTbl.save();

        }



}

    public int convertBaseToCode(int tabletcode, String name) {

        List<BaseMaterialTbl> base = BaseMaterialTbl.find(BaseMaterialTbl.class, "Tabletmain_code = ? and Description = ?", String.valueOf(tabletcode), name);

        BaseMaterialTbl materialbase = base.get(0);
        return materialbase.Tablet_Code;
    }
    /**
     * user wants to add new material
     * create a bundle named temp and put it into new material fragment
     * bundle contains information about materials , such as name, volume, unit, resposibility, status,....
     *
     */
 /*   private void setListClickHandler() {
       collectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HeaderViewListAdapter mHeaderAdapter = (HeaderViewListAdapter)collectListView.getAdapter();
                 CartableMeasurementListCustomAdapter mMaterialAdapter = (CartableMeasurementListCustomAdapter)mHeaderAdapter.getWrappedAdapter();
                Log.e("I am here", "hi");
                Bundle temp = new Bundle();
                //  temp.putString("subs", mMaterialAdapter.getMaterialList()
                //  .get(position).branchCode);
                temp.putString("goods",  mMaterialAdapter.getMaterialList()
                        .get(position).goods);
                temp.putString("estimated",  mMaterialAdapter.getMaterialList()
                        .get(position).estimated);
                temp.putString("unit", mMaterialAdapter.getMaterialList()
                        .get(position).unit);
                temp.putString("resposible",  mMaterialAdapter.getMaterialList()
                        .get(position).responsible);
                temp.putString("status",  mMaterialAdapter.getMaterialList()
                        .get(position).status);
                temp.putString("position", String.valueOf( position ));

                FragmentManager fm = getFragmentManager();
                CollectMaterial collect = new CollectMaterial();
                collect.show(fm, "edit material");
                collect.setTargetFragment(CartableCollectMaterial.this, editNumber);
                collect.setArguments(temp);
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
     * if request code = delete number, it means user wants to delete this item
     * if request code = new number, it means new material inserted and should be save in DB and add to list
     * if request code = edit number, it means old material changed and should be save in DB and replace in list so we need to know
     * the position of it
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == deleteNumber) {
            int position = Integer.valueOf(result.getStringExtra("position"));
            CollectMeterTbl item = adapter.getMaterialList().get(position);
            item.delete();
            adapter.getMaterialList().remove(position);
            adapter.notifyDataSetChanged();
        }
        else if( requestCode == newNumber ) {
            CollectMeterTbl  collectionTbl= new CollectMeterTbl();
            collectionTbl.Request_Code = this.requestNumber;
            // measurmentMaterialDB.branchCode = result.getStringExtra("subs");
            collectionTbl.BranchSrl = Long.parseLong(result.getStringExtra("parvande"));
            collectionTbl.FabrikNumber = result.getStringExtra("badane");
            collectionTbl.Desc = result.getStringExtra("desc");
            collectionTbl.SubObjectcode = Integer.parseInt(result.getStringExtra("contor"));

            adapter.getMaterialList().add(collectionTbl);
            adapter.notifyDataSetChanged();
            collectionTbl.save();
        }
        else if( requestCode == editNumber ) {
            Bundle bundle = result.getExtras();
          String position = bundle.getString("position");
            CollectMeterTbl collectMeterTbl = adapter.getMaterialList().get(Integer.parseInt(position));
            collectMeterTbl.Request_Code = this.requestNumber;
            collectMeterTbl.BranchSrl = Long.parseLong(bundle.getString("parvande"));
            collectMeterTbl.FabrikNumber = bundle.getString("badane");
            collectMeterTbl.Desc = bundle.getString("desc");
            collectMeterTbl.SubObjectcode = bundle.getInt("contorcode", 0);
            collectMeterTbl.save();
            adapter.notifyDataSetChanged();
        }
        /*
        requestcode = 11 means that new number comming from CollectMaterial DialoFragment
         */

        else if(requestCode == 11){

            CollectionTbl collectionTbl = new CollectionTbl();

            collectionTbl.MainObjectcode = convertBaseToCode(-1 ,result.getStringExtra("good"));
            collectionTbl.SubObjectcode = convertBaseToCode(convertBaseToCode(-1 ,result.getStringExtra("good")), result.getStringExtra("type"));
            collectionTbl.RequestCode = this.requestNumber;
            collectionTbl.Desc = result.getStringExtra("desc");
            collectionTbl.Quantity = Integer.parseInt(result.getStringExtra("number"));
            collectionTbl.Othermaterials = 1;
            collectionTbl.save();
            otherAdapter.getMaterialList().add(collectionTbl);
            otherAdapter.notifyDataSetChanged();



        }

        //May not be necessary
        thisItem.save();
    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                        CollectionTbl collect = otherAdapter.getMaterialList().get(position);
                        collect.delete();
                        otherAdapter.getMaterialList().remove(position);
                        otherAdapter.notifyDataSetChanged();


    }
}
