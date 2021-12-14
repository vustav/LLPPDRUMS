package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.AutoRndSliderSubsDrawable;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.popups.Popup;
import com.kiefer.popups.seqModules.autoRnd.AutoRndMinPopup;
import com.kiefer.popups.seqModules.autoRnd.AutoRndMinSubsPopup;

public class AutoRandomMin extends AutoRandomModuleFloat{

    public AutoRandomMin(LLPPDRUMS llppdrums, SequenceModule sequenceModule){
        super(llppdrums, sequenceModule);
    }

    @Override
    public Popup getOneSubPopup(ImageView stepIV, Step step){
        return new AutoRndMinPopup(llppdrums, sequenceModule, this, stepIV, step, 0);
    }

    @Override
    public Popup getSubsPopup(ImageView stepIV, Step step){
        return new AutoRndMinSubsPopup(llppdrums, sequenceModule, this, step, stepIV);
    }

    /** GET **/
    @Override
    public Drawable getDrawable(Step step){
        Boolean[] subsOn = new Boolean[step.getNofSubs()];
        Float[] subsValue = new Float[step.getNofSubs()];
        for(int i = 0; i < step.getNofSubs(); i++){
            subsOn[i] = ((step.isOn() && step.isSubOn(i)) || step.getAutoRndOn(i)) && sequenceModule.getAutoRndOn(step, i);
            subsValue[i] = sequenceModule.getAutoRndMin(step, i);
        }
        return new AutoRndSliderSubsDrawable(llppdrums, subsOn, subsValue);
    }

    @Override
    public String getLabel() {
        return llppdrums.getResources().getString(R.string.autoRndMin);
    }

    @Override
    public String getFullName() {
        return llppdrums.getResources().getString(R.string.autoRndMinFull);
    }

    /** SET **/
    @Override
    public void setAutoRndValue(Step step, float value, int sub){
        sequenceModule.setAutoRndMin(step, value, sub);
    }
}
