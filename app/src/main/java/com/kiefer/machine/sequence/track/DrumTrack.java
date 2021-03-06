package com.kiefer.machine.sequence.track;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.DrumTrackAutoBtnGraphics;
import com.kiefer.graphics.DrumTrackFxBtnGraphics;
import com.kiefer.graphics.DrumTrackMixBtnGraphics;
import com.kiefer.files.keepers.DrumTrackKeeper;
import com.kiefer.interfaces.Subilizer;
import com.kiefer.machine.DrumMachine;
import com.kiefer.machine.sequence.track.Stackables.fx.Fxer;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.track.Stackables.fx.FxManager;
import com.kiefer.machine.sequence.track.Stackables.sound.SoundManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.SoundSourceManager;
import com.kiefer.popups.stackableManager.StackableManagerPopup;
import com.kiefer.popups.nameColor.NamerColorizer;
import com.kiefer.randomization.rndTrackManager.RndTrackManager;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

import nl.igorski.mwengine.core.ProcessingChain;

public class DrumTrack implements Subilizer, NamerColorizer, Fxer {
    //ACCESSED
    private final LLPPDRUMS llppdrums;
    private final DrumSequence drumSequence;

    //OWNED
    //private final OscillatorManager oscillatorManager;
    //private final SoundSourceManager soundSourceManager;
    private SoundManager soundManager;
    private final FxManager fxManager;

    private final RndTrackManager rndTrackManager;
    private final Random random;

    private String name;
    private int color;

    //track-wide params
    private float trackVolume;

    //data
    private ArrayList<Step> steps;
    private final ArrayList<Boolean> stepManagerSubValues;

    //private int initNOfSteps;
    private int nOfSubs;

    //graphics
    private final int mixerPopupBgId, namePopupBgId;
    private final LinearLayout autoBtnGraphics, fxBtnGraphics, mixerBtnGraphics;

    public DrumTrack(LLPPDRUMS llppdrums, DrumSequence drumSequence, int steps, DrumTrackKeeper keeper) {
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;

        random = new Random();

        if(keeper == null) {
            Random r = new Random();
            nOfSubs = r.nextInt(llppdrums.getResources().getInteger(R.integer.maxNOfSubs)) + 1;
        }
        else{
            nOfSubs = keeper.nOfSubs;
        }

        randomizeName();
        color = ColorUtils.getRandomColor();

        stepManagerSubValues = new ArrayList<>();
        for(int i = 0; i<nOfSubs; i++){
            if(i == 0){
                stepManagerSubValues.add(true);
            }
            else{
                stepManagerSubValues.add(false);
            }
        }

        autoBtnGraphics = new DrumTrackAutoBtnGraphics(llppdrums).getLayout();
        fxBtnGraphics = new DrumTrackFxBtnGraphics(llppdrums).getLayout();
        mixerBtnGraphics = new DrumTrackMixBtnGraphics(llppdrums).getLayout();
        mixerPopupBgId = ImgUtils.getRandomImageId();
        namePopupBgId = ImgUtils.getRandomImageId();


        //generate an id, create a new track, an oscillatorManager and an fxManager
        //soundSourceManager = new SoundSourceManager(llppdrums, drumSequence, this);
        soundManager = new SoundManager(llppdrums, drumSequence, this);
        fxManager = new FxManager(llppdrums, this);
        setupSteps(steps);

        rndTrackManager = new RndTrackManager(llppdrums, this);


        float maxVol = (float)llppdrums.getResources().getInteger(R.integer.maxVol);
        randomizeVol(maxVol / 2, maxVol - maxVol / 10);
        //setTrackVolume(1);
    }

    private void setupSteps(int steps){
        this.steps = new ArrayList<>();

        for(int step = 0; step < steps; step++){
            addStep();
        }
    }

    public Step addStep(){
        return addStep(random.nextInt(2) == 1, nOfSubs);
    }

