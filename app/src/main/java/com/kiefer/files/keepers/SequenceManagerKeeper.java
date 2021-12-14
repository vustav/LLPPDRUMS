package com.kiefer.files.keepers;

import java.util.ArrayList;

public class SequenceManagerKeeper implements Keeper {
    public boolean progress, randomizeProgress, queue,restartAtStop;
    public ArrayList<String> seqs;
    public int nOfActiveBoxes;
}
