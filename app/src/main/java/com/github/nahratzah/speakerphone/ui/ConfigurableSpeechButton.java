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

import com.github.nahratzah.speakerphone.R;
import com.github.nahratzah.speakerphone.db.SpeechButtonDb;
import com.github.nahratzah.speakerphone.support.TtsEngine;

/**
 * A speech button that stores its configuration in a database, according to a specified key.
 * Long press will bring up a button edit dialogue.
 */
public class ConfigurableSpeechButton extends AppCompatButton implements View.OnClickListener, View.OnLongClickListener {
    @Nullable
    private String text = null;
    @Nullable
    private String key = null;

    private TtsEngine tts;

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
        if (!isInEditMode()) tts = TtsEngine.getInstance(getContext());
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

    @Nullable
    public String getKey() {
        return key;
    }

    public void setKey(@Nullable String key) {
        this.key=key;
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
        if (!isInEditMode() && key != null) {
            final SpeechButtonDb.DatabaseRO db = new SpeechButtonDb(getContext()).getReadOnlyDatabase();
            //noinspection TryFinallyCanBeTryWithResources
            try {
                final SpeechButtonDb.Entity entity = db.getByKey(key);
                if (entity != null) {
                    this.setText(entity.label);
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
            tts.speak(text, TtsEngine.QUEUE_FLUSH);
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
