package com.kiefer.machine.fx;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxWSKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.WSInfo;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.Random;

import nl.igorski.mwengine.core.WaveShaper;

public class FxWaveShaper extends Fx {
    private float amount, level;
    private SeekBar amountSeekBar, levelSeekBar;

    public FxWaveShaper(LLPPDRUMS llppdrums, FXer fxer, final int index, boolean automation){
        super(llppdrums, fxer, index, automation);

        Random r = new Random();

        amount = r.nextFloat();
        level = r.nextFloat();

        fx = new WaveShaper(amount, level);
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxWSName));
        paramNames.add(llppdrums.getResources().getString(R.string.fxWSAmount));
        paramNames.add(llppdrums.getResources().getString(R.string.fxWSLevel));
    }

    /** SET **/
    private void setAmount(float value){
        amount = NmbrUtils.removeImpossibleNumbers(value);
        ((WaveShaper)fx).setAmount(amount);
    }

    private void setLevel(float value){
        level = NmbrUtils.removeImpossibleNumbers(value);
        ((WaveShaper)fx).setLevel(level);
    }

    /** GET **/
    @Override
    public View getLayout(){
        View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_ws, null);

        //amount
        amountSeekBar = layout.findViewById(R.id.wsAmountSlider);
        amountSeekBar.setProgress((int)(((WaveShaper)fx).getAmount() * floatMultiplier));
        amountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setAmount((float)(seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(1), ((float)seekBar.getProgress() / floatMultiplier));
            }
        });

        //level
        levelSeekBar = layout.findViewById(R.id.wsLevelSlider);
        levelSeekBar.setProgress((int)(((WaveShaper)fx).getLevel() * floatMultiplier));
        levelSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setLevel((float)(seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(2), ((float)seekBar.getProgress() / floatMultiplier));
            }
        });

        return layout;
    }

    public String getName(){
        return llppdrums.getResources().getString(R.string.fxWSName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxWSColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxWSColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return WSInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxWSKeeper keeper = (FxWSKeeper) k;
        setOn(keeper.on);
        setAmount(Float.parseFloat(keeper.amount));
        setLevel(Float.parseFloat(keeper.level));

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxWSKeeper keeper = new FxWSKeeper(getFxNo(), isOn(), automationManager.getKeeper());
        keeper.amount = Float.toString(((WaveShaper)fx).getAmount());
        keeper.level = Float.toString(((WaveShaper)fx).getLevel());
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

        //amount
        else if(param.equals(paramNames.get(1))){
            float ogAmount = amount;

            if(updateUI) {
                amountSeekBar.setProgress((int)((autoValue) * floatMultiplier));
            }
            else{
                setAmount(autoValue);
            }

            return ogAmount;
        }

        //in
        else if(param.equals(paramNames.get(2))){
            float ogLevel = level;

            if(updateUI) {
                levelSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setLevel(autoValue);
            }

            return ogLevel;
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

        //amount
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                amountSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setAmount(oldValue);
            }
        }

        //in
        else if(param.equals(paramNames.get(2))){
            if(updateUI) {
                levelSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setLevel(oldValue);
            }
        }
    }
}
