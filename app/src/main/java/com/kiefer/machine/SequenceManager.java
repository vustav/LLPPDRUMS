package com.kiefer.machine;

import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import androidx.core.content.ContextCompat;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSeekBar;
import com.kiefer.files.keepers.SequenceManagerKeeper;
import com.kiefer.popups.sequenceManager.SequenceManagerPopup;
import com.kiefer.ui.SequenceCounter;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Random;

public class SequenceManager {
    private final LLPPDRUMS llppdrums;
    private SequenceCounter counter;
    private final CSeekBar progressSlider;

    //private boolean changeSeqRunning = false; //used to avoid spamming certain functions
    private boolean changesHalted = false; //true when the button on controller is momentary and pressed to avoid changes
    private boolean restartAtStop = false; //if the sequence manager should restart or continue where it is after a stop

    private final View view;

    private boolean progress, randomizeProgress, queue;

    private int activeSequenceBoxIndex = 0, queuedSeqBoxIndex = -1;

    private final TextView progressTV, queueTV;
    private final Button editBtn;

    private final GradientDrawable popupGradient, randomPopupGradient;
    public final int popupGradientColor1;

    private int nOfActiveBoxes;
    Random random = new Random();

    public SequenceManager(final LLPPDRUMS llppdrums, SequenceManagerKeeper keeper){
        this.llppdrums = llppdrums;

        popupGradientColor1 = ColorUtils.getRandomColor();
        popupGradient = ColorUtils.getRandomGradientDrawable(popupGradientColor1, ColorUtils.getRandomColor());
        randomPopupGradient = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());

        //inflate the View
        view = llppdrums.getLayoutInflater().inflate(R.layout.layout_sequence_manager, null);

        //set up the progressSlider
        progressSlider = new CSeekBar(llppdrums, CSeekBar.HORIZONTAL_LEFT_RIGHT);
        progressSlider.setThumb(false);
        progressSlider.setMargin(0);
        progressSlider.setColors(ContextCompat.getColor(llppdrums, R.color.popupBarColor), ContextCompat.getColor(llppdrums, R.color.popupBarBg));

        //empty listener to prevent clicks
        progressSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        FrameLayout progressSliderLayout = view.findViewById(R.id.sequenceManagerProgressSliderLayout);
        progressSliderLayout.addView(progressSlider);

        //set up the info
        progressTV = view.findViewById(R.id.sequenceManagerProgressInfoTV);
        queueTV = view.findViewById(R.id.sequenceManagerQueueInfoTV);

