package com.kiefer.machine.sequence.track.soundManager.presets.osc;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

public class OscPresetClap extends OscPresetSnare {

    public OscPresetClap(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public String getName(){
        return SoundSourcePreset.CLAP;
    }
}
