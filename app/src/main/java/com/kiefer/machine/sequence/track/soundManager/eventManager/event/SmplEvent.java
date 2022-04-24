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
    private final Step step;
    private final Sub sub;

    public SmplEvent(LLPPDRUMS llppdrums, Step step, Sub sub, SmplManager sampleManager){
        this.llppdrums = llppdrums;
        this.sampleManager = sampleManager;
        this.step = step;
        this.sub = sub;

        //if(step.getStepNo() == 0) {
        //Log.e("SmplEvent", "constr");
        //}

        event = new SampleEvent(sampleManager.getInstrument(0));
        event.setSample(SampleManager.getSample(sampleManager.getSelectedCategory().getSelectedSample().getName()));

        /*
        if(step.isOn() && sub.isOn()){
            addToSequencer();
        }

         */
    }

    public void updateSamples(){
        event.setSample(SampleManager.getSample(sampleManager.getSelectedCategory().getSelectedSample().getName()));
    }

    @Override
    public void addToSequencer(){
        Log.e("SmplEvent", "addToSequencer()");
        event.addToSequencer();
    }

    @Override
    public void removeFromSequencer(){
        Log.e("SmplEvent", "removeFromSequencer()");
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
        //Log.e("SmplEvent", "delete");
        //event.delete();
        llppdrums.getDeleter().addEvent(event);
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
