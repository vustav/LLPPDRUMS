package com.kiefer.files.keepers.rndSeqManager;

import com.kiefer.files.keepers.Keeper;

import java.util.ArrayList;

public class RndSeqPresetStepKeeper implements Keeper {
    public String minPan, maxPan;
    public ArrayList<RndSeqPresetSubKeeper> rndSeqPresetSubKeepers;
}
