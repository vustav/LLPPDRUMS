package com.kiefer.machine.sequence.sequenceModules;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.engine.EngineFacade;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.AutoRandom;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomModuleBool;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.Popup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.TabHoldable;
import com.kiefer.ui.tabs.interfaces.Tabable;
import com.kiefer.machine.sequence.DrumSequence;

import java.util.ArrayList;
import java.util.Random;

public abstract class SequenceModule implements TabHoldable, Tabable {
    protected LLPPDRUMS llppdrums;
    protected DrumSequence drumSequence;

    //modes
    protected ArrayList<SequenceModuleMode> modes;
    protected SequenceModuleMode selectedMode;
    public static int BASE = 0, AUTO_RANDOM = 1;

    //bitmaps
    //private final Bitmap tabBitmap, bgBitmap;
    private int bitmapId;

    //random
    private Random random;
    protected ArrayList<String> autoStepValues; //set by the kids

    public static String NONE = "0", ALL = "1", EVERY_SECOND = "1/2",EVERY_THIRD = "1/3", EVERY_FOURTH = "1/4", EVERY_FIFTH = "1/5", EVERY_SIXTH = "1/6", EVERY_SEVENTH = "1/7";
    public static int ON = 0, VOL = 1, PITCH = 2, PAN = 3;

    /** ABSTRACT METHODS **/
    public abstract Popup getOneSubPopup(ImageView stepIV, Step step);
    public abstract Popup getSubsPopup(ImageView stepIV, Step step);

    abstract void setupModes();
    abstract void setupAutoValues();//the values shown in the autoStepPopup
    public abstract float setAutoValueBase(final DrumTrack drumTrack, String s, int sub); //seqModules do their base-thing here
    public abstract String getFullName();
    public abstract Drawable getDrawable(int trackNo, int stepNo); //returns the drawable of the module, ex. different volumes-symbols for volume
    //public abstract void onStepTouch(EngineFacade engineFacade, ImageView stepIV, Step step, float startX, float startY, float endX, float endY, int action); //modules will update the engine themselves, since only they know what type they are
    public abstract void handleSequencerPositionChange(int sequencerPosition);

    //public abstract void setAutoRndOn(Drum drum, boolean on); //a children sets its autoRndValue, ex. on forOnOff, vol forVolume etc
    public abstract boolean getAutoRndOn(Step step, int sub); //Volume would return autoRndVol etc
    public abstract float getAutoRndMin(Step step, int sub);
    public abstract void setAutoRndMin(Step step, float min, int sub);
    public abstract float getAutoRndMax(Step step, int sub);
    public abstract void setAutoRndMax(Step step, float max, int sub);
    public abstract boolean getAutoRndReturn(Step step, int sub);
    public abstract void setAutoRndReturn(Step step, boolean r, int sub);
    public abstract float getAutoRndPerc(Step step, int sub);
    public abstract void setAutoRndPerc(Step step, float perc, int sub);

    public abstract void randomize(DrumTrack drumTrack);

    protected SequenceModule(LLPPDRUMS llppdrums, DrumSequence drumSequence, int bitmapId){
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.bitmapId = bitmapId;

        random = new Random();

        setupModes();
        setupAutoValues();

        selectedMode = modes.get(0);
    }

    protected long startTime = 0;
    protected boolean release;
    public void onStepTouch(EngineFacade engineFacade, final ImageView stepIV, final Step step, float startX, float startY, float endX, float endY, int action){
        if(action == MotionEvent.ACTION_UP) {
            //base
            if (modes.indexOf(selectedMode) == 0) {
                if(step.getNofSubs() > 1){
                    getSubsPopup(stepIV, step);
                }
                else {
                    getOneSubPopup(stepIV, step);
                }
            }
            //random
            else {
                ((AutoRandom) modes.get(AUTO_RANDOM)).onStepTouch(engineFacade, stepIV, step, startX, startY);
            }
        }
    }

    /** SELECTION **/
    //on selection, "select" the selected mode to make it draw its Drawable in the sequencer
    public void select(){
        setSelectedMode(modes.indexOf(selectedMode));
        selectedMode.select();
    }

    public void deselect(){
        selectedMode.deselect();
    }

    public void setSelectedMode(int i){
        selectedMode.deselect();
        selectedMode = modes.get(i);
        selectedMode.select();
    }

    /** AUTO **/
    public void setAutoValue(final DrumTrack drumTrack, String s, int sub) {
        if(isInBaseMode()) {
            setAutoValueBase(drumTrack, s, sub);//seqModules do their thing here
        }
        else {
            ((AutoRandom)modes.get(AUTO_RANDOM)).setAutoValue(drumTrack, s, sub);
        }
    }

    public void pushLeft(DrumTrack drumTrack){
        //this is for random only. OnOff overrides and handles its BASE-case
        if(!isInBaseMode()) {
            if(((AutoRandom)modes.get(AUTO_RANDOM)).getSelectedModule() instanceof AutoRandomModuleBool){
                ((AutoRandomModuleBool) ((AutoRandom)modes.get(AUTO_RANDOM)).getSelectedModule()).pushLeft(drumTrack);
            }
        }
    }

    public void pushRight(DrumTrack drumTrack){
        //this is for random only. OnOff overrides and handles its BASE-case
        if(!isInBaseMode()) {
            if(((AutoRandom)modes.get(AUTO_RANDOM)).getSelectedModule() instanceof AutoRandomModuleBool){
                ((AutoRandomModuleBool) ((AutoRandom)modes.get(AUTO_RANDOM)).getSelectedModule()).pushRight(drumTrack);
            }
        }
    }

    /** GET **/
    public AutoRandom getAutoRandom(){
        return (AutoRandom) modes.get(AUTO_RANDOM);
    }

    public ArrayList<SequenceModuleMode> getModuleModes(){
        return modes;
    }

    public SequenceModuleMode getSelectedMode() {
        return selectedMode;
    }


    @Override
    public int getBitmapId(){
        return bitmapId;
    }

    @Override
    public int getOrientation(){
        return TabManager.VERTICAL;
    }

    @Override
    public ArrayList<Tabable> getTabables(int tierNo){
        ArrayList<Tabable> tabs = new ArrayList<>();
        tabs.addAll(modes);
        return tabs;
    }

    public boolean isInBaseMode(){
        return !(selectedMode instanceof AutoRandom);
    }

    public ArrayList<String> getAutoStepValues(){
        if(isInBaseMode()) {
            return autoStepValues;
        }
        else{
            return ((AutoRandom)modes.get(AUTO_RANDOM)).getAutoStepValues();
        }
    }

    /** SET **/
    public void addAutoIndication(Step step, Canvas canvas){
        //if(step.automationActive()) {
            Paint paint = new Paint();
            paint.setColor(llppdrums.getResources().getColor(R.color.autoRndDotColor));

            float width = canvas.getWidth();

            float radius = width / 5;

            canvas.drawCircle(width, 0, radius, paint);
        //}
    }
}
