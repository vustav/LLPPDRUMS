package com.kiefer.popups.trackMenu;

import androidx.core.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.sequence.trackMenu.OrganizeTrackInfo;
import com.kiefer.machine.sequence.sequenceModules.OnOff;
import com.kiefer.machine.sequence.sequenceModules.Pan;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.AutoRandom;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomModuleBool;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

public class OrganizeStepsPopup extends Popup {

    public OrganizeStepsPopup(final LLPPDRUMS llppdrums, View parent, final SequenceModule sequenceModule, final DrumTrack drumTrack){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_organize_steps, null);
        popupView.setBackgroundColor(drumTrack.getColor());

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
        //String name = llppdrums.getResources().getString(R.string.automateStepsName) + drumTrack.getOscillatorManager().getName();
        String name = llppdrums.getResources().getString(R.string.organizeStepsLabel) + drumTrack.getTrackNo();
        TextView label = popupView.findViewById(R.id.organizeStepsLabel);
        label.setText(name);
        int bgColor = drumTrack.getColor();
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

        //RND-btn steps
        /*
        Button rndBtn = popupView.findViewById(R.id.autoStepsRndBtn);
        rndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.getRndTrackManager().randomizeModule(sequenceModule);
                drumTrack.updateDrawables();
            }
        });

         */

        //set listeners to the arrows if in OnOff or AutoRandomBool, hide them otherwise
        Button leftArrow = popupView.findViewById(R.id.organizeStepsLeftBtn);
        Button rightArrow = popupView.findViewById(R.id.organizeStepsRightBtn);
        if(sequenceModule instanceof OnOff || //OnOff
                (!sequenceModule.isInBaseMode() && ((AutoRandom) sequenceModule.getModuleModes().get(SequenceModule.AUTO_RANDOM)).getSelectedModule() instanceof AutoRandomModuleBool)) { //AutoRandomBool
            //set up the left arrow
            leftArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sequenceModule.pushLeft(drumTrack);
                    llppdrums.getSequencerUI().notifyDataSetChange();
                }
            });

            //set up the right arrow
            rightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sequenceModule.pushRight(drumTrack);
                    llppdrums.getSequencerUI().notifyDataSetChange();
                }
            });
        }
        else{
            ((ViewGroup)leftArrow.getParent()).removeView(leftArrow);
            ((ViewGroup)rightArrow.getParent()).removeView(rightArrow);
        }

        //SUBS
        LinearLayout subsLayout = popupView.findViewById(R.id.organizeStepsSubsLayout);

        if(drumTrack.getNOfSubs() == 1 || sequenceModule instanceof Pan){
            ((ViewGroup)subsLayout.getParent()).removeView(subsLayout);
        }
        else {
            subsLayout.setWeightSum(drumTrack.getNOfSubs());

            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, (int) llppdrums.getResources().getDimension(R.dimen.btnHeightSmall), 1);
            llp.setMarginStart(2);

            for (int sub = 0; sub < drumTrack.getOrganizeStepsSubValues().size(); sub++) {
                FrameLayout stepLayout = new FrameLayout(llppdrums);
                stepLayout.setLayoutParams(llp);


                setLayoutColor(stepLayout, drumTrack.getOrganizeStepsSubValues().get(sub));

                final int finalSub = sub;
                stepLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final boolean on = drumTrack.getOrganizeStepsSubValues().get(finalSub);
                        //if()
                        drumTrack.setOrganizeStepsSubValue(finalSub, !on);
                        setLayoutColor(stepLayout, !on);
                    }
                });
                subsLayout.addView(stepLayout);
            }
        }

        //populate the list
        LinearLayout listLayout = popupView.findViewById(R.id.organizeStepsLinearLayout);
        for(int i = 0; i < sequenceModule.getAutoStepValues().size(); i++){
            //for(final String s : sequencerModule.getAutoStepValues()){
            TextView tv = new TextView(llppdrums);

            final String s = sequenceModule.getAutoStepValues().get(i);
            tv.setText(s);

            int color;
            if(i % 2 == 0){
                color = ContextCompat.getColor(llppdrums, R.color.autoBgEven);
            }
            else{
                color = ContextCompat.getColor(llppdrums, R.color.autoBgUneven);
            }
            tv.setBackgroundColor(color);
            tv.setTextSize((int) llppdrums.getResources().getDimension(R.dimen.defaultListTxtSize));
            tv.setTextColor(ColorUtils.getContrastColor(color));

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for(int sub = 0; sub < drumTrack.getNOfSubs(); sub++){
                        sequenceModule.setAutoValue(drumTrack, s, sub);
                    }

                    llppdrums.getSequencerUI().notifyDataSetChange();
                }
            });
            listLayout.addView(tv);
        }

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.organizeStepsInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, OrganizeTrackInfo.key);
            }
        });

        show(popupWindow, Gravity.START, 0, false);
    }

    private void setLayoutColor(FrameLayout layout, boolean on){
        if(on){
            layout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.automationStepOn));
        }
        else{
            layout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.automationStepOff));
        }
    }
}
