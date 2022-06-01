package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryRide extends SampleCategory{

    public SampleCategoryRide(){
        samples = new ArrayList<>();
        samples.add(new Sample("drum-crash-3_5bpm_E_major"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.RIDE;
    }
}