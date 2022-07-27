package com.kiefer.machine.sequence.track.Stackables.fx.fxs;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxLPHPKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.HPLPInfo;
import com.kiefer.machine.sequence.track.Stackables.fx.FxManager;
import com.kiefer.machine.sequence.track.Stackables.fx.Fxer;
import com.kiefer.utils.ColorUtils;

import java.util.Random;

import nl.igorski.mwengine.core.LPFHPFilter;

public class FxLPFilter extends Fx {
    private int lp;
    private SeekBar lpSeekBar;
    int maxLP = 15000, lpRange = 15000;


    public FxLPFilter(LLPPDRUMS llppdrums, FxManager fxManager, Fxer fxer, final int index, boolean automation){
        super(llppdrums, fxManager, fxer, index, automation);

        Random r = new Random();
        lp = r.nextInt(lpRange) + maxLP - lpRange;

        fx = new LPFHPFilter(lp, 0, 2);
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxHPLPLP));
    }

    /** SET **/
    private void setLP(int value){
        lp = value;
        ((LPFHPFilter)fx).setLPF(value, llppdrums.getEngineFacade().getSAMPLE_RATE());
    }

    /** GET **/
    @Override
    public View getLayout(){
        View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_lp, null);

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
                automationManager.changeInBaseValue(paramNames.get(1), seekBar.getProgress() + maxLP - lpRange);
            }
        });

        return layout;
    }

    public String getName(){
        return llppdrums.getResources().getString(R.string.fxLPName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxLPColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxLPColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return HPLPInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxLPHPKeeper keeper = (FxLPHPKeeper) k;
        setOn(keeper.on);
        setLP(keeper.value);

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxLPHPKeeper keeper = new FxLPHPKeeper(getFxNo(), isOn(), automationManager.getKeeper());
        keeper.value = lp;
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

        //lp
        else if(param.equals(paramNames.get(1))){
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
        boolean updateUI = popupShowing && fxManager.getSelectedStackable() == this;

        //on
        if(param.equals(paramNames.get(0))){
            super.turnOffAutoValue(param, oldValue, popupShowing);
        }

        //lp
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                lpSeekBar.setProgress((int)oldValue - (maxLP - lpRange));
            }
            else{
                setLP((int)oldValue);
            }
        }
    }
}
