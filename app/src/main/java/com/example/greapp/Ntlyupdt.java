package com.example.greapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Ntlyupdt extends BroadcastReceiver {
    storehelper s = new storehelper();
    @Override
    public void onReceive(Context context, Intent intent) {
        s.updatestore(context);

        Toast.makeText(context,"Triggered",Toast.LENGTH_LONG).show();
    }
}
