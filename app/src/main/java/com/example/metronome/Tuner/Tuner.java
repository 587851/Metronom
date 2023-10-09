package com.example.metronome.Tuner;

public class Tuner {

    private int tuningFrequency = 440;
    private int pitch = 0;

    private String[] noteNames = {"G#*/Ab*", "A*", "A#*/Bb*", "B*", "C*", "C#*/Db*", "D*", "D#*/Eb*", "E*", "F*", "F#*/Gb*", "G*"};

    public Tuner(){

    }

    public void setTuningFrequency(int tuningFrequency) {
        this.tuningFrequency = tuningFrequency;
    }

    public int getTuningFrequency(){
        return tuningFrequency;
    }

    public void setPitch(int pitch){ this.pitch = pitch; }
    public int getPitch(){
        return pitch;
    }

    public String frequencyToNote(double frequency){
        double noteNumber = calculateToneNumber(frequency);
        return toneNumberToTone(noteNumber);
    }

    public double calculateToneNumber(double frequency){
        return 12 * (Math.log(frequency / tuningFrequency ) / Math.log(2.0)) + 49;
    }

    public double calculateFrequency(double toneNumber){
        return Math.pow(2, (toneNumber-49)/12 )* tuningFrequency;
    }

    public String toneNumberToTone(double noteNumber) {

        int index = (int) Math.floor((noteNumber + 0.5 - pitch) % 12);
        int octave = ((int) Math.round(noteNumber) + 8) / 12;

        char octaveChar = (char) ('0' + octave); // Convert to char
        String returnString = noteNames[index].replace('*', octaveChar);

        return returnString;
    }

    public int getCent(double frequency) {
        //https://en.wikipedia.org/wiki/Cent_(music)#:~:text=A%20cent%20is%20a%20unit,semitones%20and%20therefore%201200%20cents.
        double nearestNoteNumber = Math.round(calculateToneNumber(frequency));
        double nearestNoteFrequency = calculateFrequency(nearestNoteNumber);
        double cent = 1200 * (Math.log(frequency/nearestNoteFrequency)/Math.log(2));
        return (int) Math.round(cent);
    }
}
