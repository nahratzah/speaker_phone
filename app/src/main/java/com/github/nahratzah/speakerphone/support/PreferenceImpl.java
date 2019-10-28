package com.github.nahratzah.speakerphone.support;

import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.text.InputType;

import com.github.nahratzah.speakerphone.R;

/**
 * Created by ariane on 1/12/2019.
 */

public class PreferenceImpl extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        final EditTextPreference columnsPref = (EditTextPreference) findPreference("big_buttons_num_columns");
        final EditTextPreference rowsPref = (EditTextPreference) findPreference("big_buttons_num_rows");
//        columnsPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
//        rowsPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
    }
}
