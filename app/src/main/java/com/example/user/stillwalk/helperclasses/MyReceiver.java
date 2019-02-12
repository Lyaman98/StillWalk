package com.example.user.stillwalk.helperclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.user.stillwalk.classes.SmsPage;

public class MyReceiver extends BroadcastReceiver {

    private boolean screenOff;

    @Override
    public void onReceive(Context context, Intent intent) {


        screenOff = !Intent.ACTION_SCREEN_ON.equals(intent.getAction());
//        Toast.makeText(context,"screenOFF...." + screenOff , Toast.LENGTH_SHORT).show();

        Intent i = new Intent(context, SmsPage.class);
        i.putExtra("screen_state", screenOff);
        context.startService(i);

    }
}
