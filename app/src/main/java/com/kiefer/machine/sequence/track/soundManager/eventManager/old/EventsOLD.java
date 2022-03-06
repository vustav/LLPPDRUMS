package com.kiefer.machine.sequence.track.soundManager.eventManager.old;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.soundSources.EventsKeeperOLD;
import com.kiefer.files.keepers.soundSources.SoundEventsKeeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.utils.NmbrUtils;

import java.util.ArrayList;
import java.util.Random;

/** Abstract class for the holders of AudioEvents -> SynthEvent and SampleEvent.
 *
 * The Event-class inside is for the actual Synth or SampleEvents. oscEvents and SmplEvents create
 * and keep treack of them**/

public abstract class EventsOLD {
    protected final LLPPDRUMS llppdrums;

    protected float DURATION = 1f;

    protected ArrayList<Event> events;

    protected final DrumSequence drumSequence;
    private final DrumTrack drumTrack;
    protected int step;

    private boolean on = false;

    /** ABSTRACT METHODS **/
    public abstract void addEvent(boolean addToSequencer, boolean on);

    /** CONSTR **/
    public EventsOLD(LLPPDRUMS llppdrums, DrumSequence drumSequence, DrumTrack drumTrack, int step){
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.drumTrack = drumTrack;
        this.step = step;
    }

    protected void createEvents(int nOfSteps, int subs, boolean addToSequencer){
        events = new ArrayList<>();

        Random r = new Random();

        for(int sub = 0; sub < subs; sub++){
            addEvent(addToSequencer, r.nextInt(sub + 1) == 0);
            //addEvent(addToSequencer, false);
        }
        positionEvents(nOfSteps, step);
    }

    /** ONOFF **/
    public void turnOn(int nOfSteps, int step){
        on = true;
        if(events.size() > 0) {
            for(Event e : events) {
                if(e.isOn()) {
                    e.addToSequencer();
                }
            }
        }
    }

    public void turnOff(){
        on = false;
        if(events != null) {
            for (Event e : events) {
                if (e != null) {
                    // behövs if(se.on)??
                    e.removeFromSequencer();
                }
            }
        }
    }

    public void setSubOn(int sub, boolean on){
        events.get(sub).setOn(on);
    }

    public boolean isSubOn(int sub){
        return events.get(sub).isOn();
    }

    /** POSITIONING **/
    public void positionEvents(int nOfSteps, int step){
        this.step = step;
        int samplesPerStep = getSamplesPerStep(nOfSteps);

        int posInSamples = getPosInSamples(samplesPerStep);
        int samplesPerSub = getSamplesPerSub(samplesPerStep);

        if(events.size() > 0) {
            for(Event e : events){
                e.positionEvent(posInSamples);
                posInSamples += samplesPerSub;
            }
        }
    }

    /** SOUNDS **/
    public void updateSound(){
        if(events.size() > 0) {
            for (Event se : events) {
                se.updateSound();
            }
        }
    }

    /** RND **/
    public void randomizeOn(boolean autoRnd, int sub) {
        events.get(sub).randomizeOn(autoRnd);
    }
    public void randomizeSubsOn(boolean autoRnd) {
        for(Event e : events) {
            e.randomizeOn(autoRnd);
        }
    }
    public void randomizeVol(boolean autoRnd, int sub){
        events.get(sub).randomizeVol(autoRnd);
    }
    public void randomizeVols(boolean autoRnd){
        for(Event e : events){
            e.randomizeVol(autoRnd);
        }
    }
    public void randomizePitch(boolean autoRnd, int sub){
        events.get(sub).randomizePitch(autoRnd);
    }
    public void randomizePitches(boolean autoRnd){
        for(Event e : events){
            e.randomizePitch(autoRnd);
        }
    }

    /** GET **/
    public int getNOfSubs(){
        return events.size();
    }

    protected int getSamplesPerStep(int nOfSteps){
        return ((60/(drumSequence.getTempo()/llppdrums.getEngineFacade().getBEAT_AMOUNT())) * llppdrums.getEngineFacade().getSAMPLE_RATE()) / nOfSteps;
    }

    protected int getPosInSamples(int samplesPerStep){
        return samplesPerStep * step;
    }

    protected int getSamplesPerSub(int samplesPerStep){
        return samplesPerStep / drumTrack.getNOfSubs();
    }
    public float getVolumeModifier(int sub) {
        return events.get(sub).getVolumeModifier();
    }
    public float getPitchModifier(int sub) {
        return events.get(sub).getPitchModifier();
    }
    public float getPan() {
        return pan;
    }

