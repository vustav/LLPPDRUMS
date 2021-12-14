package com.kiefer.randomization.rndSeqManager.rndSeqManagerPopup;

import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSeekBar;
import com.kiefer.graphics.SeqRndManagerPercSubsDrawable;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.popups.Popup;

public class RndSeqManagerPercPopup extends Popup {
    private CSeekBar seekBar;
    private View.OnTouchListener onTouchListener;

    public RndSeqManagerPercPopup(final LLPPDRUMS llppdrums, final RndSeqManagerPopup rndSeqManagerPopup, final ImageView iv, RndSeqPresetTrack.Step step, int sub) {
        this(llppdrums, rndSeqManagerPopup, iv, step, sub, null, null);
    }

    public RndSeqManagerPercPopup(final LLPPDRUMS llppdrums, final RndSeqManagerPopup rndSeqManagerPopup, final ImageView iv, RndSeqPresetTrack.Step step, int sub, RndSeqManagerPercSubPopup subsPopup, FrameLayout subLayout){
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

        int shapeColor = R.color.rndSeqPercColor;
        //if(!drum.isOn()){
        //shapeColor = R.color.sequencerInactiveStepColor;
        //}

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

                        //drum.setVolumeModifier(seekBar.getProgress());
                        //rndSeqManagerPopup.setStepPerc(stepNo, seekBar.getProgress());
                        step.setSubPerc(sub, seekBar.getProgress());
                        //engineFacade.setStepVolume(drum);
                        break;
                    case MotionEvent.ACTION_UP:
                        seekBar.onTouchEvent(event);

                        //rndSeqManagerPopup.setStepPerc(stepNo, seekBar.getProgress());
                        step.setSubPerc(sub, seekBar.getProgress());
                        rndSeqManagerPopup.addModifiedMarker();

                        //rndSeqManagerPopup.setStepDrawable(step, seekBar.getProgress());
                        iv.setImageDrawable(new SeqRndManagerPercSubsDrawable(llppdrums, step));

                        if(subsPopup != null){
                            subsPopup.setSubLayout(subLayout, step, sub);
                        }

                        popupWindow.dismiss();
                        break;
                }
                return true;
            }
        };
        seekBar.setOnTouchListener(onTouchListener);
        seekBar.setProgress(step.getSubPerc(sub));
        popupView.addView(seekBar);
        popupWindow.showAsDropDown(iv, -100, -200);
    }

    /** SET **/
    public void setProgress(float progress){
        seekBar.setProgress(progress);
    }
}
