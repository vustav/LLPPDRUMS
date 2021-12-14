package com.kiefer.files.keepers.fx;

import com.kiefer.files.keepers.AutomationManagerKeeper;

public class FxReverbKeeper extends FxKeeper {
    public String size, damp,mix, out;

    public FxReverbKeeper(int fxIndex, boolean on, AutomationManagerKeeper automationManagerKeeper){
        super(fxIndex, on, automationManagerKeeper);
    }
}
