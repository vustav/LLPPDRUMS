package com.kiefer.files.keepers.fx;

import com.kiefer.files.keepers.AutomationManagerKeeper;

public class FxFlangerKeeper extends FxKeeper {
    public String rate, width,feedback, delay, mix;

    public FxFlangerKeeper(int fxIndex, boolean on, AutomationManagerKeeper automationManagerKeeper){
        super(fxIndex, on, automationManagerKeeper);
    }
}
