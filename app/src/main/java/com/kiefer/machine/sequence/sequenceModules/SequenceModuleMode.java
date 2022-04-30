package com.kiefer.machine.sequence.sequenceModules;

import android.graphics.Bitmap;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.Tabable;
import com.kiefer.utils.ImgUtils;

/** BASE (OnOff, Volume etc) is just one of these. AutoRandom extends it and holds all the AutoRandom-classes. **/

public class SequenceModuleMode implements Tabable{
    protected LLPPDRUMS llppdrums;
    protected DrumSequence drumSequence;
    protected String tabLabel;
    //protected Bitmap tabBitmap, bgBitmap;
    protected int bitmapId;
    int tabIndex;

    public SequenceModuleMode(LLPPDRUMS llppdrums, DrumSequence drumSequence, int tabIndex){
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        //this.tabBitmap = tabBitmap;
        //this.bgBitmap = bgBitmap;
        this.tabIndex = tabIndex;

        bitmapId = ImgUtils.getRandomImageId();
        //tabBitmap = ImgUtils.getTabBitmap(llppdrums, imgId, 0, 2, TabManager.VERTICAL);
        //bgBitmap = ImgUtils.getBgBitmap(llppdrums, imgId, TabManager.VERTICAL);

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
    public int getBitmapId() {
        return bitmapId;
    }

    @Override
    public int getOrientation(){
        return TabManager.VERTICAL;
    }

    @Override
    public String getName(){
        return tabLabel;
    }

    //TABS
    @Override
    public int getTabIndex() {
        return tabIndex;
    }

    @Override
    public int getTier(){
        return 2;
    }
}
