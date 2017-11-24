package com.github.nahratzah.speakerphone.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.github.nahratzah.speakerphone.BuildConfig;
import com.github.nahratzah.speakerphone.support.TtsEngine;

/**
 * An EditText that contains the special key bindings handlers.
 *
 * Use this instead of the normal EditText, to maintain a consistent use of text edit inputs across
 * the app.
 */

public class SPEditText extends AppCompatEditText {
    private static final String TAG = SPEditText.class.getName();
    final TtsEngine tts;  // Package visibility, to prevent access method creation.

    public SPEditText(Context context) {
        super(context);

        if (!isInEditMode())
            tts = TtsEngine.getInstance(context);
        else
            tts = null;
        setOnKeyListener(null);

        this.setRawInputType(0);
    }

    public SPEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode())
            tts = TtsEngine.getInstance(context);
        else
            tts = null;
        setOnKeyListener(null);
    }

    public SPEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode())
            tts = TtsEngine.getInstance(context);
        else
            tts = null;
        setOnKeyListener(null);
    }

    /**
     * Register a callback to be invoked when a hardware key is pressed in this view.
     * Key presses in software input methods will generally not trigger the methods of
     * this listener.
     *
     * @param l the key listener to attach to this view
     */
    @Override
    public void setOnKeyListener(@Nullable OnKeyListener l) {
        super.setOnKeyListener(new EditorWrapper(l));
    }

    private class EditorWrapper implements OnKeyListener {
        @Nullable
        private final OnKeyListener wrapped_;

        EditorWrapper(@Nullable OnKeyListener wrapped) {
            this.wrapped_ = wrapped;
        }

        /**
         * Called when a hardware key is dispatched to a view. This allows listeners to
         * get a chance to respond before the target view.
         * <p>Key presses in software keyboards will generally NOT trigger this method,
         * although some may elect to do so in some situations. Do not assume a
         * software input method has to be key-based; even if it is, it may use key presses
         * in a different way than you expect, so there is no way to reliably catch soft
         * input key presses.
         *
         * @param v       The view the key has been dispatched to.
         * @param keyCode The code for the physical key that was pressed
         * @param event   The KeyEvent object containing full information about
         *                the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (wrapped_ != null && wrapped_.onKey(v, keyCode, event))
                return true;

            Log.d(TAG, "onKey(" + v + ", " + keyCode + ", " + event + ")");
            if (BuildConfig.DEBUG && v != SPEditText.this) throw new AssertionError();
            switch (keyCode) {
                case KeyEvent.KEYCODE_C:  // Stop speaking.
                    if (event.isCtrlPressed() && event.getAction() == KeyEvent.ACTION_UP) {
                        tts.stop();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_U:  // Clear entire line.
                    if (event.isCtrlPressed() && event.getAction() == KeyEvent.ACTION_UP) {
                        getEditableText().clear();
                        return true;
                    }
                    break;
            }
            return false;
        }
    }
}
