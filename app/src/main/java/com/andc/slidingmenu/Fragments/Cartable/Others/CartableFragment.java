package com.andc.slidingmenu.Fragments.Cartable.Others;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.andc.slidingmenu.ExceptionViewer.ShowExceptions;
import com.andc.slidingmenu.Main.CartableItemActivity;
import com.andc.slidingmenu.Main.MainActivity;
import com.andc.slidingmenu.Modals.BranchTypeCodes;
import com.andc.slidingmenu.Modals.ElectricSupplyCauses;
import com.andc.slidingmenu.Modals.MeasurementMaterialStatus;
import com.andc.slidingmenu.Modals.MeasurementMaterialsUnderTaker;
import com.andc.slidingmenu.Modals.TRFHCodes;
import com.andc.slidingmenu.Modals.VoltageCodes;
import com.andc.slidingmenu.R;
import com.andc.slidingmenu.Utility.PersianWordsUnicode;
import com.andc.slidingmenu.adapter.CartableListCustomAdapter;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import slidingmenu.andc.com.dataaccess.CartableTbl;
import slidingmenu.andc.com.dataaccess.ChangeBranchTbl;
import slidingmenu.andc.com.dataaccess.CollectBranchTbl;
import slidingmenu.andc.com.dataaccess.LocationTbl;
import slidingmenu.andc.com.dataaccess.NewBranchTbl;
import slidingmenu.andc.com.dataaccess.ObservationTbl;
import slidingmenu.andc.com.dataaccess.SupplyPowerTbl;

/**
 * Created by win on 4/11/2015.
 */
public class CartableFragment extends Fragment {

    public ArrayList<CartableTbl> data;
    public ArrayList<CartableTbl> tempData;
    public ArrayList<CartableTbl> mCartableList;
    public ShowExceptions e = new ShowExceptions();
    public int mCount;
    public ActionMode mActionModeUpload;
    public ActionMode mActionModeOption;
    public CartableListCustomAdapter mListAdapter;
    public String requestNumber;



    public Boolean highestRequestNumberFirst = false;
    public Boolean highestSubscriptionNameFirst = false;
    public Boolean highestAddressFirst = false; // this is unusable
    public Boolean highestPhoneFirst = false; // unusable
    public Boolean highestStatusFirst = false;

    public String username;
    public String password;
    public String token;
    public String userID = "325";

    public TextView addressTextView;
    public TextView subscriptionNameTextView;
    public TextView requestNumberTextView;
    public TextView statusTextView;

    public android.support.design.widget.FloatingActionButton mSendBtn;
    public SwipeRefreshLayout mSwipeRefresh;
    public View rootView;
    public SearchView mSearch;
    public ListView mListView;
    public TextView title;
    public TextView cartableItems;
    public Spinner display;

    private static final String NAMESPACE = "http://tempuri.org/";
    private static String URL;
    private static final String RECEIVE_SOAP_ACTION = "http://tempuri.org/CartableView";
    private static final String RECEIVE_METHOD_NAME = "CartableView";
    private static final String SEND_SOAP_ACTION = "http://tempuri.org/SaveVisitPlace";
    private static final String SEND_METHOD_NAME = "SaveVisitPlace";

    final String Table_Cartable = "CARTABLE_TBL";
    final String Table_location = "LOCATION_TBL";
    final String Table_NewBranch = "NEW_BRANCH_TBL";
    final String Table_ChangeBranch = "CHANGE_BRANCH_TBL";
    final String Table_Collectbranch = "COLLECT_BRANCH_TBL";

    int seen;
    int notSeen;
    int sent;
    int total;


    /**
     * @param inflater
     * @param container
     * @param savedInstanceState prepare layout of cartable
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cartable, container, false);
        definition(rootView);
        makeList();
        //makeSortableList();
        return rootView;
    }

    @Override
    public void onResume() {
        mListAdapter.getRequestedCartableItemsList().clear();
        mListAdapter.getRequestedCartableItemsList().addAll(CartableTbl.listAll(CartableTbl.class));
        mListAdapter.notifyDataSetChanged();


        if (mListAdapter.getCount() < 1) {
            //receiveCartable();
        }


        super.onResume();
    }

    /**
     * @param rootView find any views on cartable view and just define variables
     */
    private void definition(View rootView) {
        //Search

        mSearch = (SearchView) rootView.findViewById(R.id.appbar_search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initiate Search
                ((SearchView) view).setIconified(false);
                mCartableList = new ArrayList<>();
                mCartableList.addAll(CartableTbl.listAll(CartableTbl.class));
            }
        });
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doQuery(newText);
                return true;
            }
        });

        //List
        mListView = (ListView) rootView.findViewById(R.id.list);
        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                receiveCartable();
            }
        });

        //List Header
        this.title = (TextView) rootView.findViewById(R.id.title);
        this.requestNumberTextView = (TextView) rootView.findViewById(R.id.cartable_request_number);
        this.subscriptionNameTextView = (TextView) rootView.findViewById(R.id.cartable_subscription_name);
        this.addressTextView = (TextView) rootView.findViewById(R.id.cartable_address);
        this.statusTextView = (TextView) rootView.findViewById(R.id.cartable_status);


