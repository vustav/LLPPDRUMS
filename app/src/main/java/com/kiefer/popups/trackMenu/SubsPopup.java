package com.kiefer.popups.trackMenu;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.interfaces.Subilizer;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

public class SubsPopup extends Popup {

    public SubsPopup(final LLPPDRUMS llppdrums, int trackNo, final Button btn, Subilizer subilizer){
        this(llppdrums, trackNo, btn, null, subilizer);
    }

    public SubsPopup(final LLPPDRUMS llppdrums, int trackNo, final CSpinnerButton cSpinnerButton, Subilizer subilizer){
        this(llppdrums, trackNo, null, cSpinnerButton, subilizer);
    }

    private SubsPopup(final LLPPDRUMS llppdrums, int trackNo, final Button btn, final CSpinnerButton cSpinnerButton, Subilizer subilizer){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, llppdrums.getDrumMachine().getSelectedSequence().getSubsBgId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        for(int i = 1; i <= llppdrums.getResources().getInteger(R.integer.maxNOfSubs); i++){
            TextView tv = new TextView(llppdrums);
            tv.setWidth(DEF_LIST_WIDTH);
            tv.setText(Integer.toString(i));

            int color;
            if (i % 2 == 0) {
                color = ContextCompat.getColor(llppdrums, R.color.wavesBgEven);
            } else {
                color = ContextCompat.getColor(llppdrums, R.color.wavesBgUneven);
            }
            tv.setBackgroundColor(color);
            tv.setTextSize((int) llppdrums.getResources().getDimension(R.dimen.defaultListTxtSize));
            tv.setTextColor(ColorUtils.getContrastColor(color));

            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subilizer.setNOfSubs(trackNo, finalI);

                    if(cSpinnerButton == null) {
                        btn.setText(Integer.toString(finalI));
                    }
                    else{
                        cSpinnerButton.setSelection(Integer.toString(finalI));
                    }
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }

        show(popupWindow);
    }
}
