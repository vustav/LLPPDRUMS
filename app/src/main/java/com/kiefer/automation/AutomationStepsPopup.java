package com.kiefer.automation;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

public class AutomationStepsPopup extends Popup {

    public AutomationStepsPopup(LLPPDRUMS llppdrums, final AutomationManager automationManager, final AutomationManagerAdapter automationAdapter, final int index, final LinearLayout anchor){
        super(llppdrums);

        FrameLayout popupView = new FrameLayout(llppdrums);
        int padding = (int) llppdrums.getResources().getDimension(R.dimen.defaultBtnPadding);
        popupView.setPadding(padding, padding, padding, padding);
        popupView.setBackgroundColor(ColorUtils.getRandomColor());

        final LinearLayout stepsLayout = new LinearLayout(llppdrums);
        stepsLayout.setWeightSum(automationManager.getAutomations().get(index).getNOfSteps());
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

        for(int step = 0; step < automationManager.getAutomations().get(index).getNOfSteps(); step++){

            final FrameLayout stepLayout = new FrameLayout(llppdrums);
            stepLayout.setLayoutParams(llp);

            setStepColor(stepLayout, automationManager.getAutomations().get(index).isStepOn(step));

            stepsLayout.addView(stepLayout);

            final int finalStep = step;
            stepLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean on = !automationManager.getStepOn(index, finalStep);

                    setStepColor(stepLayout, on);
                    automationManager.setStepOn(index, finalStep, on);
                    automationAdapter.notifyDataSetChanged();
                }
            });
        }

        //popupWindow.showAsDropDown(anchor, -stepWidth * automationManager.getAutomations().get(index).getNOfSteps() / 2, -30);
        show(popupWindow);
    }

    private void setStepColor(FrameLayout stepLayout, boolean on){
        if(on){
            stepLayout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.automationStepOn));
        }
        else{
            stepLayout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.automationStepOff));
        }
    }
}
