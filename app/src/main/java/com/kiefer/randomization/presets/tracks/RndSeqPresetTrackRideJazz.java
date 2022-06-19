package com.kiefer.randomization.presets.tracks;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;
import com.kiefer.utils.NmbrUtils;

public class RndSeqPresetTrackRideJazz extends RndSeqPresetTrack {

    public RndSeqPresetTrackRideJazz(LLPPDRUMS llppdrums, int nOfSteps){
        super(llppdrums, SoundSourcePreset.RIDE, nOfSteps, 4, llppdrums.getResources().getString(R.string.trackPresetHHBasicName));

        for(int step = 0; step < nOfSteps; step++){
            for(int sub = 0; sub < 4; sub++) {
                float perc = 0;

                setSubPerc(step, sub, perc);
                setSubVolInterval(step, sub, .5f, .7f);
                setSubPitchInterval(step, sub, .4f, .5f);
            }
            setStepPanInterval(step, 0, 0);
        }

        for(int step = 0; step < nOfSteps; step+=4){
            for(int sub = 0; sub < 4; sub++) {
                if(sub == 0){
                    setSubPerc(step, sub, 1);
                }
            }
            setStepPanInterval(step, 0, 0);
        }

        for(int step = 3; step < nOfSteps; step+=4){
            for(int sub = 0; sub < 4; sub++) {
                if(sub == 2){
                    setSubPerc(step, sub, 1);
                }
            }
            setStepPanInterval(step, .2f, .2f);
        }

        //turn on at the snares also
        setSubPerc(2, 0, 1);
        setSubPerc(6, 0, 1);
    }
}
