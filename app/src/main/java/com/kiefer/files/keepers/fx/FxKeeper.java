package com.kiefer.files.keepers.fx;

import com.kiefer.files.keepers.AutomationManagerKeeper;
import com.kiefer.files.keepers.Keeper;

public abstract class FxKeeper implements Keeper {
    public int fxIndex;
    public boolean on;
    public AutomationManagerKeeper automationManagerKeeper;

    public FxKeeper(int fxIndex, boolean on, AutomationManagerKeeper automationManagerKeeper){
        this.fxIndex = fxIndex;
        this.on = on;
        this.automationManagerKeeper = automationManagerKeeper;
    }
}
