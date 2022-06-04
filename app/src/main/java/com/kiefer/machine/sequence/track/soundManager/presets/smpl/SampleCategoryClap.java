package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryClap extends SampleCategory{

    public SampleCategoryClap(){
        samples = new ArrayList<>();
        samples.add(new Sample("CLAP1"));
        samples.add(new Sample("CLAP2"));
        samples.add(new Sample("CLAP3"));
        samples.add(new Sample("CLAP4"));
        samples.add(new Sample("CLAP5"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.CLAP;
    }
}
