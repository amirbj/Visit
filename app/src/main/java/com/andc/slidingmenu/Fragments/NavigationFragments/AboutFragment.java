package com.andc.slidingmenu.Fragments.NavigationFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andc.slidingmenu.R;

import java.util.zip.Inflater;

/**
 * Created by SiaJam on 2/18/2017.
 */

public class AboutFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate( R.layout.fragment_nav_about_us, container, false);
        definition(root);

        return root;
    }

    private void definition(View root) {
        TextView about = (TextView) root.findViewById(R.id.about_explain_text);
        about.setText("برنامه بازدید از محل شرکت توزیع نیروی برق البرز"+"\n نسخه 1.1 "+"\n توسعه توسط شرکت اندیشه کامپیوتر");

    }
}
