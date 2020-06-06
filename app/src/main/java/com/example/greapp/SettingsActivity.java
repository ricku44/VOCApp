package com.example.greapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            SharedPreferences sp = getPreferenceScreen().getSharedPreferences();

            TimePreference timePreference = findPreference("key4");
            int i = sp.getInt("startTime",0);
            int j = sp.getInt("endTime",0);
            if(i!=0 && j!=0)
                timePreference.setSummary("From "+ formatTime(i)+" To "+formatTime(j));

        }

        @Override
        public void onDisplayPreferenceDialog(Preference preference) {

            DialogFragment dialogFragment = null;
            if (preference instanceof TimePreference) {
                dialogFragment = TimePreferenceDialogFragmentCompat.newInstance(preference.getKey(),true, this);
            }

            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(this.getParentFragmentManager(), "DIALOG");
            } else {
                super.onDisplayPreferenceDialog(preference);
            }
        }

        static String formatTime(int totalMinutes) {
            String minutes = Integer.toString(totalMinutes % 60);
            minutes = minutes.length() == 1 ? "0" + minutes : minutes;
            String ampm;
            int hrs = totalMinutes/60;
            if(hrs>=12) {
                if(hrs>12)
                    hrs-=12;
                ampm = " PM";
            } else
                ampm = " AM";

            return hrs + ":" + minutes + ampm;
        }

    }
}