package com.kiefer.randomization.presets;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackBassBasic;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackHHOffbeat;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackHotTom;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackRandom;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackSnareBasic;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;

import java.util.ArrayList;

public class RandomizeSeqPresetDisco extends RandomizeSeqPreset {

    public RandomizeSeqPresetDisco(LLPPDRUMS llppdrums, RndSeqManager rndSeqManager){
        super(llppdrums, rndSeqManager, llppdrums.getResources().getString(R.string.randomizerDiscoName));
    }

    @Override
    public void createPreset() {
        tempo = 120;
        steps = 8;
        beats = 4;

        ArrayList<RndSeqPresetTrack> tracks = new ArrayList<>();

        RndSeqPresetTrack track  = new RndSeqPresetTrackBassBasic(llppdrums, steps, 1, beats);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(true);
        tracks.add(track);

        track  = new RndSeqPresetTrackSnareBasic(llppdrums, steps, 1, beats);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(true);
        tracks.add(track);

        track  = new RndSeqPresetTrackHHOffbeat(llppdrums, steps, 2);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(true);
        tracks.add(track);

        track  = new RndSeqPresetTrackHotTom(llppdrums, steps, 2);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(true);
        tracks.add(track);

        track  = new RndSeqPresetTrackHotTom(llppdrums, steps, 2);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(true);
        tracks.add(track);

        rndSeqManager.setTracks(tracks);
        rndSeqManager.setTempo(tempo);
    }
}
