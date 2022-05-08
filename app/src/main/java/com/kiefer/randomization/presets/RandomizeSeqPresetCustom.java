package com.kiefer.randomization.presets;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.rndSeqManager.RndSeqManagerKeeper;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;

import java.util.ArrayList;

public class RandomizeSeqPresetCustom extends RandomizeSeqPreset{
    private RndSeqManagerKeeper keeper;

    public RandomizeSeqPresetCustom(LLPPDRUMS llppdrums, RndSeqManager rndSeqManager, RndSeqManagerKeeper keeper){
        super(llppdrums, rndSeqManager, keeper.randomizeSeqPresetKeeper.name);
        this.keeper = keeper;
    }

    @Override
    public void createPreset() {
        tempo = keeper.randomizeSeqPresetKeeper.tempo;
        steps = keeper.randomizeSeqPresetKeeper.steps;
        beats = keeper.randomizeSeqPresetKeeper.beats;

        ArrayList<RndSeqPresetTrack> tracks = new ArrayList<>();
        for(int i = 0; i < keeper.rndSeqPresetTrackKeepers.size(); i++){
            tracks.add(new RndSeqPresetTrack(llppdrums, keeper.rndSeqPresetTrackKeepers.get(i)));
        }

        rndSeqManager.setTracks(tracks);
        rndSeqManager.setTempo(tempo);
    }
}
