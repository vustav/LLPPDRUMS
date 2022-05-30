package com.kiefer.machine.fx;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxBitCrusherKeeper;
import com.kiefer.files.keepers.fx.FxFFKeeper;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.BitCrusherInfo;
import com.kiefer.info.sequence.trackMenu.fxManager.FFInfo;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.Random;

import nl.igorski.mwengine.core.BitCrusher;
import nl.igorski.mwengine.core.FormantFilter;

public class FxFormantFilter extends Fx {
    private float vowel;
    private SeekBar vowelSeekBar;

    public FxFormantFilter(LLPPDRUMS llppdrums, FXer fxer, final int index, boolean automation){
        super(llppdrums, fxer, index, automation);

        Random r = new Random();

        vowel = r.nextFloat();

        fx = new FormantFilter(vowel);
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxFFVowel));
    }

    /** SET **/
    private void setVowel(float value){
        vowel = NmbrUtils.removeImpossibleNumbers(value);
        ((FormantFilter)fx).setVowel(vowel);
    }

    /** GET **/
    @Override
    public View getLayout(){
        View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_ff, null);

        //amount
        vowelSeekBar = layout.findViewById(R.id.ffVowelSlider);
        vowelSeekBar.setProgress((int)(((FormantFilter)fx).getVowel() * floatMultiplier));
        vowelSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setVowel((float)(seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(1), ((float)seekBar.getProgress() / floatMultiplier));
            }
        });

        return layout;
    }

    public String getName(){
        return llppdrums.getResources().getString(R.string.fxFFName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxFFColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxFFColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return FFInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxFFKeeper keeper = (FxFFKeeper) k;
        setOn(keeper.on);
        setVowel(Float.parseFloat(keeper.vowel));

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxFFKeeper keeper = new FxFFKeeper(getFxNo(), isOn(), automationManager.getKeeper());
        keeper.vowel = Float.toString((float) ((FormantFilter)fx).getVowel());
        return keeper;
    }

    /** AUTOMATION **/
    @Override
    public float turnOnAutoValue(String param, float autoValue, boolean popupShowing){
        boolean updateUI = popupShowing && fxer.getFxManager().getSelectedFx() == this;

        //on
        if(param.equals(paramNames.get(0))){
            return super.turnOnAutoValue(param, autoValue, popupShowing);
            //Log.e("AutomationManager", "automate(), orgVal: ");
        }

        //vowel
        else if(param.equals(paramNames.get(1))){
            float ogVowel = vowel;

            if(updateUI) {
                vowelSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setVowel(autoValue);
            }

            return ogVowel;
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

        //vowel
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                vowelSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setVowel(oldValue);
            }
        }
    }
}
