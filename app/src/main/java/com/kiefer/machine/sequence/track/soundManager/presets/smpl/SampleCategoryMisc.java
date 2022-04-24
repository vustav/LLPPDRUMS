package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryMisc extends SampleCategory{

    public SampleCategoryMisc(){
        samples = new ArrayList<>();
        samples.add(new Sample("Hand-Drum"));
        samples.add(new Sample("Hi-Bongo"));
        samples.add(new Sample("High-Conga-2"));
    }

    @Override
    public String getName(){
        return "MISC";
    }
}
