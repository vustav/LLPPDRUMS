package com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.osc;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.SoundSourcePreset;

import java.util.Random;

public abstract class OscPreset extends SoundSourcePreset {
    protected LLPPDRUMS llppdrums;
    protected Random random;

    protected int maxPitch;
    protected float maxVol, maxAtk, maxDecay;

    public OscPreset(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
        random = new Random();

        maxVol = 1f;
        maxPitch = llppdrums.getResources().getInteger(R.integer.maxPitch);
        maxAtk = llppdrums.getResources().getInteger(R.integer.maxAtkTime) / 100f;
        maxDecay = llppdrums.getResources().getInteger(R.integer.maxDecayTime) / 100f;
    }

    //returns a float between min and max
    protected float getRndmizer(float min, float max){
        float span = max - min;
        float n = random.nextFloat() * span + min;
        return n;
    }

    public abstract void setupOscillators(OscillatorManager oscillatorManager);
    public abstract String getName();
}
