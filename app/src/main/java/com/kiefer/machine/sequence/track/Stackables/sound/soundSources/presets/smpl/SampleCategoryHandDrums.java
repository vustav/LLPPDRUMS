package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.smpl;

import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryHandDrums extends SampleCategory{

    public SampleCategoryHandDrums(){
        samples = new ArrayList<>();
        samples.add(new Sample("Hand-Drum"));
        samples.add(new Sample("Hi-Bongo"));
        samples.add(new Sample("High-Conga-2"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.HANDDRUMS;
    }
}
