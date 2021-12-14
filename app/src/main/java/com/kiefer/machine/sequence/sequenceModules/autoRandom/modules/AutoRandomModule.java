package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.engine.EngineFacade;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.Popup;

import java.util.ArrayList;

public abstract class AutoRandomModule {
    protected LLPPDRUMS llppdrums;
    protected SequenceModule sequenceModule;

    public abstract Drawable getDrawable(Step step);
    public abstract String getLabel();
    //public abstract void onStepTouch(EngineFacade engineFacade, final ImageView stepIV, final Step step, float startX, float startY);
    public abstract ArrayList<String> getAutoStepValues();
    public abstract void setAutoValue(final DrumTrack drumTrack, String s, int sub);
    public abstract String getFullName();
    public abstract Popup getOneSubPopup(ImageView stepIV, Step step);
    public abstract Popup getSubsPopup(ImageView stepIV, Step step);

    public AutoRandomModule(LLPPDRUMS llppdrums, SequenceModule sequenceModule){
        this.llppdrums = llppdrums;
        this.sequenceModule = sequenceModule;
    }

    /** LISTENER **/
    public void onStepTouch(EngineFacade engineFacade, final ImageView stepIV, final Step step, float startX, float startY){
        if(step.getNofSubs() > 1){
            getSubsPopup(stepIV, step);
        }
        else {
            getOneSubPopup(stepIV, step);
        }
    }
}
