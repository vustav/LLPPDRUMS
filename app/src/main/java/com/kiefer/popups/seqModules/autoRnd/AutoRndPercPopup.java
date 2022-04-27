package com.kiefer.popups.seqModules.autoRnd;

import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSeekBar;
import com.kiefer.machine.sequence.sequenceModules.OnOff;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomModule;
import com.kiefer.popups.Popup;

public class AutoRndPercPopup extends Popup {
    private CSeekBar seekBar;
    private View.OnTouchListener onTouchListener;

    public AutoRndPercPopup(final LLPPDRUMS llppdrums, final SequenceModule sequenceModule, final AutoRandomModule autoRandomModule, final ImageView iv, final Step step, int sub){
        this(llppdrums, sequenceModule, autoRandomModule, iv, step, sub, null, null);
    }

    public AutoRndPercPopup(final LLPPDRUMS llppdrums, final SequenceModule sequenceModule, final AutoRandomModule autoRandomModule, final ImageView iv, final Step step, int sub, AutoRndPercSubsPopup subsPopup, FrameLayout subLayout){
        super(llppdrums);
        //Log.e("AutoRndPercPopup", "contr()");

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

        /** SLÅ IHOP **/
        int shapeColor;

        if ((step.isOn() && step.isSubOn(sub)) || step.getAutoRndOn(sub) || sequenceModule instanceof OnOff) {
            shapeColor = R.color.popupBarColor;
        }
        else{
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
                        //sequenceModule.setAutoRndPerc(step, seekBar.getProgress(), sub);
                        break;
                    case MotionEvent.ACTION_UP:
                        seekBar.onTouchEvent(event);
                        sequenceModule.setAutoRndPerc(step, seekBar.getProgress(), sub);
                        iv.setImageDrawable(autoRandomModule.getDrawable(step));

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
        seekBar.setProgress(sequenceModule.getAutoRndPerc(step, sub));
        popupView.addView(seekBar);
        popupWindow.showAsDropDown(iv, -100, -200);
    }

    /** SET **/
    public void setProgress(float progress){
        //Log.e("AutoRndPercPopup", "setProgress()");
        seekBar.setProgress(progress);
    }
}
