package com.kiefer.machine.sequence.track.Stackables.fx.fxs;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxTremoloKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.TremoloInfo;
import com.kiefer.machine.sequence.track.Stackables.fx.FxManager;
import com.kiefer.machine.sequence.track.Stackables.fx.Fxer;
import com.kiefer.utils.ColorUtils;

import java.util.Random;

import nl.igorski.mwengine.core.Tremolo;

public class FxTremolo extends Fx {
    private int leftAttack, leftDecay, rightAttack, rightDecay;
    private SeekBar leftAttackSeekBar, leftDecaySeekBar, rightAttackSeekBar, rightDecaySeekBar;
    int max = 1000;

    public FxTremolo(LLPPDRUMS llppdrums, FxManager fxManager, Fxer fxer, final int index, boolean automation){
        super(llppdrums, fxManager, fxer, index, automation);

        Random r = new Random();


        leftAttack = r.nextInt(max);
        rightAttack = r.nextInt(max);
        leftDecay = r.nextInt(max);
        rightDecay = r.nextInt(max);

        fx = new Tremolo(0, leftAttack, leftDecay, 0, rightAttack, rightDecay);
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxTremoloLeftAttack));
        paramNames.add(llppdrums.getResources().getString(R.string.fxTremoloLeftDecay));
        paramNames.add(llppdrums.getResources().getString(R.string.fxTremoloRightAttack));
        paramNames.add(llppdrums.getResources().getString(R.string.fxTremoloRightDecay));
    }

    /** SET **/
    private void setLeftAttack(int value){
        leftAttack = value;
        ((Tremolo)fx).setLeftAttack(value);
    }

    private void setLeftDecay(int value){
        leftDecay = value;
        ((Tremolo)fx).setLeftDecay(value);
    }
    private void setRightAttack(int value){
        rightAttack = value;
        ((Tremolo)fx).setRightAttack(value);
    }

    private void setRightDecay(int value){
        rightDecay = value;
        ((Tremolo)fx).setRightDecay(value);
    }

    /** GET **/
    @Override
    public View getLayout(){
        View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_tremolo, null);

        //left attack
        leftAttackSeekBar = layout.findViewById(R.id.leftAttackSlider);
        leftAttackSeekBar.setMax(max);
        leftAttackSeekBar.setProgress((int)((Tremolo)fx).getLeftAttack());
        leftAttackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setLeftAttack(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(1), seekBar.getProgress());
            }
        });

        //left decay
        leftDecaySeekBar = layout.findViewById(R.id.leftDecaySlider);
        leftDecaySeekBar.setMax(max);
        leftDecaySeekBar.setProgress((int)((Tremolo)fx).getLeftDecay());
        leftDecaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setLeftDecay(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(2), seekBar.getProgress());
            }
        });

        //right attack
        rightAttackSeekBar = layout.findViewById(R.id.rightAttackSlider);
        rightAttackSeekBar.setMax(max);
        rightAttackSeekBar.setProgress((int)((Tremolo)fx).getRightAttack());
        rightAttackSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setRightAttack(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(3), seekBar.getProgress());
            }
        });

        //left decay
        rightDecaySeekBar = layout.findViewById(R.id.rightDecaySlider);
        rightDecaySeekBar.setMax(max);
        rightDecaySeekBar.setProgress((int)((Tremolo)fx).getRightDecay());
        rightDecaySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setRightDecay(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                automationManager.changeInBaseValue(paramNames.get(4), seekBar.getProgress());
            }
        });

        return layout;
    }

    public String getName(){
        return llppdrums.getResources().getString(R.string.fxTremoloName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxTremoloColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxTremoloColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return TremoloInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxTremoloKeeper keeper = (FxTremoloKeeper) k;
        setOn(keeper.on);
        setLeftAttack(keeper.leftAttack);
        setLeftDecay(keeper.leftDecay);
        setRightAttack(keeper.rightAttack);
        setRightDecay(keeper.rightDecay);

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxTremoloKeeper keeper = new FxTremoloKeeper(getFxNo(), isOn(), automationManager.getKeeper());
        keeper.leftAttack = ((Tremolo)fx).getLeftAttack();
        keeper.leftDecay = ((Tremolo)fx).getLeftDecay();
        keeper.rightAttack = ((Tremolo)fx).getRightAttack();
        keeper.rightDecay = ((Tremolo)fx).getRightDecay();
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

        //left attack
        else if(param.equals(paramNames.get(1))){
            int ogLAttack = leftAttack;
            int autoLAttack = (int)(autoValue * max);

            if(updateUI) {
                leftAttackSeekBar.setProgress(autoLAttack);
            }
            else{
                setLeftAttack(autoLAttack);
            }

            return ogLAttack;
        }

        //left decay
        else if(param.equals(paramNames.get(2))){
            int ogLDecay = leftDecay;
            int autoLDecay = (int)(autoValue * max);

            if(updateUI) {
                leftDecaySeekBar.setProgress(autoLDecay);
            }
            else{
                setLeftDecay(autoLDecay);
            }

            return ogLDecay;
        }

        //right attack
        else if(param.equals(paramNames.get(3))){
            int ogRAttack = rightAttack;
            int autoRAttack = (int)(autoValue * max);

            if(updateUI) {
                rightAttackSeekBar.setProgress(autoRAttack);
            }
            else{
                setRightAttack(autoRAttack);
            }

            return ogRAttack;
        }

        //left decay
        else if(param.equals(paramNames.get(4))){
            int ogRDecay = rightDecay;
            int autoRDecay = (int)(autoValue * max);

            if(updateUI) {
                rightDecaySeekBar.setProgress(autoRDecay);
            }
            else{
                setRightDecay(autoRDecay);
            }

            return ogRDecay;
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

        //left attack
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                leftAttackSeekBar.setProgress((int)oldValue);
            }
            else{
                setLeftAttack((int)oldValue);
            }
        }

        //left decay
        else if(param.equals(paramNames.get(2))){
            if(updateUI) {
                leftDecaySeekBar.setProgress((int)oldValue);
            }
            else{
                setLeftDecay((int)oldValue);
            }
        }

        //right attack
        else if(param.equals(paramNames.get(3))){
            if(updateUI) {
                rightAttackSeekBar.setProgress((int)oldValue);
            }
            else{
                setRightAttack((int)oldValue);
            }
        }

        //right decay
        else if(param.equals(paramNames.get(4))){
            if(updateUI) {
                rightDecaySeekBar.setProgress((int)oldValue);
            }
            else{
                setRightDecay((int)oldValue);
            }
        }
    }
}
