package com.kiefer.randomization.presets.tracks;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.soundManager.presets.osc.OscPresetHHClosed;
import com.kiefer.utils.NmbrUtils;

public class RndSeqPresetTrackHHOffbeat extends RndSeqPresetTrack {

    public RndSeqPresetTrackHHOffbeat(LLPPDRUMS llppdrums, int nOfSteps, int nOfSubs){
        super(llppdrums, OscPresetHHClosed.name, nOfSteps, nOfSubs);


        for(int step = 0; step < nOfSteps; step++){
            for(int sub = 0; sub < nOfSubs; sub++) {
                if (step % 2 == 0) {
                    setSubPerc(step, sub, 0);
                } else {
                    setSubPerc(step, sub, 1);
                    setSubVol(step, sub, NmbrUtils.getRndmizer(.5f, .7f));
                    setSubPitch(step, sub, NmbrUtils.getRndmizer(.4f, .5f));
                }
            }
        }
    }

    @Override
    public String getName(){
        return llppdrums.getResources().getString(R.string.trackPresetHHOffbeatName);
    }
}
