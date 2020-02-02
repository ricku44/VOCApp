package com.example.greapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.adriangl.overlayhelper.OverlayHelper;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private OverlayHelper overlayHelper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        changeStatusBarColor();

        overlayHelper = new OverlayHelper(this.getApplicationContext(), new OverlayHelper.OverlayPermissionChangedListener() {
            @Override public void onOverlayPermissionCancelled() {
                Toast.makeText(Home.this, "Draw overlay permissions request canceled", Toast.LENGTH_SHORT).show();
            }

            @Override public void onOverlayPermissionGranted() {
                Toast.makeText(Home.this, "Draw overlay permissions request granted", Toast.LENGTH_SHORT).show();
            }

            @Override public void onOverlayPermissionDenied() {
                Toast.makeText(Home.this, "Draw overlay permissions request denied", Toast.LENGTH_SHORT).show();
            }
        });

        overlayHelper.startWatching();

        if (!Settings.canDrawOverlays(this)) {

            overlayHelper.requestDrawOverlaysPermission(
                    Home.this,
                    "Request draw overlays permission?",
                    "You have to enable the draw overlays permission for this app to work",
                    "Enable",
                    "Cancel");
        } else {

            registerReceiver(new UnlockTrigger(), new IntentFilter("android.intent.action.USER_PRESENT"));
        }


        startService(new Intent(this, BackService.class));


        final LinearLayout ll1 = findViewById(R.id.lll1);
        final LinearLayout ll2 = findViewById(R.id.lll2);
        final LinearLayout ll3 = findViewById(R.id.lll3);


        final Button home = findViewById(R.id.home1);
        final Button sett = findViewById(R.id.Setting1);
        final Button noti = findViewById(R.id.notify1);

        final TextView t1 = findViewById(R.id.t1);
        final TextView t2 = findViewById(R.id.t2);
        final TextView t3 = findViewById(R.id.t3);


        final LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = Float.parseFloat("1.2");

        final LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p2.weight = Float.parseFloat("0.8");


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll1.setBackground(getResources().getDrawable(R.drawable.circle_bg3));
                ll2.setBackgroundResource(0);
                ll3.setBackgroundResource(0);

               // ll1.setLayoutParams(p);
               // ll2.setLayoutParams(p2);
               // ll3.setLayoutParams(p2);

                t1.setVisibility(View.VISIBLE);
                t2.setVisibility(View.GONE);
                t3.setVisibility(View.GONE);

                home.setScaleX(Float.parseFloat("0.7"));
                home.setScaleY(Float.parseFloat("1.2"));

                sett.setScaleX(Float.parseFloat("0.3"));
                sett.setScaleY(Float.parseFloat("0.9"));

                noti.setScaleX(Float.parseFloat("0.3"));
                noti.setScaleY(Float.parseFloat("0.9"));

                home.setBackground(getResources().getDrawable(R.drawable.ic_home_black_24dp));
                sett.setBackground(getResources().getDrawable(R.drawable.ic_settings_black_24dp));
                noti.setBackground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
            }
        });


        sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll2.setBackground(getResources().getDrawable(R.drawable.circle_bg3));
                ll1.setBackgroundResource(0);
                ll3.setBackgroundResource(0);


                //ll1.setLayoutParams(p2);
                //ll2.setLayoutParams(p);
                //ll3.setLayoutParams(p2);

                t2.setVisibility(View.VISIBLE);
                t1.setVisibility(View.GONE);
                t3.setVisibility(View.GONE);

                sett.setScaleX(Float.parseFloat("0.7"));
                sett.setScaleY(Float.parseFloat("1.2"));

                home.setScaleX(Float.parseFloat("0.3"));
                home.setScaleY(Float.parseFloat("0.9"));

                noti.setScaleX(Float.parseFloat("0.3"));
                noti.setScaleY(Float.parseFloat("0.9"));


                home.setBackground(getResources().getDrawable(R.drawable.ic_home_black_24dp2));
                sett.setBackground(getResources().getDrawable(R.drawable.ic_settings_black_24dp2));
                noti.setBackground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp));
            }
        });

        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll3.setBackground(getResources().getDrawable(R.drawable.circle_bg3));
                ll2.setBackgroundResource(0);
                ll1.setBackgroundResource(0);


                //ll1.setLayoutParams(p2);
                //ll2.setLayoutParams(p2);
                //ll3.setLayoutParams(p);

                t3.setVisibility(View.VISIBLE);
                t2.setVisibility(View.GONE);
                t1.setVisibility(View.GONE);

                noti.setScaleX(Float.parseFloat("0.7"));
                noti.setScaleY(Float.parseFloat("1.2"));

                sett.setScaleX(Float.parseFloat("0.3"));
                sett.setScaleY(Float.parseFloat("0.9"));

                home.setScaleX(Float.parseFloat("0.3"));
                home.setScaleY(Float.parseFloat("0.9"));


                home.setBackground(getResources().getDrawable(R.drawable.ic_home_black_24dp2));
                sett.setBackground(getResources().getDrawable(R.drawable.ic_settings_black_24dp));
                noti.setBackground(getResources().getDrawable(R.drawable.ic_notifications_black_24dp2));
            }
        });

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overlayHelper.onRequestDrawOverlaysPermissionResult(requestCode);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overlayHelper.stopWatching();
    }


}
