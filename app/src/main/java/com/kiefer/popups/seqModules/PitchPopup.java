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
import com.kiefer.machine.sequence.sequenceModules.Pitch;
import com.kiefer.popups.Popup;

public class PitchPopup extends Popup {
    private CSeekBar seekBar;
    private View.OnTouchListener onTouchListener;

    public PitchPopup(LLPPDRUMS llppdrums, final Pitch pitch, final ImageView iv, final Step step, int sub){
        this(llppdrums, pitch, iv, step, sub, null, null);
    }
    public PitchPopup(LLPPDRUMS llppdrums, final Pitch pitch, final ImageView iv, final Step step, int sub, PitchSubsPopup pitchSubPopup, FrameLayout subLayout){
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
        seekBar.setThumb(true);

        int shapeColor = R.color.popupBarColor;
        if(!step.isOn()){
            shapeColor = R.color.sequencerInactiveStepColor;
        }

        seekBar.setThumbColor(ContextCompat.getColor(llppdrums, shapeColor));
        seekBar.setColors(ContextCompat.getColor(llppdrums, R.color.popupBarBg), ContextCompat.getColor(llppdrums, R.color.popupBarBg));
        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        seekBar.onTouchEvent(event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        seekBar.onTouchEvent(event);

                        step.setPitchModifier(seekBar.getProgress(), sub);
                        //engineFacade.setStepPitch(drum);
                        break;
                    case MotionEvent.ACTION_UP:
                        seekBar.onTouchEvent(event);

                        step.setPitchModifier(seekBar.getProgress(), sub);
                        //engineFacade.setStepPitch(drum);

                        iv.setImageDrawable(pitch.getDrawable(step.getTrackNo(), step.getStepNo()));

                        if(pitchSubPopup != null){
                            pitchSubPopup.setSubLayout(subLayout, step, sub);
                        }

                        popupWindow.dismiss();
                        break;
                }
                return true;
            }
        };
        seekBar.setOnTouchListener(onTouchListener);
        seekBar.setProgress(step.getPitchModifier(sub));
        popupView.addView(seekBar);
        popupWindow.showAsDropDown(iv, -100, -200);
    }

    public View.OnTouchListener getOnTouchListener(){
        return onTouchListener;
    }

    /** SET **/
    public void setProgress(float progress){
        seekBar.setProgress(progress);
    }
}
