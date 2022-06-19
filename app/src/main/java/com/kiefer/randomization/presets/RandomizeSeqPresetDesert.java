package com.kiefer.randomization.presets;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackBassBasic;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackHHBasic;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackRandom;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackSnareBasic;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;
import com.kiefer.utils.NmbrUtils;

import java.util.ArrayList;

public class RandomizeSeqPresetDesert extends RandomizeSeqPreset {

    public RandomizeSeqPresetDesert(LLPPDRUMS llppdrums, RndSeqManager rndSeqManager){
        super(llppdrums, rndSeqManager, llppdrums.getResources().getString(R.string.randomizerDesertName));
    }

    @Override
    public void createPreset() {
        tempo = 100;
        steps = 8;
        //beats = 4;

        ArrayList<RndSeqPresetTrack> tracks = new ArrayList<>();
        //RndSeqPresetTrack track  = new RndSeqPresetTrackBassBasic(llppdrums, steps, 1, beats);

        //BASS
        int subs = 3;
        RndSeqPresetTrack track = new RndSeqPresetTrack(llppdrums, SoundSourcePreset.BASS, steps, subs, llppdrums.getResources().getString(R.string.trackPresetBassBasicName));

        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(true);

        //turn them all off
        for(int step = 0; step < steps; step++){
            for(int sub = 0; sub < subs; sub++) {
                track.setSubPerc(step, sub, 0);
                track.setSubVolInterval(step, sub, .5f, .7f);
                track.setSubPitchInterval(step, sub, .4f, .5f);
            }
            track.setStepPanInterval(step, -.2f, -.2f);
        }

        //turn on manually
        track.setSubPerc(0, 0, 1);
        track.setSubPerc(1, 2, 1);
        track.setSubPerc(3, 1, 1);

        tracks.add(track);

        //SNARE
        subs = 3;
        track = new RndSeqPresetTrack(llppdrums, SoundSourcePreset.SNARE, steps, subs, llppdrums.getResources().getString(R.string.trackPresetSnareBasicName));

        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(true);

        //turn them all off
        for(int step = 0; step < steps; step++){
            for(int sub = 0; sub < subs; sub++) {
                track.setSubPerc(step, sub, 0);
                track.setSubVolInterval(step, sub, .5f, .7f);
                track.setSubPitchInterval(step, sub, .4f, .5f);
            }
            track.setStepPanInterval(step, -.2f, -.2f);
        }

        //turn on manually
        track.setSubPerc(0, 2, 1);
        track.setSubPerc(1, 0, 1);
        track.setSubPerc(2, 1, 1);
        track.setSubPerc(2, 2, 1);
        track.setSubPerc(4, 0, 1);
        track.setSubPerc(4, 1, 1);

        tracks.add(track);

        /*
        track  = new RndSeqPresetTrackHHBasic(llppdrums, steps, 2);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(true);
        tracks.add(track);

        track  = new RndSeqPresetTrackRandom(llppdrums, steps, 1);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(true);
        tracks.add(track);

         */

        rndSeqManager.setTracks(tracks);
        rndSeqManager.setTempo(tempo);
    }
}
