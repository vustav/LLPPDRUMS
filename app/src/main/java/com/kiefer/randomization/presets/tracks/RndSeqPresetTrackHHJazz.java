package com.kiefer.randomization.presets.tracks;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.soundManager.presets.osc.OscPresetHHClosed;
import com.kiefer.machine.sequence.track.soundManager.presets.osc.OscPresetHHOpen;
import com.kiefer.utils.NmbrUtils;

public class RndSeqPresetTrackHHJazz extends RndSeqPresetTrack {

    public RndSeqPresetTrackHHJazz(LLPPDRUMS llppdrums, int nOfSteps){
        super(llppdrums, OscPresetHHOpen.name, nOfSteps, 4);

        for(int step = 0; step < nOfSteps; step++){
            for(int sub = 0; sub < 4; sub++) {
                float perc = 0;

                setSubPerc(step, sub, perc);
                setSubVol(step, sub, NmbrUtils.getRndmizer(.5f, .7f));
                setSubPitch(step, sub, NmbrUtils.getRndmizer(.4f, .5f));
            }
        }

        for(int step = 0; step < nOfSteps; step+=4){
            for(int sub = 0; sub < 4; sub++) {
                if(sub == 0){
                    setSubPerc(step, sub, 1);
                }

            }
        }

        for(int step = 3; step < nOfSteps; step+=4){
            for(int sub = 0; sub < 4; sub++) {
                if(sub == 3){
                    setSubPerc(step, sub, 1);
                }
            }
        }


/*
        for(int step = 0; step < nOfSteps; step++) {
            for (int sub = 0; sub < 4; sub++) {
                float perc = 0;
            }
        }


        if(step == 0 || step % 4 == 0){
            if(sub == 0){
                perc = 1;
            }
        }

        if(step == 3 || step == 7){
            if(sub == 3){
                perc = 1;
            }
        }

        setSubPerc(step, sub, perc);
        setSubVol(step, sub, NmbrUtils.getRndmizer(.5f, .7f));
        setSubPitch(step, sub, NmbrUtils.getRndmizer(.4f, .5f));

 */


    }

    @Override
    public String getName(){
        return llppdrums.getResources().getString(R.string.trackPresetHHBasicName);
    }
}
