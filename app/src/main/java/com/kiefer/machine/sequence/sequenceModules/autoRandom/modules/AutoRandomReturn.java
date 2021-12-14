package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.engine.EngineFacade;
import com.kiefer.graphics.AutoRndRtrnSubsDrawable;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.popups.Popup;
import com.kiefer.popups.seqModules.autoRnd.AutoRndReturnSubsPopup;

public class AutoRandomReturn extends AutoRandomModuleBool{

    public AutoRandomReturn(LLPPDRUMS llppdrums, SequenceModule sequenceModule){
        super(llppdrums, sequenceModule);
    }

    /** LISTENER **/
    @Override
    public void onStepTouch(EngineFacade engineFacade, final ImageView stepIV, final Step step, float startX, float startY){
        if(step.getNofSubs() > 1){
            getSubsPopup(stepIV, step);
        }
        else {
            sequenceModule.setAutoRndReturn(step, !sequenceModule.getAutoRndReturn(step, 0), 0);
            stepIV.setImageDrawable(getDrawable(step));
        }
    }

    public Popup getSubsPopup(ImageView stepIV, Step step){
        //return new StepBoolDrawable(llppdrums, step);
        return new AutoRndReturnSubsPopup(llppdrums, sequenceModule, this, step, stepIV);
    }

    public Popup getOneSubPopup(ImageView stepIV, Step step){
        //stepIV.setImageDrawable(getDrawable(step));
        return null;
    }

    /** GET **/
    @Override
    public Drawable getDrawable(Step step){
        Boolean[] subsOn = new Boolean[step.getNofSubs()];
        Boolean[] subsValue = new Boolean[step.getNofSubs()];
        for(int i = 0; i < step.getNofSubs(); i++){
            subsOn[i] = ((step.isOn() && step.isSubOn(i)) || step.getAutoRndOn(i)) && sequenceModule.getAutoRndOn(step, i); //maybe introduce a third color here to show if it's doing anything (like grey in the others)
            subsValue[i] = sequenceModule.getAutoRndReturn(step, i);
        }
        return new AutoRndRtrnSubsDrawable(llppdrums, subsOn, subsValue);
    }

    @Override
    public String getLabel() {
        return llppdrums.getResources().getString(R.string.autoRndReturn);
    }

    @Override
    public String getFullName() {
        return llppdrums.getResources().getString(R.string.autoRndReturnFull);
    }

    @Override
    public boolean getAutoRndOn(Step step, int sub){
        return sequenceModule.getAutoRndReturn(step, sub);
    }

    /** SET **/
    @Override
    public void setAutoRndOn(Step step, boolean on, int sub){
        sequenceModule.setAutoRndReturn(step, on, sub);
    }
}
