package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryMisc extends SampleCategory{

    public SampleCategoryMisc(){
        samples = new ArrayList<>();
    }

    @Override
    public String getName(){
        return "MISC";
    }
}
