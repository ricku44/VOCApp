package com.example.greapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class InstNotification extends RelativeLayout {

    LayoutInflater inflater;

    public InstNotification(Context context) {
        super(context);
        initView(context);
    }

    private void initView(final Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_inst_notification, null);

        OnClickListener listener = v -> context.stopService(new Intent(getContext(), InstOverlay.class));

        view.setOnClickListener(listener);
        view.findViewById(R.id.close).setOnClickListener(listener);

        this.addView(view);
    }

}
