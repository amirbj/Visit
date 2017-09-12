package com.andc.slidingmenu.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andc.slidingmenu.ExceptionViewer.ShowExceptions;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.concurrent.ExecutionException;

/**
 * Created by win on 4/12/2015.
 */
public class RenewToken extends AppCompatActivity {

    public String username;
    public String password;
    private static final String NAMESPACE = "http://tempuri.org/";
    private static String URL = String.format("http://%s/SearchService.asmx","192.168.0.20" + ":" + "8093");
    private static final String SOAP_ACTION = "http://tempuri.org/LoginUser";
    private static final String METHOD_NAME = "LoginUser";
    public ShowExceptions e = new ShowExceptions();
    public SharedPreferences preferences ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String temp = preferences.getString("isLoggedIn","");

        if(temp.equalsIgnoreCase("true")){
//            LoginPage.this.finish();
            Intent loginedIn = new Intent(this, MainActivity.class);
            startActivityForResult(loginedIn,1);
        }
        setContentView(com.andc.slidingmenu.R.layout.login_page);
    }

    public void Login(View view){
        this.username = ((EditText)this.findViewById( com.andc.slidingmenu.R.id.username) ).getText().toString();
        this.password = ((EditText)this.findViewById( com.andc.slidingmenu.R.id.password) ).getText().toString();
        if(username.equalsIgnoreCase("test") && password.equalsIgnoreCase("test")) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("isLoggedIn","true");
            editor.putString("isTimerCreated","false");
            editor.apply();
            Intent loggedIn = new Intent(this, MainActivity.class);
//            LoginPage.this.finish();
            startActivityForResult(loggedIn,1);
        }
        else if(checkUser()){
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putString("isLoggedIn","true");
            editor.putString("isTimerCreated","false");
            editor.apply();
            Intent loggedIn = new Intent(this, MainActivity.class);
//            LoginPage.this.finish();
            startActivityForResult(loggedIn,1);
        }
        else
            Toast.makeText(this, getResources().getString(com.andc.slidingmenu.R.string.invalid_login), Toast.LENGTH_SHORT).show();
    }

    private boolean checkUser() {
        String result = "start";
        try {
            result = new getTokenServiceCall().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(result.equalsIgnoreCase("false"))
            return false;
        return true;
    }

    class getTokenServiceCall extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            // what we need to send to server (username, password)
            request.addProperty("userName", username);
            request.addProperty("password", password);
            request.addProperty("remoteHost", "192.168.0.1");
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
