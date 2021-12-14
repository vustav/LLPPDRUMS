package com.kiefer.popups.soundManager.smplManager;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.soundManager.SoundManagerPopup;

public class SampleManagerView implements SoundManagerPopup.SoundSourceView {
    private CSpinnerButton categorySpinnerButton, sampleSpinnerButton;
    private LinearLayout layout;

    public SampleManagerView(final LLPPDRUMS llppdrums, final DrumTrack drumTrack){

        //inflate the View
        layout = (LinearLayout) llppdrums.getLayoutInflater().inflate(R.layout.popup_sample_manager, null);

        //sample-spinner
        sampleSpinnerButton = new CSpinnerButton(llppdrums);
        sampleSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SampleListPopup(llppdrums, drumTrack.getSoundManager(), sampleSpinnerButton);
            }
        });
        sampleSpinnerButton.setSelection(drumTrack.getSoundManager().getSmplManager().getSelectedCategory().getSelectedSample().getName());

        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams((int) llppdrums.getResources().getDimension(R.dimen.btnWidthLarge), (int) llppdrums.getResources().getDimension(R.dimen.btnHeightLarge));
        sampleSpinnerButton.setLayoutParams(flp);

        FrameLayout sampleSpinnerContainer = layout.findViewById(R.id.sampleManagerSampleSpinnerContainer);
        sampleSpinnerContainer.addView(sampleSpinnerButton);

        //category-spinner
        categorySpinnerButton = new CSpinnerButton(llppdrums);
        categorySpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CategoryListPopup(llppdrums, drumTrack.getSoundManager(), categorySpinnerButton, sampleSpinnerButton);
            }
        });
        categorySpinnerButton.setSelection(drumTrack.getSoundManager().getSmplManager().getSelectedCategory().getName());

        flp = new FrameLayout.LayoutParams((int) llppdrums.getResources().getDimension(R.dimen.btnWidthLarge), (int) llppdrums.getResources().getDimension(R.dimen.btnHeightLarge));
        categorySpinnerButton.setLayoutParams(flp);

        FrameLayout categorySpinnerContainer = layout.findViewById(R.id.sampleManagerCategorySpinnerContainer);
        categorySpinnerContainer.addView(categorySpinnerButton);

        //..and the playBtn
        Button playBtn = layout.findViewById(R.id.sampleManagerPlayBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.getSoundManager().playDrum();
            }
        });

        //random
        Button rndBtn = layout.findViewById(R.id.sampleManagerRndBtn);
        rndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.getSoundManager().randomizeAll();
                categorySpinnerButton.setSelection(drumTrack.getSoundManager().getSmplManager().getSelectedCategory().getName());
                sampleSpinnerButton.setSelection(drumTrack.getSoundManager().getSmplManager().getSelectedCategory().getSelectedSample().getName());
            }
        });
    }

    @Override
    public LinearLayout getLayout(){
        return layout;
    }
}