    public Step addStep(boolean on, int subs){
        //Step step = new Step(llppdrums, this, soundSourceManager, subs, on);
        Step step = new Step(llppdrums, this, subs, on);
        steps.add(step);
        fxManager.addStep();
        return step;
    }

    public Step addStep(int position, boolean on){
        //Log.e("DrumTrack", "addStep()");
        Step step = new Step(llppdrums, this, nOfSubs, on);
        steps.add(position, step);
        fxManager.addStep();
        return step;
    }

    public void removeStep(){
        Step step = steps.remove(steps.size()-1);
        fxManager.removeStep();
        step.destroy();
    }

    public void positionEvents(){
        //Log.e("DrumTrack", "positionEvents(), nOfSteps: "+getNOfSteps());
        for(Step d : steps){
            d.positionEvents(getNOfSteps(), false);
        }
    }

    @Override
    public void setNOfSubs(int track, int nOfSubs){
        this.nOfSubs = nOfSubs;
        for(Step s : steps){
            s.setNOfSubs(nOfSubs);
        }
        //positionEvents();

        if(llppdrums.getDrumMachine() != null) {
            if (llppdrums.getDrumMachine().getSelectedSequence() == drumSequence) {
                llppdrums.getSequencerUI().notifyDataSetChange();
            }
        }

        //update stepManagerValues
        while(stepManagerSubValues.size() < nOfSubs){
            stepManagerSubValues.add(false);
        }
        while(stepManagerSubValues.size() > nOfSubs){
            stepManagerSubValues.remove(stepManagerSubValues.size()-1);
        }
    }

    /** RANDOMIZATION **/

    public void randomizeVol(){
        float maxVol = (float)llppdrums.getResources().getInteger(R.integer.maxVol);
        randomizeVol(maxVol / 3, maxVol);
    }

    public void randomizeVol(float min, float max){
        float vol = getRandomMultiplier(min, max);
        setTrackVolume(vol);
    }

    public void randomizeName(){
        name = Integer.toString(random.nextInt(100));
    }

    private float getRandomMultiplier(float min, float max){
        float diff = max - min;
        return random.nextFloat() * diff + min;
    }

    /** ACTIVATION **/
    public void activate(){
        soundManager.activate();
        fxManager.activate();
    }

    public void deactivate(){
        soundManager.deactivate();
        fxManager.deactivate();
    }

    /** TRANSPORT **/
    public void play(){

    }

    public void stop(){
        fxManager.stop();
    }

    /** SELECTION **/
    public void select(){
        fxManager.select();
        soundManager.select();
    }
    public void deselect(){
        fxManager.deselect();
        soundManager.deselect();
    }

    /** AUTO **/
    //used by autoStepPopup
    public void setStepOn(int step, boolean on){
        steps.get(step).setOn(on);
    }
    public boolean isStepOn(int step){
        return steps.get(step).isOn();
    }
    public void setSubOn(int step, int sub, boolean on){
        steps.get(step).setSubOn(sub, on);
    }
    public boolean getSubOn(int step, int sub){
        return steps.get(step).isSubOn(sub);
    }

    public void setStepOn(int step, boolean on, ArrayList<Boolean> autoStepValues){
        for(int i = 0; i<autoStepValues.size(); i++){
            steps.get(step).setSubOn(i, autoStepValues.get(i));
        }
        steps.get(step).setOn(on);
    }

