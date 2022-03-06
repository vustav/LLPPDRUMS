package com.kiefer.randomization.presets.tracks;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.soundManager.presets.osc.OscPresetSnare;
import com.kiefer.utils.NmbrUtils;

public class RndSeqPresetTrackSnareBasic extends RndSeqPresetTrack {

    public RndSeqPresetTrackSnareBasic(LLPPDRUMS llppdrums, int nOfSteps, int nOfSubs, int beats){
        super(llppdrums, OscPresetSnare.name, nOfSteps, nOfSubs);


        for(int step = 0; step < nOfSteps; step++){
            if((step + beats / 2) % beats == 0){
                for(int sub = 0; sub < nOfSubs; sub++) {
                    float perc = NmbrUtils.getRndmizer(.5f, .6f);
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

    @Override
    public String getName(){
        return llppdrums.getResources().getString(R.string.trackPresetSnareBasicName);
    }
}
