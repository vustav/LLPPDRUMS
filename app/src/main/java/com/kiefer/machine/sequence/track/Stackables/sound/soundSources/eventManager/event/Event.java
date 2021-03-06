package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.eventManager.event;

public abstract class Event {

    protected float DURATION = 1f;

    //abstract methods
    public abstract void enable();
    public abstract void disable();
    public abstract void removeFromSequencer();
    public abstract void positionEvent(int posInSamples);
    public abstract void setPitch(float pitch);
    public abstract void setVolume(float volume);
    public abstract void delete();
}