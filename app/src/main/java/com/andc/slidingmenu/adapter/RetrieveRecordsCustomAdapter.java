package com.andc.slidingmenu.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andc.slidingmenu.Modals.RetrieveRecords;
import com.andc.slidingmenu.R;

import java.util.ArrayList;

/**
 * Created by win on 4/4/2015.
 */
public class RetrieveRecordsCustomAdapter extends BaseAdapter {

    ArrayList<RetrieveRecords> retrieveRecordsList;


    public RetrieveRecordsCustomAdapter(){
        retrieveRecordsList = new ArrayList<RetrieveRecords>();
    }

    public ArrayList<RetrieveRecords> getRetrieveRecordsList() {
        return retrieveRecordsList;
    }

    @Override
    public int getCount() {
        return retrieveRecordsList.size();
    }

    @Override
    public RetrieveRecords getItem(int position) {
        return retrieveRecordsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_view_retrieve_records, parent,false);
        }

        TextView updateTime = (TextView)convertView.findViewById(R.id.update_time);
        TextView retrievePrice = (TextView)convertView.findViewById(R.id.retrieve_Price);
        TextView bankName = (TextView)convertView.findViewById(R.id.bank_Name);
        TextView retrieveTime = (TextView)convertView.findViewById(R.id.retrieve_Time);
        TextView period = (TextView)convertView.findViewById(R.id.period);
        updateTime.setGravity(Gravity.CENTER);
        retrievePrice.setGravity(Gravity.CENTER);
        bankName.setGravity(Gravity.CENTER);
        retrieveTime.setGravity(Gravity.CENTER);
        period.setGravity(Gravity.CENTER);

        RetrieveRecords retrieveRecords = retrieveRecordsList.get(position);
        updateTime.setText(String.valueOf( retrieveRecords.updateTime ));
        retrievePrice.setText(String.valueOf( retrieveRecords.retrievePrice ));
        bankName.setText(String.valueOf( retrieveRecords.bankName ));
        retrieveTime.setText(String.valueOf( retrieveRecords.retrieveTime ));
        period.setText(String.valueOf( retrieveRecords.period ));

        if(position%2==0){
            updateTime.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_two));
            retrievePrice.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_two));
            bankName.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_two));
            retrieveTime.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_two));
            period.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_two));
        }
        else if(position%2==1){
            updateTime.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_three));
            retrievePrice.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_three));
            bankName.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_three));
            retrieveTime.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_three));
            period.setBackground(convertView.getResources().getDrawable(R.drawable.cell_shape_three));
        }


        return convertView;
    }

}
