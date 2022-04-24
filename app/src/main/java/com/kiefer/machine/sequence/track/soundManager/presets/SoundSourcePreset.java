package com.kiefer.machine.sequence.track.soundManager.presets;

import java.util.ArrayList;

public abstract class SoundSourcePreset {
    public static final String BASS = "BASS", HHClosed = "HH CLOSED", HHOpen = "HH OPEN", SNARE = "SNARE", TOM = "TOM", RANDOM = "RANDOM";

    public abstract String getName();

    /** These are used by RndSeqManager to make it possible to select presets when randomizing. setPreset() in SoundSource will
     * be called with one of these strings, so make sure to handle it f you want something to happen. **/
    public static ArrayList<String> getCategories(){
        ArrayList<String> categories = new ArrayList<>();
        categories.add(BASS);
        categories.add(SNARE);
        categories.add(HHClosed);
        categories.add(HHOpen);
        categories.add(TOM);
        return categories;
    }
}
