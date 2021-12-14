package com.kiefer.machine.sequence.track;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.StepKeeper;
import com.kiefer.machine.sequence.track.soundManager.SoundManager;
import com.kiefer.machine.sequence.track.soundManager.events.SoundEvents;

import java.util.Random;

public class Step {
    private LLPPDRUMS llppdrums;
    private final DrumTrack drumTrack;
    private final SoundManager soundManager;

    private final Random random;

    private SoundEvents soundEvents;

    //base prms
    private boolean on;

    public Step(LLPPDRUMS llppdrums, DrumTrack drumTrack, SoundManager soundManager, int nOfSteps, int subs, boolean on){
        this.llppdrums = llppdrums;
        this.drumTrack = drumTrack;
        this.soundManager = soundManager;
        random = new Random();

        this.on = on;
        createEvents(nOfSteps, subs);
    }

    private void createEvents(int nOfSteps, int subs){

        int step = getStepNo();
        if (step == -1) {
            step = drumTrack.getSteps().size();
        }

        soundEvents = soundManager.getSoundEvents(nOfSteps, subs, step, on);
    }

    public void positionEvents(){
        soundEvents.positionEvents(drumTrack.getNOfSteps(), getStepNo());
    }

    /** RANDOMIZE **/
    //vars = should the autoRnd variables be used or not??
    public void randomizeOn(boolean autoRnd, int sub) {
        soundEvents.randomizeOn(autoRnd, sub);
    }
    public void randomizeSubsOn(boolean autoRnd) {
        soundEvents.randomizeSubsOn(autoRnd);
    }

    public void randomizeVol(boolean autoRnd, int sub){
        soundEvents.randomizeVol(autoRnd, sub);
    }

    public void randomizeVols(boolean autoRnd){
        soundEvents.randomizeVols(autoRnd);
    }

    public void randomizePitch(boolean autoRnd, int sub){
        soundEvents.randomizePitch(autoRnd, sub);
    }

    public void randomizePitches(boolean autoRnd){
        soundEvents.randomizePitches(autoRnd);
    }
    public void randomizePan(boolean autoRnd){
        soundEvents.randomizePan(autoRnd);
    }

    /** GET **/
    public boolean isOn(){
        return on;
    }

    public float getVolumeModifier(int sub) {
        return soundEvents.getVolumeModifier(sub);
    }

    public float getPitchModifier(int sub) {
        return soundEvents.getPitchModifier(sub);
    }

    public float getPan() {
        return soundEvents.getPan();
    }

    public int getStepNo() {
        return drumTrack.getSteps().indexOf(this);
    }

    public int getTrackNo(){
        return drumTrack.getTrackNo();
    }

    //auto-rnd
    public boolean getAutoRndOn(int sub){
        return soundEvents.getAutoRndOn(sub);
    }

    public boolean getRndOnReturn(int sub) {
        return soundEvents.getRndOnReturn(sub);
    }

    public float getRndOnPerc(int sub) {
        return soundEvents.getRndOnPerc(sub);
    }

    public boolean getAutoRndVol(int sub){
        return soundEvents.getAutoRndVol(sub);
    }


    public float getRndVolMin(int sub) {
        return soundEvents.getRndVolMin(sub);
    }

    public float getRndVolMax(int sub) {
        return soundEvents.getRndVolMax(sub);
    }

    public float getRndVolPerc(int sub) {
        return soundEvents.getRndVolPerc(sub);
    }

    public boolean getRndVolReturn(int sub){
        return soundEvents.getRndVolReturn(sub);
    }

    public boolean getAutoRndPan(){
        return soundEvents.getAutoRndPan();
    }

    public float getRndPanMin() {
        return soundEvents.getRndPanMin();
    }

    public float getRndPanMax() {
        return soundEvents.getRndPanMax();
    }

    public float getRndPanPerc() {
        return soundEvents.getRndPanPerc();
    }

    public boolean getRndPanReturn(){
        return soundEvents.getRndPanReturn();
    }

    public boolean getAutoRndPitch(int sub){
        return soundEvents.getAutoRndPitch(sub);
    }

    public float getRndPitchMin(int sub) {
        return soundEvents.getRndPitchMin(sub);
    }

    public float getRndPitchMax(int sub) {
        return soundEvents.getRndPitchMax(sub);
    }

    public float getRndPitchPerc(int sub) {
        return soundEvents.getRndPitchPerc(sub);
    }

    public boolean getRndPitchReturn(int sub){
        return soundEvents.getRndPitchReturn(sub);
    }

    public int getNofSubs(){
        return soundEvents.getNOfSubs();
    }

    public boolean isSubOn(int sub){
        return soundEvents.isSubOn(sub);
    }

    public float getSubVol(int sub){
        return soundEvents.getVolumeModifier(sub);
    }

    public float getSubPitch(int sub){
        return soundEvents.getPitchModifier(sub);
    }

    /** SET **/
    public void setNOfSubs(int steps, int subs){
        soundEvents.setNOfSubs(steps, subs, on);
        positionEvents();
    }

    public void setOn(boolean on){
        boolean wasOn = this.on;
        this.on = on;

        if (on && !wasOn) {
            //addToSequencer(drumTrack.getNOfSteps());
            soundEvents.turnOn(drumTrack.getNOfSteps(), getStepNo());
        }
        else if (!on && wasOn) {
            soundEvents.turnOff();
        }
    }

    public void setSubOn(int sub, boolean on){
        soundEvents.setSubOn(sub, on);
    }

    public void updateEventSound(){
        //if(on) {
            soundEvents.updateSound();
        //}
    }

    public void recreateEvent(){
        soundEvents.deleteEvents();
        createEvents(drumTrack.getNOfSteps(), drumTrack.getNOfSubs());
    }

