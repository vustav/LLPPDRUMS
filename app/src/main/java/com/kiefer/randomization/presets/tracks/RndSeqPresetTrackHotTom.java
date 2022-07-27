package com.kiefer.randomization.presets.tracks;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;

import java.util.Random;

public class RndSeqPresetTrackHotTom extends RndSeqPresetTrack{
    public RndSeqPresetTrackHotTom(LLPPDRUMS llppdrums, int nOfSteps, int nOfSubs){
        super(llppdrums, SoundSourcePreset.TOM, nOfSteps, nOfSubs, llppdrums.getResources().getString(R.string.trackPresetHotTomsName));

        Random r = new Random();

        for(int step = 0; step < nOfSteps; step++){
            for(int sub = 0; sub < nOfSubs; sub++) {
                setSubPerc(step, sub, r.nextFloat() * .3f);
                setSubVolInterval(step, sub, .3f, .6f);
                setSubPitchInterval(step, sub, .4f, .5f);
            }
            setStepPanInterval(step, -1f, 1f);
        }
    }
}

