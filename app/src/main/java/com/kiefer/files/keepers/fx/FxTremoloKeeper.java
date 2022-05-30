package com.kiefer.files.keepers.fx;

import com.kiefer.files.keepers.AutomationManagerKeeper;

public class FxTremoloKeeper extends FxKeeper {
    public int leftAttack, leftDecay, rightAttack, rightDecay;

    public FxTremoloKeeper(int fxIndex, boolean on, AutomationManagerKeeper automationManagerKeeper){
        super(fxIndex, on, automationManagerKeeper);
    }
}
