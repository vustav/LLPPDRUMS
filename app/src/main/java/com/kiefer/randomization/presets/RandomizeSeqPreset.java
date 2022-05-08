package com.kiefer.randomization.presets;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.rndSeqManager.RandomizeSeqPresetKeeper;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;

import java.util.Random;

public abstract class RandomizeSeqPreset {
    protected LLPPDRUMS llppdrums;
    protected RndSeqManager rndSeqManager;
    protected Random random;
    protected String name;
    protected int tempo, steps, beats;

    public RandomizeSeqPreset(LLPPDRUMS llppdrums, RndSeqManager rndSeqManager, String name){
        this.llppdrums = llppdrums;
        this.rndSeqManager = rndSeqManager;
        random = new Random();
        this.name = name;
    }
    public abstract void createPreset();

    /** STEPS **/

    public void addStep(){
        for(RndSeqPresetTrack track : rndSeqManager.getTracks()){
            track.addStep(track.getnOfSubs());
        }
    }

    public void removeStep(){
        for(RndSeqPresetTrack track : rndSeqManager.getTracks()){
            track.removeStep();
        }
    }

    /** MOD MARKER **/
    public void addModifiedMarker(){
        if(!(name.substring(name.length()-1).equals(llppdrums.getResources().getString(R.string.randomizerModifiedName)))) {
            name = name + llppdrums.getResources().getString(R.string.randomizerModifiedName);
        }
    }
    public void removeModifiedMarker(){
        if((name.substring(name.length()-1).equals(llppdrums.getResources().getString(R.string.randomizerModifiedName)))) {
            name = name.substring(0, name.length()-1);
        }
    }

    /** GET **/
    public String getName(){
        return name;
    }

    /** RESTORE **/
    public RandomizeSeqPresetKeeper getKeeper(){
        RandomizeSeqPresetKeeper keeper = new RandomizeSeqPresetKeeper();
        keeper.name = name;
        keeper.tempo = tempo;
        keeper.steps = steps;
        keeper.beats = beats;
        return keeper;
    }
}
