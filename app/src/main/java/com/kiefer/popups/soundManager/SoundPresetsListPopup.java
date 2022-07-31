package com.kiefer.popups.soundManager;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Stackables.sound.SoundManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.SoundSourceManager;
import com.kiefer.popups.Popup;
import com.kiefer.popups.soundManager.SoundManagerPopupOLD;
import com.kiefer.utils.ColorUtils;

public class SoundPresetsListPopup extends Popup {
    private final CSpinnerButton btn;

    public SoundPresetsListPopup(LLPPDRUMS llppdrums, DrumTrack drumTrack, SoundManagerPopup soundManagerPopup, final CSpinnerButton btn){
        super(llppdrums);
        this.btn = btn;

        //SoundManager soundManager = drumTrack.getSoundManager();
        SoundSourceManager soundSourceManager = (SoundSourceManager) drumTrack.getSoundManager().getSelectedStackable();
        SoundManagerPopupOLD.SoundSourceView soundSourceView = ((SoundSourceManager)drumTrack.getSoundManager().getSelectedStackable()).getSoundView();

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, soundSourceManager.getOscillatorManager().getPresetListImageId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        for(int i = 0; i < soundSourceManager.getOscillatorManager().getPresets().size(); i++){
            String name = soundSourceManager.getOscillatorManager().getPresets().get(i).getName();

            TextView tv = new TextView(llppdrums);
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
                    drumTrack.getRndTrackManager().rndCat(name, .7f);
                    soundSourceView.updateUI();
                    soundManagerPopup.updateAdapter();
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }
        //popupWindow.showAsDropDown(btn, 0, -100);
        show(popupWindow);
    }
}
