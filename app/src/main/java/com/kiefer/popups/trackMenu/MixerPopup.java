package com.kiefer.popups.trackMenu;

import androidx.core.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSeekBar;
import com.kiefer.info.sequence.trackMenu.MixerInfo;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

public class MixerPopup extends Popup {

    public MixerPopup(LLPPDRUMS llppdrums, final DrumTrack drumTrack){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_mixer, null);
        popupView.setBackground(ContextCompat.getDrawable(llppdrums, drumTrack.getMixerPopupBgId()));

        //create the popupWindow
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //set up the pan slider
        /*
        final CSeekBar panSlider = new CSeekBar(llppdrums, CSeekBar.HORIZONTAL_LEFT_RIGHT);
        panSlider.setThumb(true);
        panSlider.setMax(2f); //pan is a value -1 tp 1, so set max to 2 and subtract 1 when updating pan
        panSlider.setThumbColor(ContextCompat.getColor(llppdrums, R.color.popupBarColor));
        panSlider.setColors(ContextCompat.getColor(llppdrums, R.color.popupBarBg), ContextCompat.getColor(llppdrums, R.color.popupBarBg));
        //View.OnTouchListener onTouchListener =
        panSlider.setProgress(drumTrack.getPan() + 1f);
        panSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        panSlider.onTouchEvent(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        panSlider.onTouchEvent(event);
                        drumTrack.setPan(panSlider.getProgress() - 1f);
                        break;
                    case MotionEvent.ACTION_UP:
                        panSlider.onTouchEvent(event);
                        drumTrack.setPan(panSlider.getProgress() - 1f);
                        break;
                }
                return true;
            }
        });
        FrameLayout sliderLayout = popupView.findViewById(R.id.panSliderLayout);
        sliderLayout.addView(panSlider);

         */

        //and the vol slider
        final CSeekBar volSlider = new CSeekBar(llppdrums, CSeekBar.VERTICAL_DOWN_UP);
        volSlider.setThumb(false);
        volSlider.setColors(ContextCompat.getColor(llppdrums, R.color.popupBarColor), ContextCompat.getColor(llppdrums, R.color.popupBarBg));
        volSlider.setProgress(drumTrack.getTrackVolume());
        volSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        volSlider.onTouchEvent(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        volSlider.onTouchEvent(event);
                        drumTrack.setTrackVolume(volSlider.getProgress());
                        break;
                    case MotionEvent.ACTION_UP:
                        volSlider.onTouchEvent(event);
                        drumTrack.setTrackVolume(volSlider.getProgress());
                        break;
                }
                return true;
            }
        });
        FrameLayout volume = popupView.findViewById(R.id.volSliderLayout);
        volume.addView(volSlider);

        //solo
        Button soloBtn = popupView.findViewById(R.id.soloBtn);
        soloBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.setSolo();
            }
        });

        //mute
        Button muteBtn = popupView.findViewById(R.id.muteBtn);
        muteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.setMute();
            }
        });

        //setup the TV
        String name = llppdrums.getResources().getString(R.string.mixerLabel) + drumTrack.getTrackNo();
        TextView label = popupView.findViewById(R.id.label);
        label.setText(name);
        int bgColor = drumTrack.getColor();
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.mixerInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, MixerInfo.key);
            }
        });

        //show the popup with a little offset
        show(popupWindow);
    }
}
