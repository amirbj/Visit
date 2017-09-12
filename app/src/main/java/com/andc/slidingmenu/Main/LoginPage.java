package com.andc.slidingmenu.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andc.slidingmenu.Connection.ApiInterface;
import com.andc.slidingmenu.Connection.LoginObject;
import com.andc.slidingmenu.Connection.RetInterface;
import com.andc.slidingmenu.ExceptionViewer.ShowExceptions;
import com.andc.slidingmenu.Modals.AccessToken;
import com.andc.slidingmenu.R;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by win on 4/11/2015.
 */
public class LoginPage extends AppCompatActivity implements RetInterface {

    public String username;
    public String password;
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String URL = String.format("http://%s/SearchService.asmx","217.219.37.50" + ":" + "8850");
    private static final String SOAP_ACTION = "http://tempuri.org/LoginUser";
    private static final String METHOD_NAME = "LoginUser";
    public ShowExceptions e = new ShowExceptions();
    public SharedPreferences preferences ;
    private ProgressBar mLoading;
    public Retrofit retrofit;
    public boolean check;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mLoading = (ProgressBar)findViewById(R.id.login_loading);
        String timer = preferences.getString("timer","");
        //Application bomb
        if (timer.equals(""))
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            timer = sdf.format(c.getTime());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("timer",timer);
            editor.apply();
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                Date startDate = sdf.parse(timer);
                Calendar c = Calendar.getInstance();
                Date currentDate = c.getTime();
                c.setTime(startDate);
                c.add(Calendar.HOUR, 72);
                Date expireDate = c.getTime();
//                if (currentDate.after(expireDate))
//                    this.finish();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        String serverAddress = preferences.getString("webServicesAddress","");
        String port = preferences.getString("port","");

        if (serverAddress.equals("") || port.equals(""))
        {
            Intent firstSetting = new Intent(this, FirstTimeSetting.class);
            startActivityForResult(firstSetting,0);
        }

        String temp = preferences.getString("isLoggedIn","");

        if(temp.equalsIgnoreCase("true")){
            LoginPage.this.finish();
            Intent loggedIn = new Intent(this, MainActivity.class);
            startActivityForResult(loggedIn,1);
        }
    }

    public void ChangeServer(View view)
    {
        Intent firstSetting = new Intent(this, FirstTimeSetting.class);
        startActivityForResult(firstSetting,0);
    }

    public void Login(View view){

        if (!makeURLReady())
            return;
        this.username = ((EditText)this.findViewById( com.andc.slidingmenu.R.id.username) ).getText().toString();
        this.password = ((EditText)this.findViewById( com.andc.slidingmenu.R.id.password) ).getText().toString();
        if(username.equalsIgnoreCase("test") && password.equalsIgnoreCase("test")) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("isLoggedIn","true");
            editor.putString("isTimerCreated","false");
            editor.apply();
            Intent loggedIn = new Intent(this, MainActivity.class);
            LoginPage.this.finish();
            startActivityForResult(loggedIn,1);
        }
        else if (makeURLReady()){
            getToken(this);




        }
        else
            Toast.makeText(this, getResources().getString(com.andc.slidingmenu.R.string.invalid_login), Toast.LENGTH_SHORT).show();
    }

    private void checkUser() {

     /*   String result = "start";
        try {
            result = new getTokenServiceCall().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (result == null)
            return false;

        if(result.equalsIgnoreCase("false"))
           return false;
*/


    }

    private Boolean makeURLReady() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String webServicesAddress = preferences.getString("webServicesAddress", "");
        if(webServicesAddress.isEmpty()){
            Toast toast = Toast.makeText(this.getBaseContext()
                    , getResources().getString(R.string.src_invalid), Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toast.show();
            return false;
        }

        String port = preferences.getString("port","");
        if(port.isEmpty()){
            Toast toast = Toast.makeText(this.getBaseContext()
                    , getResources().getString(R.string.port_invalid), Toast.LENGTH_SHORT);
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toast.show();
            return false;
        }

        if(!port.equalsIgnoreCase("") && !webServicesAddress.equalsIgnoreCase("") ){
            URL = String.format("http://" + webServicesAddress +":"+port);
        }
        return true;
    }


    public void getToken(final RetInterface callback){

        final ProgressDialog progressDialog = new ProgressDialog(LoginPage.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("درخواست ورود");
        progressDialog.setMessage("لطفا چند لحظه صبر کنید...");
        progressDialog.show();

        retrofit = new Retrofit.Builder().baseUrl(URL).addConverterFactory(GsonConverterFactory.create()).build();

        LoginObject login = new LoginObject(username, password);
        ApiInterface apiService = retrofit.create(ApiInterface.class);
        Call<AccessToken> call = apiService.Authenticate(login);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.code()== 200 || response.code()==201) {
                    AccessToken rs = response.body();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", rs.getToken());
                    editor.putString("user", rs.getUsr_FirstName() +" " + rs.getUsr_LastName());
                    editor.apply();
                    if(!rs.getToken().isEmpty()) {
                        callback.onSucess(true);
                        Log.e("token retrofit", rs.getToken());
                    }
                    else{
                        callback.onSucess(false);
                    }
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(LoginPage.this, "خطای" + response.code() + "رخ داده است", Toast.LENGTH_SHORT).show();
                    callback.onSucess(false);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {

                callback.onError();
                Toast.makeText(LoginPage.this, t.toString(), Toast.LENGTH_SHORT);
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onSucess(boolean x) {
        if(x){
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putString("isLoggedIn","true");
            editor.putString("isTimerCreated","false");
            editor.apply();
            Intent loggedIn = new Intent(this, MainActivity.class);
            LoginPage.this.finish();
            startActivityForResult(loggedIn,1);
        }
        else{
            Toast.makeText(this,"نام کاربری یا کلمه عبور اشتباه وارد شده است" ,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError() {


    }


    class getTokenServiceCall extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLoading.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            // what we need to send to server (username, password)
            request.addProperty("userName", username);
            request.addProperty("password", password);
            //request.addProperty("remoteHost", "192.168.0.1");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
            try {
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapPrimitive resultsRequestSOAP = (SoapPrimitive) ((SoapObject) envelope.bodyIn).getProperty(0);
                String invalid = "00000000-0000-0000-0000-000000000000";
                if(!resultsRequestSOAP.toString().equalsIgnoreCase(invalid)) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", resultsRequestSOAP.toString());
                    editor.apply();
                    return resultsRequestSOAP.toString();
                }
                return "false";
            } catch (Exception execption) {
                e = new ShowExceptions(execption.getMessage());
            }
            return null;
        }
    }
}
