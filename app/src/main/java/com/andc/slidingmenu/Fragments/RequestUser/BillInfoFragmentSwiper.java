package com.andc.slidingmenu.Fragments.RequestUser;
/*
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.andc.slidingmenu.R;
import com.andc.slidingmenu.ExceptionViewer.ShowExceptions;
import com.andc.slidingmenu.adapter.CorrectSaleRecordsCustomAdapter;
import com.andc.slidingmenu.adapter.RetrieveRecordsCustomAdapter;
import com.andc.slidingmenu.Modals.CorrectSaleRecords;
import com.andc.slidingmenu.Modals.RetrieveRecords;


public class BillInfoFragmentSwiper extends Fragment {

    public String username;
    public String token;
    public String branchCode;
    public String shenaseGhabz;
    public String fabrikNumber;
    public ShowExceptions e = new ShowExceptions();

    private static final String NAMESPACE = "http://tempuri.org/";
    private static String URL;
    private static final String SOAP_ACTION = "http://tempuri.org/GetAllDataofBranch";
    private static final String METHOD_NAME = "GetAllDataofBranch";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_swiper_bill_info, container, false);
        Bundle bundle = getArguments();
        this.branchCode = bundle.getString("branchCode");
        this.shenaseGhabz = bundle.getString("shenaseGhabz");
        this.fabrikNumber = bundle.getString("fabrikNumber");
        this.username = bundle.getString("username");
        this.token = bundle.getString("token");

        MyPagerAdapter adapter = new MyPagerAdapter();
        ViewPager pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0); //set the opening screen

        if(e.getMessages()!=null)
            e.alertUser(getActivity());
        return rootView;
    }

    public class MyPagerAdapter extends PagerAdapter {

        ArrayList<CorrectSaleRecordsDB> correctSaleRecords = new ArrayList<CorrectSaleRecordsDB>();
        ArrayList<RetrieveRecordsDB> retrieveRecords = new ArrayList<RetrieveRecordsDB>();


        @Override
        public int getCount() {
            return 4; //set  number of swipe screens here
        }
        @Override
        public Object instantiateItem(final View collection, final int position) {
            LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            PrepareCorrectSaleList();
            PrepareRetrieveRecordsList();
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.layout.fragment_bill_info_list1_monitor; //set which layout will show on load
                    break;
                case 1:
                    resId = R.layout.fragment_bill_info_list2_sale; //what layout swiping shows
                    break;
                case 2:
                    resId = R.layout.fragment_bill_info_list3_retrieve; //what layout swiping shows
                    break;
                case 3:
                    resId = R.layout.fragment_bill_info_list4_accounting; //what layout swiping shows
            }
            View view = inflater.inflate(resId, null);
            ((ViewPager) collection).addView(view,0);

            if(resId == R.layout.fragment_bill_info_list1_monitor){
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String webServicesAddress = preferences.getString("webServicesAddress", "");
                String port = preferences.getString("port","");
                if(!port.equalsIgnoreCase("") && !webServicesAddress.equalsIgnoreCase("") ){
                    URL = String.format("http://%s/SearchService.asmx",webServicesAddress+":"+port);
                }
                try {
                    String result = new callSoapWebService().execute().get();
                    JSONObject jo = new JSONObject(result);
                    FillTable(view, jo);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(resId == R.layout.fragment_bill_info_list2_sale)
                prepareTabs(position, view);
            else if(resId == R.layout.fragment_bill_info_list3_retrieve)
                PrepareRetrieveRecords(position,view);

            return view;
        }

        //   ******************   Page 1
        private void FillTable(View view, JSONObject jo) {
            try {
                if(!String.valueOf(jo.get("BillId")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.Bill_ident)).setText(String.valueOf(jo.get("BillId")));
                if(!String.valueOf(jo.get("OwnerType")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.sub_type)).setText(String.valueOf(jo.get("OwnerType")));
                if(!String.valueOf(jo.get("BranchStatCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.sub_status)).setText(String.valueOf(jo.get("BranchStatCode")));
                if(!String.valueOf(jo.get("BranchTypeCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.counter_type)).setText(String.valueOf(jo.get("BranchTypeCode")));
                if(!String.valueOf(jo.get("GeoAreaCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.zone_code)).setText(String.valueOf(jo.get("GeoAreaCode")));

                if(!String.valueOf(jo.get("TrfHCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.code_tariff)).setText(String.valueOf(jo.get("TrfHCode")));
                if(!String.valueOf(jo.get("SelCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.option)).setText(String.valueOf(jo.get("SelCode")));
                if(!String.valueOf(jo.get("FmlCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.family_code)).setText(String.valueOf(jo.get("FmlCode")));
                if(!String.valueOf(jo.get("Phs")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.phase)).setText(String.valueOf(jo.get("Phs")));
                if(!String.valueOf(jo.get("Amp")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.amper)).setText(String.valueOf(jo.get("Amp")));
                if(!String.valueOf(jo.get("Demand")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.contract_demand)).setText(String.valueOf(jo.get("Demand")));
                if(!String.valueOf(jo.get("VoltCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.voltage_code)).setText(String.valueOf(jo.get("VoltCode")));
                if(!String.valueOf(jo.get("DurationType")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.work_stream)).setText(String.valueOf(jo.get("DurationType")));
                if(!String.valueOf(jo.get("OrgCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.institution_code)).setText(String.valueOf(jo.get("OrgCode")));
                if(!String.valueOf(jo.get("ActTypeCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.activity_code)).setText(String.valueOf(jo.get("ActTypeCode")));
                if(!String.valueOf(jo.get("PostType")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.postal_type)).setText(String.valueOf(jo.get("PostType")));
                if(!String.valueOf(jo.get("PresureCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.atmosphere_code)).setText(String.valueOf(jo.get("PresureCode")));
                if(!String.valueOf(jo.get("PwrIcn")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.authorization_allowed_demand)).setText(String.valueOf(jo.get("PwrIcn")));
                if(!String.valueOf(jo.get("ServiceCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.service_line_code)).setText(String.valueOf(jo.get("ServiceCode")));
                if(!String.valueOf(jo.get("BranchCntractDate")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.contract_date)).setText(String.valueOf(jo.get("BranchCntractDate")));
                if(!String.valueOf(jo.get("BranchCreateDate")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.establish_date)).setText(String.valueOf(jo.get("BranchCreateDate")));
                if(!String.valueOf(jo.get("PwrIcnExpDate")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.authorization_expirement_date)).setText(String.valueOf(jo.get("PwrIcnExpDate")));

//                ((TextView)view.findViewById(R.id.temporary_dec_expirement_date)).setText(String.valueOf(jo.get("TmpDecStartDate")));

                if(!String.valueOf(jo.get("PwrCnt")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.main_power)).setText(String.valueOf(jo.get("PwrCnt")));
                if(!String.valueOf(jo.get("TrfCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.alpha_tariff)).setText(String.valueOf(jo.get("TrfCode")));
                if(!String.valueOf(jo.get("CustomerStatus")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.misscount_code)).setText(String.valueOf(jo.get("CustomerStatus")));

                if(!String.valueOf(jo.get("OwnerName")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.sub_name)).setText(String.valueOf(jo.get("OwnerName")));
                if(!String.valueOf(jo.get("FatherName")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.father_name)).setText(String.valueOf(jo.get("FatherName")));
                if(!String.valueOf(jo.get("ShomareShenasname")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.Ident_num)).setText(String.valueOf(jo.get("ShomareShenasname")));
                if(!String.valueOf(jo.get("FixedTel")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.number)).setText(String.valueOf(jo.get("FixedTel")));
                if(!String.valueOf(jo.get("OwnerNatCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.ID_code)).setText(String.valueOf(jo.get("OwnerNatCode")));
                if(!String.valueOf(jo.get("IssuedFrom")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.birth_city)).setText(String.valueOf(jo.get("IssuedFrom")));
                if(!String.valueOf(jo.get("Email")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.email)).setText(String.valueOf(jo.get("Email")));
                if(!String.valueOf(jo.get("SpecialCode")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.promotion_code)).setText(String.valueOf(jo.get("SpecialCode")));
                if(!String.valueOf(jo.get("OwnerType")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.subs_type)).setText(String.valueOf(jo.get("OwnerType")));
                if(!String.valueOf(jo.get("MobileNo")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.phone_num)).setText(String.valueOf(jo.get("MobileNo")));
                if(!String.valueOf(jo.get("PoNum")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.zip_code)).setText(String.valueOf(jo.get("PoNum")));
                if(!String.valueOf(jo.get("Adress")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.address)).setText(String.valueOf(jo.get("Adress")));

                if(!String.valueOf(jo.get("LastReadDate")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.latest_visit_date)).setText(String.valueOf(jo.get("LastReadDate")));
                if(!String.valueOf(jo.get("PrdAmt")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.latest_sell_price)).setText(String.valueOf(jo.get("PrdAmt")));
                if(!String.valueOf(jo.get("RcptDateTime")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.latest_receive_date)).setText(String.valueOf(jo.get("RcptDateTime")));
                if(!String.valueOf(jo.get("RcptAmt")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.latest_receive_price)).setText(String.valueOf(jo.get("RcptAmt")));

                if(!String.valueOf(jo.get("CrDbTot")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.energy_debt)).setText(String.valueOf(jo.get("CrDbTot")));
                if(!String.valueOf(jo.get("EnergyDebitUnLimit")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.energy_deadline_debt)).setText(String.valueOf(jo.get("EnergyDebitUnLimit")));
                if(!String.valueOf(jo.get("EnergyDebitlimit")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.energy_non_deadline_debt)).setText(String.valueOf(jo.get("EnergyDebitlimit")));

                if(!String.valueOf(jo.get("CrDBBranchSale")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.branch_debt)).setText(String.valueOf(jo.get("CrDBBranchSale")));
                if(!String.valueOf(jo.get("BranchDebitUnLimit")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.branch_deadline_debt)).setText(String.valueOf(jo.get("BranchDebitUnLimit")));
                if(!String.valueOf(jo.get("BranchDebitLimit")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.branch_non_deadline_debt)).setText(String.valueOf(jo.get("BranchDebitLimit")));

                if(!String.valueOf(jo.get("CrDbOtherCost")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.other_debt)).setText(String.valueOf(jo.get("CrDbOtherCost")));
                if(!String.valueOf(jo.get("OtherDebitUnLimit")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.other_deadline_debt)).setText(String.valueOf(jo.get("OtherDebitUnLimit")));
                if(!String.valueOf(jo.get("OtherDebitLimit")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.other_non_deadline_debt)).setText(String.valueOf(jo.get("OtherDebitLimit")));

                if(!String.valueOf(jo.get("CrDbTotalCost")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.aggregate_debt)).setText(String.valueOf(jo.get("CrDbTotalCost")));
                if(!String.valueOf(jo.get("TotalDebitUnLimit")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.aggregate_deadline_debt)).setText(String.valueOf(jo.get("TotalDebitUnLimit")));
                if(!String.valueOf(jo.get("totalDebitLimit")).equalsIgnoreCase("null"))
                    ((TextView)view.findViewById(R.id.aggregate_non_deadline_debt)).setText(String.valueOf(jo.get("totalDebitLimit")));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //   ******************   Page 2
        private void prepareTabs(int position,View view) {

            TabHost host = (TabHost) view.findViewById(R.id.tab_host);
            host.setup();

            TabHost.TabSpec spec = host.newTabSpec(getResources().getString(R.string.correct_sale));
            spec.setContent(R.id.correct_sale);
            spec.setIndicator(getResources().getString(R.string.correct_sale));
            host.addTab(spec);

            spec = host.newTabSpec(getResources().getString(R.string.incorrect_sale));
            spec.setContent(R.id.incorrect_sale);
            spec.setIndicator(getResources().getString(R.string.incorrect_sale));
            host.addTab(spec);

            spec = host.newTabSpec(getResources().getString(R.string.current_sale));
            spec.setContent(R.id.current_sale);
            spec.setIndicator(getResources().getString(R.string.current_sale));
            host.addTab(spec);

            spec = host.newTabSpec(getResources().getString(R.string.not_exported_sale));
            spec.setContent(R.id.not_exported_sale);
            spec.setIndicator(getResources().getString(R.string.not_exported_sale));
            host.addTab(spec);

            spec = host.newTabSpec(getResources().getString(R.string.consumption_diagram));
            spec.setContent(R.id.consumption_diagram);
            spec.setIndicator(getResources().getString(R.string.consumption_diagram));
            host.addTab(spec);

            PrepareCorrectSalesRecords(position, view);
        }

        private void PrepareCorrectSalesRecords(int position,View view) {
            ListView listView = (ListView) view.findViewById(R.id.correct_sale_records);
            CorrectSaleRecordsCustomAdapter correctSaleAdapter = new CorrectSaleRecordsCustomAdapter();
            listView.setAdapter(correctSaleAdapter);

            ArrayList<CorrectSaleRecords> correctSaleRecordsListAdapter = correctSaleAdapter.getCorrectSaleRecordsList();
            for(CorrectSaleRecordsDB temp: correctSaleRecords){
                CorrectSaleRecords csr = new CorrectSaleRecords(temp.updateTime, temp.currentMonitoringTime,
                        temp.activeUsage, temp.reactiveUsage, temp.salePrice);
                correctSaleRecordsListAdapter.add(csr);
            }
            correctSaleAdapter.notifyDataSetChanged();

        }

        private void PrepareCorrectSaleList() {
            correctSaleRecords.addAll(CorrectSaleRecordsDB.listAll(CorrectSaleRecordsDB.class));
            if(correctSaleRecords.size()<=0) {
                for (int i = 0; i < 100; i++) {
                    CorrectSaleRecordsDB item = new CorrectSaleRecordsDB(i, 2 * i, 3 * i, 4*i, 5*i);
                    item.save();
                }
            }
        }

        //   ******************   Page 3
        private void PrepareRetrieveRecords(int position,View view){
            ListView listView = (ListView) view.findViewById(R.id.retrieve_records);
            RetrieveRecordsCustomAdapter retrieveRecordsAdapter = new RetrieveRecordsCustomAdapter();
            listView.setAdapter(retrieveRecordsAdapter);

            ArrayList<RetrieveRecords> retrieveRecordsListAdapter = retrieveRecordsAdapter.getRetrieveRecordsList();
            for(RetrieveRecordsDB temp: retrieveRecords){
                RetrieveRecords rr = new RetrieveRecords(temp.updateTime, temp.retrievePrice,
                        temp.bankName, temp.retrieveTime, temp.period);
                retrieveRecordsListAdapter.add(rr);
            }
            retrieveRecordsAdapter.notifyDataSetChanged();
        }

        private void PrepareRetrieveRecordsList() {
            retrieveRecords.addAll(RetrieveRecordsDB.listAll(RetrieveRecordsDB.class));
            if(retrieveRecords.size()<=0) {
                for (int i = 0; i < 100; i++) {
                    RetrieveRecordsDB item = new RetrieveRecordsDB(i, 2 * i, 3 * i, 4*i, 5*i);
                    item.save();
                }
            }
        }

        @Override
        public void destroyItem(final View arg0, final int arg1, final Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }
        @Override
        public boolean isViewFromObject(final View arg0, final Object arg1) {
            return arg0 == ((View) arg1);
        }
    }

    class callSoapWebService extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog = new ProgressDialog(getActivity());

        protected void onPreExecute() {
            progressDialog.setMessage(getResources().getString(R.string.Downloading_data));
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    callSoapWebService.this.cancel(true);
                }
            });
        }

        protected void onPostExecute() {
                this.progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... urls) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("userName", username);
            request.addProperty("token", token);
            request.addProperty("branchCode", branchCode);
            request.addProperty("shenaseGhabz", shenaseGhabz);
            request.addProperty("fabrikNumber", fabrikNumber);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);

                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) ((SoapObject) envelope.bodyIn).getProperty(0);
                JSONObject jo = new JSONObject(resultsRequestSOAP.toString());

                onPostExecute();
                return resultsRequestSOAP.toString();

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return null;
        }
    }
}
*/