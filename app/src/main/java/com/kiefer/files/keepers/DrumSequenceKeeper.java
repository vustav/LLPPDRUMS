package com.kiefer.files.keepers;

import java.util.ArrayList;

public class DrumSequenceKeeper implements Keeper{
    public ArrayList<SequenceModuleKeeper> sequenceModuleKeepers;
    public int nOfTracks, nOfSteps, tempo, color, tabIndex, selectedModule;
    public String name;
    public ArrayList<DrumTrackKeeper> drumTrackKeepers;
}
