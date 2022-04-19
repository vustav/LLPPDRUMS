package com.kiefer;

import android.util.Log;

import com.kiefer.engine.EngineFacade;

import java.util.ArrayList;

import nl.igorski.mwengine.core.BaseAudioEvent;
import nl.igorski.mwengine.core.BaseInstrument;
import nl.igorski.mwengine.core.BaseProcessor;

public class Deleter {
    private final EngineFacade engineFacade;
    private ArrayList <BaseAudioEvent> events = new ArrayList<>();
    private ArrayList <BaseProcessor> fxs = new ArrayList<>();
    private ArrayList <BaseInstrument> instruments = new ArrayList<>();

    private final boolean delayDeletion = true;

    public Deleter(EngineFacade engineFacade){
        this.engineFacade = engineFacade;
    }

    public void addFx(BaseProcessor fx){
        Log.e("Deleter", "addFx()");
        if(delayDeletion) {
            fxs.add(fx);
        }
        else{
            pauseEngine();
            fx = null;
            playEngine();
        }
    }

    public void addEvent(BaseAudioEvent event){
        Log.e("Deleter", "addEvent()");
        if(delayDeletion) {
            events.add(event);
        }
        else{
            pauseEngine();
            event.delete();
            playEngine();
        }
    }

    public void addInstrument(BaseInstrument instrument){
        Log.e("Deleter", "addInstrument()");
        if(delayDeletion) {
            instruments.add(instrument);
        }
        else{
            pauseEngine();
            instrument.delete();
            playEngine();
        }
    }

    public void delete(){
        Log.e("Deleter", "delete()");
        if(delayDeletion) {
            if (!engineFacade.isPlaying()) {
                for (BaseProcessor fx : fxs) {
                    // KRASCHAR
                    //fx.delete();

                    //STÅR SÅHÄR I EXEMPLET FÖR FXS
                    // and these (garbage collection invokes native layer destructors, so we'll let
                    // these processors be cleared lazily)
                    fx = null;
                }
                for (BaseAudioEvent event : events) {
                    event.delete();
                }
                for (BaseInstrument instrument : instruments) {
                    instrument.delete();
                }
                fxs = new ArrayList<>();
                events = new ArrayList<>();
                instruments = new ArrayList<>();
            }
        }
    }

    private boolean wasPlaying = false;
    private void pauseEngine(){
        if(engineFacade.isPlaying()){
            wasPlaying = true;
            engineFacade.pauseSequencer();
        }
    }

    private void playEngine(){
        if(wasPlaying){
            wasPlaying = false;
            engineFacade.playSequencer();
        }
    }
}
