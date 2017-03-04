package com.github.nahratzah.speakerphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.nahratzah.speakerphone.support.TtsEngine;

public class BigButtonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_button);
    }

    public void dispatchSpeakActivity(View v) {
        TtsEngine.getInstance(this).stop();  // Remove pending speach.
        startActivity(new Intent(this, SpeakActivity.class));
    }
}
