package com.example.metronome.Tuner;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.HashMap;

public class Tuner {

    private int stemmeFrekvens = 440;

    private String[] noteNavn = {"G#*/Ab*", "A*", "A#*/Bb*", "B*", "C*", "C#*/Db*", "D*", "D#*/Eb*", "E*", "F*", "F#*/Gb*", "G*"};

    public Tuner(){

    }

    public void setStemmeFrekvens(int stemmeFrekvens) {
        this.stemmeFrekvens = stemmeFrekvens;
    }

    public int getStemmeFrekvens(){
        return stemmeFrekvens;
    }

    public String frekvensTilNote(double frekvens){
        double noteNummmer = kalkulerToneNummer(frekvens);
        return toneNummerTilTone(noteNummmer);
    }

    public double kalkulerToneNummer(double frekvens){
        return 12 * (Math.log(frekvens / stemmeFrekvens ) / Math.log(2.0)) + 49;
    }

    public double kalkulerFrekvens(double toneNummer){
        return Math.pow(2, (toneNummer-49)/12 )* stemmeFrekvens;
    }

    public String toneNummerTilTone(double noteNummer) {

        int index = (int) Math.floor((noteNummer + 0.5) % 12);
        int toneHøyde = ((int) Math.round(noteNummer) + 8) / 12;

        char toneHøydeChar = (char) ('0' + toneHøyde); // Konverter til char
        String returStreng = noteNavn[index].replace('*', toneHøydeChar);

        return returStreng;
    }

    public int getCent(double frekvens) {
        //https://en.wikipedia.org/wiki/Cent_(music)#:~:text=A%20cent%20is%20a%20unit,semitones%20and%20therefore%201200%20cents.
        double nermesteNoteNummer = Math.round(kalkulerToneNummer(frekvens));
        double nermesteNoteFrekvens = kalkulerFrekvens(nermesteNoteNummer);
        double cent = 1200 * (Math.log(frekvens/nermesteNoteFrekvens)/Math.log(2));
        return (int) Math.round(cent);
    }

}
