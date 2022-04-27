package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.pan;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.graphics.SubsBoolDrawable;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomReturn;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.Popup;

public class AutoRandomReturnPan extends AutoRandomReturn {
    /** Same as parent but modified to work on a step instead of a sub **/

    public AutoRandomReturnPan(LLPPDRUMS llppdrums, SequenceModule sequenceModule){
        super(llppdrums, sequenceModule);
    }

    @Override
    public Popup getSubsPopup(ImageView stepIV, Step step){
        return getOneSubPopup(stepIV, step);
    }
    @Override
    public Drawable getDrawable(Step step){
        Boolean[] subsOn = new Boolean[1];
        Boolean[] subsValue = new Boolean[1];
        for(int i = 0; i < 1; i++){
            subsOn[i] = ((step.isOn() && step.isSubOn(i)) || step.getAutoRndOn(i)) && sequenceModule.getAutoRndOn(step, i);
            subsValue[i] = sequenceModule.getAutoRndReturn(step, i);
        }
        return new SubsBoolDrawable(llppdrums, subsOn, subsValue);
    }
}
