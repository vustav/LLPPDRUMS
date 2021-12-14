package com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets;

import java.util.ArrayList;

public abstract class SoundSourcePreset {
    public static final String BASS = "BASS", HH = "HH", SNARE = "SNARE", TOM = "TOM", MISC = "MISC";

    public abstract String getName();

    public static ArrayList<String> getCategories(){
        ArrayList<String> categories = new ArrayList<>();
        categories.add(BASS);
        categories.add(HH);
        categories.add(SNARE);
        categories.add(TOM);
        return categories;
    }
}
