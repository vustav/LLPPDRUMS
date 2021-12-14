package com.kiefer.popups.sequenceManager;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.info.sequenceManager.SequenceManagerInfo;
import com.kiefer.machine.SequenceManager;
import com.kiefer.popups.Popup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

public class SequenceManagerPopup extends Popup {
    private final LinearLayout boxesLayout;

    private final CheckBox randProgressBox;
    private final Button editRandBtn;

    public SequenceManagerPopup(final LLPPDRUMS llppdrums, final SequenceManager sequenceManager){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_sequence_manager, null);
        popupView.setBackground(sequenceManager.getPopupGradient());

        //create the popupWindow
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //setup the boxes
        boxesLayout = popupView.findViewById(R.id.seqManagerBoxContainer);

        int boxWidth = (int) llppdrums.getResources().getDimension(R.dimen.seqManagerPopBoxWidth);

        for(int i = 0; i < llppdrums.getResources().getInteger(R.integer.nOfSeqBoxes); i++){
            final int finalI = i;
            final CSpinnerButton cSpinnerBtn = new CSpinnerButton(llppdrums);
            cSpinnerBtn.setWidth(boxWidth);
            cSpinnerBtn.getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SequenceListPopup(llppdrums, sequenceManager, finalI, cSpinnerBtn);
                }
            });
            cSpinnerBtn.setSelection(sequenceManager.getBoxSelection(i));
            boxesLayout.addView(cSpinnerBtn);

            if(sequenceManager.getNOfActiveBoxes() <= i){
                cSpinnerBtn.setEnabled(false);
            }
        }

        final SeekBar seekBar = popupView.findViewById(R.id.sequenceManagerActiveBoxesSeekBar);
        seekBar.setProgress(sequenceManager.getNOfActiveBoxes());
        seekBar.setMax(llppdrums.getResources().getInteger(R.integer.nOfSeqBoxes));
        int sliderWidth = boxWidth * llppdrums.getResources().getInteger(R.integer.nOfSeqBoxes);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(sliderWidth, (int) llppdrums.getResources().getDimension(R.dimen.seqManagerPopActiveBoxesSliderHeight));
        seekBar.setLayoutParams(llp);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                if(seekBar.getProgress() == 0){
                    seekBar.setProgress(1);
                }
                sequenceManager.setNOfActiveBoxes(seekBar.getProgress());
                enableBoxes(seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sequenceManager.setNOfActiveBoxes(seekBar.getProgress());
                enableBoxes(seekBar.getProgress());
            }
        });

        //and the randomize progress-checkbox
        randProgressBox = popupView.findViewById(R.id.seqManagerRandProgressCheck);
        randProgressBox.setChecked(sequenceManager.getRandomizeProgress());
        randProgressBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequenceManager.setRandomizeProgress(randProgressBox.isChecked());
            }
        });
        //randProgressBox.setEnabled(sequenceManager.getProgress());
        enableRandomizeProgress(sequenceManager.getProgress());

        //and the edit-btn
        editRandBtn = popupView.findViewById(R.id.seqManagerPopEditRand);
        editRandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SequenceManagerRandomOptionsPopup(llppdrums, sequenceManager);
            }
        });

        //and the progress-checkbox
        final CheckBox progressBox = popupView.findViewById(R.id.seqManagerProgressCheck);
        progressBox.setChecked(sequenceManager.getProgress());
        progressBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequenceManager.setProgress(progressBox.isChecked());
                enableRandomizeProgress(sequenceManager.getProgress());
            }
        });

        //and the queue-checkbox
        final CheckBox pressBox = popupView.findViewById(R.id.seqManagerQueueCheck);
        pressBox.setChecked(sequenceManager.changeAtPress());
        pressBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequenceManager.setQueue(pressBox.isChecked());
            }
        });

        //and the restart-checkbox
        final CheckBox restartBox = popupView.findViewById(R.id.seqManagerResetAtStopCheck);
        restartBox.setChecked(sequenceManager.changeAtPress());
        restartBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sequenceManager.setRestartAtStop(restartBox.isChecked());
            }
        });

        //set the TV-colors
        TextView queueTV = popupView.findViewById(R.id.seqManagerQueueTV);
        queueTV.setTextColor(ColorUtils.getContrastColor(sequenceManager.getFirstGradientColor()));
        TextView progressTV = popupView.findViewById(R.id.seqManagerProgressTV);
        progressTV.setTextColor(ColorUtils.getContrastColor(sequenceManager.getFirstGradientColor()));
        TextView rndTV = popupView.findViewById(R.id.seqManagerRndTV);
        rndTV.setTextColor(ColorUtils.getContrastColor(sequenceManager.getFirstGradientColor()));
        TextView restartTV = popupView.findViewById(R.id.seqManagerResetAtStopTV);
        restartTV.setTextColor(ColorUtils.getContrastColor(sequenceManager.getFirstGradientColor()));

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //setup the TV
        String name = llppdrums.getResources().getString(R.string.seqManagerLabel);
        TextView label = popupView.findViewById(R.id.label);
        label.setText(name);
        int bgColor = ContextCompat.getColor(llppdrums, R.color.tabsActiveTxtBgColor);
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.seqManagerInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, SequenceManagerInfo.key);
            }
        });

        //show the popup with a little offset
        show(popupWindow);
    }

    public void enableBoxes(int n){

        //enable all boxes in the counter
        for(int i = 0; i < boxesLayout.getChildCount(); i++){
            boxesLayout.getChildAt(i).setEnabled(true);
        }

        //disable the boxes over n
        for(int i = n; i < boxesLayout.getChildCount(); i++){
            boxesLayout.getChildAt(i).setEnabled(false);
        }
    }

    private void enableRandomizeProgress(boolean enabled){
        randProgressBox.setEnabled(enabled);
    }
}
