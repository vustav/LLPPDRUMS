package com.kiefer.randomization.presets.tracks;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;
import com.kiefer.machine.sequence.track.soundManager.presets.osc.OscPresetBass;
import com.kiefer.utils.NmbrUtils;

public class RndSeqPresetTrackBassBasic extends RndSeqPresetTrack{
    public RndSeqPresetTrackBassBasic(LLPPDRUMS llppdrums, int nOfSteps, int nOfSubs, int beats){
        super(llppdrums, SoundSourcePreset.BASS, nOfSteps, nOfSubs,llppdrums.getResources().getString(R.string.trackPresetBassBasicName));

        for(int step = 0; step < nOfSteps; step++){
            if(step % beats == 0){
                for(int sub = 0; sub < nOfSubs; sub++) {
                    float perc = 0;
                    if(sub == 0){
                        perc = 1;
                    }
                    setSubPerc(step, sub, perc);
                    setSubVol(step, sub, NmbrUtils.getRndmizer(.5f, .7f));
                    setSubPitch(step, sub, NmbrUtils.getRndmizer(.4f, .5f));
                }
            }
            else{
                for(int sub = 0; sub < nOfSubs; sub++) {
                    setSubPerc(step, sub, 0);
                }
            }
        }
    }
}

