package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.osc;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.oscillatorManager.Oscillator;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;

import static com.kiefer.utils.NmbrUtils.getMiniRandomMultiplier;

import java.util.Random;

public class OscPresetSnare extends OscPreset {

    public OscPresetSnare(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupOscillators(OscillatorManager oscillatorManager){
        Oscillator osc0 = oscillatorManager.getOscillators()[0];
        //Oscillator osc1 = oscillatorManager.getOscillators()[1];

        osc0.setWaveForm(4); //noise
        osc0.setVolume(.9f * getMiniRandomMultiplier());
        osc0.setOscillatorPitch((int)((maxPitch / 3) * getMiniRandomMultiplier()));
        osc0.setAttackTime(maxAtk / 10f * getMiniRandomMultiplier());
        //osc0.setDecayTime(maxDecay * random.nextFloat() * random.nextFloat() * random.nextFloat());
        float decayMultiplier = (1f - (random.nextFloat() * .9f)) * .6f * (random.nextFloat() + .2f);
        //float decayMultiplier = random.nextFloat() * .3f;
        osc0.setReleaseTime(maxRelease / 2f * decayMultiplier * (random.nextFloat() + .2f));
        osc0.setOn(true);

        //osc1.setWaveForm(5); //PWM

/*
        Random r = new Random();
        osc1.setWaveForm(r.nextInt(oscillatorManager.getWaves().length));
        osc1.setVolume(.8f * getMiniRandomMultiplier());
        osc1.setOscillatorPitch((int)((maxPitch / 3) * getMiniRandomMultiplier()));
        osc1.setAttackTime(maxAtk / 30f * getMiniRandomMultiplier());
        //osc1.setDecayTime(maxDecay * random.nextFloat() * random.nextFloat() * random.nextFloat());
        decayMultiplier = (1f - (random.nextFloat() * .9f)) * .6f * (random.nextFloat() + .2f);
        //decayMultiplier = random.nextFloat() * .3f;
        osc1.setReleaseTime(maxRelease / 4f * decayMultiplier * (random.nextFloat() + .2f));
        osc1.setOn(true);

 */
    }

    @Override
    public String getName(){
        return SoundSourcePreset.SNARE;
    }
}
