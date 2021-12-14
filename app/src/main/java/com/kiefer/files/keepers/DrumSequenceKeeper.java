package com.kiefer.files.keepers;

import java.util.ArrayList;

public class DrumSequenceKeeper implements Keeper{
    public int nOfTracks, nOfSteps;
    public String tempo;
    public ArrayList<DrumTrackKeeper> drumTrackKeepers;

    public DrumSequenceKeeper(){
    }
}
