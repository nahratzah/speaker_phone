package com.github.nahratzah.speakerphone;

import android.annotation.TargetApi;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

public class SpeakActivity extends AppCompatActivity {
    private static final String TAG = SpeakActivity.class.getName();

    /** Bundle String key: the value of lastSentence. */
    private static final String BUNDLE_LAST_SENTENCE = "lastSentence";

    private EditText input;
    private Button repeatButton;
    private TextToSpeech tts;
    private Bundle ttsBundle;
    private String lastSentence = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);

        input = (EditText) findViewById(R.id.input);
        input.setOnEditorActionListener(new InputEnterHandler());
        input.setOnKeyListener(new InputKeyListener());

        repeatButton = (Button) findViewById(R.id.repeatButton);

        ttsBundle = new Bundle();
        tts = new TextToSpeech(getApplicationContext(), new TtsReady());
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
            speakSentence(text);
            text.clear();
        } finally {
            input.endBatchEdit();
        }
    }

    /**
     * Set the text on the repeat button after 'lastSentence' has been updated.
     */
    private void updateRepeatButton() {
        if (lastSentence.trim().isEmpty())
            repeatButton.setText(R.string.speak_repeat_button_hint);
        else
            repeatButton.setText(lastSentence);
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, TextToSpeech.QUEUE_ADD, ttsBundle, null);
        } else {
            final HashMap<String, String> bundleMap = new HashMap<>();
            for (String key : ttsBundle.keySet())
                bundleMap.put(key, ttsBundle.getString(key));
            //noinspection deprecation
            tts.speak(text.toString(), TextToSpeech.QUEUE_ADD, bundleMap);
        }
    }

    private static class TtsReady implements TextToSpeech.OnInitListener {
        @Override
        public void onInit(int status) {
            // XXX need to do something on initialization error or success.
            switch (status) {
                default:
                case TextToSpeech.ERROR:
                    /* SKIP */
                    break;
                case TextToSpeech.SUCCESS:
                    /* SKIP */
                    break;
            }
        }
    }

    private class InputEnterHandler implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.d(TAG, "onEditorAction(" + v + ", " + actionId + ", " + event + ")");
            switch (actionId) {
                default:
                    return false;
                case EditorInfo.IME_NULL:
                    if (event.getAction() == KeyEvent.ACTION_DOWN)
                        /* FALLTHROUGH */;
                    else
                        return false;
                case EditorInfo.IME_ACTION_DONE:
                    speak();
                    return true;
            }
        }
    }

    private class InputKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.d(TAG, "onKey(" + v + ", " + keyCode + ", " + event + ")");
            switch (keyCode) {
                case KeyEvent.KEYCODE_C:  // Stop speaking.
                    if (event.isCtrlPressed() && event.getAction() == KeyEvent.ACTION_UP) {
                        tts.stop();
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_R:  // Repeat last sentence.
                    if (event.isCtrlPressed() && event.getAction() == KeyEvent.ACTION_UP) {
                        repeatInput(null);
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_U:  // Clear entire line.
                    if (event.isCtrlPressed() && event.getAction() == KeyEvent.ACTION_UP) {
                        ((EditText)v).getEditableText().clear();
                        return true;
                    }
                    break;
            }
            return false;
        }
    }
}
