package com.kiefer.machine.fx;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxLPHPKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.HPLPInfo;
import com.kiefer.utils.ColorUtils;

import java.util.Random;

import nl.igorski.mwengine.core.LPFHPFilter;

public class FxHPLPFilter extends Fx {
    private int hp, lp;
    private SeekBar hpSeekBar, lpSeekBar;
    int maxHP = 5000;
    int maxLP = 15000, lpRange = 15000;


    public FxHPLPFilter(LLPPDRUMS llppdrums, FXer fxer, final int index, boolean automation){
        super(llppdrums, fxer, index, automation);

        Random r = new Random();

        hp = r.nextInt(maxHP);
        lp = r.nextInt(lpRange) + maxLP - lpRange;

        fx = new LPFHPFilter(lp, hp, 2);
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxHPLPHP));
        paramNames.add(llppdrums.getResources().getString(R.string.fxHPLPLP));
    }

    /** SET **/
    private void setHP(int value){
        hp = value;
        ((LPFHPFilter)fx).setHPF(value, llppdrums.getEngineFacade().getSAMPLE_RATE());
    }

    private void setLP(int value){
        lp = value;
        ((LPFHPFilter)fx).setLPF(value, llppdrums.getEngineFacade().getSAMPLE_RATE());
    }

    /** GET **/
    @Override
    public View getLayout(){
        View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_hplp, null);

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

        //lp
        lpSeekBar = layout.findViewById(R.id.lpSlider);
        lpSeekBar.setMax(lpRange);
        lpSeekBar.setProgress(lp - (maxLP - lpRange));
        lpSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setLP(seekBar.getProgress() + maxLP - lpRange);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(2), seekBar.getProgress() + maxLP - lpRange);
            }
        });

        return layout;
    }

    public String getName(){
        return llppdrums.getResources().getString(R.string.fxHPLPName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxLPHPColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxLPHPColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return HPLPInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxLPHPKeeper keeper = (FxLPHPKeeper) k;
        setOn(keeper.on);
        setLP(keeper.lp);
        setHP(keeper.hp);

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxLPHPKeeper keeper = new FxLPHPKeeper(getFxNo(), isOn(), automationManager.getKeeper());
        keeper.lp = lp;
        keeper.hp = hp;
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

        //lp
        else if(param.equals(paramNames.get(2))){
            float ogLP = lp;
            int autoLP = (int)(autoValue * maxLP);

            if(updateUI) {
                lpSeekBar.setProgress(autoLP - (maxLP - lpRange));
            }
            else{
                setLP(autoLP);
            }

            return ogLP;
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

        //hp
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                hpSeekBar.setProgress((int)oldValue);
            }
            else{
                setHP((int)oldValue);
            }
        }

        //lp
        else if(param.equals(paramNames.get(2))){
            if(updateUI) {
                lpSeekBar.setProgress((int)oldValue - (maxLP - lpRange));
            }
            else{
                setLP((int)oldValue);
            }
        }
    }
}
