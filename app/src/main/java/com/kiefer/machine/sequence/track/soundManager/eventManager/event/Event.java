package com.kiefer.machine.sequence.track.soundManager.eventManager.event;

public abstract class Event {

    protected float DURATION = 1f;

    //abstract methods
    public abstract void addToSequencer();
    public abstract void removeFromSequencer();
    public abstract void positionEvent(int posInSamples);
    public abstract void setPitch(float pitch);
    public abstract void setVolume(float volume);
    public abstract void delete();
}