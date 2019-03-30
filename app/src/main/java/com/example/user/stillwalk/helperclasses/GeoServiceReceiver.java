package com.example.user.stillwalk.helperclasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GeoServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            context.startService(new Intent(context, GetLocationService.class));

        }
    }
}
