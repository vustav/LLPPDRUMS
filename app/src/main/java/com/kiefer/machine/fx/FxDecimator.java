package com.kiefer.machine.fx;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxDecimatorKeeper;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.DecimatorInfo;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.Random;

import nl.igorski.mwengine.core.Decimator;

public class FxDecimator extends Fx {
    private int bits;
    private float rate;
    private final int MAX_BITS = 100; /** ?????????? **/
    private SeekBar bitsSeekBar, rateSeekBar;

    public FxDecimator(LLPPDRUMS llppdrums, DrumTrack drumTrack, int index, boolean automation){
        super(llppdrums, drumTrack, index, automation);

        Random r = new Random();

        bits = r.nextInt(MAX_BITS);
        rate = r.nextFloat();

        fx = new Decimator(bits, rate);
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxDeciamtorBits));
        paramNames.add(llppdrums.getResources().getString(R.string.fxDeciamtorRate));
    }

    /** SELECTION **/
    /*
    public void select(){
        bitsSeekBar.setProgress(bits);
        rateSeekBar.setProgress((int)(rate * floatMultiplier));
    }

     */

    /** SET **/
    private void setBits(int value){
        bits = value;
        ((Decimator)fx).setBits(bits);
    }

    private void setRate(float value){
        rate = NmbrUtils.removeImpossibleNumbers(value);
        ((Decimator)fx).setRate(rate);
    }

    /** GET **/
    @Override
    public View getLayout(){
        View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_decimator, null);

        //bits
        bitsSeekBar = layout.findViewById(R.id.decimatorBitsSlider);
        bitsSeekBar.setMax(MAX_BITS);
        bitsSeekBar.setProgress(((Decimator)fx).getBits());
        bitsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //bits = seekBar.getProgress();
                //((Decimator)fx).setBits(bits);
                setBits(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //rate
        rateSeekBar = layout.findViewById(R.id.decimatorRateSlider);
        rateSeekBar.setProgress((int)(((Decimator)fx).getRate() * floatMultiplier));
        rateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setRate(((float)seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return layout;
    }

    public String getName(){
        return llppdrums.getResources().getString(R.string.fxDecimatorName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxDecimatorColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxDecimatorColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return DecimatorInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxDecimatorKeeper keeper = (FxDecimatorKeeper) k;
        setOn(keeper.on);
        //((Decimator)fx).setBits(keeper.bits);
        setBits(keeper.bits);
        //((Decimator)fx).setRate(Float.parseFloat(keeper.rate));
        setRate(Float.parseFloat(keeper.rate));

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxDecimatorKeeper keeper = new FxDecimatorKeeper(getFxNo(), isOn(), automationManager.getKeeper());
        keeper.bits = ((Decimator)fx).getBits();
        keeper.rate = Float.toString(((Decimator)fx).getRate());
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

        //bits
        else if(param.equals(paramNames.get(1))){
            float ogBits = bits;
            int autoBits = (int)(autoValue * MAX_BITS);

            if(updateUI) {
                bitsSeekBar.setProgress(autoBits);
            }
            else{
                setBits(autoBits);
            }
            return ogBits;
        }

        //rate
        else if(param.equals(paramNames.get(2))){
            float ogRate = rate;
            //int autoRate = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                rateSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setRate(autoValue);
            }

            return ogRate;
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

        //bits
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                bitsSeekBar.setProgress((int)oldValue);
            }
            else{
                setBits((int)oldValue);
            }
        }

        //rate
        else if(param.equals(paramNames.get(2))){
            if(updateUI) {
                rateSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setRate(oldValue);
            }
        }
    }
}
