package com.kiefer.randomization.presets;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackBassBasic;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackHHBasic;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackRandom;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackSnareBasic;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;

import java.util.ArrayList;

public class RandomizeSeqPresetHiBeat extends RandomizeSeqPreset {

    public RandomizeSeqPresetHiBeat(LLPPDRUMS llppdrums, RndSeqManager rndSeqManager){
        super(llppdrums, rndSeqManager, llppdrums.getResources().getString(R.string.randomizerHiBeatName));
    }

    @Override
    public void createPreset() {
        tempo = 120;
        steps = 12;
        beats = 4;

        ArrayList<RndSeqPresetTrack> tracks = new ArrayList<>();
        RndSeqPresetTrack track  = new RndSeqPresetTrackBassBasic(llppdrums, steps, 1, beats);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        track.setSubPerc(8, 0, 1);
        track.setSubPerc(9, 0, 0);
        track.setSubPerc(10, 0, 1);
        track.setSubPerc(11, 0, 0);

        track  = new RndSeqPresetTrackSnareBasic(llppdrums, steps, 2, beats);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        for(int step = 7; step<steps; step++){
            for(int sub = 0; sub < 2; sub++) {
                track.randomizeStep(step, sub);
            }
        }

        track  = new RndSeqPresetTrackHHBasic(llppdrums, steps, 3);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        for(int step = 7; step<steps; step++){
            for(int sub = 0; sub < 3; sub++) {
                track.randomizeStep(step, sub);
            }
        }

        track  = new RndSeqPresetTrackRandom(llppdrums, steps, 1);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        for(int step = 7; step<steps; step++){
            track.randomizeStep(step, 0);
        }

        rndSeqManager.setTracks(tracks);
        rndSeqManager.setTempo(tempo);
    }
}
