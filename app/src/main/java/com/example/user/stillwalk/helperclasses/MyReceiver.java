package com.example.user.stillwalk.helperclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    private boolean screenOff;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
        }

        Intent i = new Intent(context, UpdateService.class);
        i.putExtra("screen_state",screenOff);
        context.startService(i);
    }
}
