package com.andc.slidingmenu.Fragments.Cartable.Materials;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.andc.slidingmenu.Dialogs.NewMaterial;
import com.andc.slidingmenu.R;
import com.andc.slidingmenu.adapter.CartableOtherInstalationAdapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import slidingmenu.andc.com.dataaccess.BaseMaterialTbl;
import slidingmenu.andc.com.dataaccess.CartableTbl;
import slidingmenu.andc.com.dataaccess.CollectionTbl;
import slidingmenu.andc.com.dataaccess.InstalationTbl;

/**
 * Created by SiaJam on 1/31/2017.
 */

public class CartableInstallMaterial extends Fragment implements AdapterView.OnItemClickListener {

    public ListView requirmentListView;
  //  public CartableMeasurementListCustomAdapter adapter;
    public int newNumber = 7;
    public int editNumber = 8;
    public int deleteNumber = 9;
    public long requestNumber;
    CartableOtherInstalationAdapter adapter;
    public CartableTbl thisItem;
    public List<InstalationTbl> InstallMaterials;
    public InstalationTbl installmat;
    private int width;
    private int height;
    public ImageButton doneButton;
    public Spinner materialKontor, materialKabl, materialVinch, materialClampSelf, materialBranchBox, materialTakhteKontor, materialBranchHandle, materialFioz;
    public Spinner responsibleKontor, responsibleKabl, responsibleVinch, responsibleClampSelf, responsibleBranchBox, responsibleTakhteKontor, responsibleBranchHandle, responsibleFioz;
    public EditText noCableEt, noVinchEt, noselfClampEt, noBranchBoxEt, noTakhteContorEt, noBranchHandleEt, noPhiozEt, nokontorEt;
    public  ArrayAdapter kontoradapter, kabladapter, vinchadapter, clampadapter,boxadapter, takhtekontorAdapter, fiozadapter, handleadapter;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_visit_place_requirement_tools, container, false);

        definition(rootview);
        //setListClickHandler();
        getScreenSize();
        setAdapter();



            loadData();


        return rootview;

    }



