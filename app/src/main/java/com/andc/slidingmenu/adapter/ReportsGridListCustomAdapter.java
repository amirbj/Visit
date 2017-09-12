package com.andc.slidingmenu.adapter;

/**
 * Created by win on 3/16/2015.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andc.slidingmenu.Modals.ReportsModel;
import com.andc.slidingmenu.R;

import java.util.ArrayList;

public class ReportsGridListCustomAdapter extends BaseAdapter{

    ArrayList<ReportsModel> reportedItemsList;

    public ReportsGridListCustomAdapter(ArrayList<ReportsModel> items){
            reportedItemsList = new ArrayList<ReportsModel>();
            reportedItemsList.addAll(items);
        }

    @Override
    public int getCount() {
        return reportedItemsList.size();
    }

    @Override
    public ReportsModel getItem(int position) {
        return reportedItemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.grid_view_reports, parent, false);
        }

        TextView description = (TextView) convertView.findViewById(R.id.description);

        ReportsModel reportsModel = reportedItemsList.get(position);
        description.setText( reportsModel.getDescription() );

        return convertView;
    }
}
