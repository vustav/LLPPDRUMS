package com.kiefer.popups.soundManager.smplManager;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.SoundSourceManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.sampleManager.SmplManager;
import com.kiefer.popups.soundManager.SoundManagerPopupOLD;

public class SampleManagerView implements SoundManagerPopupOLD.SoundSourceView {
    private DrumTrack drumTrack;
    private CSpinnerButton categorySpinnerButton, sampleSpinnerButton;
    private LinearLayout layout;
    private SeekBar volumeSlider;
    private final int multiplier = 100;

    public SampleManagerView(final LLPPDRUMS llppdrums, final SmplManager smplManager, final DrumTrack drumTrack){
        this.drumTrack = drumTrack;

        //inflate the View
        layout = (LinearLayout) llppdrums.getLayoutInflater().inflate(R.layout.popup_sample_manager, null);

        //sample-spinner
        sampleSpinnerButton = new CSpinnerButton(llppdrums);
        sampleSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SampleListPopup(llppdrums, (SoundSourceManager) drumTrack.getSoundManager().getSelectedStackable(), sampleSpinnerButton);
            }
        });
        sampleSpinnerButton.setSelection(((SoundSourceManager)drumTrack.getSoundManager().getSelectedStackable()).getSmplManager().getSelectedCategory().getSelectedSample().getName());

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams((int) llppdrums.getResources().getDimension(R.dimen.btnWidthLarge), (int) llppdrums.getResources().getDimension(R.dimen.btnHeightLarge));
        sampleSpinnerButton.setLayoutParams(flp);

        FrameLayout sampleSpinnerContainer = layout.findViewById(R.id.sampleManagerSampleSpinnerContainer);
        sampleSpinnerContainer.addView(sampleSpinnerButton);

        //category-spinner
        categorySpinnerButton = new CSpinnerButton(llppdrums);
        categorySpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SampleCategoryListPopup(llppdrums, (SoundSourceManager) drumTrack.getSoundManager().getSelectedStackable(), categorySpinnerButton, sampleSpinnerButton);
            }
        });
        categorySpinnerButton.setSelection(((SoundSourceManager)drumTrack.getSoundManager().getSelectedStackable()).getSmplManager().getSelectedCategory().getName());

        flp = new FrameLayout.LayoutParams((int) llppdrums.getResources().getDimension(R.dimen.btnWidthLarge), (int) llppdrums.getResources().getDimension(R.dimen.btnHeightLarge));
        categorySpinnerButton.setLayoutParams(flp);

        FrameLayout categorySpinnerContainer = layout.findViewById(R.id.sampleManagerCategorySpinnerContainer);
        categorySpinnerContainer.addView(categorySpinnerButton);

        //..and the playBtn
        Button playBtn = layout.findViewById(R.id.sampleManagerPlayBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SoundSourceManager) drumTrack.getSoundManager().getSelectedStackable()).play();
            }
        });

        //random
        Button rndBtn = layout.findViewById(R.id.sampleManagerRndBtn);
        rndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SoundSourceManager) drumTrack.getSoundManager().getSelectedStackable()).getSmplManager().randomizeAll();
                updateUI();
            }
        });

        //volume
        volumeSlider = layout.findViewById(R.id.sampleManagerVolumeSlider);
        volumeSlider.setProgress((int)(smplManager.getVolume() * multiplier));
        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float vol = ((float) seekBar.getProgress()) / multiplier;
                //drumTrack.setOscillatorVolume(oscNo, vol);
                smplManager.setVolume(vol);
                drumTrack.updateDrumVolumes();
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
    public void updateUI(){
        categorySpinnerButton.setSelection(((SoundSourceManager)drumTrack.getSoundManager().getSelectedStackable()).getSmplManager().getSelectedCategory().getName());
        sampleSpinnerButton.setSelection(((SoundSourceManager)drumTrack.getSoundManager().getSelectedStackable()).getSmplManager().getSelectedCategory().getSelectedSample().getName());
    }

    @Override
    public LinearLayout getLayout(){
        return layout;
    }
}
