package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryBass extends SampleCategory{

    public SampleCategoryBass(){
        samples = new ArrayList<>();
        samples.add(new Sample("KICK1"));
        samples.add(new Sample("KICK2"));
        samples.add(new Sample("KICK3"));
        samples.add(new Sample("KICK4"));
        samples.add(new Sample("KICK5"));
        samples.add(new Sample("KICK6"));
        samples.add(new Sample("KICK7"));
        samples.add(new Sample("KICK8"));
        samples.add(new Sample("KICK9"));
        samples.add(new Sample("KICK10"));
        samples.add(new Sample("KICK11"));
        samples.add(new Sample("KICK12"));
        samples.add(new Sample("KICK13"));
        samples.add(new Sample("KICK14"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.BASS;
    }
}
