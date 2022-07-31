package com.kiefer.popups.soundManager.oscillatorManager;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.oscillatorManager.OscillatorManager;

import java.util.ArrayList;

public class OscillatorView {
    private final OscillatorManager oscillatorManager;
    private final int oscNo;
    private LinearLayout layout;
    //private CheckBox cb;
    private CSpinnerButton cSpinnerButton;
    private SeekBar oscVolumeSlider, oscPitchSlider, atkTimeSlider, releaseTimeSlider;
    private final int multiplier = 100;

    private final ArrayList<View> disableableViews; //add all views here to be able to enable/disable them quick (not the checkBox!)

    boolean setup = true; //used to set up checkBoxes (avoid click when setting the value)

    public OscillatorView(final LLPPDRUMS llppdrums, final OscillatorManager oscillatorManager, DrumTrack drumTrack, final int oscNo){
        this.oscillatorManager = oscillatorManager;
        this.oscNo = oscNo;

        disableableViews = new ArrayList<>();

        layout = (LinearLayout) llppdrums.getLayoutInflater().inflate(R.layout.popup_oscillator, null);
/*
        cb = layout.findViewById(R.id.oscCheck);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!setup) {
                    OscillatorView.this.oscillatorManager.setOscillatorOn(oscNo, cb.isChecked());
                    setEnabled(cb.isChecked());
                }
            }
        });
        cb.setChecked(oscillatorManager.isOscillatorOn(oscNo));
        setEnabled(oscillatorManager.isOscillatorOn(oscNo));

 */
        setup = false;

        //wave-spinner
        cSpinnerButton = new CSpinnerButton(llppdrums);
        cSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new OscillatorWaveListPopup(llppdrums, oscillatorManager, OscillatorView.this, oscNo, cSpinnerButton);
            }
        });
        cSpinnerButton.setSelection(oscillatorManager.getWaves()[(int)(oscillatorManager.getOscillatorWaveForm(oscNo))]);

        FrameLayout spinnerContainer = layout.findViewById(R.id.oscWaveSpinnerContainer);
        spinnerContainer.addView(cSpinnerButton);
        disableableViews.add(cSpinnerButton);

        //volume
        oscVolumeSlider = layout.findViewById(R.id.oscVolumeSlider);
        oscVolumeSlider.setProgress((int)(oscillatorManager.getOscillatorVolume(oscNo) * multiplier));
        oscVolumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float vol = ((float) seekBar.getProgress()) / multiplier;
                //drumTrack.setOscillatorVolume(oscNo, vol);
                oscillatorManager.setOscillatorVolume(oscNo, vol);
                drumTrack.updateDrumVolumes();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        disableableViews.add(oscVolumeSlider);

        //pitch
        oscPitchSlider = layout.findViewById(R.id.oscPitchSlider);
        oscPitchSlider.setMax(llppdrums.getResources().getInteger(R.integer.maxPitch));
        oscPitchSlider.setProgress((int)(oscillatorManager.getOscillatorPitchLin(oscNo)));
        oscPitchSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //drumTrack.setOscillatorPitch(oscNo, seekBar.getProgress());
                oscillatorManager.setOscillatorPitch(oscNo, seekBar.getProgress());
                drumTrack.updateDrumVolumes();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        disableableViews.add(oscPitchSlider);

        //atk time
        atkTimeSlider = layout.findViewById(R.id.oscAtkTimeSlider);
        atkTimeSlider.setMax(llppdrums.getResources().getInteger(R.integer.maxOscAtkTime));
        atkTimeSlider.setProgress((int)(oscillatorManager.getOscillatorAtkTime(oscNo) * multiplier));
        atkTimeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float time = ((float) seekBar.getProgress()) / multiplier;
                //Oscillator.this.oscillatorManager.setAttackTime(oscNo, time);
                //drumTrack.setAttackTime(oscNo, time);
                oscillatorManager.setAttackTime(oscNo, time);
                drumTrack.updateDrumVolumes();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        disableableViews.add(atkTimeSlider);

        //decay time
        releaseTimeSlider = layout.findViewById(R.id.oscReleaseTimeSlider);
        releaseTimeSlider.setMax(llppdrums.getResources().getInteger(R.integer.maxOscDecayTime));
        releaseTimeSlider.setProgress((int)(oscillatorManager.getOscillatorReleaseTime(oscNo) * multiplier));
        releaseTimeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float time = ((float) seekBar.getProgress()) / multiplier;
                //Oscillator.this.oscillatorManager.setDecayTime(oscNo, time);
                // drumTrack.setReleaseTime(oscNo, time);
                oscillatorManager.setReleaseTime(oscNo, time);
                drumTrack.updateDrumVolumes();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        disableableViews.add(releaseTimeSlider);

        //enable/disable after all the views are created
        setEnabled(oscillatorManager.isOscillatorOn(oscNo));
    }

    //private boolean updatingUI = false;
    public void updateUI(){
        //updatingUI = true;
        //if(drumTrack.getOscillatorManager().getscillator(oscNo).isOn()) {
            //cb.setChecked(oscillatorManager.getOscillator(oscNo).isOn());
        Log.e("OscillatorView", "aupdateUI(), oscNo: "+oscNo);
            cSpinnerButton.setSelection(oscillatorManager.getWaves()[(int) (oscillatorManager.getOscillatorWaveForm(oscNo))]);
            oscVolumeSlider.setProgress((int) (oscillatorManager.getOscillatorVolume(oscNo) * multiplier));
            oscPitchSlider.setProgress((int) (oscillatorManager.getOscillatorPitchLin(oscNo)));
            atkTimeSlider.setProgress((int) (oscillatorManager.getOscillatorAtkTime(oscNo) * multiplier));
            releaseTimeSlider.setProgress((int) (oscillatorManager.getOscillatorReleaseTime(oscNo) * multiplier));
        //}
        //else{
            setEnabled(oscillatorManager.getOscillator(oscNo).isOn());
        //}
        //updatingUI = false;
    }

    public void setWaveForm(int i){
        oscillatorManager.setWaveForm(oscNo, i);
    }

    /** ACTIVATION **/
    private void setEnabled(boolean enabled){
        float alpha = 1f;
        if(!enabled){
            alpha = .5f;
        }
        for (View v : disableableViews){
            v.setAlpha(alpha);
            v.setEnabled(enabled);
        }
    }

    /** GET **/
    public LinearLayout getLayout(){
        return layout;
    }
/*
    public boolean isOn(){
        return cb.isChecked();
    }

 */
}
