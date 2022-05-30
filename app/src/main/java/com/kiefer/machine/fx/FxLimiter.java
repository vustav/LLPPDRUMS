package com.kiefer.machine.fx;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxLimiterKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.LimiterInfo;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.Random;

import nl.igorski.mwengine.core.Limiter;

public class FxLimiter extends Fx {
    private float attack, release, threshold;
    private SeekBar attackSeekBar, releaseSeekBar, thresholdSeekBar;

    public FxLimiter(LLPPDRUMS llppdrums, FXer fxer, final int index, boolean automation){
        super(llppdrums, fxer, index, automation);

        Random r = new Random();

        attack = r.nextFloat();
        release = r.nextFloat();
        threshold = r.nextFloat();

        fx = new Limiter(attack, release, threshold);
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxLimiterAttack));
        paramNames.add(llppdrums.getResources().getString(R.string.fxLimiterRelease));
        paramNames.add(llppdrums.getResources().getString(R.string.fxLimiterThreshold));
    }

    /** SET **/
    private void setAttack(float value){
        attack = NmbrUtils.removeImpossibleNumbers(value);
        ((Limiter)fx).setAttack(attack);
    }
    private void setRelease(float value){
        release = NmbrUtils.removeImpossibleNumbers(value);
        ((Limiter)fx).setRelease(release);
    }
    private void setThreshold(float value){
        threshold = NmbrUtils.removeImpossibleNumbers(value);
        ((Limiter)fx).setThreshold(threshold);
    }

    /** GET **/
    @Override
    public View getLayout(){
        View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_limiter, null);

        //amount
        attackSeekBar = layout.findViewById(R.id.limiterAttackSlider);
        attackSeekBar.setProgress((int)(((Limiter)fx).getAttack() * floatMultiplier));
        attackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setAttack((float)(seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(1), ((float)seekBar.getProgress() / floatMultiplier));
            }
        });

        //release
        releaseSeekBar = layout.findViewById(R.id.limiterReleaseSlider);
        releaseSeekBar.setProgress((int)(((Limiter)fx).getRelease() * floatMultiplier));
        releaseSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setRelease((float)(seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(2), ((float)seekBar.getProgress() / floatMultiplier));
            }
        });

        //threshold
        thresholdSeekBar = layout.findViewById(R.id.limiterThresholdSlider);
        thresholdSeekBar.setProgress((int)(((Limiter)fx).getThreshold() * floatMultiplier));
        thresholdSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setThreshold((float)(seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(3), ((float)seekBar.getProgress() / floatMultiplier));
            }
        });

        return layout;
    }

    public String getName(){
        return llppdrums.getResources().getString(R.string.fxLimiterName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxLimiterColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxLimiterColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return LimiterInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxLimiterKeeper keeper = (FxLimiterKeeper) k;
        setOn(keeper.on);
        setAttack(Float.parseFloat(keeper.attack));
        setRelease(Float.parseFloat(keeper.release));
        setThreshold(Float.parseFloat(keeper.threshold));

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxLimiterKeeper keeper = new FxLimiterKeeper(getFxNo(), isOn(), automationManager.getKeeper());
        keeper.attack = Float.toString((float) ((Limiter)fx).getAttack());
        keeper.release = Float.toString((float) ((Limiter)fx).getRelease());
        keeper.threshold = Float.toString((float) ((Limiter)fx).getThreshold());
        return keeper;
    }

    /** AUTOMATION **/
    @Override
    public float turnOnAutoValue(String param, float autoValue, boolean popupShowing){
        boolean updateUI = popupShowing && fxer.getFxManager().getSelectedFx() == this;

        //on
        if(param.equals(paramNames.get(0))){
            return super.turnOnAutoValue(param, autoValue, popupShowing);
        }

        //attack
        else if(param.equals(paramNames.get(1))){
            float ogAttack = attack;

            if(updateUI) {
                attackSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setAttack(autoValue);
            }

            return ogAttack;
        }

        //release
        else if(param.equals(paramNames.get(2))){
            float ogRelease = release;

            if(updateUI) {
                releaseSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setRelease(autoValue);
            }

            return ogRelease;
        }

        //threshold
        else if(param.equals(paramNames.get(3))){
            float ogThreshold = threshold;

            if(updateUI) {
                thresholdSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setThreshold(autoValue);
            }

            return ogThreshold;
        }
        return -1;
    }

    @Override
    public void turnOffAutoValue(String param, float oldValue, boolean popupShowing){
        boolean updateUI = popupShowing && fxer.getFxManager().getSelectedFx() == this;

        //on
        if(param.equals(paramNames.get(0))){
            super.turnOffAutoValue(param, oldValue, popupShowing);
        }

        //attack
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                attackSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setAttack(oldValue);
            }
        }

        //release
        else if(param.equals(paramNames.get(2))){
            if(updateUI) {
                releaseSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setRelease(oldValue);
            }
        }

        //threshold
        else if(param.equals(paramNames.get(3))){
            if(updateUI) {
                thresholdSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setThreshold(oldValue);
            }
        }
    }
}
