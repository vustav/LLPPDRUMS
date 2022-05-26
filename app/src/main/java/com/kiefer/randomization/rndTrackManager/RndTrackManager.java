package com.kiefer.randomization.rndTrackManager;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
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
    private boolean rndOsc, rndOn, rndSubs, rndVol, rndPitch, rndPan, rndFx, rndMix;

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
        rndSubs = false;
        rndVol = random.nextInt(2) == 0;
        rndPitch = random.nextInt(2) == 0;
        rndPan = random.nextInt(2) == 0;
        rndFx = random.nextInt(2) == 0;
        rndMix = random.nextInt(2) == 0;
    }

    public void randomizeSubOn(Step d, int sub) {
        d.randomizeSubOn(sub);
    }

    public void randomizeSubsOn(Step d) {
        d.randomizeSubsOn();
    }

    public void randomizeSubVolume(Step d, int sub) {
        d.randomizeVol(sub);
    }

    public void randomizeSubVolumes(Step d) {
        d.randomizeVols();
    }

    public void randomizeSubPitch(Step d, int sub) {
        d.randomizePitch(sub);
    }

    public void randomizeSubPitches(Step d) {
        d.randomizePitches();
    }

    public void randomizePan(Step d) {
        d.randomizePan();
    }

    public void randomizeModule(SequenceModule seqModule){
        seqModule.randomize(drumTrack);
    }

    public static final String RANDOM = "RANDOM";

    public void randomize(RndSeqPresetTrack rndTrack){
        Random r = new Random();

        //drumTrack.reset(false);

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
                    drumTrack.setSubOn(stepNo, sub, true);
                    drumTrack.setSubVolume(stepNo, step.getSubVol(sub), sub);
                    drumTrack.setSubPitchModifier(stepNo, step.getSubPitch(sub), sub);
                    aSubIsOn = true;
                }
                else{
                    drumTrack.setSubOn(stepNo, sub, false);
                }

                if(rndTrack.getAutoRnd()) {
                    drumTrack.getSteps().get(stepNo).setRndOnPerc(.1f, sub);
                    drumTrack.getSteps().get(stepNo).setRndOnReturn(true, sub);
                }
            }

            drumTrack.setStepOn(stepNo, aSubIsOn); //turn off the step if no subs are on

            if (rndTrack.getRandomizePan()){
                drumTrack.setStepPan(stepNo, step.getPan());
            }
        }

        if(rndTrack.getRandomizeFx()) {
            drumTrack.getFxManager().randomizeAll(true);
        }
        else{
            //börjar med reset så behövs inte
            //drumTrack.getFxManager().reset();
        }

        if (rndTrack.getRandomizeVol()){
            drumTrack.randomizeVol();
        }

        if(llppdrums.getDrumMachine().getSelectedSequence() == drumTrack.getDrumSequence()) {
            drumTrack.updateDrawables();
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
                    boolean aSubIsOn = false;

                    for (int sub = 0; sub < drumTrack.getNOfSubs(); sub++) {

                        //start by randomizing on/off
                        if (s.getAutoRndOn(sub)) {

                            //start by turning the step on if one sub is set for autoRandomization
                            //drumTrack.getSteps().get(stepNo).setOn(true);

                            //if there's only one sub, randomize the step
                            if (s.getNofSubs() == 1) {
                                //aSubIsOn = s.isOn();
                                //Log.e("RndTrackManager", "auto one sub");

                                //keep the former state to know if any updates are necessary
                                //boolean wasOn = s.isOn();

                                s.savePrevOn();

                                float r = random.nextFloat();
                                //Log.e("RndTRackManager", "autoRnd(): r: "+r);
                                if(r < s.getRndOnPerc(sub)) {
                                    //Log.e("RndTRackManager", "autoRnd(): s.getRndOnPerc(sub): "+s.getRndOnPerc(sub));
                                    randomizeStepOn(stepNo);
                                }
                                aSubIsOn = s.isOn();

                                //drawables should be updated if a change has occurred
                                if (s.isOn() != s.getPrevOn()) {
                                    //we should always update colors here so don't only do this for OnOff
                                    updateDrawables = true;
                                }
                            }
                            //otherwise randomize the sub
                            else {
                                //aSubIsOn = s.isSubOn(sub);
                                //boolean wasOn = s.isSubOn(sub);

                                s.saveSubPrevOn(sub);

                                if(random.nextFloat() < s.getRndOnPerc(sub)) {
                                    randomizeSubOn(s, sub);
                                    //Log.e("RndTRackManager", "rndSub: "+s.ges);
                                }
                                //if(s.isSubOn(sub)){
                                    //aSubIsOn = true;
                                //}
                                aSubIsOn = s.isSubOn(sub);

                                //if(s.isSubOn(sub))xcbsdfgsdfg

                                if(s.isSubOn(sub) != s.getSubPrevOn(sub)){
                                    //we should always update colors here so don't only do this for OnOff
                                    updateDrawables = true;
                                }
                            }
                        }

                        /** BORDE FIXA SÅ RANDOMIZE RETURNERAR BOOLEAN SOM ÄR TRUE OM FÖRÄNDRING ÄGT RUM OCH BARA UPPDATERAR DRAWABLES OM SÅ ÄR FALLET **/

                        //if its on after that, randomize the other values
                        if (s.isOn() && s.isSubOn(sub)) {
                            if (s.getAutoRndVol(sub)) {
                                s.saveSubPrevVol(sub);

                                if(random.nextFloat() < s.getRndVolPerc(sub)) {
                                    randomizeSubVolume(s, sub);
                                }

                                if (llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Volume) {
                                    updateDrawables = true;
                                }
                            }
                            if (s.getAutoRndPitch(sub)) {
                                s.saveSubPrevPitch(sub);

                                if(random.nextFloat() < s.getRndVolPerc(sub)) {
                                    randomizeSubPitch(s, sub);
                                }

                                if (llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Pitch) {
                                    updateDrawables = true;
                                }
                            }
                        }
                    }

                    //pans don't work on subs so use it here
                    if (s.getAutoRndPan()) {
                        s.saveSubPrevPan();

                        if(random.nextFloat() < s.getRndPanPerc()) {
                            randomizePan(s);
                        }

                        if (llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof Pan) {
                            updateDrawable(stepNo);
                        }
                    }

                    drumTrack.setStepOn(stepNo, aSubIsOn); //turn off the step if no subs are on

                    //update the drawable if a change occurred in the selected sequence and the playing sequence is selected
                    if (updateDrawables && llppdrums.getDrumMachine().getPlayingSequence() == llppdrums.getDrumMachine().getSelectedSequence()) {
                        updateDrawable(stepNo);
                    }
                }
            }
        }
    }

    public void returnAutoRandomization(int stepNo){
        if(drumTrack.automationsActive() && drumTrack.returnActive()) {

            boolean updateDrawables = false;

            Step step = drumTrack.getSteps().get(stepNo);

            if (step.returnActive()) {
                boolean aSubIsOn = false;
                for (int sub = 0; sub < step.getNofSubs(); sub++) {

                    //on/off
                    if (step.getRndOnReturn(sub)) {

                        if(step.getNofSubs() == 1){
                            boolean oldValue = step.isOn();

                            step.setOn(step.getPrevOn());
                            aSubIsOn = step.isOn();

                            if (oldValue != step.isOn() &&
                                    llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule() instanceof OnOff) {
                                updateDrawables = true;
                            }
                        }
                        else {
                            boolean oldValue = step.isSubOn(sub);

                            step.setSubOn(sub, step.getSubPrevOn(sub));
                            aSubIsOn = step.isSubOn(sub);

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
                drumTrack.setStepOn(stepNo, aSubIsOn); //turn off the step if no subs are on

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

            //update the drawable if a change occurred in the selected sequence and the playing sequence is selected
            if (updateDrawables && llppdrums.getDrumMachine().getPlayingSequence() == llppdrums.getDrumMachine().getSelectedSequence()) {
                updateDrawable(stepNo);
            }
        }
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
        s.randomizeSubsOn();
    }

    public void randomizeStepVols(){
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            final Step d = drumTrack.getSteps().get(step);

            //randomize
            randomizeSubVolumes(d);
        }
    }

    public void randomizeSubs(){
        int nOfSubs = random.nextInt(llppdrums.getResources().getInteger(R.integer.maxNOfSubs)) + 1;
        drumTrack.setNOfSubs(0, nOfSubs); //track is irrelevant here
    }

    public void randomizeStepPitches(){
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            final Step d = drumTrack.getSteps().get(step);

            //randomize
            randomizeSubPitches(d);
        }
    }

    public void randomizeStepPans(){
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            final Step d = drumTrack.getSteps().get(step);

            //randomize
            randomizePan(d);
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

    public void setRndSubs(boolean rndSubs) {
        this.rndSubs = rndSubs;
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

    public boolean getRndSubs() {
        return rndSubs;
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
