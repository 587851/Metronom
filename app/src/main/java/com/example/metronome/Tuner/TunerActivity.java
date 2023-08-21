package com.example.metronome.Tuner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.metronome.MainActivity;
import com.example.metronome.R;
import com.example.metronome.SettingsActivity;
import com.example.metronome.Tuner.Kalkulatorere.LydKalkulator;
import com.example.metronome.Tuner.LydOpptak.Callback;
import com.example.metronome.Tuner.LydOpptak.LydOpptaker;

public class TunerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private LydOpptaker recorder;
    private LydKalkulator audioCalculator;
    private Handler handler;
    private Tuner tuner;

    private TextView textAmplitude;
    private TextView textDecibel;

    private TextView textFrequency;
    private TextView textNote;
    private TextView textCent;
    private ImageView redArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        recorder = new LydOpptaker(callback);
        audioCalculator = new LydKalkulator();
        handler = new Handler(Looper.getMainLooper());
        tuner = new Tuner();


        textAmplitude = (TextView) findViewById(R.id.textAmplitude);
        textDecibel = (TextView) findViewById(R.id.textDecibel);
        textFrequency = (TextView) findViewById(R.id.textFrequency);
        textNote = (TextView) findViewById(R.id.textNote);
        textCent = (TextView) findViewById(R.id.textCent);
        redArrow = (ImageView) findViewById(R.id.redArrow);

        Button toMetronomeButton = findViewById(R.id.to_metronome_button);
        toMetronomeButton.setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.spinnerFrequency);
        ArrayAdapter<CharSequence> differenceFrequencies = ArrayAdapter.createFromResource(this,
                R.array.tuner_Frequency, android.R.layout.simple_spinner_item);
        differenceFrequencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(differenceFrequencies);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(10);

        Spinner pitchSpinner = (Spinner) findViewById(R.id.spinnerPitch);
        ArrayAdapter<CharSequence> differencePitches = ArrayAdapter.createFromResource(this,
                R.array.tuner_Pitch, android.R.layout.simple_spinner_item);
        differencePitches.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pitchSpinner.setAdapter(differencePitches);
        pitchSpinner.setOnItemSelectedListener(this);
        pitchSpinner.setSelection(0);
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
            if(decibel < -10){
                frequency = tuner.getStemmeFrekvens();
            }
            double cent = tuner.getCent(frequency);

            //final String amp = String.valueOf(amplitude + " Amp");
            //final String db = String.valueOf(decibel + " db");
            //final String hz = String.valueOf(frequency + " Hz");
            final String note = tuner.frekvensTilNote(frequency);
            final String ce = cent + " Ȼ";
            final float arrowDegree = (float) ((57/50) * cent);


            handler.post(new Runnable() {
                @Override
                public void run() {
                    //textAmplitude.setText(amp);
                    //textDecibel.setText(db);
                    //textFrequency.setText(hz);
                    textNote.setText(note);
                    textCent.setText(ce);
                    redArrow.setRotation(arrowDegree);

                    if(cent < 10 && cent > -10){
                        textNote.setTextColor(Color.GREEN);
                    }else{
                        textNote.setTextColor(Color.RED);
                    }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int idSpinner = parent.getId();

        if(idSpinner == R.id.spinnerFrequency){
            int n = Integer.parseInt((String) parent.getItemAtPosition(position));
            tuner.setStemmeFrekvens(n);
        }else if(idSpinner == R.id.spinnerPitch){
            tuner.setPitch(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            Intent intent = new Intent(TunerActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
