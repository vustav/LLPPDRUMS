package com.kiefer.randomization.rndTrackManager;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.info.sequence.trackMenu.RandomizeTrackInfo;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

public class RndTrackManagerPopup extends Popup {
    private final CSpinnerButton cSpinnerButton;

    public RndTrackManagerPopup(LLPPDRUMS llppdrums, final DrumTrack drumTrack){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_track_rnd, null);
        //popupView.setBackground(ContextCompat.getDrawable(llppdrums, drumTrack.getRndPopupGradientDrawable()));
        popupView.setBackground(drumTrack.getRndPopupGradientDrawable());

        //create the popupWindow
        int tabs = (int) llppdrums.getResources().getDimension(R.dimen.tabsSize) * 3;
        int padding = (int) llppdrums.getResources().getDimension(R.dimen.tabsPaddingLarge) * 4;
        int borders = (int) llppdrums.getResources().getDimension(R.dimen.tabsBorderLarge) * 6;
        int width = tabs + padding + borders;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //label
        String name = llppdrums.getResources().getString(R.string.rndTrackLabel) + drumTrack.getTrackNo();
        TextView label = popupView.findViewById(R.id.trackRndLabel);
        label.setText(name);
        int bgColor = drumTrack.getColor();
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

        //preset spinner
        cSpinnerButton = new CSpinnerButton(llppdrums);
        cSpinnerButton.setSelection("PRESETS");
        //cSpinnerButton.setSelection(rndSeqManager.getSelectedRandomizePreset().getName());

        cSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RndTrackPresetListPopup(llppdrums, drumTrack, cSpinnerButton);
            }
        });

        FrameLayout spinnerContainer = popupView.findViewById(R.id.trackRndPresetsBtn);

        spinnerContainer.addView(cSpinnerButton);

        //LinearLayout presetLayout = popupView.findViewById(R.id.seqRndOptionsPresetLayout);
        //presetLayout.setBackground(ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor()));

        //the btn
        Button rndBtn = popupView.findViewById(R.id.trackRndBtn);
        rndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean updateDrawables = false;

                if(drumTrack.getRndOsc()){
                    drumTrack.getSoundManager().randomizeAll();
                }

                if(drumTrack.getRndOn()) {
                    drumTrack.getRndTrackManager().randomizeStepsOn();
                    updateDrawables = true;
                    //drumTrack.updateDrawables();
                }

                if(drumTrack.getRndVol()) {
                    drumTrack.getRndTrackManager().randomizeStepVols();
                    updateDrawables = true;
                    //drumTrack.updateDrawables();
                }

                if(drumTrack.getRndPitch()) {
                    drumTrack.getRndTrackManager().randomizeStepPitches();
                    updateDrawables = true;
                    //drumTrack.updateDrawables();
                }

                if(drumTrack.getRndPan()) {
                    drumTrack.getRndTrackManager().randomizeStepPans();
                    updateDrawables = true;
                    //drumTrack.updateDrawables();
                }

                if(drumTrack.getRndFx()) {
                    drumTrack.getFxManager().randomizeAll();
                }

                if(drumTrack.getRndMix()) {
                    //drumTrack.randomizePan();
                    drumTrack.randomizeVol();
                }

                if(updateDrawables){
                    drumTrack.updateDrawables();
                }
            }
        });

        //osc-check
        final CheckBox oscCheck = popupView.findViewById(R.id.trackRndOscCheck);
        oscCheck.setChecked(drumTrack.getRndOsc());
        oscCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.setRndOsc(oscCheck.isChecked());
            }
        });

        //on-check
        final CheckBox onCheck = popupView.findViewById(R.id.trackRndOnCheck);
        onCheck.setChecked(drumTrack.getRndOn());
        onCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.setRndOn(onCheck.isChecked());
            }
        });

        //vol-check
        final CheckBox volCheck = popupView.findViewById(R.id.trackRndVolCheck);
        volCheck.setChecked(drumTrack.getRndVol());
        volCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.setRndVol(volCheck.isChecked());
            }
        });

        //pitch-check
        final CheckBox pitchCheck = popupView.findViewById(R.id.trackRndPitchlCheck);
        pitchCheck.setChecked(drumTrack.getRndPitch());
        pitchCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.setRndPitch(pitchCheck.isChecked());
            }
        });

        //pan-check
        final CheckBox panCheck = popupView.findViewById(R.id.trackRndPanlCheck);
        panCheck.setChecked(drumTrack.getRndPan());
        panCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.setRndPan(panCheck.isChecked());
            }
        });

        //fx-check
        final CheckBox fxCheck = popupView.findViewById(R.id.trackRndFxCheck);
        fxCheck.setChecked(drumTrack.getRndFx());
        fxCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.setRndFx(fxCheck.isChecked());
            }
        });

        //mix-check
        final CheckBox mixCheck = popupView.findViewById(R.id.trackRndMixCheck);
        mixCheck.setChecked(drumTrack.getRndMix());
        mixCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.setRndMix(mixCheck.isChecked());
            }
        });

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.trackRndInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, RandomizeTrackInfo.key);
            }
        });

        show(popupWindow, Gravity.START, 0, false);
    }
}
