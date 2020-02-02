package com.example.greapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Problem extends LinearLayout {

    LayoutInflater inflater;

    public Problem(Context context) {
        super(context);
        initView(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        for(int i = 0 ; i < getChildCount() ; i++){
            getChildAt(i).layout(l, t, r, b);
        }
    }













    //  Click events Below >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>













    private void initView(final Context context){
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.overlay, null);

        final Button op1 = view.findViewById(R.id.btn1);
        final TextView t1 = view.findViewById(R.id.txt1);

        op1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.exit(0);
                context.stopService(new Intent(getContext(), ServiceOverlay.class));            }
        });


        Button op2 = view.findViewById(R.id.btn2);

        op2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                op1.setTextColor(Color.RED);
                t1.setTextColor(Color.RED);
            }
        });

        Button op3 = view.findViewById(R.id.btn3);

        op3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                op1.setTextColor(Color.RED);
                t1.setTextColor(Color.RED);
            }
        });

        Button op4 = view.findViewById(R.id.btn4);

        op4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                op1.setTextColor(Color.RED);
                t1.setTextColor(Color.RED);
            }
        });



        this.addView(view);
    }

}
