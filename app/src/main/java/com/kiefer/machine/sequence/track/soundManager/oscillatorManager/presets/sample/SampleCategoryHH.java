package com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.sample;

import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryHH extends SampleCategory{
    public static String name = SoundSourcePreset.HH;

    public SampleCategoryHH(){
        samples = new ArrayList<>();
        samples.add(new Sample("Cowbell-1"));
        samples.add(new Sample("Cowbell-2"));
    }

    @Override
    public String getName(){
        return name;
    }
}
