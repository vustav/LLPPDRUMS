package com.kiefer.machine.sequence.track.soundManager.presets.osc;

import static com.kiefer.utils.NmbrUtils.getMiniRandomMultiplier;

import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.Oscillator;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

/** COPY OF HHOpen -- FIXXX!!! **/

public class OscPresetCrash extends OscPresetHHClosed {

    public OscPresetCrash(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupOscillators(OscillatorManager oscillatorManager){
        super.setupOscillators(oscillatorManager);
        Oscillator osc0 = oscillatorManager.getOscillators()[0];
        Oscillator osc1 = oscillatorManager.getOscillators()[1];

        //add some decay to make it sound open
        float decayMultiplier = 1f - (random.nextFloat() * .1f);
        osc0.setDecayTime(maxDecay * .8f * decayMultiplier);
        osc0.setOn(true);

        decayMultiplier = 1f - (random.nextFloat() * .1f);
        osc1.setDecayTime(maxDecay * .8f * decayMultiplier);
        osc1.setOn(true);
    }

    @Override
    public String getName(){
        return SoundSourcePreset.CRASH;
    }
}