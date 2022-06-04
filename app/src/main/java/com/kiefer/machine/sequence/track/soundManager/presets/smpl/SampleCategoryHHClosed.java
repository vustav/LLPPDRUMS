package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryHHClosed extends SampleCategory{

    public SampleCategoryHHClosed(){
        samples = new ArrayList<>();
        samples.add(new Sample("HH_CLOSED1"));
        samples.add(new Sample("HH_CLOSED2"));
        samples.add(new Sample("HH_CLOSED3"));
        samples.add(new Sample("HH_CLOSED4"));
        samples.add(new Sample("HH_CLOSED5"));
        samples.add(new Sample("HH_CLOSED6"));
        samples.add(new Sample("HH_CLOSED7"));
        samples.add(new Sample("HH_CLOSED8"));
        samples.add(new Sample("HH_CLOSED9"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.HHClosed;
    }
}