    public boolean getAutoRndOn(int sub){
        return events.get(sub).getAutoRndOn();
    }

    public boolean getRndOnReturn(int sub) {
        return events.get(sub).getRndOnReturn();
    }

    public float getRndOnPerc(int sub) {
        return events.get(sub).getRndOnPerc();
    }

    public boolean getAutoRndVol(int sub){
        return events.get(sub).getAutoRndVol();
    }

    public float getRndVolMin(int sub) {
        return events.get(sub).getRndVolMin();
    }

    public float getRndVolMax(int sub) {
        return events.get(sub).getRndVolMax();
    }

    public float getRndVolPerc(int sub) {
        return events.get(sub).getRndVolPerc();
    }

    public boolean getRndVolReturn(int sub){
        return events.get(sub).getRndVolReturn();
    }

    public boolean getAutoRndPitch(int sub){
        return events.get(sub).getAutoRndPitch();
    }

    public float getRndPitchMin(int sub) {
        return events.get(sub).getRndPitchMin();
    }

    public float getRndPitchMax(int sub) {
        return events.get(sub).getRndPitchMax();
    }

    public float getRndPitchPerc(int sub) {
        return events.get(sub).getRndPitchPerc();
    }

    public boolean getRndPitchReturn(int sub){
        return events.get(sub).getRndPitchReturn();
    }

    /** SET **/
    public void setNOfSubs(int nOfSteps, int subs, boolean addToSequencer){
        Random r = new Random();
        while (events.size() < subs) {
            addEvent(addToSequencer, r.nextInt(events.size()) == 0);
        }
        while (events.size() > subs){
            deleteEvent();
        }
    }

    /*
    public void setVolume(float volume){
        //this.volume = volume;
        if(events.size() > 0) {
            for(Event se : events) {
                se.setVolume(volume);
            }
        }
    }

    public void setPitch(float pitch){
        //this.pitch = pitch;
        if(events.size() > 0) {
            for(Event se : events) {
                se.setPitch(pitch);
            }
        }
    }

     */

    //vol
    public void setVolumeModifier(float modifier, int sub){
        //volumeModifier = modifier;
        //updateEventVolume();
        events.get(sub).setVolumeModifier(modifier);
    }

    public void updateEventVolume(){
        for(Event e : events){
            e.updateEventVolume();
        }
        //events.get(sub).updateEventVolume();
    }

    //pitch
    public void setPitchModifier(float modifier, int sub){
        //pitchModifier = modifier;
        //updateEventPitches();
        events.get(sub).setPitchModifier(modifier);
    }

    public void updateEventPitches(){
        //soundEvents.setPitch(getConvertedPitchModifier());
        for(Event e : events){
            e.updateEventPitch();
        }
        //events.get(sub).updateEventPitch();
    }

    /*
    private float getConvertedPitchModifier(){
        // pitchModifier in drum is a float 0-1, change to a corresponding value between .5 and 2 (octave down to octave up)
        float minPitchModifier = .5f;
        float maxPitchModifier = 2f;
        return  getPitchModifier() * (maxPitchModifier - minPitchModifier) + minPitchModifier;
    }

     */

    //pan
    public void setPan(float pan){
        this.pan = pan;
    }

    //autorandom
    /*
    public void setAutoRndOn(boolean on){
        autoRndOn = on;
    }

     */

    public void setRndOnPerc(float rndOnPerc, int sub) {
        //this.rndOnPerc = rndOnPerc;
        events.get(sub).setRndOnPerc(rndOnPerc);
    }

    public void setRndOnReturn(boolean rndOnReturn, int sub) {
        //this.rndOnReturn = rndOnReturn;
        events.get(sub).setRndOnReturn(rndOnReturn);
    }
/*
    public void setAutoRndVol(boolean on){
        autoRndVol = on;
    }

 */

    public void setRndVolMin(float rndVolMin, int sub) {
        //this.rndVolMin = rndVolMin;
        events.get(sub).setRndVolMin(rndVolMin);
    }

    public void setRndVolMax(float rndVolMax, int sub) {
        //this.rndVolMax = rndVolMax;
        events.get(sub).setRndVolMax(rndVolMax);
    }

    public void setRndVolPerc(float rndVolPerc, int sub) {
        //this.rndVolPerc = rndVolPerc;
        events.get(sub).setRndVolPerc(rndVolPerc);
    }

