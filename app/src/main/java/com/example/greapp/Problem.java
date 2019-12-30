package com.example.greapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

        Button op1 = view.findViewById(R.id.btn1);
        final Button op5 = view.findViewById(R.id.btn5);

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
                op5.setVisibility(VISIBLE);
                op5.setText("Correct A");
            }
        });

        Button op3 = view.findViewById(R.id.btn3);

        op3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                op5.setVisibility(VISIBLE);
                op5.setText("Correct A");
            }
        });

        Button op4 = view.findViewById(R.id.btn4);

        op4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                op5.setVisibility(VISIBLE);
                op5.setText("Correct A");
            }
        });



        this.addView(view);
    }

}