public void save(){
  //  installmat = InstallMaterials.get(j);
    String response_kontor = responsibleKontor.getSelectedItem().toString();
    String kontor_type = materialKontor.getSelectedItem().toString();
    if(!kontor_type.isEmpty() && !nokontorEt.getText().toString().isEmpty()){
        InstalationTbl instalationTbl = new InstalationTbl();
        instalationTbl.MainObjectcode = 15;
        instalationTbl.SubObjectcode = convertBaseToCode(15, kontor_type);
        instalationTbl.RequestCode = requestNumber;
        instalationTbl.Quantity = Integer.parseInt(nokontorEt.getText().toString());
        if(response_kontor.equals("متقاضی"))
            instalationTbl.Promiser = 1;
        else
            instalationTbl.Promiser = 2;
        instalationTbl.save();

    }


    String response_cable = responsibleKabl.getSelectedItem().toString();
    String cable_type = materialKabl.getSelectedItem().toString();


    if(!cable_type.isEmpty() && !noCableEt.getText().toString().isEmpty()){
        InstalationTbl instalationTbl = new InstalationTbl();

        instalationTbl.MainObjectcode = 16;
        instalationTbl.SubObjectcode =  convertBaseToCode(16, cable_type);
        instalationTbl.RequestCode = this.requestNumber;

      instalationTbl.Quantity = Integer.parseInt(noCableEt.getText().toString());
        if(response_cable.equals("متقاضی"))
            instalationTbl.Promiser = 1;
        else
            instalationTbl.Promiser = 2;
              instalationTbl.save();


    }
    String respon_vinch = responsibleVinch.getSelectedItem().toString();
    String vinch_clamp = materialVinch.getSelectedItem().toString();
    if(!vinch_clamp.isEmpty() && !noVinchEt.getText().toString().isEmpty()){

        InstalationTbl instalationTbl = new InstalationTbl();
        instalationTbl.MainObjectcode = 17;
        instalationTbl.SubObjectcode = convertBaseToCode(17, vinch_clamp);
        instalationTbl.RequestCode = this.requestNumber;
        instalationTbl.Quantity = Integer.parseInt(noVinchEt.getText().toString());
        if(respon_vinch.equals("متقاضی"))
            instalationTbl.Promiser = 1;
        else
            instalationTbl.Promiser = 2;

        instalationTbl.save();

    }
    String respon_self = responsibleClampSelf.getSelectedItem().toString();
    String clamp_self = materialClampSelf.getSelectedItem().toString();
    if(!clamp_self.isEmpty() && !noselfClampEt.getText().toString().isEmpty()){
        InstalationTbl instalationTbl = new InstalationTbl();
        instalationTbl.MainObjectcode = 18;
        instalationTbl.SubObjectcode = convertBaseToCode(18, clamp_self);
        instalationTbl.RequestCode = this.requestNumber;
        instalationTbl.Quantity = Integer.parseInt(noselfClampEt.getText().toString());
        if(respon_self.equals("متقاضی"))
        instalationTbl.Promiser = 1;
        else
        instalationTbl.Promiser = 2;

        instalationTbl.save();



    }

    String respons_Box = responsibleBranchBox.getSelectedItem().toString();
    String branch_box = materialBranchBox.getSelectedItem().toString();
    if(!branch_box.isEmpty()&& !noBranchBoxEt.getText().toString().isEmpty()){
        InstalationTbl instalationTbl = new InstalationTbl();
        instalationTbl.MainObjectcode = 19;
        instalationTbl.SubObjectcode = convertBaseToCode(19, branch_box);
        instalationTbl.RequestCode = this.requestNumber;
        instalationTbl.Quantity = Integer.parseInt(noBranchBoxEt.getText().toString());
        if(respons_Box.equals("متقاضی"))
            instalationTbl.Promiser = 1;
        else
            instalationTbl.Promiser = 2;
        instalationTbl.save();


    }
    String response_takhte = responsibleTakhteKontor.getSelectedItem().toString();
    String takhte_kontor = materialTakhteKontor.getSelectedItem().toString();
    if(!takhte_kontor.isEmpty() && !noTakhteContorEt.getText().toString().isEmpty()){
        InstalationTbl instalationTbl = new InstalationTbl();
       instalationTbl.MainObjectcode = 20;
        instalationTbl.SubObjectcode = convertBaseToCode(20,takhte_kontor);
        instalationTbl.RequestCode = this.requestNumber;
        instalationTbl.Quantity = Integer.parseInt(noTakhteContorEt.getText().toString());
        if(response_takhte.equals("متقاضی"))
            instalationTbl.Promiser = 1;
        else
            instalationTbl.Promiser = 2;
        instalationTbl.save();


    }

    String response_handle = responsibleTakhteKontor.getSelectedItem().toString();
    String branch_handle = materialBranchHandle.getSelectedItem().toString();
    if(!branch_handle.isEmpty() &&!noBranchHandleEt.getText().toString().isEmpty()){
        InstalationTbl instalationTbl = new InstalationTbl();
        instalationTbl.MainObjectcode = 21;
        instalationTbl.SubObjectcode = convertBaseToCode(21,branch_handle);
        instalationTbl.RequestCode = this.requestNumber;
        instalationTbl.Quantity = Integer.parseInt(noBranchHandleEt.getText().toString());
        if(response_handle.equals("متقاضی"))
            instalationTbl.Promiser = 1;
        else
            instalationTbl.Promiser = 2;
        instalationTbl.save();

    }

    String response_fioz = responsibleFioz.getSelectedItem().toString();
    String fioz_type = materialFioz.getSelectedItem().toString();
    if(!fioz_type.isEmpty() && !noPhiozEt.getText().toString().isEmpty()){
        InstalationTbl instalationTbl = new InstalationTbl();
        instalationTbl.MainObjectcode = 22;
        instalationTbl.SubObjectcode = convertBaseToCode(22, fioz_type);
        instalationTbl.RequestCode = this.requestNumber;
        instalationTbl.Quantity = Integer.parseInt(noPhiozEt.getText().toString());
        if(response_fioz.equals("متقاضی"))
            instalationTbl.Promiser = 1;
        else
            instalationTbl.Promiser = 2;
        instalationTbl.save();

    }





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
       List<InstalationTbl> instal = InstalationTbl.find(InstalationTbl.class, "Request_Code = ?", String.valueOf(requestNumber));

       for(InstalationTbl instalationTbl: instal) {
           if (instalationTbl.MainObjectcode == 15 && instalationTbl.Quantity != 0) {
               nokontorEt.setText(String.valueOf(instalationTbl.Quantity));
               materialKontor.setSelection(getAdapterPosion(materialKontor, instalationTbl.MainObjectcode, instalationTbl.SubObjectcode));
           }

           if (instalationTbl.MainObjectcode == 16 && instalationTbl.Quantity != 0) {
               noCableEt.setText(String.valueOf(instalationTbl.Quantity));
               materialKabl.setSelection(getAdapterPosion(materialKabl, instalationTbl.MainObjectcode, instalationTbl.SubObjectcode));
           }
           if (instalationTbl.MainObjectcode == 17 && instalationTbl.Quantity != 0) {
               noVinchEt.setText(String.valueOf(instalationTbl.Quantity));
               materialVinch.setSelection(getAdapterPosion(materialVinch, instalationTbl.MainObjectcode, instalationTbl.SubObjectcode));
           }
           if (instalationTbl.MainObjectcode == 18 && instalationTbl.Quantity != 0) {
               noselfClampEt.setText(String.valueOf(instalationTbl.Quantity));
               materialClampSelf.setSelection(getAdapterPosion(materialClampSelf, instalationTbl.MainObjectcode, instalationTbl.SubObjectcode));
           }
           if (instalationTbl.MainObjectcode == 19 && instalationTbl.Quantity != 0) {
               noBranchBoxEt.setText(String.valueOf(instalationTbl.Quantity));
               materialBranchBox.setSelection(getAdapterPosion(materialBranchBox, instalationTbl.MainObjectcode, instalationTbl.SubObjectcode));

           }
           if (instalationTbl.MainObjectcode == 20 && instalationTbl.Quantity != 0) {
               noTakhteContorEt.setText(String.valueOf(instalationTbl.Quantity));
               materialTakhteKontor.setSelection(getAdapterPosion(materialTakhteKontor, instalationTbl.MainObjectcode, instalationTbl.SubObjectcode));

           }
           if (instalationTbl.MainObjectcode == 21 && instalationTbl.Quantity != 0) {
               noBranchHandleEt.setText(String.valueOf(instalationTbl.Quantity));
               materialBranchHandle.setSelection(getAdapterPosion(materialBranchHandle, instalationTbl.MainObjectcode, instalationTbl.SubObjectcode));

           }
           if (instalationTbl.MainObjectcode == 22 && instalationTbl.Quantity != 0) {
               noPhiozEt.setText(String.valueOf(instalationTbl.Quantity));
               materialFioz.setSelection(getAdapterPosion(materialFioz, instalationTbl.MainObjectcode, instalationTbl.SubObjectcode));
           }

           if(instalationTbl.MainObjectcode == 15 && instalationTbl.Promiser == 1)
               responsibleKontor.setSelection(0);
           else
               responsibleKontor.setSelection(1);
           if(instalationTbl.MainObjectcode == 16 && instalationTbl.Promiser == 1)
               responsibleKabl.setSelection(0);
           else
               responsibleKabl.setSelection(1);

           if(instalationTbl.MainObjectcode == 17 && instalationTbl.Promiser == 1)
               responsibleVinch.setSelection(0);
           else
               responsibleVinch.setSelection(1);

           if(instalationTbl.MainObjectcode == 18 && instalationTbl.Promiser == 1)
               responsibleClampSelf.setSelection(0);
           else
               responsibleClampSelf.setSelection(1);

           if(instalationTbl.MainObjectcode == 19 && instalationTbl.Promiser == 1)
               responsibleBranchBox.setSelection(0);
           else
               responsibleBranchBox.setSelection(1);

           if(instalationTbl.MainObjectcode == 20 && instalationTbl.Promiser == 1)
               responsibleTakhteKontor.setSelection(0);
           else
               responsibleTakhteKontor.setSelection(1);

           if(instalationTbl.MainObjectcode == 21 && instalationTbl.Promiser == 1)
               responsibleBranchHandle.setSelection(0);
           else
               responsibleBranchHandle.setSelection(1);

           if(instalationTbl.MainObjectcode == 22 && instalationTbl.Promiser == 1)
               responsibleFioz.setSelection(0);
           else
               responsibleFioz.setSelection(1);
       }
    }

    private void setAdapter() {

       List<BaseMaterialTbl> kontorlist = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "15");
        List<String> kontors = new ArrayList<>();
        for(BaseMaterialTbl base: kontorlist){
            kontors.add(base.Description);
        }

        kontoradapter =new ArrayAdapter<>(getActivity(), R.layout.spinner_dropdown_item, kontors);
        materialKontor.setAdapter(kontoradapter);

        List<BaseMaterialTbl> kableList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "16");
        List<String> cables = new ArrayList();
        for(BaseMaterialTbl base: kableList){
            cables.add(base.Description);
        }

        kabladapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, cables);
        materialKabl.setAdapter(kabladapter);

        List<BaseMaterialTbl> VinchList = BaseMaterialTbl.find(BaseMaterialTbl.class, "TabletMain_Code = ?","17");
        List<String> vinchs = new ArrayList<>();
        for(BaseMaterialTbl base: VinchList){
            vinchs.add(base.Description);
        }

        vinchadapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item, vinchs);
        materialVinch.setAdapter(vinchadapter);

        List<BaseMaterialTbl> clampselfList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "18");
        List<String> clampselfs = new ArrayList();
        for(BaseMaterialTbl base: clampselfList){
            clampselfs.add(base.Description);
        }

         clampadapter = new ArrayAdapter(getActivity(), R.layout.spinner_dropdown_item , clampselfs);
        materialClampSelf.setAdapter(clampadapter);



        List<BaseMaterialTbl> branhboxList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "19");
        List<String> branchbox = new ArrayList();
        for(BaseMaterialTbl base: branhboxList){
            branchbox.add(base.Description);
        }
         boxadapter = new ArrayAdapter(getActivity(),R.layout.spinner_dropdown_item, branchbox);
        materialBranchBox.setAdapter(boxadapter);

        List<BaseMaterialTbl> takhtekontorList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "20");
        List<String> takhtekontor = new ArrayList();
        for(BaseMaterialTbl base: takhtekontorList){
            takhtekontor.add(base.Description);
        }

        takhtekontorAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_dropdown_item , takhtekontor);
        materialTakhteKontor.setAdapter(takhtekontorAdapter);

        List<BaseMaterialTbl> branchhandleList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "21");
        List<String> branchhandle = new ArrayList();
        for(BaseMaterialTbl base: branchhandleList){
            branchhandle.add(base.Description);
        }

         handleadapter = new  ArrayAdapter(getActivity(),R.layout.spinner_dropdown_item, branchhandle);
        materialBranchHandle.setAdapter(handleadapter);
        List<BaseMaterialTbl> phiozList = BaseMaterialTbl.find(BaseMaterialTbl.class , "TabletMain_Code = ?", "22");
        List<String> phiozes = new ArrayList();
        for(BaseMaterialTbl base: phiozList){
            phiozes.add(base.Description);
        }
        fiozadapter = new ArrayAdapter(getActivity(),R.layout.spinner_dropdown_item, phiozes);
        fiozadapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        materialFioz.setAdapter(fiozadapter);
        List<String> responsible = new ArrayList<>();

        ArrayAdapter responsibleAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, getResources().getTextArray(R.array.responsible_item));
        responsibleKabl.setAdapter(responsibleAdapter);
        responsibleVinch.setAdapter(responsibleAdapter);
        responsibleClampSelf.setAdapter(responsibleAdapter);
        responsibleBranchBox.setAdapter(responsibleAdapter);
        responsibleBranchHandle.setAdapter(responsibleAdapter);
        responsibleFioz.setAdapter(responsibleAdapter);
        responsibleTakhteKontor.setAdapter(responsibleAdapter);
        responsibleKontor.setAdapter(responsibleAdapter);

    }

    private void definition(View rootview) {

        requirmentListView = (ListView) rootview.findViewById(R.id.requirement_tools_list);
        requirmentListView.setClickable(true);
        requirmentListView.setOnItemClickListener(this);
        //setListClickHandler();
        //Set Up Adapter
      //  adapter = new CartableMeasurementListCustomAdapter(CartableInstallMaterial.this);

        Bundle info = getArguments();
        this.requestNumber = info.getLong("requestnumber");

        List<CartableTbl> cartable = CartableTbl.find(CartableTbl.class, "Request_Code = ?", String.valueOf(this.requestNumber));
        this.thisItem = cartable.get(0);
        InstallMaterials = new ArrayList<>();
       List<InstalationTbl> measurmentMaterial = InstalationTbl.find(InstalationTbl.class, "Request_code = ? and Othermaterials = ?", String.valueOf(this.requestNumber), "0");

        for(InstalationTbl material:measurmentMaterial){
            InstallMaterials.add(material);
        }
        for(int i=0; i<InstallMaterials.size();i++) {
            installmat = InstallMaterials.get(i);
        }
        adapter = new CartableOtherInstalationAdapter(CartableInstallMaterial.this);
        List<InstalationTbl> otherlist = InstalationTbl.find(InstalationTbl.class, "Request_code = ? and Othermaterials = ?",String.valueOf(this.requestNumber), "1" );
        for(InstalationTbl installMaterials: otherlist){
            adapter.getMaterialList().add(installMaterials);


        }
        requirmentListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

       //     adapter.getMaterialList().add(material);
        //Defining dropDown list
        materialKontor = (Spinner) rootview.findViewById(R.id.cartable_material_kontor);
        materialKabl = (Spinner) rootview.findViewById(R.id.cartable_material_kabl);
        materialVinch = (Spinner) rootview.findViewById(R.id.cartable_material_vinch);
        materialClampSelf = (Spinner) rootview.findViewById(R.id.cartable_material_clamp_self);
        materialTakhteKontor = (Spinner) rootview.findViewById(R.id.cartable_material_takhte_kontor);
        materialBranchBox = (Spinner) rootview.findViewById(R.id.cartable_material_branch_box);
        materialBranchHandle = (Spinner) rootview.findViewById(R.id.cartable_material_brnach_handle);
        materialFioz = (Spinner) rootview.findViewById(R.id.cartable_material_fioz);

        responsibleKontor = (Spinner) rootview.findViewById(R.id.cartable_material_responsible_kontor);
        responsibleKabl = (Spinner) rootview.findViewById(R.id.cartable_material_responsible_kabl);
        responsibleVinch = (Spinner) rootview.findViewById(R.id.cartable_material_responsible_vinch);
        responsibleClampSelf = (Spinner) rootview.findViewById(R.id.cartable_material_responsible_clamp_self);
        responsibleTakhteKontor = (Spinner) rootview.findViewById(R.id.cartable_material_responsible_takhte_kontor);
        responsibleBranchBox = (Spinner) rootview.findViewById(R.id.cartable_material_responsible_branch_box);
        responsibleBranchHandle = (Spinner) rootview.findViewById(R.id.cartable_material_responsible_branch_handle);
        responsibleFioz = (Spinner) rootview.findViewById(R.id.cartable_material_responsible_fioz);

        // Defining add and done button
        ImageButton imageButton = (ImageButton) rootview.findViewById(R.id.requirement_add_button);
        doneButton = (ImageButton) rootview.findViewById(R.id.material_install_add);
        //Defining Edit Text
        nokontorEt = (EditText) rootview.findViewById(R.id.numberofkontor);
        noCableEt = (EditText) rootview.findViewById(R.id.numberofkabl);
        noVinchEt = (EditText) rootview.findViewById(R.id.numberofvinch);
        noselfClampEt = (EditText) rootview.findViewById(R.id.numberofclampself);
        noBranchBoxEt = (EditText) rootview.findViewById(R.id.numberof_brancgbox);
        noTakhteContorEt = (EditText) rootview.findViewById(R.id.numberof_takhte_kontor);
        noBranchHandleEt = (EditText) rootview.findViewById(R.id.numberof_branch_handle);
        noPhiozEt = (EditText) rootview.findViewById(R.id.numberof_fioz);
        //Set Header Button to List View

        LinearLayout mAddButton = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.list_view_add_button, null, false);
        mAddButton.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        TextView mAddText = (TextView)mAddButton.findViewById(R.id.list_item_branch_add_button);
        mAddText.setText(R.string.cartable_measurment_create);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                NewMaterial newMaterial = new NewMaterial();
                newMaterial.show(fm, "new material");
                newMaterial.setTargetFragment(CartableInstallMaterial.this, newNumber);
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    InstalationTbl.deleteAll(InstalationTbl.class, "Request_Code = ? and Othermaterials = ?", String.valueOf(requestNumber), "0");
                    save();
                Toast.makeText(getActivity(), "تغییرات با موفقیت ذخیره گردید", Toast.LENGTH_SHORT).show();


                }

        });

     //   requirmentListView.setAdapter(adapter);
    //    adapter.notifyDataSetChanged();
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
     * user wants to add new material
     * create a bundle named temp and put it into new material fragment
     * bundle contains information about materials , such as name, volume, unit, resposibility, status,....
     *
     */
 /*   private void setListClickHandler() {
        requirmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //HeaderViewListAdapter mHeaderAdapter = (HeaderViewListAdapter)requirmentListView.getAdapter();
               // CartableMeasurementListCustomAdapter mMaterialAdapter = (CartableMeasurementListCustomAdapter)mHeaderAdapter.getWrappedAdapter();

                Bundle temp = new Bundle();
              //  temp.putString("subs", mMaterialAdapter.getMaterialList()
                      //  .get(position).branchCode);
                temp.putString("goods", adapter.getMaterialList()
                        .get(position).goods);
                temp.putString("estimated", adapter.getMaterialList()
                        .get(position).estimated);
                temp.putString("unit", adapter.getMaterialList()
                        .get(position).unit);
                temp.putString("resposible",adapter.getMaterialList()
                        .get(position).responsible);
                temp.putString("status",adapter.getMaterialList()
                        .get(position).status);
                temp.putString("position", String.valueOf( position ));

                FragmentManager fm = getFragmentManager();
                NewMaterial newMaterial = new NewMaterial();
                newMaterial.show(fm, "edit material");
                newMaterial.setTargetFragment(CartableInstallMaterial.this, editNumber);
                newMaterial.setArguments(temp);
            }
        });
    } */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == deleteNumber) {
            int position = Integer.valueOf(result.getStringExtra("position"));
            InstalationTbl item = adapter.getMaterialList().get(position);
            item.delete();
            adapter.getMaterialList().remove(position);
            adapter.notifyDataSetChanged();
        }
        else if( requestCode == newNumber ) {
            InstalationTbl instalationTbl = new InstalationTbl();
            instalationTbl.RequestCode= this.requestNumber;
            // measurmentMaterialDB.branchCode = result.getStringExtra("subs");
            instalationTbl.MainObjectcode = convertBaseToCode(-1 ,result.getStringExtra("goods"));
            instalationTbl.SubObjectcode = convertBaseToCode(convertBaseToCode(-1,result.getStringExtra("goods")), result.getStringExtra("type"));
            instalationTbl.Quantity = Integer.parseInt(result.getStringExtra("estimated"));
            if(result.getStringExtra("resposible").equals("متقاضی"))
            instalationTbl.Promiser = 1;
            else
            instalationTbl.Promiser = 2;
            instalationTbl.Othermaterials = 1;
            adapter.getMaterialList().add(instalationTbl);
            adapter.notifyDataSetChanged();
            instalationTbl.save();
        }
        else if( requestCode == editNumber ) {
            int position = Integer.valueOf( result.getStringExtra("position") );

            adapter.notifyDataSetChanged();
        }

        //May not be necessary
        thisItem.save();
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                        InstalationTbl collect =adapter.getMaterialList().get(position);
                        collect.delete();
                        adapter.getMaterialList().remove(position);
                        adapter.notifyDataSetChanged();


    }
}
