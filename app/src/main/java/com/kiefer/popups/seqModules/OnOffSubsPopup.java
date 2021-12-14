package com.kiefer.popups.seqModules;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.sequenceModules.OnOff;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.track.Step;

public class OnOffSubsPopup extends SubsPopup {

    public OnOffSubsPopup(LLPPDRUMS llppdrums, SequenceModule sequenceModule, final Step step, final ImageView stepIV) {
        super(llppdrums, sequenceModule, step, stepIV);
    }

    @Override
    public View.OnClickListener getListener(FrameLayout subLayout, int sub){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean on = !step.isSubOn(sub);

                step.setSubOn(sub, on);
                setSubLayout(subLayout, step, sub);

                OnOff onOff = (OnOff) sequenceModule;
                subIV.setImageDrawable(onOff.getDrawable(step.getTrackNo(), step.getStepNo()));
            }
        };
    }

    @Override
    public void setSubLayout(FrameLayout layout, Step step, int sub){
        if(step.isSubOn(sub)){
            layout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.automationStepOn));
        }
        else{
            layout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.automationStepOff));
        }
    }
}
