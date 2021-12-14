package com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.osc;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.Oscillator;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.SoundSourcePreset;

import static com.kiefer.utils.NmbrUtils.getMiniRandomMultiplier;

public class OscPresetHH extends OscPreset {
    public static String name = SoundSourcePreset.HH;

    public OscPresetHH(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupOscillators(OscillatorManager oscillatorManager){
        Oscillator osc0 = oscillatorManager.getOscillators()[0];
        Oscillator osc1 = oscillatorManager.getOscillators()[1];

        if(random.nextInt(2) == 0){
            osc0.setWaveForm(1); //triangle
        }
        else{
            osc0.setWaveForm(3); //square
        }
        osc0.setVolume(getRndmizer(.7f, .9f));
        osc0.setOscillatorPitch((int)(maxPitch * .8 * getMiniRandomMultiplier()));
        osc0.setAttackTime(maxAtk / 10f * getMiniRandomMultiplier());
        osc0.setDecayTime(maxDecay / 9f * getMiniRandomMultiplier());
        osc0.setOn(true);

        osc1.setWaveForm(4); //noise
        osc1.setVolume(getRndmizer(.8f, 1));
        osc1.setOscillatorPitch((int)(maxPitch * .9 * getMiniRandomMultiplier()));
        osc1.setAttackTime(maxAtk / 11f * getMiniRandomMultiplier());
        osc1.setDecayTime(maxDecay * getRndmizer(.1f, .5f));
        osc1.setOn(true);
    }

    @Override
    public String getName(){
        return name;
    }
}
