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

    public Deleter(EngineFacade engineFacade){
        this.engineFacade = engineFacade;
    }

    public void addEvent(BaseAudioEvent event){
        events.add(event);
    }

    public void addFx(BaseProcessor fx){
        fxs.add(fx);
    }

    public void addInstrument(BaseInstrument instrument){
        instruments.add(instrument);
    }

    public void delete(){
        if(!engineFacade.isPlaying()) {
            //Log.e("Deleter", "delete(), events.size(): "+events.size());
            //Log.e("Deleter", "delete(), instruments.size(): "+instruments.size());
            for (BaseProcessor fx : fxs) {
                fx.delete();
            }
            for (BaseAudioEvent event : events) {
                event.delete();
            }
            for (BaseInstrument instrument : instruments) {
                instrument.delete();
            }
            events = new ArrayList<>();
            instruments = new ArrayList<>();
        }
    }
}
