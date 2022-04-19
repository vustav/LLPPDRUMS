package com.kiefer.machine.sequence.sequenceModules;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.OnOffSubsDrawable;
import com.kiefer.engine.EngineFacade;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.AutoRandom;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.AutoRandomOnOff;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.Popup;
import com.kiefer.popups.seqModules.OnOffSubsPopup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

public class OnOff extends SequenceModule {

    public OnOff(LLPPDRUMS llppdrums, DrumSequence drumSequence, int bitmapId){
        super(llppdrums, drumSequence, bitmapId);
    }

    @Override
    void setupModes(){
        modes = new ArrayList<>();

        //create the necessary Bitmaps before creating the modes and adding them to the array
        //int imgId = ImgUtils.getRandomImageId();
        //Bitmap tabBitmap = ImgUtils.getTabImg(llppdrums, imgId, 0, 2, TabManager.VERTICAL);
        //Bitmap bgBitmap = ImgUtils.getBgImg(llppdrums, imgId, TabManager.VERTICAL);
        modes.add(new SequenceModuleMode(llppdrums, drumSequence));

        //imgId = ImgUtils.getRandomImageId();
        //tabBitmap = ImgUtils.getTabImg(llppdrums, imgId, 1, 2, TabManager.VERTICAL);
        //bgBitmap = ImgUtils.getBgImg(llppdrums, imgId, TabManager.VERTICAL);
        modes.add(new AutoRandomOnOff(llppdrums, drumSequence, this));
    }

    /** LISTENER **/
    @Override
    public void onStepTouch(EngineFacade engineFacade, final ImageView stepIV, final Step step, float startX, float startY, float endX, float endY, int action){
        if(action == MotionEvent.ACTION_DOWN){

            //base
            if(isInBaseMode()) {
                if(step.getNofSubs() > 1) {
                    //release = false;
                    startTime = System.currentTimeMillis();

                    new CountDownTimer(llppdrums.getResources().getInteger(R.integer.seqStepPopupTimer), llppdrums.getResources().getInteger(R.integer.seqStepPopupTimer)) {
                        public void onTick(long millisUntilFinished) {
                            //
                        }

                        public void onFinish() {
                            if (!release) {
                                getSubsPopup(stepIV, step);
                            }
                        }
                    }.start();
                }
            }

            //random
            else{
                ((AutoRandom)modes.get(AUTO_RANDOM)).onStepTouch(engineFacade, stepIV, step, startX, startY);
            }
            release = false;
        }

        if(action == MotionEvent.ACTION_UP){
            release = true;
            long elapseTime = (System.currentTimeMillis() - startTime);
            if(elapseTime < llppdrums.getResources().getInteger(R.integer.seqStepPopupTimer) || step.getNofSubs() == 1) {
                step.setOn(!step.isOn());

                //OnOff updates the UI itself, for the other modules the popup takes care of that
                //new Thread(new Runnable() { //thread for creating the Drawable
                    //public void run() {
                        final Drawable drawable = getDrawable(step.getTrackNo(), step.getStepNo());
                        stepIV.post(new Runnable() { //modify the View in the UI thread
                            public void run() {
                                stepIV.setImageDrawable(drawable);
                            }
                        });
                    //}
                //}).start();
            }
        }
    }

    /** POPUP **/
    @Override
    public Popup getOneSubPopup(ImageView stepIV, Step step){
        //never called since we hijack the listener
        return null;
    }

    @Override
    public Popup getSubsPopup(ImageView stepIV, Step step){
        return new OnOffSubsPopup(llppdrums, OnOff.this, step, stepIV);
    }

    /** AUTO **/
    @Override
    public void setupAutoValues(){
        autoStepValues = new ArrayList<>();
        autoStepValues.add(SequenceModule.NONE);
        autoStepValues.add(SequenceModule.ALL);
        autoStepValues.add(SequenceModule.EVERY_SECOND);
        autoStepValues.add(SequenceModule.EVERY_THIRD);
        autoStepValues.add(SequenceModule.EVERY_FOURTH);
        autoStepValues.add(SequenceModule.EVERY_FIFTH);
        autoStepValues.add(SequenceModule.EVERY_SIXTH);
        autoStepValues.add(SequenceModule.EVERY_SEVENTH);
    }

