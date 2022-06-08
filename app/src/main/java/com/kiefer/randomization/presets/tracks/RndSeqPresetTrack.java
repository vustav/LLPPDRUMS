package com.kiefer.randomization.presets.tracks;

import android.graphics.drawable.GradientDrawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.rndSeqManager.RndSeqPresetStepKeeper;
import com.kiefer.files.keepers.rndSeqManager.RndSeqPresetSubKeeper;
import com.kiefer.files.keepers.rndSeqManager.RndSeqPresetTrackKeeper;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.ArrayList;
import java.util.Random;

public class RndSeqPresetTrack {
    protected final LLPPDRUMS llppdrums;

    private String presetCategory;

    //percentage of it beeing a sample when soundSource is randomized
    private float samplePerc = .7f;

    private ArrayList<Step> steps;
    private String name;
    private GradientDrawable gradientDrawable;
    private final int oscListImgId;

    private boolean autoRnd, randomizeFx, randomizePan, randomizeVol;

    public RndSeqPresetTrack(LLPPDRUMS llppdrums, RndSeqPresetTrackKeeper keeper){
        this.llppdrums = llppdrums;

        gradientDrawable = ColorUtils.getGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor(), ColorUtils.HORIZONTAL);
        oscListImgId = ImgUtils.getRandomImageId();

        presetCategory = keeper.presetCategory;

        name = keeper.name;

        setupSteps(keeper.nOfSteps, keeper.nOfSubs);

        autoRnd = keeper.autoRnd;
        randomizeFx = keeper.randomizeFx;
        randomizePan = keeper.randomizePan;
        randomizeVol = keeper.randomizeVol;

