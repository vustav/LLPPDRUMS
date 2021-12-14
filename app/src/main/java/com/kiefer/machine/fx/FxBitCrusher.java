package com.kiefer.machine.fx;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxBitCrusherKeeper;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.BitCrusherInfo;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.Random;

import nl.igorski.mwengine.core.BitCrusher;

public class FxBitCrusher extends Fx {
    private float amount, inMix, outMix;
    private final SeekBar amountSeekBar, inSeekBar, outSeekBar;

    public FxBitCrusher(LLPPDRUMS llppdrums, DrumTrack drumTrack, final int index, boolean automation){
        super(llppdrums, drumTrack, index, automation);

        layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_bit_crusher, null);

        Random r = new Random();

        amount = r.nextFloat();
        inMix = r.nextFloat();
        outMix = r.nextFloat();

        fx = new BitCrusher(amount, inMix, outMix);

        //amount
        amountSeekBar = layout.findViewById(R.id.bitAmountSlider);
        amountSeekBar.setProgress((int)(((BitCrusher)fx).getAmount() * floatMultiplier));
        amountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setAmount(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //in
        inSeekBar = layout.findViewById(R.id.bitInSlider);
        inSeekBar.setProgress((int)(((BitCrusher)fx).getInputMix() * floatMultiplier));
        inSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setIn(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //out
        outSeekBar = layout.findViewById(R.id.bitOutSlider);
        outSeekBar.setProgress((int)(((BitCrusher)fx).getOutputMix() * floatMultiplier));
        outSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setOut(seekBar.getProgress());
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
        paramNames.add(llppdrums.getResources().getString(R.string.fxBitCrusherAmout));
        paramNames.add(llppdrums.getResources().getString(R.string.fxBitCrusherIn));
        paramNames.add(llppdrums.getResources().getString(R.string.fxBitCrusherOut));
    }

    /** SELECTION **/
    public void select(){
    }

    /** SET **/
    private void setAmount(int value){
        amount = ((float) value) / floatMultiplier;
        ((BitCrusher)fx).setAmount(NmbrUtils.removeImpossibleNumbers(amount));
    }

    private void setIn(int value){
        inMix = ((float) value) / floatMultiplier;
        ((BitCrusher)fx).setInputMix(NmbrUtils.removeImpossibleNumbers(inMix));
    }

    private void setOut(int value){
        outMix = ((float) value) / floatMultiplier;
        ((BitCrusher)fx).setOutputMix(NmbrUtils.removeImpossibleNumbers(outMix));
    }

    /** GET **/
    public String getName(){
        return llppdrums.getResources().getString(R.string.fxBitCrusherName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxBitCrusherColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxBitCrusherColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return BitCrusherInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxBitCrusherKeeper keeper = (FxBitCrusherKeeper) k;
        setOn(keeper.on);
        ((BitCrusher)fx).setAmount(Float.parseFloat(keeper.amount));
        ((BitCrusher)fx).setInputMix(Float.parseFloat(keeper.inMix));
        ((BitCrusher)fx).setOutputMix(Float.parseFloat(keeper.outMix));

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxBitCrusherKeeper keeper = new FxBitCrusherKeeper(getIndex(), isOn(), automationManager.getKeeper());
        keeper.amount = Float.toString(((BitCrusher)fx).getAmount());
        keeper.inMix = Float.toString(((BitCrusher)fx).getInputMix());
        keeper.outMix = Float.toString(((BitCrusher)fx).getOutputMix());
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

        //amount
        else if(param.equals(paramNames.get(1))){
            float ogAmount = amount;
            int autoAmount = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                amountSeekBar.setProgress(autoAmount);
            }
            else{
                setAmount(autoAmount);
            }

            return ogAmount;
        }

        //in
        else if(param.equals(paramNames.get(2))){
            float ogIn = inMix;
            int autoIn = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                inSeekBar.setProgress(autoIn);
            }
            else{
                setIn(autoIn);
            }

            return ogIn;
        }

        //out
        else if(param.equals(paramNames.get(3))){
            float ogOut = outMix;
            int autoOut = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                outSeekBar.setProgress(autoOut);
            }
            else{
                setOut(autoOut);
            }

            return ogOut;
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

        //amount
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                amountSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setAmount((int)(oldValue * floatMultiplier));
            }
        }

        //in
        else if(param.equals(paramNames.get(2))){
            if(updateUI) {
                inSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setIn((int)(oldValue * floatMultiplier));
            }
        }

        //out
        else if(param.equals(paramNames.get(3))){
            if(updateUI) {
                outSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setOut((int)(oldValue * floatMultiplier));
            }
        }
    }
}
