package com.kiefer.controller;

import android.view.View;

import com.kiefer.files.keepers.controller.SeqBtnManagerKeeper;
import com.kiefer.machine.SequenceManager;

public class SeqBtnManager {
    private final Controller controller;
    private final SequenceManager sequenceManager;
    private boolean momentary;

    public SeqBtnManager(Controller controller, SequenceManager sequenceManager, SeqBtnManagerKeeper keeper){
        this.controller = controller;
        this.sequenceManager = sequenceManager;

        if(keeper != null) {
            momentary = keeper.momentary;
        }
        else{
            momentary = true;
        }
    }

    int oldPlayingSeq;
    public void buttonPressed(View view){
        if(momentary){
            sequenceManager.setChangesHalted(true);
            oldPlayingSeq = sequenceManager.getActiveSequenceBoxIndex();
            sequenceManager.activateSequenceBox(controller.getSelectedSeqIndex());
        }
        else{
            sequenceManager.getCounter().getListener(controller.getSelectedSeqIndex()).onClick(view);
        }
    }

    public void buttonReleased(View view){
        if(momentary){
            sequenceManager.activateSequenceBox(oldPlayingSeq);
            sequenceManager.setChangesHalted(false);
        }
    }

    /** SET **/
    public void setMomentary(boolean momentary) {
        this.momentary = momentary;
    }

    /** GET **/
    public boolean isMomentary() {
        return momentary;
    }

    /** KEEPING **/
    public SeqBtnManagerKeeper getKeeper(){
        SeqBtnManagerKeeper k = new SeqBtnManagerKeeper();
        k.momentary = momentary;
        return k;
    }

    public void load(SeqBtnManagerKeeper k) {
        setMomentary(k.momentary);
    }
}