    //override to store autoStepInterval
    private int autoStepInterval = -1;
    @Override
    public void setAutoValue(final DrumTrack drumTrack, String s, int sub) {
        super.setAutoValue(drumTrack, s, sub); //random is handled here
        if(isInBaseMode()) {
            autoStepInterval = Integer.parseInt(s.substring(s.length()-1));
            setAutoValueBase(drumTrack, s, sub);
        }
    }

    //since setAutoValue() is dependent on what mode we're in it can't be used for presets (if in random the patterns would end up there instead)
    //SO USE THIS WHEN BUILDING PRESETS
    @Override
    public float setAutoValueBase(final DrumTrack drumTrack, String s, int sub){
        int autoStepInterval = Integer.parseInt(s.substring(s.length()-1));
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            Step drum = drumTrack.getSteps().get(step);
            if (autoStepInterval != 0) {
                if (step % autoStepInterval == 0) {
                    //if (!drum.isOn()) {
                    drumTrack.setStepOn(step, true, drumTrack.getAutoStepValues());
                    //}
                } else {
                    if (drum.isOn()) {
                        drumTrack.setStepOn(step, false);
                    }
                }
            }
            else {
                if (drum.isOn()) {
                    drumTrack.setStepOn(step, false);
                }
            }
        }
        return autoStepInterval;
    }

    @Override
    public void pushLeft(DrumTrack drumTrack){
        if(isInBaseMode()) {
            drumTrack.pushLeft(autoStepInterval);
        }
        else{
            super.pushLeft(drumTrack);
        }
    }

    @Override
    public void pushRight(DrumTrack drumTrack){
        if(isInBaseMode()) {
            drumTrack.pushRight(autoStepInterval);
        }
        else{
            super.pushRight(drumTrack);
        }
    }

    @Override
    public void randomize(DrumTrack drumTrack){
        /*
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            Step s = drumTrack.getSteps().get(step);
            s.randomizeStepOn();
            s.randomizeSubsOn(false);
        }

         */
        drumTrack.getRndTrackManager().randomizeStepsOn();
    }

    /** GET **/
    @Override
    public boolean getAutoRndOn(Step step, int sub){
        return step.getAutoRndOn(sub);
    }

    @Override
    public float getAutoRndMin(Step step, int sub){
        return 0;
    }

    @Override
    public float getAutoRndMax(Step step, int sub){
        return 0;
    }

    @Override
    public float getAutoRndPerc(Step step, int sub){
        return step.getRndOnPerc(sub);
    }

    @Override
    public boolean getAutoRndReturn(Step step, int sub){
        return step.getRndOnReturn(sub);
    }

    @Override
    public String getLabel() {
        return llppdrums.getResources().getString(R.string.onoffTabLabel);
    }

    @Override
    public String getFullName(){
        if(isInBaseMode()) {
            return llppdrums.getResources().getString(R.string.onoffName);
        }
        else{
            return llppdrums.getResources().getString(R.string.onoffName) + llppdrums.getResources().getString(R.string.stringDivider) + ((AutoRandom)modes.get(AUTO_RANDOM)).getFullName();
        }
    }

    @Override
    public Drawable getDrawable(int trackNo, int stepNo){
        //Log.e("OnOff", "++++++++++++++++++++++++++++++++++");
        //Log.e("OnOff", "getDrawable(), trackNo: "+trackNo);
        Step step = drumSequence.getTracks().get(trackNo).getSteps().get(stepNo);
        //Log.e("OnOff", "++++++++++++++++++++++++++++++++++");

        //base
        if(isInBaseMode()) {
            return new OnOffSubsDrawable(llppdrums, step);
        }
        //random
        else{
            return ((AutoRandom)modes.get(AUTO_RANDOM)).getDrawable(step);
        }
    }

    /** SET **/
    /*
    @Override
    public void setAutoRndOn(Drum drum, boolean on){
        drum.setAutoRndOn(on);
    }

     */
    @Override
    public void setAutoRndMin(Step step, float min, int sub){
        //
    }
    @Override
    public void setAutoRndMax(Step step, float min, int sub){
        //
    }
    @Override
    public void setAutoRndPerc(Step step, float perc, int sub){
        step.setRndOnPerc(perc, sub);
    }
    @Override
    public void setAutoRndReturn(Step step, boolean r, int sub){
        step.setRndOnReturn(r, sub);
    }

    /** SEQUENCER UPDATES **/
    public void handleSequencerPositionChange(int sequencerPosition){

    }
}
