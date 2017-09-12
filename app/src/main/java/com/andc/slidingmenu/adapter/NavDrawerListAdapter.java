package com.andc.slidingmenu.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andc.slidingmenu.Modals.NavDrawerItem;
import com.andc.slidingmenu.R;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<NavDrawerItem> navDrawerItems;


	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		mContext = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        if(position==0){
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.drawer_list_header, null);
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String userName = preferences.getString("user","");

            TextView mUserName = (TextView)convertView.findViewById(R.id.drawer_username);
            mUserName.setText(userName);

          //  Button mVisitedBtn = (Button)convertView.findViewById(R.id.drawer_visited_count);
          //  Button mUnvisitedBtn = (Button)convertView.findViewById(R.id.drawer_unvisited_count);
           // setCounters();
         //   mVisitedBtn.setText(String.valueOf(mVisitedCount));
          //  mUnvisitedBtn.setText(String.valueOf(mUnvisitedCount));



        } else {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            }

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
            TextView txtCount = (TextView) convertView.findViewById(R.id.counter);

            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
            txtTitle.setText(navDrawerItems.get(position).getTitle());

            // displaying count
            // check whether it set visible or not
            if (navDrawerItems.get(position).getCounterVisibility()) {
                txtCount.setText(navDrawerItems.get(position).getCount());
            } else {
                // hide the counter view
                txtCount.setVisibility(View.GONE);
            }

        }
        return convertView;
	}


}
