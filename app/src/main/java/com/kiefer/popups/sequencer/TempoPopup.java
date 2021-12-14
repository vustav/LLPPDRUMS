package com.kiefer.popups.sequencer;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.interfaces.Tempoizer;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;

public class TempoPopup extends Popup {

    public TempoPopup(final LLPPDRUMS llppdrums, final CSpinnerButton btn, Tempoizer tempoizer){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, llppdrums.getDrumMachine().getSelectedSequence().getTempoBgId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        //set up the tempo spinner
        //create an array with tempos
        final ArrayList<Integer> tempos = new ArrayList<>();
        for(int tempo = llppdrums.getResources().getInteger(R.integer.minTempo); tempo <= llppdrums.getResources().getInteger(R.integer.maxTempo); tempo+=llppdrums.getResources().getInteger(R.integer.tempoInterval)){
            tempos.add(tempo);
        }

        for(int i = 0; i < tempos.size(); i++){
            TextView tv = new TextView(llppdrums);
            tv.setWidth(DEF_LIST_WIDTH);
            tv.setText(Integer.toString(tempos.get(i)));

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
                    int tempo = tempos.get(finalI);
                    tempoizer.setTempo(tempo);
                    btn.setSelection(Integer.toString(tempo));
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }

        show(popupWindow);
    }
}
