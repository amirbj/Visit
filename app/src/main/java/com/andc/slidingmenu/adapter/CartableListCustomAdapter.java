package com.andc.slidingmenu.adapter;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.andc.slidingmenu.Fragments.Cartable.Others.CartableFragment;
import com.andc.slidingmenu.Main.CartableItemActivity;
import com.andc.slidingmenu.R;
import com.andc.slidingmenu.Utility.ConvertorNumberToPersian;

import slidingmenu.andc.com.dataaccess.CartableTbl;

/**
 * Created by win on 4/11/2015.
 */
public class CartableListCustomAdapter extends BaseAdapter {

    public CartableFragment cartableFragment;
    ConvertorNumberToPersian convertorNumberToPersian;
    ArrayList<CartableTbl> requestedCartableItemsList;
    TextView requestNumber;
    TextView subscriptionName;
    TextView address;
    TextView phone;
    TextView status;
    CheckBox send;

    public CartableListCustomAdapter(CartableFragment cartableFragment, ArrayList<CartableTbl> data) {
        this.cartableFragment = cartableFragment;
        requestedCartableItemsList = data;
    }

    public ArrayList<CartableTbl> getRequestedCartableItemsList() {
        return requestedCartableItemsList;
    }

    @Override
    public int getCount() {
        return requestedCartableItemsList.size();
    }

    @Override
    public CartableTbl getItem(int position) {
        return requestedCartableItemsList.get(position);
    }


    public int getItem(long requestNumber) {
        for(int i=0 ; i<getCount() ; i++){
            if(getItem(i).RequestCode == requestNumber)
                return i;

        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *      This is the most important method.
     *      This method will be called to get the View for each row.
     *      This is the method where we can use our custom listitem and bind it with the data.
     *      The first argument passed to getView is the listview item position ie row number.
     *      The second parameter is recycled view reference(as we know listview recycles a view, you can confirm through this parameter).
     *      Third parameter is the parent to which this view will get attached to.
     */
    @TargetApi(17)
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        /*
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_view_cartable, parent, false);
        }
        convertorNumberToPersian = new ConvertorNumberToPersian();
        requestNumber = (TextView) convertView.findViewById(R.id.cartable_request_number);
        subscriptionName = (TextView) convertView.findViewById(R.id.cartable_subscription_name);
        address = (TextView) convertView.findViewById(R.id.cartable_address);
        phone = (TextView) convertView.findViewById(R.id.cartable_phone);
        status = (TextView) convertView.findViewById(R.id.cartable_status);
        send = (CheckBox) convertView.findViewById(R.id.cartable_send);

        requestNumber.setGravity(Gravity.CENTER);
        subscriptionName.setGravity(Gravity.CENTER);
        address.setGravity(Gravity.CENTER);
        phone.setGravity(Gravity.CENTER);
        status.setGravity(Gravity.CENTER);
        send.setGravity(Gravity.CENTER);

        final CartableListDB requestedCartableItem = requestedCartableItemsList.get(position);

        status.setText(requestedCartableItem.status);
        phone.setText(convertorNumberToPersian.toPersianNumber(requestedCartableItem.phone));
        address.setText("   آدرس: " + requestedCartableItem.address);
        subscriptionName.setText(convertorNumberToPersian.toPersianNumber(requestedCartableItem.subscriptionName));
        requestNumber.setText(requestedCartableItem.requestNumber);

        clickListener(requestedCartableItem, position);
        cartablePainting(cartableFragment, position, convertView);

        return convertView;
        */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_view_item_cartable, parent, false);
        }

        convertorNumberToPersian = new ConvertorNumberToPersian();
        TextView requestNumber = (TextView) convertView.findViewById(R.id.cartable_request_number);
        TextView subscriptionName = (TextView) convertView.findViewById(R.id.cartable_subscription_name);
        TextView address = (TextView) convertView.findViewById(R.id.cartable_address);
        ImageView phone = (ImageView) convertView.findViewById(R.id.cartable_phone);
        ImageView status = (ImageView) convertView.findViewById(R.id.cartable_status);

