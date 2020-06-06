package com.example.greapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

import static com.example.greapp.SettingsActivity.SettingsFragment.formatTime;

public class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    private TimePicker mTimePicker;
    static boolean bool = true;
    static int startTime, endTime;
    static Fragment frag = null;

    public static TimePreferenceDialogFragmentCompat newInstance(String key, Boolean b1, Fragment x) {
        final TimePreferenceDialogFragmentCompat fragment = new TimePreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        bool = b1;
        frag = x;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                .setTitle(bool?"Start Time":"End Time")
                .setPositiveButton(bool?"Next":"OK",
                        (dialog, whichButton) -> {
                            onDialogClosed(true);
                        }
                )
                .setNegativeButton("Cancel",
                        (dialog, whichButton) -> dialog.dismiss()
                );

        LayoutInflater i = getActivity().getLayoutInflater();

        View v = i.inflate(R.layout.pref_dialog_time,null);
        onBindDialogView(v);
        b.setView(v);
        return b.create();
    }


    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mTimePicker = view.findViewById(R.id.edit);

        // Exception: There is no TimePicker with the id 'edit' in the dialog.
        if (mTimePicker == null) {
            throw new IllegalStateException("Dialog view must contain a TimePicker with id 'edit'");
        }

        // Get the time from the related Preference
        Integer minutesAfterMidnight = null;
        DialogPreference preference = getPreference();
        if (preference instanceof TimePreference) {
            if(bool)
                minutesAfterMidnight = ((TimePreference) preference).getTime1();
            else
                minutesAfterMidnight = ((TimePreference) preference).getTime2();
        }

        // Set the time to the TimePicker
        if (minutesAfterMidnight != null) {
            int hours = minutesAfterMidnight / 60;
            int minutes = minutesAfterMidnight % 60;
            boolean is24hour = DateFormat.is24HourFormat(getContext());

            mTimePicker.setIs24HourView(is24hour);
            mTimePicker.setHour(hours);
            mTimePicker.setMinute(minutes);
        }
    }


    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // Get the current values from the TimePicker
            int hours;
            int minutes;
            if (Build.VERSION.SDK_INT >= 23) {
                hours = mTimePicker.getHour();
                minutes = mTimePicker.getMinute();
            } else {
                hours = mTimePicker.getHour();
                minutes = mTimePicker.getMinute();
            }

            int minutesAfterMidnight = (hours * 60) + minutes;

            DialogPreference preference = getPreference();
            if (preference instanceof TimePreference) {
                TimePreference timePreference = ((TimePreference) preference);
                if (timePreference.callChangeListener(minutesAfterMidnight)) {
                    if(bool){
                        timePreference.setTime1(minutesAfterMidnight);
                        startTime = minutesAfterMidnight;
                        DialogFragment dialogFragment = TimePreferenceDialogFragmentCompat.newInstance(preference.getKey(),false, frag);

                        if (dialogFragment != null) {
                            dialogFragment.setTargetFragment(frag, 0);
                            dialogFragment.show(this.getParentFragmentManager(), "DIALOG2");
                        }
                    } else {
                        endTime = minutesAfterMidnight;
                        timePreference.setTime2(minutesAfterMidnight);
                    }
                }
            }
        }
    }
}