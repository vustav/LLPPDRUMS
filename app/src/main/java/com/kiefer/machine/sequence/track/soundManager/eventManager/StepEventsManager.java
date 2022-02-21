package com.kiefer.machine.sequence.track.soundManager.eventManager;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.Keeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.track.soundManager.SoundManager;
import com.kiefer.machine.sequence.track.soundManager.eventManager.old.EventsOLD;
import com.kiefer.utils.NmbrUtils;

import java.util.ArrayList;
import java.util.Random;

/** every step has an eventManager that manages both Synth and SampleEvents **/

public class StepEventsManager {
    private final LLPPDRUMS llppdrums;
    private final DrumSequence drumSequence;
    private final DrumTrack drumTrack;
    private final SoundManager soundManager;
    private final Step step;

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

        createSubs(nOfSubs);
        positionEvents(drumSequence.getNOfSteps());

        setupAutoRndParams();

        randomizePan(false);
    }

    private void setupAutoRndParams(){
        //autoRndPan = false;
        rndPanPerc = 0;
        rndPanMin = NmbrUtils.getRndmizer(0, .2f);
        rndPanMax = NmbrUtils.getRndmizer(.7f, 1);
        rndPanReturn = false;
    }

    /** ONOFF **/
    public void turnOn(int nOfSteps, int step){
        //on = true;

        for(int sub = 0; sub < subs.size(); sub++){
            setSubOn(sub, true);
        }
        /*
        if(subs.size() > 0) {
            for(Sub s : subs) {
                if(step.is()) {
                    s.addToSequencer();
                }
            }
        }

         */
    }

    public void turnOff(){


        for(int sub = 0; sub < subs.size(); sub++){
            setSubOn(sub, false);
        }
        //on = false;
        /*
        if(subs != null) {
            for (Sub s : subs) {
                if (s != null) {
                    // behövs if(se.on)??
                    s.removeFromSequencer();
                }
            }
        }

         */
    }

    public void setSubOn(int sub, boolean on){
        subs.get(sub).setOn(on);
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
            addSub();
        }
    }

    private void addSub(){
        subs.add(new Sub(llppdrums, drumSequence, drumTrack, soundManager, step));
    }

    public void setNOfSubs(int nOfSteps, int nOfSubs, boolean addToSequencer){
        Random r = new Random();
        while (subs.size() < nOfSubs) {
            addSub();
        }
        while (subs.size() > nOfSubs){
            deleteSub();
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

    /** POSITION **/
    public void positionEvents(int nOfSteps){
        //this.step = step;
        int samplesPerStep = getSamplesPerStep(nOfSteps);

        int posInSamples = getPosInSamples(samplesPerStep);
        int samplesPerSub = getSamplesPerSub(samplesPerStep);

        if(subs.size() > 0) {
            for(Sub s : subs){
                s.positionEvents(posInSamples);
                posInSamples += samplesPerSub;
            }
        }
    }

    protected int getSamplesPerStep(int nOfSteps){
        return ((60/(drumSequence.getTempo()/llppdrums.getEngineFacade().getBEAT_AMOUNT())) * llppdrums.getEngineFacade().getSAMPLE_RATE()) / nOfSteps;
    }

    protected int getPosInSamples(int samplesPerStep){
        return samplesPerStep * step.getStepNo();
    }

    protected int getSamplesPerSub(int samplesPerStep){
        return samplesPerStep / drumTrack.getNOfSubs();
    }

    /** RND **/
    public void randomizeOn(boolean autoRnd, int sub) {
        subs.get(sub).randomizeOn(autoRnd);
        //events.get(sub).randomizeOn(autoRnd);
    }
    public void randomizeSubsOn(boolean autoRnd) {
        //for(EventsOLD.Event e : events) {
            //e.randomizeOn(autoRnd);
        //}
        for(Sub s : subs){
            s.randomizeOn(autoRnd);
        }
    }
    public void randomizeVol(boolean autoRnd, int sub){
        subs.get(sub).randomizeVol(autoRnd);
    }
    public void randomizeVols(boolean autoRnd){
        //for(EventsOLD.Event e : events){
            //e.randomizeVol(autoRnd);
        //}

        for(Sub s : subs){
            s.randomizeVol(autoRnd);
        }
    }
    public void randomizePitch(boolean autoRnd, int sub){
        subs.get(sub).randomizePitch(autoRnd);
    }
    public void randomizePitches(boolean autoRnd){
        //for(EventsOLD.Event e : events){
            //e.randomizePitch(autoRnd);
        //}

        for(Sub s : subs){
            s.randomizePitch(autoRnd);
        }
    }

    /** PAN (all subs will have the same pan so it's handled here, everything else is handled in subs) **/
    public void randomizePan(boolean autoRnd){
        Random random = new Random();
        if(autoRnd){
            if(rndPanPerc >= random.nextFloat()) {
                setPan(NmbrUtils.getRndmizer(rndPanMin, rndPanMax));
            }
        }
        else{
            setPan(random.nextFloat());
        }
    }

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
    public void setPan(float pan){
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
    public void restore(Keeper k){
        //
    }

    public Keeper getKeeper(){
        return null;
    }

    /** RESET **/
    public void reset(){
        setRndPanPerc(0);
        for(Sub s : subs) {
            s.setRndOnPerc(0);
            s.setRndVolPerc(0);
            s.setRndPitchPerc(0);
        }
    }

    /** DESTRUCTION **/public void destroy(){
        turnOff();
        deleteSubs();
        subs = null;
    }
}
