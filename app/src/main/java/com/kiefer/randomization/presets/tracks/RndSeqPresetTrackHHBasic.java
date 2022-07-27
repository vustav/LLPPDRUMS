package com.kiefer.randomization.presets.tracks;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;
import com.kiefer.utils.NmbrUtils;

public class RndSeqPresetTrackHHBasic extends RndSeqPresetTrack {

    public RndSeqPresetTrackHHBasic(LLPPDRUMS llppdrums, int nOfSteps, int nOfSubs){
        super(llppdrums, SoundSourcePreset.HHClosed, nOfSteps, nOfSubs, llppdrums.getResources().getString(R.string.trackPresetHHBasicName));

        for(int step = 0; step < nOfSteps; step++){
            for(int sub = 0; sub < nOfSubs; sub++) {
                float perc;
                if(sub == 0){
                    perc = 1;
                }
                else{
                    perc = NmbrUtils.getRndmizer(.6f, .85f);
                }
                setSubPerc(step, sub, perc);
                setSubVolInterval(step, sub, .5f, .7f);
                setSubPitchInterval(step, sub, .4f, .5f);
            }
            setStepPanInterval(step, .2f, .2f);
        }
    }
}
