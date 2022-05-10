package com.kiefer.machine.fx;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxDelayKeeper;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.DelayInfo;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.Random;

import nl.igorski.mwengine.core.Delay;

public class FxDelay extends Fx {
    private final int MAX_DELAY = 1000; //ms
    private int time;
    private float feedback, mix;
    private SeekBar timeSeekBar, feedbackSeekBar, mixSeekBar;

    public FxDelay(LLPPDRUMS llppdrums, DrumTrack drumTrack, int index, boolean automation){
        super(llppdrums, drumTrack, index, automation);

        Random r = new Random();

        time = r.nextInt(MAX_DELAY);
        feedback = r.nextFloat();
        mix = r.nextFloat();

        fx = new Delay(time, MAX_DELAY, feedback, mix, 2);
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxDelayTime));
        paramNames.add(llppdrums.getResources().getString(R.string.fxDelayFeedback));
        paramNames.add(llppdrums.getResources().getString(R.string.fxDelayMix));
    }

    /** SELECTION **/
    /*
    @Override
    public void select(){
        timeSeekBar.setProgress(time);
        feedbackSeekBar.setProgress((int)(feedback * floatMultiplier));
        mixSeekBar.setProgress((int)(mix * floatMultiplier));
    }

     */

    /** SET **/
    private void setTime(int value){
        time = value;
        ((Delay)fx).setDelayTime(value + 5); //just to avoid 0
    }

    private void setFeedback(float value){
        //feedback = ((float) value) / floatMultiplier;
        feedback = NmbrUtils.removeImpossibleNumbers(value);
        ((Delay)fx).setFeedback(feedback);
    }

    private void setMix(float value){
        //mix = ((float) value) / floatMultiplier;
        mix = NmbrUtils.removeImpossibleNumbers(value);
        ((Delay)fx).setMix(mix);
    }

    /** GET **/
    @Override
    public View getLayout(){
        View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_delay, null);

        //time
        timeSeekBar = layout.findViewById(R.id.delayTimeSlider);
        timeSeekBar.setMax(MAX_DELAY);
        timeSeekBar.setProgress(((Delay)fx).getDelayTime());
        //Log.e("FxDelay", "getLayout(), timeSeekBar.setProgress(): "+((Delay)fx).getDelayTime());
        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setTime(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(1), seekBar.getProgress());
            }
        });

        //feedback
        feedbackSeekBar = layout.findViewById(R.id.delayFeedbackSlider);
        feedbackSeekBar.setProgress((int)(((Delay)fx).getFeedback() * floatMultiplier));
        feedbackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setFeedback(((float)seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(2), ((float)seekBar.getProgress()) / floatMultiplier);
            }
        });

        //mix
        mixSeekBar = layout.findViewById(R.id.delayMixSlider);
        mixSeekBar.setProgress((int)(((Delay)fx).getMix() * floatMultiplier));
        mixSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setMix(((float)seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(3), ((float)seekBar.getProgress()) / floatMultiplier);
            }
        });

        return layout;
    }

    public String getName(){
        return llppdrums.getResources().getString(R.string.fxDelayName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxDelayColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxDelayColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return DelayInfo.key;
    }

    /** RESTORATION **/
    @Override
    public void restore(FxKeeper k){
        FxDelayKeeper keeper = (FxDelayKeeper) k;
        setOn(keeper.on);
        setTime(keeper.time);
        setFeedback(Float.parseFloat(keeper.feedback));
        setMix(Float.parseFloat(keeper.mix));

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxDelayKeeper keeper = new FxDelayKeeper(getFxNo(), isOn(), automationManager.getKeeper());
        keeper.time = ((Delay)fx).getDelayTime();
        //Log.e("FxDelay", "getKeeper, time: "+((Delay)fx).getDelayTime());
        keeper.feedback = Float.toString(((Delay)fx).getFeedback());
        keeper.mix = Float.toString(((Delay)fx).getMix());
        return keeper;
    }

    /** AUTOMATION **/
    @Override
    public float turnOnAutoValue(String param, float autoValue, boolean popupShowing){
        boolean updateUI = popupShowing && drumTrack.getFxManager().getSelectedFx() == this;

        //on
        if(param.equals(paramNames.get(0))){
            return super.turnOnAutoValue(param, autoValue, popupShowing);
        }

        //time
        else if(param.equals(paramNames.get(1))){
            int ogTime = time;
            int autoTime = (int)(autoValue * MAX_DELAY);

            if(updateUI) {
                timeSeekBar.setProgress(autoTime);
            }
            else{
                setTime(autoTime);
            }
            return ogTime;
        }

        //feedback
        else if(param.equals(paramNames.get(2))){
            float ogFeedback = feedback;
            //float autoFeedback = autoValue;

            if(updateUI) {
                feedbackSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setFeedback(autoValue);
            }

            return ogFeedback;
        }

        //mix
        else if(param.equals(paramNames.get(3))){
            float ogMix = mix;
            //float autoMix = autoValue;

            if(updateUI) {
                mixSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setMix(autoValue);
            }

            return ogMix;
        }
        return -1;
    }

    @Override
    public void turnOffAutoValue(String param, float oldValue, boolean popupShowing){
        boolean updateUI = popupShowing && drumTrack.getFxManager().getSelectedFx() == this;

        //on
        if (param.equals(paramNames.get(0))) {
            super.turnOffAutoValue(param, oldValue, popupShowing);
        }

        //time
        else if (param.equals(paramNames.get(1))) {
            if (updateUI) {
                timeSeekBar.setProgress((int) oldValue);
            } else {
                setTime((int) oldValue);
            }
        }

        //feedback
        else if (param.equals(paramNames.get(2))) {
            if (updateUI) {
                feedbackSeekBar.setProgress((int) (oldValue * floatMultiplier));
            } else {
                setFeedback(oldValue);
            }
        }

        //mix
        else if (param.equals(paramNames.get(3))) {
            if (updateUI) {
                mixSeekBar.setProgress((int) (oldValue * floatMultiplier));
            } else {
                setMix(oldValue);
            }
        }
    }
}
