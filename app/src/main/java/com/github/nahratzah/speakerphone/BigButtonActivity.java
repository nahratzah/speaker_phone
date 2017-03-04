package com.github.nahratzah.speakerphone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BigButtonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_button);
    }

    public void dispatchSpeakActivity(View v) {
        startActivity(new Intent(this, SpeakActivity.class));
    }
}
