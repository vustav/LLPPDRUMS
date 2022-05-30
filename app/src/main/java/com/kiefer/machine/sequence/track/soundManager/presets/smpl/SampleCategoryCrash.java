package com.kiefer.machine.sequence.track.soundManager.presets.smpl;

import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;

import java.util.ArrayList;

public class SampleCategoryCrash extends SampleCategory{

    public SampleCategoryCrash(){
        samples = new ArrayList<>();
        samples.add(new Sample("drum-crash-3_5bpm_E_major"));
        samples.add(new Sample("drum-crash-4_5bpm_E_major"));
        samples.add(new Sample("drum-crash_1bpm_E_minor"));
        samples.add(new Sample("future-drum-hit-crash-2_3bpm_C_minor"));
        samples.add(new Sample("future-drum-hit-crash_3bpm_E_major"));
    }

    @Override
    public String getName(){
        return SoundSourcePreset.CRASH;
    }
}
