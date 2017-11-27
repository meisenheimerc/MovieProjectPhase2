package com.example.android.movieprojectphase2.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;

import com.example.android.movieprojectphase2.R;



public class SettingsFragment extends PreferenceFragmentCompat {
    SharedPreferences mSharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);

        getPreferenceScreen().getSharedPreferences();

        SwitchPreferenceCompat switchSort = (SwitchPreferenceCompat) findPreference("show_popular_key");

        if (switchSort != null) {
            switchSort.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object isPopularSort) {

                    boolean isPopularSortOn = ((Boolean)isPopularSort).booleanValue();
                    boolean isHighestSortOn = ((Boolean)isPopularSort).booleanValue();

                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean(getString(R.string.show_popular_key), isPopularSortOn);
                    editor.putBoolean(getString(R.string.show_highest_rated_key), isHighestSortOn);
                    editor.commit();
                    return true;
                }
            });
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

}
