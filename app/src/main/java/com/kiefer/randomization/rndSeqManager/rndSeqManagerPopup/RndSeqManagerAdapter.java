package com.kiefer.randomization.rndSeqManager.rndSeqManagerPopup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.graphics.SeqRndManagerPercSubsDrawable;
import com.kiefer.interfaces.Subilizer;
import com.kiefer.popups.trackMenu.SubsPopup;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;

public class RndSeqManagerAdapter extends RecyclerView.Adapter<RndSeqManagerAdapter.RndSeqManagerTrackViewHolder> implements Subilizer {
    private final LLPPDRUMS llppdrums;
    private final RndSeqManagerPopup rndSeqManagerPopup;
    private final RndSeqManager rndSeqManager;

    //private CSpinnerButton cSpinnerButton;

    public RndSeqManagerAdapter(LLPPDRUMS llppdrums, RndSeqManagerPopup rndSeqManagerPopup, RndSeqManager rndSeqManager) {
        this.llppdrums = llppdrums;
        this.rndSeqManagerPopup = rndSeqManagerPopup;
        this.rndSeqManager = rndSeqManager;
    }

    // Create new viewHolder
    @Override
    public RndSeqManagerTrackViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View trackView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_popup_rnd_sequence_manager_track, viewGroup, false);
        RndSeqManagerTrackViewHolder rndSeqManagerTrackViewHolder = new RndSeqManagerTrackViewHolder(trackView);

        //create the soundBtn
        CSpinnerButton soundSpinnerButton = new CSpinnerButton(llppdrums);
        //cSpinnerButton.setWidth((int) llppdrums.getResources().getDimension(R.dimen.rndSeqManTrackSpinnerOscWidth));
        //FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams((int) llppdrums.getResources().getDimension(R.dimen.rndSeqManTrackSpinnerOscWidth), ViewGroup.LayoutParams.WRAP_CONTENT);
        //cSpinnerButton.setLayoutParams(flp);

        FrameLayout soundSpinnerContainer = rndSeqManagerTrackViewHolder.soundSpinnerLayout;
        soundSpinnerContainer.addView(soundSpinnerButton);

        //and the subsBtn
        CSpinnerButton subsSpinnerButton = new CSpinnerButton(llppdrums);
        FrameLayout subsSpinnerContainer = rndSeqManagerTrackViewHolder.subsSpinnerLayout;
        subsSpinnerContainer.addView(subsSpinnerButton);

        //create the steps
        for(int step = 0; step < rndSeqManager.getTracks().get(0).getSteps().size(); step++){
            createStep(rndSeqManagerTrackViewHolder.stepsLayout);
        }

        return rndSeqManagerTrackViewHolder;
    }

    public void createStep(final LinearLayout stepsLayout){
        final ImageView stepIV = new ImageView(llppdrums);
        int stepWidth = (int) llppdrums.getResources().getDimension(R.dimen.sequencerStepWidth);
        int stepHeight = (int) llppdrums.getResources().getDimension(R.dimen.sequencerTrackStepHeight);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(stepWidth, stepHeight);
        stepIV.setLayoutParams(llp);
        stepsLayout.addView(stepIV);

        //left padding only
        stepIV.setPadding(0, 0, (int) llppdrums.getResources().getDimension(R.dimen.sequencerTrackStepPadding), 0);
    }

    public void removeStep(final LinearLayout stepsLayout){
        stepsLayout.removeViewAt(stepsLayout.getChildCount() - 1);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RndSeqManagerTrackViewHolder rndSeqManagerTrackViewHolder, final int position) {
        RndSeqPresetTrack track = rndSeqManager.getTracks().get(rndSeqManagerTrackViewHolder.getAdapterPosition());

        rndSeqManagerTrackViewHolder.bgView.setBackground(track.getGradientDrawable());

        //setup the osc-spinner
        CSpinnerButton sounsSpinnerButton = (CSpinnerButton) rndSeqManagerTrackViewHolder.soundSpinnerLayout.getChildAt(0);
        sounsSpinnerButton.setSelection(rndSeqManager.getTracks().get(rndSeqManagerTrackViewHolder.getAdapterPosition()).getPresetCategory());
        sounsSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RndSeqManagerOscListPopup(llppdrums, rndSeqManagerPopup, rndSeqManager, rndSeqManagerTrackViewHolder.getAdapterPosition(), sounsSpinnerButton);
            }
        });

        //setup the subs-spinner
        CSpinnerButton subsSpinnerButton = (CSpinnerButton) rndSeqManagerTrackViewHolder.subsSpinnerLayout.getChildAt(0);
        subsSpinnerButton.setSelection(Integer.toString(rndSeqManager.getTracks().get(rndSeqManagerTrackViewHolder.getAdapterPosition()).getnOfSubs()));
        subsSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubsPopup(llppdrums, rndSeqManagerTrackViewHolder.getAdapterPosition(), subsSpinnerButton, RndSeqManagerAdapter.this);
            }
        });

        //and the steps
        for(int step = 0; step < rndSeqManagerTrackViewHolder.stepsLayout.getChildCount(); step++) {

            while(rndSeqManagerTrackViewHolder.stepsLayout.getChildCount() < rndSeqManager.getTracks().get(0).getSteps().size()){
                createStep(rndSeqManagerTrackViewHolder.stepsLayout);
            }
            while(rndSeqManagerTrackViewHolder.stepsLayout.getChildCount() > rndSeqManager.getTracks().get(0).getSteps().size()){
                removeStep(rndSeqManagerTrackViewHolder.stepsLayout);
            }

            final ImageView stepIV = ((ImageView) rndSeqManagerTrackViewHolder.stepsLayout.getChildAt(step));
            final int stepNo = step;

            stepIV.setImageDrawable(new SeqRndManagerPercSubsDrawable(llppdrums, rndSeqManager.getTracks().get(position).getSteps().get(step)));
            stepIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RndSeqPresetTrack.Step s = rndSeqManager.getTracks().get(rndSeqManagerTrackViewHolder.getAdapterPosition()).getSteps().get(stepNo);

                    if(s.getNofSubs() == 1){
                        new RndSeqManagerPercPopup(llppdrums, rndSeqManagerPopup, stepIV, s, 0);
                    }
                    else{
                        new RndSeqManagerPercSubPopup(llppdrums, rndSeqManagerPopup, s, stepIV);
                    }

                    //new RndSeqPercPopup(llppdrums, rndSeqManagerPopup, stepIV, stepNo, rndSeqManager.getTracks().get(rndSeqManagerTrackViewHolder.getAdapterPosition()).getSteps().get(stepNo));

                }
            });
        }

        //and the fx-cb
        rndSeqManagerTrackViewHolder.fxCB.setChecked(rndSeqManager.getTracks().get(rndSeqManagerTrackViewHolder.getAdapterPosition()).getRandomizeFx());
        rndSeqManagerTrackViewHolder.fxCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rndSeqManager.getTracks().get(rndSeqManagerTrackViewHolder.getAdapterPosition()).setRandomizeFx(rndSeqManagerTrackViewHolder.fxCB.isChecked());
                rndSeqManagerPopup.addModifiedMarker();
            }
        });

        //and the removeBtn
        rndSeqManagerTrackViewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rndSeqManagerPopup.removeTrack(rndSeqManagerTrackViewHolder.getAdapterPosition());
                rndSeqManagerPopup.addModifiedMarker();
            }
        });
    }

    @Override
    public void setNOfSubs(int trackNo, int nOfSubs){
        rndSeqManager.getTracks().get(trackNo).setNOfSubs(nOfSubs);
        notifyDataSetChanged();
    }

    @Override
    public int getNOfSubs(){
        return 0; //never used
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rndSeqManagerPopup.getRndSeqManager().getTracks().size();
    }

    public void moveTrack(int from, int to){
        rndSeqManagerPopup.moveTrack(from, to);
    }

    /** VIEWHOLDER **/
    public static class RndSeqManagerTrackViewHolder extends RecyclerView.ViewHolder {
        final View bgView;
        final FrameLayout soundSpinnerLayout, subsSpinnerLayout;
        final LinearLayout stepsLayout;
        final CheckBox fxCB;
        final Button removeBtn;

        public RndSeqManagerTrackViewHolder(View v) {
            super(v);
            bgView = v;
            soundSpinnerLayout = v.findViewById(R.id.rndSeqSoundSpinnerLayout);
            subsSpinnerLayout = v.findViewById(R.id.rndSeqSubsSpinnerLayout);
            stepsLayout = v.findViewById(R.id.rndSeqStepsLayout);
            removeBtn = v.findViewById(R.id.rndSeqRemoveTrackBtn);
            fxCB = v.findViewById(R.id.rndSeqFxCB);
        }
    }
}