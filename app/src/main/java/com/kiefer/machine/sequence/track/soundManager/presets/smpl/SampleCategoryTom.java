package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryTom extends SampleCategory{

    public SampleCategoryTom(){
        samples = new ArrayList<>();
        samples.add(new Sample("TOM1"));
        samples.add(new Sample("TOM2"));
        samples.add(new Sample("TOM3"));
        samples.add(new Sample("TOM4"));
        samples.add(new Sample("TOM5"));
        samples.add(new Sample("TOM6"));
        samples.add(new Sample("TOM7"));
        samples.add(new Sample("TOM8"));
        samples.add(new Sample("TOM9"));
        samples.add(new Sample("TOM10"));
        samples.add(new Sample("TOM11"));
        samples.add(new Sample("TOM12"));
        samples.add(new Sample("TOM13"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.TOM;
    }
}
