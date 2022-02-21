package com.kiefer.machine.sequencerUI;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButtonRndMode;
import com.kiefer.graphics.customViews.ObservableHorizontalScrollView;
import com.kiefer.info.sequence.AutoRandomInfo;
import com.kiefer.info.sequence.SequenceInfo;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.AutoRandom;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.seqModules.autoRnd.AutoRndModePopup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.ui.Counter;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Objects;

public class SequencerUI implements ObservableHorizontalScrollView.ObservableHorizontalScrollViewListener {
    //public static final boolean EXPANDED = false;

    private final LLPPDRUMS llppdrums;

    //listener
    protected OnSequencerClickedListener callback;

    //steps
    private Button removeStepBtn; //global to be able to hide it at 1 step

    //rootView
    private RelativeLayout rootView;

    //label
    private TextView textView;

    //rnd mode
    private CSpinnerButtonRndMode rndModeSpinnerBtn;

    //counter
    //private LinearLayout counterLayout;
    private boolean firstRun = true; //used to make the counter look better, see handleSequencerPositionChange
    //CounterDotDrawable counterDotDrawable;

    //tracks recyclerView
    private RecyclerView recyclerView;
    private TracksAdapter adapter;

    //scrollViews
    private ObservableHorizontalScrollView counterScroll;

    //lockable UI (for disabling during playback)
    private ArrayList<View> lockableUI;

    public SequencerUI(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
        setupLayout();
    }

