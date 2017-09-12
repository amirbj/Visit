package com.andc.slidingmenu.Utility;

import com.andc.slidingmenu.Modals.Observe_Activity;
import com.andc.slidingmenu.Modals.Observe_List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win on 6/7/2015.
 */
public class WSHelper {
    public static List<Observe_List> GetObserveList(String serverAddress) {
        ArrayList<Observe_List> observe_list = new ArrayList<Observe_List>();
        try {
            String action = "http://tempuri.org/GetHHRemarksJson";
            String url = String.format("http://%s/ACCRMHelperWebSrv.asmx", serverAddress);
            String method = "GetHHRemarksJson";
            String namespace = "http://tempuri.org/";

            SoapObject request = new SoapObject(namespace, method);
            request.addProperty("getCount", false);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.dotNet = true;
            envelope.avoidExceptionForUnknownProperty = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE ht = new HttpTransportSE(url);
            ht.debug = true;
            ht.call(action, envelope);
            Object soap = envelope.getResponse();
            observe_list = JSONHelper.ObserveListJsonParse(soap.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return observe_list;
    }

    public static List<Observe_Activity> GetBranchActivities(String serverAddress) {
        ArrayList<Observe_Activity> branch_activities = new ArrayList<Observe_Activity>();
        try {
            String action = "http://tempuri.org/GetActivityTypesJson";
            String url = String.format("http://%s/ACCRMHelperWebSrv.asmx", serverAddress);
            String method = "GetActivityTypesJson";
            String namespace = "http://tempuri.org/";

            SoapObject request = new SoapObject(namespace, method);
            request.addProperty("DoCompress", false);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.dotNet = true;
            envelope.avoidExceptionForUnknownProperty = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE ht = new HttpTransportSE(url);
            ht.debug = true;
            ht.call(action, envelope);
            Object soap = envelope.getResponse();
            String str = soap.toString();
            branch_activities = JSONHelper.ObserveActivityJsonParse(soap.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return branch_activities;
    }
}
