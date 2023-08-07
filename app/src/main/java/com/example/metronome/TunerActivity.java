package com.example.metronome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TunerActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

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
}
