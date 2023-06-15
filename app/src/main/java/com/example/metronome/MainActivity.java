package com.example.metronome;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText tempoText;
    private TextView warningText;
    private Metronome metronome;
    Button playTempoButton;

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
                playTempoButton.setText("Stop");
                metronome.start();
            }else{
                playTempoButton.setText("Start");
                metronome.stop();
            }
        }

        int tempo = metronome.getTempo();
        tempoText.setText(String.valueOf(tempo));

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

        metronome.setTempo(tempo);
        metronome.changeActiveTempo();
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        metronome.release();
    }
}
