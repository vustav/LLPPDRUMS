package com.kiefer.popups.seqModules.autoRnd;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.graphics.AutoRndSliderSubsDrawable;
import com.kiefer.machine.sequence.sequenceModules.OnOff;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomModule;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.seqModules.SubsPopup;

public class AutoRndPercSubsPopup extends SubsPopup {
    private final AutoRandomModule autoRandomModule;

    public AutoRndPercSubsPopup(LLPPDRUMS llppdrums, SequenceModule sequenceModule, final AutoRandomModule autoRandomModule, final Step step, final ImageView stepIV) {
        super(llppdrums, sequenceModule, step, stepIV);
        this.autoRandomModule = autoRandomModule;
    }

    @Override
    public View.OnClickListener getListener(FrameLayout subLayout, int sub){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AutoRndPercPopup(llppdrums, sequenceModule, autoRandomModule, subIV, step, sub, AutoRndPercSubsPopup.this, subLayout);
            }
        };
    }

    @Override
    public void setSubLayout(FrameLayout layout, Step step, int sub){
        boolean on = (step.isOn() && step.isSubOn(sub)) || step.getAutoRndOn(sub) || sequenceModule instanceof OnOff;
        layout.setBackground(new AutoRndSliderSubsDrawable(llppdrums, on, sequenceModule.getAutoRndPerc(step, sub)));
    }
}
