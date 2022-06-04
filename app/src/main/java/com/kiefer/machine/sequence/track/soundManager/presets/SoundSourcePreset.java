package com.kiefer.machine.sequence.track.soundManager.presets;

import java.util.ArrayList;

public abstract class SoundSourcePreset {
    public static final String BASS = "KICK", HHClosed = "HH CLOSED", HHOpen = "HH OPEN", CRASH = "CRASH", RIDE = "RIDE", SNARE = "SNARE", CLAP ="CLAP", TOM = "TOM", HANDDRUMS = "HAND DRUMS", RANDOM = "RANDOM";

    public abstract String getName();

    /** These are used by RndSeqManager to make it possible to select presets when randomizing. setPreset() in SoundSource will
     * be called with one of these strings, so make sure to handle it f you want something to happen. **/
    public static ArrayList<String> getCategories(){
        ArrayList<String> categories = new ArrayList<>();
        categories.add(BASS);
        categories.add(SNARE);
        categories.add(CLAP);
        categories.add(HHClosed);
        categories.add(HHOpen);
        categories.add(TOM);
        categories.add(CRASH);
        categories.add(RIDE);
        //categories.add(HANDDRUMS);
        return categories;
    }
}
