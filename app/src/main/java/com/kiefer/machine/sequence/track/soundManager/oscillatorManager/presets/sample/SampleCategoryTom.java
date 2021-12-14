package com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.sample;

import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryTom extends SampleCategory{
    public static String name = SoundSourcePreset.TOM;

    public SampleCategoryTom(){
        samples = new ArrayList<>();
        samples.add(new Sample("Electro-Tom"));
        samples.add(new Sample("Electronic-Tom-1"));
        samples.add(new Sample("Electronic-Tom-2"));
        samples.add(new Sample("Electronic-Tom-3"));
        samples.add(new Sample("Electronic-Tom-4"));
    }

    @Override
    public String getName(){
        return name;
    }
}
