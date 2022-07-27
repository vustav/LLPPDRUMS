package com.kiefer.machine.sequence.track.Stackables;

import com.kiefer.machine.sequence.track.Stackables.fx.FxManager;
import com.kiefer.popups.stackableManager.StackableManagerPopup;

public interface Stacker {

    //used to set an active open popup to know if automation-graphics should update
    void setStackableManagerPopup(StackableManagerPopup stackableManagerPopup, int type);
    StackableManagerPopup getStackableManagerPopup(int id);

    int getColor();
    String getName();

    int getNOfSteps();
}
