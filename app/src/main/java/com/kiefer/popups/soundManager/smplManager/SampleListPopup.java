package com.kiefer.popups.soundManager.smplManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.SoundSourceManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.smpl.Sample;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;

public class SampleListPopup extends Popup {
    private final CSpinnerButton btn;

    public SampleListPopup(LLPPDRUMS llppdrums, final SoundSourceManager soundSourceManager, final CSpinnerButton btn){
        super(llppdrums);
        this.btn = btn;

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, soundSourceManager.getSmplManager().getSampleListImageId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        ArrayList<Sample> samples = soundSourceManager.getSmplManager().getSelectedCategory().getSamples();

        for(int i = 0; i < samples.size(); i++){
            TextView tv = new TextView(llppdrums);
            tv.setText(samples.get(i).getName());

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
                    soundSourceManager.getSmplManager().setSample(finalI);
                    soundSourceManager.getSmplManager().getSelectedCategory().setSelectedSample(finalI);
                    btn.setSelection(samples.get(finalI).getName());
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }
        //popupWindow.showAsDropDown(btn, 0, -100);
        show(popupWindow);
    }
}
