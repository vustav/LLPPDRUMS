package com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.sample;

import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.SoundSourcePreset;

import java.util.ArrayList;
import java.util.Random;

public abstract class SampleCategory extends SoundSourcePreset {
    protected Sample selectedSample;
    protected ArrayList<Sample> samples;

    public void randomizeSample(){
        Random r = new Random();
        selectedSample = samples.get(r.nextInt(samples.size()));
    }

    public void setSelectedSample(int i){
        selectedSample = samples.get(i);
    }

    public Sample getSelectedSample(){
        return selectedSample;
    }

    public int getSelectedSampleIndex(){
        return samples.indexOf(selectedSample);
    }

    public ArrayList<Sample> getSamples(){
        return samples;
    }
}
