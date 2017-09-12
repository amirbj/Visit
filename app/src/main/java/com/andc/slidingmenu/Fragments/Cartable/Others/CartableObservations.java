package com.andc.slidingmenu.Fragments.Cartable.Others;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.andc.slidingmenu.Main.CartableItemActivity;
import com.andc.slidingmenu.Modals.ObserveDataType;
import com.andc.slidingmenu.Modals.Observe_List_Item;
import com.andc.slidingmenu.R;
import com.andc.slidingmenu.Utility.JSONHelper;
import com.andc.slidingmenu.Utility.NationalCode;
import com.andc.slidingmenu.Utility.SaveFile;
import com.andc.slidingmenu.adapter.CartableObservationListAdapter;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import slidingmenu.andc.com.dataaccess.CartableTbl;
import slidingmenu.andc.com.dataaccess.ObservationListTbl;
import slidingmenu.andc.com.dataaccess.ObservationTbl;
import slidingmenu.andc.com.dataaccess.ObserveGroupTbl;

import static android.R.layout.simple_spinner_dropdown_item;
import static android.R.layout.simple_spinner_item;


public class CartableObservations extends Fragment implements CartableItemActivity.Callbacks {
    private ObservationTbl mObservation;
    private long requestNumber;
    private ListView lvList;
    private ListView lvSelect;
    private ArrayList<Observe_List_Item> lst_observe_group, lst_observe_list, lst_selected_observes;
    private CartableObservationListAdapter list_customListViewAdapter;
    private CartableObservationListAdapter selectedItems_customListViewAdapter;
    private View rootView;
    private List<String> activityList;
    private ArrayAdapter<String> activityListAdapter;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;


    @Override
    public boolean isValid(){
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_visit_place_observations, container, false);

        Bundle info = getArguments();
        requestNumber = info.getLong("requestnumber");
        //mObservation = new ObservationTbl();


        ListView lvGroup = (ListView) rootView.findViewById(R.id.lvGroups);
        lvList = (ListView) rootView.findViewById(R.id.lvList);
        lvSelect = (ListView) rootView.findViewById(R.id.lvSelected);
        ProgressDialog pd = new ProgressDialog(getActivity());

        //XIF Etelaat Daryafti moshtarak (Cartable)
        //XOF Etelaat por shode
        //XIF xif = recordManager.GetCurrentXIF();
        //XOF xof = ((IDBManager) getActivity()).GetDatabase().getXOF(xif.xifId);


        ArrayList<ObserveGroupTbl> og = (ArrayList<ObserveGroupTbl>) ObserveGroupTbl.listAll(ObserveGroupTbl.class);
        lst_observe_group = new ArrayList<Observe_List_Item>();
        for (int i=0; i<og.size(); i++)
        {
            Observe_List_Item oli = new Observe_List_Item();
            oli.Id = og.get(i).GroupId;
            oli.Title = og.get(i).Title;
            lst_observe_group.add(oli);
        }

      //  LoadActivityList loadActivityList = new LoadActivityList();
      //  loadActivityList.execute();


        CartableObservationListAdapter group_customListViewAdapter = new CartableObservationListAdapter(getActivity(), R.layout.list_view_observe, lst_observe_group);
        lvGroup.setAdapter(group_customListViewAdapter);

        List<ObservationTbl> t =ObservationTbl.listAll(ObservationTbl.class);

        final List<ObservationTbl> serverlist = ObservationTbl.find(ObservationTbl.class, "Request_Code = ? ", String.valueOf(requestNumber));

        lst_selected_observes = new ArrayList<Observe_List_Item>();
         ArrayList<Observe_List_Item> lst_list_server = new ArrayList<Observe_List_Item>();
        if(serverlist.size() != 0)
        for(int j=0; j <serverlist.size(); j++) {
            mObservation = serverlist.get(j);
            ObservationListTbl ol = ObservationListTbl.find(ObservationListTbl.class, "ObserveCode = ?", String.valueOf(mObservation.ObserveCode)).get(0);

            Observe_List_Item observe = new Observe_List_Item();
            observe.Id = ol.Observecode;
            observe.Value =  mObservation.Value;
            observe.Desc = mObservation.Desc;
           observe.ObserveCode= mObservation.ObserveCode;
            observe.ObserveType = ol.TemplateType;
            observe.DataType = ol.DataType;
            lst_selected_observes.add(observe);
        }
        else
        mObservation = new ObservationTbl();

