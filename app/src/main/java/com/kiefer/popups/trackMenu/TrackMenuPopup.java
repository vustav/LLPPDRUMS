package com.kiefer.popups.trackMenu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSeekBar;
import com.kiefer.info.sequence.trackMenu.TrackMenuInfo;
import com.kiefer.machine.DrumMachine;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;

public class TrackMenuPopup extends Popup {
    private final ArrayList<View> lockableUI = new ArrayList<>();
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
        lockableUI.add(subBtn);
        /*
            if (llppdrums.getDrumMachine().getSelectedSequence() == llppdrums.getDrumMachine().getPlayingSequence() && llppdrums.getEngineFacade().isPlaying()) {
                subBtn.setEnabled(false);
            }

         */

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

        //rnd
        Button rndBtn = popupView.findViewById(R.id.sequencerTrackRndBtn);
        rndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumMachine.openRndPopup(trackNo, parent);
                popupWindow.dismiss();
            }
        });

        //fx
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

        //mixer
        FrameLayout mixerBtnGraphics = popupView.findViewById(R.id.sequencerTrackMixerBtnGraphics);
        Button mixerBtn = popupView.findViewById(R.id.sequencerTrackMixerBtn);

        CSeekBar volBar = new CSeekBar(llppdrums, CSeekBar.VERTICAL_DOWN_UP);
        volBar.setMargin(0);
        volBar.setThumb(false);
        volBar.setColors(ContextCompat.getColor(llppdrums, R.color.popupBarColor), ContextCompat.getColor(llppdrums, R.color.popupBarBg));
        volBar.setProgress(llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackNo).getTrackVolume() / 10f);

        View.OnClickListener volListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                new TrackVolPopup(llppdrums, llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackNo), mixerBtn, volBar);
                popupWindow.dismiss();
            }
        };

        //add the listener to both. Only button makes the slider on the button clickable and modifiable
        //and only the slider makes it do nothing if you click the button beside the slider
        volBar.setOnClickListener(volListener);
        mixerBtn.setOnClickListener(volListener);

        mixerBtnGraphics.addView(volBar);

        //remove
        Button removeBtn = popupView.findViewById(R.id.sequencerTrackRemoveTrackBtn);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drumMachine.getSelectedSequence().removeTrack(trackNo);
                popupWindow.dismiss();
            }
        });
        /*
        if(llppdrums.getDrumMachine().getSelectedSequence() == llppdrums.getDrumMachine().getPlayingSequence() && llppdrums.getEngineFacade().isPlaying()){
            removeBtn.setEnabled(false);
        }

         */
        lockableUI.add(removeBtn);

        if(LLPPDRUMS.hideUIonPlay && (llppdrums.getDrumMachine().getSelectedSequence() == llppdrums.getDrumMachine().getPlayingSequence() && llppdrums.getEngineFacade().isPlaying())) {
            lockUI();
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

    private void lockUI(){
        for (View v : lockableUI) {
            v.setEnabled(false);
        }
    }
}
