package com.kiefer.files.keepers;

import java.util.ArrayList;

public class SequenceManagerKeeper implements Keeper {
    public boolean on, progress, randomizeProgress, queue,restartAtStop;
    public ArrayList<Integer> seqs;
    public int nOfActiveBoxes, activeSequenceBoxIndex;
}
