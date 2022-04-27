package com.kiefer.machine.sequencerUI;

import android.graphics.drawable.Drawable;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButtonTrack;
import com.kiefer.graphics.customViews.ObservableHorizontalScrollView;

/**
 * Provide views to RecyclerView with data from  a dataSet.
 */
public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.TrackViewHolder>{
    private final LLPPDRUMS llppdrums;
    private final SequencerUI sequencerUI;

    //used by the step-listener
    private float startX, startY;
    private float minDrag = 10;

    public TracksAdapter(LLPPDRUMS llppdrums, SequencerUI sequencerUI) {
        this.llppdrums = llppdrums;
        this.sequencerUI = sequencerUI;
    }

    // Create new viewHolder
    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate the layout and create a viewHolder
        View trackView;
        trackView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_sequencer_track, viewGroup, false);
        TrackViewHolder trackViewHolder = new TrackViewHolder(trackView);

        //create the steps
        for(int step = 0; step < llppdrums.getDrumMachine().getSelectedSequenceNOfSteps(); step++){
            createStep(trackViewHolder.stepsLayout);
        }

        //set a scrollViewListener to be able to update the other horizontal scrollViews (all tracks have one)
        trackViewHolder.stepsScrollView.setScrollViewListener(sequencerUI);

        return trackViewHolder;
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
        //new Thread(new Runnable() {
        //public void run() {
        stepsLayout.removeViewAt(stepsLayout.getChildCount() - 1);
        //}
        //}).start();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final TrackViewHolder trackViewHolder, final int position) {

        //Log.e("TracksAdapter", "onBindViewHolder(), trackNo: "+position);

        //update the scrollViews position
        trackViewHolder.stepsScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                trackViewHolder.stepsScrollView.setScrollX(sequencerUI.getScrollX());
            }
        }, llppdrums.getResources().getInteger(R.integer.recyclerViewUpdateDelay));

        trackViewHolder.singleBtn.setSelection(llppdrums.getDrumMachine().getSelectedSequence().getTracks().get(trackViewHolder.getAdapterPosition()).getName());

        trackViewHolder.singleBtn.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().expandTrackBtns(trackViewHolder.getAdapterPosition(), trackViewHolder.singleBtn);
            }
        });

        //set the bgColor
        trackViewHolder.bgView.setBackgroundColor(llppdrums.getDrumMachine().getTrackColor(trackViewHolder.getAdapterPosition()));

        /** FÅR LITE UI-THREAD-PROBLEM MED DEN HÄR TRÅDEN MEN JOBBA MED DEN OM DET LAGGAR MASSA **/
        //new Thread(new Runnable() { //thread for creating the Drawable
        //public void run() {
        for(int step = 0; step < trackViewHolder.stepsLayout.getChildCount(); step++) {

            /** HACK **/
            /*
            Since recyclerView.getChildCount() (which should return the number of tracks) isn't updated
            yet (it returns the amount of children in the tab we're leaving, not the one we just selected)
            in Sequencer.updateNOfSteps() we get errors with the amount of steps in the tracks that
            aren't counted. This is a buggy fix for that. It's very weird without this.
             */
                    /*
                    llppdrums.runOnUiThread(new Runnable() {
                        public void run() {
                            while(trackViewHolder.stepsLayout.getChildCount() < llppdrums.getDrumMachine().getSelectedSequenceNOfSteps()){
                                createStep(trackViewHolder.stepsLayout);
                            }
                            while(trackViewHolder.stepsLayout.getChildCount() > llppdrums.getDrumMachine().getSelectedSequenceNOfSteps()){
                                removeStep(trackViewHolder.stepsLayout);
                            }
                        }
                    });

                     */
            while(trackViewHolder.stepsLayout.getChildCount() < llppdrums.getDrumMachine().getSelectedSequenceNOfSteps()){
                createStep(trackViewHolder.stepsLayout);
            }
            while(trackViewHolder.stepsLayout.getChildCount() > llppdrums.getDrumMachine().getSelectedSequenceNOfSteps()){
                removeStep(trackViewHolder.stepsLayout);
            }

            final ImageView stepIV = ((ImageView) trackViewHolder.stepsLayout.getChildAt(step));

            final int stepNo = step;

            //Log.e("TracksAdapter", "---------------------------------");
            //Log.e("TracksAdapter", "onBindViewHolder(), trackNo: "+trackViewHolder.getAdapterPosition());
            //Log.e("TracksAdapter", "------------------------------------");
            final Drawable drawable = llppdrums.getDrumMachine().getSelectedSequenceDrumDrawable(trackViewHolder.getAdapterPosition(), stepNo);
            //stepIV.post(new Runnable() { //modify the View in the UI thread
                //public void run() {
                    stepIV.setImageDrawable(drawable);
                //}
            //});

            stepIV.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    float x, y;

                    int action = MotionEventCompat.getActionMasked(motionEvent);

                    switch (action) {
                        case (MotionEvent.ACTION_DOWN):
                            startX = motionEvent.getX();
                            startY = motionEvent.getY();
                            x = motionEvent.getX();
                            y = motionEvent.getY();
                            sequencerUI.getCallback().onStepTouch(trackViewHolder.getAdapterPosition(), stepNo, stepIV, startX, startY, x, y, action);
                            return true;
                        case (MotionEvent.ACTION_MOVE):
                            x = motionEvent.getX();
                            y = motionEvent.getY();
                            sequencerUI.getCallback().onStepTouch(trackViewHolder.getAdapterPosition(), stepNo, stepIV, startX, startY, x, y, action);
                            return true;
                        case (MotionEvent.ACTION_UP):
                            x = motionEvent.getX();
                            y = motionEvent.getY();
                            sequencerUI.getCallback().onStepTouch(trackViewHolder.getAdapterPosition(), stepNo, stepIV, startX, startY, x, y, action);
                            //Log.e("asd", ""+stepNo);
                            return true;
                        case (MotionEvent.ACTION_CANCEL):
                            return true;
                        default:
                            return false;
                    }
                }
            });

        }

        trackViewHolder.stepsScrollView.setScrollX(sequencerUI.getScrollX());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return llppdrums.getDrumMachine().getSelectedSequenceNOfTracks();
    }

    public void moveTrack(int from, int to){
        llppdrums.getDrumMachine().getSelectedSequence().moveTrack(from, to);
    }

    /** VIEWHOLDER **/
    public class TrackViewHolder extends RecyclerView.ViewHolder {
        final View bgView;
        ObservableHorizontalScrollView stepsScrollView;
        LinearLayout stepsLayout;

        //not expanded
        CSpinnerButtonTrack singleBtn;

        public TrackViewHolder(View v) {
            super(v);
            bgView = v;
            stepsLayout = v.findViewById(R.id.sequencerTrackStepsLayout);
            stepsScrollView = v.findViewById(R.id.sequencerTrackStepsScrollView);
            singleBtn = new CSpinnerButtonTrack(llppdrums);

            FrameLayout spinnerLayout = v.findViewById(R.id.spinnerLayout);
            spinnerLayout.addView(singleBtn);
        }
    }
}