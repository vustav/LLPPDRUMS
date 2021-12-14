package com.kiefer.machine.sequence.sequenceModules;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.PitchSubsDrawable;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.AutoRandom;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.Popup;
import com.kiefer.popups.seqModules.PitchSubsPopup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.utils.ImgUtils;
import com.kiefer.popups.seqModules.PitchPopup;

import java.util.ArrayList;

public class Pitch extends SequenceModule {

    public Pitch(LLPPDRUMS llppdrums, DrumSequence drumSequence, Bitmap tabBitmap, Bitmap bgBitmap){
        super(llppdrums, drumSequence, tabBitmap, bgBitmap);
    }

    @Override
    void setupModes(){
        modes = new ArrayList<>();

        //create the necessary Bitmaps before creating the modes and adding them to the array
        int imgId = ImgUtils.getRandomImageId();
        Bitmap tabBitmap = ImgUtils.getTabImg(llppdrums, imgId, 0, 2, TabManager.VERTICAL);
        Bitmap bgBitmap = ImgUtils.getBgImg(llppdrums, imgId, TabManager.VERTICAL);
        modes.add(new SequenceModuleMode(llppdrums, drumSequence, tabBitmap, bgBitmap));

        imgId = ImgUtils.getRandomImageId();
        tabBitmap = ImgUtils.getTabImg(llppdrums, imgId, 1, 2, TabManager.VERTICAL);
        bgBitmap = ImgUtils.getBgImg(llppdrums, imgId, TabManager.VERTICAL);
        modes.add(new AutoRandom(llppdrums, drumSequence, this, tabBitmap, bgBitmap));
    }

    /** POPUP **/
    @Override
    public Popup getOneSubPopup(ImageView stepIV, Step step){
        return new PitchPopup(llppdrums, this, stepIV, step, 0);
    }

    @Override
    public Popup getSubsPopup(ImageView stepIV, Step step){
        return new PitchSubsPopup(llppdrums, this, step, stepIV);
    }

    /** AUTO **/
    @Override
    public void setupAutoValues(){
        autoStepValues = new ArrayList<>();
        autoStepValues.add("1");
        autoStepValues.add("0.8");
        autoStepValues.add("0.6");
        autoStepValues.add("0.4");
        autoStepValues.add("0.2");
        autoStepValues.add("0");
        autoStepValues.add("-0.2");
        autoStepValues.add("-0.4");
        autoStepValues.add("-0.6");
        autoStepValues.add("-0.8");
        autoStepValues.add("-1");
    }

    @Override
    public float setAutoValueBase(DrumTrack drumTrack, String s, int sub) {
        float pitch = (Float.parseFloat(s) + 1) / 2; //make it a float 0-1
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            drumTrack.setSubPitchModifier(step, pitch, sub);
        }
        return pitch;
    }

    @Override
    public void randomizeAll(DrumTrack drumTrack){
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            Step d = drumTrack.getSteps().get(step);
            d.randomizePitches(false);
        }
    }

    /** GET **/
    //auto-rnd
    @Override
    public boolean getAutoRndOn(Step step, int sub){
        return step.getAutoRndPitch(sub);
    }

    @Override
    public float getAutoRndMin(Step step, int sub){
        return step.getRndPitchMin(sub);
    }

    @Override
    public float getAutoRndMax(Step step, int sub){
        return step.getRndPitchMax(sub);
    }

    @Override
    public boolean getAutoRndReturn(Step step, int sub){
        return step.getRndPitchReturn(sub);
    }

    @Override
    public float getAutoRndPerc(Step step, int sub){
        return step.getRndPitchPerc(sub);
    }

    @Override
    public String getLabel() {
        return llppdrums.getResources().getString(R.string.pitchTabLabel);
    }

    @Override
    public String getFullName(){
        if(isInBaseMode()) {
            return llppdrums.getResources().getString(R.string.pitchName);
        }
        else{
            return llppdrums.getResources().getString(R.string.pitchName) + llppdrums.getResources().getString(R.string.stringDivider) + ((AutoRandom)modes.get(AUTO_RANDOM)).getFullName();
        }
    }

    @Override
    public Drawable getDrawable(int trackNo, int stepNo){
        Step drum = drumSequence.getTracks().get(trackNo).getSteps().get(stepNo);

        //base
        if(modes.indexOf(selectedMode) == 0) {
            return new PitchSubsDrawable(llppdrums, drum);
        }
        //random
        else{
            //return new AutoRandomDrawable(llppdrums, drum, drum.getAutoRndPitch(), true);
            return ((AutoRandom)modes.get(AUTO_RANDOM)).getDrawable(drum);
        }
    }

    /** SET **/
    /*
    @Override
    public void setAutoRndOn(Drum drum, boolean on){
        drum.setAutoRndPitch(on);
    }

     */
    @Override
    public void setAutoRndMin(Step step, float min, int sub){
        step.setRndPitchMin(min, sub);
    }
    @Override
    public void setAutoRndMax(Step step, float max, int sub){
        step.setRndPitchMax(max, sub);
    }
    @Override
    public void setAutoRndPerc(Step step, float perc, int sub){
        step.setRndPitchPerc(perc, sub);
    }
    @Override
    public void setAutoRndReturn(Step step, boolean r, int sub){
        step.setRndPitchReturn(r, sub);
    }

    /** SEQUENCER UPDATES **/
    public void handleSequencerPositionChange(int sequencerPosition){

    }
}
