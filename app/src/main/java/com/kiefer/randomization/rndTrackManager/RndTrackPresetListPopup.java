package com.kiefer.randomization.rndTrackManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.Popup;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackBassBasic;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackHHBasic;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackHHOffbeat;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackHotTom;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackSnareBasic;
import com.kiefer.utils.ColorUtils;

public class RndTrackPresetListPopup extends Popup {
    private final DrumTrack drumTrack;
    private final PopupWindow popupWindow;
    private final LinearLayout listLayout;

    public RndTrackPresetListPopup(LLPPDRUMS llppdrums, DrumTrack drumTrack, final CSpinnerButton btn){
        super(llppdrums);

        this.drumTrack = drumTrack;

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, drumTrack.getRndTrackManager().getPresetListImgId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        listLayout = popupView.findViewById(R.id.listLayout);

        createItem(new RndSeqPresetTrackBassBasic(llppdrums, drumTrack.getNOfSteps(), 1, 4));
        createItem(new RndSeqPresetTrackSnareBasic(llppdrums, drumTrack.getNOfSteps(), 1, 4));
        createItem(new RndSeqPresetTrackHHBasic(llppdrums, drumTrack.getNOfSteps(), 1));
        createItem(new RndSeqPresetTrackHHOffbeat(llppdrums, drumTrack.getNOfSteps(), 1));
        createItem(new RndSeqPresetTrackHotTom(llppdrums, drumTrack.getNOfSteps(), 3));

        popupWindow.showAsDropDown(btn, 0, -100);
    }

    private void createItem(RndSeqPresetTrack presetTrack){
        TextView tv = new TextView(llppdrums);

        tv.setText(presetTrack.getName());

        int color;
        if(listLayout.getChildCount() % 2 == 0){
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
                drumTrack.getRndTrackManager().randomize(presetTrack);
                popupWindow.dismiss();
            }
        });
        listLayout.addView(tv);
    }
}