    public void setRndVolReturn(boolean rndVolReturn, int sub) {
        //this.rndVolReturn = rndVolReturn;
        events.get(sub).setRndVolReturn(rndVolReturn);
    }
/*
    public void setAutoRndPitch(boolean on){
        autoRndPitch = on;
    }

 */

    public void setRndPitchMin(float rndPitchMin, int sub) {
        //this.rndPitchMin = rndPitchMin;
        events.get(sub).setRndPitchMin(rndPitchMin);
    }

    public void setRndPitchMax(float rndPitchMax, int sub) {
        //this.rndPitchMax = rndPitchMax;
        events.get(sub).setRndPitchMax(rndPitchMax);
    }

    public void setRndPitchPerc(float rndVolPerc, int sub) {
        //this.rndPitchPerc = rndVolPerc;
        events.get(sub).setRndPitchPerc(rndVolPerc);
    }

    public void setRndPitchReturn(boolean rndVolReturn, int sub) {
        //this.rndPitchReturn = rndVolReturn;
        events.get(sub).setRndPitchReturn(rndVolReturn);
    }
/*
    public void setAutoRndPan(boolean on){
        autoRndPan = on;
    }

 */

    /** PAN **/
    private float pan;
    private float rndPanMin, rndPanMax, rndPanPerc;
    private boolean rndPanReturn;
    public void randomizePan(boolean autoRnd){
        Random random = new Random();
        if(autoRnd){
            if(rndPanPerc >= random.nextFloat()) {
                setPan(NmbrUtils.getRndmizer(rndPanMin, rndPanMax));
            }
        }
        else{
            setPan(random.nextFloat());
        }
    }

    public boolean getAutoRndPan(){
        return rndPanPerc > 0;
    }

    public float getRndPanMin() {
        return rndPanMin;
    }

    public float getRndPanMax() {
        return rndPanMax;
    }

    public float getRndPanPerc() {
        return rndPanPerc;
    }

    public boolean getRndPanReturn(){
        return rndPanReturn;
    }

    /** RESET **/
    public void reset(){
        setRndPanPerc(0);
        for(Event e : events) {
            e.setRndOnPerc(0);
            e.setRndVolPerc(0);
            e.setRndPitchPerc(0);
        }
    }

    public void setRndPanMin(float rndPitchMin) {
        this.rndPanMin = rndPitchMin;
    }

    public void setRndPanMax(float rndVolMax) {
        this.rndPanMax = rndVolMax;
    }

    public void setRndPanPerc(float rndVolPerc) {
        this.rndPanPerc = rndVolPerc;
    }

    public void setRndPanReturn(boolean rndVolReturn) {
        this.rndPanReturn = rndVolReturn;
    }

    /** DELETE **/
    public void deleteEvents(){
        if(events != null) {
            if (events.size() > 0) {
                for (int i = events.size() - 1; i >= 0; i--) {
                    deleteEvent(events.get(i));
                }
            }
        }
    }

    private void deleteEvent(Event event){
        events.remove(event);
        event.delete();
    }

    public void deleteEvent(){
        deleteEvent(events.get(events.size()-1));
    }

    /** DESTRUCTION **/
    public void destroy(){
        turnOff();
        deleteEvents();
        events = null;
    }

    /** RESTORE **/
    public abstract SoundEventsKeeper getKeeper();
    public void restore(SoundEventsKeeper keeper){
        for(int i = 0; i < events.size(); i++){
            events.get(i).restore(keeper.eventsKeepers.get(i));
        }
    }

    /** CLASS EVENT **/
    protected abstract class Event{
        private boolean on;
        private float volumeModifier, pitchModifier;

        //rnd
        private Random random;

        private float rndOnPerc;
        private boolean rndOnReturn;

        private float rndVolMin, rndVolMax, rndVolPerc;
        private boolean rndVolReturn;

        private float rndPitchMin, rndPitchMax, rndPitchPerc;
        private boolean rndPitchReturn;

        //abstract methods
        public abstract void addToSequencer();
        public abstract void removeFromSequencer();
        public abstract void updateSound();
        public abstract void positionEvent(int posInSamples);
        public abstract void setPitch(float pitch);
        public abstract void setVolume(float volume);
        public abstract void delete();

        public Event(boolean on){
            this.on = on;
            random = new Random();

            setupAutoRndParams();
        }

