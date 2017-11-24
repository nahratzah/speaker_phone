package com.github.nahratzah.speakerphone.support;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;

/**
 * Singleton that holds the text-to-speech engine.
 */
public class TtsEngine {
    public static final int QUEUE_ADD = TextToSpeech.QUEUE_ADD;
    public static final int QUEUE_FLUSH = TextToSpeech.QUEUE_FLUSH;

    private static TtsEngine ourInstance = null;
    private TextToSpeech tts;
    private Bundle ttsBundle;

    public static synchronized TtsEngine getInstance(Context ctx) {
        if (ourInstance == null)
            ourInstance = new TtsEngine(ctx);
        return ourInstance;
    }

    private TtsEngine(Context ctx) {
        ttsBundle = new Bundle();
        final Context appCtx = ctx.getApplicationContext();
        tts = new TextToSpeech(appCtx, new TtsReady(appCtx));
    }

    /**
     * Enqueue text in the speech API.
     * @param text The text to speak.
     * @param queueMode The queueing mode to use.
     */
    public void speak(CharSequence text, int queueMode) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text, queueMode, ttsBundle, null);
        } else {
            final HashMap<String, String> bundleMap = new HashMap<>();
            for (String key : ttsBundle.keySet())
                bundleMap.put(key, ttsBundle.getString(key));
            //noinspection deprecation
            tts.speak(text.toString(), queueMode, bundleMap);
        }
    }

    /**
     * Enqueue text in the speech API.
     * @param text The text to speak.
     */
    public void speak(CharSequence text) {
        speak(text, QUEUE_ADD);
    }

    /**
     * Stop speaking.
     */
    public void stop() {
        tts.stop();
    }

    private class TtsReady implements TextToSpeech.OnInitListener {
        private final Context appCtx;

        private TtsReady(Context appCtx) {
            this.appCtx = appCtx;
        }

        @Override
        public void onInit(int status) {
            // XXX need to do something on initialization error or success.
            switch (status) {
                default:
                case TextToSpeech.ERROR:
//                    appCtx.startActivity(null);  // XXX show error or engine configuration menu?
                    /* SKIP */
                    break;
                case TextToSpeech.SUCCESS:
                    /* SKIP */
                    break;
            }
        }
    }
}
