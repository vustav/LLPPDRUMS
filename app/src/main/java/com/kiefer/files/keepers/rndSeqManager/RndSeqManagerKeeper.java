package com.kiefer.files.keepers.rndSeqManager;

import com.kiefer.files.keepers.Keeper;

import java.util.ArrayList;

public class RndSeqManagerKeeper implements Keeper {
    public RandomizeSeqPresetKeeper randomizeSeqPresetKeeper;
    public ArrayList<RndSeqPresetTrackKeeper> rndSeqPresetTrackKeepers;
}
