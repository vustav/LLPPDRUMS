package com.kiefer.machine.sequence.track.Stackables.sound.soundSources;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.Keeper;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;

import java.util.ArrayList;

import nl.igorski.mwengine.core.ProcessingChain;
import nl.igorski.mwengine.core.BaseInstrument;

public abstract class SoundSource {
    protected LLPPDRUMS llppdrums;

    public abstract void activate();
    public abstract void deactivate();

    /** NAME **/
    public abstract String getName();

    /** PLAY **/
    public abstract void playDrum();

    /** RND **/
    public abstract void randomizeAll();

    /** PRESETS **/
    //ArrayList<SoundSourcePreset> getPresets();
    public abstract void setRandomPreset();
    public abstract void setPreset(String s);

    /** AUTO **/
    public abstract void setupParamNames();

    //PARAMS
    protected ArrayList<String> paramNames; //has to be populated by children!

    /** SET **/
    public abstract void setPan(float pan);

    /** GET **/
    /*
    public SoundEvents getSoundEvents(int nOfSteps, int subs, int step, boolean add){
        return new SoundEvents(llppdrums, drumSequence, drumTrack, this, nOfSteps, subs, step, add);
    }

     */
    public abstract ProcessingChain[] getProcessingChains();
    public abstract BaseInstrument getInstrument(int instrNo);
    /*
    ArrayList<SoundSourcePreset> getPresets();
    void setRandomPreset();
    void setPreset(String s);
    void setPreset(int i);
    */

    /** RESTORATION **/
    public abstract void restore(Keeper k);
    public abstract Keeper getKeeper();

    /** DESTRUCTION **/
    public abstract void destroy();

    /** MEMBERS **/
    protected ArrayList<SoundSourcePreset> presets;

    public SoundSource(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
        presets = new ArrayList<>();

        paramNames = new ArrayList<>();
        setupParamNames();
    }

    public ArrayList<SoundSourcePreset> getPresets(){
        return presets;
    }

    public ArrayList<String> getParams(){
        return paramNames;
    }
/*
    public void setPreset(int i){
        setPreset(presets.get(i).getName());
    }

 */
}
