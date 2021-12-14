package com.kiefer.randomization.rndSeqManager.rndSeqManagerPopup;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

public class RndSeqManagerPresetListPopup extends Popup {

    public RndSeqManagerPresetListPopup(LLPPDRUMS llppdrums, final RndSeqManagerPopup rndSeqManagerPopup, final RndSeqManager rndSeqManager, final CSpinnerButton btn){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, rndSeqManager.getPresetListImgId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        for(int i = 0; i < rndSeqManager.getRndSeqPresets().size(); i++){
            TextView tv = new TextView(llppdrums);

            //remove any * from the end of the string
            String name = rndSeqManager.getRndSeqPresets().get(i).getName();
            if((name.substring(name.length()-1).equals(llppdrums.getResources().getString(R.string.randomizerModifiedName)))) {
                name = name.substring(0, name.length()-1);
            }

            tv.setText(name);

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
                    rndSeqManager.setSelectedRandomizePreset(rndSeqManager.getRndSeqPresets().get(finalI).getName());
                    rndSeqManagerPopup.removeModifiedMarker();
                    btn.setSelection(rndSeqManager.getRndSeqPresets().get(finalI).getName());
                    rndSeqManagerPopup.notifyDataSetChanged();
                    rndSeqManagerPopup.setTempo(rndSeqManager.getTempo()); //update the tempo-spinner
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }
        popupWindow.showAsDropDown(btn, 0, -100);
    }
}
