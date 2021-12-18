package com.kiefer.machine.sequence.track.soundManager.presets.osc;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.Oscillator;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import static com.kiefer.utils.NmbrUtils.getMiniRandomMultiplier;

public class OscPresetSnare extends OscPreset {
    public static String name = SoundSourcePreset.SNARE;

    public OscPresetSnare(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupOscillators(OscillatorManager oscillatorManager){
        Oscillator osc0 = oscillatorManager.getOscillators()[0];
        Oscillator osc1 = oscillatorManager.getOscillators()[1];

        osc0.setWaveForm(4); //noise
        osc0.setVolume(.9f * getMiniRandomMultiplier());
        osc0.setOscillatorPitch((int)((maxPitch / 5) * getMiniRandomMultiplier()));
        osc0.setAttackTime(maxAtk / 20f * getMiniRandomMultiplier());
        osc0.setDecayTime(maxDecay * random.nextFloat() * random.nextFloat());
        osc0.setOn(true);

        osc1.setWaveForm(5); //PWM
        osc1.setVolume(.8f * getMiniRandomMultiplier());
        osc1.setOscillatorPitch((int)((maxPitch / 5) * getMiniRandomMultiplier()));
        osc1.setAttackTime(maxAtk / 20f * getMiniRandomMultiplier());
        osc1.setDecayTime(maxDecay * random.nextFloat() * random.nextFloat());
        osc1.setOn(true);
    }

    @Override
    public String getName(){
        return name;
    }
}
