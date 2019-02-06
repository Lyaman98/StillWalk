package com.example.user.stillwalk.helperclasses;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SMS implements PermissionCheck {

    private Context context;
    private SmsManager smsManager;
    private static boolean checkForPermission = false;

    public SMS(Context context) {

        this.context = context;

        if (checkPermission(Manifest.permission.SEND_SMS)){
            checkForPermission = true;
        }

    }

    public boolean sendMessage(String phoneNumber,String message){

        if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(message)){

            if (checkPermission(Manifest.permission.SEND_SMS)){

                smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber,null,message,null,null);
                Log.i("MYTAG","SENDING THE MESSAGE...");
                return true;
            }else {
                Toast.makeText(context,"Permission denied",Toast.LENGTH_SHORT).show();

            }
        }else {
            Toast.makeText(context,"Enter phone number and message",Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    @Override
    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(context,permission);

        return check == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isCheckForPermission() {
        return checkForPermission;
    }
}
