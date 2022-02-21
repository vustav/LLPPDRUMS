package com.kiefer.popups.seqModules;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.graphics.SubsSliderDrawable;
import com.kiefer.machine.sequence.sequenceModules.Pitch;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.track.Step;

public class PitchSubsPopup extends SubsPopup {

    public PitchSubsPopup(LLPPDRUMS llppdrums, SequenceModule sequenceModule, final Step step, final ImageView stepIV) {
        super(llppdrums, sequenceModule, step, stepIV);
    }

    @Override
    public View.OnClickListener getListener(FrameLayout subLayout, int sub){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pitch pitch = (Pitch) sequenceModule;
                new PitchPopup(llppdrums, pitch, subIV, step, sub, PitchSubsPopup.this, subLayout);
            }
        };
    }

    @Override
    public void setSubLayout(FrameLayout layout, Step step, int sub){
        layout.setBackground(new SubsSliderDrawable(llppdrums, step.isOn() && step.isSubOn(sub), step.getSubPitch(sub)));
    }
}
