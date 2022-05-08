package com.kiefer.randomization.rndSeqManager.rndSeqManagerPopup;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.SubsSliderDrawable;
import com.kiefer.popups.Popup;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.utils.ColorUtils;

public class RndSeqManagerPercSubPopup extends Popup {

    public RndSeqManagerPercSubPopup(LLPPDRUMS llppdrums, RndSeqManagerPopup rndSeqManagerPopup, final RndSeqPresetTrack.Step step, final ImageView subIV) {
        super(llppdrums);

        FrameLayout popupView = new FrameLayout(llppdrums);
        int padding = (int) llppdrums.getResources().getDimension(R.dimen.defaultBtnPadding);
        popupView.setPadding(padding, padding, padding, padding);
        popupView.setBackgroundColor(ColorUtils.getRandomColor());

        final LinearLayout stepsLayout = new LinearLayout(llppdrums);

        stepsLayout.setWeightSum(step.getNofSubs());
        popupView.addView(stepsLayout);

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


            //subLayout.setBackground(new AutoRndSliderSubsDrawable(llppdrums, true, step.getSubPerc(sub)));


            stepsLayout.addView(subLayout);

            //final int finalSub = sub;
            //subLayout.setOnClickListener(getListener(subLayout, sub));
            final int finalSub = sub;
            subLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new RndSeqManagerPercPopup(llppdrums, rndSeqManagerPopup, subIV, step, finalSub, RndSeqManagerPercSubPopup.this, subLayout);
                }
            });
        }

        popupWindow.showAsDropDown(subIV, -stepWidth * step.getNofSubs() / 2, -30);
    }

    /*
    public View.OnClickListener getListener(FrameLayout subLayout, int sub){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new AutoRndPercPopup(llppdrums, sequenceModule, autoRandomModule, subIV, step, sub, AutoRndPercSubsPopup.this, subLayout);
                new RndSeqPercPopup(llppdrums, rndSeqManagerPopup, stepIV, stepNo, rndSeqManager.getTracks().get(rndSeqManagerTrackViewHolder.getAdapterPosition()).getSteps().get(stepNo));
            }
        };
    }

     */

    public void setSubLayout(FrameLayout layout, RndSeqPresetTrack.Step step, int sub){
        //boolean on = (step.isOn() && step.isSubOn(sub)) || step.getAutoRndOn(sub) || sequenceModule instanceof OnOff;
        //layout.setBackground(new AutoRndSliderSubsDrawable(llppdrums, on, sequenceModule.getAutoRndPerc(step, sub)));
        //int color = R.color.rndSeqPercColor;
        layout.setBackground(new SubsSliderDrawable(llppdrums, true, step.getSubPerc(sub), SubsSliderDrawable.BLUE));
    }
}
