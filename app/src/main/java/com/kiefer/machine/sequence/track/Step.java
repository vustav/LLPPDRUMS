package com.kiefer.machine.sequence.track;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.StepKeeper;
import com.kiefer.machine.sequence.track.soundManager.SoundManager;
import com.kiefer.machine.sequence.track.soundManager.eventManager.StepEventsManager;

import java.util.Random;

public class Step {
    private LLPPDRUMS llppdrums;
    private final DrumTrack drumTrack;

    private final SoundManager soundManager;
    //private Events eventManager;
    private StepEventsManager stepEventsManager;

    private final Random random;


    //base prms
    private boolean on;

    public Step(LLPPDRUMS llppdrums, DrumTrack drumTrack, SoundManager soundManager, int nOfSteps, int subs, boolean on){
        this.llppdrums = llppdrums;
        this.drumTrack = drumTrack;
        this.soundManager = soundManager;
        random = new Random();

        this.on = on;
        createEvents(subs);
    }


    private void createEvents(int subs){

        int step = getStepNo();
        if (step == -1) {
            step = drumTrack.getSteps().size();
        }

        //events = soundManager.getSoundEvents(nOfSteps, subs, step, on);
        stepEventsManager = soundManager.getStepEventManager(this, subs);
    }



    public void positionEvents(){
        //events.positionEvents(drumTrack.getNOfSteps(), getStepNo());
        stepEventsManager.positionEvents(drumTrack.getNOfSteps());
    }

    /** RANDOMIZE **/
    //vars = should the autoRnd variables be used or not??
    public void randomizeOn(boolean autoRnd, int sub) {
        stepEventsManager.randomizeOn(autoRnd, sub);
    }
    public void randomizeSubsOn(boolean autoRnd) {
        stepEventsManager.randomizeSubsOn(autoRnd);
    }

    public void randomizeVol(boolean autoRnd, int sub){
        stepEventsManager.randomizeVol(autoRnd, sub);
    }

    public void randomizeVols(boolean autoRnd){
        stepEventsManager.randomizeVols(autoRnd);
    }

    public void randomizePitch(boolean autoRnd, int sub){
        //events.randomizePitch(autoRnd, sub);
        stepEventsManager.randomizePitch(autoRnd, sub);
    }

    public void randomizePitches(boolean autoRnd){
        stepEventsManager.randomizePitches(autoRnd);
    }
    public void randomizePan(boolean autoRnd){
        stepEventsManager.randomizePan(autoRnd);
    }

    /** GET **/
    public boolean isOn(){
        return on;
    }

    public float getVolumeModifier(int sub) {
        return stepEventsManager.getVolumeModifier(sub);
    }

    public float getPitchModifier(int sub) {
        return stepEventsManager.getPitchModifier(sub);
    }

    public float getPan() {
        return stepEventsManager.getPan();
    }

    public int getStepNo() {
        return drumTrack.getSteps().indexOf(this);
    }

    public int getTrackNo(){
        return drumTrack.getTrackNo();
    }

    //auto-rnd
    public boolean getAutoRndOn(int sub){
        return stepEventsManager.getAutoRndOn(sub);
    }

    public boolean getRndOnReturn(int sub) {
        return stepEventsManager.getRndOnReturn(sub);
    }

    public float getRndOnPerc(int sub) {
        return stepEventsManager.getRndOnPerc(sub);
    }

    public boolean getAutoRndVol(int sub){
        return stepEventsManager.getAutoRndVol(sub);
    }


    public float getRndVolMin(int sub) {
        return stepEventsManager.getRndVolMin(sub);
    }

    public float getRndVolMax(int sub) {
        return stepEventsManager.getRndVolMax(sub);
    }

    public float getRndVolPerc(int sub) {
        return stepEventsManager.getRndVolPerc(sub);
    }

    public boolean getRndVolReturn(int sub){
        return stepEventsManager.getRndVolReturn(sub);
    }

    public boolean getAutoRndPan(){
        return stepEventsManager.getAutoRndPan();
    }

    public float getRndPanMin() {
        return stepEventsManager.getRndPanMin();
    }

    public float getRndPanMax() {
        return stepEventsManager.getRndPanMax();
    }

    public float getRndPanPerc() {
        return stepEventsManager.getRndPanPerc();
    }

    public boolean getRndPanReturn(){
        return stepEventsManager.getRndPanReturn();
    }

    public boolean getAutoRndPitch(int sub){
        return stepEventsManager.getAutoRndPitch(sub);
    }

    public float getRndPitchMin(int sub) {
        return stepEventsManager.getRndPitchMin(sub);
    }

    public float getRndPitchMax(int sub) {
        return stepEventsManager.getRndPitchMax(sub);
    }

    public float getRndPitchPerc(int sub) {
        return stepEventsManager.getRndPitchPerc(sub);
    }

    public boolean getRndPitchReturn(int sub){
        return stepEventsManager.getRndPitchReturn(sub);
    }

    public int getNofSubs(){
        return stepEventsManager.getNOfSubs();
    }

    public boolean isSubOn(int sub){
        return stepEventsManager.isSubOn(sub);
    }

    public float getSubVol(int sub){
        return stepEventsManager.getVolumeModifier(sub);
    }

    public float getSubPitch(int sub){
        return stepEventsManager.getPitchModifier(sub);
    }

