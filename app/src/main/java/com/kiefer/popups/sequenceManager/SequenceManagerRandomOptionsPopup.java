package com.kiefer.popups.sequenceManager;

import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.sequenceManager.SequenceManagerRandomOptionsInfo;
import com.kiefer.machine.SequenceManager;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

public class SequenceManagerRandomOptionsPopup extends Popup {

    public SequenceManagerRandomOptionsPopup(LLPPDRUMS llppdrums, SequenceManager sequenceManager){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_sequence_manager_random_options, null);
        popupView.setBackground(sequenceManager.getRandomPopupGradient());

        //create the popupWindow
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //setup the TV
        String name = llppdrums.getResources().getString(R.string.seqManagerEditRndLabel);
        TextView label = popupView.findViewById(R.id.label);
        label.setText(name);
        int bgColor = ContextCompat.getColor(llppdrums, R.color.tabsActiveTxtBgColor);
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.seqManRndOptionsInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, SequenceManagerRandomOptionsInfo.key);
            }
        });

        //show the popup with a little offset
        show(popupWindow);
    }
}
