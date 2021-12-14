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
import com.kiefer.randomization.rndTrackManager.RndTrackManager;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;

public class RndSeqManagerOscListPopup extends Popup {

    public RndSeqManagerOscListPopup(LLPPDRUMS llppdrums, final RndSeqManagerPopup rndSeqManagerPopup, final RndSeqManager rndSeqManager, int trackNo, final CSpinnerButton btn){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, rndSeqManager.getTracks().get(trackNo).getOscListImgId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        //get the list of oscillatorPresets from OscillatorManager
        ArrayList<String> oscPresets = rndSeqManager.getSoundPresetCategories();

        for(int i = 0; i < oscPresets.size() + 1; i++){
            TextView tv = new TextView(llppdrums);

            //RANDOM is not in the oscPresetList so we add it manually here
            String name;
            if(i == oscPresets.size()){
                name = RndTrackManager.RANDOM;
            }
            else{
               name =  oscPresets.get(i);
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

            //final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rndSeqManager.setTrackOsc(trackNo, name);
                    rndSeqManagerPopup.addModifiedMarker();
                    btn.setSelection(name);
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }
        popupWindow.showAsDropDown(btn, 0, -100);
    }
}
