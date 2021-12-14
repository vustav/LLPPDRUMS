package com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.osc;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.Oscillator;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.SoundSourcePreset;

import static com.kiefer.utils.NmbrUtils.getMiniRandomMultiplier;

import java.util.Random;

public class OscPresetBass extends OscPreset {
    public static String name = SoundSourcePreset.BASS;

    public OscPresetBass(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupOscillators(OscillatorManager oscillatorManager){
        Oscillator osc0 = oscillatorManager.getOscillators()[0];
        Oscillator osc1 = oscillatorManager.getOscillators()[1];

        osc0.setWaveForm(0); //sine
        osc0.setVolume(.9f * getMiniRandomMultiplier());
        osc0.setOscillatorPitch((int)((maxPitch / 20) * getMiniRandomMultiplier()));
        osc0.setAttackTime(maxAtk / 10 * getMiniRandomMultiplier());
        osc0.setDecayTime(maxDecay / 3 * getMiniRandomMultiplier());
        osc0.setOn(true);

        Random r = new Random();
        osc1.setWaveForm(r.nextInt(4));
        osc1.setVolume(.45f * getMiniRandomMultiplier());
        osc1.setOscillatorPitch((int)((maxPitch / 18) * getMiniRandomMultiplier()));
        osc1.setAttackTime(maxAtk / 9 * getMiniRandomMultiplier());
        osc1.setDecayTime(maxDecay / 5 * getMiniRandomMultiplier());
        osc1.setOn(true);
    }

    @Override
    public String getName(){
        return name;
    }
}
