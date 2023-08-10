package com.example.metronome.Tuner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.metronome.MainActivity;
import com.example.metronome.R;
import com.example.metronome.Tuner.Kalkulatorere.LydKalkulator;
import com.example.metronome.Tuner.LydOpptak.Callback;
import com.example.metronome.Tuner.LydOpptak.LydOpptaker;

public class TunerActivity extends AppCompatActivity implements View.OnClickListener {

    private LydOpptaker recorder;
    private LydKalkulator audioCalculator;
    private Handler handler;

    private TextView textAmplitude;
    private TextView textDecibel;
    private TextView textFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        recorder = new LydOpptaker(callback);
        audioCalculator = new LydKalkulator();
        handler = new Handler(Looper.getMainLooper());

        textAmplitude = (TextView) findViewById(R.id.textAmplitude);
        textDecibel = (TextView) findViewById(R.id.textDecibel);
        textFrequency = (TextView) findViewById(R.id.textFrequency);

        Button toMetronomeButton = findViewById(R.id.to_metronome_button);
        toMetronomeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.to_metronome_button) {
            Intent intent = new Intent(TunerActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private Callback callback = new Callback() {

        @Override
        public void onBufferAvailable(byte[] buffer) {
            audioCalculator.setBytes(buffer);
            int amplitude = audioCalculator.getAmplitude();
            double decibel = audioCalculator.getDecibel();
            double frequency = audioCalculator.getFrequency();

            final String amp = String.valueOf(amplitude + " Amp");
            final String db = String.valueOf(decibel + " db");
            final String hz = String.valueOf(frequency + " Hz");

            handler.post(new Runnable() {
                @Override
                public void run() {
                    textAmplitude.setText(amp);
                    textDecibel.setText(db);
                    textFrequency.setText(hz);
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        recorder.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        recorder.stop();
    }

}
