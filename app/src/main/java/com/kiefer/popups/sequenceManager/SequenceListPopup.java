package com.kiefer.popups.sequenceManager;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.SequenceManager;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

public class SequenceListPopup extends Popup {

    public SequenceListPopup(final LLPPDRUMS llppdrums, final SequenceManager sequenceManager, final int step, final CSpinnerButton btn){
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

        for(int i = 0; i < llppdrums.getDrumMachine().getNOfSequences(); i++){
            TextView tv = new TextView(llppdrums);
            tv.setWidth(DEF_LIST_WIDTH);
            tv.setText(sequenceManager.getDrumSequences().get(i).getName());

            /*
            int color;
            if (i % 2 == 0) {
                color = ContextCompat.getColor(llppdrums, R.color.wavesBgEven);
            } else {
                color = ContextCompat.getColor(llppdrums, R.color.wavesBgUneven);
            }

             */
            int color = sequenceManager.getDrumSequences().get(i).getColor();
            tv.setBackgroundColor(color);
            tv.setTextSize((int) llppdrums.getResources().getDimension(R.dimen.defaultListTxtSize));
            tv.setTextColor(ColorUtils.getContrastColor(color));

            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btn.setSelection(llppdrums.getDrumMachine().getSequences().get(finalI).getName());
                    sequenceManager.setStepSelection(step, finalI);
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }

        show(popupWindow);
    }
}
