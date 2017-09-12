package com.andc.slidingmenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andc.slidingmenu.Modals.Observe_List_Item;
import com.andc.slidingmenu.R;

import java.util.List;

public class CartableObservationListAdapter extends ArrayAdapter<Observe_List_Item> {

    public CartableObservationListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CartableObservationListAdapter(Context context, int resource, List<Observe_List_Item> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_view_observe, null);
        }

        Observe_List_Item p = getItem(position);

        if (p != null) {
            TextView tvIndex = (TextView) v.findViewById(R.id.txtIndex);
            TextView tvTitle = (TextView) v.findViewById(R.id.txtTitle);
            tvIndex.setText(String.valueOf(p.Id));
            tvTitle.setText(p.Title);
        }
        return v;
    }
}
