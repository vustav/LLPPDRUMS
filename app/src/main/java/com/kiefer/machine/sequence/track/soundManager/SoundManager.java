package com.kiefer.machine.sequence.track.soundManager;

import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.soundSources.SoundManagerKeeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.soundManager.events.SoundEvents;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;
import com.kiefer.machine.sequence.track.soundManager.sampleManager.SmplManager;

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

    public SoundManager(LLPPDRUMS llppdrums, DrumSequence drumSequence, DrumTrack drumTrack){
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.drumTrack = drumTrack;

        soundSources = new ArrayList<>();
        OscillatorManager oscillatorManager = new OscillatorManager(llppdrums, drumSequence, drumTrack);
        oscillatorManager.deactivate();
        soundSources.add(oscillatorManager);
        SmplManager smplManager = new SmplManager(llppdrums, drumSequence, drumTrack);
        smplManager.deactivate();
        soundSources.add(smplManager);

        //can't uns randomizeSoundSource() here since steps aren't created yet
        Random r = new Random();
        activeSoundSource = soundSources.get(r.nextInt(2));
        activeSoundSource.activate();
    }

    /** ACTIVATION **/
    public void activate(){
        activeSoundSource.activate();
    }

    public void deactivate(){
        activeSoundSource.deactivate();
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
        if(r.nextInt(2) == 0){
            setActiveSoundSource(OSC);
        }
        else{
            setActiveSoundSource(SAMPLE);
        }
    }

    /** SET **/
    public void setActiveSoundSource(String tag) {
        /** KRASCHAR HÄR **/
        if(tag.equals(OSC)){
            if(activeSoundSource != null) {
                activeSoundSource.deactivate();
            }
            activeSoundSource = soundSources.get(0);

            if(llppdrums.getDrumMachine().getPlayingSequence() == drumSequence) {
                activeSoundSource.activate();
            }
        }
        else{
            if(activeSoundSource != null) {
                activeSoundSource.deactivate();
            }
            activeSoundSource = soundSources.get(1);

            if(llppdrums.getDrumMachine().getPlayingSequence() == drumSequence) {
                activeSoundSource.activate();
            }
        }
        /**************************************/
        drumTrack.recreateEvents();
    }

    //atk
    public void setAttackTime(int oscNo, float time){
        ((OscillatorManager)soundSources.get(0)).setAttackTime(oscNo, time);
    }

    //dec
    public void setDecayTime(int oscNo, float time){
        ((OscillatorManager)soundSources.get(0)).setDecayTime(oscNo, time);
    }

    public void setOscillatorPitch(int oscNo, int pitch){
        ((OscillatorManager)soundSources.get(0)).setOscillatorPitch(oscNo, pitch);
    }

    public void setWaveForm(final int oscNo, final int waveForm){
        ((OscillatorManager)soundSources.get(0)).setWaveForm(oscNo, waveForm);
    }

    //pan
    public void setPan(float pan){
        activeSoundSource.setPan(pan);
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

    public SoundSource getSoundSource() {
        return activeSoundSource;
    }

    public ProcessingChain[] getProcessingChains() {
        return activeSoundSource.getProcessingChains();
    }

    public ArrayList<String> getPresetCategories(){
        return SoundSourcePreset.getCategories();
    }

    public void setRandomPreset(){
        activeSoundSource.setRandomPreset();
    }

    /** RndSeqManager calls this with one of the static strings in SoundSourcePreset, so make sure to cover them and add anything extra class-specific **/
    public void setPreset(String s){
        //Log.e("SoundManager", "setPreset(): "+s);
        activeSoundSource.setPreset(s);
    }

    public SoundEvents getSoundEvents(int nOfSteps, int subs, int step, boolean add){
        return activeSoundSource.getSoundEvents(nOfSteps, subs, step, add);
    }

    /** SUBS **/
    public void updateSubs(int subs){
        getOscillatorManager().updateSubs(subs);
    }

    /** PLAY **/
    public void playDrum(){
        activeSoundSource.playDrum();
    }

    /** RND **/
    public void randomizeAll(){
        activeSoundSource.randomizeAll();
    }

    /** GFX **/

    public int getBgImageId() {
        return activeSoundSource.getBgImageId();
    }

    public int getPresetsListImageId() {
        return activeSoundSource.getPresetListImageId();
    }

    /** RESTORATION **/
    public void restore(SoundManagerKeeper k){
        activeSoundSource = soundSources.get(k.activeSoundSourceIndex);
        for(int i = 0; i<k.ssKeepers.size(); i++){
            soundSources.get(i).restore(k.ssKeepers.get(i));
        }
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
        activeSoundSource.destroy();
    }
}
