package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.osc;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.oscillatorManager.Oscillator;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;

public class OscPresetHHOpen extends OscPresetHHClosed {

    public OscPresetHHOpen(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupOscillators(OscillatorManager oscillatorManager){
        super.setupOscillators(oscillatorManager);
        Oscillator osc0 = oscillatorManager.getOscillators()[0];
        //Oscillator osc1 = oscillatorManager.getOscillators()[1];

        //add some decay to make it sound open
        float decayMultiplier = 1f - (random.nextFloat() * .4f);
        osc0.setReleaseTime(maxRelease / 5f * decayMultiplier);
        osc0.setOn(true);

        /*
        decayMultiplier = 1f - (random.nextFloat() * .4f);
        osc1.setReleaseTime(maxRelease / 5f * decayMultiplier);
        osc1.setOn(true);

         */
    }

    @Override
    public String getName(){
        return SoundSourcePreset.HHOpen;
    }
}





















