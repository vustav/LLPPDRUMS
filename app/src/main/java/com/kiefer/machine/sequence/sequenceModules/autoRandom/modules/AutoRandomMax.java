package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.SubsSliderDrawable;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.popups.Popup;
import com.kiefer.popups.seqModules.autoRnd.AutoRndMaxPopup;
import com.kiefer.popups.seqModules.autoRnd.AutoRndMaxSubsPopup;

public class AutoRandomMax extends AutoRandomModuleFloat{

    public AutoRandomMax(LLPPDRUMS llppdrums, SequenceModule sequenceModule){
        super(llppdrums, sequenceModule);
    }

    @Override
    public Popup getOneSubPopup(ImageView stepIV, Step step){
        return new AutoRndMaxPopup(llppdrums, sequenceModule, this, stepIV, step, 0);
    }

    @Override
    public Popup getSubsPopup(ImageView stepIV, Step step){
        return new AutoRndMaxSubsPopup(llppdrums, sequenceModule, this, step, stepIV);
    }

    /** GET **/
    @Override
    public Drawable getDrawable(Step step){
        Boolean[] subsOn = new Boolean[step.getNofSubs()];
        Float[] subsValue = new Float[step.getNofSubs()];
        for(int i = 0; i < step.getNofSubs(); i++){
            subsOn[i] = ((step.isOn() && step.isSubOn(i)) || step.getAutoRndOn(i)) && sequenceModule.getAutoRndOn(step, i);
            subsValue[i] = sequenceModule.getAutoRndMax(step, i);
        }
        return new SubsSliderDrawable(llppdrums, subsOn, subsValue);
    }

    @Override
    public String getLabel() {
        return llppdrums.getResources().getString(R.string.autoRndMax);
    }

    @Override
    public String getFullName() {
        return llppdrums.getResources().getString(R.string.autoRndMaxFull);
    }

    /** SET **/
    @Override
    public void setAutoRndValue(Step step, float value, int sub){
        sequenceModule.setAutoRndMax(step, value, sub);
    }
}
