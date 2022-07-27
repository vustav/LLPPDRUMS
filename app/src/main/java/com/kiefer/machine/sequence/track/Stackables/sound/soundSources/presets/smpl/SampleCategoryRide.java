package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.smpl;

import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryRide extends SampleCategory{

    public SampleCategoryRide(){
        samples = new ArrayList<>();
        samples.add(new Sample("RIDE1"));
        samples.add(new Sample("RIDE2"));
        samples.add(new Sample("RIDE3"));
        samples.add(new Sample("RIDE4"));
        samples.add(new Sample("RIDE5"));
        samples.add(new Sample("RIDE6"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.RIDE;
    }
}
