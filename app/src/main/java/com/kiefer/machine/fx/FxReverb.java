package com.kiefer.machine.fx;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxReverbKeeper;
import com.kiefer.info.sequence.trackMenu.fxManager.ReverbInfo;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.Random;

import nl.igorski.mwengine.core.Reverb;

public class FxReverb extends Fx {
    private float size, damp, mix, out;
    private SeekBar sizeSeekBar, dampSeekBar, mixSeekBar, outSeekBar;

    public FxReverb(LLPPDRUMS llppdrums, DrumTrack drumTrack, int index, boolean automation){
        super(llppdrums, drumTrack, index, automation);

        Random r = new Random();

        size = r.nextFloat();
        damp = r.nextFloat();
        mix = r.nextFloat();
        out = r.nextFloat();

        fx = new Reverb(size, damp, mix, out);
    }

    @Override
    public void setupParamNames(){
        paramNames.add(llppdrums.getResources().getString(R.string.fxRevLoFiSize));
        paramNames.add(llppdrums.getResources().getString(R.string.fxRevLoFiDamp));
        paramNames.add(llppdrums.getResources().getString(R.string.fxRevLoFiMix));
        paramNames.add(llppdrums.getResources().getString(R.string.fxRevLoFiOut));
    }

    /** SELECTION **/
    /*
    public void select(){
        sizeSeekBar.setProgress((int)(size * floatMultiplier));
        dampSeekBar.setProgress((int)(damp * floatMultiplier));
        mixSeekBar.setProgress((int)(mix * floatMultiplier));
        outSeekBar.setProgress((int)(out * floatMultiplier));
    }

     */

    /** SET **/
    private void setSize(float value){
        size = NmbrUtils.removeImpossibleNumbers(value);
        ((Reverb)fx).setSize(size);
    }

    private void setDamp(float value){
        damp = NmbrUtils.removeImpossibleNumbers(value);
        ((Reverb)fx).setHFDamp(damp);
    }

    private void setMix(float value){
        mix = NmbrUtils.removeImpossibleNumbers(value);
        ((Reverb)fx).setMix(mix);
    }

    private void setOut(float value){
        out = NmbrUtils.removeImpossibleNumbers(value);
        ((Reverb)fx).setOutput(out);
    }

    /** GET **/
    @Override
    public View getLayout(){
        View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager_reverb_lofi, null);

        //size
        sizeSeekBar = layout.findViewById(R.id.revLoFiSizeSlider);
        sizeSeekBar.setProgress((int)(((Reverb)fx).getSize() * floatMultiplier));
        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setSize(((float)seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //damp
        dampSeekBar = layout.findViewById(R.id.revLoFiDampSlider);
        dampSeekBar.setProgress((int)(((Reverb)fx).getHFDamp() * floatMultiplier));
        dampSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setDamp(((float)seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //mix
        mixSeekBar = layout.findViewById(R.id.revLoFiMixSlider);
        mixSeekBar.setProgress((int)(((Reverb)fx).getMix() * floatMultiplier));
        mixSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setMix(((float)seekBar.getProgress()) / floatMultiplier);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //out
        outSeekBar = layout.findViewById(R.id.revLoFiOutSlider);
        outSeekBar.setProgress((int)(((Reverb)fx).getOutput() * floatMultiplier));
        outSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setOut(((float)seekBar.getProgress()) / floatMultiplier);
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
        return llppdrums.getResources().getString(R.string.fxReverbName);
    }

    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(ContextCompat.getColor(llppdrums, R.color.fxReverbColor), gradientColorTwo, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradientColorTwo, ContextCompat.getColor(llppdrums, R.color.fxReverbColor), ColorUtils.HORIZONTAL);
    }

    @Override
    public String getInfoKey(){
        return ReverbInfo.key;
    }

    /** RESTORATION **/
    public void restore(FxKeeper k){
        FxReverbKeeper keeper = (FxReverbKeeper) k;
        setOn(keeper.on);
        //((Reverb)fx).setSize(Float.parseFloat(keeper.size));
        setSize(Float.parseFloat(keeper.size));
        //((Reverb)fx).setHFDamp(Float.parseFloat(keeper.damp));
        setDamp(Float.parseFloat(keeper.damp));
        //((Reverb)fx).setMix(Float.parseFloat(keeper.mix));
        setMix(Float.parseFloat(keeper.mix));
        //((Reverb)fx).setOutput(Float.parseFloat(keeper.out));
        setOut(Float.parseFloat(keeper.out));

        automationManager.restore(keeper.automationManagerKeeper);
    }

    @Override
    public FxKeeper getKeeper(){
        FxReverbKeeper keeper = new FxReverbKeeper(getFxNo(), isOn(), automationManager.getKeeper());
        keeper.size = Float.toString(((Reverb)fx).getSize());
        keeper.damp = Float.toString(((Reverb)fx).getHFDamp());
        keeper.mix = Float.toString(((Reverb)fx).getMix());
        keeper.out = Float.toString(((Reverb)fx).getOutput());
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

        //size
        else if(param.equals(paramNames.get(1))){
            float ogSize = size;
            //int autoSize = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                sizeSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setSize(autoValue);
            }
            return ogSize;
        }

        //damp
        else if(param.equals(paramNames.get(2))){
            float ogDamp = damp;
            //int autoDamp = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                dampSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setDamp(autoValue);
            }

            return ogDamp;
        }

        //mix
        else if(param.equals(paramNames.get(3))){
            float ogMix = mix;
            //int autoMix = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                mixSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setMix(autoValue);
            }

            return ogMix;
        }

        //out
        else if(param.equals(paramNames.get(4))){
            float ogOut = out;
            //int autoDelay = (int)(autoValue * floatMultiplier);

            if(updateUI) {
                outSeekBar.setProgress((int)(autoValue * floatMultiplier));
            }
            else{
                setOut(autoValue);
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

        //size
        else if(param.equals(paramNames.get(1))){
            if(updateUI) {
                sizeSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setSize(oldValue);
            }
        }

        //damp
        else if(param.equals(paramNames.get(2))){
            if(updateUI) {
                dampSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setDamp(oldValue);
            }
        }

        //mix
        else if(param.equals(paramNames.get(3))){
            if(updateUI) {
                mixSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setMix(oldValue);
            }
        }

        //out
        else if(param.equals(paramNames.get(4))){
            if(updateUI) {
                outSeekBar.setProgress((int)(oldValue * floatMultiplier));
            }
            else{
                setOut(oldValue);
            }
        }
    }
}
