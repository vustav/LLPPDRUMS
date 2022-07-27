package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.smpl;

import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategorySnare extends SampleCategory{

    public SampleCategorySnare(){
        samples = new ArrayList<>();
        samples.add(new Sample("SNARE1"));
        samples.add(new Sample("SNARE2"));
        samples.add(new Sample("SNARE3"));
        samples.add(new Sample("SNARE4"));
        samples.add(new Sample("SNARE5"));
        samples.add(new Sample("SNARE6"));
        samples.add(new Sample("SNARE7"));
        samples.add(new Sample("SNARE8"));
        samples.add(new Sample("SNARE9"));
        samples.add(new Sample("SNARE10"));
        samples.add(new Sample("SNARE11"));
        samples.add(new Sample("SNARE12"));
        samples.add(new Sample("SNARE13"));
        samples.add(new Sample("SNARE14"));
        samples.add(new Sample("SNARE15"));
        samples.add(new Sample("SNARE16"));
        samples.add(new Sample("SNARE17"));
        samples.add(new Sample("SNARE18"));
        samples.add(new Sample("SNARE19"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.SNARE;
    }
}
