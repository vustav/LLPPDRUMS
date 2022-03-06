package com.kiefer.files.keepers.soundSources;

import com.kiefer.files.keepers.Keeper;

import java.util.ArrayList;

public class StepEventsManagerKeeper implements Keeper {
    public String pan;
    public ArrayList<SubKeeper> subKeepers;

    //public boolean autoRndPan;
    public String rndPanMin, rndPanMax, rndPanPerc;
    public boolean rndPanReturn;
}
