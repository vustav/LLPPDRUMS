package com.kiefer.popups.sequencer;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

public class CopyFromPopup extends Popup {

    public CopyFromPopup(final LLPPDRUMS llppdrums, final CSpinnerButton btn){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        //popupView.setBackground(ContextCompat.getDrawable(llppdrums, oscillatorManager.getWavePopupImageId(oscNo)));
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, llppdrums.getDrumMachine().getSelectedSequence().getCopyFromBgId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        int nOfLines = 0; //used to know what color the lines should have (since selectedSequence doesn't become a line we can't use i)
        for(int i = 0; i < llppdrums.getResources().getInteger(R.integer.nOfSequences); i++){
            if(i != llppdrums.getDrumMachine().getSelectedSequenceIndex()) {
                nOfLines++;
                TextView tv = new TextView(llppdrums);
                tv.setWidth(DEF_LIST_WIDTH);
                tv.setText(Integer.toString(i));

                int color;
                if (nOfLines % 2 == 0) {
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
                        //llppdrums.getDrumMachine().getSelectedSequence().load(llppdrums.getDrumMachine().getSequences().get(finalI).getKeeper());
                        llppdrums.getDrumMachine().loadSelectedSequence(llppdrums.getDrumMachine().getSequences().get(finalI).getKeeper());
                        llppdrums.getSequencer().notifyDataSetChange();
                        popupWindow.dismiss();
                    }
                });
                listLayout.addView(tv);
            }
        }

        //show(popupWindow);
        //popupWindow.showAsDropDown(btn, 0, -100);
        show(popupWindow);
    }
}
