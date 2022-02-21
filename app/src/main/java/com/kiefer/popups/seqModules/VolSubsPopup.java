package com.kiefer.popups.seqModules;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.SubsSliderDrawable;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.Volume;
import com.kiefer.machine.sequence.track.Step;

public class VolSubsPopup extends SubsPopup {

    public VolSubsPopup(LLPPDRUMS llppdrums, SequenceModule sequenceModule, final Step step, final ImageView stepIV) {
        super(llppdrums, sequenceModule, step, stepIV);
    }

    @Override
    public View.OnClickListener getListener(FrameLayout subLayout, int sub){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Volume volume = (Volume) sequenceModule;
                new VolPopup(llppdrums, volume, subIV, step, sub, VolSubsPopup.this, subLayout);
            }
        };
    }

    @Override
    public void setSubLayout(FrameLayout layout, Step step, int sub){
        layout.setBackground(new SubsSliderDrawable(llppdrums, step.isOn() && step.isSubOn(sub), step.getSubVol(sub)));
    }
}