    private void setupLayout(){
        lockableUI = new ArrayList<>();

        //inflate the rootView
        rootView = (RelativeLayout) llppdrums.getLayoutInflater().inflate(R.layout.layout_sequencer, null);

        //set up the label
        textView = rootView.findViewById(R.id.sequencerTV);
        textView.setTextColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtColor));

        //remove step
        removeStepBtn = rootView.findViewById(R.id.sequencerRemoveStepBtn);
        removeStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().getSelectedSequence().removeStep();

                if(llppdrums.getDrumMachine().getSelectedSequence().getNOfSteps() <= 1){
                    setRemoveStepBtnEnabled(false);
                }
            }
        });
        lockableUI.add(removeStepBtn);

        //add step
        Button addStepBtn = rootView.findViewById(R.id.sequencerAddStepBtn);
        addStepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().getSelectedSequence().addStep();

                if(llppdrums.getDrumMachine().getSelectedSequence().getNOfSteps() > 1){
                    setRemoveStepBtnEnabled(true);
                }
            }
        });
        lockableUI.add(addStepBtn);

        //rndMode-spinner
        rndModeSpinnerBtn = new CSpinnerButtonRndMode(llppdrums);
        rndModeSpinnerBtn.setTextSize(8);
        rndModeSpinnerBtn.getButton().setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AutoRndModePopup(llppdrums, rndModeSpinnerBtn);
            }
        });
        FrameLayout spinnerContainer = rootView.findViewById(R.id.seqRndModeSpinnerContainer);
        spinnerContainer.addView(rndModeSpinnerBtn);
        setAutoRndModeVisibility(false);

        //set up the counter (the tracks are created in the adapter)
        //counterDotDrawable = new CounterDotDrawable(llppdrums);
        createCounter(llppdrums.getDrumMachine().getSelectedSequenceNOfSteps());

        //set up the recyclerView
        recyclerView = rootView.findViewById(R.id.sequencerTracksRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        LinearLayoutManager layoutManager = new LinearLayoutManager(llppdrums);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //create an adapter
        adapter = new TracksAdapter(llppdrums, this);

        //create and attach the ItemTouchHelper
        ItemTouchHelper.Callback callback = new TrackTouchHelper(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        //add the adapter to the recycler view
        recyclerView.setAdapter(adapter);

        //setup the add track button
        Button addTrackBtn = rootView.findViewById(R.id.sequencerAddTrackBtn);
        addTrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().getSelectedSequence().addTrack();
            }
        });
        lockableUI.add(addTrackBtn);

        //set up the helpBtn
        ImageView helpBtn = rootView.findViewById(R.id.seqInfoBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule().getSelectedMode() instanceof AutoRandom){
                    new InfoPopup(llppdrums, AutoRandomInfo.key);
                }
                else {
                    new InfoPopup(llppdrums, SequenceInfo.key);
                }
            }
        });
    }

    public void setStepDrawable(Drawable drawable, int track, int step){
        //Log.e("SequencerUI", "setStepDrawable");
        TracksAdapter.TrackViewHolder holder = (TracksAdapter.TrackViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(track));
        final ImageView stepIV = ((ImageView) holder.stepsLayout.getChildAt(step));
        stepIV.setImageDrawable(drawable);
    }

    //use this to be able to run a thread and update the IV in the UI thread (instead of using setStepDrawable())
    public ImageView getStepIV(int track, int step){
        if(recyclerView.getChildAt(track) != null) {
            try {
                TracksAdapter.TrackViewHolder holder = (TracksAdapter.TrackViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(track)); //Attempt to invoke virtual method 'android.view.ViewParent android.view.View.getParent()' on a null object reference
                return ((ImageView) holder.stepsLayout.getChildAt(step));
            }
            catch (Exception e){
                Log.e("SequencerUI.getStepIV()", Objects.requireNonNull(e.getMessage()));
                return null;
            }
        }
        return null;
    }

    /** COUNTER **/
    private Counter counter;
    private void createCounter(int nOfSteps){

        //get the scrollView to put the counter in and set padding to make it match the steps only of the tracks
        counterScroll = rootView.findViewById(R.id.sequencerCounter);
        int paddingLeft, paddingRight;
        paddingLeft = (int) llppdrums.getResources().getDimension(R.dimen.tabsBorderSmall) * 1 + (int) llppdrums.getResources().getDimension(R.dimen.sequencerSingleBtnWidth);
        paddingRight = 0;
        counterScroll.setPadding(paddingLeft, 0, paddingRight, 0);

        //set this as listener to the scrolls
        counterScroll.setScrollViewListener(this);

        //create the layout for the counter
        counter = new Counter(llppdrums, nOfSteps);

        //add it to the scrollView
        counterScroll.addView(counter.getLayout());
    }

    private void setActiveCounterColor(final RelativeLayout bg, TextView tv){
        int bgColor = ContextCompat.getColor(llppdrums, R.color.counterActiveBgColor);
        bg.setBackgroundColor(bgColor);
        tv.setTextColor(ColorUtils.getContrastColor(bgColor));
    }

    private void setInactiveCounterColor(final RelativeLayout bg, final TextView tv){
        final int bgColor = ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor);
        bg.setBackgroundColor(bgColor);
        tv.setTextColor(ColorUtils.getContrastColor(bgColor));
    }

    private void updateCounter(int step, boolean playing){
        resetCounter();
        if(playing) {
            counter.activateStep(step);
        }
    }

    public void resetCounter(){
        counter.reset();
    }

    /** ADD/REMOVE TRACKS/STEPS **/

    //called in DrumMachine().addTrack() which is called by the button
    public void addTrack(){
        adapter.notifyDataSetChanged();
        //adapter.notifyItemInserted();
    }

    public void removeTrack(int trackNo){
        //adapter.notifyItemRemoved(trackNo);
        adapter.notifyDataSetChanged(); //notifyItemRemoved looks terrible
    }

    //called in DrumMachine().addStep() which is called by the button
    public void addStep(){

        //update the recyclerView
        adapter.notifyDataSetChanged();

        //add a step in every track in the recyclerView
        for (int track = 0; track < recyclerView.getChildCount(); ++track) {
            final TracksAdapter.TrackViewHolder holder = (TracksAdapter.TrackViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(track));
            adapter.createStep(holder.stepsLayout);

            //update the scrollViews position
            holder.stepsScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.stepsScrollView.fullScroll(View.FOCUS_RIGHT);
                }
            }, llppdrums.getResources().getInteger(R.integer.recyclerViewUpdateDelay));
        }

        //update the counter
        //addCounterStep();
        counter.addStep();
    }

    //called in DrumMachine().addStep() which is called by the button
    public void addStep(int trackNo){
        TracksAdapter.TrackViewHolder holder = (TracksAdapter.TrackViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(trackNo));
        adapter.createStep(holder.stepsLayout);
        adapter.notifyDataSetChanged();

        //update the counter
        //addCounterStep();
        counter.addStep();
    }

    //dame as above
    public void removeStep(){
        for (int track = 0; track < recyclerView.getChildCount(); ++track) {
            TracksAdapter.TrackViewHolder holder = (TracksAdapter.TrackViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(track));
            adapter.removeStep(holder.stepsLayout);
        }
        adapter.notifyDataSetChanged();
        //removeCounterStep();
        counter.removeStep();
    }

    public void setTrackName(int trackNo, String name){
        if (recyclerView.getChildAt(trackNo) != null) {
            TracksAdapter.TrackViewHolder holder = (TracksAdapter.TrackViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(trackNo));
            holder.singleBtn.setSelection(name);
        }
    }

    public void setTrackColor(int trackNo, int trackColor){
        TracksAdapter.TrackViewHolder holder = (TracksAdapter.TrackViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(trackNo));
        holder.bgView.setBackgroundColor(trackColor);
    }

    public void updateNOfSteps(int newNoOfSteps){
        if(recyclerView.getChildAt(0) != null) {

            TracksAdapter.TrackViewHolder holder = (TracksAdapter.TrackViewHolder)recyclerView.getChildViewHolder(recyclerView.getChildAt(0));
            int currentSeqSteps = holder.stepsLayout.getChildCount();
            //int currentSeqSteps = holder.stepsLayout.getItemCount();

            while (newNoOfSteps < currentSeqSteps) {
                removeStep();
                currentSeqSteps--;
            }
            while (newNoOfSteps > currentSeqSteps) {
                addStep();
                currentSeqSteps++;
            }
        }
    }

    //change the drawables in the steps when changing sequence or mode
    public void setSequencerDrawables(final ArrayList<DrumTrack> drumTracks){
        new Thread(new Runnable() { //thread for creating the Drawable
            public void run() {
                for(int track = 0; track<drumTracks.size(); track++){
                    for(int step = 0; step< drumTracks.get(track).getNOfSteps(); step++){
                        final Step drum = drumTracks.get(track).getSteps().get(step);
                        if(recyclerView.findViewHolderForAdapterPosition(track) != null) {

                            final ImageView iv = ((ImageView) ((TracksAdapter.TrackViewHolder) recyclerView.findViewHolderForAdapterPosition(track)).stepsLayout.getChildAt(step));
                            final Drawable drawable = llppdrums.getDrumMachine().getDrumDrawable(drum);
                            iv.post(new Runnable() { //modify the View in the UI thread
                                public void run() {
                                    iv.setImageDrawable(drawable);
                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }

    public void notifyDataSetChange(){
        adapter.notifyDataSetChanged();
    }

    //doesn't look very good
    public void notifyItemChange(int track){
        adapter.notifyItemChanged(track);
    }

    /** LOCK UI **/
    //disable some UI that shouldn't be usable during playback
    public void lockUI(){
        for(View v : lockableUI){
            //v.setAlpha(.7f);
            v.setEnabled(false);
        }
    }

    //enable them again
    public void unlockUI(){
        for(View v : lockableUI){

            final View finalView = v;

            finalView.post(new Runnable() { //modify the View in the UI thread
                public void run() {

                    //don't unlock removeStepBtn if steps == 1
                    if(!(finalView == removeStepBtn && llppdrums.getDrumMachine().getSelectedSequence().getNOfSteps() <= 1)) {
                        finalView.setAlpha(1f);
                        finalView.setEnabled(true);
                    }
                }
            });
        }
    }

    /** SCROLLING **/
// ObservableHorizontalScrollView.ObservableHorizontalScrollViewListener
    @Override
    public void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int oldx, int oldy){

        counterScroll.scrollTo(x, counterScroll.getScrollY());

        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final TracksAdapter.TrackViewHolder holder = (TracksAdapter.TrackViewHolder)recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            holder.stepsScrollView.scrollTo(x, y);
        }
    }

    /** COUNTROLS **/
//called when playback is stopped
    public void stop(){
        firstRun = true;
        resetCounter();
    }

    /** GET **/
    public int getScrollX(){
        return counterScroll.getScrollX();
    }

    public RelativeLayout getLayout() {
        return rootView;
    }

    //testing
    public int getRecyclerViewChildCount(){
        return recyclerView.getChildCount();
    }

    /** SET **/
    public void setRemoveStepBtnEnabled(boolean enabled){
        //Log.e("Sequencer.setRemoveS...", "enable: "+enabled);
        if(!(llppdrums.getDrumMachine().getSelectedSequence() == llppdrums.getDrumMachine().getPlayingSequence() && llppdrums.getEngineFacade().isPlaying())) {
            removeStepBtn.setEnabled(enabled);
        }
    }
    public void setAutoRndModeLabel(String label){
        rndModeSpinnerBtn.setSelection(label);
    }

    public void setAutoRndModeVisibility(boolean visible){
        if(visible){
            rndModeSpinnerBtn.setVisibility(View.VISIBLE);
        }
        else{
            rndModeSpinnerBtn.setVisibility(View.INVISIBLE);
        }
    }
    public void setLabelText(String text){
        textView.setText(text);
    }

    /** LISTENER INTERFACE **/
    public void setSequencerClickedListener(OnSequencerClickedListener callback) {
        this.callback = callback;
    }

    //implemented by DrumMachine, and it sets itself as callback in setTabSelectedListener()
    public interface OnSequencerClickedListener {
        void onStepTouch(int track, int step, ImageView stepIV, float startX, float startY, float endX, float endY, int action); //for drags/clicks on a step
        void onTrackItemClicked(int track, View view, int btn); //for clicks on something else, fx, synth etc..
    }

    public OnSequencerClickedListener getCallback(){
        return callback;
    }

    /** SEQUENCER UPDATES **/
    public void handleSequencerPositionChange(int sequencerPosition){

        /** HAR MED STEPS OCH TEMPO ATT GÖRA, MÅSTE FIXA FLERA OLIKA **/

        //only update the counter if the selected sequence is playing

        //update step
        /** NEEDS TUNING **/
        /*
        int counterStep;
        if(llppdrums.getDrumMachine().getSelectedSequence().getTempo() >= 90) {

            //for some reason it looks better with sequencerPosition - 1 (except for the first time)
            if (sequencerPosition == 0) {
                counterStep = llppdrums.getDrumMachine().getPlayingSequenceNOfSteps() - 1;
            } else {
                counterStep = sequencerPosition - 1;
            }
        }
        else{
            counterStep = sequencerPosition;
        }

         */

        //update the counter
        //playing
        if(llppdrums.getDrumMachine().getPlayingSequence() == llppdrums.getDrumMachine().getSelectedSequence()) {
            if (!firstRun) {
                updateCounter(sequencerPosition, true);
            }
            firstRun = false;
        }
        //not playing
        else{
            if (!firstRun) {
                updateCounter(sequencerPosition, false);
            }
            firstRun = false;
        }
    }
}
