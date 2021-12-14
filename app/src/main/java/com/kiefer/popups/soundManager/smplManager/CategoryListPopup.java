package com.kiefer.popups.soundManager.smplManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.track.soundManager.SoundManager;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

public class CategoryListPopup extends Popup {
    private final CSpinnerButton categorySpinnerBtn, sampleSpinnerBtn;

    public CategoryListPopup(LLPPDRUMS llppdrums, final SoundManager soundManager, final CSpinnerButton categorySpinnerBtn, final CSpinnerButton sampleSpinnerBtn){
        super(llppdrums);
        this.categorySpinnerBtn = categorySpinnerBtn;
        this.sampleSpinnerBtn =sampleSpinnerBtn;

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, soundManager.getPresetsListImageId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        for(int i = 0; i < soundManager.getPresetCategories().size(); i++){
            TextView tv = new TextView(llppdrums);
            tv.setText(soundManager.getPresetCategories().get(i));

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
                    soundManager.setPreset(finalI);
                    categorySpinnerBtn.setSelection(soundManager.getPresetCategories().get(finalI));
                    sampleSpinnerBtn.setSelection(soundManager.getSmplManager().getSelectedCategory().getSelectedSample().getName());
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }
        popupWindow.showAsDropDown(categorySpinnerBtn, 0, -100);
    }
}
