package com.v2s;

import android.os.Bundle;
import android.preference.PreferenceActivity;



public class prefsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);
    }
    
}