package com.kiefer.popups.seqModules.autoRnd;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomModule;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.seqModules.SubsPopup;

public class AutoRndReturnSubsPopup extends SubsPopup {
    private final AutoRandomModule autoRandomModule;

    public AutoRndReturnSubsPopup(LLPPDRUMS llppdrums, SequenceModule sequenceModule, final AutoRandomModule autoRandomModule, final Step step, final ImageView stepIV) {
        super(llppdrums, sequenceModule, step, stepIV);
        this.autoRandomModule = autoRandomModule;
    }

    @Override
    public View.OnClickListener getListener(FrameLayout subLayout, int sub){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean r = !sequenceModule.getAutoRndReturn(step, sub);

                //step.setSubOn(sub, on);
                //step.setret

                sequenceModule.setAutoRndReturn(step, r, sub);

                setSubLayout(subLayout, step, sub);

                //AutoRandomReturn autoRandomReturn = (AutoRandomReturn) sequenceModule;
                subIV.setImageDrawable(autoRandomModule.getDrawable(step));
            }
        };
    }

    @Override
    public void setSubLayout(FrameLayout layout, Step step, int sub){

        //autoRnd ON and return ON
        if(sequenceModule.getAutoRndOn(step, sub) && sequenceModule.getAutoRndReturn(step, sub)) {
            layout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.autoRndRtrnColor));
        }

        //autoRnd OFF and return ON
        else if(!sequenceModule.getAutoRndOn(step, sub) && sequenceModule.getAutoRndReturn(step, sub)) {
            layout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.sequencerInactiveStepColor));
        }

        else{
            layout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.popupBarBg));
        }
    }
}
