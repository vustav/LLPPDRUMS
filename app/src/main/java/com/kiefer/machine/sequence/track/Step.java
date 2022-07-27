package com.kiefer.machine.sequence.track;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.StepKeeper;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.SoundSourceManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.eventManager.StepEventsManager;

import java.util.ArrayList;
import java.util.Random;

/** Each Step creates a StepEventManager that holds events (both Sample and Synth with one of them being muted) for all the Subs.
 * The events are added removed from the sequencer when OnOff is clicked, but never deleted unless the track is deleted. All
 * tracks that aren't supposed to be playing are muted. Their events are still in the sequencer, but since their instruments
 * are muted they won't play and doesn't impact performance in any way (other than memory)**/

public class Step {
    private LLPPDRUMS llppdrums;
    private final DrumTrack drumTrack;

    //private Events eventManager;
    private ArrayList<StepEventsManager> stepEventsManagers;

    private final Random random;


    //base prms
    private boolean on;

    public Step(LLPPDRUMS llppdrums, DrumTrack drumTrack, int subs, boolean on){
        this.llppdrums = llppdrums;
        this.drumTrack = drumTrack;
        random = new Random();

        this.on = on;
        //stepEventsManagers = new ArrayList<>();

        //on cretation we have to get these. Later when new soundSOurces are created they will add their managers themselves, but on creation steps is created after SSs, so that's not possible.
        getStepEvenntsManagers(subs);
    }

    private void getStepEvenntsManagers(int subs){
        stepEventsManagers = drumTrack.getSoundManager().getStepEventManagers(this, subs);
    }



    public void addStepEventsManager(StepEventsManager sem){
        stepEventsManagers.add(sem);
    }