        //lst_selected_observes = JSONHelper.convertJsonStringToObserveList(mCartable.O);
        for (int i=0; i<lst_selected_observes.size(); i++)
        {
            long id = lst_selected_observes.get(i).Id;
            ObservationListTbl ol = ObservationListTbl.find(ObservationListTbl.class, "ObserveCode = ?", String.valueOf(id)).get(0);


            if (ol != null) {



                        lst_selected_observes.get(i).Id= ol.Observecode;
                        lst_selected_observes.get(i).Title = ol.Title;
                        lst_selected_observes.get(i).ObserveType = ol.TemplateType;
                        lst_selected_observes.get(i).DataType = ol.DataType;
                        lst_selected_observes.get(i).ObserveCode = ol.Observecode;
                    }
                }



        selectedItems_customListViewAdapter = new CartableObservationListAdapter(getActivity(), R.layout.list_view_observe, lst_selected_observes);
        lvSelect.setAdapter(selectedItems_customListViewAdapter);

     //   List<ObservationTbl> serverlist = ObservationTbl.find(ObservationTbl.class, "Observe_Code = ? and Request_Code = ?", String.valueOf(lst_observe_group.get(position).ObserveCode), String.valueOf(requestNumber));

        lvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<ObservationListTbl> ol = (ArrayList<ObservationListTbl>)ObservationListTbl.find(ObservationListTbl.class, "group_id = ?", String.valueOf(lst_observe_group.get(position).Id));
                List<ObservationTbl> list = null;
                lst_observe_list = new ArrayList<Observe_List_Item>();
                try {
                     list = ObservationTbl.find(ObservationTbl.class, "Request_Code = ?", String.valueOf(requestNumber));
                }catch (EmptyStackException e){
                    e.printStackTrace();
                }
                if(serverlist.size()== 0) {

                    for (int i = 0; i < ol.size(); i++) {


                        Observe_List_Item oli = new Observe_List_Item();
                        oli.Id = ol.get(i).Observecode;
                        oli.Title = ol.get(i).Title;
                        oli.GroupId = ol.get(i).GroupId;
                        oli.ObserveType = ol.get(i).TemplateType;
                        oli.DataType = ol.get(i).DataType;
                        oli.ObserveCode = ol.get(i).Observecode;
                        oli.Value = "";
                        oli.Desc ="";
                        lst_observe_list.add(oli);

                    }
                }
                else{


                        for (int i = 0; i < ol.size(); i++) {

                            if (ol.get(i).Observecode != list.get(0).ObserveCode) {
                                Observe_List_Item oli = new Observe_List_Item();
                                oli.Id = ol.get(i).Observecode;
                                oli.Title = ol.get(i).Title;
                                oli.GroupId = ol.get(i).GroupId;
                                oli.ObserveType = ol.get(i).TemplateType;
                                oli.DataType = ol.get(i).DataType;
                                oli.ObserveCode = ol.get(i).Observecode;
                                oli.Value = "";
                                oli.Desc ="";
                                lst_observe_list.add(oli);
                            }

                        }

                }
                list_customListViewAdapter = new CartableObservationListAdapter(getActivity(), R.layout.list_view_observe, lst_observe_list);
                lvList.setAdapter(list_customListViewAdapter);
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean findFlg = false;
                for (int i=0; i<lst_selected_observes.size(); i++)
                    if (lst_selected_observes.get(i).ObserveCode == lst_observe_list.get(position).ObserveCode)
                    {
                        findFlg = true;
                        break;
                    }
                if (!findFlg) {
                    lst_selected_observes.add(lst_observe_list.get(position));
                    selectedItems_customListViewAdapter.notifyDataSetChanged();
                    updateCartable();

                }
                else
                    Toast.makeText(getActivity().getBaseContext(), getResources().getString(R.string.item_exist), Toast.LENGTH_SHORT).show();
            }
        });

        lvSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater li = LayoutInflater.from(rootView.getContext());
                View promptsView;
                AlertDialog.Builder alertDialogBuilder;
                EditText value, idPart1, idPart2, idPart3, idPart4;
                EditText desc;
                AlertDialog alertDialog;
                AutoCompleteTextView actvActivites;

                switch (lst_selected_observes.get(position).ObserveType) {
                    case 1: // Modification Mode
                        promptsView = li.inflate(R.layout.modification_layout, null);
                        alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
                        alertDialogBuilder.setView(promptsView);
                        value = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput1);
                        desc = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput2);
                        if (lst_selected_observes.get(position).Value.equals(""))
                            try {
                                value.setText(getInitValue(ObserveDataType.values()[lst_selected_observes.get(position).DataType]));
                            }catch (Exception e) {e.printStackTrace();}
                        else
                            value.setText(lst_selected_observes.get(position).Value);
                        try {
                            value = restrictEditText(value, ObserveDataType.values()[lst_selected_observes.get(position).DataType]);
                        }catch (Exception e) {e.printStackTrace();}
                        value.requestFocus();
                        desc.setText(lst_selected_observes.get(position).Desc);
                        final EditText finalUserInput = desc;
                        final EditText finalValue = value;
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("تائید", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String result="";
                                        try{
                                            result = validateValue(finalValue.getText().toString(), ObserveDataType.values()[lst_selected_observes.get(position).DataType]);
                                        }catch (Exception e) {e.printStackTrace();}
                                        if (result.equals("")) {
                                            lst_selected_observes.get(position).Value = finalValue.getText().toString();
                                            lst_selected_observes.get(position).Desc = finalUserInput.getText().toString();
                                             updateCartable();
                                        }
                                        else
                                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("لغو",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        break;
                    case 2: // Change Activity Mode
                        li = LayoutInflater.from(rootView.getContext());
                        promptsView = li.inflate(R.layout.change_branch_activity_layout, null);
                        alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
                        alertDialogBuilder.setView(promptsView);

                        actvActivites = (AutoCompleteTextView) promptsView.findViewById(R.id.autoCompleteTextView);
                        activityListAdapter = new ArrayAdapter<String>(getActivity(), simple_spinner_item, activityList);
                        activityListAdapter.setDropDownViewResource(simple_spinner_dropdown_item);
                        actvActivites.setAdapter(activityListAdapter);
                        String initValueText = "";
                        if (!lst_selected_observes.get(position).Value.equals(""))
                           initValueText = lst_selected_observes.get(position).Value + " - " + getActivityTitle(lst_selected_observes.get(position).Value);
                        actvActivites.setText(initValueText);
                        actvActivites.requestFocus();
                        desc = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                        desc.setText(lst_selected_observes.get(position).Desc);
                        final EditText finalUserInput1 = desc;
                        final AutoCompleteTextView finalAutoCompleteTV = actvActivites;
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("تائید", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        updateCartable();
                                     //   String result = getObserveCodeFromString(finalAutoCompleteTV.getText().toString());
                                      //  if (result.equals("invalid")) {
                                      //      Toast.makeText(getActivity(), "کد وارد شده معتبر نیست", Toast.LENGTH_SHORT).show();
                                      //      result = "";
                                    //    }
                                    //    else if (result.equals("not_exist")) {
                                      //      Toast.makeText(getActivity(), "کد وارد شده وجود ندارد", Toast.LENGTH_SHORT).show();
                                     //       result = "";
                                        }
                                      // lst_selected_observes.get(position).Value = result;
                                     //   lst_selected_observes.get(position).Desc = finalUserInput1.getText().toString();

                             //       }
                                })
                                .setNegativeButton("لغو",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        break;
        /*            case 3: // Change XIF Identity Mode
                        li = LayoutInflater.from(rootView.getContext());
                        promptsView = li.inflate(R.layout.change_xif_identity_layout, null);
                        alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
                        alertDialogBuilder.setView(promptsView);

                        //final XIF xif = recordManager.GetCurrentXIF();

                        idPart1 = (EditText) promptsView.findViewById(R.id.part1);
                        idPart2 = (EditText) promptsView.findViewById(R.id.part2);
                        idPart3 = (EditText) promptsView.findViewById(R.id.part3);
                        idPart4 = (EditText) promptsView.findViewById(R.id.part4);
                        desc = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

                        if (!lst_selected_observes.get(position).Value.equals("")) {
                            mapIdentity(
                                    lst_selected_observes.get(position).Value,
                                    idPart1,
                                    idPart2,
                                    idPart3,
                                    idPart4);
                        }
                        else {
                     //       idPart1.setText(String.valueOf(mCartable.city_code));
                     //       idPart2.setText(String.valueOf(mCartable.work_day_code));
                      //      idPart3.setText(String.valueOf(mCartable.reader_code));
                      //      idPart4.setText(String.valueOf(mCartable.rdg_serial));
                        }
                        desc.setText(lst_selected_observes.get(position).Desc);

                        final EditText finalDesc = desc;
                        final EditText finalIdPart1 = idPart1;
                        final EditText finalIdPart2 = idPart2;
                        final EditText finalIdPart3 = idPart3;
                        final EditText finalIdPart4 = idPart4;
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("تائید", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String result = validateIdentityCode(
                                                finalIdPart1.getText().toString(),
                                                finalIdPart2.getText().toString(),
                                                finalIdPart3.getText().toString(),
                                                finalIdPart4.getText().toString());
                                        if (!result.equals("")) {
                                            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                     //  if (hasNewValue(finalIdPart1.getText().toString(), finalIdPart2.getText().toString(), finalIdPart3.getText().toString(), finalIdPart4.getText().toString()))
                                        {
                                            lst_selected_observes.get(position).Value =
                                                    finalIdPart1.getText().toString() + "/" +
                                                            finalIdPart2.getText().toString() + "/" +
                                                            finalIdPart3.getText().toString() + "/" +
                                                            finalIdPart4.getText().toString();
                                            lst_selected_observes.get(position).Desc = finalDesc.getText().toString();
                                            //updateCartable();
                                        }
                                    }
                                })
                                .setNegativeButton("لغو",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        break; */
                    case 0: // Report Mode
                        li = LayoutInflater.from(rootView.getContext());
                        promptsView = li.inflate(R.layout.observation_desc_layout, null);
                        alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
                        alertDialogBuilder.setView(promptsView);
                        desc = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                        Button btnImgCamera = (Button) promptsView.findViewById(R.id.btnImgCamera);
                        Button btnMovCamera = (Button) promptsView.findViewById(R.id.btnMovCamera);
                        desc.setText(lst_selected_observes.get(position).Desc);

                        btnImgCamera.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    // create Intent to take a picture and return control to the calling application
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    String requestCode = String.valueOf(mObservation.RequestCode);
                                    // create a file to save the image
                                    Uri fileUri = SaveFile.getInstance().getOutputMediaFileUri(SaveFile.MEDIA_TYPE_IMAGE, "ObserveMedia",
                                            "Observer-" + requestCode + "-" +
                                                    String.valueOf(lst_selected_observes.get(position).GroupId) + "-" +
                                                    String.valueOf(lst_selected_observes.get(position).Id));
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                                    // start the image capture Intent
                                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        btnMovCamera.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    // create Intent to take a picture and return control to the calling application
                                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                                    String requestCode = String.valueOf(mObservation.RequestCode);
                                    // create a file to save the image
                                    Uri fileUri = SaveFile.getInstance().getOutputMediaFileUri(SaveFile.MEDIA_TYPE_VIDEO, "ObserveMedia",
                                            "Observer-" + requestCode + "-" +
                                                    String.valueOf(lst_selected_observes.get(position).GroupId) + "-" +
                                                    String.valueOf(lst_selected_observes.get(position).Id));
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
                                    // start the image capture Intent
                                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        final EditText finalUserInput2 = desc;
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("تائید", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        lst_selected_observes.get(position).Desc = finalUserInput2.getText().toString();
                                        updateCartable();
                                    }
                                })
                                .setNegativeButton("لغو",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
                                                dialog.cancel();
                                            }
                                        });
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                }
            }
        });
        lvSelect.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                lst_selected_observes.remove(position);
                updateCartable();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectedItems_customListViewAdapter.notifyDataSetChanged();
                    }
                });
                return true;
            }
        });
        return rootView;
    }

    private EditText restrictEditText(EditText et, ObserveDataType odt)
    {
        switch (odt)
        {
            case address:
                return et;
            case mobile:
                et.setInputType(InputType.TYPE_CLASS_PHONE);
                return et;
            case phone:
                et.setInputType(InputType.TYPE_CLASS_PHONE);
                return et;
            case postal_code:
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                return et;
            case national_code:
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                return et;
            case fbrg_num:
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                return et;
        }
        return et;
    }

    private void mapIdentity(String strIdentity, EditText etPart1, EditText etPart2, EditText etPart3, EditText etPart4)
    {
        if (strIdentity.equals(""))
            return;
        int indx = strIdentity.indexOf("/");
        if (indx <= 0)
            return;
        etPart1.setText(strIdentity.substring(0, indx));
        strIdentity = strIdentity.substring(indx+1, strIdentity.length());
        indx = strIdentity.indexOf("/");
        if (indx <= 0)
            return;
        etPart2.setText(strIdentity.substring(0, indx));
        strIdentity = strIdentity.substring(indx+1, strIdentity.length());
        indx = strIdentity.indexOf("/");
        if (indx <= 0)
            return;
        etPart3.setText(strIdentity.substring(0, indx));
        strIdentity = strIdentity.substring(indx+1, strIdentity.length());
        etPart4.setText(strIdentity);
    }

    private String validateValue(String value, ObserveDataType odt){

        if (value.equals(""))
            return "";

        String rv = "";
        switch (odt)
        {
            case address:
                break;
            case mobile:
                break;
            case phone:
                break;
            case postal_code:
                if (value.length()<10)
                    rv = String.format(getActivity().getString(R.string.msg_postalcode_invalid), 10);
                break;
            case national_code:
                if (!NationalCode.checkNationalCode(value))
                    rv = getActivity().getString(R.string.msg_invalid_national_code);
                break;
            case fbrg_num:
                break;
        }
        return rv;
    }
