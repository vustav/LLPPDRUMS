package com.kiefer.machine.sequence.sequenceModules;

import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.util.Log;
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
import com.kiefer.machine.sequence.DrumSequence;

import java.util.ArrayList;

public class OnOff extends SequenceModule {

    public OnOff(LLPPDRUMS llppdrums, DrumSequence drumSequence, int tabIndex){
        super(llppdrums, drumSequence, tabIndex);
    }

    @Override
    void setupModes(){
        modes = new ArrayList<>();
        modes.add(new SequenceModuleMode(llppdrums, drumSequence, 0));
        modes.add(new AutoRandomOnOff(llppdrums, drumSequence, this, 1));
    }

    /** LISTENER **/
    private float currentX = 0;
    @Override
    public void onStepTouch(EngineFacade engineFacade, final ImageView stepIV, final Step step, float startX, float startY, float endX, float endY, int action){
        currentX = endX;
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
                                /** FÖRSÖK FIXA NÅT SÅ DEN INTE ÖPPNAS OM MAN SCROLLAR (den i DrumMachine funkar inte på onOff) **/
                                //if(Math.abs(startX - currentX) < 1) {
                                    //Log.e("ASD", ": "+(Math.abs(startX - currentX)));
                                    getSubsPopup(stepIV, step);
                                //}
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

        /** BUGGAR MED SNABBA KLICK MEN SKA FIXAS AV ENGINE-MAKAREN! **/
        if(action == MotionEvent.ACTION_UP){
            if(isInBaseMode()) {
                release = true;
                long elapseTime = (System.currentTimeMillis() - startTime);
                if (elapseTime < llppdrums.getResources().getInteger(R.integer.seqStepPopupTimer) || step.getNofSubs() == 1) {
                    if (timerOnClicks) {
                        if (readyToChange) {
                            step.setOn(!step.isOn());
                            final Drawable drawable = getDrawable(step.getTrackNo(), step.getStepNo());
                            stepIV.setImageDrawable(drawable);
                        }
                        readyToChange = false;

                        if (timerFree) {
                            new CountDownTimer(1400, 1400) {
                                public void onTick(long millisUntilFinished) {
                                    timerFree = false;
                                }

                                public void onFinish() {
                                    timerFree = true;
                                    readyToChange = true;
                                }
                            }.start();
                        }
                    }
                    else {
                        step.setOn(!step.isOn());
                        final Drawable drawable = getDrawable(step.getTrackNo(), step.getStepNo());
                        stepIV.setImageDrawable(drawable);

                        //setImageDrawable(step.getTrackNo(), step.getStepNo(), stepIV, getDrawable(step.getTrackNo(), step.getStepNo()));
                    }
                }
            }
        }
    }
    //till timern ovan
    private boolean timerOnClicks = false;
    private boolean readyToChange = true, timerFree = true;

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

    private int autoStepInterval = -1;

    //since setAutoValue() is dependent on what mode we're in it can't be used for presets (if in random the patterns would end up there instead)
    //SO USE THIS WHEN BUILDING PRESETS

    @Override
    public float setAutoValueBase(final DrumTrack drumTrack, String s, int sub){
        autoStepInterval = Integer.parseInt(s.substring(s.length()-1));
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            Step drum = drumTrack.getSteps().get(step);
            if (autoStepInterval != 0) {
                if (step % autoStepInterval == 0) {
                    //if (!drum.isOn()) {
                    drumTrack.setStepOn(step, true, drumTrack.getOrganizeStepsSubValues());
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

    /*
    @Override
    public float setAutoValueBase(final DrumTrack drumTrack, String s, int sub){
        boolean aSubIsOn = false;
        autoStepInterval = Integer.parseInt(s.substring(s.length()-1));
        for (int stepNo = 0; stepNo < drumTrack.getSteps().size(); stepNo++) {
            Step step = drumTrack.getSteps().get(stepNo);
            if (autoStepInterval != 0) {
                if (stepNo % autoStepInterval == 0) {
                    step.setSubOn(sub, true);
                    aSubIsOn = true;
                } else {
                    if (step.isOn()) {
                        //drumTrack.setStepOn(stepNo, false);
                        step.setOn(false);
                    }
                }
            }
            else {
                if (step.isOn()) {
                    //drumTrack.setStepOn(stepNo, false);
                    step.setOn(false);
                }
            }
            if(aSubIsOn){
                step.setOn(true);
            }
        }
        return autoStepInterval;
    }

     */

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
    public String getName() {
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
        Step step = drumSequence.getTracks().get(trackNo).getSteps().get(stepNo);

        //base
        if(isInBaseMode()) {
            return new OnOffSubsDrawable(llppdrums, this, step);
        }
        //random
        else{
            return ((AutoRandom)modes.get(AUTO_RANDOM)).getDrawable(step);
        }
    }

    /** SET **/
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
        Log.e("OnOff", "setAutoRndPerc()");
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
