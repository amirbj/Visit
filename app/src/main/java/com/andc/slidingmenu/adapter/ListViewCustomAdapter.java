package com.andc.slidingmenu.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;

import com.andc.slidingmenu.R;
import com.andc.slidingmenu.Modals.CustomerInformation;


/**
 * Created by win on 3/14/2015.
 */
public class ListViewCustomAdapter extends BaseAdapter {

    ArrayList<CustomerInformation> requestedItemsList;


    public ListViewCustomAdapter(){
        requestedItemsList = new ArrayList<CustomerInformation>();
    }

    public ArrayList<CustomerInformation> getRequestedItemsList() {
        return requestedItemsList;
    }

    // This method tells the listview the number of rows
    // it will require This count can come from your data source.
    // It can be the size of your Data Source.
    // If you have your datasource as a list of objects, this value will be the size of the list.
    @Override
    public int getCount() {
        return requestedItemsList.size();
    }

    // This method helps ListView to get data for each row.
    // The parameter passed is the row number starting from 0.
    // In our List of Objects, this method will return the object at the passed index.
    @Override
    public CustomerInformation getItem(int position) {
        return requestedItemsList.get(position);
    }


    // You can ignore this method.
    // It just returns the same value as passed. This in general helps ListView to map its rows to the data set elements.
    @Override
    public long getItemId(int position) {
        return position;
    }

    // This is the most important method.
    // This method will be called to get the View for each row.
    // This is the method where we can use our custom listitem and bind it with the data.
    // The first argument passed to getView is the listview item position ie row number.
    // The second parameter is recycled view reference(as we know listview recycles a view, you can confirm through this parameter).
    // Third parameter is the parent to which this view will get attached to.
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_view_search_user, parent,false);
        }

        if(position%2==0){
            convertView.setBackgroundColor(R.color.dark_gray);
        }
        else if(position%2==1){
            convertView.setBackgroundColor(R.color.cream);
        }

        TextView branch = (TextView)convertView.findViewById(R.id.branch);
        TextView bill = (TextView)convertView.findViewById(R.id.bill);
        TextView fabrik = (TextView)convertView.findViewById(R.id.fabrik_number);
        TextView firstName = (TextView)convertView.findViewById(R.id.firstName);
        TextView lastName = (TextView)convertView.findViewById(R.id.lastName);
        branch.setGravity(Gravity.RIGHT);
        bill.setGravity(Gravity.RIGHT);
        fabrik.setGravity(Gravity.RIGHT);
        firstName.setGravity(Gravity.RIGHT);
        lastName.setGravity(Gravity.RIGHT);

        CustomerInformation requestedItem = requestedItemsList.get(position);
        lastName.setText(String.valueOf( requestedItem.lastName ));
        firstName.setText(String.valueOf( requestedItem.firstName ));
        fabrik.setText(String.valueOf( requestedItem.fabrikNumber ));
        branch.setText(String.valueOf( requestedItem.branchCode ));
        bill.setText(String.valueOf( requestedItem.billIdentification ));

        return convertView;
    }

}
