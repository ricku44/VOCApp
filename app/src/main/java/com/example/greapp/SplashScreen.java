package com.example.greapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView main = findViewById(R.id.imageView);
        ImageView cloud0 = findViewById(R.id.cloud0);
        ImageView cloud1 = findViewById(R.id.cloud1);
        ImageView cloud2 = findViewById(R.id.cloud2);


        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        Animation aniFadeO = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        main.startAnimation(aniFade);

        main.animate().scaleY(1F).setDuration(4000);
        main.animate().scaleX(1F).setDuration(4000);

        cloud0.startAnimation(aniFadeO);
        cloud1.startAnimation(aniFadeO);
        cloud2.startAnimation(aniFadeO);

        Animation aniFade1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate);
        Animation aniFade2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate0);
        cloud0.startAnimation(aniFade1);
        cloud1.startAnimation(aniFade2);
        cloud2.startAnimation(aniFade2);


        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this, Onboarding.class);
            startActivity(i);
            finish();
        }, 3000);
    }
}