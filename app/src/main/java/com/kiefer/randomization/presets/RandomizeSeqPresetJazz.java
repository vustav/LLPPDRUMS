package com.kiefer.randomization.presets;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackBassBasic;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackHHBasic;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackHHJazz;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackRandom;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackSnareBasic;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;

import java.util.ArrayList;

public class RandomizeSeqPresetJazz extends RandomizeSeqPreset {

    public RandomizeSeqPresetJazz(LLPPDRUMS llppdrums, RndSeqManager rndSeqManager){
        super(llppdrums, rndSeqManager, llppdrums.getResources().getString(R.string.randomizerJazzName));
    }

    @Override
    public void createPreset() {
        int tempo = 120;
        int steps = 8;
        int beats = 4;

        ArrayList<RndSeqPresetTrack> tracks = new ArrayList<>();
        RndSeqPresetTrack track  = new RndSeqPresetTrackBassBasic(llppdrums, steps, 1, beats);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        track  = new RndSeqPresetTrackSnareBasic(llppdrums, steps, 1, beats);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        track  = new RndSeqPresetTrackHHJazz(llppdrums, steps);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        track  = new RndSeqPresetTrackRandom(llppdrums, steps, 1);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        rndSeqManager.setTracks(tracks);
        rndSeqManager.setTempo(tempo);
    }
}
