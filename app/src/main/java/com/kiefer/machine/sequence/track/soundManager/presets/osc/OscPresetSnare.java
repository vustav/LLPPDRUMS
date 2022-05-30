package com.kiefer.machine.sequence.track.soundManager.presets.osc;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.Oscillator;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import static com.kiefer.utils.NmbrUtils.getMiniRandomMultiplier;

import java.util.Random;

public class OscPresetSnare extends OscPreset {

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
        //osc0.setDecayTime(maxDecay * random.nextFloat() * random.nextFloat() * random.nextFloat());
        float decayMultiplier = (1f - (random.nextFloat() * .9f)) * .6f;
        //float decayMultiplier = random.nextFloat() * .3f;
        osc0.setDecayTime(maxDecay / 3f * decayMultiplier);
        osc0.setOn(true);

        //osc1.setWaveForm(5); //PWM

        /*
        Random r = new Random();
        osc1.setWaveForm(r.nextInt(oscillatorManager.getWaves().length));
        osc1.setVolume(.8f * getMiniRandomMultiplier());
        osc1.setOscillatorPitch((int)((maxPitch / 5) * getMiniRandomMultiplier()));
        osc1.setAttackTime(maxAtk / 20f * getMiniRandomMultiplier());
        //osc1.setDecayTime(maxDecay * random.nextFloat() * random.nextFloat() * random.nextFloat());
        decayMultiplier = (1f - (random.nextFloat() * .9f)) * .6f;
        //decayMultiplier = random.nextFloat() * .3f;
        osc1.setDecayTime(maxDecay / 3f * decayMultiplier);

         */
        osc1.setOn(false);
    }

    @Override
    public String getName(){
        return SoundSourcePreset.SNARE;
    }
}
