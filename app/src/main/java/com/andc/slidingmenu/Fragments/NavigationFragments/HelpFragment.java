package com.andc.slidingmenu.Fragments.NavigationFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andc.slidingmenu.R;

/**
 * Created by Bijarchian on 3/14/2017.
 */

public class HelpFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

     View root =   inflater.inflate( R.layout.list_view_help,container,false);

        return root;

    }
}