    public void pushLeft(int interval){
        //remove the first drum and add a new to the end
        steps.remove(0).destroy();
        Step step = addStep(false, nOfSubs);

        //set subs to stepManagerSubValues
        for(int i = 0; i < stepManagerSubValues.size(); i++){
            step.setSubOn(i, stepManagerSubValues.get(i));
        }

        switch (interval) {
            case 1:
                step.setOn(true);

                break;

            case 2:
                //if the second to last is off, turn on the last
                if(!(steps.get(steps.size() - 2).isOn())) {
                    step.setOn(true);
                }
                break;

            case 3:
                //if the second and third to last is off, turn on the last
                if(!(steps.get(steps.size() - 2).isOn()) &&
                        !(steps.get(steps.size() - 3).isOn())) {
                    step.setOn(true);
                }
                break;

            case 4:
                //etc...
                if(!(steps.get(steps.size() - 2).isOn()) &&
                        !(steps.get(steps.size() - 3).isOn()) &&
                        !(steps.get(steps.size() - 4).isOn())) {
                    step.setOn(true);
                }
                break;

            case 5:
                if(!(steps.get(steps.size() - 2).isOn()) &&
                        !(steps.get(steps.size() - 3).isOn()) &&
                        !(steps.get(steps.size() - 4).isOn()) &&
                        !(steps.get(steps.size() - 5).isOn())) {
                    step.setOn(true);
                }
                break;

            case 6:
                if(!(steps.get(steps.size() - 2).isOn()) &&
                        !(steps.get(steps.size() - 3).isOn()) &&
                        !(steps.get(steps.size() - 4).isOn()) &&
                        !(steps.get(steps.size() - 5).isOn()) &&
                        !(steps.get(steps.size() - 6).isOn())) {
                    step.setOn(true);
                }
                break;

            case 7:
                if(!(steps.get(steps.size() - 2).isOn()) &&
                        !(steps.get(steps.size() - 3).isOn()) &&
                        !(steps.get(steps.size() - 4).isOn()) &&
                        !(steps.get(steps.size() - 5).isOn()) &&
                        !(steps.get(steps.size() - 6).isOn()) &&
                        !(steps.get(steps.size() - 7).isOn())) {
                    step.setOn(true);
                }
                break;

            //randomize of no pattern is selected
            default:
                step.setOn(random.nextInt(2) == 1);
                break;
        }

        //we need to reactivate here since the drums hasn't moved their events, only moved in the list in DrumTrack
        //reactivate();
        positionEvents();
    }

    public void pushRight(int interval){
        steps.remove(steps.size() - 1).destroy();
        Step step = addStep(0, false); //when adding a drum at a position we don't need to update the drums positions since its done there

        //set subs to stepManagerSubValues
        for(int i = 0; i < stepManagerSubValues.size(); i++){
            step.setSubOn(i, stepManagerSubValues.get(i));
        }

        switch (interval) {
            case 1:
                step.setOn(true);
                break;

            case 2:
                if(!(steps.get(1).isOn())) {
                    step.setOn(true);
                }
                break;

            case 3:
                if(!(steps.get(1).isOn()) &&
                        !(steps.get(2).isOn())) {
                    step.setOn(true);
                }
                break;

            case 4:
                if(!(steps.get(1).isOn()) &&
                        !(steps.get(2).isOn()) &&
                        !(steps.get(3).isOn())) {
                    step.setOn(true);
                }
                break;

            case 5:
                if(!(steps.get(1).isOn()) &&
                        !(steps.get(2).isOn()) &&
                        !(steps.get(3).isOn()) &&
                        !(steps.get(4).isOn())) {
                    step.setOn(true);
                }
                break;

            case 6:
                if(!(steps.get(1).isOn()) &&
                        !(steps.get(2).isOn()) &&
                        !(steps.get(3).isOn()) &&
                        !(steps.get(4).isOn()) &&
                        !(steps.get(5).isOn())) {
                    step.setOn(true);
                }
                break;

            case 7:
                if(!(steps.get(1).isOn()) &&
                        !(steps.get(2).isOn()) &&
                        !(steps.get(3).isOn()) &&
                        !(steps.get(4).isOn()) &&
                        !(steps.get(5).isOn()) &&
                        !(steps.get(6).isOn())) {
                    step.setOn(true);
                }
                break;

            //randomize of no pattern is selected
            default:
                step.setOn(random.nextInt(2) == 1);
                break;
        }

        //tell the engine about the shuffle
        //engineFacade.rearrangeDrums(trackId, drums);
        //reactivate();
        positionEvents();
    }

