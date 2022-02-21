package com.kiefer.machine.sequence.track.soundManager.eventManager.old;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.soundSources.EventsKeeper;
import com.kiefer.files.keepers.soundSources.SmplEventKeeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.soundManager.sampleManager.SmplManager;

import java.util.ArrayList;

import nl.igorski.mwengine.core.SampleManager;
import nl.igorski.mwengine.core.SampleEvent;

public class SmplEventsOLD extends EventsOLD {
    private final SmplManager smplManager;

    public SmplEventsOLD(LLPPDRUMS llppdrums, DrumSequence drumSequence, DrumTrack drumTrack, SmplManager smplManager, int nOfSteps, int subs, int step, boolean addToSequencer) {
        super(llppdrums, drumSequence, drumTrack, step);
        this.smplManager = smplManager;

        createEvents(nOfSteps, subs, addToSequencer);

        //getting bugs without removing then adding again
        turnOff();
        if(addToSequencer){
            turnOn(nOfSteps, step);
        }
    }

    @Override
    public void addEvent(boolean addToSequencer, boolean on){
        SmplEvent event = new SmplEvent(on);
        events.add(event);

        if(addToSequencer){
            event.addToSequencer();
        }
    }

    /** RESTORE **/
    public SmplEventKeeper getKeeper(){
        SmplEventKeeper keeper = new SmplEventKeeper();

        ArrayList<EventsKeeper> smplEventsKeepers = new ArrayList<>();
        for(Event e : events){
            smplEventsKeepers.add(e.getKeeper());
        }
        keeper.eventsKeepers = smplEventsKeepers;

        return keeper;
    }

    /** CLASS SMPLEVENT **/
    private class SmplEvent extends Event{
        private SampleEvent event;

        private SmplEvent(boolean on){
            super(on);
            event = new SampleEvent(smplManager.getInstrument(0));
            event.setSample(SampleManager.getSample(smplManager.getSelectedCategory().getSelectedSample().getName()));

            randomizeVol(false);
            randomizePitch(false);
            randomizePan(false);
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
        public void updateSound(){
            event.setSample(SampleManager.getSample(smplManager.getSelectedCategory().getSelectedSample().getName()));
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
            //.delete() causes nullPointer-crashes, use this instead
            //event.setDeletable(true);
            event.delete();
        }

        /** RESTORE **/
        @Override
        public EventsKeeper getKeeper(){
            EventsKeeper keeper = super.getKeeper();
            return keeper;
        }
    }
}
