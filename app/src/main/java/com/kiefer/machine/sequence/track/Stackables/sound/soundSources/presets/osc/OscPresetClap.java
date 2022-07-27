package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.osc;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;

public class OscPresetClap extends OscPresetSnare {

    public OscPresetClap(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public String getName(){
        return SoundSourcePreset.CLAP;
    }
}
