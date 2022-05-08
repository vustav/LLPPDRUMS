package com.kiefer.randomization.rndSeqManager.rndSeqManagerPopup;

import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.info.sequence.SeqRndOptionsInfo;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;
import com.kiefer.popups.Popup;
import com.kiefer.popups.sequencer.TempoPopup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

public class RndSeqManagerPopup extends Popup {
    private final RndSeqManager rndSeqManager;
    private final CSpinnerButton cSpinnerButton, tempoSpinnerBtn;

    //recyclerView
    final private RecyclerView recyclerView;
    final private RndSeqManagerAdapter adapter;

    public RndSeqManagerPopup(LLPPDRUMS llppdrums, RndSeqManager rndSeqManager){
        super(llppdrums);
        this.rndSeqManager = rndSeqManager;

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_seq_rnd_options, null);
        popupView.setBackground(ContextCompat.getDrawable(llppdrums, rndSeqManager.getBgImgId()));

        //create the popupWindow
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //set a margin on the window since we want to max its size
        Display display = llppdrums.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        popupWindow.setWidth(size.x-size.x/5);
        popupWindow.setHeight(size.y-size.y/20);
        popupWindow.setFocusable(true);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //setup the TV
        TextView label = popupView.findViewById(R.id.seqRndOptionsLabel);
        label.setText(llppdrums.getResources().getString(R.string.randomizeSeqLabel));
        int bgColor = ColorUtils.getRandomColor();
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

        //set the bg of the top row
        LinearLayout presetLayout = popupView.findViewById(R.id.seqRndOptionsTopRowBg);
        presetLayout.setBackground(ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor()));

        //preset spinner
        cSpinnerButton = new CSpinnerButton(llppdrums);
        cSpinnerButton.setSelection(rndSeqManager.getSelectedRandomizePreset().getName());

        cSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RndSeqManagerPresetListPopup(llppdrums, RndSeqManagerPopup.this, rndSeqManager, cSpinnerButton);
            }
        });

        FrameLayout spinnerContainer = popupView.findViewById(R.id.seqRndOptionsPresetContainer);
        spinnerContainer.addView(cSpinnerButton);

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.seqRndOptionsBaseInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, SeqRndOptionsInfo.key);
            }
        });

        //tempo
        tempoSpinnerBtn = new CSpinnerButton(llppdrums);
        tempoSpinnerBtn.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TempoPopup(llppdrums, tempoSpinnerBtn, rndSeqManager);
                addModifiedMarker();
            }
        });

        //lower tempo
        Button lowerTempoBtn = popupView.findViewById(R.id.trackRndOptionsLowerTempoBtn);
        lowerTempoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rndSeqManager.setTempo(rndSeqManager.getTempo()-1);
                tempoSpinnerBtn.setSelection(Integer.toString(rndSeqManager.getTempo()));
                addModifiedMarker();
            }
        });

        //raise tempo
        Button raiseTempoBtn = popupView.findViewById(R.id.seqRndOptionsRaiseTempoBtn);
        raiseTempoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //llppdrums.getDrumMachine().getSelectedSequence().raiseTempo();
                rndSeqManager.setTempo(rndSeqManager.getTempo()+1);
                tempoSpinnerBtn.setSelection(Integer.toString(rndSeqManager.getTempo()));
                addModifiedMarker();
            }
        });

        LinearLayout tempoLayout = popupView.findViewById(R.id.seqRndOptionsTempoLayout);
        tempoLayout.setBackground(ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor()));

        FrameLayout tempoContainer = popupView.findViewById(R.id.seqRndOptionsTempoContainer);
        tempoContainer.addView(tempoSpinnerBtn);

        setTempo(rndSeqManager.getTempo());

        //steps

        //remove steps
        Button lowerStepsBtn = popupView.findViewById(R.id.seqRndOptionsRemoveStepBtn);
        lowerStepsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rndSeqManager.removeStep();
                addModifiedMarker();
                notifyDataSetChanged();
            }
        });

        //add steps
        Button raiseStepsBtn = popupView.findViewById(R.id.seqRndOptionsAddStepBtn);
        raiseStepsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rndSeqManager.addStep();
                addModifiedMarker();
                notifyDataSetChanged();
            }
        });

        LinearLayout stepsLayout = popupView.findViewById(R.id.seqRndOptionsStepsLayout);
        stepsLayout.setBackground(ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor()));

        //FrameLayout stepsContainer = popupView.findViewById(R.id.trackRndOptionsStepsContainer);
        //stepsContainer.addView(stepsSpinnerBtn);

        //setSteps(rndSeqManager.getSteps());

        //set up the addTraxBtn
        Button addBtn = popupView.findViewById(R.id.trackRndOptionsAddTrackBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new InfoPopup(llppdrums, SeqRndOptionsBaseInfo.key);
                rndSeqManager.addTrack();
                notifyDataSetChanged();
                addModifiedMarker();
            }
        });

        //setup the fx-recyclerView
        recyclerView = popupView.findViewById(R.id.seqRndOptionsRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        LinearLayoutManager trackLayoutManager = new LinearLayoutManager(llppdrums);
        trackLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(trackLayoutManager);

        //create an adapter
        adapter = new RndSeqManagerAdapter(llppdrums, this, rndSeqManager);

        //create and attach the ItemTouchHelper
        ItemTouchHelper.Callback fxCallback = new RndSeqManagerTouchHelper(adapter);
        ItemTouchHelper fxHelper = new ItemTouchHelper(fxCallback);
        fxHelper.attachToRecyclerView(recyclerView);

        //add the adapter to the recyclerView
        recyclerView.setAdapter(adapter);

        //show the popup with a little offset
        show(popupWindow);
    }

    public void addModifiedMarker(){
        getRndSeqManager().addModifiedMarker();
        cSpinnerButton.setSelection(rndSeqManager.getSelectedRandomizePreset().getName());
    }

    public void removeModifiedMarker(){
        getRndSeqManager().removeModifiedMarker();
        cSpinnerButton.setSelection(rndSeqManager.getSelectedRandomizePreset().getName());
    }

    public void moveTrack(int from, int to){
        rndSeqManager.moveTrack(from, to);
    }

    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
    }

    public void removeTrack(int trackNo){
        rndSeqManager.removeTrack(trackNo);
        notifyDataSetChanged();
    }

    /** SET **/
    public void setTempo(int tempo){
        if(tempoSpinnerBtn != null) {
            tempoSpinnerBtn.setSelection(Integer.toString(tempo));
            rndSeqManager.setTempo(tempo);
        }
    }
    /*
    public void setSteps(int steps){
        //if(stepsSpinnerBtn != null) {
            //stepsSpinnerBtn.setSelection(Integer.toString(steps));
            rndSeqManager.setSteps(steps);
            //notifyDataSetChanged();
        //}
    }

     */
    /** GET **/
    public RndSeqManager getRndSeqManager(){
        return rndSeqManager;
    }
}
