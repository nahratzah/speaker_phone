package com.github.nahratzah.speakerphone.support;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.nahratzah.speakerphone.R;
import com.github.nahratzah.speakerphone.SettingsActivity;

/**
 * This class deals with certain UI things that are dealt with by every activity.
 *
 * In particular:
 * - the application bar at the top.
 */

public abstract class AbstractSpeakerPhoneActivity extends AppCompatActivity {
    private ViewGroup.LayoutParams makeLayoutParams() {
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        return layoutParams;
    }

    protected SharedPreferences getPrefs() {
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preferences, false);
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    /**
     * Safely read an `int` preference.
     *
     * In the case that the preference doesn't hold an integer, a warning message will be logged and the default value returned.
     * @param key The key of the preference.
     * @param defValue The default value, to use if the preference is absent.
     * @return The `int` value stored in the preference.
     * If the preference is unset/absent, the `defValue` is returned.
     */
    protected int getIntPrefOrDefault(String key, int defValue) {
        try {
            return getPrefs().getInt(key, defValue);
        } catch (ClassCastException ex) {
            Log.w("SpeakerPhone_preference", "Bad preference: " + key, ex);
            return defValue;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        setContentView(getLayoutInflater().inflate(layoutResId, null));
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, makeLayoutParams());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        super.setContentView(R.layout.support_abstract_speakerphone_activity);

        final Toolbar myToolbar = getToolbar();
        setSupportActionBar(myToolbar);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.support_abstract_speakerphone_activity);
        layout.addView(view, layoutParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    protected Toolbar getToolbar() {
        return findViewById(R.id.my_toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
