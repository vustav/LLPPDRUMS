package com.kiefer.files.keepers.rndSeqManager;

import com.kiefer.files.keepers.Keeper;

import java.util.ArrayList;

public class RndSeqPresetTrackKeeper implements Keeper {
    public String presetCategory, name;
    public int nOfSteps, nOfSubs;
    public boolean randomizeFx, randomizePan, randomizeVol;
    public ArrayList<RndSeqPresetStepKeeper> rndSeqPresetStepKeepers;
}
