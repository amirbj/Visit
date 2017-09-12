package com.andc.slidingmenu.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by win on 4/13/2015.
 */
public class TimerServices extends Service{
    public CountDownTimer countDownTimer;
    private Toast toast = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.countDownTimer = new CountDownTimer(10 * 60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setText(String.valueOf(millisUntilFinished / 1000));
                toast.show();
                Log.e("timer : ", String.valueOf(millisUntilFinished / 1000));
            }
            public void onFinish() {
//                RenewToken renewToken = new RenewToken(mainActivity);
            }
        }.start();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isTimerCreated","true");
        editor.apply();

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }


}