    public void positionEvents(int nOfSteps, boolean onlySynth){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.positionEvents(nOfSteps, onlySynth);
        }
    }

    /** RANDOMIZE **/
    public void randomizeStepOn(){
        Random r = new Random();
        setOn(r.nextBoolean());

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            //if there's only one sub we need to turn it on, otherwise it will be temporarily off since you can't open the subs interface with one sub
            if (stepEventsManager.getNOfSubs() == 1) {
                stepEventsManager.setSubOn(0, on);
            }
        }
    }
    //vars = should the autoRnd variables be used or not??
    public void randomizeSubOn(int sub) {
        boolean on = random.nextBoolean();

        for (StepEventsManager stepEventsManager : stepEventsManagers) {
            if (stepEventsManager.getNOfSubs() > 1) {
                stepEventsManager.setSubOn(sub, on);
                //stepEventsManager.randomizeOn(sub);
            }
            //if there's only one sub we need to turn it on, otherwise it will be temporarily off since you can't open the subs interface with one sub
            else {
                stepEventsManager.setSubOn(0, on);
            }
        }
    }

    public void randomizeSubsOn() {
        for(int s = 0; s < drumTrack.getNOfSubs(); s++){
            randomizeSubOn(s);
        }
    }

    public void randomizeVol(int sub){
        for (StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setVolumeModifier(random.nextFloat(), sub);
        }
    }

    public void randomizeVols(){
        for(int s = 0; s < drumTrack.getNOfSubs(); s++){
            randomizeVol(s);
        }
    }

    public void randomizePitch(int sub){
        for (StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setPitchModifier(random.nextFloat(), sub);
        }
    }

    public void randomizePitches(){
        for(int s = 0; s < drumTrack.getNOfSubs(); s++){
            randomizePitch(s);
        }
    }
    public void randomizePan(){
        for (StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.randomizePan();
        }
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
        return stepEventsManagers.get(0).getVolumeModifier(sub);
    }

    public float getPitchModifier(int sub) {
        return stepEventsManagers.get(0).getPitchModifier(sub);
    }

    public float getPan() {
        return stepEventsManagers.get(0).getPan();
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
        return stepEventsManagers.get(0).getAutoRndOn(sub);
    }

    public boolean getRndOnReturn(int sub) {
        return stepEventsManagers.get(0).getRndOnReturn(sub);
    }

    public float getRndOnPerc(int sub) {
        return stepEventsManagers.get(0).getRndOnPerc(sub);
    }

    public boolean getAutoRndVol(int sub){
        return stepEventsManagers.get(0).getAutoRndVol(sub);
    }

    public float getRndVolMin(int sub) {
        return stepEventsManagers.get(0).getRndVolMin(sub);
    }

    public float getRndVolMax(int sub) {
        return stepEventsManagers.get(0).getRndVolMax(sub);
    }

    public float getRndVolPerc(int sub) {
        return stepEventsManagers.get(0).getRndVolPerc(sub);
    }

    public boolean getRndVolReturn(int sub){
        return stepEventsManagers.get(0).getRndVolReturn(sub);
    }

    public boolean getAutoRndPan(){
        return stepEventsManagers.get(0).getAutoRndPan();
    }

    public float getRndPanMin() {
        return stepEventsManagers.get(0).getRndPanMin();
    }

    public float getRndPanMax() {
        return stepEventsManagers.get(0).getRndPanMax();
    }

    public float getRndPanPerc() {
        return stepEventsManagers.get(0).getRndPanPerc();
    }

    public boolean getRndPanReturn(){
        return stepEventsManagers.get(0).getRndPanReturn();
    }

    public boolean getAutoRndPitch(int sub){
        return stepEventsManagers.get(0).getAutoRndPitch(sub);
    }

    public float getRndPitchMin(int sub) {
        return stepEventsManagers.get(0).getRndPitchMin(sub);
    }

    public float getRndPitchMax(int sub) {
        return stepEventsManagers.get(0).getRndPitchMax(sub);
    }

    public float getRndPitchPerc(int sub) {
        return stepEventsManagers.get(0).getRndPitchPerc(sub);
    }

    public boolean getRndPitchReturn(int sub){
        return stepEventsManagers.get(0).getRndPitchReturn(sub);
    }

    public int getNofSubs(){
        return stepEventsManagers.get(0).getNOfSubs();
    }

    public boolean isSubOn(int sub){
        return stepEventsManagers.get(0).isSubOn(sub);
    }

    public float getSubVol(int sub){
        return stepEventsManagers.get(0).getVolumeModifier(sub);
    }

    public float getSubPitch(int sub){
        return stepEventsManagers.get(0).getPitchModifier(sub);
    }

    /** PREV **/

    private boolean prevOn; //used return is on in AutoRandomization
    public void savePrevOn(){
        prevOn = on;
    }

    //used to manually save what it should return to (if return is on).
    public void saveSubPrevOn(int sub){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.savePrevOn(sub);
        }
    }

    public void saveSubPrevVol(int sub){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.savePrevVol(sub);
        }
    }

    public void saveSubPrevPitch(int sub){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.savePrevPitch(sub);
        }
    }
    public void saveSubPrevPan(){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.savePrevPan();
        }
    }

    public boolean getSubPrevOn(int sub){
        return stepEventsManagers.get(0).getPrevOn(sub);

    }

    public boolean getPrevOn(){
        return prevOn;
    }

    public float getSubPrevVol(int sub){
        return stepEventsManagers.get(0).getPrevVol(sub);

    }

    public float getSubPrevPitch(int sub){
        return stepEventsManagers.get(0).getPrevPitch(sub);

    }

    public float getPrevPan(){
        return stepEventsManagers.get(0).getPrevPan();

    }

    /** SET **/
    public void setNOfSubs(int subs){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setNOfSubs(subs);
        }
        positionEvents(drumTrack.getNOfSteps(), false);
    }

    public void setOn(boolean on){
        this.on = on;

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            if (on) {
                stepEventsManager.turnOn();
            } else {
                stepEventsManager.turnOff();
            }
        }
    }

    public void setSubOn(int sub, boolean on){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setSubOn(sub, on);
        }
    }

    public void updateEventSamples(){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.updateSamples();
        }
    }

    //vol
    public void setVolumeModifier(float modifier, int sub){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setVolumeModifier(modifier, sub);
        }
    }

    public void updateEventVolumes(){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.updateEventVolume();
        }
        //soundEvents.setVolume(drumTrack.getTrackVolume() * getVolumeModifier());
    }

    //pitch
    public void setPitchModifier(float modifier, int sub){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setPitchModifier(modifier, sub);
        }
        //pitchModifier = modifier;
        //updateEventPitches();
    }

    public void updateEventPitches(){
        //soundEvents.setPitch(getConvertedPitchModifier());
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.updateEventPitches();
        }
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
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setPan(pan);
        }
    }

    public void setRndOnPerc(float rndOnPerc, int sub) {

        //register an automation if it was off and is now on ONLY
        if (rndOnPerc > 0 && !stepEventsManagers.get(0).getAutoRndOn(sub)) {
            onAutosModified(true);
        } else if (rndOnPerc == 0 && stepEventsManagers.get(0).getAutoRndOn(sub)) {
            onAutosModified(false);
        }

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndOnPerc(rndOnPerc, sub);
        }
    }

    public void setRndOnReturn(boolean rndOnReturn, int sub) {
        //save here, otherwise an empty prevValue will be used if return is on but not perc
        savePrevOn();
        saveSubPrevOn(sub);

        returnModified(rndOnReturn);

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndOnReturn(rndOnReturn, sub);
        }
    }

    public void setRndVolMin(float rndVolMin, int sub) {

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndVolMin(rndVolMin, sub);
        }
    }

    public void setRndVolMax(float rndVolMax, int sub) {

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndVolMax(rndVolMax, sub);
        }
    }

    public void setRndVolPerc(float rndVolPerc, int sub) {


        //register an automation if it was off and is now on ONLY
        if (rndVolPerc > 0 && !stepEventsManagers.get(0).getAutoRndVol(sub)) {
            volAutosModified(true);
        } else if (rndVolPerc == 0 && stepEventsManagers.get(0).getAutoRndVol(sub)) {
            volAutosModified(false);
        }

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndVolPerc(rndVolPerc, sub);
        }
    }

    public void setRndVolReturn(boolean rndVolReturn, int sub) {
        //save here, otherwise an empty prevValue will be used if return is on but not perc
        saveSubPrevVol(sub);

        returnModified(rndVolReturn);
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndVolReturn(rndVolReturn, sub);
        }
        //this.rndVolReturn = rndVolReturn;
    }

    public void setRndPitchMin(float rndPitchMin, int sub) {
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndPitchMin(rndPitchMin, sub);
        }
        //this.rndPitchMin = rndPitchMin;
    }

    public void setRndPitchMax(float rndPitchMax, int sub) {
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndPitchMax(rndPitchMax, sub);
        }
        //this.rndPitchMax = rndPitchMax;
    }

    public void setRndPitchPerc(float rndPitchPerc, int sub) {

        //register an automation if it was off and is now on ONLY
        if(rndPitchPerc > 0 && !stepEventsManagers.get(0).getAutoRndPitch(sub)){
            pitchAutosModified(true);
        }
        else if(rndPitchPerc == 0 && stepEventsManagers.get(0).getAutoRndPitch(sub)){
            pitchAutosModified(false);
        }

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndPitchPerc(rndPitchPerc, sub);
        }
    }

    public void setRndPitchReturn(boolean rndPitchReturn, int sub) {
        //save here, otherwise an empty prevValue will be used if return is on but not perc
        saveSubPrevPitch(sub);

        returnModified(rndPitchReturn);

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndPitchReturn(rndPitchReturn, sub);
        }
        //this.rndPitchReturn = rndVolReturn;
    }

    public void setRndPanMin(float rndPanMin) {
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndPanMin(rndPanMin);
        }
        //this.rndPanMin = rndPitchMin;
    }

    public void setRndPanMax(float rndPanMax) {
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndPanMax(rndPanMax);
        }
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

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndPanPerc(rndPanPerc);
        }
        //this.rndPanPerc = rndVolPerc;
    }

    public void setRndPanReturn(boolean rndPanReturn) {
        //save here, otherwise an empty prevValue will be used if return is on but not perc
        saveSubPrevPan();

        returnModified(rndPanReturn);
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.setRndPanReturn(rndPanReturn);
        }
        //this.rndPanReturn = rndVolReturn;
    }

    /** SEQUENCER **/
    public void handleSequencerPositionChange(int sequencerPosition){
        drumTrack.getSoundManager().setPan(getPan());
    }

    /** RESET **/
    public void reset(){
        setOn(false);
        resetAutos();

        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.reset();
        }
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
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.restore(k.stepEventsManagerKeeper);
        }
    }

    public StepKeeper getKeeper(){
        StepKeeper keeper = new StepKeeper();

        keeper.on = isOn();
        keeper.stepEventsManagerKeeper = stepEventsManagers.get(0).getKeeper();
        return keeper;
    }

    /** DESTRUCTION **/

    public void destroyStepEventsManager(SoundSourceManager ssm){
        for(int i = 0; i < stepEventsManagers.size(); i++){
            if(stepEventsManagers.get(i).getSoundSourceManager() == ssm){
                StepEventsManager ssmm = stepEventsManagers.remove(i);
                ssmm.destroy();
                return;
            }
        }
    }

    public void destroy(){
        for(StepEventsManager stepEventsManager : stepEventsManagers) {
            stepEventsManager.destroy();
        }
    }
}

