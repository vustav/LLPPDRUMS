package com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.sample;

import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryMisc extends SampleCategory{
    public static String name = SoundSourcePreset.MISC;

    public SampleCategoryMisc(){
        samples = new ArrayList<>();
        samples.add(new Sample("Hand-Drum"));
        samples.add(new Sample("Hi-Bongo"));
        samples.add(new Sample("High-Conga-2"));
    }

    @Override
    public String getName(){
        return name;
    }
}
