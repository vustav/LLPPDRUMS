package com.kiefer.machine.sequence.sequenceModules;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.PanSubsDrawable;
import com.kiefer.engine.EngineFacade;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.AutoRandom;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.pan.AutoRandomPan;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.Popup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.utils.ImgUtils;
import com.kiefer.popups.seqModules.PanPopup;

import java.util.ArrayList;

public class Pan extends SequenceModule {

    public Pan(LLPPDRUMS llppdrums, DrumSequence drumSequence, int bitmapId){
        super(llppdrums, drumSequence, bitmapId);
    }

    //override to never trigger getSubsPopup().. (pan works on steps, not subs, so there's no need for it)
    @Override
    public void onStepTouch(EngineFacade engineFacade, final ImageView stepIV, final Step step, float startX, float startY, float endX, float endY, int action){
        if(action == MotionEvent.ACTION_UP) {
            //base
            if (modes.indexOf(selectedMode) == 0) {
                getOneSubPopup(stepIV, step);
            }
            //random
            else {
                ((AutoRandom) modes.get(AUTO_RANDOM)).onStepTouch(engineFacade, stepIV, step, startX, startY);
            }
        }
    }

    @Override
    void setupModes(){
        modes = new ArrayList<>();

        modes.add(new SequenceModuleMode(llppdrums, drumSequence));

        //since PAN works on steps and not subs it has it's own set of AutoRandom classes
        modes.add(new AutoRandomPan(llppdrums, drumSequence, this));
    }

    /** POPUP **/
    @Override
    public Popup getOneSubPopup(ImageView stepIV, Step step){
        return new PanPopup(llppdrums, this, stepIV, step, 0);
    }

    @Override
    public Popup getSubsPopup(ImageView stepIV, Step step){
        //never used
        return null;
    }

    /** AUTO **/
    @Override
    public void setupAutoValues(){
        autoStepValues = new ArrayList<>();
        autoStepValues.add("-1");
        autoStepValues.add("-0.8");
        autoStepValues.add("-0.6");
        autoStepValues.add("-0.4");
        autoStepValues.add("-0.2");
        autoStepValues.add("0");
        autoStepValues.add("0.2");
        autoStepValues.add("0.4");
        autoStepValues.add("0.6");
        autoStepValues.add("0.8");
        autoStepValues.add("1");
    }

    @Override
    public float setAutoValueBase(DrumTrack drumTrack, String s, int sub) {
        //float pan = (Float.parseFloat(s) + 1) / 2; //make it a float 0-1
        float pan = Float.parseFloat(s);
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            //drumTrack.setSubPan(step, pan, sub);
            drumTrack.getSteps().get(step).setPan(pan);
        }
        return pan;
    }

    @Override
    public void randomize(DrumTrack drumTrack){
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            Step d = drumTrack.getSteps().get(step);
            d.randomizePan(false);
        }
    }

    /** GET **/
    @Override
    public boolean getAutoRndOn(Step step, int sub){
        return step.getAutoRndPan();
    }

    @Override
    public float getAutoRndMin(Step step, int sub){
        return step.getRndPanMin();
    }

    @Override
    public float getAutoRndMax(Step step, int sub){
        return step.getRndPanMax();
    }

    @Override
    public float getAutoRndPerc(Step step, int sub){
        return step.getRndPanPerc();
    }

    @Override
    public boolean getAutoRndReturn(Step step, int sub){
        return step.getRndPanReturn();
    }

    @Override
    public String getName() {
        return llppdrums.getResources().getString(R.string.panTabLabel);
    }

    @Override
    public String getFullName(){
        if(isInBaseMode()) {
            return llppdrums.getResources().getString(R.string.panName);
        }
        else{
            return llppdrums.getResources().getString(R.string.panName) + llppdrums.getResources().getString(R.string.stringDivider) + ((AutoRandom)modes.get(AUTO_RANDOM)).getFullName();
        }
    }

    @Override
    public Drawable getDrawable(int trackNo, int stepNo){
        Step drum = drumSequence.getTracks().get(trackNo).getSteps().get(stepNo);

        //base
        if(modes.indexOf(selectedMode) == 0) {
            return new PanSubsDrawable(llppdrums, this, drum);
        }
        //random
        else{
            //return new AutoRandomDrawable(llppdrums, drum, drum.getAutoRndPan(), true);
            return ((AutoRandom)modes.get(AUTO_RANDOM)).getDrawable(drum);
        }
    }

    /** SET **/
    /*
    @Override
    public void setAutoRndOn(Drum drum, boolean on){
        drum.setAutoRndPan(on);
    }

     */
    @Override
    public void setAutoRndMin(Step step, float min, int sub){
        step.setRndPanMin(min);
    }
    @Override
    public void setAutoRndMax(Step step, float min, int sub){
        step.setRndPanMax(min);
    }
    @Override
    public void setAutoRndPerc(Step step, float perc, int sub){
        step.setRndPanPerc(perc);
    }
    @Override
    public void setAutoRndReturn(Step step, boolean r, int sub){
        step.setRndPanReturn(r);
    }

    /** SEQUENCER UPDATES **/
    public void handleSequencerPositionChange(int sequencerPosition){

    }
}
