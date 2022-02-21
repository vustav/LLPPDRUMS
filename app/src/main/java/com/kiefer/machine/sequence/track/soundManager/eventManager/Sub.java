package com.kiefer.machine.sequence.track.soundManager.eventManager;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.track.soundManager.SoundManager;
import com.kiefer.machine.sequence.track.soundManager.eventManager.event.Event;
import com.kiefer.machine.sequence.track.soundManager.eventManager.event.SmplEvent;
import com.kiefer.machine.sequence.track.soundManager.eventManager.event.SnthEvent;
import com.kiefer.utils.NmbrUtils;

import java.util.ArrayList;
import java.util.Random;

public class Sub {
    private LLPPDRUMS llppdrums;
    private DrumSequence drumSequence;
    private DrumTrack drumTrack;
    private Step step;

    private ArrayList<Event> events;

    //rnd
    private Random random;

    private boolean on;
    private float volumeModifier, pitchModifier;

    private float rndOnPerc;
    private boolean rndOnReturn;

    private float rndVolMin, rndVolMax, rndVolPerc;
    private boolean rndVolReturn;

    private float rndPitchMin, rndPitchMax, rndPitchPerc;
    private boolean rndPitchReturn;

    public Sub(LLPPDRUMS llppdrums, DrumSequence drumSequence, DrumTrack drumTrack, SoundManager soundManager, Step step){
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.drumTrack = drumTrack;
        this.step = step;

        random = new Random();

        events = new ArrayList<>();
        events.add(new SnthEvent(step, this, soundManager.getOscillatorManager()));
        events.add(new SmplEvent(step, this, soundManager.getSmplManager()));

        setupAutoRndParams();

        randomizeVol(false);
        randomizePitch(false);
    }

    private void setupAutoRndParams(){
        //autoRndOn = false;
        rndOnPerc = 0;
        rndOnReturn = false;

        //autoRndVol = false;
        rndVolPerc = 0;
        rndVolMin = NmbrUtils.getRndmizer(0, .2f);
        rndVolMax = NmbrUtils.getRndmizer(.7f, 1);
        rndVolReturn = false;

        //autoRndPitch = false;
        rndPitchPerc = 0;
        rndPitchMin = NmbrUtils.getRndmizer(0, .2f);
        rndPitchMax = NmbrUtils.getRndmizer(.7f, 1);
        rndPitchReturn = false;
    }

    /** ONOFF **/
    public boolean isOn(){
        return on;
    }

    public void setOn(boolean on){
        this.on = on;

        if(on) {
            //if (llppdrums.getDrumMachine().getPlayingSequence() == drumSequence && EventsOLD.this.on) {
            if (llppdrums.getDrumMachine().getPlayingSequence() == drumSequence && step.isOn()) {
                //addToSequencer();
                for(Event e : events){
                    e.addToSequencer();
                }
            }
        }
        else{
            for(Event e : events){
                e.removeFromSequencer();
            }
            //removeFromSequencer();
        }
    }

    /** SAMPLES **/
    public void updateSamples(){
        for(Event e : events){
            if(e instanceof SmplEvent){
                ((SmplEvent)e).updateSamples();
            }
        }
    }

    /** POS **/
    public void positionEvents(int posInSamples){
        for(Event e : events){
            e.positionEvent(posInSamples);
        }
    }

    /** RND **/
    public void randomizeOn(boolean autoRnd) {
        if(autoRnd) {
            float r = random.nextFloat();
            if (rndOnPerc >= r) {
                setOn(random.nextInt(2) == 1);
            }
        }
        else{
            setOn(random.nextInt(2) == 1);
        }
    }
    public void randomizeVol(boolean autoRnd){
        if(autoRnd){
            if(rndVolPerc >= random.nextFloat()) {
                setVolumeModifier(NmbrUtils.getRndmizer(rndVolMin, rndVolMax));
            }
        }
        else{
            setVolumeModifier(random.nextFloat());
        }
    }
    public void randomizePitch(boolean autoRnd){
        if(autoRnd){
            if(rndPitchPerc >= random.nextFloat()) {
                setPitchModifier(NmbrUtils.getRndmizer(rndPitchMin, rndPitchMax));
            }
        }
        else{
            setPitchModifier(random.nextFloat());
        }
    }


