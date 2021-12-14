package com.kiefer.machine.sequence.track.soundManager.events;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.soundSources.EventsKeeper;
import com.kiefer.files.keepers.soundSources.OscEventKeeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.OscillatorManager;

import java.util.ArrayList;

import nl.igorski.mwengine.core.SynthEvent;
import nl.igorski.mwengine.core.SynthInstrument;

public class OscEvents extends SoundEvents {
    private final OscillatorManager oscillatorManager;
    //private ArrayList<SnthEvent> events;

    public OscEvents(LLPPDRUMS llppdrums, DrumSequence drumSequence, DrumTrack drumTrack, OscillatorManager oscillatorManager, int nOfSteps, int subs, int step, boolean addToSequencer) {
        super(llppdrums, drumSequence, drumTrack, step);
        this.oscillatorManager = oscillatorManager;

        createEvents(nOfSteps, subs, addToSequencer);

        //getting bugs without removing then adding again
        turnOff();
        if(addToSequencer){
            turnOn(nOfSteps, step);
        }
    }

    @Override
    public void addEvent(boolean addToSequencer, boolean on){
        SnthEvent event = new SnthEvent(on);
        events.add(event);

        if(addToSequencer){
            event.addToSequencer();
        }
    }

    /** RESTORE **/
    public OscEventKeeper getKeeper(){
        OscEventKeeper keeper = new OscEventKeeper();

        ArrayList<EventsKeeper> oscEventsKeepers = new ArrayList<>();
        for(Event e : events){
            oscEventsKeepers.add(e.getKeeper());
        }
        keeper.eventsKeepers = oscEventsKeepers;

        return keeper;
    }

    /** CLASS SNTHEVENT **/
    private class SnthEvent extends Event{
        private SynthEvent[] events;

        private SnthEvent(boolean on){
            super(on);
            events = new SynthEvent[oscillatorManager.getOscillators().length];
            for (int oscNo = 0; oscNo < oscillatorManager.getOscillators().length; oscNo++) {
                SynthEvent event = new SynthEvent(1000, step, DURATION, (SynthInstrument)oscillatorManager.getInstrument(oscNo));
                events[oscNo] = event;
            }

            randomizeVol(false);
            randomizePitch(false);
            randomizePan(false);
        }

        private float getConvertedPitchModifier(float pitchModifier){
            // pitchModifier in drum is a float 0-1, change to a corresponding value between .5 and 2 (octave down to octave up)
            float minPitchModifier = .5f;
            float maxPitchModifier = 2f;
            return pitchModifier * (maxPitchModifier - minPitchModifier) + minPitchModifier;
        }

        @Override
        public void addToSequencer(){
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
        public void updateSound(){
            //sounds are set in the instruments
        }

        @Override
        public void positionEvent(int posInSamples){
            for (SynthEvent event : events) {
                event.setEventStart(posInSamples);
            }

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
                event.setDeletable(true);
            }
        }

        /** RESTORE **/
        @Override
        public EventsKeeper getKeeper(){
            EventsKeeper keeper = super.getKeeper();
            return keeper;
        }
    }
}
