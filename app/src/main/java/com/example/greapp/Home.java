package com.example.greapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import com.adriangl.overlayhelper.OverlayHelper;
import com.example.greapp.ui.dashboard.DashboardFragment;
import com.example.greapp.ui.home.HomeFragment;
import com.example.greapp.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Home extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<GradientDrawable> clrs = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<String> btns = new ArrayList<>();
    private OverlayHelper overlayHelper;
    private UnlockTrigger unlockTrigger;
    SmoothBottomBar nav;
    FragmentManager manager;
    public static TextToSpeech t1 = null;
    private int previousView=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        stopService(new Intent(this, BackService.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nav = findViewById(R.id.nav_view);
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();

        nav.setOnItemSelectedListener(this::onNavigationItemSelected);

        nav.setOnItemReselectedListener(this::onNavigationItemSelected);


        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0xFF181d27, 0xFF0c0c0c});
        gd.setCornerRadius(0f);

        //getWindow().getDecorView().setBackground(gd);

        changeStatusBarColor();

        isWriteStoragePermissionGranted();

        overlayHelper = new OverlayHelper(this.getApplicationContext(), new OverlayHelper.OverlayPermissionChangedListener() {
            @Override
            public void onOverlayPermissionCancelled() {
                Toast.makeText(Home.this, "Permissions required to run this App", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOverlayPermissionGranted() {
                unlockTrigger = new UnlockTrigger();
                registerReceiver(unlockTrigger, new IntentFilter("android.intent.action.USER_PRESENT"));
            }

            @Override
            public void onOverlayPermissionDenied() {
                Toast.makeText(Home.this, "Permissions required to run this App", Toast.LENGTH_SHORT).show();
            }
        });


        overlayHelper.startWatching();

        if (!Settings.canDrawOverlays(this)) {

            overlayHelper.requestDrawOverlaysPermission(
                    Home.this,
                    "Setting up the app",
                    "Provide overlay permissions for this app to work",
                    "Enable",
                    "Cancel");
        }
    }


    private void changeStatusBarColor() {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        overlayHelper.onRequestDrawOverlaysPermissionResult(requestCode);

    }

    @Override
    protected void onPause() {

        try {
            if(unlockTrigger!=null)
                unregisterReceiver(unlockTrigger);
        } catch (IllegalArgumentException e) { e.printStackTrace(); }

        startService(new Intent(this, BackService.class));

        super.onPause();
    }


    @Override
    protected void onResume() {

        stopService(new Intent(this, BackService.class));

        if(Settings.canDrawOverlays(this))
            registerReceiver(unlockTrigger, new IntentFilter("android.intent.action.USER_PRESENT"));

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    public void isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

    }


    public boolean onNavigationItemSelected(int i) {

        switch (i) {
            case 0:
                if(previousView!=0)
                    manager.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                break;
            case 1:
                if(previousView<1)
                    manager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.nav_host_fragment, new DashboardFragment()).commit();
                else if(previousView>1)
                    manager.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.nav_host_fragment, new DashboardFragment()).commit();
                break;
            case 2:
                if(previousView!=2)
                    manager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.nav_host_fragment, new NotificationsFragment()).commit();
                break;
        }

        previousView = i;
        return true;
    }

}
