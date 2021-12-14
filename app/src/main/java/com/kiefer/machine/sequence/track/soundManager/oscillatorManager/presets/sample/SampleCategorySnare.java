package com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.sample;

import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategorySnare extends SampleCategory{
    public static String name = SoundSourcePreset.SNARE;

    public SampleCategorySnare(){
        samples = new ArrayList<>();
        samples.add(new Sample("Ensoniq-ESQ-1-Snare"));
    }

    @Override
    public String getName(){
        return name;
    }
}
