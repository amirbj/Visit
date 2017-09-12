package com.andc.slidingmenu.Fragments.NavigationFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.andc.slidingmenu.R;




/**
 * Created by SiaJam on 2/28/2017.
 */

public class InternetBalance extends Fragment {

   ImageView logo;
    Button balanceNet;
    Button balanceSim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate( R.layout.fragment_nav_net_balance, container, false);
        definition(root);
        getSimInfo();
        return root;
    }

    private void definition(View root) {

            logo = (ImageView) root.findViewById(R.id.nav_balance_operator_logo);
            balanceNet = (Button) root.findViewById(R.id.nav_balance_internet_info);
            balanceSim = (Button) root.findViewById(R.id.nav_balance_sim_info);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "*141*1#", null));
                startActivity(intent);

            }
        }
        if(requestCode == 2){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "*555*1*4*1#", null));
                startActivity(intent);
            }
        }
    }

    public void getSimInfo(){
        TelephonyManager telephony = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);
        String opertator = telephony.getNetworkOperatorName();

        if(opertator.equals("IR-MCI")){
            logo.setImageDrawable(getResources().getDrawable(R.drawable.ic_logo_hamrah_aval));
            balanceSim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.CALL_PHONE)) {

                            Toast.makeText(getActivity() , "لطفا دوباره امتحان کنین", Toast.LENGTH_SHORT).show();

                        } else {

                            ActivityCompat.requestPermissions(
                                    getActivity(),
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    3);

                        }
                    } else{

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "*140*11#", null));
                        startActivity(intent);
                    }


                }
            });

            balanceNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.CALL_PHONE)) {

                            Toast.makeText(getActivity() , "لطفا دوباره امتحان کنین", Toast.LENGTH_SHORT).show();

                        } else {

                            ActivityCompat.requestPermissions(
                                    getActivity(),
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    4);

                        }
                    } else{

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "*10*320#", null));
                        startActivity(intent);
                    }
                }
            });
        }

        if(opertator.equals("Irancell")){
            logo.setImageDrawable(getResources().getDrawable(R.drawable.ic_logo_irancell));

            balanceNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.CALL_PHONE)) {

                            Toast.makeText(getActivity() , "لطفا دوباره امتحان کنین", Toast.LENGTH_SHORT).show();

                        } else {

                            ActivityCompat.requestPermissions(
                                    getActivity(),
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    2);

                        }
                    } else{

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "*555*1*4*1#", null));
                        startActivity(intent);
                    }


                }
            });

            balanceSim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                                Manifest.permission.CALL_PHONE)) {

                            Toast.makeText(getActivity() , "لطفا دوباره امتحان کنین", Toast.LENGTH_SHORT).show();

                        } else {

                            ActivityCompat.requestPermissions(
                                    getActivity(),
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);

                        }
                    } else{

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", "*141*1#", null));
                        startActivity(intent);
                    }



                }




            });
        }

    }


}
