package com.andc.slidingmenu.Fragments.RequestUser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.andc.slidingmenu.adapter.ListViewCustomAdapter;
import com.andc.slidingmenu.Modals.CustomerInformation;

public class RequestFragment extends Fragment {

    public View rootView;
    public Animation slide;
    public ListViewCustomAdapter requestListAdapter;
    private TextView title;
    private ArrayList<CustomerInformation> data;

    private Button nextButton;
    private Button previousButton;

    public int TOTAL_LIST_ITEMS;
    public int NUM_ITEMS_PAGE = 100;
    private int noOfPages;
    private int noOfCurrentPages;
    private int firstItemOfList;

    public String username;
    public String password;
    public String branchCode;
    public String shenaseGhabz;
    public String fabrikNumber;
    public String token;
    public ShowExceptions e = new ShowExceptions();

    private static final String NAMESPACE = "http://tempuri.org/";
    private static String URL;
    private static final String SOAP_ACTION = "http://tempuri.org/GetDataofBranch";
    private static final String METHOD_NAME = "GetDataofBranch";

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_request, container, false);
        final View slider = rootView.findViewById(R.id.slider);
        slide = AnimationUtils.loadAnimation(inflater.getContext(), R.anim.slide_down);
        slider.startAnimation(slide);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.username = preferences.getString("username","");
        this.password = preferences.getString("password","");
        this.token = preferences.getString("token","");

        data = new ArrayList<CustomerInformation>();

        ListView listView = (ListView) rootView.findViewById(R.id.list);
        title = (TextView) rootView.findViewById(R.id.title);
        Button searchButton = (Button) rootView.findViewById(R.id.searchbtn);
        nextButton = (Button) rootView.findViewById(R.id.next_button);
        previousButton = (Button) rootView.findViewById(R.id.previous_button);

        requestListAdapter = new ListViewCustomAdapter();
        listView.setAdapter(requestListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("branchCode", branchCode);
                bundle.putString("shenaseGhabz", shenaseGhabz);
                bundle.putString("fabrikNumber", fabrikNumber);
                bundle.putString("username", username);
                bundle.putString("token", token);
              /*  Fragment fragment = new BillInfoFragmentSwiper();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().add(R.id.frame_container, fragment).addToBackStack(null).commit();
                fragment.setArguments(bundle); */
            }
        });

        firstItemOfList = 1;
        noOfCurrentPages = 1;

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchCode = ((TextView) rootView.findViewById(R.id.subscription_number)).getText().toString();
                shenaseGhabz = ((TextView) rootView.findViewById(R.id.bill_identification)).getText().toString();
                fabrikNumber = ((TextView) rootView.findViewById(R.id.fabrik_number)).getText().toString();
                if (!ValidateData(branchCode, shenaseGhabz, fabrikNumber)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (branchCode.isEmpty())
                    branchCode = "0";
                if (shenaseGhabz.isEmpty())
                    shenaseGhabz = "0";
                if (fabrikNumber.isEmpty())
                    fabrikNumber = "0";
                slide = AnimationUtils.loadAnimation(inflater.getContext(), R.anim.slide_up);
                slider.startAnimation(slide);
                slider.setVisibility(View.GONE);
                rootView.findViewById(R.id.result).setVisibility(View.VISIBLE);

                if(!makeURLReady())
                    return;

                try {
                    new callSoapWebService().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                CountOfPages();
                title.setText(getActivity().getApplicationContext().getString(R.string.Page) + (noOfCurrentPages) + getActivity().getApplicationContext().getString(R.string.of) + noOfPages);
                loadList(firstItemOfList);

                if (e.getMessages() != null)
                    e.alertUser(getActivity());
            }

        });
        ConfigurePagination(inflater, slider);
        return rootView;
    }

    private boolean makeURLReady() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String webServicesAddress = preferences.getString("webServicesAddress", "");
        if(webServicesAddress.isEmpty()){
            Toast toast = Toast.makeText(getActivity().getBaseContext()
                    , getResources().getString(R.string.src_invalid), Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toast.show();
            return false;
        }

        String port = preferences.getString("port","");
        if(port.isEmpty()){
            Toast toast = Toast.makeText(getActivity().getBaseContext()
                    , getResources().getString(R.string.port_invalid), Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toast.show();
            return false;
        }

        if(!port.equalsIgnoreCase("") && !webServicesAddress.equalsIgnoreCase("") ){
            URL = String.format("http://%s/SearchService.asmx",webServicesAddress+":"+port);
        }
        return true;
    }

    private boolean ValidateData(String branchCode, String shenaseGhabz, String fabrikNumber) {
        return !(branchCode.isEmpty() && shenaseGhabz.isEmpty() && fabrikNumber.isEmpty());
    }

    private void ConfigurePagination(final LayoutInflater inflater, final View slider) {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOfCurrentPages < noOfPages) {
                    firstItemOfList += NUM_ITEMS_PAGE;
                    loadList(firstItemOfList);
                    checkIsPreButtonEnabled();
                    noOfCurrentPages++;
                    title.setText(getActivity().getApplicationContext().getString(R.string.Page) +
                            (noOfCurrentPages) + getActivity().getApplicationContext().getString(R.string.of) + noOfPages);
                    IsLastPage();
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOfCurrentPages > 1) {
                    firstItemOfList -= NUM_ITEMS_PAGE;
                    loadList(firstItemOfList);
                    checkIsNextButtonEnabled();
                    noOfCurrentPages--;
                    title.setText(getActivity().getApplicationContext().getString(R.string.Page) +
                            (noOfCurrentPages) + getActivity().getApplicationContext().getString(R.string.of) + noOfPages);
                    IsFirstPage();
                }
            }
        });
    }

    private void loadList(int firstItemOfList) {
        requestListAdapter.getRequestedItemsList().clear();
        ArrayList<CustomerInformation> temp = new ArrayList<CustomerInformation>();
        for (int i = firstItemOfList; i < firstItemOfList + NUM_ITEMS_PAGE; i++) {
            if (i <= TOTAL_LIST_ITEMS)
                temp.add(data.get(i-1));
            else
                break;
        }
        for (CustomerInformation item : temp) {
            CustomerInformation customerInformation = new CustomerInformation(item.branchCode, item.billIdentification,
                    item.fabrikNumber, item.firstName, item.lastName);
            requestListAdapter.getRequestedItemsList().add(customerInformation);
        }
        requestListAdapter.notifyDataSetChanged();
    }

    private void IsFirstPage() {
        if (noOfCurrentPages == 1)
            previousButton.setVisibility(View.INVISIBLE);
    }

    private void IsLastPage() {
        if (noOfCurrentPages == noOfPages)
            nextButton.setVisibility(View.INVISIBLE);
    }

    private void checkIsPreButtonEnabled() {
        if (previousButton.getVisibility() == View.INVISIBLE)
            previousButton.setVisibility(View.VISIBLE);
    }

    private void checkIsNextButtonEnabled() {
        if (nextButton.getVisibility() == View.INVISIBLE)
            nextButton.setVisibility(View.VISIBLE);
    }

    private void CountOfPages() {
        this.TOTAL_LIST_ITEMS = data.size();
        int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
        val = val == 0 ? 0 : 1;
        noOfPages = TOTAL_LIST_ITEMS / NUM_ITEMS_PAGE + val;
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
                this.progressDialog.cancel();
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
                CustomerInformation customerInformation = new CustomerInformation();
                customerInformation.branchCode = Integer.valueOf( jo.get("BranchCode").toString() );
                customerInformation.billIdentification = Long.valueOf( jo.get("BillId").toString() );
                customerInformation.fabrikNumber = jo.get("FabrikNumber").toString();
                customerInformation.firstName = jo.get("FirstName").toString();
                customerInformation.lastName = jo.get("LastName").toString();
                data.add(customerInformation);

                onPostExecute();
                return resultsRequestSOAP.toString();

            } catch (Exception execption) {
                e = new ShowExceptions(execption.getMessage());
            }
            return null;
        }
    }
}