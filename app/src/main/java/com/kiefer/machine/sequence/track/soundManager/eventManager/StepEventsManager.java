package com.kiefer.machine.sequence.track.soundManager.eventManager;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.soundSources.StepEventsManagerKeeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.track.soundManager.SoundManager;
import com.kiefer.utils.NmbrUtils;

import java.util.ArrayList;
import java.util.Random;

/** Each Step creates a StepEventManager that holds events (both Sample and Synth with one of them being muted) for all the Subs.
 * The events are added removed from the sequencer when OnOff is clicked, but never deleted unless the track is deleted. All
 * tracks that aren't supposed to be playing are muted. Their events are still in the sequencer, but since their instruments
 * are muted they won't play and doesn't impact performance in any way (other than memory)**/

public class StepEventsManager {
    private final LLPPDRUMS llppdrums;
    private final DrumSequence drumSequence;
    private final DrumTrack drumTrack;
    private final SoundManager soundManager;
    private final Step step;
    //private final int stepNo; //we need to pass it since step.getStepNo() returns -1 at creation when we need it

    private ArrayList<Sub> subs;

    private float pan;
    private float rndPanMin, rndPanMax, rndPanPerc;
    private boolean rndPanReturn;

    public StepEventsManager(LLPPDRUMS llppdrums, DrumSequence drumSequence, DrumTrack drumTrack, SoundManager soundManager, Step step, int nOfSubs){
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.drumTrack = drumTrack;
        this.soundManager = soundManager;
        this.step = step;
        //this.stepNo = stepNo;

        //Log.e("StepEventsManager", "createSubs");
        createSubs(nOfSubs);
        positionEvents(drumSequence.getNOfSteps(), false);

        setupAutoRndParams();

        randomizePan();
    }

    private void setupAutoRndParams(){
        //autoRndPan = false;
        rndPanPerc = 0;
        rndPanMin = NmbrUtils.getRndmizer(0, .2f);
        rndPanMax = NmbrUtils.getRndmizer(.7f, 1);
        rndPanReturn = false;
    }

    /** ONOFF STEP **/
    //these are for steps, so don't use setSubOn() since it will turn off the sub
    public void turnOn(){
        for(int sub = 0; sub < subs.size(); sub++){
            //Log.e("StepEventsManager", "turnOn(), sub.size(): "+(subs.size()));
            //Log.e("StepEventsManager", "turnOn(), sub = on: "+(subs.get(sub).isOn()));
            boolean on = subs.get(sub).isOn(); //subs can still be on even if the step is off, turn them back on if so
            subs.get(sub).addToSequencer(on);
        }
    }

    public void turnOff(){
        for(int sub = 0; sub < subs.size(); sub++){
            subs.get(sub).addToSequencer(false);
        }
    }

    /** ONOFF SUB **/
    public void setSubOn(int sub, boolean on){
        if(subs.size() > 1) {
            subs.get(sub).setOn(on);
        }
        else{
            //always on if 1 sub
            subs.get(sub).setOn(true);
        }
    }

    public boolean isSubOn(int sub){
        return subs.get(sub).isOn();
    }

    /** SOUND **/
    public void updateSamples(){
        for(Sub s : subs){
            s.updateSamples();
        }
    }

    /** SUBS **/
    private void createSubs(int nOfSubs){
        subs = new ArrayList<>();
        for(int i = 0; i<nOfSubs; i++){
            if(nOfSubs == 1) {
                addSub(true);
            }
            else{
                addSub(false);
            }
        }
    }

    private void addSub(boolean guaranteeOn){
        subs.add(new Sub(llppdrums, drumSequence, drumTrack, soundManager, this, step, guaranteeOn));
    }

    public void setNOfSubs(int nOfSubs){
        while (subs.size() < nOfSubs) {
            addSub(false);
        }
        while (subs.size() > nOfSubs){
            deleteSub();
        }

        if(subs.size() == 1){
            setSubOn(0, true);
        }
    }

    private void deleteSub(Sub sub){
        subs.remove(sub);
        sub.delete();
    }

    public void deleteSub(){
        deleteSub(subs.get(subs.size()-1));
    }

    public void deleteSubs(){
        if(subs != null) {
            if (subs.size() > 0) {
                for (int i = subs.size() - 1; i >= 0; i--) {
                    deleteSub(subs.get(i));
                }
            }
        }
    }

    /** PREV **/
    public void savePrevOn(int sub){
        subs.get(sub).savePrevOn();
    }

    public void savePrevVol(int sub){
        subs.get(sub).savePrevVol();
    }

    public void savePrevPitch(int sub){
        subs.get(sub).savePrevPitch();
    }

