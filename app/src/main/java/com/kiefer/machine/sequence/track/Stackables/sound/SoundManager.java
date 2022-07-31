package com.kiefer.machine.sequence.track.Stackables.sound;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.soundSources.SoundManagerKeeper;
import com.kiefer.files.keepers.soundSources.SoundSourceManagerKeeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Stackables.StackableManager;
import com.kiefer.machine.sequence.track.Stackables.Stacker;
import com.kiefer.machine.sequence.track.Stackables.Stackable;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.SoundSourceManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.eventManager.StepEventsManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

import nl.igorski.mwengine.core.ProcessingChain;

public class SoundManager extends StackableManager {
    private int maxNOfSoundSources = 3;

    protected LLPPDRUMS llppdrums;
    protected DrumSequence drumSequence;
    //private FxManagerUser fxManagerUser;
    protected ArrayList<SoundSourceManager> soundSourceManagers;

    //used to create random fx and to poopulate the list. Otherwise we'd have to create actual fxs to get access to the info which seems bad
    protected ArrayList<StackableInfo> stackableInfos;

    protected SoundSourceManager selectedSoundSourceManager;

    //tabManager used by the popup
    //private TabManager tabManager;

    //IMGs
    private final int bgImgId, soundListPopupImgId;

    //rnd
    private final Random random;
    private final int MAX_N_RND_SSMs = 3;

    //public FxManager(LLPPDRUMS llppdrums, FxManagerUser fxManagerUser){
    public SoundManager(LLPPDRUMS llppdrums, DrumSequence drumSequence, Stacker stacker) {
        super(stacker);
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;

        //tabManager = new TabManager(llppdrums);

        random = new Random();

        bgImgId = ImgUtils.getRandomImageId();
        soundListPopupImgId = ImgUtils.getRandomImageId();

        stackableInfos = new ArrayList<>();
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.oscLabel), ContextCompat.getColor(llppdrums, R.color.fxDelayColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.sampleLabel), ContextCompat.getColor(llppdrums, R.color.fxFlangerColor)));

        //int nOfSSs = random.nextInt(maxNOfSoundSources - 1) + 1;
        createRandomStackables(false, maxNOfSoundSources);

        //Log.e("SoundManager", "selectedSoundSourceManager set in constr");
        selectedSoundSourceManager = soundSourceManagers.get(0);
    }

    //has to be in the same order as fxInfos
    private SoundSourceManager getNewSoundSource(boolean randomizeAutomation) {
        return getNewSoundSource(random.nextInt(stackableInfos.size()), randomizeAutomation);
    }

    private SoundSourceManager getNewSoundSource(int n, boolean randomizeAutomation) {
        return new SoundSourceManager(llppdrums, drumSequence, (DrumTrack) stacker, n, randomizeAutomation);
    }

    protected SoundSourceManager createNewSoundSourceManager(int i, boolean randomizeAutomation) {
        SoundSourceManager ssm = getNewSoundSource(i, randomizeAutomation);
        selectedSoundSourceManager = ssm; //created are always selected
        soundSourceManagers.add(ssm);
        return ssm;
    }

    public void createRandomStackables(boolean automation, int n){
        if(soundSourceManagers != null){
            destroy();
        }
        soundSourceManagers = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            createRandomStackable(automation);
        }
    }

    public void setNOfStackables(int n, boolean automation){
        while(soundSourceManagers.size() < n){
            createRandomStackable(automation);
        }
        while(soundSourceManagers.size() > n){
            for(int i = soundSourceManagers.size()-1; i > n - 1; i--){
                SoundSourceManager ssm = soundSourceManagers.remove(i);
                destroySoundSourceManager(ssm);
            }
        }
    }

    public void setNOfStackables(int min, int max, boolean automation){

        int nOfStackables = random.nextInt(max) + min;

        while(soundSourceManagers.size() < nOfStackables){
            createRandomStackable(automation);
        }
        while(soundSourceManagers.size() > nOfStackables){
            for(int i = soundSourceManagers.size()-1; i > nOfStackables - 1; i--){
                SoundSourceManager ssm = soundSourceManagers.remove(i);
                destroySoundSourceManager(ssm);
            }
        }
    }

    public void randomizeSoundSources(float samplePerc){
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.randomizeSoundSource(samplePerc);
        }
    }

    public void setSoundSource(int ssm, String ss){
        if(soundSourceManagers.size() > ssm) {
            soundSourceManagers.get(ssm).setActiveSoundSource(ss);
        }
    }

    public void setPresets(String presetCategory){
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.setPresets(presetCategory);
        }
    }

    public void setPreset(int ssm, String presetCategory){
        //randomizeSoundSources(.7f);

        if(soundSourceManagers.size() > ssm) {
            soundSourceManagers.get(ssm).setPresets(presetCategory);
        }
    }

    public void setRandomPresets(){
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.setRandomPresets();
        }
    }

    public void createRandomStackable(boolean automation) {
        createNewSoundSourceManager(random.nextInt(stackableInfos.size()), automation);
    }

    public void moveStackable(final int from, final int to) {
        SoundSourceManager ssm = soundSourceManagers.remove(from);
        soundSourceManagers.add(to, ssm);
        //rearrangeFxs();
    }
