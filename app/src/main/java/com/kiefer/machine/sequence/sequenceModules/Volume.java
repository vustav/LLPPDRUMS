package com.kiefer.machine.sequence.sequenceModules;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.VolSubsDrawable;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.AutoRandom;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.Popup;
import com.kiefer.popups.seqModules.VolSubsPopup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.utils.ImgUtils;
import com.kiefer.popups.seqModules.VolPopup;

import java.util.ArrayList;

public class Volume extends SequenceModule {

    public Volume(LLPPDRUMS llppdrums, DrumSequence drumSequence, Bitmap tabBitmap, Bitmap bgBitmap){
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
        return new VolPopup(llppdrums, this, stepIV, step, 0);
    }

    @Override
    public Popup getSubsPopup(ImageView stepIV, Step step){
        return new VolSubsPopup(llppdrums, this, step, stepIV);
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
    }

    @Override
    public float setAutoValueBase(DrumTrack drumTrack, String s, int sub) {
        float vol = Float.parseFloat(s);
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            drumTrack.setSubVolume(step, vol, sub);
        }
        return vol;
    }

    @Override
    public void randomizeAll(DrumTrack drumTrack){
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            Step d = drumTrack.getSteps().get(step);
            d.randomizeVols(false);
        }
    }

    /** GET **/

    //auto-rnd
    @Override
    public boolean getAutoRndOn(Step step, int sub){
        return step.getAutoRndVol(sub);
    }

    @Override
    public float getAutoRndMin(Step step, int sub){
        return step.getRndVolMin(sub);
    }

    @Override
    public float getAutoRndMax(Step step, int sub){
        return step.getRndVolMax(sub);
    }

    @Override
    public float getAutoRndPerc(Step step, int sub){
        return step.getRndVolPerc(sub);
    }

    @Override
    public boolean getAutoRndReturn(Step step, int sub){
        return step.getRndVolReturn(sub);
    }

    @Override
    public String getLabel() {
        return llppdrums.getResources().getString(R.string.volumeTabLabel);
    }

    @Override
    public String getFullName(){
        if(isInBaseMode()) {
            return llppdrums.getResources().getString(R.string.volumeName);
        }
        else{
            return llppdrums.getResources().getString(R.string.volumeName) + llppdrums.getResources().getString(R.string.stringDivider) + ((AutoRandom)modes.get(AUTO_RANDOM)).getFullName();
        }
    }

    @Override
    public Drawable getDrawable(int trackNo, int stepNo){
        Step drum = drumSequence.getTracks().get(trackNo).getSteps().get(stepNo);

        //base
        if(modes.indexOf(selectedMode) == 0) {
            return new VolSubsDrawable(llppdrums, drum);
        }
        //random
        else{
            //return new AutoRandomDrawable(llppdrums, drum, drum.getAutoRndVol(), true);
            return ((AutoRandom)modes.get(AUTO_RANDOM)).getDrawable(drum);
        }
    }

    /** SET **/
    /*
    @Override
    public void setAutoRndOn(Drum drum, boolean on){
        drum.setAutoRndVol(on);
    }

     */
    @Override
    public void setAutoRndMin(Step step, float min, int sub){
        step.setRndVolMin(min, sub);
    }
    @Override
    public void setAutoRndMax(Step step, float max, int sub){
        step.setRndVolMax(max, sub);
    }
    @Override
    public void setAutoRndPerc(Step step, float perc, int sub){
        step.setRndVolPerc(perc, sub);
    }
    @Override
    public void setAutoRndReturn(Step step, boolean r, int sub){
        step.setRndVolReturn(r, sub);
    }

    /** SEQUENCER UPDATES **/
    public void handleSequencerPositionChange(int sequencerPosition){

    }
}
