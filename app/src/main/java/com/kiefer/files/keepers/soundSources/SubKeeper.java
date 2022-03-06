package com.kiefer.files.keepers.soundSources;

import com.kiefer.files.keepers.Keeper;

import java.util.ArrayList;

public class SubKeeper implements Keeper {
    public boolean on;
    public String volumeModifier, pitchModifier;

    public String rndOnPerc;
    public boolean rndOnReturn;

    public String rndVolMin, rndVolMax, rndVolPerc;
    public boolean rndVolReturn;

    public String rndPitchMin, rndPitchMax, rndPitchPerc;
    public boolean rndPitchReturn;
}
