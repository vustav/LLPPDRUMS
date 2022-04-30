package com.kiefer.files.keepers;

import java.util.ArrayList;

public class DrumSequenceKeeper implements Keeper{
    public int nOfTracks, nOfSteps, tempo, color;
    public String name;
    public ArrayList<DrumTrackKeeper> drumTrackKeepers;

    public DrumSequenceKeeper(){
    }
}
