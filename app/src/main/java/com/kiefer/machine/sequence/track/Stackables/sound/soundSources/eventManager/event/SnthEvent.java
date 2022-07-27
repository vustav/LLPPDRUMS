package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.eventManager.event;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.oscillatorManager.OscillatorManager;

import nl.igorski.mwengine.core.SynthEvent;
import nl.igorski.mwengine.core.SynthInstrument;

public class SnthEvent extends Event{
    private final LLPPDRUMS llppdrums;
    private final OscillatorManager oscillatorManager;
    private final SynthEvent[] events;

    public SnthEvent(LLPPDRUMS llppdrums, Step step, OscillatorManager oscillatorManager){
        this.llppdrums = llppdrums;
        this.oscillatorManager = oscillatorManager;

        events = new SynthEvent[oscillatorManager.getOscillators().length];
        for (int oscNo = 0; oscNo < oscillatorManager.getOscillators().length; oscNo++) {
            SynthEvent event = new SynthEvent(1000, step.getStepNo(), DURATION, (SynthInstrument)oscillatorManager.getInstrument(oscNo));
            events[oscNo] = event;

            /** The way events work now is that events are added to the sequencer on creation and then never removed, just enabled/disabled **/
            event.addToSequencer();
            event.setEnabled(false);
        }

    }
/*
    private void calculateBuffers(){
        for (SynthEvent event : events) {
            event.calculateBuffers();
        }
    }

 */

    @Override
    public void addToSequencer(){
        //Log.e("SnthEvent", "addToSequencer()");
        for (SynthEvent event : events) {
            //event.addToSequencer();
            event.setEnabled(true);
        }
    }

    @Override
    public void removeFromSequencer(){
        //Log.e("SnthEvent", "removeFromSequencer()");
        for (SynthEvent event : events) {
            //event.removeFromSequencer();
            event.setEnabled(false);
        }
    }

    @Override
    public void positionEvent(int posInSamples){
        for (SynthEvent event : events) {
            event.setEventStart(posInSamples);
        }
    }

    @Override
    public void setPitch(float pitch){
        for (int oscNo = 0; oscNo < oscillatorManager.getOscillators().length; oscNo++) {
            events[oscNo].setFrequency(oscillatorManager.getOscillatorPitchLog(oscNo) * pitch);
        }
    }

    @Override
    public void setVolume(float volume){
        for (int oscNo = 0; oscNo < oscillatorManager.getOscillators().length; oscNo++) {
            events[oscNo].setVolume(oscillatorManager.getOscillatorVolume(oscNo) * volume);
        }
    }

    @Override
    public void delete(){
        for (SynthEvent event : events) {
            llppdrums.getDeleter().addEvent(event);
        }
    }
}