/*
    private boolean hasNewValue(String strPart1, String strPart2, String strPart3, String strPart4){
        boolean rv = false;
            if (!strPart1.equals(String.valueOf(mCartable.city_code)))
                rv = true;
            else if (!strPart2.equals(String.valueOf(mCartable.work_day_code)))
                rv = true;
            else if (!strPart3.equals(String.valueOf(mCartable.reader_code)))
                rv = true;
            else if (!strPart4.equals(String.valueOf(mCartable.rdg_serial)))
                rv = true;

        return rv;
    }
*/
private String getInitValue(ObserveDataType odt)
{
    CartableTbl location = CartableTbl.find(CartableTbl.class, "Request_Code = ?", String.valueOf(requestNumber)).get(0);
    switch (odt)
    {
        case address:
            return location.Address;
        case mobile:
            return location.MobileNo;
        case phone:
            return location.FixedTel;

        case fbrg_num:
            //Return Branch Fabrik Number
            return "شماره بدنه را انتخاب نمایید";
    }
    return "";
}

    private String getActivityTitle(String strActivityCode)
    {
        String rv = "";
        try {
            long temp = Long.parseLong(strActivityCode);
            ObserveGroupTbl activity = ObserveGroupTbl.find(ObserveGroupTbl.class, "group_id = ?", String.valueOf(temp)).get(0);
            if (activity.Title != null)
                rv = activity.Title;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return rv;
    }
    private String validateIdentityCode(String str1, String str2, String str3, String str4){
        String rv = "";
        if (str4.equals(""))
            rv = "ردیف وارد نشده";
        else {
            long tmpVal = Long.parseLong(str4);
            if (tmpVal <= 0)
                rv = "ردیف معتبر نمیباشد.";
        }
        if (str3.equals(""))
            rv = "کد مامور وارد نشده";
        else {
            long tmpVal = Long.parseLong(str3);
            if (tmpVal <= 0)
                rv = "کد مامور معتبر نمیباشد.";
        }
        if (str2.equals(""))
            rv = "روز کار وارد نشده";
        else {
            long tmpVal = Long.parseLong(str2);
            if (tmpVal <= 0)
                rv = "روز کار معتبر نمیباشد.";
        }
        if (str1.equals(""))
            rv = "کد منطقه وارد نشده";
        else {
            long tmpVal = Long.parseLong(str1);
            if (tmpVal <= 0)
                rv = "کد منطقه معتبر نمیباشد.";
        }
        return rv;
    }
/*
    private String getObserveCodeFromString(String strObserveItemValue)
    {
        String rv = strObserveItemValue;
        try {
            int pos = strObserveItemValue.indexOf("-");
            rv = strObserveItemValue.substring(0, pos);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        rv = rv.trim();
        try {
            long temp = Long.parseLong(rv);
            ObserveActivityTbl activity = ObserveActivityTbl.find(ObserveActivityTbl.class, "Id = ?", String.valueOf(temp)).get(0);
            if (activity.Title == null)
                rv = "not_exist";
            else
                rv = String.valueOf(temp);
        }
        catch (Exception e)
        {
            rv = "invalid";
        }

        return rv;
    } */
    private void updateCartable(){


        for(int i=0; i<lst_selected_observes.size(); i++){



            mObservation.Value = lst_selected_observes.get(i).Value;
            mObservation.Desc = lst_selected_observes.get(i).Desc;
            mObservation.RequestCode = this.requestNumber;
            mObservation.ObserveCode = lst_selected_observes.get(i).ObserveCode;

            mObservation.save();
        }


}
/*
    class LoadActivityList extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            List<ObserveActivityTbl> activitiesList = ObserveActivityTbl.listAll(ObserveActivityTbl.class);
            activityList = new ArrayList<String>();
            for (int i=0; i<activitiesList.size(); i++)
                activityList.add(String.valueOf(activitiesList.get(i).Id) + " - " + activitiesList.get(i).Title);
            return null;
        }
    } */
}