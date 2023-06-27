package com.example.metronome;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class SoundChoiceActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemClickListener itemClickListener;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_choice);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        String[] metronomeSoundStrings = getResources().getStringArray(R.array.metronome_sounds);

        itemClickListener = new ItemClickListener() {
            @Override
            public void onClick(String s, int position) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
                Toast.makeText(getApplicationContext(), "Selected : " + s, Toast.LENGTH_SHORT).show();

                MainActivity.metronome.changeActiveSound(position);

            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(metronomeSoundStrings, itemClickListener, MainActivity.metronome.getActiveSoundPosition());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}