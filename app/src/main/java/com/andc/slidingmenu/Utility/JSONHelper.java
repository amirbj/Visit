package com.andc.slidingmenu.Utility;

import com.andc.slidingmenu.Main.MainActivity;
import com.andc.slidingmenu.Modals.Observe_Activity;
import com.andc.slidingmenu.Modals.Observe_List;
import com.andc.slidingmenu.Modals.Observe_List_Item;
import com.andc.slidingmenu.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONHelper {
    public static String convertObserveListToJsonString(ArrayList<Observe_List_Item> list){
        String rv;
        JSONArray jArray = new JSONArray();
        for (int i=0; i<list.size();i++){
            JSONObject jsonObj = new JSONObject();
            if (list.get(i).Value==null) list.get(i).Value = "";
            if (list.get(i).Desc==null) list.get(i).Desc = "";
            if ((list.get(i).ObserveType > 0) && (list.get(i).Value.equals("")))
                continue;
            try {
                jsonObj.put("id", list.get(i).Id);
                jsonObj.put("m_code", list.get(i).GroupId);
                jsonObj.put("value", list.get(i).Value);
                jsonObj.put("desc", list.get(i).Desc);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jArray.put(jsonObj);
        }
        rv = jArray.toString();
        return rv;
    }

  /*  public static ArrayList<Observe_List_Item> convertJsonStringToObserveList(String strJSon){
        ArrayList<Observe_List_Item> rv = new ArrayList<Observe_List_Item>();
        try {
            if (strJSon == null) strJSon = "";
            JSONArray jsArray = new JSONArray(strJSon);
            for(int i = 0; i < jsArray.length(); i++)
            {
                JSONObject jsonObj = jsArray.getJSONObject(i);

                Observe_List_Item oli = new Observe_List_Item();
                try { oli.Id = jsonObj.getLong("id");} catch(Exception e0) {oli.Id=-1;}
                try { oli.GroupId = jsonObj.getLong("m_code");} catch(Exception e1) {oli.GroupId=-1;}
                try { oli.Value = jsonObj.getString("value");} catch(Exception e2) {oli.Value="";}
                try { oli.Desc = jsonObj.getString("desc");} catch(Exception e3) {oli.Desc="";}
                rv.add(oli);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rv;
    }
*/
    public static ArrayList<Observe_List> ObserveListJsonParse(String strJSon){
        ArrayList<Observe_List> rv = null;
        try {
            JSONArray jsArray = new JSONArray(strJSon);
            rv = new ArrayList<Observe_List>();
            for(int i = 0; i < jsArray.length(); i++)
            {
                JSONObject jsonObj = jsArray.getJSONObject(i);

                Observe_List ol = new Observe_List();
                ol.groupId = jsonObj.getLong(MainActivity.context.getResources().getString(R.string.jsn_mainCode));
                //ol.itemId = jsonObj.getLong(MainActivity.context.getResources().getString(R.string.jsn_SubCode1));
                ol.itemId = jsonObj.getLong(MainActivity.context.getResources().getString(R.string.jsn_observeCode));
                ol.title = jsonObj.getString(MainActivity.context.getResources().getString(R.string.jsn_desc));
                //ol.observeType = jsonObj.getInt(MainActivity.context.getResources().getString(R.string.jsn_ObserveType));
                ol.observeType = jsonObj.getInt(MainActivity.context.getResources().getString(R.string.jsn_templateType));
                ol.dataType = jsonObj.getString(MainActivity.context.getResources().getString(R.string.jsn_DataType));
                rv.add(ol);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rv;
    }

    public static ArrayList<Observe_Activity> ObserveActivityJsonParse(String strJSon){
        ArrayList<Observe_Activity> rv = null;
        try {
            JSONArray jsArray = new JSONArray(strJSon);
            rv = new ArrayList<Observe_Activity>();
            for(int i = 0; i < jsArray.length(); i++)
            {
                JSONObject jsonObj = jsArray.getJSONObject(i);

                Observe_Activity ba = new Observe_Activity();
                ba.Id = jsonObj.getLong(MainActivity.context.getResources().getString(R.string.jsn_Code));
                ba.Title = jsonObj.getString(MainActivity.context.getResources().getString(R.string.jsn_Title));
                rv.add(ba);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rv;
    }
/*
    public static ArrayList<ObserveActivityTbl> BranchActivityJsonParse(String strJSon)
    {
        ArrayList<ObserveActivityTbl> rv = null;
        try {
            JSONArray jsArray = new JSONArray(strJSon);
            rv = new ArrayList<ObserveActivityTbl>();
            for(int i = 0; i < jsArray.length(); i++)
            {
                JSONObject jsonObj = jsArray.getJSONObject(i);

                ObserveActivityTbl ba = new ObserveActivityTbl();
                ba.Id = jsonObj.getLong(MainActivity.context.getResources().getString(R.string.jsn_Code));
                ba.Title = jsonObj.getString(MainActivity.context.getResources().getString(R.string.jsn_Title));
                rv.add(ba);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rv;
    }
*/
}
