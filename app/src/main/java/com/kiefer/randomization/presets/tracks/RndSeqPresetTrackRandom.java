package com.kiefer.randomization.presets.tracks;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.randomization.rndTrackManager.RndTrackManager;

import java.util.Random;

public class RndSeqPresetTrackRandom extends RndSeqPresetTrack {

    public RndSeqPresetTrackRandom(LLPPDRUMS llppdrums, int nOfSteps, int nOfSubs){
        super(llppdrums, RndTrackManager.RANDOM, nOfSteps, nOfSubs);

        Random random = new Random();

        for(int step = 0; step < nOfSteps; step++){
            for(int sub = 0; sub < nOfSubs; sub++) {
                setSubPerc(step, sub, random.nextFloat());
                setSubVol(step, sub, random.nextFloat());
                setSubPitch(step, sub, random.nextFloat());
            }
        }
    }

    @Override
    public String getName(){
        return llppdrums.getResources().getString(R.string.trackPresetRandomName);
    }
}
