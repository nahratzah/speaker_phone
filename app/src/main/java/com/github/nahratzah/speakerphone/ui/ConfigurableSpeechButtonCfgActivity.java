package com.github.nahratzah.speakerphone.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nahratzah.speakerphone.R;
import com.github.nahratzah.speakerphone.db.SpeechButtonDb;

import java.io.Closeable;
import java.util.Objects;

public class ConfigurableSpeechButtonCfgActivity extends AppCompatActivity implements TextWatcher {
    public static String DB_KEY = "db_key";

    private String key;

    private SpeechButtonDb.DatabaseRW db;
    private Button storeButton;
    private EditText buttonLabel;
    private EditText buttonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurable_speech_button_cfg);
        key = getIntent().getExtras().getString(DB_KEY);

        storeButton = (Button) findViewById(R.id.configurable_speech_button_store);
        buttonLabel = (EditText) findViewById(R.id.configurable_speech_button_label);
        buttonText = (EditText) findViewById(R.id.configurable_speech_button_text);

        buttonLabel.addTextChangedListener(this);
        buttonText.addTextChangedListener(this);

        db = new SpeechButtonDb(this).getReadWriteDatabase();
        final SpeechButtonDb.Entity entity = db.getByKey(key);
        if (entity != null) {
            buttonLabel.setText(entity.label);
            buttonText.setText(entity.text);
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

    public void store(View v) {
        db.setEntity(key, buttonLabel.getText().toString(), buttonText.getText().toString());
        onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        storeButton.setEnabled(buttonText.getText().length() > 0 && buttonLabel.getText().length() > 0);
    }
}
