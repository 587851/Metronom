package com.example.metronome;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.metronome.Tuner.TunerActivity;
import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText tempoText;
    private TextView warningText;
    static public Metronome metronome;
    //private WakeLock wakeLock;
    private Button playTempoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        // Acquire WakeLock to prevent the device from sleeping
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "app:metronomewakelock");
        wakeLock.acquire();
        */

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        metronome = new Metronome(this);

        tempoText = findViewById(R.id.tempoText);
        tempoText.addTextChangedListener(this);
        warningText = findViewById(R.id.textView);

        Slider tempoSlider = findViewById(R.id.slider);
        tempoSlider.addOnChangeListener((slider, value, fromUser) -> {
            int tempo = (int) value;
            metronome.setTempo(tempo);
            metronome.changeActiveTempo();
            tempoText.setText(String.valueOf(tempo));
        });

        Button addFiveButton = findViewById(R.id.buttonAddFive);
        addFiveButton.setOnClickListener(this);
        Button removeFiveButton = findViewById(R.id.buttonRemoveFive);
        removeFiveButton.setOnClickListener(this);
        Button addOneButton = findViewById(R.id.buttonAddOne);
        addOneButton.setOnClickListener(this);
        Button removeOneButton = findViewById(R.id.buttonRemoveOne);
        removeOneButton.setOnClickListener(this);
        playTempoButton = findViewById(R.id.buttonPlayTempo);
        playTempoButton.setOnClickListener(this);
        Button tunerButton = findViewById(R.id.tuner_button);
        tunerButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            stopMetronome();

            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonAddFive) {
            metronome.addToTempo(5);
        } else if (id == R.id.buttonRemoveFive) {
            metronome.removeFromTempo(5);
        } else if (id == R.id.buttonAddOne) {
            metronome.addToTempo(1);
        } else if (id == R.id.buttonRemoveOne) {
            metronome.removeFromTempo(1);
        } else if (id == R.id.buttonPlayTempo) {
            if (!metronome.getIsPlaying()) {
                startMetronome();
            } else {
                stopMetronome();
            }
        } else if (id == R.id.tuner_button){
            stopMetronome();
            Intent intent = new Intent(MainActivity.this, TunerActivity.class);
            startActivity(intent);
            this.finish();
        }
        tempoText.setText(String.valueOf(metronome.getTempo()));
    }

    public void startMetronome() {
        playTempoButton.setText("Stop");
        metronome.start();
    }

    public void stopMetronome() {
        playTempoButton.setText("Start");
        metronome.stop();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        int tempo;
        try {
            tempo = Integer.parseInt(tempoText.getText().toString());
        } catch (NumberFormatException e) {
            tempo = 1;
            warningText.setText(getString(R.string.tempoWarning));
        }

        if (tempo > 300) {
            tempo = 300;
            warningText.setText(getString(R.string.tempoWarning));
        } else if (tempo < 1) {
            tempo = 1;
            warningText.setText(getString(R.string.tempoWarning));
        } else {
            warningText.setText("");
        }

        if (tempo != metronome.getTempo()) {
            metronome.setTempo(tempo);
            metronome.changeActiveTempo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        metronome.release();

        /*
        // Release the WakeLock
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }

         */
    }
}