    /** POSITION **/
    public void positionEvents(int nOfSteps, boolean onlySynth){

        //Log.e("StepEventsManager", "nOfSteps: "+nOfSteps);
        //Log.e("StepEventsManager", "subs size: "+subs.size());

        //if(step.getStepNo() == 0) {
        //Log.e("StepEventsManager", "positionEvents");
        //}
        //this.step = step;
        int samplesPerStep = getSamplesPerStep(nOfSteps);

        int posInSamples = getPosInSamples(samplesPerStep);
        int samplesPerSub = getSamplesPerSub(samplesPerStep);

        //Log.e("StepEventsManager", "positionEvents(), nOfSteps: "+nOfSteps);
        //Log.e("StepEventsManager", "positionEvents(), getPosInSamples: "+posInSamples);
        //Log.e("StepEventsManager", "positionEvents(), getSamplesPerSub: "+samplesPerSub);

        if(subs.size() > 0) {
            for(Sub s : subs){
                //Log.e("StepEventsManager", "sub: "+s.getIndex());
                s.positionEvents(posInSamples, onlySynth);
                posInSamples += samplesPerSub;
            }
        }
    }

    protected int getSamplesPerStep(int nOfSteps){
        /** FIXA MASSA LOGGAR OCH KOLLA SÅ DET BLIR RÄTT **/
        return ((60/(drumSequence.getTempo()/llppdrums.getEngineFacade().getBEAT_AMOUNT())) * llppdrums.getEngineFacade().getSAMPLE_RATE()) / nOfSteps;
    }

    protected int getPosInSamples(int samplesPerStep){

        //if(step.getStepNo() == -1){
        //Log.e("StepEventsManager", "samplesPerStep: "+samplesPerStep);
        //Log.e("StepEventsManager", "drumTrack.getSteps().size(): "+drumTrack.getSteps().size());
        //return samplesPerStep * drumTrack.getSteps().size();
        //}

        return samplesPerStep * step.getStepNo();
    }

    protected int getSamplesPerSub(int samplesPerStep){
        //Log.e("StepEventsManager", "getSamplesPerSub(), nOfSubs: "+drumTrack.getNOfSubs());
        return samplesPerStep / drumTrack.getNOfSubs();
    }

    /** RND **/
    public void randomizeOn(int sub) {
        subs.get(sub).randomizeOn();
        //events.get(sub).randomizeOn(autoRnd);
    }
    public void randomizeSubsOn() {
        //for(EventsOLD.Event e : events) {
        //e.randomizeOn(autoRnd);
        //}
        for(Sub s : subs){
            s.randomizeOn();
        }
    }
    public void randomizeVol(int sub){
        subs.get(sub).randomizeVol();
    }
    public void randomizeVols(){
        for(Sub s : subs){
            s.randomizeVol();
        }
    }
    public void randomizePitch(int sub){
        subs.get(sub).randomizePitch();
    }
    public void randomizePitches(){
        //for(EventsOLD.Event e : events){
        //e.randomizePitch(autoRnd);
        //}

        for(Sub s : subs){
            s.randomizePitch();
        }
    }

    /** PAN (all subs will have the same pan so it's handled here, everything else is handled in subs) **/
    public void randomizePan(){
        Random random = new Random();
        setPan(random.nextFloat());
    }

    /** PREV **/
    public void savePrevPan(){
        prevPan = pan;
    }

    public float getPrevPan() {
        return prevPan;
    }

    public boolean getPrevOn(int sub){
        return subs.get(sub).getPrevOn();
    }

    public float getPrevVol(int sub){
        return subs.get(sub).getPrevVolumeModifier();
    }

    public float getPrevPitch(int sub){
        return subs.get(sub).getPrevPitchModifier();
    }

    /**************/
    public boolean getAutoRndPan(){
        return rndPanPerc > 0;
    }

    public float getRndPanMin() {
        return rndPanMin;
    }

    public float getRndPanMax() {
        return rndPanMax;
    }

    public float getRndPanPerc() {
        return rndPanPerc;
    }

    public boolean getRndPanReturn(){
        return rndPanReturn;
    }

    public float getPan(){
        return pan;
    }
    public void setRndPanMin(float rndPitchMin) {
        this.rndPanMin = rndPitchMin;
    }

    public void setRndPanMax(float rndVolMax) {
        this.rndPanMax = rndVolMax;
    }

    public void setRndPanPerc(float rndVolPerc) {
        this.rndPanPerc = rndVolPerc;
    }

    public void setRndPanReturn(boolean rndVolReturn) {
        this.rndPanReturn = rndVolReturn;
    }

    /** GET **/
    public ArrayList<Sub> getSubs(){
        return subs;
    }

    public int getNOfSubs(){
        return subs.size();
    }

    public float getVolumeModifier(int sub) {
        return subs.get(sub).getVolumeModifier();
    }
    public float getPitchModifier(int sub) {
        return subs.get(sub).getPitchModifier();
    }

    public boolean getAutoRndOn(int sub){
        return subs.get(sub).getAutoRndOn();
    }

    public boolean getRndOnReturn(int sub) {
        return subs.get(sub).getRndOnReturn();
    }

    public float getRndOnPerc(int sub) {
        return subs.get(sub).getRndOnPerc();
    }

