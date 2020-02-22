package com.example.greapp;



import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;

class BottomNavigationViewHelper {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("RestrictedApi")
    static void changePosition(BottomNavigationView view, int x) {


        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {

            for (int i = 0; i < menuView.getChildCount(); i++) {

                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(90,90);
                params.gravity = Gravity.BOTTOM|Gravity.START;

                //menuView.setItemIconSize(75);

                ImageView icon = item.findViewById(R.id.icon);
                TextView small = item.findViewById(R.id.smallLabel);
                TextView large = item.findViewById(R.id.largeLabel);

                icon.setLayoutParams(params);

                small.setTextSize(20);
                large.setTextSize(20);

                large.setGravity(Gravity.TOP | Gravity.END);
                large.setPadding(30,0,0,20);



                small.setGravity(Gravity.TOP | Gravity.END);
                small.setPadding(30,0,0,20);


                large.setTypeface(large.getTypeface(), Typeface.BOLD);
                small.setTypeface(small.getTypeface(), Typeface.BOLD);


                if(i==x) {
                    if(x==0) {
                        large.setVisibility(View.VISIBLE);
                        small.setVisibility(View.GONE);
                        item.setPadding(0, 0, 0, 0);
                        //item.setBackgroundResource(R.drawable.circle_bg3);
                    } else {
                        if(i==1) {
                            small.setPadding(50,0,0,20);
                            item.setPadding(0, 0, 0, 0);
                            //item.setBackgroundResource(R.drawable.circle_bg3);

                        }
                        if(i==2) {
                            item.setPadding(0, 0, 0, 0);
                            //item.setBackgroundResource(R.drawable.circle_bg3);
                        }
                            large.setVisibility(View.GONE);
                        small.setVisibility(View.VISIBLE);
                    }
                } else {
                    if(i==0)
                        item.setPadding(30, 0, 0, 0);
                    if(i==1)
                        item.setPadding(50, 0, 0, 0);
                    if(i==2)
                        item.setPadding(30, 0, 0, 0);
                    large.setVisibility(View.GONE);
                    small.setVisibility(View.GONE);
                    //item.setBackgroundResource(0);
                }



            }
        } catch (Exception e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        }
    }
}