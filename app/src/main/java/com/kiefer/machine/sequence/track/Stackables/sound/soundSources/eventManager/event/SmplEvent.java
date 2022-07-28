package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.eventManager.event;

import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.sampleManager.SmplManager;

import nl.igorski.mwengine.core.SampleEvent;
import nl.igorski.mwengine.core.SampleManager;

public class SmplEvent extends Event{
    private final LLPPDRUMS llppdrums;
    private SmplManager sampleManager;
    private SampleEvent event;

    public SmplEvent(LLPPDRUMS llppdrums, SmplManager sampleManager){
        this.llppdrums = llppdrums;
        this.sampleManager = sampleManager;

        event = new SampleEvent(sampleManager.getInstrument(0));
        event.setSample(SampleManager.getSample(sampleManager.getSelectedCategory().getSelectedSample().getName()));

        /** The way events work now is that events are added to the sequencer on creation and then never removed, just enabled/disabled **/
        event.addToSequencer();
        event.setEnabled(false);
    }

    public void updateSamples(){
        event.setSample(SampleManager.getSample(sampleManager.getSelectedCategory().getSelectedSample().getName()));
    }

    @Override
    public void enable(){
        event.setEnabled(true);
    }

    @Override
    public void disable(){
        event.setEnabled(false);
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
        llppdrums.getDeleter().addEvent(event);
    }
}
