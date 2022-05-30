package com.kiefer.machine.sequence.track.soundManager.presets.osc;

import static com.kiefer.utils.NmbrUtils.getMiniRandomMultiplier;

import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.Oscillator;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

/** COPY OF HHOpen -- FIXXX!!! **/

public class OscPresetRide extends OscPresetCrash {

    public OscPresetRide(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public String getName(){
        return SoundSourcePreset.RIDE;
    }
}