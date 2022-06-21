package com.kiefer.popups.trackMenu;

import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSeekBar;
import com.kiefer.machine.sequence.sequenceModules.Volume;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.Popup;
import com.kiefer.popups.seqModules.VolSubsPopup;
import com.kiefer.utils.ColorUtils;

public class TrackVolPopup extends Popup {
    public static float VOL_MULTIPLIER = 10f;
    //private CSeekBar seekBar;
    //private View.OnTouchListener onTouchListener;

    public TrackVolPopup(final LLPPDRUMS llppdrums, final DrumTrack drumTrack, View view, final CSeekBar cSeekBar){
        //this(llppdrums, volume, iv, step, sub, null, null);

        super(llppdrums);

        FrameLayout popupView = new FrameLayout(llppdrums);
        int padding = (int) llppdrums.getResources().getDimension(R.dimen.defaultBtnPadding);
        popupView.setPadding(padding, padding, padding, padding);
        popupView.setBackgroundColor(ColorUtils.getRandomColor());

        //inflate the View
        final FrameLayout frameLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.popup_seek_bar, null);
        popupView.addView(frameLayout);

        //create the popupWindow
        int width = 200;
        int height = 400;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        CSeekBar seekBar = new CSeekBar(llppdrums, CSeekBar.VERTICAL_DOWN_UP);
        seekBar.setThumb(false);

        int shapeColor = R.color.popupBarColor;

        seekBar.setColors(ContextCompat.getColor(llppdrums, shapeColor), ContextCompat.getColor(llppdrums, R.color.popupBarBg));
        //seekBar.setMargin(0);
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        seekBar.onTouchEvent(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        seekBar.onTouchEvent(event);

                        //step.setVolumeModifier(seekBar.getProgress(), sub);
                        drumTrack.setTrackVolume(seekBar.getProgress() * VOL_MULTIPLIER);
                        //engineFacade.setStepVolume(drum);
                        break;
                    case MotionEvent.ACTION_UP:
                        seekBar.onTouchEvent(event);

                        //step.setVolumeModifier(seekBar.getProgress(), sub);
                        drumTrack.setTrackVolume(seekBar.getProgress() * VOL_MULTIPLIER);
                        //engineFacade.setStepVolume(drum);

                        //iv.setImageDrawable(volume.getDrawable(step.getTrackNo(), step.getStepNo()));

                        //if(volSubPopup != null){
                            //volSubPopup.setSubLayout(subLayout, step, sub);
                        //}

                        popupWindow.dismiss();
                        break;
                }
                return true;
            }
        };
        seekBar.setOnTouchListener(onTouchListener);
        seekBar.setProgress(drumTrack.getTrackVolume() / VOL_MULTIPLIER);
        frameLayout.addView(seekBar);
        popupWindow.showAsDropDown(view, -100, -200);
    }
}
