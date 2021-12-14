package com.kiefer.machine.fx;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxFlangerKeeper;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.FlangerInfo;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.Random;

import nl.igorski.mwengine.core.Flanger;

public class FxFlanger extends Fx {
    private float rate, width, feedback, delay, mix;
    private SeekBar rateSeekBar, widthSeekBar, feedbackSeekBar, delaySeekBar, mixSeekBar;

    public FxFlanger(LLPPDRUMS llppdrums, DrumTrack drumTrack, int index, boolean automation){
        super(llppdrums, drumTrack, index, automation);

        layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_flanger, null);

        Random r = new Random();

        rate = r.nextFloat();
        width = r.nextFloat();
        feedback = r.nextFloat();
        delay = r.nextFloat();
        mix = r.nextFloat();

        fx = new Flanger(rate, width, feedback, delay, mix);

        //rate
        rateSeekBar = layout.findViewById(R.id.flangerRateSlider);
        rateSeekBar.setProgress((int)(((Flanger)fx).getRate() * floatMultiplier));
        rateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setRate(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //width
        widthSeekBar = layout.findViewById(R.id.flangerWidthSlider);
        widthSeekBar.setProgress((int)(((Flanger)fx).getWidth() * floatMultiplier));
        widthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setWidth(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //feedback
        feedbackSeekBar = layout.findViewById(R.id.flangerFeedSlider);
        feedbackSeekBar.setProgress((int)(((Flanger)fx).getFeedback() * floatMultiplier));
        feedbackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setFeedback(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //delay
        delaySeekBar = layout.findViewById(R.id.flangerDelaySlider);
        delaySeekBar.setProgress((int)(((Flanger)fx).getDelay() * floatMultiplier));
        delaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setDelay(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //mix
        mixSeekBar = layout.findViewById(R.id.flangerMixSlider);
        mixSeekBar.setProgress((int)(((Flanger)fx).getMix() * floatMultiplier));
        mixSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setMix(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxFlangerRate));
        paramNames.add(llppdrums.getResources().getString(R.string.fxFlangerWidth));
        paramNames.add(llppdrums.getResources().getString(R.string.fxFlangerFeedback));
        paramNames.add(llppdrums.getResources().getString(R.string.fxFlangerDelay));
        paramNames.add(llppdrums.getResources().getString(R.string.fxFlangerMix));
    }

    /** SELECTION **/
    public void select(){
        rateSeekBar.setProgress((int)(rate * floatMultiplier));
        widthSeekBar.setProgress((int)(width * floatMultiplier));
        feedbackSeekBar.setProgress((int)(feedback * floatMultiplier));
        delaySeekBar.setProgress((int)(delay * floatMultiplier));
        mixSeekBar.setProgress((int)(mix * floatMultiplier));
    }

    /** SET **/
    private void setRate(int value){
        rate = ((float) value) / floatMultiplier;
        ((Flanger)fx).setRate(NmbrUtils.removeImpossibleNumbers(rate));
    }

    private void setWidth(int value){
        width = ((float) value) / floatMultiplier;
        ((Flanger)fx).setWidth(NmbrUtils.removeImpossibleNumbers(width));
    }

    private void setFeedback(int value){
        feedback = ((float) value) / floatMultiplier;
        ((Flanger)fx).setFeedback(NmbrUtils.removeImpossibleNumbers(feedback));
    }

    private void setDelay(int value){
        delay = ((float) value) / floatMultiplier;
        ((Flanger)fx).setDelay(NmbrUtils.removeImpossibleNumbers(delay));
    }

    private void setMix(int value){
        mix = ((float) value) / floatMultiplier;
        ((Flanger)fx).setMix(NmbrUtils.removeImpossibleNumbers(mix));
    }

    /** GET **/
    public String getName(){
        return llppdrums.getResources().getString(R.string.fxFlangerName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxFlangerColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxFlangerColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return FlangerInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxFlangerKeeper keeper = (FxFlangerKeeper) k;
        setOn(keeper.on);
        ((Flanger)fx).setRate(Float.parseFloat(keeper.rate));
        ((Flanger)fx).setWidth(Float.parseFloat(keeper.width));
        ((Flanger)fx).setFeedback(Float.parseFloat(keeper.feedback));
        ((Flanger)fx).setDelay(Float.parseFloat(keeper.delay));
        ((Flanger)fx).setMix(Float.parseFloat(keeper.mix));

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxFlangerKeeper keeper = new FxFlangerKeeper(getIndex(), isOn(), automationManager.getKeeper());
        keeper.rate = Float.toString(((Flanger)fx).getRate());
        keeper.width = Float.toString(((Flanger)fx).getWidth());
        keeper.feedback = Float.toString(((Flanger)fx).getFeedback());
        keeper.delay = Float.toString(((Flanger)fx).getDelay());
        keeper.mix = Float.toString(((Flanger)fx).getMix());
        return keeper;
    }

    /** AUTOMATION **/
    @Override
    public float turnOnAutoValue(String param, float autoValue, boolean popupShowing){
        boolean updateUI = popupShowing && drumTrack.getFxManager().getSelectedFx() == this;

        //on
        if(param.equals(paramNames.get(0))){
            super.turnOnAutoValue(param, autoValue, popupShowing);
        }

        //rate
        else if(param.equals(paramNames.get(1))){
            float ogRate = rate;
            int autoRate = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                rateSeekBar.setProgress(autoRate);
            }
            else{
                setRate(autoRate);
            }
            return ogRate;
        }

        //width
        else if(param.equals(paramNames.get(2))){
            float ogWidth = width;
            int autoWidth = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                widthSeekBar.setProgress(autoWidth);
            }
            else{
                setWidth(autoWidth);
            }

            return ogWidth;
        }

        //feedback
        else if(param.equals(paramNames.get(3))){
            float ogFeedback = feedback;
            int autoFeedback = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                feedbackSeekBar.setProgress(autoFeedback);
            }
            else{
                setFeedback(autoFeedback);
            }

            return ogFeedback;
        }

        //delay
        else if(param.equals(paramNames.get(4))){
            float ogDelay = delay;
            int autoDelay = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                delaySeekBar.setProgress(autoDelay);
            }
            else{
                setDelay(autoDelay);
            }

            return ogDelay;
        }

        //mix
        else if(param.equals(paramNames.get(5))){
            float ogMix = mix;
            int autoMix = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                mixSeekBar.setProgress(autoMix);
            }
            else{
                setMix(autoMix);
            }

            return ogMix;
        }
        return -1;
    }

    @Override
    public void turnOffAutoValue(String param, float oldValue, boolean popupShowing){
        boolean updateUI = popupShowing && drumTrack.getFxManager().getSelectedFx() == this;

        //on
        if(param.equals(paramNames.get(0))){
            super.turnOffAutoValue(param, oldValue, popupShowing);
        }

        //rate
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                rateSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setRate((int)(oldValue * floatMultiplier));
            }
        }

        //width
        else if(param.equals(paramNames.get(2))){
            if(updateUI) {
                widthSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setWidth((int)(oldValue * floatMultiplier));
            }
        }

        //feedback
        else if(param.equals(paramNames.get(3))){
            if(updateUI) {
                feedbackSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setFeedback((int)(oldValue * floatMultiplier));
            }
        }

        //delay
        else if(param.equals(paramNames.get(4))){
            if(updateUI) {
                delaySeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setDelay((int)(oldValue * floatMultiplier));
            }
        }

        //mix
        else if(param.equals(paramNames.get(5))){
            if(updateUI) {
                mixSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setMix((int)(oldValue * floatMultiplier));
            }
        }
    }
}
