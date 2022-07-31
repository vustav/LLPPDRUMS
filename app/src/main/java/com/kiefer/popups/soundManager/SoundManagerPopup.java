package com.kiefer.popups.soundManager;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.SoundSourceManager;
import com.kiefer.popups.stackableManager.StackableManagerPopup;

public class SoundManagerPopup extends StackableManagerPopup {

    public SoundManagerPopup(final LLPPDRUMS llppdrums, final DrumTrack drumTrack, int type){
        super(llppdrums, drumTrack.getSoundManager(), drumTrack, type);

        final View customView = llppdrums.getLayoutInflater().inflate(R.layout.layout_sound_manager_custom_area, null);

        //play
        Button playBtn = customView.findViewById(R.id.soundManagerPlayBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drumTrack.getSoundManager().play();
            }
        });

        //presets
        CSpinnerButton presetSpinnerButton = new CSpinnerButton(llppdrums);
        presetSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SoundPresetsListPopup(llppdrums, drumTrack, SoundManagerPopup.this, presetSpinnerButton);
            }
        });
        presetSpinnerButton.setSelection("PRESETS");

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams((int) llppdrums.getResources().getDimension(R.dimen.btnWidthLarge), (int) llppdrums.getResources().getDimension(R.dimen.btnHeightLarge));
        presetSpinnerButton.setLayoutParams(flp);

        FrameLayout spinnerContainer = customView.findViewById(R.id.soundManagerPresetSpinnerContainer);
        spinnerContainer.addView(presetSpinnerButton);

        customArea.addView(customView);
    }
}
