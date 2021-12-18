package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryBass extends SampleCategory{
    public static String name = SoundSourcePreset.BASS;

    public SampleCategoryBass(){
        samples = new ArrayList<>();
        samples.add(new Sample("E-Mu-Proteus-FX-909-Kick"));
        samples.add(new Sample("E-Mu-Proteus-FX-Wacky-Kick"));
        samples.add(new Sample("Dry-Kick"));
    }

    @Override
    public String getName(){
        return name;
    }
}
