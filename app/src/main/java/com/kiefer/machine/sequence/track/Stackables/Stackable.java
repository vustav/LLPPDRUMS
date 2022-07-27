package com.kiefer.machine.sequence.track.Stackables;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.kiefer.automation.Automatable;
import com.kiefer.automation.AutomationManager;

public interface Stackable extends Automatable {
    View getLayout();
    String getInfoKey();
    AutomationManager getAutomationManager();
    boolean isOn();
    String getName();
    GradientDrawable getBgGradient();
    GradientDrawable getTabGradient();
    int getAutomationBgId();

    void setOn(boolean on);
}
