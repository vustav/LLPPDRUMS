package com.kiefer.machine.sequence.track.soundManager.presets.osc;

import static com.kiefer.utils.NmbrUtils.getMiniRandomMultiplier;

import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.Oscillator;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

public class OscPresetRide extends OscPresetCrash {

    public OscPresetRide(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupOscillators(OscillatorManager oscillatorManager){
        super.setupOscillators(oscillatorManager);

        Oscillator osc0 = oscillatorManager.getOscillators()[0];
        Oscillator osc1 = oscillatorManager.getOscillators()[1];

        osc0.setVolume(osc0.getVolume() * .75f);
        osc1.setVolume(osc1.getVolume() * .75f);
        osc0.setReleaseTime(osc0.getReleaseTime() * .75f);
        osc1.setReleaseTime(osc1.getReleaseTime() * .75f);
    }

    @Override
    public String getName(){
        return SoundSourcePreset.RIDE;
    }
}