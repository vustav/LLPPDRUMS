package com.kiefer.files.keepers.fx;

import com.kiefer.files.keepers.AutomationManagerKeeper;

public class FxDecimatorKeeper extends FxKeeper {
    public int bits;
    public String rate;

    public FxDecimatorKeeper(int fxIndex, boolean on, AutomationManagerKeeper automationManagerKeeper){
        super(fxIndex, on, automationManagerKeeper);
    }
}
