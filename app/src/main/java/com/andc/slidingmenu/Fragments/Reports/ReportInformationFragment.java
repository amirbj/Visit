package com.andc.slidingmenu.Fragments.Reports;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.andc.slidingmenu.R;
import ir.smartlab.persindatepicker.PersianDatePicker;
import ir.smartlab.persindatepicker.util.PersianCalendar;

/**
 * Created by win on 3/16/2015.
 */
public class ReportInformationFragment extends Fragment {


    public ReportInformationFragment(){}

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_report_information, container, false);

        final EditText fromDate= (EditText)rootView.findViewById(R.id.from_date);
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View persiandate = inflater.inflate(R.layout.show_persian_date_dialog_fragment, container, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setView(persiandate);
                builder.setTitle(getResources().getText(R.string.datePicker));
                builder.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        PersianDatePicker persianDatePicker = (PersianDatePicker)persiandate.findViewById(R.id.persianDate);
                        PersianCalendar persianCalendar = persianDatePicker.getDisplayPersianDate();
                        int day = persianCalendar.getPersianDay();
                        int month = persianCalendar.getPersianMonth();
                        int year = persianCalendar.getPersianYear();
                        fromDate.setText( toPersianNumber(String.valueOf(year)) + "/" + toPersianNumber(String.valueOf(month))
                                + "/" +  toPersianNumber(String.valueOf(day)) );
                    }
                });
                builder.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //DO TASK
                    }
                });

                builder.create().show();
            }
        });

        final EditText toDate = (EditText)rootView.findViewById(R.id.to_date);
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View persiandate = inflater.inflate(R.layout.show_persian_date_dialog_fragment, container, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_dialog_info);
                builder.setView(persiandate);
                builder.setTitle(getResources().getText(R.string.datePicker));
                builder.setPositiveButton(getResources().getText(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        PersianDatePicker persianDatePicker = (PersianDatePicker)persiandate.findViewById(R.id.persianDate);
                        PersianCalendar persianCalendar = persianDatePicker.getDisplayPersianDate();
                        int day = persianCalendar.getPersianDay();
                        int month = persianCalendar.getPersianMonth();
                        int year = persianCalendar.getPersianYear();
                        Log.e("date = " , String.valueOf(day)+":"+month+":"+String.valueOf(year));
                        toDate.setText( toPersianNumber(String.valueOf(year)) + "/" + toPersianNumber(String.valueOf(month))
                                + "/" +  toPersianNumber(String.valueOf(day)) );
                    }
                });
                builder.setNegativeButton(getResources().getText(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //DO TASK
                    }
                });

                builder.create().show();
            }
        });

        Button button = (Button)rootView.findViewById(R.id.searchbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Fragment fragment = new ReportFragment();
                ((ReportFragment) fragment).type = R.string.pie_chart;
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().add(R.id.frame_container, fragment).addToBackStack(null).commit(); */
            }
        });

        return rootView;
    }

    private String toPersianNumber(String input)
    {
        String[] persian = { "۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹" };

        for (int j=0; j<persian.length; j++)
            input = input.replace(String.valueOf(j), persian[j]);

        return input;
    }

}
