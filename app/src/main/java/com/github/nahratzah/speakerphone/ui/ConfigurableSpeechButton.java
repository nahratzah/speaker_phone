package com.github.nahratzah.speakerphone.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.github.nahratzah.speakerphone.R;
import com.github.nahratzah.speakerphone.db.SpeechButtonDb;

/**
 * A speech button that stores its configuration in a database, according to a specified key.
 * Long press will bring up a button edit dialogue.
 */
public class ConfigurableSpeechButton extends AppCompatButton implements View.OnClickListener, View.OnLongClickListener {
    @Nullable
    private String text = null;
    @Nullable
    private String label = null;
    @Nullable
    private String key = null;

    public ConfigurableSpeechButton(Context context) {
        super(context);
        init(null);
    }

    public ConfigurableSpeechButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ConfigurableSpeechButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        setOnClickListener(this);
        setOnLongClickListener(this);

        // Figure out value for key.
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ConfigurableSpeechButton,
                0, 0);
        try {
            key = a.getString(R.styleable.ConfigurableSpeechButton_key);
        } finally {
            a.recycle();
        }

        initLabelAndText();
    }

    // Hook into onLayout callback to respond to database updates.
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        initLabelAndText();
        super.onLayout(changed, left, top, right, bottom);
    }

    private void initLabelAndText() {
        // Load label and text from database.
        if (key != null) {
            final SpeechButtonDb.DatabaseRO db = new SpeechButtonDb(getContext()).getReadOnlyDatabase();
            //noinspection TryFinallyCanBeTryWithResources
            try {
                final SpeechButtonDb.Entity entity = db.getByKey(key);
                if (entity != null) {
                    label = entity.label;
                    this.setText(label);
                    text = entity.text;
                }
            } finally {
                db.close();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (text != null)
            Toast.makeText(getContext(), "Speaking: " + text, Toast.LENGTH_LONG)
                    .show();
        else
            onLongClick(v);
    }

    @Override
    public boolean onLongClick(View v) {
        final Context ctx = getContext();
        final Intent intent = new Intent(ctx, ConfigurableSpeechButtonCfgActivity.class);
        intent.putExtra(ConfigurableSpeechButtonCfgActivity.DB_KEY, key);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            ctx.startActivity(intent, ActivityOptions.makeScaleUpAnimation(this, 0, 0, this.getWidth(), this.getHeight()).toBundle());
        else
            ctx.startActivity(intent);
        return false;
    }
}