    public void setSubVolume(int step, float volume, int sub){
        steps.get(step).setVolumeModifier(volume, sub);
        //engineFacade.setStepVolume(drums.get(step));
    }

    public void setSubPitchModifier(int step, float pitch, int sub){
        steps.get(step).setPitchModifier(pitch, sub);
    }

    public void setStepPan(int step, float pan){
        steps.get(step).setPan(pan);
    }

    /** DRAWABLES **/
    public void updateDrawables(){
        if(drumSequence == llppdrums.getDrumMachine().getSelectedSequence()) {
            for (int step = 0; step < steps.size(); step++) {
                //try {
                final ImageView iv = llppdrums.getSequencerUI().getStepIV(getTrackNo(), step);
                final Drawable drawable = llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule().getDrawable(getTrackNo(), step);
                if (iv != null) {
                    iv.setImageDrawable(drawable);
                }
                //}
                //catch (Exception e){
                //Log.e("rack.updateDrawables()", Objects.requireNonNull(e.getMessage()));
                //}
            }

            /** VERKAR H??NGA SIG EN DEL H??R MED TR??DEN P?? **/
            /*
            new Thread(new Runnable() {
                public void run() {

                    for (int step = 0; step < drums.size(); step++) {
                        try {
                            final ImageView iv = llppdrums.getSequencer().getStepIV(getTrackNo(), step);
                            final Drawable drawable = llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule().getDrawable(getTrackNo(), step);
                            if (iv != null) {
                                iv.post(new Runnable() {
                                    public void run() {
                                        iv.setImageDrawable(drawable);
                                    }
                                });
                            }

                        }
                        catch (Exception e){
                            //f??r "Attempt to invoke virtual method 'android.view.ViewParent android.view.View.getParent()' on a null object reference" i SequencerUI.getStep() ibland
                            Log.e("rack.updateDrawables()", "//f??r \"Attempt to invoke virtual method 'android.view.ViewParent android.view.View.getParent()' on a null object reference\" i SequencerUI.getStep() ibland");
                        }
                    }
                }
            }).start();
             */
        }
    }

    /** SET **/

    //vol
    public void setTrackVolume(float vol){
        trackVolume = vol;

        //Log.e("DrumTrack", "setTrackVolume(): "+(trackVolume));
        updateDrumVolumes();
        //oscillatorManager.setTrackVolume(vol);
    }

    public void setOscillatorVolume(final int oscillatorNo, final float volume){
        soundManager.setOscillatorVolume(oscillatorNo, volume);
        updateDrumVolumes();
    }

    public void updateDrumVolumes(){
        for (Step d : steps){
            d.updateEventVolumes();
        }
    }

    public void updateEventSamples(){
        if(steps != null) {
            for (Step d : steps) {
                d.updateEventSamples();
            }
        }
    }

    //pitch
    public void setOscillatorPitch(int oscNo, int pitch){
        soundManager.setOscillatorPitch(oscNo, pitch);
        for (Step d : steps){
            d.updateEventPitches();
        }
    }

    //atk
    public void setAttackTime(int oscNo, float time){
        soundManager.setOscillatorAttackTime(oscNo, time);
    }

    //dec
    public void setReleaseTime(int oscNo, float time){
        soundManager.setOscillatorReleaseTime(oscNo, time);
    }

    public void setRndFx(boolean rndFx) {
        rndTrackManager.setRndFx(rndFx);
    }

    public void setRndMix(boolean rndMix) {
        rndTrackManager.setRndMix(rndMix);
    }

    public void setRndOsc(boolean rndOsc) {
        rndTrackManager.setRndOsc(rndOsc);
    }

    public void setRndOn(boolean rndOn) {
        rndTrackManager.setRndOn(rndOn);
    }

    public void setRndSubs(boolean rndSubs) {
        rndTrackManager.setRndSubs(rndSubs);
    }

