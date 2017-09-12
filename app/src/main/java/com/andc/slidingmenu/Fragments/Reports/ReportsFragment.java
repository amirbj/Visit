package com.andc.slidingmenu.Fragments.Reports;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import com.andc.slidingmenu.R;
import com.andc.slidingmenu.adapter.ReportsGridListCustomAdapter;
import com.andc.slidingmenu.Modals.ReportsModel;

public class ReportsFragment extends Fragment {

    public ReportsFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reports, container, false);
        ArrayList<ReportsModel> items = new ArrayList<ReportsModel>();
        //InsertItems();
        CreateItem(items);
        GridView reportGridView = (GridView)rootView.findViewById(R.id.gridview);
        ReportsGridListCustomAdapter reportsGridListCustomAdapter = new ReportsGridListCustomAdapter(items);
        reportGridView.setAdapter(reportsGridListCustomAdapter);
        reportGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new ReportInformationFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().add(R.id.frame_container, fragment).addToBackStack(null).commit();
            }
        });
        return rootView;
    }
/*
    private void InsertItems() {
        ArrayList<ReportItemDB> items = new ArrayList<ReportItemDB>();
        items.addAll(ReportItemDB.listAll(ReportItemDB.class));
        if(items.size()<=0) {
            ReportItemDB item = new ReportItemDB(1, "A", "10");
            item.save();
            item = new ReportItemDB(1, "B", "20");
            item.save();
            item = new ReportItemDB(1, "C", "30");
            item.save();
            item = new ReportItemDB(1, "D", "40");
            item.save();
        }
    } */

    private void CreateItem(ArrayList<ReportsModel> items) {
        for(int i=0;i<100;i++){
            ReportsModel temp = new ReportsModel("report " + i);
            items.add(temp);
        }
    }
}
