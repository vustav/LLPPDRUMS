package com.kiefer.machine.sequence.track.soundManager.presets.osc;

import static com.kiefer.utils.NmbrUtils.getMaxiRandomMultiplier;
import static com.kiefer.utils.NmbrUtils.getMiniRandomMultiplier;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.Oscillator;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

public class OscPresetTom extends OscPreset {

    public OscPresetTom(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupOscillators(OscillatorManager oscillatorManager){
        Oscillator osc0 = oscillatorManager.getOscillators()[0];
        Oscillator osc1 = oscillatorManager.getOscillators()[1];

        osc0.setWaveForm(0); //sine
        osc0.setVolume(.9f * getMiniRandomMultiplier());
        osc0.setOscillatorPitch((int)((maxPitch / 2) * getMiniRandomMultiplier()));
        osc0.setAttackTime(maxAtk / 20 * getMiniRandomMultiplier());

        osc0.setDecayTime(maxDecay * .7f * getMaxiRandomMultiplier());
        osc0.setOn(true);

        osc1.setWaveForm(1); //triangle
        osc1.setVolume(.45f * getMiniRandomMultiplier());
        osc1.setOscillatorPitch((int)((maxPitch * .4f) * getMiniRandomMultiplier()));
        osc1.setAttackTime(maxAtk / 19 * getMiniRandomMultiplier());
        osc1.setDecayTime(maxDecay * .3f * getMaxiRandomMultiplier());
        osc1.setOn(true);
    }

    @Override
    public String getName(){
        return SoundSourcePreset.TOM;
    }
}