        private void setupAutoRndParams(){
            //autoRndOn = false;
            rndOnPerc = 0;
            rndOnReturn = false;

            //autoRndVol = false;
            rndVolPerc = 0;
            rndVolMin = NmbrUtils.getRndmizer(0, .2f);
            rndVolMax = NmbrUtils.getRndmizer(.7f, 1);
            rndVolReturn = false;

            //autoRndPan = false;
            rndPanPerc = 0;
            rndPanMin = NmbrUtils.getRndmizer(0, .2f);
            rndPanMax = NmbrUtils.getRndmizer(.7f, 1);
            rndPanReturn = false;

            //autoRndPitch = false;
            rndPitchPerc = 0;
            rndPitchMin = NmbrUtils.getRndmizer(0, .2f);
            rndPitchMax = NmbrUtils.getRndmizer(.7f, 1);
            rndPitchReturn = false;
        }

        /** ONOFF **/
        public boolean isOn(){
            return on;
        }

        public void setOn(boolean on){
            this.on = on;

            if(on) {
                if (llppdrums.getDrumMachine().getPlayingSequence() == drumSequence && EventsOLD.this.on) {
                    addToSequencer();
                }
            }
            else{
                removeFromSequencer();
            }
        }

        /** RND **/
        public void randomizeOn(boolean autoRnd) {
            if(autoRnd) {
                float r = random.nextFloat();
                if (rndOnPerc >= r) {
                    setOn(random.nextInt(2) == 1);
                }
            }
            else{
                setOn(random.nextInt(2) == 1);
            }
        }
        public void randomizeVol(boolean autoRnd){
            if(autoRnd){
                if(rndVolPerc >= random.nextFloat()) {
                    setVolumeModifier(NmbrUtils.getRndmizer(rndVolMin, rndVolMax));
                }
            }
            else{
                setVolumeModifier(random.nextFloat());
            }
        }
        public void randomizePitch(boolean autoRnd){
            if(autoRnd){
                if(rndPitchPerc >= random.nextFloat()) {
                    setPitchModifier(NmbrUtils.getRndmizer(rndPitchMin, rndPitchMax));
                }
            }
            else{
                setPitchModifier(random.nextFloat());
            }
        }


        /** GET **/
        public float getPitchModifier() {
            return pitchModifier;
        }

        public float getVolumeModifier() {
            return volumeModifier;
        }

        public float getPan() {
            return pan;
        }
        public boolean getAutoRndOn(){
            return rndOnPerc > 0;
        }

        public boolean getRndOnReturn() {
            return rndOnReturn;
        }

        public float getRndOnPerc() {
            return rndOnPerc;
        }

        public boolean getAutoRndVol(){
            return rndVolPerc > 0;
        }


        public float getRndVolMin() {
            return rndVolMin;
        }

        public float getRndVolMax() {
            return rndVolMax;
        }

        public float getRndVolPerc() {
            return rndVolPerc;
        }

        public boolean getRndVolReturn(){
            return rndVolReturn;
        }

        public boolean getAutoRndPitch(){
            return rndPitchPerc > 0;
        }

        public float getRndPitchMin() {
            return rndPitchMin;
        }

        public float getRndPitchMax() {
            return rndPitchMax;
        }

        public float getRndPitchPerc() {
            return rndPitchPerc;
        }

        public boolean getRndPitchReturn(){
            return rndPitchReturn;
        }

        /** SET **/
        //pitch
        public void setVolumeModifier(float modifier){
            volumeModifier = modifier;
            updateEventVolume();
        }

        public void updateEventVolume(){
            //soundEvents.setPitch(getConvertedPitchModifier());
            setVolume(drumTrack.getTrackVolume() * getVolumeModifier());
        }


        //pitch
        public void setPitchModifier(float modifier){
            pitchModifier = modifier;
            updateEventPitch();
        }

        public void updateEventPitch(){
            setPitch(getConvertedPitchModifier());
        }

        private float getConvertedPitchModifier(){
            // pitchModifier in drum is a float 0-1, change to a corresponding value between .5 and 2 (octave down to octave up)
            float minPitchModifier = .5f;
            float maxPitchModifier = 2f;
            return  getPitchModifier() * (maxPitchModifier - minPitchModifier) + minPitchModifier;
        }

        //autorandom
    /*
    public void setAutoRndOn(boolean on){
        autoRndOn = on;
    }

     */

