package com.kiefer.machine.sequence.sequenceModules;

import android.graphics.Bitmap;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.ui.tabs.interfaces.Tabable;

/** BASE (OnOff, Volume etc) is just one of these. AutoRandom extends it and holds all the AutoRandom-classes. **/

public class SequenceModuleMode implements Tabable{
    protected LLPPDRUMS llppdrums;
    protected DrumSequence drumSequence;
    protected String tabLabel;
    protected Bitmap tabBitmap, bgBitmap;

    public SequenceModuleMode(LLPPDRUMS llppdrums, DrumSequence drumSequence, Bitmap tabBitmap, Bitmap bgBitmap){
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.tabBitmap = tabBitmap;
        this.bgBitmap = bgBitmap;

        tabLabel = llppdrums.getResources().getString(R.string.seqModBaseModeTabName);
    }

    /** SELECTION **/
    public void select(){
        llppdrums.getSequencer().setSequencerDrawables(drumSequence.getTracks());
    }
    public void deselect(){
        //overridden in Auto
    }

    /** GET **/
    @Override
    public Bitmap getTabBitmap() {
        return tabBitmap;
    }

    @Override
    public Bitmap getBgBitmap() {
        return bgBitmap;
    }

    @Override
    public String getLabel(){
        return tabLabel;
    }
}