        editBtn = view.findViewById(R.id.sequenceManagerEditBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SequenceManagerPopup(llppdrums, SequenceManager.this);
            }
        });

        //setup the counter
        setupSequences(keeper);

        //add the counter to the layout
        FrameLayout counterLayout = view.findViewById(R.id.sequenceManagerCounterLayout);
        counterLayout.addView(counter.getLayout());
    }

    private void setupSequences(SequenceManagerKeeper keeper){

        counter = new SequenceCounter(llppdrums, llppdrums.getResources().getInteger(R.integer.nOfSeqBoxes), (int) llppdrums.getResources().getDimension(R.dimen.controllerBoxWidth), (int) llppdrums.getResources().getDimension(R.dimen.controllerBoxHeight), (int) llppdrums.getResources().getDimension(R.dimen.controllerBoxTxt));
        counter.setSize((int) llppdrums.getResources().getDimension(R.dimen.seqManagerBoxWidth), (int) llppdrums.getResources().getDimension(R.dimen.seqManagerBoxHeight));

        int txtCounter = 0; //used to set selected sequences if there's no keeper

        if(keeper != null){
            setProgress(keeper.progress);
            setRandomizeProgress(true);
            setQueue(keeper.queue);
            setRestartAtStop(keeper.restartAtStop);
            setNOfActiveBoxes(keeper.nOfActiveBoxes);
        }
        else{
            setProgress(false);
            setRandomizeProgress(false);
            setQueue(false);
            setRestartAtStop(false);
            setNOfActiveBoxes(8);
        }

        Random r = new Random();
        for(int step = 0; step < counter.getLayout().getChildCount(); step++){

            if(keeper != null){
                setStepSelection(step, (keeper.seqs.get(step)));
            }
            else{
                if(step == 0){
                    setStepSelection(step, Integer.toString(0));
                }
                else {
                    setStepSelection(step, Integer.toString(r.nextInt(llppdrums.getResources().getInteger(R.integer.nOfSequences))));
                }
            }

            final int finalStep = step;
            counter.setStepListener(step, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!queue || !llppdrums.getEngineFacade().isPlaying()) {
                        activateSequenceBox(finalStep);
                    }
                    else{
                        queueChange(finalStep);
                    }

                }
            });
        }
        counter.activateStep(activeSequenceBoxIndex);
    }

    public void activateSequenceBox(int step){
        counter.reset();
        counter.activateStep(step);
        changeSequence(Integer.parseInt(counter.getStepTxt(step)));

        activeSequenceBoxIndex = step;
    }

    private boolean changeAtNext = false;
    private void queueChange(int index){

        //reset if a box was already queued
        if(changeAtNext){
            counter.setStepColor(queuedSeqBoxIndex, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));
        }

        if(index != queuedSeqBoxIndex) {
            changeAtNext = true;
            queuedSeqBoxIndex = index;
            counter.setStepColor(index, ContextCompat.getColor(llppdrums, R.color.queuedSeqColor));
        }
        //remove the flag if the same box is pushed
        else{
            changeAtNext = false;
            queuedSeqBoxIndex = -1;
        }
    }

    public void updateSeq(){
        if(!changesHalted) {
            if (changeAtNext) {
                activateSequenceBox(queuedSeqBoxIndex);
                changeAtNext = false;
            }
            else if (progress) {
                if(!randomizeProgress) {
                    if (activeSequenceBoxIndex == getNOfActiveBoxes() - 1) {
                        activeSequenceBoxIndex = 0;
                    } else {
                        activeSequenceBoxIndex++;
                    }
                }
                else{
                    activeSequenceBoxIndex = random.nextInt(getNOfActiveBoxes());
                }
                activateSequenceBox(activeSequenceBoxIndex);
            }
        }
    }

    private void changeSequence(int sequence){
        llppdrums.getDrumMachine().changePlayingSequence(sequence);
    }

    /** CONTROLLER **/
    public void setSelectedControllerSeqBox(int index){
        //Log.e("SeqMan.setSelected...", "index: "+index);
        counter.setSelectedSeqBox(index);
    }

    /** ACTIVE BOXES **/
    public void setNOfActiveBoxes(int n){
        nOfActiveBoxes = n;

        //enable all boxes in the counter
        for(int i = 0; i < counter.getSize(); i++){
            counter.enableStep(i, true);
        }

        //disable the boxes over n
        for(int i = n; i < counter.getSize(); i++){
            counter.enableStep(i, false);
        }
    }

    /** GET **/

    public int getNOfActiveBoxes() {
        return nOfActiveBoxes;
    }

    public int getActiveSequenceBoxIndex() {
        return activeSequenceBoxIndex;
    }

    public GradientDrawable getPopupGradient() {
        return popupGradient;
    }

    public int getFirstGradientColor(){
        return popupGradientColor1;
    }

    public GradientDrawable getRandomPopupGradient() {
        return randomPopupGradient;
    }

    public View getView() {
        return view;
    }

    public boolean getProgress() {
        return progress;
    }

    public boolean getRandomizeProgress(){
        return randomizeProgress;
    }

    public boolean changeAtPress() {
        return queue;
    }

    public String getBoxSelection(int step){
        return counter.getStepTxt(step);
    }

    public SequenceCounter getCounter() {
        return counter;
    }

    /** SET **/
    public void setStepSelection(int step, String string){
        counter.setStepText(step, string);
    }

    public void setRandomizeProgress(boolean randomizeProgress){
        this.randomizeProgress = randomizeProgress;
    }

    public void setProgress(boolean on){
        progress = on;
        String onString = llppdrums.getResources().getString(R.string.seqManagerProgressLabel)+": ";
        if(on){
            onString += "ON";
        }
        else{
            onString += "OFF";
        }
        progressTV.setText(onString);
    }

    public void setQueue(boolean on){
        queue = on;
        String onString = llppdrums.getResources().getString(R.string.seqManagerQueueLabel)+": ";
        if(on){
            onString += "ON";
        }
        else{
            onString += "OFF";
        }
        queueTV.setText(onString);
    }

    public void setRestartAtStop(boolean restartAtStop) {
        this.restartAtStop = restartAtStop;
    }

    public void setChangesHalted(boolean changesHalted) {
        this.changesHalted = changesHalted;
    }

    /** KEEPING **/
    public SequenceManagerKeeper getKeeper(){
        SequenceManagerKeeper sequenceManagerKeeper = new SequenceManagerKeeper();

        sequenceManagerKeeper.queue = queue;
        sequenceManagerKeeper.progress = progress;
        sequenceManagerKeeper.randomizeProgress = randomizeProgress;
        sequenceManagerKeeper.nOfActiveBoxes = nOfActiveBoxes;
        sequenceManagerKeeper.restartAtStop = restartAtStop;

        ArrayList<String> seqs = new ArrayList<>();
        for(int i = 0; i < counter.getSize(); i++){
            seqs.add(counter.getStepTxt(i));
        }
        sequenceManagerKeeper.seqs = seqs;

        return sequenceManagerKeeper;
    }

    public void load(SequenceManagerKeeper k) {
        for (int step = 0; step < counter.getLayout().getChildCount(); step++) {
            setStepSelection(step, (k.seqs.get(step)));
            //setProgress(k.progress);
            //setChangeAtPress(k.changeAtPress);

        }
        setProgress(k.progress);
        setRandomizeProgress(k.randomizeProgress);
        setQueue(k.queue);
        setRestartAtStop(k.restartAtStop);
        setNOfActiveBoxes(k.nOfActiveBoxes);
    }

    public void reset(){
        progressSlider.setProgress(0);
        firstPlayedSeq = true;

        if(restartAtStop) {
            activeSequenceBoxIndex = 0;
            activateSequenceBox(activeSequenceBoxIndex);
        }
    }

    /** UPDATES **/
    private boolean firstPlayedSeq = true; //just to prevent seq-update when starting the sequence the first time
    public void handleSequencerPositionChange(int sequencerPosition){
        float progress = ((float)(sequencerPosition + 1)) / ((float)llppdrums.getDrumMachine().getPlayingSequence().getNOfSteps());
        progressSlider.setProgress(progress);

        if(sequencerPosition == 0 && !firstPlayedSeq){
            updateSeq();
        }
        else if(firstPlayedSeq){
            //this is normally done in updateSeq() but the first time we need to do it here
            counter.activateStep(activeSequenceBoxIndex);
        }
        firstPlayedSeq = false;
    }
}
