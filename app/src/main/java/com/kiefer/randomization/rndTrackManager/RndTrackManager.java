package com.kiefer.randomization.rndTrackManager;

import android.graphics.drawable.Drawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.sequenceModules.OnOff;
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
        d.randomizeSubOn(autoRnd, sub);
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

    public void randomizeModule(SequenceModule seqModule){
        seqModule.randomize(drumTrack);
    }

    public static final String RANDOM = "RANDOM";
    public void randomize(RndSeqPresetTrack rndTrack){
        Random r = new Random();

        drumTrack.setNOfSubs(666, rndTrack.getnOfSubs()); //trackNo doesn't matter since the drumTrack is the subilizer

        if(rndTrack.getPresetCategory().equals(RANDOM)){
            drumTrack.getSoundManager().setRandomPresets();
        }
        else{
            drumTrack.getSoundManager().setPresets(rndTrack.getPresetCategory());
        }
        drumTrack.getSoundManager().randomizeSoundSource();

        //Log.e("RndTrackManager", "randomize(), nOfSteps: "+drumTrack.getNOfSteps());
        for(int stepNo = 0; stepNo < drumTrack.getNOfSteps(); stepNo++){
            RndSeqPresetTrack.Step step = rndTrack.getSteps().get(stepNo);

            //used to set the step ON if at least on sub is ON
            boolean aSubIsOn = false;

            for(int sub = 0; sub < drumTrack.getNOfSubs(); sub++) {
                if(r.nextFloat() <= step.getSubPerc(sub)){
                    //drumTrack.setStepOn(stepNo, true); //step needs to be on when turning on subs to get them to add the event to the sequencer, so do this first
                    drumTrack.setSubOn(stepNo, true, sub);
                    drumTrack.setSubVolume(stepNo, step.getSubVol(sub), sub);
                    drumTrack.setSubPitchModifier(stepNo, step.getSubPitch(sub), sub);
                    aSubIsOn = true;
                }
                else{
                    drumTrack.setSubOn(stepNo, false, sub);
                }
            }

            /** ÄNDRA TILLBAKS, SUBS MÅSTE ADDA EVENTS HÄR OM DOM ÄR ON **/
            //if(aSubIsOn) {
            drumTrack.setStepOn(stepNo, aSubIsOn); //turn off the step if no subs are on
            //Log.e("RndTrackManager", "randomize -----------------------------------------------");
            //}
            /*
            if(!aSubIsOn) {
                drumTrack.setStepOn(stepNo, false); //turn off the step if no subs are on
            }

             */

            if (rndTrack.getRandomizePan()){
                drumTrack.setStepPan(stepNo, step.getPan());
                //randomizePan(drumTrack, false);
            }
        }

        if(rndTrack.getRandomizeFx()) {
            drumTrack.getFxManager().randomizeAll();
        }

        if (rndTrack.getRandomizeVol()){
            drumTrack.randomizeVol();
        }

        if(llppdrums.getDrumMachine().getSelectedSequence() == drumTrack.getDrumSequence()) {
            drumTrack.updateDrawables();
        }
    }

    public void randomize() {

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
            randomizeStepOn(step);
        }
    }

    public void randomizeStepOn(int step){
        //Log.e("RndTrackManager","randomizeStepOn()");
        Step s = drumTrack.getSteps().get(step);
        s.randomizeStepOn();
        s.randomizeSubsOn(false);
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

    public void autoRandomize(){
        if(drumTrack.automationsActive()) {
            for (int stepNo = 0; stepNo < drumTrack.getSteps().size(); stepNo++) {

                boolean updateDrawables = false;
                Step s = drumTrack.getSteps().get(stepNo);

                if(s.automationActive()) {

                    //used to turn on the step if a sub is on or off otherwise. Start with the the
                    // steps on-vale, that way no change is made if no randomization has occured.
                    boolean atLeastOneSubOn = false;

                    for (int sub = 0; sub < drumTrack.getNOfSubs(); sub++) {

                        //start by randomizing on/off
                        if (s.getAutoRndOn(sub)) {

                            //start by turning the step on if one sub is set for autoRandomization
                            drumTrack.getSteps().get(stepNo).setOn(true);

                            //if there's only one sub, randomize the step
                            if (s.getNofSubs() == 1) {
                                //Log.e("RndTrackManager", "auto one sub");

                                //keep the former state to know if any updates are necessary
                                boolean wasOn = s.isOn();

                                s.savePrevOn();

                                randomizeStepOn(stepNo);

                                /** nån bugg här som gör att den inte uppdaterar ordentligt så checken är borttagen, fixa vid tid **/
                                //drawables should be updated if a change has occurred
                                //if (s.isOn() != wasOn) {
                                //we should always update colors here so don't only do this for OnOff
                                updateDrawables = true;
                                //}
                            }
                            //otherwise randomize the sub
                            else {
                                boolean wasOn = s.isSubOn(sub);

                                s.saveSubPrevOn(sub);

                                randomizeSubOn(s, true, sub);

                                if (s.isSubOn(sub)) {
                                    atLeastOneSubOn = true;
                                }

                                if(s.isSubOn(sub) != wasOn){
                                    updateDrawables = true;
                                }
                            }
                        }

                        //turn on the step if a sub is on (only of more subs than one, otherwise it was handled in randomizeStepOn())
                        if(s.getNofSubs() > 1){
                            s.setOn(atLeastOneSubOn);
                        }

                        //if its on after that, randomize the other values
                        if (s.isOn()) {
                            if (s.getAutoRndVol(sub)) {
                                //if(s.ges)
                                s.saveSubPrevVol(sub);
                                randomizeSubVolume(s, true, sub);

                                if (llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Volume) {
                                    updateDrawables = true;
                                }
                            }
                            if (s.getAutoRndPitch(sub)) {
                                s.saveSubPrevPitch(sub);
                                randomizeSubPitch(s, true, sub);

                                if (llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Pitch) {
                                    updateDrawables = true;
                                }
                            }
                        }
                    }

                    //pans don't work on subs so use it here
                    if (s.getAutoRndPan()) {
                        s.saveSubPrevPan();
                        randomizePan(s, true);

                        if (llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Pan) {
                            updateDrawable(stepNo);
                        }
                    }

                    //update the drawable if a change occurred in the selected sequence and the playing sequence is selected
                    if (updateDrawables && llppdrums.getDrumMachine().getPlayingSequence() == llppdrums.getDrumMachine().getSelectedSequence()) {
                        updateDrawable(stepNo);
                    }
                }
            }
        }
    }

    public void returnAutoRandomization(int stepNo){
        boolean updateDrawables = false;

        if(drumTrack.returnActive()) {
            Step step = drumTrack.getSteps().get(stepNo);

            if (step.returnActive()) {
                for (int sub = 0; sub < step.getNofSubs(); sub++) {

                    //on/off
                    if (step.getRndOnReturn(sub)) {

                        if(step.getNofSubs() == 1){
                            boolean oldValue = step.isOn();

                            step.setOn(step.getPrevOn());

                            if (oldValue != step.isOn() &&
                                    llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof OnOff) {
                                updateDrawables = true;
                            }
                        }
                        else {
                            boolean oldValue = step.isSubOn(sub);

                            step.setSubOn(sub, step.getSubPrevOn(sub));

                            if (oldValue != step.isSubOn(sub) &&
                                    llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof OnOff) {
                                updateDrawables = true;
                            }
                        }
                    }

                    //vol
                    if (step.getRndVolReturn(sub)) {
                        float oldValue = step.getVolumeModifier(sub);

                        step.setVolumeModifier(step.getSubPrevVol(sub), sub);

                        if (oldValue != step.getVolumeModifier(sub) &&
                                llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Volume) {
                            updateDrawables = true;
                        }
                    }

                    //pitch
                    if (step.getRndPitchReturn(sub)) {
                        float oldValue = step.getPitchModifier(sub);

                        step.setPitchModifier(step.getSubPrevPitch(sub), sub);

                        if (oldValue != step.getPitchModifier(sub) &&
                                llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Pitch) {
                            updateDrawables = true;
                        }
                    }
                }

                //pan
                if (step.getRndPanReturn()) {
                    float oldValue = step.getPan();

                    step.setPan(step.getPrevPan());

                    if (oldValue != step.getPan() &&
                            llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Pan) {
                        updateDrawables = true;
                    }
                }
            }
        }

        //update the drawable if a change occurred in the selected sequence and the playing sequence is selected
        if (updateDrawables && llppdrums.getDrumMachine().getPlayingSequence() == llppdrums.getDrumMachine().getSelectedSequence()) {
            updateDrawable(stepNo);
        }
    }

    private void updateDrawable(final int step){
        if(llppdrums.getDrumMachine().getSelectedSequence().isInBaseMode()) {
            //Log.e("RndTrackManager", "UPDATE, seq pos: "+llppdrums.getEngineFacade().getStep());
            //Log.e("RndTrackManager", "UPDATE step: "+step);
            Drawable drawable = llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule().getDrawable(drumTrack.getTrackNo(), step);
            llppdrums.getSequencerUI().setStepDrawable(drawable, drumTrack.getTrackNo(), step);
        }
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