    /** SET **/
    public void setNOfSubs(int steps, int subs){
        stepEventsManager.setNOfSubs(steps, subs, on);
        positionEvents();
    }

    public void setOn(boolean on){
        boolean wasOn = this.on;
        this.on = on;

        if (on && !wasOn) {
            //addToSequencer(drumTrack.getNOfSteps());
            stepEventsManager.turnOn(drumTrack.getNOfSteps(), getStepNo());
        }
        else if (!on && wasOn) {
            stepEventsManager.turnOff();
        }
    }

    public void setSubOn(int sub, boolean on){
        stepEventsManager.setSubOn(sub, on);
    }

    public void updateEventSamples(){
        //if(on) {
        stepEventsManager.updateSamples();
        //}
    }

    /*
    public void recreateEvent(){
        events.deleteEvents();
        createEvents(drumTrack.getNOfSteps(), drumTrack.getNOfSubs());
    }

     */

    //vol
    public void setVolumeModifier(float modifier, int sub){
        stepEventsManager.setVolumeModifier(modifier, sub);
        //volumeModifier = modifier;
        //updateEventVolume();
    }

    public void updateEventVolumes(){
        stepEventsManager.updateEventVolume();
        //soundEvents.setVolume(drumTrack.getTrackVolume() * getVolumeModifier());
    }

    //pitch
    public void setPitchModifier(float modifier, int sub){
        stepEventsManager.setPitchModifier(modifier, sub);
        //pitchModifier = modifier;
        //updateEventPitches();
    }

    public void updateEventPitches(){
        //soundEvents.setPitch(getConvertedPitchModifier());
        stepEventsManager.updateEventPitches();
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
        stepEventsManager.setPan(pan);
    }

    //autorandom
    /*
    public void setAutoRndOn(boolean on){
        autoRndOn = on;
    }

     */

    public void setRndOnPerc(float rndOnPerc, int sub) {
        stepEventsManager.setRndOnPerc(rndOnPerc, sub);
        //this.rndOnPerc = rndOnPerc;
    }

    public void setRndOnReturn(boolean rndOnReturn, int sub) {
        stepEventsManager.setRndOnReturn(rndOnReturn, sub);
        //this.rndOnReturn = rndOnReturn;
    }
/*
    public void setAutoRndVol(boolean on){
        autoRndVol = on;
    }

 */

    public void setRndVolMin(float rndVolMin, int sub) {
        stepEventsManager.setRndVolMin(rndVolMin, sub);
        //this.rndVolMin = rndVolMin;
    }

    public void setRndVolMax(float rndVolMax, int sub) {
        stepEventsManager.setRndVolMax(rndVolMax, sub);
        //this.rndVolMax = rndVolMax;
    }

    public void setRndVolPerc(float rndVolPerc, int sub) {
        stepEventsManager.setRndVolPerc(rndVolPerc, sub);
        //this.rndVolPerc = rndVolPerc;
    }

    public void setRndVolReturn(boolean rndVolReturn, int sub) {
        stepEventsManager.setRndVolReturn(rndVolReturn, sub);
        //this.rndVolReturn = rndVolReturn;
    }
/*
    public void setAutoRndPitch(boolean on){
        autoRndPitch = on;
    }

 */

    public void setRndPitchMin(float rndPitchMin, int sub) {
        stepEventsManager.setRndPitchMin(rndPitchMin, sub);
        //this.rndPitchMin = rndPitchMin;
    }

    public void setRndPitchMax(float rndPitchMax, int sub) {
        stepEventsManager.setRndPitchMax(rndPitchMax, sub);
        //this.rndPitchMax = rndPitchMax;
    }

    public void setRndPitchPerc(float rndVolPerc, int sub) {
        stepEventsManager.setRndPitchPerc(rndVolPerc, sub);
        //this.rndPitchPerc = rndVolPerc;
    }

    public void setRndPitchReturn(boolean rndVolReturn, int sub) {
        stepEventsManager.setRndPitchReturn(rndVolReturn, sub);
        //this.rndPitchReturn = rndVolReturn;
    }
/*
    public void setAutoRndPan(boolean on){
        autoRndPan = on;
    }

 */

    public void setRndPanMin(float rndPitchMin) {
        stepEventsManager.setRndPanMin(rndPitchMin);
        //this.rndPanMin = rndPitchMin;
    }

    public void setRndPanMax(float rndVolMax) {
        stepEventsManager.setRndPanMax(rndVolMax);
        //this.rndPanMax = rndVolMax;
    }

    public void setRndPanPerc(float rndVolPerc) {
        stepEventsManager.setRndPanPerc(rndVolPerc);
        //this.rndPanPerc = rndVolPerc;
    }

    public void setRndPanReturn(boolean rndVolReturn) {
        stepEventsManager.setRndPanReturn(rndVolReturn);
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
        stepEventsManager.reset();
    }

    /** RESTORATION **/
    public void restore(StepKeeper k){
        setOn(k.on);
        stepEventsManager.restore(k.soundEventsKeeper);
    }

    public StepKeeper getKeeper(){
        StepKeeper keeper = new StepKeeper();

        keeper.on = isOn();
        keeper.soundEventsKeeper = stepEventsManager.getKeeper();
        return keeper;
    }

    /** DESTRUCTION **/
    public void destroy(){
        stepEventsManager.destroy();
    }
}

