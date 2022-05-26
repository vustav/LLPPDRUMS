package com.kiefer.machine.fx;

import com.kiefer.popups.fxManager.FxManagerPopup;

import java.util.ArrayList;

import nl.igorski.mwengine.core.ProcessingChain;

public interface FXer {
    FxManager getFxManager();

    //used to set an active open popup to know if automation-graphics should update
    void setFxManagerPopup(FxManagerPopup fxManagerPopup);
    FxManagerPopup getFxManagerPopup();

    int getColor();
    String getName();

    int getNOfSteps();

    ArrayList<ProcessingChain> getProcessingChains();
}
