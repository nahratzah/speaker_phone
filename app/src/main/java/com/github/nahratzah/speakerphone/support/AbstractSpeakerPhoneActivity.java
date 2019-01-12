package com.github.nahratzah.speakerphone.support;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.nahratzah.speakerphone.R;

/**
 * This class deals with certain UI things that are dealt with by every activity.
 *
 * In particular:
 * - the application bar at the top.
 */

public class AbstractSpeakerPhoneActivity extends AppCompatActivity {
    private ViewGroup.LayoutParams makeLayoutParams() {
        final ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return layoutParams;
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

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.support_abstract_speakerphone_activity);
        layout.addView(view, layoutParams);
    }
}
