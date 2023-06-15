package com.example.metronome;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.Timer;
import java.util.TimerTask;

public class Metronome {

    private Timer timer;
    private TimerTask timerTask;
    private SoundPool soundPool;
    private int dogBarking, drumstick;
    private int tempo = 100;
    private boolean isPlaying = false;

    public Metronome(Context c){

        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes  = new AudioAttributes
                                                    .Builder()
                                                    .setUsage(AudioAttributes
                                                            .USAGE_MEDIA)
                                                    .setContentType(AudioAttributes
                                                            .CONTENT_TYPE_MUSIC)
                                                    .build();
            soundPool = new SoundPool
                        .Builder()
                        .setMaxStreams(3)
                        .setAudioAttributes(audioAttributes)
                        .build();
        }
        else {
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

        dogBarking = soundPool.load(c, R.raw.dogbarking, 1);
        drumstick = soundPool.load(c, R.raw.softsound, 1);
    }

    public void start(){
        if(!isPlaying){
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    soundPool.play(drumstick, 1, 1, 0, 0, 1);
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, calculateInterval());
            isPlaying = true;
        }
    }

    public void stop() {
        if (isPlaying) {
            timerTask.cancel();
            timer.purge();
            timer = null;
            timerTask = null;
            isPlaying = false;
        }
    }

    public void changeActiveTempo() {
        if (isPlaying) {
            stop();
            start();
        }
    }
    private long calculateInterval() {
        return 60000L / tempo;
    }

    public void addToTempo(int i){
        tempo += i;
        if(tempo > 300){
            tempo = 300;
        }
        changeActiveTempo();
    }
    public void removeFromTempo(int i){
        tempo -= i;
        if(tempo < 1){
            tempo = 1;
        }
        changeActiveTempo();
    }

    public void setTempo(int tempo){
        this.tempo = tempo;
    }
    public int getTempo() {
        return tempo;
    }

    public boolean getIsPlaying(){
        return isPlaying;
    }

    public void setIsPlaying(boolean playTempo){
        this.isPlaying = playTempo;
    }

    public void release(){
        soundPool.release();
    }
}