        public void setRndOnPerc(float rndOnPerc) {
            this.rndOnPerc = rndOnPerc;
        }

        public void setRndOnReturn(boolean rndOnReturn) {
            this.rndOnReturn = rndOnReturn;
        }
/*
    public void setAutoRndVol(boolean on){
        autoRndVol = on;
    }

 */

        public void setRndVolMin(float rndVolMin) {
            this.rndVolMin = rndVolMin;
        }

        public void setRndVolMax(float rndVolMax) {
            this.rndVolMax = rndVolMax;
        }

        public void setRndVolPerc(float rndVolPerc) {
            this.rndVolPerc = rndVolPerc;
        }

        public void setRndVolReturn(boolean rndVolReturn) {
            this.rndVolReturn = rndVolReturn;
        }
/*
    public void setAutoRndPitch(boolean on){
        autoRndPitch = on;
    }

 */

        public void setRndPitchMin(float rndPitchMin) {
            this.rndPitchMin = rndPitchMin;
        }

        public void setRndPitchMax(float rndPitchMax) {
            this.rndPitchMax = rndPitchMax;
        }

        public void setRndPitchPerc(float rndVolPerc) {
            this.rndPitchPerc = rndVolPerc;
        }

        public void setRndPitchReturn(boolean rndVolReturn) {
            this.rndPitchReturn = rndVolReturn;
        }
/*
    public void setAutoRndPan(boolean on){
        autoRndPan = on;
    }

 */

        /** RESTORATION **/
        public void restore(EventsKeeperOLD k){
            setVolumeModifier(Float.parseFloat(k.volumeModifier));
            setPan(Float.parseFloat(k.pan));
            setPitchModifier(Float.parseFloat(k.pitchModifier));

            //setAutoRndOn(k.autoRndOn);
            setRndOnPerc(Float.parseFloat(k.rndOnPerc));
            setRndOnReturn(k.rndOnReturn);

            //setAutoRndVol(k.autoRndVol);
            setRndVolMin(Float.parseFloat(k.rndVolMin));
            setRndVolMax(Float.parseFloat(k.rndVolMax));
            setRndVolPerc(Float.parseFloat(k.rndVolPerc));
            setRndVolReturn(k.rndVolReturn);

            //setAutoRndPan(k.autoRndPan);
            setRndPanMin(Float.parseFloat(k.rndPanMin));
            setRndPanMax(Float.parseFloat(k.rndPanMax));
            setRndPanPerc(Float.parseFloat(k.rndPanPerc));
            setRndPanReturn(k.rndPanReturn);

            //setAutoRndPitch(k.autoRndPitch);
            setRndPitchMin(Float.parseFloat(k.rndPitchMin));
            setRndPitchMax(Float.parseFloat(k.rndPitchMax));
            setRndPitchPerc(Float.parseFloat(k.rndPitchPerc));
            setRndPitchReturn(k.rndPitchReturn);

            updateSound();
        }

        public EventsKeeperOLD getKeeper(){
            EventsKeeperOLD keeper = new EventsKeeperOLD();
            keeper.volumeModifier = Float.toString(getVolumeModifier());
            keeper.pan = Float.toString(getPan());
            keeper.pitchModifier = Float.toString(getPitchModifier());

            //keeper.autoRndOn = getAutoRndOn();
            keeper.rndOnPerc = Float.toString(getRndOnPerc());
            keeper.rndOnReturn = getRndOnReturn();

            //keeper.autoRndVol = getAutoRndVol();
            keeper.rndVolMin = Float.toString(getRndVolMin());
            keeper.rndVolMax = Float.toString(getRndVolMax());
            keeper.rndVolPerc = Float.toString(getRndVolPerc());
            keeper.rndVolReturn = getRndVolReturn();

            //keeper.autoRndPan = getAutoRndPan();
            keeper.rndPanMin = Float.toString(getRndPanMin());
            keeper.rndPanMax = Float.toString(getRndPanMax());
            keeper.rndPanPerc= Float.toString(getRndPanPerc());
            keeper.rndPanReturn = getRndPanReturn();

            //keeper.autoRndPitch = getAutoRndPitch();
            keeper.rndPitchMin = Float.toString(getRndPitchMin());
            keeper.rndPitchMax = Float.toString(getRndPitchMax());
            keeper.rndPitchPerc = Float.toString(getRndPitchPerc());
            keeper.rndPitchReturn = getRndPitchReturn();

            return keeper;
        }
    }
}
