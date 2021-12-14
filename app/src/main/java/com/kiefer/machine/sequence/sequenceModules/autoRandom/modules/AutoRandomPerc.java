package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.AutoRndSliderSubsDrawable;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.popups.Popup;
import com.kiefer.popups.seqModules.autoRnd.AutoRndPercPopup;
import com.kiefer.popups.seqModules.autoRnd.AutoRndPercSubsPopup;

public class AutoRandomPerc extends AutoRandomModuleFloat{

    public AutoRandomPerc(LLPPDRUMS llppdrums, SequenceModule sequenceModule){
        super(llppdrums, sequenceModule);
    }

    @Override
    public Popup getOneSubPopup(ImageView stepIV, Step step){
        return new AutoRndPercPopup(llppdrums, sequenceModule, this, stepIV, step, 0);
    }

    @Override
    public Popup getSubsPopup(ImageView stepIV, Step step){
        return new AutoRndPercSubsPopup(llppdrums, sequenceModule, this, step, stepIV);
    }

    /** GET **/
    @Override
    public Drawable getDrawable(Step step){
        Boolean[] subsOn = new Boolean[step.getNofSubs()];
        Float[] subsValue = new Float[step.getNofSubs()];
        for(int i = 0; i < step.getNofSubs(); i++){
            //om inte nån av stepOn eller subON eller att autoRndOn på sub är på (så att nån sub kan bli on), kommer inte perc spela nån roll och blir grå).
            // OBS detta gäller inte OnOff, den är alltid grön
            subsOn[i] = (step.isOn() && step.isSubOn(i)) || step.getAutoRndOn(i);
            subsValue[i] = sequenceModule.getAutoRndPerc(step, i);
        }
        return new AutoRndSliderSubsDrawable(llppdrums, subsOn, subsValue);
    }

    @Override
    public String getLabel() {
        return llppdrums.getResources().getString(R.string.autoRndPerc);
    }

    @Override
    public String getFullName() {
        return llppdrums.getResources().getString(R.string.autoRndPercFull);
    }

    /** SET **/
    @Override
    public void setAutoRndValue(Step step, float value, int sub){
        sequenceModule.setAutoRndPerc(step, value, sub);
    }
}
