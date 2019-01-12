package com.github.nahratzah.speakerphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.GridLayout.LayoutParams;
import android.view.View;

import com.github.nahratzah.speakerphone.support.TtsEngine;
import com.github.nahratzah.speakerphone.ui.ConfigurableSpeechButton;

public class BigButtonActivity extends AppCompatActivity {
    /**
     * The root layout of this activity.
     */
    private GridLayout layout;

    private void rebuildLayout() {
        layout.removeAllViews();
        final int numColumns = layout.getColumnCount();
        final int numRows = layout.getRowCount();
        for (int row = 0; row < numRows; ++row) {
            for (int column = 0; column < numColumns; ++column) {
                final View cell = createViewAtGridIdx(row, column, numRows, numColumns);
                final GridLayout.LayoutParams layoutParams = new LayoutParams(GridLayout.spec(row, 1f), GridLayout.spec(column, 1f));
                layout.addView(cell, layoutParams);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_button);
        this.layout = findViewById(R.id.activity_big_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        rebuildLayout();
    }

    public void dispatchSpeakActivity(View v) {
        TtsEngine.getInstance(this).stop();  // Remove pending speach.
        startActivity(new Intent(this, SpeakActivity.class));
    }

    private View createViewAtGridIdx(int row, int column, int numRows, int numColumns) {
        if (row == 0 && column == 0)
            return createFreeFormButton();

        if (row == numRows - 1 && column == numColumns - 1)
            return createAlertButton();

        return createProgrammableButton(row, column);
    }

    private View createFreeFormButton() {
        return this.getLayoutInflater().inflate(R.layout.activity_big_button__dispatch_speak_activity_button, null);
    }

    private View createAlertButton() {
        final ConfigurableSpeechButton alertButton = (ConfigurableSpeechButton) this.getLayoutInflater().inflate(R.layout.activity_big_button__alert_button, null);
        alertButton.setKey("activity_big_button_alert");
        return alertButton;
    }

    private View createProgrammableButton(int row, int column) {
        final ConfigurableSpeechButton button = (ConfigurableSpeechButton) this.getLayoutInflater().inflate(R.layout.activity_big_button__configurable_speak_button, null);
        button.setKey(String.format("activity_big_button_%d_%d", row, column));
        return button;
    }
}
