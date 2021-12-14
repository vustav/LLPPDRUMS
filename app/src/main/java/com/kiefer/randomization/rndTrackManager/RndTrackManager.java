package com.kiefer.randomization.rndTrackManager;

import android.graphics.drawable.Drawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.sequenceModules.Pan;
import com.kiefer.machine.sequence.sequenceModules.Pitch;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.Volume;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.utils.ImgUtils;

import java.util.Random;

/** ONÖDIGT ATT VARJE TRACK HAR VARSIN =|| **/

public class RndTrackManager {
    private final LLPPDRUMS llppdrums;
    private final DrumTrack drumTrack;

    private final int presetListImgId;

    //rnd-params
    private boolean rndOsc, rndOn, rndVol, rndPitch, rndPan, rndFx, rndMix;

    int onBound = 2;

    private Random random;

    public RndTrackManager(LLPPDRUMS llppdrums, DrumTrack drumTrack) {
        this.llppdrums = llppdrums;
        this.drumTrack = drumTrack;

        presetListImgId = ImgUtils.getRandomImageId();

        random = new Random();
        setupRndParams();
    }

    private void setupRndParams(){
        rndOsc = random.nextInt(2) == 0;
        rndOn = random.nextInt(2) == 0;
        rndVol = random.nextInt(2) == 0;
        rndPitch = random.nextInt(2) == 0;
        rndPan = random.nextInt(2) == 0;
        rndFx = random.nextInt(2) == 0;
        rndMix = random.nextInt(2) == 0;
    }

    public void randomizeSubOn(Step d, boolean autoRnd, int sub) {
        d.randomizeOn(autoRnd, sub);
    }

    public void randomizeSubsOn(Step d, boolean autoRnd) {
        d.randomizeSubsOn(autoRnd);
    }

    public void randomizeSubVolume(Step d, boolean autoRnd, int sub) {
        d.randomizeVol(autoRnd, sub);
    }

    public void randomizeSubVolumes(Step d, boolean autoRnd) {
        d.randomizeVols(autoRnd);
    }

    public void randomizeSubPitch(Step d, boolean autoRnd, int sub) {
        d.randomizePitch(autoRnd, sub);
    }

    public void randomizeSubPitches(Step d, boolean autoRnd) {
        d.randomizePitches(autoRnd);
    }

    public void randomizePan(Step d, boolean autoRnd) {
        d.randomizePan(autoRnd);
    }

    public void randomizeAll(SequenceModule seqModule){
        seqModule.randomizeAll(drumTrack);
    }

    public static final String RANDOM = "RANDOM";
    public void randomize(RndSeqPresetTrack rndTrack){
        Random r = new Random();

        drumTrack.setNOfSubs(666, rndTrack.getnOfSubs()); //trackNo doesn't matter since the drumTrack is the subilizer

        //first randomize the soundSource, then set the preset category

        /** KRASCHAR HÄR NÅNSTANS **/
        //drumTrack.getSoundManager().randomizeSoundSource();
        /*****************************/
        if(rndTrack.getPresetCategory().equals(RANDOM)){
            drumTrack.getSoundManager().setRandomPreset();
        }
        else{
            drumTrack.getSoundManager().setPreset(rndTrack.getPresetCategory());
        }

        for(int stepNo = 0; stepNo < drumTrack.getNOfSteps(); stepNo++){
            RndSeqPresetTrack.Step step = rndTrack.getSteps().get(stepNo);

            //used to set the step ON if at least on sub is ON
            boolean subOn = false;

            for(int sub = 0; sub<drumTrack.getNOfSubs(); sub++) {
                if(r.nextFloat() < step.getSubPerc(sub)){
                    drumTrack.setSubOn(stepNo, true, sub);
                    subOn = true;
                    drumTrack.setSubVolume(stepNo, step.getSubVol(sub), sub);
                    drumTrack.setSubPitchModifier(stepNo, step.getSubPitch(sub), sub);
                }
                else{
                    drumTrack.setStepOn(stepNo, false);
                }
            }
            drumTrack.setStepOn(stepNo, subOn);

            if (rndTrack.getRandomizePan()){
                drumTrack.setStepPan(stepNo, step.getSubPan());
            }
        }

        if(rndTrack.getRandomizeFx()) {
            drumTrack.getFxManager().randomizeAll();
        }

        if (rndTrack.getRandomizeVol()){
            drumTrack.randomizeVol();
        }

        //just a little chance of hotting up the pitch and volume here
        if(r.nextInt(9) == 0){
            drumTrack.getRndTrackManager().hottenUp(true);
        }
        else {
            drumTrack.getRndTrackManager().hottenUp(false);
        }

        if(llppdrums.getDrumMachine().getSelectedSequence() == drumTrack.getDrumSequence()) {
            drumTrack.updateDrawables();
        }
    }

    public void randomizeAll() {

        for (int step = 0; step < drumTrack.getSteps().size(); step++) {

            final Step d = drumTrack.getSteps().get(step);

            //randomize
            randomizeSubsOn(d, false);


            //new Thread(new Runnable() {
            //public void run() {

            //if its on after that, randomize the other values
            if (d.isOn()) {
                randomizeSubVolumes(d, false);
                randomizeSubPitches(d, false);
                randomizePan(d, false);
            }
        }
        //drumTrack.updateDrawables();
    }