/*
    protected void rearrangeFxs() {
        for (ProcessingChain pc : stacker.getProcessingChains()) {
            if (pc != null) {
                pc.reset();
                for (Fx fx : soundSourceManagers) {
                    if (fx.isOn()) {
                        //don't use addFxToEngine() here since we already loop the pcs to reset them
                        pc.addProcessor(fx.getBaseProcessor());
                    }
                }
            }
        }
    }

 */

    public void removeStackable(int n) {
        SoundSourceManager ssm = soundSourceManagers.remove(n);
        destroySoundSourceManager(ssm);
    }

    public void destroySoundSourceManager(SoundSourceManager ssm) {

        ssm.destroy();
        if (ssm == selectedSoundSourceManager) {
            selectedSoundSourceManager = null;
        }
    }

    private void destroySSMs() {
        if (soundSourceManagers != null) {
            for (SoundSourceManager ssm : soundSourceManagers) {
                destroySoundSourceManager(ssm);
            }
        }
    }

    //use this one to select soundSource instead
    public void changeSelectedStackable(int newStackableIndex) {
        selectedSoundSourceManager.setActiveSoundSource(newStackableIndex);
        //Log.e("asd", "asd"+newStackableIndex);
        /*

        //create the new fx
        SoundSourceManager newSSM = getNewSoundSource(newStackableIndex, false);

        //set on to whatever the old fx had. No need to add the enw to the engine, it will be done in rearrangeFxs()
        newSSM.setOn(selectedSoundSourceManager.isOn());

        //replace them in the array
        soundSourceManagers.set(soundSourceManagers.indexOf(selectedSoundSourceManager), newSSM);

        //destroy the old one
        //selectedFx.destroy();
        destroySoundSourceManager(selectedSoundSourceManager);

        //replace the variable
        selectedSoundSourceManager = newSSM;

        //rearrange to add it in the right position in the engine
        //rearrangeFxs();

         */
    }

    public void turnSelectedStackableOn(Boolean on) {
        turnSoundSourceManagerOn(selectedSoundSourceManager, on);
    }

    public void turnSoundSourceManagerOn(SoundSourceManager ssm, Boolean on) {
        ssm.setOn(on);
        /*
        if (on) {
            rearrangeFxs();
        } else {
            removeFxFromEngine(ssm);
        }

         */

        //turn on/off the indicator
        setIndicator();
    }

    public void randomizeSSColors(){
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.randomizeColors();
        }
    }

    protected void setIndicator() {
        /*
        if (llppdrums.getSequencer() != null) {
            int i = 0;
            for (Fx fx : fxs) {
                if (fx.isOn()) {
                    i++;
                }
            }
            llppdrums.getSequencer().setTrackFxIndicator(drumTrack.getTrackNo(), i > 0);
        }

         */
    }
/*
    public boolean isAnFxOn() {
        for (Fx fx : soundSourceManagers) {
            if (fx.isOn()) {
                return true;
            }
        }
        return false;
    }

 */
