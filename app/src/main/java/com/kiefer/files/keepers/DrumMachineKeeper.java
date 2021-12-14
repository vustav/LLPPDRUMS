package com.kiefer.files.keepers;

import java.util.ArrayList;

public class DrumMachineKeeper implements Keeper {
    public int initTempo;
    public int initSteps;
    public ArrayList<DrumSequenceKeeper> sequenceKeepers;
    public SequenceManagerKeeper sequenceManagerKeeper;

    public DrumMachineKeeper(){}
}
