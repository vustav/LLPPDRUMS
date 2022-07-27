package com.kiefer.machine.sequence.track.Stackables.fx.fxs;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.SeekBar;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxLPHPKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.HPInfo;
import com.kiefer.machine.sequence.track.Stackables.fx.FxManager;
import com.kiefer.machine.sequence.track.Stackables.fx.Fxer;
import com.kiefer.utils.ColorUtils;

import java.util.Random;

import nl.igorski.mwengine.MWEngine;
import nl.igorski.mwengine.core.LPFHPFilter;

public class FxHPFilter extends Fx {
    private int hp, maxHP = 5000;
    private SeekBar hpSeekBar;

    public FxHPFilter(LLPPDRUMS llppdrums, FxManager fxManager, Fxer fxer, final int index, boolean automation){
        super(llppdrums, fxManager, fxer, index, automation);

        Random r = new Random();

        hp = r.nextInt(maxHP);
        int lp = MWEngine.SAMPLE_RATE;

        fx = new LPFHPFilter(lp, hp, 2);
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxHPLPHP));
    }

    /** SET **/
    private void setHP(int value){
        hp = value;
        ((LPFHPFilter)fx).setHPF(value, llppdrums.getEngineFacade().getSAMPLE_RATE());
    }

    /** GET **/
    @Override
    public View getLayout(){
        View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_hp, null);

        //hp
        hpSeekBar = layout.findViewById(R.id.hpSlider);
        hpSeekBar.setMax(maxHP);
        hpSeekBar.setProgress(hp);
        hpSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setHP(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(1), seekBar.getProgress());
            }
        });

        return layout;
    }

    public String getName(){
        return llppdrums.getResources().getString(R.string.fxHPName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxHPColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxHPColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return HPInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxLPHPKeeper keeper = (FxLPHPKeeper) k;
        setOn(keeper.on);
        setHP(keeper.value);

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxLPHPKeeper keeper = new FxLPHPKeeper(getFxNo(), isOn(), automationManager.getKeeper());
        keeper.value = hp;
        return keeper;
    }
    /** AUTOMATION **/
    @Override
    public float turnOnAutoValue(String param, float autoValue, boolean popupShowing){
        boolean updateUI = popupShowing && fxManager.getSelectedStackable() == this;

        //on
        if(param.equals(paramNames.get(0))){
            return super.turnOnAutoValue(param, autoValue, popupShowing);
        }

        //hp
        else if(param.equals(paramNames.get(1))){
            int ogHP = hp;
            int autoHP = (int)(autoValue * maxHP);

            if(updateUI) {
                hpSeekBar.setProgress(autoHP);
            }
            else{
                setHP(autoHP);
            }

            return ogHP;
        }
        return -1;
    }

    @Override
    public void turnOffAutoValue(String param, float oldValue, boolean popupShowing){
        boolean updateUI = popupShowing && fxManager.getSelectedStackable() == this;

        //on
        if(param.equals(paramNames.get(0))){
            super.turnOffAutoValue(param, oldValue, popupShowing);
        }

        //hp
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                hpSeekBar.setProgress((int)oldValue);
            }
            else{
                setHP((int)oldValue);
            }
        }
    }
}
