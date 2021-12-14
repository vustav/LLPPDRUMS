package com.kiefer.popups.seqModules;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.Popup;

public abstract class SubsPopup extends Popup {
    protected SequenceModule sequenceModule;
    protected Step step;
    protected ImageView subIV;

    public abstract View.OnClickListener getListener(FrameLayout subLayout, int sub);

    public abstract void setSubLayout(FrameLayout layout, Step step, int sub);

    public SubsPopup(LLPPDRUMS llppdrums, SequenceModule sequenceModule, final Step step, final ImageView subIV){
        super(llppdrums);
        this.sequenceModule = sequenceModule;
        this.step = step;
        this.subIV = subIV;

        //inflate the View
        final LinearLayout popupView = new LinearLayout(llppdrums);
        popupView.setWeightSum(step.getNofSubs());

        //create the popupWindow
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        int stepWidth = (int) llppdrums.getResources().getDimension(R.dimen.sequencerStepWidth);
        int stepHeight = (int) llppdrums.getResources().getDimension(R.dimen.sequencerTrackStepHeight);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(stepWidth, stepHeight, 1);
        llp.setMarginStart(2);

        for(int sub = 0; sub < step.getNofSubs(); sub++){

            final FrameLayout subLayout = new FrameLayout(llppdrums);
            subLayout.setLayoutParams(llp);

            setSubLayout(subLayout, step, sub);

            popupView.addView(subLayout);

            //final int finalSub = sub;
            subLayout.setOnClickListener(getListener(subLayout, sub));
        }

        popupWindow.showAsDropDown(subIV, -stepWidth * step.getNofSubs() / 2, -30);
    }
}
