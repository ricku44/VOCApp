package com.example.greapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.adriangl.overlayhelper.OverlayHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.Settings;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private OverlayHelper overlayHelper;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent in = new Intent(getApplicationContext(),ServiceOverlay.class);
                startService(in);


            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
