package com.kiefer.machine.sequence.track.soundManager.eventManager.event;

import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.track.soundManager.eventManager.Sub;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.sampleManager.SmplManager;

import nl.igorski.mwengine.core.SampleEvent;
import nl.igorski.mwengine.core.SampleManager;
import nl.igorski.mwengine.core.SynthEvent;

public class SmplEvent extends Event{
    private SmplManager sampleManager;
    private SampleEvent event;

    public SmplEvent(Step step, Sub sub, SmplManager sampleManager){
        this.sampleManager = sampleManager;

        event = new SampleEvent(sampleManager.getInstrument(0));
        event.setSample(SampleManager.getSample(sampleManager.getSelectedCategory().getSelectedSample().getName()));

        if(step.isOn() && sub.isOn()){
            addToSequencer();
        }
    }

    public void updateSamples(){
        event.setSample(SampleManager.getSample(sampleManager.getSelectedCategory().getSelectedSample().getName()));
    }

    @Override
    public void addToSequencer(){
        event.addToSequencer();
    }

    @Override
    public void removeFromSequencer(){
        event.removeFromSequencer();
    }
    @Override
    public void positionEvent(int posInSamples){
        event.setEventStart(posInSamples);
    }

    @Override
    public void setPitch(float pitch){
        event.setPlaybackRate(pitch);
    }

    @Override
    public void setVolume(float volume){
        event.setVolume(volume);
    }

    @Override
    public void delete(){
        event.delete();
    }

    /** RESTORE **/
    /*
    @Override
    public EventsKeeper getKeeper(){
        EventsKeeper keeper = super.getKeeper();
        return keeper;
    }

     */
}
