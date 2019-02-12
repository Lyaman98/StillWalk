package com.example.user.stillwalk.helperclasses;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class UpdateService extends Service {

    BroadcastReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();

        // register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        receiver = new MyReceiver();
        registerReceiver(receiver , filter);

    }

    @Override
    public void onDestroy() {

        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("here" , "");
        boolean screenOn = intent.getBooleanExtra("sleep_state",false);
        if (!screenOn){
            Toast.makeText(getApplicationContext(),"Awake", Toast.LENGTH_LONG).show();
        }else {

        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
