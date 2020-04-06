package com.example.bq.ui.settings;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.bq.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        final EditTextPreference editTextPreference = findPreference("random_text");

        assert editTextPreference != null;
        editTextPreference.setOnBindEditTextListener(new EditTextPreference.OnBindEditTextListener() {
            @Override
            public void onBindEditText(@NonNull EditText editText) {
                editTextPreference.setSummary(editText.getText());
            }
        });
    }
}