package com.kiefer.machine.sequence.sequenceModules.autoRandom;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.engine.EngineFacade;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.SequenceModuleMode;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomMax;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomMin;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomPerc;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomReturn;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

public class AutoRandom extends SequenceModuleMode {
    protected SequenceModule sequenceModule;
    protected ArrayList<AutoRandomModule> modules;
    protected AutoRandomModule selectedModule;

    //bg
    int rndModuleListBg;

    public AutoRandom(LLPPDRUMS llppdrums, DrumSequence drumSequence, SequenceModule sequenceModule, int tabIndex){
        super(llppdrums, drumSequence, tabIndex);
        this.sequenceModule = sequenceModule;

        rndModuleListBg = ImgUtils.getRandomImageId();
        tabLabel = llppdrums.getResources().getString(R.string.seqModRandomModeTabName);

        setup();
    }

    protected void setup(){
        modules = new ArrayList<>();
        //modules.add(new AutoRandomOn(llppdrums, sequenceModule));
        modules.add(new AutoRandomPerc(llppdrums, sequenceModule));
        modules.add(new AutoRandomMin(llppdrums, sequenceModule));
        modules.add(new AutoRandomMax(llppdrums, sequenceModule));
        modules.add(new AutoRandomReturn(llppdrums, sequenceModule));
        selectedModule = modules.get(0);
    }

    /** SELECTION **/
    public void select(){
        //Log.e("", "AutoRandom.select()");
        llppdrums.getSequencer().setSequencerDrawables(drumSequence.getTracks());
        llppdrums.getSequencer().setAutoRndModeLabel(selectedModule.getLabel());
        llppdrums.getSequencer().setAutoRndModeVisibility(true);

    }
    public void deselect(){
        llppdrums.getSequencer().setAutoRndModeVisibility(false);
    }

    /** LISTENER **/
    public void onStepTouch(EngineFacade engineFacade, final ImageView stepIV, final Step step, float startX, float startY){
        //Log.e("AutoRandom", "onStepTouch()");
        selectedModule.onStepTouch(engineFacade, stepIV, step, startX, startY);
    }

    /** GET **/
    public void setAutoValue(final DrumTrack drumTrack, String s, int sub){
        selectedModule.setAutoValue(drumTrack, s, sub);
    }

    public AutoRandomModule getSelectedModule(){
        return selectedModule;
    }

    public ArrayList<String> getAutoStepValues(){
        return selectedModule.getAutoStepValues();
    }

    public Drawable getDrawable(Step step){
        return selectedModule.getDrawable(step);
    }

    public ArrayList<AutoRandomModule> getModules() {
        return modules;
    }

    public SequenceModule getSequenceModule() {
        return sequenceModule;
    }

    public int getRndModuleListBg() {
        return rndModuleListBg;
    }

    public String getFullName(){
        return llppdrums.getResources().getString(R.string.seqModRandomModeName) + llppdrums.getResources().getString(R.string.stringDivider) + selectedModule.getFullName();
    }

    /** SET **/
    public void setSelectedModule(AutoRandomModule m){
        selectedModule = m;
        this.select();
    }
}
