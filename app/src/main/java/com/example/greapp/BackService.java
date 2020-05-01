package com.example.greapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class BackService extends Service {

    UnlockTrigger myReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForeground();
        else
            startForeground(1, new Notification());

        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.GreApp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);
        chan.setLightColor(Color.parseColor("#00FFFFFF"));
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Click to set visibility for this notification")
                .setPriority(Notification.PRIORITY_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true).setWhen(0)
                .build();
        startForeground(1337, notification);

        try{

            myReceiver = new UnlockTrigger();
            registerReceiver(myReceiver, new IntentFilter("android.intent.action.USER_PRESENT"));
            dbtransactions();

        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myReceiver != null){
            try{
                unregisterReceiver(myReceiver);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void dbtransactions() {

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 2);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                new storehelper().updatestore(BackService.this);
            }
        }, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));

    }

}
