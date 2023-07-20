package com.example.metronome;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText tempoText;
    private TextView warningText;
    static public Metronome metronome;
    Button playTempoButton;
    private long lastChangeTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                                            .setUsage(AudioAttributes.USAGE_MEDIA)
                                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                            .build();
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        metronome = new Metronome(this);

        tempoText = (EditText) findViewById(R.id.tempoText);
        tempoText.addTextChangedListener(this);
        warningText = (TextView) findViewById(R.id.textView);

        Slider tempoSlider = findViewById(R.id.slider);
        tempoSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                metronome.setTempo((int)value);
                metronome.changeActiveTempo();
                tempoText.setText(String.valueOf((int)value));
            }
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(R.id.settings == id){
            stopMetronome();

            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else{
            return(super.onOptionsItemSelected(item));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.buttonAddFive){
            metronome.addToTempo(5);
        } else if (id == R.id.buttonRemoveFive) {
            metronome.removeFromTempo(5);
        } else if(id == R.id.buttonAddOne){
            metronome.addToTempo(1);
        } else if(id == R.id.buttonRemoveOne){
            metronome.removeFromTempo(1);
        } else if(id == R.id.buttonPlayTempo){
            if(!metronome.getIsPlaying()){
                startMetronome();
            }else{
                stopMetronome();
            }
        }
        int tempo = metronome.getTempo();
        tempoText.setText(String.valueOf(tempo));
    }

    public void startMetronome(){
        playTempoButton.setText("Stop");
        metronome.start();
    }
    public void stopMetronome(){
        playTempoButton.setText("Start");
        metronome.stop();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //
    }

    @Override
    public void afterTextChanged(Editable s) {
        int tempo;
        try{
            tempo = Integer.parseInt(tempoText.getText().toString());
            if(tempo > 300){
                tempo = 300;
                warningText.setText(getString(R.string.tempoWarning));
            }else if(tempo < 1){
                tempo = 1;
                warningText.setText(getString(R.string.tempoWarning));
            }else{
                warningText.setText("");
            }
        }catch (NumberFormatException e) {
            tempo = 1;
            warningText.setText(getString(R.string.tempoWarning));
        }

        if(tempo != metronome.getTempo()){
            metronome.setTempo(tempo);
            metronome.changeActiveTempo();
        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        metronome.release();
    }
}
