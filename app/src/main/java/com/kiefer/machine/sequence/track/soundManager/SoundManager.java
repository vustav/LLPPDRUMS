package com.kiefer.machine.sequence.track.soundManager;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.soundSources.SoundManagerKeeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.track.soundManager.eventManager.StepEventsManager;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;
import com.kiefer.machine.sequence.track.soundManager.sampleManager.SmplManager;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

import nl.igorski.mwengine.core.ProcessingChain;

/** Holds a SampleManager and an OscillatorManager.**/

public class SoundManager {
    public static final String OSC = "osc", SAMPLE = "sample";

    private final LLPPDRUMS llppdrums;
    private final DrumSequence drumSequence;
    private final DrumTrack drumTrack;

    private SoundSource activeSoundSource;

    private ArrayList<SoundSource> soundSources;

    private final int bgImageId;

    public SoundManager(LLPPDRUMS llppdrums, DrumSequence drumSequence, DrumTrack drumTrack){
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.drumTrack = drumTrack;

        bgImageId = ImgUtils.getRandomImageId();

        soundSources = new ArrayList<>();
        OscillatorManager oscillatorManager = new OscillatorManager(llppdrums, drumSequence, drumTrack);
        oscillatorManager.deactivate();
        soundSources.add(oscillatorManager);
        SmplManager smplManager = new SmplManager(llppdrums, drumSequence, drumTrack);
        smplManager.deactivate();
        soundSources.add(smplManager);

        //can't use randomizeSoundSource() here since steps aren't created yet
        Random r = new Random();
        activeSoundSource = soundSources.get(r.nextInt(2));
        activeSoundSource.activate();
    }

    /** ACTIVATION **/
    public void activate(){
        activeSoundSource.activate();
    }

    public void deactivate(){
        //activeSoundSource.deactivate();

        for(SoundSource ss : soundSources){
            ss.deactivate();
        }
    }

    /** SELECTION **/
    public void select(){
        //oscillatorManager.select();
    }

    public void deselect(){
        //oscillatorManager.deselect();
    }

    /** RND **/
    public void randomizeSoundSource(){
        Random r = new Random();
        if(r.nextBoolean()){
            setActiveSoundSource(OSC);
        }
        else{
            setActiveSoundSource(SAMPLE);
        }
    }
    public void randomizeSoundSource(float samplePerc){
        Random r = new Random();
        if(r.nextFloat() > samplePerc){
            setActiveSoundSource(OSC);
        }
        else{
            setActiveSoundSource(SAMPLE);
        }
    }

    /** SET **/
    public void setActiveSoundSource(String tag) {
        if(activeSoundSource != null) {
            activeSoundSource.deactivate();
        }

        if(tag.equals(OSC)){
            activeSoundSource = soundSources.get(0);

            //shouldn't be needed but sometimes both are on so do this just in case
            soundSources.get(1).deactivate();
        }
        else{
            activeSoundSource = soundSources.get(1);
            soundSources.get(0).deactivate();
        }

        if(llppdrums.getDrumMachine().getPlayingSequence() == drumSequence) {
            activeSoundSource.activate();
        }
    }

    //atk
    public void setAttackTime(int oscNo, float time){
        ((OscillatorManager)soundSources.get(0)).setAttackTime(oscNo, time);
    }

    //dec
    public void setReleaseTime(int oscNo, float time){
        ((OscillatorManager)soundSources.get(0)).setReleaseTime(oscNo, time);
    }

    public void setOscillatorPitch(int oscNo, int pitch){
        ((OscillatorManager)soundSources.get(0)).setOscillatorPitch(oscNo, pitch);
    }

    public void setWaveForm(final int oscNo, final int waveForm){
        ((OscillatorManager)soundSources.get(0)).setWaveForm(oscNo, waveForm);
    }

    //pan
    public void setPan(float pan){
        //activeSoundSource.setPan(pan);

        for(SoundSource ss : soundSources){
            ss.setPan(pan);
        }
    }

    public void setOscillatorVolume(final int oscillatorNo, final float volume){
        ((OscillatorManager)soundSources.get(0)).setOscillatorVolume(oscillatorNo, volume);
    }

    /** GET **/

    public OscillatorManager getOscillatorManager(){
        return (OscillatorManager) soundSources.get(0);
    }

    public SmplManager getSmplManager(){
        return (SmplManager) soundSources.get(1);
    }

    public SoundSource getActiveSoundSource() {
        return activeSoundSource;
    }

    public ArrayList<ProcessingChain> getProcessingChains() {
        ArrayList<ProcessingChain> chains = new ArrayList<>();
        for(SoundSource ss : soundSources){
            for(ProcessingChain pc : ss.getProcessingChains()) {
                chains.add(pc);
            }
        }
        return chains;
    }

    public ArrayList<String> getPresetCategories(){
        return SoundSourcePreset.getCategories();
    }

    public void setRandomPresets(){
        for(SoundSource ss : soundSources){
            ss.setRandomPreset();
        }
    }

    /** RndSeqManager calls this with one of the static strings in SoundSourcePreset, so make sure to cover them and add anything extra class-specific **/
    public void setPresets(String s){
        //Log.e("SoundManager", "setPreset(): "+s);
        //activeSoundSource.setPreset(s);
        setOscPreset(s);
        setSmplPreset(s);
    }

    public void setOscPreset(String s){
        getOscillatorManager().setPreset(s);
    }

    public void setSmplPreset(String s){
        getSmplManager().setPreset(s);
    }

/*
    public SoundEvents getSoundEvents(int nOfSteps, int subs, int step, boolean add){
        return activeSoundSource.getSoundEvents(nOfSteps, subs, step, add);
    }

 */

    public StepEventsManager getStepEventManager(Step step, int subs){
        return new StepEventsManager(llppdrums, drumSequence, drumTrack, this, step, subs);
    }

    /** EVENTS **/
    /*
    public void positionEvents(int nOfSteps, int step){
        for(SoundSource ss : soundSources){
            ss.positionEvents(nOfSteps, step);
        }
    }

    public void randomizePitch(boolean autoRnd, int sub){
        for(SoundSource ss : soundSources){
            ss.randomizePitch(autoRnd, sub);
        }
    }

     */

    /** SUBS **/
    public void updateSubs(int subs){
        getOscillatorManager().updateSubs(subs);
    }

    /** PLAY **/
    public void play(){
        activeSoundSource.playDrum();
    }

    /** RND **/
    public void randomizeAll(){
        activeSoundSource.randomizeAll();
    }

    /** GFX **/

    public int getBgImageId() {
        return bgImageId;
    }

    /** RESTORATION **/
    public void restore(SoundManagerKeeper k){
        activeSoundSource = soundSources.get(k.activeSoundSourceIndex);
        for(int i = 0; i<k.ssKeepers.size(); i++){
            soundSources.get(i).restore(k.ssKeepers.get(i));
        }
        drumTrack.updateEventSamples();
    }

    public SoundManagerKeeper getKeeper(){
        SoundManagerKeeper smk = new SoundManagerKeeper();

        smk.activeSoundSourceIndex = soundSources.indexOf(activeSoundSource);

        smk.ssKeepers = new ArrayList<>();
        for(SoundSource ss : soundSources){
            smk.ssKeepers.add(ss.getKeeper());
        }

        return smk;
    }

    /** DESTRUCTION **/
    public void destroy(){
        for(SoundSource ss : soundSources){
            ss.destroy();
        }
    }
}