        final CartableTbl requestedCartableItem = requestedCartableItemsList.get(position);

        //status.setText(requestedCartableItem.status);
        //phone.setText(convertorNumberToPersian.toPersianNumber(requestedCartableItem.phone));
        address.setText("   آدرس: " + requestedCartableItem.Address);
        subscriptionName.setText(convertorNumberToPersian.toPersianNumber(requestedCartableItem.Name));
        requestNumber.setText(convertorNumberToPersian.toPersianNumber(String.valueOf(requestedCartableItem.RequestCode)));


        if(requestedCartableItem.Status == 1) {
            //Set Visited Icon
            status.setImageResource(R.drawable.ic_state_visited);

        } else if(requestedCartableItem.Status == 0){
            //Set unVisited Icon
            status.setImageResource(R.drawable.ic_state_unvisited);

        } else if(requestedCartableItem.Status == 2) {
            //Set Updated Icon
            status.setImageResource(R.drawable.ic_state_uploaded);

        } else if(requestedCartableItem.Status == 9) {
            ////Set Faulty Icon
            status.setImageResource(R.drawable.ic_state_faulty);
        }

        final String number = requestedCartableItemsList.get(position).MobileNo;
        final String mName = getItem(position).Name;
        final String mContactMessage = String.format(cartableFragment.getActivity().getResources().getString(R.string.dialog_contact_body), mName);
        phone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(number!=null) {
        View view = null;
                    LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(parent.getContext());
                    view = inflater.inflate(R.layout.create_unsuccess_visit, parent, false);
                    Spinner spin = (Spinner) view.findViewById(R.id.reason_spinner);
                    //Create Contact Dialog
                    //AlertDialog.Builder builder = new AlertDialog.Builder(cartableFragment.getActivity(),R.style.Theme_Sphinx_Dialog_Alert)
                    AlertDialog.Builder builder = new AlertDialog.Builder(cartableFragment.getActivity())
                            .setTitle(cartableFragment.getActivity().getResources().getText(R.string.dialog_contact_title))
                            //.setIcon(android.R.drawable.ic_menu_call)
                            .setMessage(mContactMessage)
                            .setNegativeButton(cartableFragment.getActivity().getResources().getText(R.string.sms), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences preferences = PreferenceManager.
                                            getDefaultSharedPreferences(cartableFragment.getActivity());
                                    String smsbody = preferences.getString("smsbody", "");
                                    Uri uri = Uri.parse("smsto:" + number);
                                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                                    it.putExtra("sms_body", smsbody);
                                    cartableFragment.getActivity().startActivity(it);
                                }
                            })
                            .setPositiveButton(cartableFragment.getActivity().getResources().getText(R.string.call), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    String uri = "tel:" + number;
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse(uri));
                                    cartableFragment.getActivity().startActivity(intent);
                                }
                            });


                    AlertDialog dialog = builder.create();
                    dialog.show();

                    //Set Icons to the buttons
                    Drawable drawable;
                    Button mNegativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    drawable = cartableFragment.getActivity().getResources().getDrawable(R.drawable.ic_text);
                    mNegativeButton.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,  null);

                    Button mPositiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    drawable = cartableFragment.getActivity().getResources().getDrawable(R.drawable.ic_call);
                    mPositiveButton.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,  null);

                    //Set Title Gravity
                    final int alertTitle = cartableFragment.getActivity().getResources().getIdentifier( "alertTitle", "id", "android" );
                    TextView messageText = (TextView)dialog.findViewById(alertTitle);
                    //messageText.setGravity(Gravity.Right);
                    messageText.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                }
            }
        });

        cartablePainting(cartableFragment, position, convertView);
        return convertView;

    }

    /**
     *     we paint cartable list background. odds are cream, evens are dark_gray
     */
    private void cartablePainting(CartableFragment cartableFragment, int position, View convertView) {
        //LinearLayout mListViewItem = (LinearLayout)convertView.findViewById(R.id.list_view_item);
        //LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView mHeader = (ImageView)convertView.findViewById(R.id.list_item_indicator);
        if(position%2==0) {
            //mLayoutParams.setMargins(20, 0, 0, 0);
            //mListViewItem.setLayoutParams(mLayoutParams);
            //convertView.setBackground(cartableFragment.getActivity().getResources().getDrawable(R.color.cream));
            mHeader.setBackgroundResource(R.drawable.bg_header1);
        }
        else if(position%2==1) {
            //mLayoutParams.setMargins(0,0,20,0);
            //mListViewItem.setLayoutParams(mLayoutParams);
            //convertView.setBackground(cartableFragment.getActivity().getResources().getDrawable(R.color.dark_gray));
            mHeader.setBackgroundResource(R.drawable.bg_header2);
        }
    }

    /**
     *
     * @param requestedCartableItem
     * user interaction with devices
     * if touches req number, status, address or name wizard starts
     * if touches phone, an alert dialog is show to ask him decided to call or text to user
     * @param position
     */
    private void clickListener(final CartableTbl requestedCartableItem, final int position) {
        requestNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSwiper(cartableFragment, requestedCartableItem.RequestCode);
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSwiper(cartableFragment, requestedCartableItem.RequestCode);
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSwiper(cartableFragment, requestedCartableItem.RequestCode);
            }
        });
        subscriptionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSwiper(cartableFragment, requestedCartableItem.RequestCode);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number = requestedCartableItemsList.get(position).MobileNo;
                if(number!=null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(cartableFragment.getActivity())
                            .setTitle(cartableFragment.getActivity().getResources().getText(R.string.sms_call))
                            .setNegativeButton(cartableFragment.getActivity().getResources().getText(R.string.sms), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences preferences = PreferenceManager.
                                            getDefaultSharedPreferences(cartableFragment.getActivity());
                                    String smsbody = preferences.getString("smsbody", "");
                                    Uri uri = Uri.parse("smsto:" + number);
                                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                                    it.putExtra("sms_body", smsbody);
                                    cartableFragment.getActivity().startActivity(it);
                                }
                            })
                            .setPositiveButton(cartableFragment.getActivity().getResources().getText(R.string.call), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    String uri = "tel:" + number;
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse(uri));
                                    cartableFragment.getActivity().startActivity(intent);
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        });
     /*   send.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    List<ChangeBranchDB> changeBranches = ChangeBranchDB.find(ChangeBranchDB.class, "request_Number = ?", String.valueOf(requestedCartableItem.RequestCode));
                    for(ChangeBranchDB changeBranch:changeBranches){
                        if(changeBranch.phase==null){
                            Toast toast = Toast.makeText(cartableFragment.getActivity().getBaseContext()
                                    , cartableFragment.getResources().getString(R.string.change_branch_empty_phase), Toast.LENGTH_SHORT);
                            LinearLayout toastLayout = (LinearLayout) toast.getView();
                            TextView toastTV = (TextView) toastLayout.getChildAt(0);
                            toastTV.setTextSize(30);
                            toast.show();
                            buttonView.setChecked(false);
                        }
                    }
                    (requestedCartableItemsList.get(position)).IsSend = "true";
                }
                else
                    (requestedCartableItemsList.get(position)).IsSend = "false";

            }
        }); */

    }

    /**
     *
     * @param cartableFragment
     * @param requestNumber
     * user start to see cartable wizard
     * request number of selected cartable put into a bundle and send to wizard
     */
    private void createSwiper(CartableFragment cartableFragment, long requestNumber) {

        //Bundle info = new Bundle();
        //info.putString("requestnumber",requestNumber);
        //Fragment fragment = new CartablePlaceInfo();
        //fragment.setArguments(info);
        //FragmentManager fragmentManager = cartableFragment.getFragmentManager();
        //fragmentManager.beginTransaction().add(R.id.frame_container, fragment, "place").addToBackStack(null).commit();

        Intent intent = new Intent(this.cartableFragment.getActivity(), CartableItemActivity.class);
        intent.putExtra("requestnumber",requestNumber);
        cartableFragment.startActivity(intent);
    }
}
