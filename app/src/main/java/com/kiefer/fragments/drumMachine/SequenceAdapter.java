package com.kiefer.fragments.drumMachine;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.ui.tabs.interfaces.Tab;

import java.util.ArrayList;

public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.SequenceTabViewHolder> {
    private final LLPPDRUMS llppdrums;
    //private final ArrayList<Tab> tabs;

    //private CSpinnerButton cSpinnerButton;

    public SequenceAdapter(LLPPDRUMS llppdrums, ArrayList<Tab> tabs) {
        this.llppdrums = llppdrums;
        //this.tabs = tabs;
    }

    // Create new viewHolder
    @Override
    public SequenceAdapter.SequenceTabViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //View trackView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tab_single_vertical, viewGroup, false);
        SequenceAdapter.SequenceTabViewHolder sequenceTabViewHolder = new SequenceAdapter.SequenceTabViewHolder(new FrameLayout(llppdrums));
        return sequenceTabViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final SequenceAdapter.SequenceTabViewHolder sequenceTabViewHolder, final int position) {
        //Tab tab = tabs.get(sequenceTabViewHolder.getAdapterPosition());

        FrameLayout bg = sequenceTabViewHolder.bgView;
        //bg.addView(tab.getBackground());

        /*
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().onTabClicked(tab);
            }
        });

         */
        /*
        RndSeqPresetTrack track = rndSeqManager.getTracks().get(sequenceTabViewHolder.getAdapterPosition());

        sequenceTabViewHolder.bgView.setBackground(track.getGradientDrawable());

        //setup the osc-spinner
        CSpinnerButton sounsSpinnerButton = (CSpinnerButton) sequenceTabViewHolder.soundSpinnerLayout.getChildAt(0);
        sounsSpinnerButton.setSelection(rndSeqManager.getTracks().get(sequenceTabViewHolder.getAdapterPosition()).getPresetCategory());
        sounsSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RndSeqManagerOscListPopup(llppdrums, rndSeqManagerPopup, rndSeqManager, sequenceTabViewHolder.getAdapterPosition(), sounsSpinnerButton);
            }
        });

        //setup the subs-spinner
        CSpinnerButton subsSpinnerButton = (CSpinnerButton) sequenceTabViewHolder.subsSpinnerLayout.getChildAt(0);
        subsSpinnerButton.setSelection(Integer.toString(rndSeqManager.getTracks().get(sequenceTabViewHolder.getAdapterPosition()).getnOfSubs()));
        subsSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SubsPopup(llppdrums, sequenceTabViewHolder.getAdapterPosition(), subsSpinnerButton, RndSeqManagerAdapter.this);
            }
        });

        //and the steps
        for(int step = 0; step < sequenceTabViewHolder.stepsLayout.getChildCount(); step++) {

            while(sequenceTabViewHolder.stepsLayout.getChildCount() < rndSeqManager.getTracks().get(0).getSteps().size()){
                createStep(sequenceTabViewHolder.stepsLayout);
            }
            while(sequenceTabViewHolder.stepsLayout.getChildCount() > rndSeqManager.getTracks().get(0).getSteps().size()){
                removeStep(sequenceTabViewHolder.stepsLayout);
            }

            final ImageView stepIV = ((ImageView) sequenceTabViewHolder.stepsLayout.getChildAt(step));
            final int stepNo = step;

            stepIV.setImageDrawable(new SeqRndManagerPercSubsDrawable(llppdrums, rndSeqManager.getTracks().get(position).getSteps().get(step)));
            stepIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RndSeqPresetTrack.Step s = rndSeqManager.getTracks().get(sequenceTabViewHolder.getAdapterPosition()).getSteps().get(stepNo);

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
        sequenceTabViewHolder.fxCB.setChecked(rndSeqManager.getTracks().get(sequenceTabViewHolder.getAdapterPosition()).getRandomizeFx());
        sequenceTabViewHolder.fxCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rndSeqManager.getTracks().get(sequenceTabViewHolder.getAdapterPosition()).setRandomizeFx(sequenceTabViewHolder.fxCB.isChecked());
                rndSeqManagerPopup.addModifiedMarker();
            }
        });

        //and the removeBtn
        sequenceTabViewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rndSeqManagerPopup.removeTrack(sequenceTabViewHolder.getAdapterPosition());
                rndSeqManagerPopup.addModifiedMarker();
            }
        });

         */
    }
/*
    @Override
    public void setNOfSubs(int trackNo, int nOfSubs){
        rndSeqManager.getTracks().get(trackNo).setNOfSubs(nOfSubs);
        notifyDataSetChanged();
        /*
        this.nOfSubs = nOfSubs;
        for(Step d : steps){
            d.setNOfSubs(getNOfSteps(), nOfSubs);
        }
        positionDrums();

        if(llppdrums.getDrumMachine().getSelectedSequence() == drumSequence) {
            llppdrums.getSequencer().notifyDataSetChange();
        }

        //update autoStepValues
        while(autoStepValues.size() < nOfSubs){
            autoStepValues.add(false);
        }
        while(autoStepValues.size() > nOfSubs){
            autoStepValues.remove(autoStepValues.size()-1);
        }

    }
         */


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return llppdrums.getDrumMachine().getNOfSequences();
    }

    /** VIEWHOLDER **/
    public static class SequenceTabViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout bgView;

        public SequenceTabViewHolder(FrameLayout v) {
            super(v);
            bgView = v;
        }
    }
}