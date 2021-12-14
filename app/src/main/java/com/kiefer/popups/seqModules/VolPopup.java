package com.kiefer.popups.seqModules;

import androidx.core.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSeekBar;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.sequenceModules.Volume;
import com.kiefer.popups.Popup;

public class VolPopup extends Popup {
    private CSeekBar seekBar;
    private View.OnTouchListener onTouchListener;

    public VolPopup(final LLPPDRUMS llppdrums, final Volume volume, final ImageView iv, final Step step, int sub){
        this(llppdrums, volume, iv, step, sub, null, null);
    }

    public VolPopup(final LLPPDRUMS llppdrums, final Volume volume, final ImageView iv, final Step step, int sub, VolSubsPopup volSubPopup, FrameLayout subLayout){
        super(llppdrums);

        //inflate the View
        final FrameLayout popupView = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.popup_seek_bar, null);

        //create the popupWindow
        int width = 200;
        int height = 400;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        seekBar = new CSeekBar(llppdrums, CSeekBar.VERTICAL_DOWN_UP);
        seekBar.setThumb(false);

        int shapeColor = R.color.popupBarColor;
        if(!step.isOn()){
            shapeColor = R.color.sequencerInactiveStepColor;
        }

        seekBar.setColors(ContextCompat.getColor(llppdrums, shapeColor), ContextCompat.getColor(llppdrums, R.color.popupBarBg));
        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        seekBar.onTouchEvent(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        seekBar.onTouchEvent(event);

                        step.setVolumeModifier(seekBar.getProgress(), sub);
                        //engineFacade.setStepVolume(drum);
                        break;
                    case MotionEvent.ACTION_UP:
                        seekBar.onTouchEvent(event);

                        step.setVolumeModifier(seekBar.getProgress(), sub);
                        //engineFacade.setStepVolume(drum);

                        iv.setImageDrawable(volume.getDrawable(step.getTrackNo(), step.getStepNo()));

                        if(volSubPopup != null){
                            volSubPopup.setSubLayout(subLayout, step, sub);
                        }

                        popupWindow.dismiss();
                        break;
                }
                return true;
            }
        };
        seekBar.setOnTouchListener(onTouchListener);
        seekBar.setProgress(step.getVolumeModifier(sub));
        popupView.addView(seekBar);
        popupWindow.showAsDropDown(iv, -100, -200);
    }

    /** SET **/
    public void setProgress(float progress){
        seekBar.setProgress(progress);
    }
}
