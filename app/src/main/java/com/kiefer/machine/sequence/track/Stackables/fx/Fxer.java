package com.kiefer.machine.sequence.track.Stackables.fx;

import com.kiefer.machine.sequence.track.Stackables.Stacker;

import java.util.ArrayList;

import nl.igorski.mwengine.core.ProcessingChain;

public interface Fxer extends Stacker {

    ArrayList<ProcessingChain> getProcessingChains();
}
