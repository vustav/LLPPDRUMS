package com.kiefer.popups.seqModules.autoRnd;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomModule;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;

/** THE SPINNER TO CHOOSE PERC, MIN ETC. IN AUTORANDOMIZATION **/

public class AutoRndModePopup extends Popup {

    public AutoRndModePopup(final LLPPDRUMS llppdrums, final CSpinnerButton btn){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule().getAutoRandom().getRndModuleListBg()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        final ArrayList<AutoRandomModule> autoRandomModules = llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule().getAutoRandom().getModules();

        for(int i = 0; i < autoRandomModules.size(); i++){
            TextView tv = new TextView(llppdrums);
            tv.setText(autoRandomModules.get(i).getLabel());

            int color;
            if(i % 2 == 0){
                color = ContextCompat.getColor(llppdrums, R.color.wavesBgEven);
            }
            else{
                color = ContextCompat.getColor(llppdrums, R.color.wavesBgUneven);
            }
            tv.setBackgroundColor(color);
            tv.setTextSize((int) llppdrums.getResources().getDimension(R.dimen.defaultListTxtSize));
            tv.setTextColor(ColorUtils.getContrastColor(color));

            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule().getAutoRandom().setSelectedModule(autoRandomModules.get(finalI));
                    llppdrums.getDrumMachine().updateSeqLabel();
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }

        popupWindow.showAsDropDown(btn);
    }
}
