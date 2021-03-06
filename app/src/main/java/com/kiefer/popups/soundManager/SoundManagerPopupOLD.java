package com.kiefer.popups.soundManager;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.sequence.trackMenu.SoundManagerInfo;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.SoundSourceManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.sampleManager.SmplManager;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.popups.soundManager.oscillatorManager.OscillatorManagerView;
import com.kiefer.popups.soundManager.smplManager.SampleManagerView;
import com.kiefer.utils.ColorUtils;

public class SoundManagerPopupOLD extends Popup {
    private final DrumTrack drumTrack;
    private final FrameLayout soundView;
    private final OscillatorManagerView oscillatorManagerView;
    private final SampleManagerView sampleManagerView;
    //private final RadioButton oscRadio, sampleRadio;

    public SoundManagerPopupOLD(final LLPPDRUMS llppdrums, final DrumTrack drumTrack){
        super(llppdrums);
        this.drumTrack = drumTrack;

        //create the members
        oscillatorManagerView = new OscillatorManagerView(llppdrums, drumTrack);
        sampleManagerView = new SampleManagerView(llppdrums, ((SoundSourceManager)drumTrack.getSoundManager().getSelectedStackable()).getSmplManager(), drumTrack);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_sound_manager, null);
        popupView.setBackground(ContextCompat.getDrawable(llppdrums, drumTrack.getSoundManager().getBgImgId()));

        //create the popupWindow
        //int width = RelativeLayout.LayoutParams.WRAP_CONTENT;@dimen/defaultSeekBarWidth
        int width = (int) llppdrums.getResources().getDimension(R.dimen.defaultSeekBarWidth) * 2 + (int) llppdrums.getResources().getDimension(R.dimen.marginLarge) * 9;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //setup the TV
        /*
        String name = llppdrums.getResources().getString(R.string.soundManagerLabel) + drumTrack.getName();
        TextView label = popupView.findViewById(R.id.soundManagerLabel);
        label.setText(name);
        int bgColor = drumTrack.getColor();
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

         */

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.soundManagerInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, SoundManagerInfo.key);
            }
        });

        //set up the sound view
        soundView = popupView.findViewById(R.id.soundManagerSoundView);
/*
        //set up the radio btns
        oscRadio = popupView.findViewById(R.id.radioOscButton);
        oscRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.getSoundManager().setActiveSoundSource(SoundSourceManager.OSC);
                setSoundView();
            }
        });
        sampleRadio = popupView.findViewById(R.id.radioSampleButton);
        sampleRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumTrack.getSoundManager().setActiveSoundSource(SoundSourceManager.SAMPLE);
                setSoundView();
            }
        });
        setSoundView();

 */

        //show the popup with a little offset
        show(popupWindow);
    }
/*
    private void setSoundView(){
        soundView.removeAllViews();
        if(drumTrack.getSoundManager().getActiveSoundSource() instanceof OscillatorManager){
            oscRadio.setChecked(true);
            soundView.addView(oscillatorManagerView.getLayout());
        }
        if(drumTrack.getSoundManager().getActiveSoundSource() instanceof SmplManager){
            sampleRadio.setChecked(true);
            soundView.addView(sampleManagerView.getLayout());
        }
    }

 */

    public interface SoundSourceView{
        LinearLayout getLayout();
        void updateUI();
    }
}
