package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.smpl;

import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryCrash extends SampleCategory{

    public SampleCategoryCrash(){
        samples = new ArrayList<>();
        samples.add(new Sample("CRASH1"));
        samples.add(new Sample("CRASH2"));
        samples.add(new Sample("CRASH3"));
        samples.add(new Sample("CRASH4"));
        samples.add(new Sample("CRASH5"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.CRASH;
    }
}
