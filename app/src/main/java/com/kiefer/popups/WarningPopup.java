package com.kiefer.popups;

import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.utils.ColorUtils;

public class WarningPopup extends Popup{

    public WarningPopup(LLPPDRUMS llppdrums, String labelTxt, String text){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_warning, null);
        int bgColor1 = ColorUtils.getRandomColor(), bgColor2 = ColorUtils.getRandomColor();
        popupView.setBackgroundDrawable(ColorUtils.getRandomGradientDrawable(bgColor1, bgColor2));

        //create the popupWindow
        int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //setup the label
        TextView label = popupView.findViewById(R.id.warningLabel);
        label.setText(labelTxt);
        int bgColor = ColorUtils.getRandomColor();
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

        //setup the TV
        TextView tv = popupView.findViewById(R.id.warningTV);
        tv.setText(text);
        tv.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.warningBg));
        tv.setTextColor(ColorUtils.getContrastColor(ContextCompat.getColor(llppdrums, R.color.warningTxt)));


        //ok
        Button muteBtn = popupView.findViewById(R.id.warningOkBtn);
        muteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        //show the popup with a little offset
        show(popupWindow);
    }
}