    public boolean getAutoRndVol(int sub){
        return subs.get(sub).getAutoRndVol();
    }

    public float getRndVolMin(int sub) {
        return subs.get(sub).getRndVolMin();
    }

    public float getRndVolMax(int sub) {
        return subs.get(sub).getRndVolMax();
    }

    public float getRndVolPerc(int sub) {
        return subs.get(sub).getRndVolPerc();
    }

    public boolean getRndVolReturn(int sub){
        return subs.get(sub).getRndVolReturn();
    }

    public boolean getAutoRndPitch(int sub){
        return subs.get(sub).getAutoRndPitch();
    }

    public float getRndPitchMin(int sub) {
        return subs.get(sub).getRndPitchMin();
    }

    public float getRndPitchMax(int sub) {
        return subs.get(sub).getRndPitchMax();
    }

    public float getRndPitchPerc(int sub) {
        return subs.get(sub).getRndPitchPerc();
    }

    public boolean getRndPitchReturn(int sub){
        return subs.get(sub).getRndPitchReturn();
    }

    /** SET **/

    //vol
    public void setVolumeModifier(float modifier, int sub){
        subs.get(sub).setVolumeModifier(modifier);
    }

    public void updateEventVolume(){
        for(Sub s : subs){
            s.updateEventVolume();
        }
    }

    //pitch
    public void setPitchModifier(float modifier, int sub){
        subs.get(sub).setPitchModifier(modifier);
    }

    public void updateEventPitches(){
        for(Sub s : subs){
            s.updateEventPitch();
        }
    }

    //pan
    private float prevPan;
    public void setPan(float pan){
        //prevPan = this.pan;
        this.pan = pan;
    }

    //on
    public void setRndOnPerc(float rndOnPerc, int sub) {
        subs.get(sub).setRndOnPerc(rndOnPerc);
    }

    public void setRndOnReturn(boolean rndOnReturn, int sub) {
        subs.get(sub).setRndOnReturn(rndOnReturn);
    }

    //vol
    public void setRndVolMin(float rndVolMin, int sub) {
        subs.get(sub).setRndVolMin(rndVolMin);
    }

    public void setRndVolMax(float rndVolMax, int sub) {
        //this.rndVolMax = rndVolMax;
        subs.get(sub).setRndVolMax(rndVolMax);
    }

    public void setRndVolPerc(float rndVolPerc, int sub) {
        //this.rndVolPerc = rndVolPerc;
        subs.get(sub).setRndVolPerc(rndVolPerc);
    }

    public void setRndVolReturn(boolean rndVolReturn, int sub) {
        //this.rndVolReturn = rndVolReturn;
        subs.get(sub).setRndVolReturn(rndVolReturn);
    }

    //pitch
    public void setRndPitchMin(float rndPitchMin, int sub) {
        //this.rndPitchMin = rndPitchMin;
        subs.get(sub).setRndPitchMin(rndPitchMin);
    }

    public void setRndPitchMax(float rndPitchMax, int sub) {
        //this.rndPitchMax = rndPitchMax;
        subs.get(sub).setRndPitchMax(rndPitchMax);
    }

    public void setRndPitchPerc(float rndVolPerc, int sub) {
        //this.rndPitchPerc = rndVolPerc;
        subs.get(sub).setRndPitchPerc(rndVolPerc);
    }

    public void setRndPitchReturn(boolean rndVolReturn, int sub) {
        //this.rndPitchReturn = rndVolReturn;
        subs.get(sub).setRndPitchReturn(rndVolReturn);
    }

    /** RESTORE **/
    public void restore(StepEventsManagerKeeper k){

        setPan(Float.parseFloat(k.pan));
        setRndPanMin(Float.parseFloat(k.rndPanMin));
        setRndPanMax(Float.parseFloat(k.rndPanMax));
        setRndPanPerc(Float.parseFloat(k.rndPanPerc));
        setRndPanReturn(k.rndPanReturn);

        for(int i = 0; i<subs.size(); i++){
            subs.get(i).restore(k.subKeepers.get(i));
        }
    }

    public StepEventsManagerKeeper getKeeper(){

        StepEventsManagerKeeper keeper = new StepEventsManagerKeeper();

        keeper.pan = Float.toString(getPan());

        //keeper.autoRndPan = getAutoRndPan();
        keeper.rndPanMin = Float.toString(getRndPanMin());
        keeper.rndPanMax = Float.toString(getRndPanMax());
        keeper.rndPanPerc= Float.toString(getRndPanPerc());
        keeper.rndPanReturn = getRndPanReturn();

        keeper.subKeepers = new ArrayList<>();
        for(Sub s : subs){
            keeper.subKeepers.add(s.getKeeper());
        }

        return keeper;
    }

    /** RESET **/
    public void reset(){
        setRndPanPerc(0);
        for(Sub s : subs) {
            s.reset();
        }
    }

    /** DESTRUCTION **/
    public void destroy(){
        turnOff();
        deleteSubs();
        subs = null;
    }
}
