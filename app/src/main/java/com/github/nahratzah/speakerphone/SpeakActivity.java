package com.github.nahratzah.speakerphone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nahratzah.speakerphone.support.TtsEngine;

public class SpeakActivity extends AppCompatActivity {
    private static final String TAG = SpeakActivity.class.getName();

    /** Bundle String key: the value of lastSentence. */
    private static final String BUNDLE_LAST_SENTENCE = "lastSentence";

    private EditText input;
    private Button repeatButton;
    private @Nullable String lastSentence = null;
    private TtsEngine tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);

        tts = TtsEngine.getInstance(this);

        input = (EditText) findViewById(R.id.input);
        input.setOnEditorActionListener(new InputEnterHandler());
        input.setOnKeyListener(new InputKeyListener());
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setRawInputType(InputType.TYPE_CLASS_TEXT);
        input.requestFocus();  // Start with focus on text input.

        repeatButton = (Button) findViewById(R.id.repeatButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.menu_settings:
                Toast.makeText(this, "Settings is not implemented.", Toast.LENGTH_SHORT)
                        .show();
                return true;
            default:
                break;
        }

        return false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastSentence = savedInstanceState.getString(BUNDLE_LAST_SENTENCE);
        updateRepeatButton();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_LAST_SENTENCE, lastSentence);
        super.onSaveInstanceState(outState);
    }

    /**
     * Send input to speech engine and clear the text input.
     */
    void speak() {  // Package visibility, to avoid access method creation.
        final Editable text = input.getEditableText();
        input.beginBatchEdit();
        try {
            lastSentence = text.toString();
            updateRepeatButton();
            speakSentence(lastSentence);
            text.clear();
        } finally {
            input.endBatchEdit();
        }
    }

    /**
     * Set the text on the repeat button after 'lastSentence' has been updated.
     */
    private void updateRepeatButton() {
        if (lastSentence == null || lastSentence.trim().isEmpty()) {
            repeatButton.setText(R.string.speak_repeat_button_hint);
            repeatButton.setEnabled(false);
        } else {
            repeatButton.setText(lastSentence);
            repeatButton.setEnabled(true);
        }
    }

    /**
     * Speak the last sentence on the repeat button.
     * @param v The repeat button view (unused).
     */
    public void repeatInput(@Nullable View v) {
        if (lastSentence != null)
            speakSentence(lastSentence);
    }

    /**
     * Enqueue text in the speech API.
     * Wrapper around different API versions of the speech API.
     * @param text The text to speak.
     */
    private void speakSentence(CharSequence text) {
        tts.speak(text);
    }

    private class InputEnterHandler implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.d(TAG, "onEditorAction(" + v + ", " + actionId + ", " + event + ")");
            if (event == null) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    speak();
                    return true;
                }
            } else if (actionId == EditorInfo.IME_NULL) {
                if (event.getAction() == KeyEvent.ACTION_UP)
                    speak();
                if (event.getAction() == KeyEvent.ACTION_UP || event.getAction() == KeyEvent.ACTION_DOWN)
                    return true;
            }

            return false;
        }
    }

    private class InputKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.d(TAG, "onKey(" + v + ", " + keyCode + ", " + event + ")");
            switch (keyCode) {
                case KeyEvent.KEYCODE_R:  // Repeat last sentence.
                    if (event.isCtrlPressed() && event.getAction() == KeyEvent.ACTION_UP) {
                        repeatInput(null);
                        return true;
                    }
                    break;
            }
            return false;
        }
    }
}
