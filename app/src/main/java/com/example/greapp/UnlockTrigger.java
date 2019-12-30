package com.example.greapp;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UnlockTrigger extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardSecure()) {

            Intent intent1 = new Intent(context,ServiceOverlay.class);
            context.startActivity(intent1);

        }
    }

}