        restore(keeper);
    }

    public RndSeqPresetTrack(LLPPDRUMS llppdrums, String presetCategory, int nOfSteps, int nOfSubs, String name){
        this.llppdrums = llppdrums;
        this.name = name;

        this.presetCategory = presetCategory;

        gradientDrawable = ColorUtils.getGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor(), ColorUtils.HORIZONTAL);
        oscListImgId = ImgUtils.getRandomImageId();

        setupSteps(nOfSteps, nOfSubs);

        autoRnd = false;
        randomizeFx = true;
        randomizePan = true;
        randomizeVol = false;
    }

    private void setupSteps(int nOfSteps, int nOfSubs){
        steps = new ArrayList<>();

        for(int i = 0; i<nOfSteps; i++){
            addStep(nOfSubs);
        }
    }

    public void addStep(int nOfSubs){
        steps.add(new Step(nOfSubs));
    }

    public void removeStep(){
        if(steps.size() > 1) {
            steps.remove(steps.size() - 1);
        }
    }

    public void randomizeStep(int step, int sub){
        steps.get(step).setSubPerc(sub, .5f);
    }

    /** SET **/
    public void setSubPerc(int step, int sub, float perc){
        steps.get(step).setSubPerc(sub, perc);
    }
    public void setSubVolInterval(int step, int sub, float min, float max){
        steps.get(step).setSubVolInterval(sub, min, max);
    }
    public void setSubPitchInterval(int step, int sub, float min, float max){
        steps.get(step).setSubPitchInterval(sub, min, max);
    }
    public void setStepPanInterval(int step, float min, float max){
        steps.get(step).setPanInterval(min, max);
    }
    public void setPresetCategories(String presetCategory) {
        this.presetCategory = presetCategory;
    }

    public void setRandomizeFx(boolean randomizeFx) {
        this.randomizeFx = randomizeFx;
    }

    public void setAutoRnd(boolean autoRnd){
        this.autoRnd = autoRnd;
    }

    public void setRandomizePan(boolean randomizePan) {
        this.randomizePan = randomizePan;
    }

    public void setRandomizeVol(boolean randomizeVol) {
        this.randomizeVol = randomizeVol;
    }

    public void setNOfSubs(int nOfSubs) {
        for(Step step : steps){
            step.setNOfSubs(nOfSubs);
        }
        //this.nOfSubs = nOfSubs;
    }

    public void setSamplePerc(float perc){
        samplePerc = perc;
    }

    /** GET **/
    public float getSamplePerc() {
        return samplePerc;
    }

    public String getPresetCategory() {
        return presetCategory;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public GradientDrawable getGradientDrawable() {
        return gradientDrawable;
    }

    public int getOscListImgId() {
        return oscListImgId;
    }

    public boolean getRandomizeFx() {
        return randomizeFx;
    }

    public boolean getAutoRnd(){
        return autoRnd;
    }

    public boolean getRandomizePan() {
        return randomizePan;
    }

    public boolean getRandomizeVol() {
        return randomizeVol;
    }

    public int getnOfSubs() {
        return steps.get(0).getNofSubs();
    }

    public String getName(){
        return name;
    }

    /** RESTORATION **/
    public RndSeqPresetTrackKeeper getKeeper(){
        RndSeqPresetTrackKeeper keeper = new RndSeqPresetTrackKeeper();
        keeper.randomizeFx = randomizeFx;
        keeper.randomizePan = randomizePan;
        keeper.randomizeVol = randomizeVol;
        keeper.nOfSteps = steps.size();
        keeper.nOfSubs = getnOfSubs();
        keeper.autoRnd = autoRnd;

        ArrayList<RndSeqPresetStepKeeper> stepKeepers = new ArrayList<>();
        for(int i = 0; i < steps.size(); i++){
            stepKeepers.add(steps.get(i).getKeeper());
        }
        keeper.rndSeqPresetStepKeepers = stepKeepers;
        return keeper;
    }

    public void restore(RndSeqPresetTrackKeeper keeper){
        for(int i = 0; i < steps.size(); i++){
            steps.get(i).restore(keeper.rndSeqPresetStepKeepers.get(i));
        }
    }

    /** STEP CLASS **/
    public class Step{
        ArrayList<Sub> subs;
        private float minPan, maxPan;

        private Step(int nOfSubs){
            minPan = -1f;
            minPan = 1f;
            subs = new ArrayList<>();
            for(int i = 0; i < nOfSubs; i++){
                addSub();
            }
        }

        private void addSub(){
            subs.add(new Sub());
        }

        private void removeSub(){
            subs.remove(subs.size()-1);
        }

        private void setNOfSubs(int nOfSubs){
            while (subs.size() < nOfSubs) {
                //addEvent(addToSequencer, r.nextInt(events.size()) == 0);
                addSub();
            }
            while (subs.size() > nOfSubs){
                //deleteEvent();
                removeSub();
            }
        }

        /** SET **/

        public void setSubPerc(int sub, float perc) {
            subs.get(sub).setPerc(perc);
        }

        public void setSubVolInterval(int sub, float min, float max) {
            subs.get(sub).setVolInterval(min, max);
        }

        public void setSubPitchInterval(int sub, float min, float max) {
            subs.get(sub).setPitchInterval(min, max);
        }

        public void setPanInterval(float min, float max){
            minPan = min;
            maxPan = max;
        }

        /** GET **/
        public float getSubPerc(int sub){
            return subs.get(sub).getPerc();
        }

        public float getSubVol(int sub) {
            return subs.get(sub).getVol();
        }

        public float getSubPitch(int sub) {
            return subs.get(sub).getPitch();
        }

        public float getPan(){
            return NmbrUtils.getRndmizer(minPan, maxPan);
        }

        public int getNofSubs(){
            return subs.size();
        }

        /** RESTORATION **/
        public RndSeqPresetStepKeeper getKeeper(){
            RndSeqPresetStepKeeper keeper = new RndSeqPresetStepKeeper();
            keeper.minPan = Float.toString(minPan);
            keeper.maxPan = Float.toString(maxPan);

            ArrayList<RndSeqPresetSubKeeper> rndSeqPresetSubKeepers = new ArrayList<>();
            for(Sub s : subs){
                rndSeqPresetSubKeepers.add(s.getKeeper());
            }
            keeper.rndSeqPresetSubKeepers = rndSeqPresetSubKeepers;

            return keeper;
        }

        public void restore(RndSeqPresetStepKeeper keeper){
            minPan = Float.parseFloat(keeper.minPan);
            maxPan = Float.parseFloat(keeper.maxPan);

            for(int i = 0; i < subs.size(); i++){
                subs.get(i).restore(keeper.rndSeqPresetSubKeepers.get(i));
            }
        }
    }

    /** SUB CLASS **/
    public class Sub{
        private float perc, minVol, maxVol, minPitch, maxPitch;

        public Sub(){
            Random r = new Random();
            perc = r.nextFloat();
            minVol = .3f;
            maxVol = 1f;
            minPitch = 0f;
            maxPitch = 1f;
        }

        /** SET **/
        public void setPerc(float perc) {
            this.perc = perc;
        }

        public void setVolInterval(float min, float max) {
            minVol = min;
            maxVol = max;
        }

        public void setPitchInterval(float min, float max) {
            minPitch = min;
            maxPitch = max;
        }

        /** GET **/
        public float getPerc(){
            return perc;
        }

        public float getVol() {
            return NmbrUtils.getRndmizer(minVol, maxVol);
        }

        public float getPitch() {
            return NmbrUtils.getRndmizer(minPitch, maxPitch);
        }

        /** RESTORE **/
        public RndSeqPresetSubKeeper getKeeper(){
            RndSeqPresetSubKeeper keeper = new RndSeqPresetSubKeeper();
            keeper.perc = Float.toString(perc);
            keeper.minVol = Float.toString(minVol);
            keeper.maxVol = Float.toString(maxVol);
            keeper.minPitch = Float.toString(minPitch);
            keeper.maxPitch = Float.toString(maxPitch);
            keeper.presetCategory = presetCategory;

            return keeper;
        }

        public void restore(RndSeqPresetSubKeeper keeper){
            perc = Float.parseFloat(keeper.perc);
            minVol = Float.parseFloat(keeper.minVol);
            maxVol = Float.parseFloat(keeper.maxVol);
            minPitch = Float.parseFloat(keeper.minPitch);
            maxPitch = Float.parseFloat(keeper.maxPitch);
            presetCategory = keeper.presetCategory;
        }
    }
}
