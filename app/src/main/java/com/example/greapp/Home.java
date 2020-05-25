package com.example.greapp;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    private ArrayList<GradientDrawable> clrs = new ArrayList<>();
    private ArrayList<String> ids = new ArrayList<>();
    private ArrayList<String> texts = new ArrayList<>();
    private ArrayList<String> btns = new ArrayList<>();
    private OverlayHelper overlayHelper;
    private UnlockTrigger unlockTrigger;
    BottomNavigationView nav;
    FragmentManager manager;
    public static TextToSpeech t1 = null;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        stopService(new Intent(this, BackService.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nav = findViewById(R.id.nav_view);
        manager = getSupportFragmentManager();

        nav.setElevation(0);
        manager.beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
        BottomNavigationViewHelper.changePosition(nav, 0);
        nav.setOnNavigationItemSelectedListener(this);


        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0xFF181d27, 0xFF0c0c0c});
        gd.setCornerRadius(0f);

        getWindow().getDecorView().setBackground(gd);


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

                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    boolean previouslyStarted1 = prefs.getBoolean(getString(R.string.pref_previously_started1), false);
                    if (!previouslyStarted1) {
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putBoolean(getString(R.string.pref_previously_started1), Boolean.TRUE);
                        edit.apply();

                        Intent intent = new Intent();

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getApplicationContext().getPackageName());
                            intent.putExtra(Settings.EXTRA_CHANNEL_ID, "com.example.GreApp");
                        } else {
                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                            intent.putExtra("app_package", getApplicationContext().getPackageName());
                            intent.putExtra("app_uid", getApplicationContext().getApplicationInfo().uid);
                        }

                        startActivity(intent);
                        startService(new Intent(getApplicationContext(), InstOverlay.class));
                    }
                }
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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted2 = prefs.getBoolean(getString(R.string.pref_previously_started2), false);
        if (!previouslyStarted2 && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {

            builder = new AlertDialog.Builder(this);

            builder.setMessage("Please disable the notification for better experience.")
                .setCancelable(false)
                .setPositiveButton("Proceed", (dialog, id) -> {

                   SharedPreferences.Editor edit = prefs.edit();
                   edit.putBoolean(getString(R.string.pref_previously_started2), Boolean.TRUE);
                   edit.apply();

                   Intent intent = new Intent();

                   intent.setAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                   intent.putExtra(Settings.EXTRA_APP_PACKAGE, getApplicationContext().getPackageName());
                   intent.putExtra(Settings.EXTRA_CHANNEL_ID, "com.example.GreApp");

                   startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                   dialog.cancel();
                });
            
            AlertDialog alert = builder.create();
            alert.setTitle("Next Steps");
            alert.show();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                BottomNavigationViewHelper.changePosition(nav, 0);
                manager.beginTransaction().replace(R.id.nav_host_fragment, new HomeFragment()).commit();
                break;
            case R.id.navigation_dashboard:
                BottomNavigationViewHelper.changePosition(nav, 1);
                manager.beginTransaction().replace(R.id.nav_host_fragment, new DashboardFragment()).commit();
                break;
            case R.id.navigation_notifications:
                BottomNavigationViewHelper.changePosition(nav, 2);
                manager.beginTransaction().replace(R.id.nav_host_fragment, new NotificationsFragment()).commit();
                break;
        }

        return false;
    }

}
