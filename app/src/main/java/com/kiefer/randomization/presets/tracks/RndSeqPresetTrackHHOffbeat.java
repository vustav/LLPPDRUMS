package com.kiefer.randomization.presets.tracks;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;

public class RndSeqPresetTrackHHOffbeat extends RndSeqPresetTrack {

    public RndSeqPresetTrackHHOffbeat(LLPPDRUMS llppdrums, int nOfSteps, int nOfSubs){
        super(llppdrums, SoundSourcePreset.HHClosed, nOfSteps, nOfSubs, llppdrums.getResources().getString(R.string.trackPresetHHOffbeatName));


        for(int step = 0; step < nOfSteps; step++){
            for(int sub = 0; sub < nOfSubs; sub++) {
                if (step % 2 == 0) {
                    setSubPerc(step, sub, 0);
                } else {
                    if(sub == 0) {
                        setSubPerc(step, sub, 1);
                    }
                    else{
                        setSubPerc(step, sub, .3f);
                    }
                    setSubVolInterval(step, sub, .5f, .7f);
                    setSubPitchInterval(step, sub, .4f, .5f);
                }
            }
            setStepPanInterval(step, .2f, .2f);
        }
    }
}
