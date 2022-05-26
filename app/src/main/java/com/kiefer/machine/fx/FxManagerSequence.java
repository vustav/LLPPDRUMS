package com.kiefer.machine.fx;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxManagerKeeper;
import com.kiefer.machine.sequence.DrumSequence;

import java.util.ArrayList;

import nl.igorski.mwengine.core.ProcessingChain;

/** Sequence works different than tracks in that it hasn't its own instruments and processingChains
 * but uses master instead. So only add to engine/reset if playing, otherwise it will be done on activation **/

public class FxManagerSequence extends FxManager{
    private DrumSequence drumSequence;

    public FxManagerSequence(LLPPDRUMS llppdrums, DrumSequence drumSequence){
        super(llppdrums, drumSequence);
        this.drumSequence = drumSequence;
    }

    //this one is only used on init before DrumMachine is done and we can't check for playingSequence yet
    public void addFxsToEngineHARD(){
        for (ProcessingChain pc : fxer.getProcessingChains()) {
            for (Fx fx : fxs) {
                if (fx.isOn()) {
                    //don't use addFxToEngine() here since we already loop the pcs to reset them
                    pc.addProcessor(fx.getBaseProcessor());
                }
            }
        }
    }

    @Override
    protected void addFxToEngine(final Fx fx){
        if(llppdrums.getDrumMachine() != null) {
            if (llppdrums.getDrumMachine().getPlayingSequence() == drumSequence) {
                for (ProcessingChain pc : fxer.getProcessingChains()) {
                    pc.addProcessor(fx.getBaseProcessor());
                }
            }
            setIndicator();
        }
    }

    @Override
    protected void rearrangeFxs(){
        if(llppdrums.getDrumMachine() != null) {
            if (llppdrums.getDrumMachine().getPlayingSequence() == drumSequence) {
                for (ProcessingChain pc : fxer.getProcessingChains()) {
                    if (pc != null) {
                        pc.reset();
                        for (Fx fx : fxs) {
                            if (fx.isOn()) {
                                //don't use addFxToEngine() here since we already loop the pcs to reset them
                                pc.addProcessor(fx.getBaseProcessor());
                            }
                        }
                    }
                }
            }
        }
    }

    /** RESTORATION **/
    public void restore(FxManagerKeeper k){

        destroy();
        fxs = new ArrayList<>(); //fxs add themselves in createNewFx

        for(FxKeeper fxk : k.fxKeepers){
            Fx fx = createNewFx(fxk.fxIndex, false);
            fx.restore(fxk);
            //addFxToEngine(fx);

            if(fx.isOn()) {
                //addFxToEngine(fx);
            }
        }

        //only set a selected if at least one exists
        if(k.selectedFxIndex > 0) {
            setSelectedFx(k.selectedFxIndex);
        }
    }
}
