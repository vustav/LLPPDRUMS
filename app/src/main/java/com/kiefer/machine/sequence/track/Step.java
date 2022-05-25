package com.kiefer.machine.sequence.track;

import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.StepKeeper;
import com.kiefer.machine.sequence.track.soundManager.SoundManager;
import com.kiefer.machine.sequence.track.soundManager.eventManager.StepEventsManager;

import java.util.Random;

/** Each Step creates a StepEventManager that holds events (both Sample and Synth with one of them being muted) for all the Subs.
 * The events are added removed from the sequencer when OnOff is clicked, but never deleted unless the track is deleted. All
 * tracks that aren't supposed to be playing are muted. Their events are still in the sequencer, but since their instruments
 * are muted they won't play and doesn't impact performance in any way (other than memory)**/

public class Step {
    private LLPPDRUMS llppdrums;
    private final DrumTrack drumTrack;

    private final SoundManager soundManager;
    //private Events eventManager;
    private StepEventsManager stepEventsManager;

    private final Random random;


    //base prms
    private boolean on;

    public Step(LLPPDRUMS llppdrums, DrumTrack drumTrack, SoundManager soundManager, int subs, boolean on){
        this.llppdrums = llppdrums;
        this.drumTrack = drumTrack;
        this.soundManager = soundManager;
        random = new Random();

        this.on = on;
        createEvents(subs);
    }


    private void createEvents(int subs){
        stepEventsManager = soundManager.getStepEventManager(this, subs);
    }



    public void positionEvents(int nOfSteps, boolean onlySynth){
        stepEventsManager.positionEvents(nOfSteps, onlySynth);
    }

    /** RANDOMIZE **/
    public void randomizeStepOn(){
        Random r = new Random();
        setOn(r.nextBoolean());

        //if there's only one sub we need to turn it on, otherwise it will be temporarily off since you can't open the subs interface with one sub
        if(stepEventsManager.getNOfSubs() == 1){
            stepEventsManager.setSubOn(0, on);
        }
    }
    //vars = should the autoRnd variables be used or not??
    public void randomizeSubOn(int sub) {
        if(stepEventsManager.getNOfSubs() > 1) {
            stepEventsManager.randomizeOn(sub);
        }
        //if there's only one sub we need to turn it on, otherwise it will be temporarily off since you can't open the subs interface with one sub
        else{
            stepEventsManager.setSubOn(0, on);
        }
    }
    public void randomizeSubsOn() {
        if(stepEventsManager.getNOfSubs() > 1) {
            stepEventsManager.randomizeSubsOn();
        }
        //if there's only one sub we need to turn it on, otherwise it will be temporarily off since you can't open the subs interface with one sub
        else{
            stepEventsManager.setSubOn(0, true);
        }
    }

    public void randomizeVol(int sub){
        stepEventsManager.randomizeVol(sub);
    }

    public void randomizeVols(){
        stepEventsManager.randomizeVols();
    }

    public void randomizePitch(int sub){
        stepEventsManager.randomizePitch(sub);
    }

    public void randomizePitches(){
        stepEventsManager.randomizePitches();
    }
    public void randomizePan(){
        stepEventsManager.randomizePan();
    }

    /** RETURN MEMORY **/
    //keep track if automations are going to be returned, otherwise don't do unnecessary loops

    int returns = 0;
    public void returnModified(boolean returnValue){
        drumTrack.returnModified(returnValue);
        if(returnValue){
            returns++;
        }
        else{
            returns--;
        }
    }

    public boolean returnActive() {
        return returns > 0;
    }

    /** AUTOMATION MEMORY **/
    //Step keeps track of individual automations for indication reasons. automationActive still works as expected.

    int onAutos = 0;
    public void onAutosModified(boolean autoValue){
        drumTrack.automationsModified(autoValue);
        if(autoValue){
            onAutos++;
        }
        else{
            onAutos--;
        }
    }

    public boolean onAutomationActive() {
        return onAutos > 0;
    }

    int volAutos = 0;
    public void volAutosModified(boolean autoValue){
        drumTrack.automationsModified(autoValue);
        if(autoValue){
            volAutos++;
        }
        else{
            volAutos--;
        }
    }

