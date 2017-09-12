package com.andc.slidingmenu.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.andc.slidingmenu.R;
import com.andc.slidingmenu.Modals.CorrectSaleRecords;

/**
 * Created by win on 4/4/2015.
 */
public class CorrectSaleRecordsCustomAdapter extends BaseAdapter {

    ArrayList<CorrectSaleRecords> correctSaleRecordsList;


    public CorrectSaleRecordsCustomAdapter(){
        correctSaleRecordsList = new ArrayList<CorrectSaleRecords>();
    }

    public ArrayList<CorrectSaleRecords> getCorrectSaleRecordsList() {
        return correctSaleRecordsList;
    }

    @Override
    public int getCount() {
        return correctSaleRecordsList.size();
    }

    @Override
    public CorrectSaleRecords getItem(int position) {
        return correctSaleRecordsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_view_correct_sale_records, parent,false);
        }

        TextView updateTime = (TextView)convertView.findViewById(R.id.update_time);
        TextView currentMonitoringTime = (TextView)convertView.findViewById(R.id.current_monitoring_time);
        TextView activeUsage = (TextView)convertView.findViewById(R.id.active_usage);
        TextView reactiveUsage = (TextView)convertView.findViewById(R.id.reactive_usage);
        TextView salePrice = (TextView)convertView.findViewById(R.id.sale_price);
        updateTime.setGravity(Gravity.CENTER);
        currentMonitoringTime.setGravity(Gravity.CENTER);
        activeUsage.setGravity(Gravity.CENTER);
        reactiveUsage.setGravity(Gravity.CENTER);
        salePrice.setGravity(Gravity.CENTER);

        CorrectSaleRecords correctSaleRecords = correctSaleRecordsList.get(position);
        updateTime.setText(String.valueOf( correctSaleRecords.updateTime ));
        currentMonitoringTime.setText(String.valueOf( correctSaleRecords.currentMonitoringTime ));
        activeUsage.setText(String.valueOf( correctSaleRecords.activeUsage ));
        reactiveUsage.setText(String.valueOf( correctSaleRecords.reactiveUsage ));
        salePrice.setText(String.valueOf( correctSaleRecords.salePrice ));

        if(position%2==0){
            updateTime.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_two));
            currentMonitoringTime.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_two));
            activeUsage.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_two));
            reactiveUsage.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_two));
            salePrice.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_two));
        }
        else if(position%2==1){
            updateTime.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_three));
            currentMonitoringTime.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_three));
            activeUsage.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_three));
            reactiveUsage.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_three));
            salePrice.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_three));
        }


        return convertView;
    }

}