/*
    public void releaseFxViews() {
        for (Fx fx : soundSourceManagers) {
            if (fx.getLayout().getParent() != null) {
                ((ViewGroup) fx.getLayout().getParent()).removeView(fx.getLayout());
            }
        }
    }

 */

    public void setSelectedStackable(int index, int sender) {
        //Log.e("SoundManager", "setSelectedStackable(), index: "+index+", sender: "+sender);
        selectedSoundSourceManager = soundSourceManagers.get(index);
    }

    public void setOscillatorVolume(int oscillatorNo, float volume){
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.setOscillatorVolume(oscillatorNo, volume);
        }
    }

    public void setOscillatorPitch(int oscNo, int pitch){
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.setOscillatorPitch(oscNo, pitch);
        }
    }

    public void setOscillatorAttackTime(int oscillatorNo, float time){
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.setOscillatorAttackTime(oscillatorNo, time);
        }
    }

    public void setOscillatorReleaseTime(int oscillatorNo, float volume){
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.setOscillatorReleaseTime(oscillatorNo, volume);
        }
    }

    /**
     * ACTIVATION
     **/
    public void activate() {
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.activate();
        }
        automate(llppdrums.getEngineFacade().getStep(), false);
    }

    public void deactivate() {
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.deactivate();
        }
        resetAutomations();
    }

    /**
     * ADD/REMOVE
     **/
    public void addFxsToEngine() {
        //rearrangeFxs();
    }

    /**
     * SELECTION
     **/
    public void select() {
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.select();
        }
    }

    public void deselect() {
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.deselect();
        }
    }

    /**
     * STEPS
     **/
    public void addStep() {
        for (SoundSourceManager ssm : soundSourceManagers) {
            ssm.addStep();
        }
    }

    public void removeStep() {
        for (SoundSourceManager ssm : soundSourceManagers) {
            ssm.removeStep();
        }
    }

    /**
     * RANDOMIZATION
     **/
    public void randomizeAll(boolean allowZero) {
        destroy();
        soundSourceManagers = new ArrayList<>();

        int nOfSSMs = random.nextInt(MAX_N_RND_SSMs) + 1;

        for (int i = 0; i < nOfSSMs; i++) {
            createRandomStackable(true);
        }

        if (soundSourceManagers.size() > 0) {
            selectedSoundSourceManager = soundSourceManagers.get(0);
        } else {
            setIndicator(); //id < 0 this is calls in createRandomFx()
        }
    }

    /**
     * GET
     **/

    public ArrayList<ProcessingChain> getProcessingChains() {
        ArrayList<ProcessingChain> pcs = new ArrayList<>();
        for(SoundSourceManager ssm : soundSourceManagers){
            pcs.addAll(ssm.getProcessingChains());
            //for(ProcessingChain pc : ssm.getProcessingChains()){
                //pcs.add(pc);
            //}
        }
        return pcs;
    }

    public ArrayList<StepEventsManager> getStepEventManagers(Step step, int subs){
        ArrayList<StepEventsManager> stepEventsManagers = new ArrayList<>();
        for(SoundSourceManager ssm : soundSourceManagers){
            stepEventsManagers.add(ssm.getStepEventManager(step, subs));
        }
        return stepEventsManagers;
    }

    public ArrayList<StackableInfo> getStackableInfos() {
        return stackableInfos;
    }

    public int getBgImgId() {
        return bgImgId;
    }

    public int getNOfSounds(){
        return soundSourceManagers.size();
    }

    public String getName(){
        return "SOUND MANAGER";
    }

    public int getListPopupImgId() {
        return soundListPopupImgId;
    }

    public ArrayList<Stackable> getStackables() {
        ArrayList<Stackable> al = new ArrayList<>();
        for (SoundSourceManager ssm : soundSourceManagers) {
            al.add(ssm);
        }
        return al;
    }

    public ArrayList<String> getPresetCategories(){
        return SoundSourcePreset.getCategories();
    }

    public Stackable getSelectedStackable() {
        return selectedSoundSourceManager;
    }

    public int getSelectedStackableIndex() {
        //Log.e("SoundManager", "getSelectedStackableIndex(), index: "+soundSourceManagers.indexOf(selectedSoundSourceManager));
        return soundSourceManagers.indexOf(selectedSoundSourceManager);
    }

    /** SET **/
    public void setPan(float pan){
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.setPan(pan);
        }
    }

    /** ACTIVATION **/

    /**
     * TRANSPORT
     **/
    public void play() {
        for(SoundSourceManager ssm : soundSourceManagers){
            ssm.play();
        }
    }

    public void stop() {
        for (SoundSourceManager ssm : soundSourceManagers) {
            ssm.stop();
        }
    }

    /**
     * AUTOMATE
     **/
    public void automate(int step, boolean isPopupShowing) {
        for (SoundSourceManager ssm : soundSourceManagers) {
            ssm.automate(step, isPopupShowing);
        }
    }

    public void resetAutomations() {
        for (SoundSourceManager ssm : soundSourceManagers) {
            ssm.resetAutomation();
        }
    }

    /**
     * RESET
     **/
    public void reset() {
        for (SoundSourceManager ssm : soundSourceManagers) {
            destroySoundSourceManager(ssm);
        }
        soundSourceManagers = new ArrayList<>();
        setIndicator();
    }

    /**
     * RESTORATION
     **/
    public void restore(SoundManagerKeeper k) {

        destroy();
        soundSourceManagers = new ArrayList<>(); //fxs add themselves in createNewFx

        for (SoundSourceManagerKeeper ssmk : k.soundSourceManagerKeepers) {
            SoundSourceManager ssm = createNewSoundSourceManager(ssmk.activeSoundSourceIndex, false);
            ssm.restore(ssmk);
            //addFxToEngine(fx);
/*
            if (ssm.isOn()) {
                addFxToEngine(ssm);
            }

 */
        }

        //only set a selected if at least one exists
        //if (k.selectedFxIndex > 0) {
            //setSelectedStackable(k.selectedSSMIndex, 1);
        //}
    }

    public SoundManagerKeeper getKeeper() {
        SoundManagerKeeper keeper = new SoundManagerKeeper();
        keeper.soundSourceManagerKeepers = new ArrayList<>();
        for (SoundSourceManager ssm : soundSourceManagers) {
            keeper.soundSourceManagerKeepers.add(ssm.getKeeper());
        }
        keeper.selectedSSMIndex = soundSourceManagers.indexOf(selectedSoundSourceManager);
        return keeper;
    }

    /**
     * DESTROY
     **/
    public void destroy() {
        destroySSMs();
        soundSourceManagers = null;
    }
}
