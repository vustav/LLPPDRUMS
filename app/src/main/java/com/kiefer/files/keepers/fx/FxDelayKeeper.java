package com.kiefer.files.keepers.fx;

import com.kiefer.files.keepers.AutomationManagerKeeper;

public class FxDelayKeeper extends FxKeeper {
    public int time;
    public String feedback, mix;

    public FxDelayKeeper(int fxIndex, boolean on, AutomationManagerKeeper automationManagerKeeper){
        super(fxIndex, on, automationManagerKeeper);
    }
}
