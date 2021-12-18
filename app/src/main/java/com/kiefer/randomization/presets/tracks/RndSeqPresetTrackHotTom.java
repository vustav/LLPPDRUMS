package com.kiefer.randomization.presets.tracks;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.soundManager.presets.osc.OscPresetBass;
import com.kiefer.utils.NmbrUtils;

import java.util.Random;

public class RndSeqPresetTrackHotTom extends RndSeqPresetTrack{
    public RndSeqPresetTrackHotTom(LLPPDRUMS llppdrums, int nOfSteps, int nOfSubs){
        super(llppdrums, OscPresetBass.name, nOfSteps, nOfSubs);

        Random r = new Random();

        for(int step = 0; step < nOfSteps; step++){
            for(int sub = 0; sub < nOfSubs; sub++) {
                setSubPerc(step, sub, r.nextFloat());
                setSubVol(step, sub, NmbrUtils.getRndmizer(.5f, .7f));
                setSubPitch(step, sub, NmbrUtils.getRndmizer(.4f, .5f));
            }
        }
    }

    @Override
    public String getName(){
        return llppdrums.getResources().getString(R.string.trackPresetHotTomsName);
    }
}