    public void setRndVol(boolean rndVol) {
        rndTrackManager.setRndVol(rndVol);
    }

    public void setRndPitch(boolean rndPitch) {
        rndTrackManager.setRndPitch(rndPitch);
    }

    public void setRndPan(boolean rndPan) {
        rndTrackManager.setRndPan(rndPan);
    }

    public void setName(String name){
        this.name = name;
        llppdrums.getSequencerUI().setTrackName(getTrackNo(), name);
    }

    public void setColor(int color){
        this.color = color;
        llppdrums.getSequencerUI().setTrackColor(getTrackNo(), color);
    }

    public void setOrganizeStepsSubValue(int sub, boolean on){
        stepManagerSubValues.set(sub, on);
    }

    /** GET **/
    public ArrayList<Boolean> getStepManagerSubSubValues(){
        return stepManagerSubValues;
    }



    public DrumSequence getDrumSequence() {
        return drumSequence;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public boolean getRndFx() {
        return rndTrackManager.getRndFx();
    }

    public boolean getRndMix() {
        return rndTrackManager.getRndMix();
    }

    public boolean getRndOsc() {
        return rndTrackManager.getRndOsc();
    }

    public boolean getRndOn() {
        return rndTrackManager.getRndOn();
    }

    public boolean getRndSubs() {
        return rndTrackManager.getRndSubs();
    }

    public boolean getRndVol() {
        return rndTrackManager.getRndVol();
    }

    public boolean getRndPitch() {
        return rndTrackManager.getRndPitch();
    }

    public boolean getRndPan() {
        return rndTrackManager.getRndPan();
    }

    public float getTrackVolume(){
        return trackVolume;
    }
/*
    public float getVol(){
        return vol;
    }

 */

    public void setSolo(){

    }

    public void setMute(){

    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public int getTrackNo(){
        return drumSequence.getTracks().indexOf(this);
    }

    public FxManager getFxManager() {
        return fxManager;
    }

    public String getPopupLabel(){
        return "TRACK "+getTrackNo();
    }

    //DATA

    public int getNOfSteps(){
        //if this step isn't in the array yet this is called on creation (before it's been added) which means this is the last step
        //if(!drumTrack.getSteps().contains(this)){
        //return drumTrack.getSteps().size();
        //}
        //Log.e("DrumTrack", "getNOfSteps(), steps: "+steps.size());

        return steps.size();
        //return initNOfSteps;
    }

    public ArrayList<ProcessingChain> getProcessingChains(){
        //return ((SoundSourceManager)soundManager.getSelectedStackable()).getProcessingChains();
        return soundManager.getProcessingChains();
    }

    @Override
    public int getNOfSubs(){
        return nOfSubs;
    }

    public int getMixerPopupBgId() {
        return mixerPopupBgId;
    }

    public int getNamePopupBgId() {
        return namePopupBgId;
    }

    public RndTrackManager getRndTrackManager() {
        return rndTrackManager;
    }

    //graphics
    public LinearLayout getAutoBtnGraphics() {
        return autoBtnGraphics;
    }

    public LinearLayout getFxBtnGraphics() {
        return fxBtnGraphics;
    }

    public LinearLayout getMixerBtnGraphics() {
        return mixerBtnGraphics;
    }

    /** STATS **/
/*
    public int getNOfEvents(){
        int n = 0;
        for(Drum d : drums){
            n += d.getNOfEvents();
        }
        return n;
    }

    public int getNOfSequencedEvents(){
        int n = 0;
        for(Drum d : drums){
            n += d.getNOfSequencedEvents();
        }
        return n;
    }

 */

    /** RETURN MEMORY **/
    //keep track if automations are going to be returned, otherwise don't do unnecessary loops

    int returns = 0;
    public void returnModified(boolean returnValue){
        drumSequence.returnModified(returnValue);
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
    //keep track if automations are going to be returned, otherwise don't do unnecessary loops

    int automations = 0;
    public void automationsModified(boolean automationValue){
        drumSequence.automationsModified(automationValue);
        if(automationValue){
            automations++;
        }
        else{
            automations--;
        }
    }

    public boolean automationsActive() {
        return automations > 0;
    }

    /** SEQUENCER UPDATES **/
    public void handleSequencerPositionChange(int sequencerPosition){
        if(soundManager.getNOfSounds() > 0) {
            if (soundManager.getNOfSounds() > 0) {
                fxManager.automate(sequencerPosition, fxManagerPopup != null);
            }

            if (llppdrums.getDrumMachine().getPlayingSequence() == drumSequence) {
                steps.get(sequencerPosition).handleSequencerPositionChange(sequencerPosition);
            }
        }
    }

    /** FX POPUP **/
    private StackableManagerPopup fxManagerPopup, soundManagerPopup;
    @Override
    public void setStackableManagerPopup(StackableManagerPopup stackableManagerPopup, int type){
        if(type == DrumMachine.FX_TYPE) {
            fxManagerPopup = stackableManagerPopup;
        }
        else if(type == DrumMachine.SOUND_TYPE){
            soundManagerPopup = stackableManagerPopup;
        }
    }

    public StackableManagerPopup getStackableManagerPopup(int type){
        if(type == DrumMachine.FX_TYPE) {
            return fxManagerPopup;
        }
        else if(type == DrumMachine.SOUND_TYPE){
            return soundManagerPopup;
        }
        return null;
    }

    /** RESET **/
    public void reset(boolean updateDrawables){
        for (int sub = 0; sub < getNOfSubs(); sub++) {
            llppdrums.getDrumMachine().getSelectedSequence().getSequenceModules().get(SequenceModule.VOL).setAutoValueBase(this, ".8", sub);
            llppdrums.getDrumMachine().getSelectedSequence().getSequenceModules().get(SequenceModule.PITCH).setAutoValueBase(this, "0", sub);
            llppdrums.getDrumMachine().getSelectedSequence().getSequenceModules().get(SequenceModule.PAN).setAutoValueBase(this, "0", sub);
        }

        //use reset instead of autoStep since we need to reset autoValues
        for(Step d : steps){
            d.reset();
        }
        fxManager.reset();

        //only update if selected!
        if(updateDrawables && drumSequence == llppdrums.getDrumMachine().getSelectedSequence()) {
            updateDrawables();
        }
    }

    /** RESTORATION **/
    public void restore(DrumTrackKeeper k){
        //Log.e("DrumTrack.restore()", "nOfSubs: "+k.nOfSubs);
        color = k.color;
        //nOfSubs = k.nOfSubs;
        name = k.name;

        setTrackVolume(k.volume);
        setNOfSubs(666, k.nOfSubs); //trackNo doesn't matter
        soundManager.restore(k.soundManagerKeeper);
        fxManager.restore(k.fxManagerKeeper);
        for(int step = 0; step < steps.size(); step++){
            steps.get(step).restore(k.stepKeepers.get(step));
        }
        positionEvents();
    }

    public DrumTrackKeeper getKeeper(){
        DrumTrackKeeper keeper = new DrumTrackKeeper();
        keeper.color = getColor();
        keeper.nOfSubs = getNOfSubs();
        keeper.name = getName();
        keeper.volume = getTrackVolume();
        keeper.soundManagerKeeper = soundManager.getKeeper();
        keeper.fxManagerKeeper = fxManager.getKeeper();
        keeper.stepKeepers = new ArrayList<>();
        for(Step d : steps){
            keeper.stepKeepers.add(d.getKeeper());
        }
        positionEvents();
        return keeper;
    }

    /** DESTRUCTION **/
    public void destroy(){
        fxManager.destroy();
        soundManager.destroy();

        for(Step d : steps){
            d.destroy();
        }
        steps = null;

    }
}
