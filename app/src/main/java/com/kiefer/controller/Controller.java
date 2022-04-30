package com.kiefer.controller;

import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.engine.EngineFacade;
import com.kiefer.files.keepers.controller.ControllerKeeper;
import com.kiefer.machine.SequenceManager;
import com.kiefer.popups.controller.FunBtnEditPopup;
import com.kiefer.popups.controller.SeqBtnEditPopup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.Tabable;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

/** The controls and the tabs at the top of the screen **/

public class Controller implements Tabable {
    private LLPPDRUMS llppdrums;
    private EngineFacade engineFacade;
    private SequenceManager sequenceManager;
    private int selectedSeqIndex = 0;

    private SeqBtnManager seqBtnManager;
    private FunBtnManager funBtnManager;

    private final GradientDrawable seqPopupGradient, funPopupGradient;

    //bitmaps
    private Bitmap tabBitmap, bgBitmap;
    private int bitmapId;

    private String name;
    private int tabIndex;

    public Controller(LLPPDRUMS llppdrums, EngineFacade engineFacade, SequenceManager sequenceManager, int tabIndex, ControllerKeeper keeper){
        this.llppdrums = llppdrums;
        this.engineFacade = engineFacade;
        //this.tabBitmap = tabBitmap;
        //this.bgBitmap = bgBitmap;
        this.sequenceManager = sequenceManager;

        this.tabIndex = tabIndex;


        bitmapId = ImgUtils.getRandomImageId();
        //tabBitmap = ImgUtils.getTabImg(llppdrums, imgId, 1, 2, TabManager.HORIZONTAL);
        //bgBitmap = ImgUtils.getBgImg(llppdrums, imgId, TabManager.HORIZONTAL);

        seqPopupGradient = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
        funPopupGradient = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());

        try {
            seqBtnManager = new SeqBtnManager(this, sequenceManager, keeper.seqBtnManagerKeeper);
            funBtnManager = new FunBtnManager(this, keeper.funBtnManagerKeeper);
        }
        catch (Exception e){
            seqBtnManager = new SeqBtnManager(this, sequenceManager, null);
            funBtnManager = new FunBtnManager(this, null);
        }

        name = llppdrums.getResources().getString(R.string.controllerName);

        sequenceManager.setSelectedControllerSeqBox(selectedSeqIndex);
    }

    public SequenceManager getSequenceManager() {
        return sequenceManager;
    }

    /** SEQ SELECTION **/
    public void selectPrevSeq(){
        if(selectedSeqIndex == 0 || selectedSeqIndex > sequenceManager.getNOfActiveBoxes() - 1){
            selectedSeqIndex = sequenceManager.getNOfActiveBoxes() - 1;
        }
        else{
            selectedSeqIndex--;
        }
        sequenceManager.setSelectedControllerSeqBox(selectedSeqIndex);
    }

    public void selectNextSeq(){
        if(selectedSeqIndex >= sequenceManager.getNOfActiveBoxes() - 1){
            selectedSeqIndex = 0;
        }
        else{
            selectedSeqIndex++;
        }
        sequenceManager.setSelectedControllerSeqBox(selectedSeqIndex);
    }

    /** POPUPS **/
    public void openEditSeqPopup(){
        new SeqBtnEditPopup(llppdrums, this);
    }
    public void openEditFunPopup(){
        new FunBtnEditPopup(llppdrums, this);
    }

    /** GET**/
    public GradientDrawable getSeqPopupGradient() {
        return seqPopupGradient;
    }
    public GradientDrawable getFunPopupGradient() {
        return funPopupGradient;
    }

    //tabs
    //@Override
    //public Bitmap getTabBitmap(){
        //return tabBitmap;
    //}

    @Override
    public int getBitmapId(){
        return bitmapId;
    }

    @Override
    public int getOrientation(){
        return TabManager.HORIZONTAL;
    }

    @Override
    public String getName(){
        return name;
    }

    public SeqBtnManager getSeqBtnManager() {
        return seqBtnManager;
    }

    public FunBtnManager getFunBtnManager() {
        return funBtnManager;
    }

    public int getSelectedSeqIndex() {
        return selectedSeqIndex;
    }

    //TABS
    @Override
    public int getTabIndex() {
        return tabIndex;
    }

    @Override
    public int getTier(){
        return 0;
    }

    /** FILE HANDLING **/

    public ControllerKeeper getKeeper(){
        ControllerKeeper k = new ControllerKeeper();
        k.seqBtnManagerKeeper = seqBtnManager.getKeeper();
        k.funBtnManagerKeeper = funBtnManager.getKeeper();
        return k;
    }

    //gets a keeper as argument and uses it during creation and sends it further to tracks
    public void load(final ControllerKeeper k){
        seqBtnManager.load(k.seqBtnManagerKeeper);
        funBtnManager.load(k.funBtnManagerKeeper);
    }
}
