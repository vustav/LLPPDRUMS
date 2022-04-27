package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.pan;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.graphics.SubsSliderDrawable;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomPerc;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.Popup;

public class AutoRandomPercPan extends AutoRandomPerc {
    /** Same as parent but modified to work on a step instead of a sub **/

    public AutoRandomPercPan(LLPPDRUMS llppdrums, SequenceModule sequenceModule){
        super(llppdrums, sequenceModule);
    }

    @Override
    public Popup getSubsPopup(ImageView stepIV, Step step){
        return getOneSubPopup(stepIV, step);
    }

    @Override
    public Drawable getDrawable(Step step){
        Boolean[] subsOn = new Boolean[1];
        Float[] subsValue = new Float[1];
        for(int i = 0; i < 1; i++){
            subsOn[i] = (step.isOn() && step.isSubOn(i)) || step.getAutoRndOn(i);
            subsValue[i] = sequenceModule.getAutoRndPerc(step, i);
        }
        return new SubsSliderDrawable(llppdrums, subsOn, subsValue);
    }
}
