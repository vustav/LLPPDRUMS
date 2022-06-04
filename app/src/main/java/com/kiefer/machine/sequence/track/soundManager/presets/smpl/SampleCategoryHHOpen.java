package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryHHOpen extends SampleCategory{

    public SampleCategoryHHOpen(){
        samples = new ArrayList<>();
        samples.add(new Sample("HH_OPEN1"));
        samples.add(new Sample("HH_OPEN2"));
        samples.add(new Sample("HH_OPEN3"));
        samples.add(new Sample("HH_OPEN4"));
        samples.add(new Sample("HH_OPEN5"));
        samples.add(new Sample("HH_OPEN6"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.HHOpen;
    }
}
