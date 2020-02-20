package com.example.greapp;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import com.adriangl.overlayhelper.OverlayHelper;
import android.provider.Settings;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<GradientDrawable> clrs = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<String> btns = new ArrayList<>();
    private OverlayHelper overlayHelper;
    private UnlockTrigger unlockTrigger;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0xFF181d27,0xFF0c0c0c});
        gd.setCornerRadius(0f);

        getWindow().getDecorView().setBackground(gd);


        changeStatusBarColor();

        isWriteStoragePermissionGranted();

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

            unlockTrigger = new UnlockTrigger();
            registerReceiver(unlockTrigger, new IntentFilter("android.intent.action.USER_PRESENT"));
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


        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (20*scale + 0.5f);
        int dpAsPixels2 = (int) (10*scale + 0.5f);

        home.setOnClickListener(v -> {
            ll1.setBackground(getResources().getDrawable(R.drawable.circle_bg3));
            ll2.setBackground(getResources().getDrawable(R.drawable.circle_bg4));
            ll3.setBackground(getResources().getDrawable(R.drawable.circle_bg4));

            ll1.setPadding(dpAsPixels,dpAsPixels2,dpAsPixels,dpAsPixels2);
            //ll2.setPadding(0,dpAsPixels,0,dpAsPixels);
            //ll3.setPadding(0,dpAsPixels,0,dpAsPixels);

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
        });


        sett.setOnClickListener(v -> {
            ll2.setBackground(getResources().getDrawable(R.drawable.circle_bg3));
            ll1.setBackground(getResources().getDrawable(R.drawable.circle_bg4));
            ll3.setBackground(getResources().getDrawable(R.drawable.circle_bg4));


            ll2.setPadding(dpAsPixels,dpAsPixels2,dpAsPixels,dpAsPixels2);
            //ll1.setPadding(0,dpAsPixels,0,dpAsPixels);
            //ll3.setPadding(0,dpAsPixels,0,dpAsPixels);


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
        });

        noti.setOnClickListener(v -> {
            ll3.setBackground(getResources().getDrawable(R.drawable.circle_bg3));
            ll2.setBackground(getResources().getDrawable(R.drawable.circle_bg4));
            ll1.setBackground(getResources().getDrawable(R.drawable.circle_bg4));


            ll3.setPadding(dpAsPixels,dpAsPixels2,dpAsPixels,dpAsPixels2);
            //ll2.setPadding(0,dpAsPixels,0,dpAsPixels);
            //ll1.setPadding(0,dpAsPixels,0,dpAsPixels);


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
        });


        getImages();


    }


    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        ArrayList<GradientDrawable> gdx = new ArrayList<>();


        gdx.add(new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0x779c3636,0x77a12323}));

        gdx.add(new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0x772d59be,0x772d59be}));

        gdx.add(new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0x7739c450,0x7734b349}));

        gdx.add(new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0x77eaf03a,0x77dbe036}));


        gdx.get(0).setCornerRadius(50f);
        gdx.get(1).setCornerRadius(50f);
        gdx.get(2).setCornerRadius(50f);
        gdx.get(3).setCornerRadius(50f);

        ids.add("01.");
        texts.add("Quick Puzzle");
        btns.add("Play Now");
        clrs.add(gdx.get(0));

        ids.add("02.");
        texts.add("Easy Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(1));

        ids.add("03.");
        texts.add("Fun Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(2));

        ids.add("04.");
        texts.add("Hard Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(3));

        ids.add("01.");
        texts.add("Quick Puzzle");
        btns.add("Play Now");
        clrs.add(gdx.get(0));

        ids.add("02.");
        texts.add("Easy Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(1));

        ids.add("03.");
        texts.add("Fun Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(2));

        ids.add("04.");
        texts.add("Hard Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(3));

        ids.add("01.");
        texts.add("Quick Puzzle");
        btns.add("Play Now");
        clrs.add(gdx.get(0));

        ids.add("02.");
        texts.add("Easy Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(1));

        ids.add("03.");
        texts.add("Fun Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(2));

        ids.add("04.");
        texts.add("Hard Mode");
        btns.add("Play Now");
        clrs.add(gdx.get(3));

        initRecyclerView();

    }


    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, ids, texts, btns, clrs);
        recyclerView.setAdapter(adapter);
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

        if(unlockTrigger!=null)
            unregisterReceiver(unlockTrigger);

        startService(new Intent(this, BackService.class));
        super.onDestroy();


    }

    @Override
    public void onPause() {

        //unregisterReceiver(unlockTrigger);

        startService(new Intent(this, BackService.class));
        super.onPause();



    }

    @Override
    public void onStop() {
        //unregisterReceiver(unlockTrigger);

        startService(new Intent(this, BackService.class));
        super.onStop();



    }


    public void isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
        }
    }



}
