package com.kiefer.files.keepers.fx;

import com.kiefer.files.keepers.AutomationManagerKeeper;

public class FxBitCrusherKeeper extends FxKeeper {
    public String amount, inMix, outMix;

    public FxBitCrusherKeeper(int fxIndex, boolean on, AutomationManagerKeeper automationManagerKeeper){
        super(fxIndex, on, automationManagerKeeper);
    }
}
