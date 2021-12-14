package com.kiefer.popups.controller;

import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.controller.Controller;
import com.kiefer.info.controller.SeqBtnOptionsInfo;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

public class SeqBtnEditPopup extends Popup {

    public SeqBtnEditPopup(LLPPDRUMS llppdrums, final Controller controller){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_seq_btn_edit, null);
        popupView.setBackground(controller.getSeqPopupGradient());

        //create the popupWindow
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //set up the momentary-check
        final CheckBox momentaryBox = popupView.findViewById(R.id.seqBtnPopupMomentaryCheck);
        momentaryBox.setChecked(controller.getSeqBtnManager().isMomentary());
        momentaryBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.getSeqBtnManager().setMomentary(momentaryBox.isChecked());
            }
        });

        //setup the TV
        String name = llppdrums.getResources().getString(R.string.seqBtnEditLabel);
        TextView label = popupView.findViewById(R.id.seqBtnPopupLabel);
        label.setText(name);
        int bgColor = ContextCompat.getColor(llppdrums, R.color.tabsActiveTxtBgColor);
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.seqBtnOptionsInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, SeqBtnOptionsInfo.key);
            }
        });

        //show the popup with a little offset
        show(popupWindow);
    }
}