    public void randomizeStepsOn(){
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            final Step d = drumTrack.getSteps().get(step);

            //randomize
            randomizeSubsOn(d, false);
        }
    }

    public void randomizeStepVols(){
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            final Step d = drumTrack.getSteps().get(step);

            //randomize
            randomizeSubVolumes(d, false);
        }
    }

    public void randomizeStepPitches(){
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            final Step d = drumTrack.getSteps().get(step);

            //randomize
            randomizeSubPitches(d, false);
        }
    }

    public void randomizeStepPans(){
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            final Step d = drumTrack.getSteps().get(step);

            //randomize
            randomizePan(d, false);
        }
    }

    public void hottenUp(boolean pitch){
        /*
        for(Step d : drumTrack.getDrums()){
            d.setVolumeModifier(d.getVolumeModifier() * getMiniRandomMultiplier());

            if(pitch) {
                d.setPitchModifier(d.getPitchModifier() * getMiniMiniRandomMultiplier());
            }
        }

         */
    }

    // .9 - 1.1
    public float getMiniRandomMultiplier(){
        return random.nextFloat() * .2f + .9f;
    }

    // .95 - 1.05
    public float getMiniMiniRandomMultiplier(){
        return random.nextFloat() * .1f + .95f;
    }

    public void autoRandomize(){
        //is it better to always start a thread and do the loop in it or only start a thread if changes has occured when fetching the drawable??
        for(int stepNo = 0; stepNo < drumTrack.getSteps().size(); stepNo++){
            boolean updateDrawables = false;
            Step d = drumTrack.getSteps().get(stepNo);

            //if(drumTrack.getSteps().get(stepNo).isOn())

            for(int sub = 0; sub < drumTrack.getNOfSubs(); sub++) {

                //start by randomizing on/off
                if (d.getAutoRndOn(sub)) {

                    //start by turning the step on if one sub is set for autoRandomization
                    drumTrack.getSteps().get(stepNo).setOn(true);

                    //keep the former state to know if any updates are necessary
                    boolean wasOn = d.isSubOn(sub);

                    //randomize
                    randomizeSubOn(d, true, sub);

                    //drawables should be updated if a change has occurred and OnOff is selected
                    if (d.isSubOn(sub) != wasOn) {
                        //we should always update colors here so don't only do this for OnOff
                        updateDrawables = true;
                    }

                }

                //if its on after that, randomize the other values
                if (d.isOn()) {
                    if (d.getAutoRndVol(sub)) {
                        randomizeSubVolume(d, true, sub);

                        if (llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Volume) {
                            updateDrawables = true;
                        }
                    }
                    if (d.getAutoRndPitch(sub)) {
                        randomizeSubPitch(d, true, sub);

                        if (llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Pitch) {
                            updateDrawables = true;
                        }
                    }

                }

                //update the drawable if a change occurred in the selected sequence and the playing sequence is selected
                if (updateDrawables && llppdrums.getDrumMachine().getPlayingSequence() == llppdrums.getDrumMachine().getSelectedSequence()) {

                    /** KOLLA OM MAN KAN KÖRA ALLA EFTER LOOPEN IST SÅ KAN MAN GÖRA DET I EGEN TRÅD **/
                    updateDrawable(stepNo);
                }
            }

            //pans don't work on subs so use it here
            if (d.getAutoRndPan()) {
                randomizePan(d, true);

                if (llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Pan) {
                    updateDrawable(stepNo);
                }
            }
        }
    }

    private void updateDrawable(final int step){
        Drawable drawable = llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule().getDrawable(drumTrack.getTrackNo(), step);
        llppdrums.getSequencer().setStepDrawable(drawable, drumTrack.getTrackNo(), step);
    }

    /** SET **/
    public void setRndFx(boolean rndFx) {
        this.rndFx = rndFx;
    }

    public void setRndMix(boolean rndMix) {
        this.rndMix = rndMix;
    }

    public void setRndOsc(boolean rndOsc) {
        this.rndOsc = rndOsc;
    }

    public void setRndOn(boolean rndSteps) {
        this.rndOn = rndSteps;
    }

    public void setRndVol(boolean rndVol) {
        this.rndVol = rndVol;
    }

    public void setRndPitch(boolean rndPitch) {
        this.rndPitch = rndPitch;
    }

    public void setRndPan(boolean rndPan) {
        this.rndPan = rndPan;
    }

    /** GET **/
    public boolean getRndFx() {
        return rndFx;
    }

    public boolean getRndMix() {
        return rndMix;
    }

    public boolean getRndOsc() {
        return rndOsc;
    }

    public boolean getRndOn() {
        return rndOn;
    }

    public boolean getRndVol() {
        return rndVol;
    }

    public boolean getRndPitch() {
        return rndPitch;
    }

    public boolean getRndPan() {
        return rndPan;
    }

    public int getPresetListImgId() {
        return presetListImgId;
    }
}
