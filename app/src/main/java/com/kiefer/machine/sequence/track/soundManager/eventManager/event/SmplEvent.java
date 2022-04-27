package com.kiefer.machine.sequence.track.soundManager.eventManager.event;

import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.track.soundManager.eventManager.Sub;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.soundManager.sampleManager.SmplManager;

import nl.igorski.mwengine.core.SampleEvent;
import nl.igorski.mwengine.core.SampleManager;
import nl.igorski.mwengine.core.SynthEvent;

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
    public void addToSequencer(){
        //Log.e("SmplEvent", "addToSequencer()");
        //event.addToSequencer();
        event.setEnabled(true);
    }

    @Override
    public void removeFromSequencer(){
        //Log.e("SmplEvent", "removeFromSequencer()");
        //event.removeFromSequencer();
        event.setEnabled(false);
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
