package com.kiefer.files.keepers;

import java.net.PortUnreachableException;
import java.util.ArrayList;

public class DrumMachineKeeper implements Keeper {
    public int selectedSequence, initTempo, initSteps;
    //public int playingSequence; //spara in seqManager ist
    public ArrayList<DrumSequenceKeeper> sequenceKeepers;
    public SequenceManagerKeeper sequenceManagerKeeper;

    public DrumMachineKeeper(){}
}
