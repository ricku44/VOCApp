package com.example.greapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;

import androidx.preference.DialogPreference;

import static com.example.greapp.SettingsActivity.SettingsFragment.formatTime;

public class TimePreference extends DialogPreference {

    private int mTime1, mTime2;
    private int mDialogLayoutResId = R.layout.pref_dialog_time;
    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    public TimePreference(Context context) {
        this(context, null);
        shared(context);
    }

    public TimePreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.preferenceStyle);
        shared(context);
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
        shared(context);
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        shared(context);
    }


    public int getTime1() {
        return mTime1;
    }

    public int getTime2() {
        return mTime1;
    }

    public void setTime1(int startTime) {
        mTime1 = startTime;
        edit = prefs.edit();
        edit.putInt("startTime", mTime1);
        edit.apply();

    }

    public void setTime2(int endTime) {
        mTime2 = endTime;
        edit = prefs.edit();
        edit.putInt("endTime", mTime2);
        edit.apply();
        setSummary("From "+ formatTime(prefs.getInt("startTime",0))+" To "+formatTime(prefs.getInt("endTime",0)));
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }


    @Override
    public int getDialogLayoutResource() {
        return mDialogLayoutResId;
    }

    private void shared(Context mContext) {
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    }
}