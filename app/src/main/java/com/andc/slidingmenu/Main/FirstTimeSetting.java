package com.andc.slidingmenu.Main;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andc.slidingmenu.R;

public class FirstTimeSetting extends AppCompatActivity {

    private EditText etServerAddress, etPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_setting);
        etServerAddress = ((EditText)findViewById(R.id.etIP));
        etPort = ((EditText)findViewById(R.id.etPort));
        getPreferences();
    }

    public void confirm(View view)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("webServicesAddress",etServerAddress.getText().toString().trim());
        editor.putString("port",etPort.getText().toString().trim());
        editor.apply();
        Toast toast = Toast.makeText(getBaseContext(), getString(R.string.msg_save_settings), Toast.LENGTH_SHORT);
        toast.show();
        this.finish();
    }

    private void getPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        etServerAddress.setText(preferences.getString("webServicesAddress",""));
        etPort.setText(preferences.getString("port",""));
    }

}