    //vol
    public void setVolumeModifier(float modifier, int sub){
        soundEvents.setVolumeModifier(modifier, sub);
        //volumeModifier = modifier;
        //updateEventVolume();
    }

    public void updateEventVolumes(){
        soundEvents.updateEventVolume();
        //soundEvents.setVolume(drumTrack.getTrackVolume() * getVolumeModifier());
    }

    //pitch
    public void setPitchModifier(float modifier, int sub){
        soundEvents.setPitchModifier(modifier, sub);
        //pitchModifier = modifier;
        //updateEventPitches();
    }

    public void updateEventPitches(){
        //soundEvents.setPitch(getConvertedPitchModifier());
        soundEvents.updateEventPitches();
    }
/*
    private float getConvertedPitchModifier(){
        // pitchModifier in drum is a float 0-1, change to a corresponding value between .5 and 2 (octave down to octave up)
        float minPitchModifier = .5f;
        float maxPitchModifier = 2f;
        return  getPitchModifier() * (maxPitchModifier - minPitchModifier) + minPitchModifier;
    }

 */
    public void setPan(float pan){
        //Log.e("Step", "set pan: "+pan);
        soundEvents.setPan(pan);
    }

    //autorandom
    /*
    public void setAutoRndOn(boolean on){
        autoRndOn = on;
    }

     */

    public void setRndOnPerc(float rndOnPerc, int sub) {
        soundEvents.setRndOnPerc(rndOnPerc, sub);
        //this.rndOnPerc = rndOnPerc;
    }

    public void setRndOnReturn(boolean rndOnReturn, int sub) {
        soundEvents.setRndOnReturn(rndOnReturn, sub);
        //this.rndOnReturn = rndOnReturn;
    }
/*
    public void setAutoRndVol(boolean on){
        autoRndVol = on;
    }

 */

    public void setRndVolMin(float rndVolMin, int sub) {
        soundEvents.setRndVolMin(rndVolMin, sub);
        //this.rndVolMin = rndVolMin;
    }

    public void setRndVolMax(float rndVolMax, int sub) {
        soundEvents.setRndVolMax(rndVolMax, sub);
        //this.rndVolMax = rndVolMax;
    }

    public void setRndVolPerc(float rndVolPerc, int sub) {
        soundEvents.setRndVolPerc(rndVolPerc, sub);
        //this.rndVolPerc = rndVolPerc;
    }

    public void setRndVolReturn(boolean rndVolReturn, int sub) {
        soundEvents.setRndVolReturn(rndVolReturn, sub);
        //this.rndVolReturn = rndVolReturn;
    }
/*
    public void setAutoRndPitch(boolean on){
        autoRndPitch = on;
    }

 */

    public void setRndPitchMin(float rndPitchMin, int sub) {
        soundEvents.setRndPitchMin(rndPitchMin, sub);
        //this.rndPitchMin = rndPitchMin;
    }

    public void setRndPitchMax(float rndPitchMax, int sub) {
        soundEvents.setRndPitchMax(rndPitchMax, sub);
        //this.rndPitchMax = rndPitchMax;
    }

    public void setRndPitchPerc(float rndVolPerc, int sub) {
        soundEvents.setRndPitchPerc(rndVolPerc, sub);
        //this.rndPitchPerc = rndVolPerc;
    }

    public void setRndPitchReturn(boolean rndVolReturn, int sub) {
        soundEvents.setRndPitchReturn(rndVolReturn, sub);
        //this.rndPitchReturn = rndVolReturn;
    }
/*
    public void setAutoRndPan(boolean on){
        autoRndPan = on;
    }

 */

    public void setRndPanMin(float rndPitchMin) {
        soundEvents.setRndPanMin(rndPitchMin);
        //this.rndPanMin = rndPitchMin;
    }

    public void setRndPanMax(float rndVolMax) {
        soundEvents.setRndPanMax(rndVolMax);
        //this.rndPanMax = rndVolMax;
    }

    public void setRndPanPerc(float rndVolPerc) {
        soundEvents.setRndPanPerc(rndVolPerc);
        //this.rndPanPerc = rndVolPerc;
    }

    public void setRndPanReturn(boolean rndVolReturn) {
        soundEvents.setRndPanReturn(rndVolReturn);
        //this.rndPanReturn = rndVolReturn;
    }

    /** SEQUENCER **/
    public void handleSequencerPositionChange(int sequencerPosition){
        soundManager.setPan(getPan());
    }

    /** STATS **/
    /*
    public int getNOfEvents(){
        int n = 0;
        if(soundManager.getActiveSound() == SoundManager.OSC) {
            for (SynthEvent se : synthEvents) {
                if (se != null) {
                    n++;
                }
            }
        }
        else{
            n = 1;
        }
        return n;
    }

    public int getNOfSequencedEvents(){
        int n = 0;
        if(soundManager.getActiveSound() == SoundManager.OSC) {
            for (SynthEvent se : synthEvents) {
                if (se != null) {
                    if (se.getIsSequenced()) {
                        n++;
                    }
                }
            }
        }
        else{
            if (sampleEvent != null) {
                if (sampleEvent.getIsSequenced()) {
                    n++;
                }
            }
        }
        return n;
    }

     */

    /** RESET **/
    public void reset(){
        setOn(false);
        soundEvents.reset();
    }

    /** RESTORATION **/
    public void restore(StepKeeper k){
        setOn(k.on);
        soundEvents.restore(k.soundEventsKeeper);
    }

    public StepKeeper getKeeper(){
        StepKeeper keeper = new StepKeeper();

        keeper.on = isOn();
        keeper.soundEventsKeeper = soundEvents.getKeeper();
        return keeper;
    }

    /** DESTRUCTION **/
    public void destroy(){
        soundEvents.destroy();
    }
}

