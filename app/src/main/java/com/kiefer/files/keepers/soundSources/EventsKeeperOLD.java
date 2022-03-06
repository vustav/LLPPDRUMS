package com.kiefer.files.keepers.soundSources;

import com.kiefer.files.keepers.Keeper;

public class EventsKeeperOLD implements Keeper {
    public String volumeModifier, pan, pitchModifier;

    //public boolean autoRndOn;
    public String rndOnPerc;
    public boolean rndOnReturn;

    //public boolean autoRndVol;
    public String rndVolMin, rndVolMax, rndVolPerc;
    public boolean rndVolReturn;

    //public boolean autoRndPan;
    public String rndPanMin, rndPanMax, rndPanPerc;
    public boolean rndPanReturn;

    //public boolean autoRndPitch;
    public String rndPitchMin, rndPitchMax, rndPitchPerc;
    public boolean rndPitchReturn;
}
