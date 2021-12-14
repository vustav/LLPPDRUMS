package com.kiefer.popups.trackMenu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.sequence.trackMenu.TrackMenuInfo;
import com.kiefer.machine.DrumMachine;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

public class TrackMenuPopup extends Popup {
    //private FrameLayout divider0, divider1;

    public TrackMenuPopup(final LLPPDRUMS llppdrums, final DrumMachine drumMachine, final int trackNo, final View parent){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_track_btns, null);

        //popupView.findViewById(R.id.trackBgIV).setBackground(ContextCompat.getDrawable(llppdrums, llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackNo).getBtnsPopupBgId()));
        popupView.findViewById(R.id.trackBgIV).setBackground(llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackNo).getBtnsPopupGradient());

        //create the popupWindow
        //int width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        int width = (int) llppdrums.getResources().getDimension(R.dimen.btnHeightSmall) * 12;
        int height = (int) llppdrums.getResources().getDimension(R.dimen.btnHeightSmall) + (int) llppdrums.getResources().getDimension(R.dimen.sequencerTrackPadding) * 2;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        popupView.findViewById(R.id.sequencerTrackNameBtnGraphics).setBackgroundColor(llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackNo).getColor());

        //dividers
        /*
        divider0 = popupView.findViewById(R.id.sequencerTrackDivider0);
        divider0.setBackgroundColor(ColorUtils.getRandomColor());
        divider1 = popupView.findViewById(R.id.sequencerTrackDivider1);
        divider1.setBackgroundColor(ColorUtils.getRandomColor());
        */

        //name/color
        ((TextView)popupView.findViewById(R.id.sequencerTrackNameBtnTV)).setText(llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackNo).getName());
        ((TextView)popupView.findViewById(R.id.sequencerTrackNameBtnTV)).setTextColor(ColorUtils.getContrastColor(llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackNo).getColor()));
        popupView.findViewById(R.id.sequencerTrackNameBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumMachine.openNamePopup(trackNo);
                popupWindow.dismiss();
            }
        });

        //snd
        Button oscBtn = popupView.findViewById(R.id.sequencerTrackSndBtn);
        oscBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumMachine.openSoundManagerPopup(trackNo);
                popupWindow.dismiss();
            }
        });

        //subs
        final Button subBtn = popupView.findViewById(R.id.sequencerTrackSubBtn);
        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //trackNo is never used here (since the subilizer belongs to a track already) so it can be anything
                new SubsPopup(llppdrums, 666, subBtn, drumMachine.getSelectedSequence().getTracks().get(trackNo));
                popupWindow.dismiss();
            }
        });
        subBtn.setText(Integer.toString(drumMachine.getSelectedSequence().getTracks().get(trackNo).getNOfSubs()));

        //auto step
        Button autoStepBtn = popupView.findViewById(R.id.sequencerTrackAutoStepBtn);

        //set up its graphics
        FrameLayout autoStepBtnGraphics = popupView.findViewById(R.id.sequencerTrackAutoStepBtnGraphics);

        //remove the parent of the new one
        LinearLayout autoGraphics = llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackNo).getAutoBtnGraphics();
        if (autoGraphics.getParent() != null) {
            ((ViewGroup) autoGraphics.getParent()).removeView(autoGraphics);
        }
        //add it
        autoStepBtnGraphics.addView(autoGraphics);

        //set a listener
        autoStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumMachine.openAutoStepPopup(trackNo, parent);
                popupWindow.dismiss();
            }
        });

        Button rndBtn = popupView.findViewById(R.id.sequencerTrackRndBtn);
        rndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumMachine.openRndPopup(trackNo, parent);
                popupWindow.dismiss();
            }
        });

        Button fxBtn = popupView.findViewById(R.id.sequencerTrackFxBtn);
        FrameLayout fxBtnGraphics = popupView.findViewById(R.id.sequencerTrackFxBtnGraphics);
        LinearLayout fxGraphics = llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackNo).getFxBtnGraphics();
        if (fxGraphics.getParent() != null) {
            ((ViewGroup) fxGraphics.getParent()).removeView(fxGraphics);
        }
        fxBtnGraphics.addView(fxGraphics);
        fxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumMachine.openFxManagerPopup(trackNo);
                popupWindow.dismiss();
            }
        });

        Button mixerBtn = popupView.findViewById(R.id.sequencerTrackMixerBtn);
        FrameLayout mixerBtnGraphics = popupView.findViewById(R.id.sequencerTrackMixerBtnGraphics);
        LinearLayout mixerGraphics = llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackNo).getMixerBtnGraphics();
        if (mixerGraphics.getParent() != null) {
            ((ViewGroup) mixerGraphics.getParent()).removeView(mixerGraphics);
        }
        mixerBtnGraphics.addView(mixerGraphics);
        mixerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumMachine.openMixerPopup(trackNo);
                popupWindow.dismiss();
            }
        });

        Button removeBtn = popupView.findViewById(R.id.sequencerTrackRemoveTrackBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumMachine.getSelectedSequence().removeTrack(trackNo);
                popupWindow.dismiss();
            }
        });
        if(llppdrums.getDrumMachine().getSelectedSequence() == llppdrums.getDrumMachine().getPlayingSequence() && llppdrums.getEngineFacade().isPlaying()){
            removeBtn.setAlpha(.7f);
            removeBtn.setEnabled(false);
        }

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.sequencerTrackInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, TrackMenuInfo.key);
            }
        });

        popupWindow.showAsDropDown(parent, -(int) llppdrums.getResources().getDimension(R.dimen.sequencerTrackPadding), -((int) llppdrums.getResources().getDimension(R.dimen.btnHeightSmall) + 5));
    }
}
