package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.pan;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.graphics.SubsSliderDrawable;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomMax;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.Popup;
import com.kiefer.popups.seqModules.autoRnd.AutoRndMaxSubsPopup;

public class AutoRandomMaxPan extends AutoRandomMax {
    /** Same as parent but modified to work on a step instead of a sub **/

    public AutoRandomMaxPan(LLPPDRUMS llppdrums, SequenceModule sequenceModule){
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
            subsOn[i] = ((step.isOn() && step.isSubOn(i)) || step.getAutoRndOn(i)) && sequenceModule.getAutoRndOn(step, i);
            subsValue[i] = sequenceModule.getAutoRndMax(step, i);
        }
        return new SubsSliderDrawable(llppdrums, subsOn, subsValue);
    }
}