//        setCounters();

        this.display = (Spinner) rootView.findViewById(R.id.cartable_spinner_display);
        this.cartableItems = (TextView) rootView.findViewById(R.id.cartable_number);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.cartable_number, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        display.setAdapter(adapter);
        display.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // int i = display.getSelectedItemPosition();
                String select = display.getSelectedItem().toString();
                switch (position) {
                    case 0:

                        mListAdapter.getRequestedCartableItemsList().clear();
                        mListAdapter.getRequestedCartableItemsList().addAll(CartableTbl.listAll(CartableTbl.class));
                        mListAdapter.notifyDataSetChanged();
                        total = mListAdapter.getRequestedCartableItemsList().size();
                        cartableItems.setText("" + total);
                        break;

                    case 1:
                        seen = querycartable(1);
                        cartableItems.setText("" + seen);

                        break;
                    case 2:
                        sent = querycartable(2);
                        cartableItems.setText("" + sent);

                        break;
                    case 3:
                        int notSeen = querycartable(0);
                        cartableItems.setText("" + notSeen);

                        break;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //User Preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.username = preferences.getString("username", "");
        this.password = preferences.getString("password", "");
        this.token = preferences.getString("token", "");


        //Send And Receive Buttons
        mSendBtn = (android.support.design.widget.FloatingActionButton) rootView.findViewById(R.id.sendToServer);
        mSendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!makeURLReady())
                    return;
                else
                    sendData();
            }
        });
    }

   public int querycartable(int status){
       mListAdapter.getRequestedCartableItemsList().clear();
       mListAdapter.getRequestedCartableItemsList().addAll(CartableTbl.find(CartableTbl.class, "Status = ?", String.valueOf(status)));
       int quantity =  mListAdapter.getRequestedCartableItemsList().size();
       mListAdapter.notifyDataSetChanged();
       return  quantity;
   }

    @TargetApi(17)
    private void sendCartable() {
        Toast.makeText(getActivity(), "hi", Toast.LENGTH_LONG).show();
        //Finish Option Action Mode if Exists
        if (mActionModeOption != null)
            mActionModeOption.finish();

        //On First Click goes into ActionModeUpload
        //And on Second Click Upload Selected Cartable
        if (mActionModeUpload != null) {
            final String mUploadMessage = String.format(getActivity().getResources().getString(R.string.dialog_upload_body), mCount);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(getResources().getString(R.string.dialog_upload_title))
                    .setMessage(mUploadMessage)
                    .setNegativeButton(R.string.dialog_button_return, null)
                    .setPositiveButton(R.string.dialog_button_upload, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Start Send Animation
                            mSendBtn.setImageResource(R.drawable.btn_send_anim);
                            final AnimationDrawable mSendAnim = (AnimationDrawable) mSendBtn.getDrawable();
                            mSendAnim.start();

                            //Prepare and Send Cartable


                            //Fake Send Action
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Stop Animation
                                    mSendAnim.stop();
                                    mSendBtn.setImageResource(R.drawable.cast_ic_notification_on);

                                    //Finish Send Mode
                                    mActionModeUpload.finish();
                                    //listView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);

                                    //Fake Send Message
                                    final Snackbar mSnackBar = Snackbar.make(rootView, "Upload Successful!", Snackbar.LENGTH_INDEFINITE);
                                    mSnackBar.setAction("Done", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //mSnackBar.dismiss();
                                        }
                                    }).show();
                                }
                            }, 3000);

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

            //Set Title Gravity
            final int alertTitle = getActivity().getResources().getIdentifier("alertTitle", "id", "android");
            TextView messageText = (TextView) dialog.findViewById(alertTitle);
            //messageText.setGravity(Gravity.Right);
            messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            //Toast.makeText(getActivity(),"Plz Select Chosen records..",Toast.LENGTH_SHORT).show();
            //listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new uploadMode());

        }


    }

    public void sendData() {

        SendAsync send = new SendAsync();
        send.execute();

    }


    public class SendAsync extends AsyncTask<String, Void, Response> {
        final ProgressDialog progress = new ProgressDialog(getActivity());

        @Override
        protected void onPreExecute() {

            progress.setMessage("ارسال اطلاعات به سرور...");
            progress.show();
        }

        @Override
        protected Response doInBackground(String... params) {
            final MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");
            JSONObject object = preparesend();
            OkHttpClient client = new OkHttpClient();


            RequestBody body = RequestBody.create(JSON, object.toString());
            Request request = new Request.Builder()
                    .url(URL + "/api/VisitService/SetVisitData")
                    .addHeader("UserName", username)
                    .addHeader("Token", token)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response res = null;
            try {
                res = client.newCall(request).execute();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(Response s) {
            progress.dismiss();
            if (s.isSuccessful()) {
                List<CartableTbl> beforesend = CartableTbl.find(CartableTbl.class, "Status = ?", "1");
                for(CartableTbl cartableTbl: beforesend){
                    cartableTbl.Status = 2;
                    cartableTbl.save();
                }



                Toast.makeText(getActivity(), "ارسال با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String msg = s.body().string();
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }

    public JSONObject preparesend() {

        List<LocationTbl> location = LocationTbl.listAll(LocationTbl.class);

        JSONObject json = new JSONObject();
        try {

            for (LocationTbl lo : location) {
               CartableTbl  cartablesend = CartableTbl.find(CartableTbl.class, "Request_Code = ?", String.valueOf(lo.RequestCode)).get(0);
                if(cartablesend.Status == 1) {
                    json.put("RequestCode", lo.RequestCode);
                    json.put("VisitDur", lo.VisitDur);
                   // cartableItems(lo.RequestCode, json);

                    json.put("RequestDate", cartablesend.RequestDate);
                    json.put("latituex", lo.LastX);
                    json.put("longitudey", lo.LastY);
                    //  json.put("RequestDate", lo.VisitDateEn);
                    json.put("VisitDate", lo.VisitDateEn);
                    json.put("TotalRequestedSquare", lo.TotalArea);
                    json.put("RequestedSquare", lo.BaseArea);
                    json.put("FloorCount", lo.FloorCount);
                    json.put("UnitinFloor", null);
                    json.put("RgnCode", lo.RgnCode);
                    json.put("CityCode", lo.CityCode);
                    json.put("VillageCode", lo.VillageCode);
                    SupplyPowerTbl power = SupplyPowerTbl.find(SupplyPowerTbl.class, "Request_Code = ?", String.valueOf(lo.RequestCode)).get(0);
                    json.put("TamienBarghStatus", power.Supplymethod);
                    json.put("InstallCable", 0);
                    json.put("EarthBox", 0);
                    json.put("PostType", 0);
                    json.put("InstallWeakPressure", 0);
                    json.put("InstallAveragePressure", 0);
                    json.put("IsHarim", null);
                    json.put("Comment", "");
                    json.put("IsRural", lo.IsVillage);
                    json.put("FmlNum", lo.FamilyCount);
                    json.put("BranchMojaverLeft", lo.NextLeftFileNo);
                    json.put("MojaverIDLeft", lo.NextLeftShn);
                    json.put("BranchMojaverRight", lo.NextRightFileNo);
                    json.put("MojaverIDRight", lo.NextRightShe);
                    json.put("MapBuildingStatus", lo.BuildingStatus);
                    json.put("MapAreaCode", lo.AreaStatus);
                    json.put("MapGeoDir", lo.GeoDir);
                    json.put("MapBranchTownCode", lo.Industrialcity);
                    json.put("LastVisitDate", "");
                    json.put("LastComment", "");
                    json.put("LastReason", "");
                    json.put("Visit_NewBranchData", Visit_Branch(lo.RequestCode));
                    json.put("Visit_NeededDeviceData", new JSONArray());
                    json.put("VisitData_ObservationData", Observationarray(lo.RequestCode));
                }
            }


        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return json;
    }

    private JSONArray Observationarray(long requestCode) {
        List<ObservationTbl> listobservation = ObservationTbl.find(ObservationTbl.class, "Request_Code = ?", String.valueOf(requestCode));
        JSONArray Observationarray = new JSONArray();
        for (int i = 0; i < listobservation.size(); i++) {
            JSONObject observe = new JSONObject();

            try {
                observe.put("RequestCode", listobservation.get(i).RequestCode);
                observe.put("observeCode", listobservation.get(i).ObserveCode);
                observe.put("Description", listobservation.get(i).Desc);
                observe.put("branchsrl", listobservation.get(i).BranchSrl);
                observe.put("BranchCode", listobservation.get(i).BranchSrl);
                observe.put("FabricNumber", listobservation.get(i).FabrikNumber);
                observe.put("Value", listobservation.get(i).Value);
                Observationarray.put(i, observe);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
        return Observationarray;
    }


    private JSONArray Visit_Branch(long requestCode) {

        List<NewBranchTbl> listNEwBranch = NewBranchTbl.find(NewBranchTbl.class, "RequestCode = ?", String.valueOf(requestCode));
        List<ChangeBranchTbl> listChangeBranch = ChangeBranchTbl.find(ChangeBranchTbl.class, "Request_code = ?", String.valueOf(requestCode));
        List<CollectBranchTbl> listCollectBranch = CollectBranchTbl.find(CollectBranchTbl.class, "Request_Code = ?", String.valueOf(requestCode));

        int listnew = listNEwBranch.size();
        int listchange = listChangeBranch.size();
        int listcollect = listCollectBranch.size();
        try {
            JSONArray Visit_newBranch = new JSONArray();
            for (int i = 0; i < listnew; i++) {
                JSONObject newbranchObj = new JSONObject();
                newbranchObj.put("RequestCode", listNEwBranch.get(i).Request_Code);
                newbranchObj.put("BranchCode", listNEwBranch.get(i).Branch_Code);
                newbranchObj.put("PwrCnt", listNEwBranch.get(i).Pwrcnt);
                newbranchObj.put("PwrIcn", listNEwBranch.get(i).PwrIcn);
                newbranchObj.put("BranchTypeCode", listNEwBranch.get(i).BranchTypeCode);
                newbranchObj.put("ActionType", listNEwBranch.get(i).ActionType);
                newbranchObj.put("RequestActionType", listNEwBranch.get(i).RequestActionType);
                newbranchObj.put("MapAmp", listNEwBranch.get(i).Phs);
                newbranchObj.put("MapPhs", listNEwBranch.get(i).Amp);
                newbranchObj.put("MapTrfType", listNEwBranch.get(i).TrfType);
                newbranchObj.put("MapVoltCode", listNEwBranch.get(i).VoltCode);
                newbranchObj.put("MapTrfHCode", listNEwBranch.get(i).TrfHcode);
                newbranchObj.put("Count", listNEwBranch.get(i).Count);


                Visit_newBranch.put(i, newbranchObj);

            }
            for (int j = listnew; j < listchange; j++) {
                JSONObject changeObj = new JSONObject();
                changeObj.put("RequestCode", listChangeBranch.get(j).RequestCode);
                changeObj.put("BranchCode", listChangeBranch.get(j).BranchCode);
                changeObj.put("BranchSrl", listChangeBranch.get(j).BranchSrl);
                changeObj.put("OldPwrCnt", listChangeBranch.get(j).OldPwrcnt);
                changeObj.put("PwrCnt", listChangeBranch.get(j).Pwrcnt);
                changeObj.put("OldPwrIcn", listChangeBranch.get(j).OldPwrIcn);
                changeObj.put("PwrIcn", listChangeBranch.get(j).PwrIcn);
                changeObj.put("OldBranchTypeCode", listChangeBranch.get(j).OldBranchTypeCode);
                changeObj.put("BranchTypeCode", listChangeBranch.get(j).BranchTypeCode);
                changeObj.put("ActionType", listChangeBranch.get(j).ActionType);
                changeObj.put("RequestActionType", listChangeBranch.get(j).RequestActionType);
                changeObj.put("FabrikNumber", listChangeBranch.get(j).FabrikNumber);
                changeObj.put("HaveChangePlace", listChangeBranch.get(j).HaveChangePlace);
                changeObj.put("NeededChange", listChangeBranch.get(j).HaveChangeMeter);
                changeObj.put("MapOldAmp", listChangeBranch.get(j).OldAmp);
                changeObj.put("MapAmp", listChangeBranch.get(j).Amp);
                changeObj.put("MapOldPhs", listChangeBranch.get(j).OldPhs);
                changeObj.put("MapPhs", listChangeBranch.get(j).Phs);
                changeObj.put("MapOldTrfType", listChangeBranch.get(j).OldTrfType);
                changeObj.put("MapTrfType", listChangeBranch.get(j).TrfType);
                changeObj.put("MapOldVoltCode", listChangeBranch.get(j).OldVoltcode);
                changeObj.put("MapVoltCode", listChangeBranch.get(j).VoltCode);
                changeObj.put("MapOldTrfHCode", listChangeBranch.get(j).OldtrfHcode);
                changeObj.put("MapTrfHCode", listChangeBranch.get(j).TrfHcode);
                Visit_newBranch.put(j, changeObj);

            }
            for (int k = listchange; k < listcollect; k++) {

                JSONObject collectObject = new JSONObject();
                collectObject.put("RequestCode", listCollectBranch.get(k).RequestCode);
                collectObject.put("BranchCode", listCollectBranch.get(k).BranchCode);
                collectObject.put("BranchSrl", listCollectBranch.get(k).BranchSrl);
                collectObject.put("PwrCnt", listCollectBranch.get(k).Pwrcnt);
                collectObject.put("BranchTypeCode", listCollectBranch.get(k).BranchTypeCode);
                collectObject.put("ActionType", listCollectBranch.get(k).ActionType);
                collectObject.put("RequestActionType", listCollectBranch.get(k).RequestActionType);
                collectObject.put("FabrikNumber", listCollectBranch.get(k).FabrikNumber);
                collectObject.put("MapAmp", listCollectBranch.get(k).Amp);
                collectObject.put("MapPhs", listCollectBranch.get(k).Phs);
                collectObject.put("MapTrfType", listCollectBranch.get(k).TrfType);
                collectObject.put("MapVoltCode", listCollectBranch.get(k).VoltCode);
                collectObject.put("MapTrfHCode", listCollectBranch.get(k).TrfHcode);

                Visit_newBranch.put(k, collectObject);
            }
            return Visit_newBranch;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    private void cartableItems(long requestCode, JSONObject json) {

        CartableTbl cartable = CartableTbl.find(CartableTbl.class, "Request_Code = ?", String.valueOf(requestCode)).get(0);
        try {
          /*  json.put("FirstName", cartable.FirstName);
            json.put("LastName", cartable.LastName);
            json.put("Address", cartable.Address);
            json.put("MobileNo", cartable.MobileNo);
            json.put("FixedTel", cartable.FixedTel); */
            json.put("RequestDate", cartable.RequestDate);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }


    /**
     * create list view and its adapter to make it custom
     * tempData array list is temporary array for sorting procedure
     */
    private void makeList() {
        this.data = new ArrayList<CartableTbl>();
        mListAdapter = new CartableListCustomAdapter(this, data);

        this.data.addAll(CartableTbl.listAll(CartableTbl.class));     // add DB cartable
        mListAdapter.notifyDataSetChanged();
        mListView.setAdapter(mListAdapter);
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new optionMode());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long requestNumber = ((CartableListCustomAdapter) mListView.getAdapter()).getItem(position).RequestCode;
                Intent intent = new Intent(getActivity(), CartableItemActivity.class);
                intent.putExtra("requestnumber", requestNumber);
                getActivity().startActivity(intent);
            }
        });
        /*
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
                listView.setMultiChoiceModeListener(new optionMode());
                listView.setItemChecked(position, true);
                return true;
            }
        });
        */

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (mSendBtn.getVisibility() != View.VISIBLE) {
                        mSendBtn.show();
                        //mSendBtn.setVisibility(View.VISIBLE);
                        //mReceiveBtn.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mSendBtn.getVisibility() == View.VISIBLE) {
                        mSendBtn.hide();
                        //mSendBtn.setVisibility(View.GONE);
                        //mReceiveBtn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /*
                AppCompatActivity mActivity = (AppCompatActivity) getActivity();
                ActionBar mActionBar = mActivity.getSupportActionBar();

                if (mLastFirstVisibleItem != firstVisibleItem) {
                    if (mLastFirstVisibleItem < firstVisibleItem) {
                        if (mActionBar.isShowing())
                            mActionBar.hide();

                    } else if (mLastFirstVisibleItem > firstVisibleItem) {
                        if (!mActionBar.isShowing())
                            mActionBar.show();

                    }
                    mLastFirstVisibleItem = firstVisibleItem;
                }
                */
            }
        });
    }

    public void doQuery(String query) {

        //If Query is Null Return
        if (query == null)
            return;

        //If Query is Empty Show All Data
        if (query.length() < 1) {
            mListAdapter.getRequestedCartableItemsList().clear();
            mListAdapter.getRequestedCartableItemsList().addAll(CartableTbl.listAll(CartableTbl.class));
            mListAdapter.notifyDataSetChanged();
            return;
        }

        //Clear List
        mListAdapter.getRequestedCartableItemsList().clear();

        //Find Results
        if (query.charAt(0) < '0' || query.charAt(0) > '9') {
            //If Query Is not a Valid Number Search Base on subscriptionName
            for (CartableTbl mCartable : mCartableList)
                if (mCartable.Name.contains(query))
                    mListAdapter.getRequestedCartableItemsList().add(mCartable);
        } else {
            //If Query Is a Valid Number Search Base on requestNumber
            for (CartableTbl mCartable : mCartableList)
                if (mCartable.RequestCode == Long.parseLong(query))
                    mListAdapter.getRequestedCartableItemsList().add(mCartable);
        }

        //Display Results
        mListAdapter.notifyDataSetChanged();
    }

    /**
     * making 4 sortable items
     * the tempData arraylist used as a temporary array for sorting operation
     * 4 boolean flags is used to help ascending/descending sort
     * 1.highestRequestNumberFirst
     * 2.highestSubscriptionNameFirst
     * 3.highestPhoneFirst
     * 4.highestStatusFirst
     * to sorting persian word we need to make hashtable
     * every persian word map to a number
     * numbers and words are sequential base on persian alphabets
     */
    private void makeSortableList() {
        this.tempData = new ArrayList<CartableTbl>();

        requestNumberTextView.setClickable(true);
        requestNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionModeOption != null)
                    mActionModeOption.finish();

                if (mActionModeUpload != null)
                    mActionModeUpload.finish();

                tempData.clear();
                for (CartableTbl cartable : data)
                    tempData.add(cartable);

                if (highestRequestNumberFirst == false) {
                    for (int j = 0; j < tempData.size() - 1; j++) {
                        for (int i = j + 1; i < tempData.size(); i++) {
                            if (Long.valueOf(tempData.get(i).RequestCode) < Long.valueOf(tempData.get(j).RequestCode)) {
                                //  Swap(i, j);
                            }
                        }
                    }
                    highestRequestNumberFirst = true;
                } else {
                    for (int j = 0; j < tempData.size() - 1; j++) {
                        for (int i = j + 1; i < tempData.size(); i++) {
                            if (Long.valueOf(tempData.get(i).RequestCode) > Long.valueOf(tempData.get(j).RequestCode)) {
                                //   Swap(i, j);
                            }
                        }
                    }
                    highestRequestNumberFirst = false;
                }

                data.clear();
                for (CartableTbl cartable : tempData)
                    data.add(cartable);

                mListAdapter.notifyDataSetChanged();
            }
        });

        final PersianWordsUnicode unicode = new PersianWordsUnicode(this.getActivity());
    }
        /*
        subscriptionNameTextView.setClickable(true);
        subscriptionNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tempData.clear();
                for (CartableListDB cartable : data)
                    tempData.add(cartable);

                if(highestSubscriptionNameFirst == false) {
                    for (int j = 0; j < tempData.size() - 1; j++) {
                        for (int i = j+1; i < tempData.size() ; i++) {
                            if( unicode.getNumberOfCharacter( String.valueOf( tempData.get(i).subscriptionName.charAt(0) )) <
                                    unicode.getNumberOfCharacter( String.valueOf( tempData.get(j).subscriptionName.charAt(0) )) )
                                Swap(i,j);
                        }
                    }
                    highestSubscriptionNameFirst = true;
                }
                else{
                    for (int j = 0; j < tempData.size() - 1; j++) {
                        for (int i = j+1; i < tempData.size() ; i++) {
                            if( unicode.getNumberOfCharacter( String.valueOf( tempData.get(i).subscriptionName.charAt(0) )) >
                                    unicode.getNumberOfCharacter( String.valueOf( tempData.get(j).subscriptionName.charAt(0) ) ))
                                Swap(i,j);
                        }
                    }
                    highestSubscriptionNameFirst = false;
                }

                data.clear();
                for(CartableListDB cartable: tempData)
                    data.add(cartable);

                requestedCartableListAdapter.notifyDataSetChanged();
            }
        });

        phoneNumberTextView.setClickable(true);
        phoneNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempData.clear();
                for (CartableListDB cartable : requestedCartableListAdapter.getRequestedCartableItemsList())
                    tempData.add(cartable);

                if(highestPhoneFirst == false) {
                    for (int j = 0; j < tempData.size() - 1; j++) {
                        for (int i = j+1; i < tempData.size() ; i++) {
                            if (Long.valueOf(tempData.get(i).phone) < Long.valueOf(tempData.get(j).phone)) {
                                Swap(i, j);
                            }
                        }
                    }
                    highestPhoneFirst = true;
                }
                else{
                    for (int j = 0; j < tempData.size() - 1; j++) {
                        for (int i = j+1; i < tempData.size() ; i++) {
                            if (Long.valueOf(tempData.get(i).phone) > Long.valueOf(tempData.get(j).phone)) {
                                Swap(i, j);
                            }
                        }
                    }
                    highestPhoneFirst = false;
                }

                requestedCartableListAdapter.getRequestedCartableItemsList().clear();
                for(CartableListDB cartable:tempData)
                    requestedCartableListAdapter.getRequestedCartableItemsList().add(cartable);

                requestedCartableListAdapter.notifyDataSetChanged();
            }
        });*/

       /* statusTextView.setClickable(true);
        statusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mActionModeOption!=null)
                    mActionModeOption.finish();

                if(mActionModeUpload!=null)
                    mActionModeUpload.finish();

                tempData.clear();
                for (CartableListDB cartable : data)
                    tempData.add(cartable);



                if(highestStatusFirst == false) {
                    for (int j = 0; j < tempData.size() - 1; j++) {
                        for (int i = j+1; i < tempData.size() ; i++) {
                            if( unicode.getNumberOfCharacter( String.valueOf( tempData.get(i).status.charAt(7) )) <
                                    unicode.getNumberOfCharacter( String.valueOf( tempData.get(j).status.charAt(7) )) )
                                Swap(i,j);
                        }
                    }
                    highestStatusFirst = true;
                }
                else{
                    for (int j = 0; j < tempData.size() - 1; j++) {
                        for (int i = j+1; i < tempData.size() ; i++) {
                            if( unicode.getNumberOfCharacter( String.valueOf( tempData.get(i).status.charAt(7) )) >
                                    unicode.getNumberOfCharacter( String.valueOf( tempData.get(j).status.charAt(7) )) )
                                Swap(i,j);
                        }
                    }
                    highestStatusFirst = false;
                }

                data.clear();
                for(CartableListDB cartable: tempData)
                    data.add(cartable);

                mListAdapter.notifyDataSetChanged();
            }
        });

    }
*/
    /**
     *
     * @param i
     * @param j
     * this method receive index of two elements that should be substitute
     * there are 5 item in cartable that are related together and need to swap
     */
  /*  private void Swap(int i, int j) {
        String temp = tempData.get(i).requestNumber;
        tempData.get(i).requestNumber = tempData.get(j).requestNumber;
        tempData.get(j).requestNumber = temp;

        temp = tempData.get(i).subscriptionName;
        tempData.get(i).subscriptionName = tempData.get(j).subscriptionName;
        tempData.get(j).subscriptionName = temp;

        temp = tempData.get(i).address;
        tempData.get(i).address = tempData.get(j).address;
        tempData.get(j).address = temp;

        temp = tempData.get(i).phone;
        tempData.get(i).phone = tempData.get(j).phone;
        tempData.get(j).phone = temp;

        temp = tempData.get(i).status;
        tempData.get(i).status = tempData.get(j).status;
        tempData.get(j).status = temp;
    }
*/

    /**
     * this method try to get src address and port from application preferences
     * if it is not settled one request Toast show to user that he/she should set them in preferences fragment
     * if every thing does right a true value returned
     *
     * @return boolean value
     */
    private Boolean makeURLReady() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String webServicesAddress = preferences.getString("webServicesAddress", "");
        if (webServicesAddress.isEmpty()) {
            Toast toast = Toast.makeText(getActivity().getBaseContext()
                    , getResources().getString(R.string.src_invalid), Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toast.show();
            return false;
        }

        String port = preferences.getString("port", "");
        if (port.isEmpty()) {
            Toast toast = Toast.makeText(getActivity().getBaseContext()
                    , getResources().getString(R.string.port_invalid), Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toast.show();
            return false;
        }

        if (!port.equalsIgnoreCase("") && !webServicesAddress.equalsIgnoreCase("")) {
            URL = String.format("http://" + webServicesAddress + ":" + port);
        }
        return true;
    }


    // *************************************  receive Section

    /**
     * first we make service url ready to call, if url did not make button does not work
     * when user push receive button, the list of new cartable retrieve from server
     * this procedure done by calling async class (soap web service)
     * old ones remain the same
     */
    private void receiveCartable() {
        if (!makeURLReady())
            return;
        else
            new ReceiveRestCartable().execute();

        if (e.getMessages() != null)
            e.alertUser(getActivity());
    }

    class ReceiveRestCartable extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String Message = null;
            String s = null;
            final Request request = new Request.Builder()
                    .url(URL + "/api/VisitService/GetVisitData/332")
                    // .addHeader("Accept", "application/json")
                    .addHeader("UserName", username)
                    .addHeader("Token", token)
                    .build();

            try {
                OkHttpClient client = new OkHttpClient();
                okhttp3.Response res = client.newCall(request).execute();
                s = res.body().string();

                // Do something with the response.
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (s != null) {
                try {
                    JSONArray array = new JSONArray(s);

                    Message = saveCartable(array);
                    saveLocation(array);
                    saveNewBranch(array);
                    saveChangeBranch(array);
                    saveCollectBranch(array);
                    saveObservation(array);
                    saveSupplypower(array);


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }
            return Message;
        }

        @Override
        protected void onPostExecute(String s) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(s);
            dialog.show();

            if (mSwipeRefresh.isRefreshing())
                mSwipeRefresh.setRefreshing(false);
            mListAdapter.getRequestedCartableItemsList().clear();
            mListAdapter.getRequestedCartableItemsList().addAll(CartableTbl.listAll(CartableTbl.class));
            mListAdapter.notifyDataSetChanged();
            ((MainActivity) getActivity()).mDrawerAdapter.notifyDataSetChanged();
        }
    }

    public void saveSupplypower(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {

            try {
                JSONObject obj = array.getJSONObject(i);
                SupplyPowerTbl supplyPowerTbl = new SupplyPowerTbl();
                supplyPowerTbl.Supplymethod = obj.optInt("TamienBarghStatus");
                supplyPowerTbl.RequestCode = obj.optLong("RequestCode");
                supplyPowerTbl.ReasonnotSupply = obj.optInt("MapForbiddenReasonCode");
                supplyPowerTbl.save();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    public String saveCollectBranch(JSONArray array) {


        CollectBranchTbl.deleteAll(CollectBranchTbl.class, "Status = ?", "0");
        CollectBranchTbl.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_Collectbranch + "'");
        try {

            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);
                JSONArray newBranchArray = obj.getJSONArray("Visit_NewBranchData");
                for (int j = 0; j < newBranchArray.length(); j++) {
                    CollectBranchTbl colllectBranchTbl = new CollectBranchTbl();
                    JSONObject branch = newBranchArray.getJSONObject(j);
                    if (branch.optInt("ActionType") == 5) {
                        colllectBranchTbl.ActionType = branch.optInt("ActionType");
                        colllectBranchTbl.VisitDur = obj.optInt("VisitDur");
                        colllectBranchTbl.RequestCode = branch.optLong("RequestCode");
                        colllectBranchTbl.BranchCode = branch.optLong("BranchCode");
                        colllectBranchTbl.BranchSrl = branch.optLong("BranchSrl");
                        colllectBranchTbl.FabrikNumber = branch.optString("FabrikNumber");
                        colllectBranchTbl.Phs = branch.optInt("MapPhs");
                        colllectBranchTbl.Amp = branch.optInt("MapAmp");
                        colllectBranchTbl.Pwrcnt = branch.optInt("PwrCnt");
                        colllectBranchTbl.TrfHcode = branch.optInt("MapTrfHCode");
                        colllectBranchTbl.VoltCode = branch.optInt("MapVoltCode");
                        colllectBranchTbl.RequestActionType = branch.optInt("RequestActionType");
                        colllectBranchTbl.BranchTypeCode = branch.optInt("BranchTypeCode");
                        colllectBranchTbl.TrfType = branch.optInt("MapTrfType");
                        colllectBranchTbl.save();


                    }
                }


            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }


        return null;
    }


    public String saveObservation(JSONArray array) {

        ObservationTbl.deleteAll(ObservationTbl.class, "Status = ?", "0");


        try {
            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = null;

                obj = array.getJSONObject(i);
                JSONArray observeArray = obj.getJSONArray("VisitData_ObservationData");
                for (int j = 0; j < observeArray.length(); j++) {

                    ObservationTbl observationTbl = new ObservationTbl();
                    JSONObject observe = observeArray.getJSONObject(j);
                    observationTbl.RequestCode = observe.optLong("RequestCode");
                    observationTbl.ObserveCode = observe.optInt("observeCode");
                    observationTbl.Desc = observe.optString("Description");
                    observationTbl.BranchSrl = observe.optLong("branchsrl");
                    observationTbl.BranchCode = observe.optLong("BranchCode");
                    observationTbl.FabrikNumber = observe.optString("FabricNumber");
                    observationTbl.Value = observe.optString("Value");
                    observationTbl.save();
                }

            }
        } catch (JSONException e1) {
            e1.printStackTrace();

        }
        return null;
    }

    public String saveChangeBranch(JSONArray array) {

        ChangeBranchTbl.deleteAll(ChangeBranchTbl.class, "Status = ?", "0");
        ChangeBranchTbl.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_ChangeBranch + "'");
        try {

            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);
                JSONArray newBranchArray = obj.getJSONArray("Visit_NewBranchData");
                for (int j = 0; j < newBranchArray.length(); j++) {
                    ChangeBranchTbl changeBranchTbl = new ChangeBranchTbl();
                    JSONObject branch = newBranchArray.getJSONObject(j);
                    if (branch.optInt("ActionType") == 2 || branch.optInt("ActionType") == 3) {
                        changeBranchTbl.ActionType = branch.optInt("ActionType");
                        changeBranchTbl.VisitDur = obj.optInt("VisitDur");
                        changeBranchTbl.RequestCode = branch.optLong("RequestCode");
                        changeBranchTbl.BranchCode = branch.optLong("BranchCode");
                        changeBranchTbl.BranchSrl = branch.optLong("BranchSrl");
                        changeBranchTbl.FabrikNumber = branch.optString("FabrikNumber");
                        changeBranchTbl.OldPhs = branch.optInt("MapOldPhs");
                        changeBranchTbl.OldAmp = branch.optInt("MapOldAmp");
                        changeBranchTbl.OldPwrcnt = branch.optInt("OldPwrCnt");
                        changeBranchTbl.OldtrfHcode = branch.optInt("MapOldTrfHCode");
                        changeBranchTbl.OldVoltcode = branch.optInt("MapOldVoltCode");
                        changeBranchTbl.OldBranchTypeCode = branch.optInt("OldBranchTypeCode");
                        changeBranchTbl.OldTrfType = branch.optInt("MapOldTrfType");
                        changeBranchTbl.OldPwrIcn = branch.optInt("OldPwrIcn");
                        changeBranchTbl.PwrIcn = branch.optInt("PwrIcn");
                        changeBranchTbl.Phs = branch.optInt("MapPhs");
                        changeBranchTbl.Amp = branch.optInt("MapAmp");
                        changeBranchTbl.Pwrcnt = branch.optInt("PwrCnt");
                        changeBranchTbl.TrfHcode = branch.optInt("MapTrfHCode");
                        changeBranchTbl.VoltCode = branch.optInt("MapVoltCode");
                        changeBranchTbl.RequestActionType = branch.optInt("RequestActionType");
                        changeBranchTbl.BranchTypeCode = branch.optInt("BranchTypeCode");
                        changeBranchTbl.TrfType = branch.optInt("MapTrfType");
                        changeBranchTbl.PwrIcn = branch.optInt("PwrIcn");
                        changeBranchTbl.HaveChangePlace = branch.optBoolean("HaveChangePlace");
                        changeBranchTbl.HaveChangeMeter = branch.optBoolean("NeededChange");
                        changeBranchTbl.save();


                    }
                }


            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return null;
    }


    public String saveLocation(JSONArray array) {

        List<CartableTbl> listcartable = new ArrayList<>();
        listcartable.addAll(CartableTbl.find(CartableTbl.class, "Status = ?", "0"));
        for (CartableTbl c : listcartable) {
            LocationTbl.deleteAll(LocationTbl.class, "Request_Code = ?", String.valueOf(c.RequestCode));
        }
        LocationTbl.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_location + "'");
        try {


            for (int i = 0; i < array.length(); i++) {
                LocationTbl location = new LocationTbl();
                JSONObject obj = array.getJSONObject(i);
                location.AreaStatus = obj.optInt("MapAreaCode");
                location.RequestCode = obj.optLong("RequestCode");
                location.VisitDur = obj.optInt("VisitDur");
                location.VisitDate = obj.optString("VisitDate");
                location.TotalArea = obj.optInt("TotalRequestedSquare");
                location.BaseArea = obj.optInt("RequestedSquare");
                location.AreaStatus = obj.optInt("AreaCode");
                location.FloorCount = obj.optString("FloorCount");
                location.UnitCount = obj.optString("UnitinFloor");
                location.BuildingStatus = obj.optInt("BuildingStatus");
                location.RgnCode = obj.optInt("RgnCode");
                location.CityCode = obj.optInt("CityCode");
                location.VillageCode = obj.optInt("VillageCode");
                location.LastX = obj.optInt("latituex");
                location.LastY = obj.optInt("longitudey");
                location.FamilyCount = obj.optInt("FmlNum");
                location.IsVillage = obj.optBoolean("IsRural");
                location.NextRightFileNo = obj.optLong("BranchMojaverRight");
                location.NextLeftFileNo = obj.optLong("BranchMojaverLeft");
                location.NextRightShe = obj.optLong("MojaverIDRight");
                location.NextLeftShn = obj.optLong("MojaverIDLeft");
                location.Industrialcity = obj.optInt("MapBranchTownCode");
                location.GeoDir = obj.optString("MapGeoDir");


                boolean isthere = false;
                List<CartableTbl> list = new ArrayList<>();
                list.addAll(CartableTbl.listAll(CartableTbl.class));
                for (CartableTbl savedlocation : list) {
                    if (savedlocation.RequestCode == location.RequestCode && savedlocation.Status != 0) {
                        isthere = true;
                        break;
                    } else {
                        isthere = false;
                    }

                }

                if (isthere == false) {

                    location.save();
                }

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public String saveCartable(JSONArray array) {

        CartableTbl.deleteAll(CartableTbl.class, "Status = ?", "0");
        CartableTbl.deleteAll(CartableTbl.class, "Status = ?", "3");
        CartableTbl.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_Cartable + "'");


        try {

            for (int i = 0; i < array.length(); i++) {
                CartableTbl cartableTbl = new CartableTbl();
                JSONObject obj = array.getJSONObject(i);
                cartableTbl.RequestCode = obj.optLong("RequestCode");
                cartableTbl.RequestDate = obj.optString("RequestDate");
                cartableTbl.Name = obj.optString("FirstName") + " " + obj.optString("LastName");
                cartableTbl.FirstName = obj.optString("FirstName");
                cartableTbl.LastName = obj.optString("LastName");
                cartableTbl.VisitDur = obj.optInt("VisitDur");
                cartableTbl.Address = obj.optString("Address");
                cartableTbl.MobileNo = obj.optString("MobileNo");
                cartableTbl.FixedTel = obj.optString("FixedTel");
                ArrayList<CartableTbl> cartableList = new ArrayList<CartableTbl>();
                cartableList.addAll(CartableTbl.listAll(CartableTbl.class));

                // is there cartable case added before or not


                boolean isThere = false;
                for (CartableTbl savedcartable : cartableList) {
                    if (savedcartable.RequestCode == cartableTbl.RequestCode && savedcartable.Status == 0) {
                        isThere = false;
                        cartableTbl.Status = 0;

                    }
                    if (savedcartable.RequestCode == cartableTbl.RequestCode && savedcartable.Status == 1) {
                        isThere = true;
                        cartableTbl.Status = 1;
                    }
                    if (savedcartable.RequestCode != cartableTbl.RequestCode && savedcartable.Status == 2) {
                        isThere = true;
                        cartableTbl.Status = 3;
                    }
                    if (savedcartable.RequestCode == cartableTbl.RequestCode && savedcartable.Status == 2) {
                        isThere = true;
                        cartableTbl.Status = 1;
                    }
                    if (savedcartable.RequestCode == cartableTbl.RequestCode && savedcartable.Status == 9) {
                        isThere = true;
                        cartableTbl.Status = 9;
                    }
                }
                if (isThere == false) {

                    cartableTbl.save();
                    // only save one time per each request number
                    data.add(cartableTbl);
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        CartableTbl tbl = new CartableTbl();
        List<CartableTbl> list = tbl.listAll(CartableTbl.class);
        return " رکورد با موفقیت دریافت شد" + list.size();


    }

    /**
     * @throws JSONException save new branch list of cartable request number
     */
    private void saveNewBranch(JSONArray array) {

        NewBranchTbl.deleteAll(NewBranchTbl.class);
        NewBranchTbl.executeQuery("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + Table_NewBranch + "'");

        try {

            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);
                JSONArray newBranchArray = obj.getJSONArray("Visit_NewBranchData");
                for (int j = 0; j < newBranchArray.length(); j++) {
                    NewBranchTbl newBranchTbl = new NewBranchTbl();
                    JSONObject branch = newBranchArray.getJSONObject(j);
                    if (branch.optInt("ActionType") == 1) {
                        newBranchTbl.ActionType = branch.optInt("ActionType");
                        newBranchTbl.VisitDur = obj.optInt("VisitDur");
                        newBranchTbl.Request_Code = branch.optLong("RequestCode");
                        newBranchTbl.Branch_Code = branch.optLong("BranchCode");
                        newBranchTbl.Phs = branch.optInt("MapPhs");
                        newBranchTbl.Amp = branch.optInt("MapAmp");
                        newBranchTbl.Pwrcnt = branch.optInt("PwrCnt");
                        newBranchTbl.TrfHcode = branch.optInt("MapTrfHCode");
                        newBranchTbl.VoltCode = branch.optInt("MapVoltCode");
                        newBranchTbl.Count = branch.optInt("Count");
                        newBranchTbl.RequestActionType = branch.optInt("RequestActionType");
                        newBranchTbl.BranchTypeCode = branch.optInt("BranchTypeCode");
                        newBranchTbl.TrfType = branch.optInt("MapTrfType");
                        newBranchTbl.PwrIcn = branch.optInt("PwrIcn");
                        newBranchTbl.save();


                    }
                }


            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

    /**
     * web service call is instance of async class
     * there is a progress dialog that you can use it or not
     * progress dialog is used to show a dialog progress to user when receiving tempData process is completing
     */
    class ReceiveSoapWebService extends AsyncTask<String, Void, String> {

        //public CartableListCustomAdapter requestedCartableListAdapter;
        //private ProgressDialog progressDialog = new ProgressDialog(getActivity());

        public ReceiveSoapWebService(CartableListCustomAdapter requestedCartableListAdapter) {
            //this.requestedCartableListAdapter = requestedCartableListAdapter;
        }

        /**
         * any thing you need to be done before service call running
         */
        @Override
        protected void onPreExecute() {
            /*
            progressDialog.setMessage(getResources().getString(R.string.dialog_receive_data));
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    ReceiveSoapWebService.this.cancel(true);
                }
            });
            */
        }

        /**
         * any thing you need to be done after service call running , such as canceling progress dialog
         */
        @Override
        protected void onPostExecute(String temp) {
            //this.progressDialog.dismiss();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Now we call setRefreshing(false) to signal refresh has finished
                    if (mSwipeRefresh.isRefreshing())
                        mSwipeRefresh.setRefreshing(false);
                    mListAdapter.getRequestedCartableItemsList().clear();
                    mListAdapter.getRequestedCartableItemsList().addAll(CartableTbl.listAll(CartableTbl.class));
                    mListAdapter.notifyDataSetChanged();
                    ((MainActivity) getActivity()).mDrawerAdapter.notifyDataSetChanged();
                }
            });
        }

        /**
         * @param urls first we need to create Soap Object with namespace and method name that we created before
         *             every properties that server need you can add to this object
         *             then you should serialize your request and make it ready to send through http call
         *             response is a soap object and need to convert any property of that to soap primitive
         *             soap primitive is a jason object you need
         * @return
         */
        @Override
        protected String doInBackground(String... urls) {
            SoapObject request = new SoapObject(NAMESPACE, RECEIVE_METHOD_NAME);
            request.addProperty("userName", username);
            request.addProperty("token", token);
            //request.addProperty("userId", userID);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(RECEIVE_SOAP_ACTION, envelope);

                SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();
                SoapPrimitive sp;
                for (int i = 0; i < resultsRequestSOAP.getPropertyCount(); i++) {
                    sp = (SoapPrimitive) resultsRequestSOAP.getProperty(i);
                    JSONObject jo = new JSONObject(sp.toString());
                    //joParser(jo);
                }
                return resultsRequestSOAP.toString();
            } catch (Exception exception) {
                e = new ShowExceptions(exception.getMessage());
            }
            return null;
        }


        /**
         *
         * @param changeBranchDB
         * correct change branch tempData to save in local DB
         * request is used to determine whether branch separation or merge or not
         */


        // *************************************  send Section

        /**
         * create send button click listener
         * first we make service url ready to call, if url did not make button does not work
         * when user push send button, the list of new cartable send to server
         * this procedure done by calling async class (soap web service)
         * old ones remain the same
         */


        public void selectAllVisitedCartable(ListView listView) {
            for (CartableTbl mCartable : data) {
                if (mCartable.Status == 1) {
                    int position = ((CartableListCustomAdapter) listView.getAdapter()).getItem(mCartable.RequestCode);
                    if (!listView.isItemChecked(position))
                        listView.setItemChecked(position, true);
                }
            }

            //If there is no Item To send Close Action Mode with a Message
            if (listView.getCheckedItemCount() < 1) {
                //Close Action Mode (Not Needed)
                if (mActionModeUpload != null)
                    mActionModeUpload.finish();

                //Set Choice Mode Listener to Option Mode
                mListView.setMultiChoiceModeListener(new optionMode());

                //Prompt a Message
                final Snackbar mSnackBar = Snackbar.make(rootView, "There is no valid Cartable to upload!", Snackbar.LENGTH_INDEFINITE);
                mSnackBar.setAction("Done", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //mSnackBar.dismiss();
                    }
                }).show();
            }
        }


    }


    //Inner Classes
    class uploadMode implements AbsListView.MultiChoiceModeListener {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            CartableTbl mCartable = ((CartableListCustomAdapter) mListView.getAdapter()).getItem(position);

            if (checked) {
                //Check To See if the Cartable is valid
                if (mCartable.Status != 1) {
                    mCount++;  //You Need to Increase counter so when the else trigger count would stay unchanged
                    Toast.makeText(getActivity(), getResources().getString(R.string.cartable_cannot_send), Toast.LENGTH_LONG).show();
                    mListView.setItemChecked(position, false);
                } else {
                    mCount++;
                }
            } else {
                mCount--;
            }
            mode.setTitle(mCount + " پرونده انتخاب شده است");
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.cartable_list_upload, menu);
            mCount = 0;
            mActionModeUpload = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_upload_all:
                    Toast.makeText(getActivity(), "Upload All", Toast.LENGTH_SHORT).show();
                    // selectAllVisitedCartable(mListView);
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionModeUpload = null;
            mListView.setMultiChoiceModeListener(new optionMode());
        }
    }

    class optionMode implements AbsListView.MultiChoiceModeListener {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            CartableTbl mCartable = ((CartableListCustomAdapter) mListView.getAdapter()).getItem(position);
            if (checked) {
                //Check To See if the Cartable is uploaded
                if (mCartable.Status == 2) {
                    mCount++;  //You Need to Increase counter so when the else trigger count would stay unchanged
                    Toast.makeText(getActivity(), getResources().getString(R.string.cartable_cannot_change), Toast.LENGTH_LONG).show();
                    mListView.setItemChecked(position, false);
                } else {
                    mCount++;
                }
            } else {
                mCount--;
            }
            mode.setTitle(mCount + " پرونده انتخاب شده است");
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.cartable_list_option, menu);
            mCount = 0;
            mActionModeOption = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @TargetApi(17)
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_restore:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                            .setTitle(getResources().getString(R.string.dialog_upload_title))
                            .setMessage(R.string.dialog_upload_title)
                            .setNegativeButton(R.string.dialog_button_return, null)
                            .setPositiveButton(R.string.dialog_button_upload, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    Toast.makeText(getActivity(), "Restoring!", Toast.LENGTH_SHORT).show();
                                    for (int i = 0; i < mListAdapter.getCount(); i++)
                                        if (mListView.isItemChecked(i)) {
                                            mListAdapter.getItem(i).delete();
                                        }
                                    receiveCartable();
                                    mActionModeOption.finish();
                                    ((MainActivity) getActivity()).mDrawerAdapter.notifyDataSetChanged();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    //Set Title Gravity
                    final int alertTitle = getActivity().getResources().getIdentifier("alertTitle", "id", "android");
                    TextView messageText = (TextView) dialog.findViewById(alertTitle);
                    //messageText.setGravity(Gravity.Right);
                    messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

                    return true;
                case R.id.menu_unvisit:
                    for (int i = 0; i < mListAdapter.getCount(); i++)
                        if (mListView.isItemChecked(i)) {
                            mListAdapter.getItem(i).Status = 0;
                            mListAdapter.getItem(i).save();
                            ((MainActivity) getActivity()).mDrawerAdapter.notifyDataSetChanged();
                        }

                    Toast.makeText(getActivity(), "UnVisiting!", Toast.LENGTH_SHORT).show();

                    mActionModeOption.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionModeOption = null;
        }
    }

    public void setCounters() {
        total = 0;
        seen = 0;
        notSeen = 0;
        sent = 0;



        for (CartableTbl mCartable : mCartableList) {

            if (mCartable.Status == 0 && mCartable.Status == 1 && mCartable.Status == 2 && mCartable.Status == 9);
                 total++;


            if (mCartable.Status == 1)
                seen++;


            if (mCartable.Status == 2)
                sent++;

            if (mCartable.Status == 0)
                notSeen++;

        }
    }


    class SendSoapWebService extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog = new ProgressDialog(getActivity());
        public JSONArray requestedCartableList;

        public SendSoapWebService(JSONArray requestedCartableList) {
            this.requestedCartableList = requestedCartableList;

        }

        @Override
        protected void onPreExecute() {
            try {
                progressDialog.setMessage(getResources().getString(R.string.Downloading_data));
                progressDialog.show();
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface arg0) {
                        SendSoapWebService.this.cancel(true);
                    }
                });
            } catch (Exception e) {

            }
        }

        @Override
        protected void onPostExecute(String temp) {
            this.progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... urls) {
            SoapObject request = new SoapObject(NAMESPACE, SEND_METHOD_NAME);
            request.addProperty("userName", username);
            request.addProperty("token", token);
            request.addProperty("userId", userID);
            request.addProperty("cartableViewList", requestedCartableList.toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

            try {
                androidHttpTransport.call(SEND_SOAP_ACTION, envelope);
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                SoapPrimitive sp = null;
                for (int i = 0; i < resultsRequestSOAP.getPropertyCount(); i++) {
                    sp = (SoapPrimitive) resultsRequestSOAP.getProperty(i);
                }
                JSONArray ja = new JSONArray(sp.toString());
                String obj = null;
                for (int i = 0; i < ja.length(); i++) {
                    obj = ja.get(i).toString();
                }
                return obj;
            } catch (IOException e1) {
                e = new ShowExceptions(e1.getMessage());
            } catch (XmlPullParserException e1) {
                e = new ShowExceptions(e1.getMessage());
            } catch (JSONException e1) {
                e1.printStackTrace();
            } catch (Exception e1) {
                e = new ShowExceptions("خطایی در سرور رخ داده" + "\r\n" + envelope.bodyIn.toString());
            }

            return null;
        }

    }
}

