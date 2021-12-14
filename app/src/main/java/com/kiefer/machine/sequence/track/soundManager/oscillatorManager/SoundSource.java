package com.kiefer.machine.sequence.track.soundManager.oscillatorManager;

import com.kiefer.files.keepers.Keeper;
import com.kiefer.machine.sequence.track.soundManager.events.SoundEvents;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.SoundSourcePreset;

import java.util.ArrayList;

import nl.igorski.mwengine.core.ProcessingChain;
import nl.igorski.mwengine.core.BaseInstrument;

public abstract class SoundSource {
    public abstract void activate();
    public abstract void deactivate();

    /** PLAY **/
    public abstract void playDrum();

    /** RND **/
    public abstract void randomizeAll();

    /** PRESETS **/
    //ArrayList<SoundSourcePreset> getPresets();
    public abstract void setRandomPreset();
    public abstract void setPreset(String s);

    /** SET **/
    public abstract void setPan(float pan);

    /** GET **/
    public abstract SoundEvents getSoundEvents(int nOfSteps, int subs, int step, boolean add);
    public abstract ProcessingChain[] getProcessingChains();
    public abstract BaseInstrument getInstrument(int instrNo);
    /*
    ArrayList<SoundSourcePreset> getPresets();
    void setRandomPreset();
    void setPreset(String s);
    void setPreset(int i);
    */

    /** GFX **/
    public abstract int getBgImageId();
    public abstract int getPresetListImageId();

    /** RESTORATION **/
    public abstract void restore(Keeper k);
    public abstract Keeper getKeeper();

    /** DESTRUCTION **/
    public abstract void destroy();

    /** MEMBERS **/
    protected ArrayList<SoundSourcePreset> presets;

    public SoundSource(){
        presets = new ArrayList<>();
    }

    public void setPreset(int i){
        setPreset(presets.get(i).getName());
    }
}