    /** GET **/
    public float getPitchModifier() {
        return pitchModifier;
    }

    public float getVolumeModifier() {
        return volumeModifier;
    }
/*
    public float getPan() {
        return pan;
    }

 */
    public boolean getAutoRndOn(){
        return rndOnPerc > 0;
    }

    public boolean getRndOnReturn() {
        return rndOnReturn;
    }

    public float getRndOnPerc() {
        return rndOnPerc;
    }

    public boolean getAutoRndVol(){
        return rndVolPerc > 0;
    }


    public float getRndVolMin() {
        return rndVolMin;
    }

    public float getRndVolMax() {
        return rndVolMax;
    }

    public float getRndVolPerc() {
        return rndVolPerc;
    }

    public boolean getRndVolReturn(){
        return rndVolReturn;
    }

    public boolean getAutoRndPitch(){
        return rndPitchPerc > 0;
    }

    public float getRndPitchMin() {
        return rndPitchMin;
    }

    public float getRndPitchMax() {
        return rndPitchMax;
    }

    public float getRndPitchPerc() {
        return rndPitchPerc;
    }

    public boolean getRndPitchReturn(){
        return rndPitchReturn;
    }

    /** SET **/
    //pitch
    public void setVolumeModifier(float modifier){
        volumeModifier = modifier;
        updateEventVolume();
    }

    public void updateEventVolume(){
        for(Event e : events){
            e.setVolume(drumTrack.getTrackVolume() * getVolumeModifier());
        }
        //soundEvents.setPitch(getConvertedPitchModifier());
        //setVolume(drumTrack.getTrackVolume() * getVolumeModifier());
    }


    //pitch
    public void setPitchModifier(float modifier){
        pitchModifier = modifier;
        updateEventPitch();
    }

    public void updateEventPitch(){
        for(Event e : events) {
            e.setPitch(getConvertedPitchModifier());
        }
    }

    private float getConvertedPitchModifier(){
        // pitchModifier in drum is a float 0-1, change to a corresponding value between .5 and 2 (octave down to octave up)
        float minPitchModifier = .5f;
        float maxPitchModifier = 2f;
        return  getPitchModifier() * (maxPitchModifier - minPitchModifier) + minPitchModifier;
    }

    //autorandom
    /*
    public void setAutoRndOn(boolean on){
        autoRndOn = on;
    }

     */

    public void setRndOnPerc(float rndOnPerc) {
        this.rndOnPerc = rndOnPerc;
    }

    public void setRndOnReturn(boolean rndOnReturn) {
        this.rndOnReturn = rndOnReturn;
    }
/*
    public void setAutoRndVol(boolean on){
        autoRndVol = on;
    }

 */

    public void setRndVolMin(float rndVolMin) {
        this.rndVolMin = rndVolMin;
    }

    public void setRndVolMax(float rndVolMax) {
        this.rndVolMax = rndVolMax;
    }

    public void setRndVolPerc(float rndVolPerc) {
        this.rndVolPerc = rndVolPerc;
    }

    public void setRndVolReturn(boolean rndVolReturn) {
        this.rndVolReturn = rndVolReturn;
    }

    public void setRndPitchMin(float rndPitchMin) {
        this.rndPitchMin = rndPitchMin;
    }

    public void setRndPitchMax(float rndPitchMax) {
        this.rndPitchMax = rndPitchMax;
    }

    public void setRndPitchPerc(float rndVolPerc) {
        this.rndPitchPerc = rndVolPerc;
    }

    public void setRndPitchReturn(boolean rndVolReturn) {
        this.rndPitchReturn = rndVolReturn;
    }

    /** DELETE **/
    public void delete(){
        for(Event e : events){
            e.delete();
        }
        events = null;
    }
}
