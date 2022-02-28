package com.kiefer.machine.sequence.track.soundManager.eventManager.event;

import android.util.Log;

import com.kiefer.files.keepers.soundSources.EventsKeeper;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.track.soundManager.eventManager.Sub;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;

import java.util.Random;

import nl.igorski.mwengine.core.SynthEvent;
import nl.igorski.mwengine.core.SynthInstrument;

public class SnthEvent extends Event{
    private final OscillatorManager oscillatorManager;
    private final SynthEvent[] events;
    private final Step step;
    private final Sub sub;

    public SnthEvent(Step step, Sub sub, OscillatorManager oscillatorManager){
        this.oscillatorManager = oscillatorManager;
        this.step = step;
        this.sub = sub;

        if(step.getStepNo() == 0) {
            //Log.e("SnthEvent", "constr");
        }

        events = new SynthEvent[oscillatorManager.getOscillators().length];
        for (int oscNo = 0; oscNo < oscillatorManager.getOscillators().length; oscNo++) {
            SynthEvent event = new SynthEvent(1000, step.getStepNo(), DURATION, (SynthInstrument)oscillatorManager.getInstrument(oscNo));
            events[oscNo] = event;
        }

        /*
        if(step.isOn() && sub.isOn()){
            addToSequencer();
        }

        calculateBuffers();

         */
    }

    private void calculateBuffers(){
        for (SynthEvent event : events) {
            event.calculateBuffers();
        }
    }

    @Override
    public void addToSequencer(){
        if(step.getStepNo() == 0) {
            Log.e("SnthEvent", "addToSequencer(), sub: "+sub.getIndex());
        }
        //Random r = new Random();
        //Log.e("Snth", "add"+r.nextInt());
        for (SynthEvent event : events) {
            event.addToSequencer();
        }
    }

    @Override
    public void removeFromSequencer(){
        for (SynthEvent event : events) {
            event.removeFromSequencer();
        }
    }

    @Override
    public void positionEvent(int posInSamples){
        for (SynthEvent event : events) {
            event.setEventStart(posInSamples);
        }
        //calculateBuffers();

        //event[oscNo].invalidateProperties(step, DURATION, (SynthInstrument)oscillatorManager.getInstrument(oscNo));
        //event[oscNo].setEventStart(posInSamples);
        //event[oscNo].setEventEnd(samplesPerSub);
        //event[oscNo].calculateBuffers();
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
            //.delete() causes nullPointer-crashes, use this instead
            //event.setDeletable(true);
            event.delete();
        }
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
