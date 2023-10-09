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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.metronome.MainActivity;
import com.example.metronome.R;
import com.example.metronome.SettingsActivity;
import com.example.metronome.Tuner.Kalkulatorere.SoundCalculator;
import com.example.metronome.Tuner.LydOpptak.Callback;
import com.example.metronome.Tuner.LydOpptak.SoundRecorder;

public class TunerActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private SoundRecorder recorder;
    private SoundCalculator audioCalculator;
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

        recorder = new SoundRecorder(callback);
        audioCalculator = new SoundCalculator();
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

        AutoCompleteTextView frequencyList = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewFrequency);
        ArrayAdapter<CharSequence> differenceFrequency2 = ArrayAdapter.createFromResource(this,
                R.array.tuner_Frequency, R.layout.frequency_drowdown_item);
        frequencyList.setAdapter(differenceFrequency2);
        frequencyList.setOnItemClickListener(this);

        AutoCompleteTextView pitchList = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewPitch);
        ArrayAdapter<CharSequence> differencePitch = ArrayAdapter.createFromResource(this,
                R.array.tuner_Pitch, R.layout.pitch_dropdown_item);
        pitchList.setAdapter(differencePitch);
        pitchList.setOnItemClickListener(this);


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
                frequency = tuner.getTuningFrequency();
            }
            double cent = tuner.getCent(frequency);

            //final String amp = String.valueOf(amplitude + " Amp");
            //final String db = String.valueOf(decibel + " db");
            //final String hz = String.valueOf(frequency + " Hz");
            final String note = tuner.frequencyToNote(frequency);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int idList = view.getId();
        if(idList == R.id.textViewFrequencyList){
            int n = Integer.parseInt((String) parent.getItemAtPosition(position));
            tuner.setTuningFrequency(n);
        }else if(idList == R.id.textViewPitchList){
            tuner.setPitch(position);
        }
    }
}
