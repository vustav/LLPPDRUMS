package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryHHClosed extends SampleCategory{
    public static String name = SoundSourcePreset.HHClosed;

    public SampleCategoryHHClosed(){
        samples = new ArrayList<>();
        samples.add(new Sample("Cowbell-1"));
        samples.add(new Sample("Cowbell-2"));
    }

    @Override
    public String getName(){
        return name;
    }
}