    public boolean volAutomationActive() {
        return volAutos > 0;
    }

    int pitchAutos = 0;
    public void pitchAutosModified(boolean autoValue){
        drumTrack.automationsModified(autoValue);
        if(autoValue){
            pitchAutos++;
        }
        else{
            pitchAutos--;
        }
    }

    public boolean pitchAutomationActive() {
        return pitchAutos > 0;
    }

    int panAutos = 0;
    public void panAutosModified(boolean autoValue){
        drumTrack.automationsModified(autoValue);
        if(autoValue){
            panAutos++;
        }
        else{
            panAutos--;
        }
    }

    public boolean panAutomationActive() {
        return panAutos > 0;
    }

    public boolean automationActive() {
        //return panAutomationActive() || volAutomationActive() || pitchAutomationActive() || panAutomationActive();
        return onAutos + volAutos + pitchAutos + panAutos > 0;
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
        //if this step isn't in the array yet this is called on creation (before it's been added) which means this is the last step
        if(!drumTrack.getSteps().contains(this)){
            //Log.e("Step", "getStepNo(), stepNo: "+ drumTrack.getSteps().size());
            return drumTrack.getSteps().size();
        }
        //Log.e("Step", "getStepNo(), stepNo: "+ drumTrack.getSteps().indexOf(this));
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

    /** PREV **/

    private boolean prevOn; //used return is on in AutoRandomization
    public void savePrevOn(){
        prevOn = on;
    }

    public void saveSubPrevOn(int sub){
        stepEventsManager.savePrevOn(sub);
    }

    public void saveSubPrevVol(int sub){
        stepEventsManager.savePrevVol(sub);
    }

    public void saveSubPrevPitch(int sub){
        stepEventsManager.savePrevPitch(sub);
    }
    public void saveSubPrevPan(){
        stepEventsManager.savePrevPan();
    }

    public boolean getSubPrevOn(int sub){
        return stepEventsManager.getPrevOn(sub);
    }

    public boolean getPrevOn(){
        return prevOn;
    }

    public float getSubPrevVol(int sub){
        return stepEventsManager.getPrevVol(sub);
    }

    public float getSubPrevPitch(int sub){
        return stepEventsManager.getPrevPitch(sub);
    }

    public float getPrevPan(){
        return stepEventsManager.getPrevPan();
    }

    /** SET **/
    public void setNOfSubs(int subs){
        stepEventsManager.setNOfSubs(subs);
        positionEvents(drumTrack.getNOfSteps(), false);
    }

    public void setOn(boolean on){
        this.on = on;

        if(on){
            stepEventsManager.turnOn();
        }
        else{
            stepEventsManager.turnOff();
        }
    }

    public void setSubOn(int sub, boolean on){
        stepEventsManager.setSubOn(sub, on);
    }

    public void updateEventSamples(){
        stepEventsManager.updateSamples();
    }

    //vol
    public void setVolumeModifier(float modifier, int sub){
        stepEventsManager.setVolumeModifier(modifier, sub);
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

    public void setRndOnPerc(float rndOnPerc, int sub) {

        //register an automation if it was off and is now on ONLY
        if(rndOnPerc > 0 && !stepEventsManager.getAutoRndOn(sub)){
            onAutosModified(true);
        }
        else if(rndOnPerc == 0 && stepEventsManager.getAutoRndOn(sub)){
            onAutosModified(false);
        }

        stepEventsManager.setRndOnPerc(rndOnPerc, sub);
    }

    public void setRndOnReturn(boolean rndOnReturn, int sub) {
        //save here, otherwise an empty prevValue will be used if return is on but not perc
        savePrevOn();
        saveSubPrevOn(sub);

        returnModified(rndOnReturn);
        stepEventsManager.setRndOnReturn(rndOnReturn, sub);
    }

    public void setRndVolMin(float rndVolMin, int sub) {
        stepEventsManager.setRndVolMin(rndVolMin, sub);
        //this.rndVolMin = rndVolMin;
    }

    public void setRndVolMax(float rndVolMax, int sub) {
        stepEventsManager.setRndVolMax(rndVolMax, sub);
        //this.rndVolMax = rndVolMax;
    }

    public void setRndVolPerc(float rndVolPerc, int sub) {

        //register an automation if it was off and is now on ONLY
        if(rndVolPerc > 0 && !stepEventsManager.getAutoRndVol(sub)){
            volAutosModified(true);
        }
        else if(rndVolPerc == 0 && stepEventsManager.getAutoRndVol(sub)){
            volAutosModified(false);
        }

        stepEventsManager.setRndVolPerc(rndVolPerc, sub);
        //this.rndVolPerc = rndVolPerc;
    }

    public void setRndVolReturn(boolean rndVolReturn, int sub) {
        //save here, otherwise an empty prevValue will be used if return is on but not perc
        saveSubPrevVol(sub);

        returnModified(rndVolReturn);
        stepEventsManager.setRndVolReturn(rndVolReturn, sub);
        //this.rndVolReturn = rndVolReturn;
    }

    public void setRndPitchMin(float rndPitchMin, int sub) {
        stepEventsManager.setRndPitchMin(rndPitchMin, sub);
        //this.rndPitchMin = rndPitchMin;
    }

    public void setRndPitchMax(float rndPitchMax, int sub) {
        stepEventsManager.setRndPitchMax(rndPitchMax, sub);
        //this.rndPitchMax = rndPitchMax;
    }

    public void setRndPitchPerc(float rndPitchPerc, int sub) {

        //register an automation if it was off and is now on ONLY
        if(rndPitchPerc > 0 && !stepEventsManager.getAutoRndPitch(sub)){
            pitchAutosModified(true);
        }
        else if(rndPitchPerc == 0 && stepEventsManager.getAutoRndPitch(sub)){
            pitchAutosModified(false);
        }

        stepEventsManager.setRndPitchPerc(rndPitchPerc, sub);
    }

    public void setRndPitchReturn(boolean rndPitchReturn, int sub) {
        //save here, otherwise an empty prevValue will be used if return is on but not perc
        saveSubPrevPitch(sub);

        returnModified(rndPitchReturn);
        stepEventsManager.setRndPitchReturn(rndPitchReturn, sub);
        //this.rndPitchReturn = rndVolReturn;
    }

    public void setRndPanMin(float rndPanMin) {
        stepEventsManager.setRndPanMin(rndPanMin);
        //this.rndPanMin = rndPitchMin;
    }

    public void setRndPanMax(float rndPanMax) {
        stepEventsManager.setRndPanMax(rndPanMax);
        //this.rndPanMax = rndVolMax;
    }

    public void setRndPanPerc(float rndPanPerc) {

        //register an automation if it was off and is now on ONLY
        if(rndPanPerc > 0 && !getAutoRndPan()){
            panAutosModified(true);
        }
        else if(rndPanPerc == 0 && getAutoRndPan()){
            panAutosModified(false);
        }

        stepEventsManager.setRndPanPerc(rndPanPerc);
        //this.rndPanPerc = rndVolPerc;
    }

    public void setRndPanReturn(boolean rndPanReturn) {
        //save here, otherwise an empty prevValue will be used if return is on but not perc
        saveSubPrevPan();

        returnModified(rndPanReturn);
        stepEventsManager.setRndPanReturn(rndPanReturn);
        //this.rndPanReturn = rndVolReturn;
    }

    /** SEQUENCER **/
    public void handleSequencerPositionChange(int sequencerPosition){
        soundManager.setPan(getPan());
    }

    /** RESET **/
    public void reset(){
        setOn(false);
        resetAutos();
        stepEventsManager.reset();
    }

    private void resetAutos(){
        onAutos = 0;
        panAutos = 0;
        volAutos = 0;
        pitchAutos = 0;
        returns = 0;
    }

    /** RESTORATION **/
    public void restore(StepKeeper k){
        setOn(k.on);
        stepEventsManager.restore(k.stepEventsManagerKeeper);
    }

    public StepKeeper getKeeper(){
        StepKeeper keeper = new StepKeeper();

        keeper.on = isOn();
        keeper.stepEventsManagerKeeper = stepEventsManager.getKeeper();
        return keeper;
    }

    /** DESTRUCTION **/
    public void destroy(){
        stepEventsManager.destroy();
    }
}

