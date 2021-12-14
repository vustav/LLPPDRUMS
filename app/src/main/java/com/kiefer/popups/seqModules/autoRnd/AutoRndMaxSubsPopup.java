package com.kiefer.popups.seqModules.autoRnd;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.graphics.AutoRndSliderSubsDrawable;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomModule;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.seqModules.SubsPopup;

public class AutoRndMaxSubsPopup extends SubsPopup {
    private final AutoRandomModule autoRandomModule;

    public AutoRndMaxSubsPopup(LLPPDRUMS llppdrums, SequenceModule sequenceModule, final AutoRandomModule autoRandomModule, final Step step, final ImageView stepIV) {
        super(llppdrums, sequenceModule, step, stepIV);
        this.autoRandomModule = autoRandomModule;
    }

    @Override
    public View.OnClickListener getListener(FrameLayout subLayout, int sub){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AutoRndMaxPopup(llppdrums, sequenceModule, autoRandomModule, subIV, step, sub, AutoRndMaxSubsPopup.this, subLayout);
            }
        };
    }

    @Override
    public void setSubLayout(FrameLayout layout, Step step, int sub){
        layout.setBackground(new AutoRndSliderSubsDrawable(llppdrums, ((step.isOn() && step.isSubOn(sub)) || step.getAutoRndOn(sub)) && sequenceModule.getAutoRndOn(step, sub), sequenceModule.getAutoRndMax(step, sub)));
    }
}
