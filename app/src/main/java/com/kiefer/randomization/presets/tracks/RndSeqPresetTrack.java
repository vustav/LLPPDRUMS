package com.kiefer.randomization.presets.tracks;

import android.graphics.drawable.GradientDrawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.ArrayList;
import java.util.Random;

public class RndSeqPresetTrack {
    protected final LLPPDRUMS llppdrums;

    private String presetCategory;
    private ArrayList<Step> steps;
    private String name;
    //protected int nOfSteps, nOfSubs;
    private GradientDrawable gradientDrawable;
    private final int oscListImgId;

    private boolean randomizeFx, randomizePan, randomizeVol;

    public RndSeqPresetTrack(LLPPDRUMS llppdrums, String presetCategory, int nOfSteps, int nOfSubs, String name){
        this.llppdrums = llppdrums;
        this.presetCategory = presetCategory;
        this.name = name;
        gradientDrawable = ColorUtils.getGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor(), ColorUtils.HORIZONTAL);
        oscListImgId = ImgUtils.getRandomImageId();

        //this.nOfSteps = nOfSteps;
        //this.nOfSubs = nOfSubs;

        setupSteps(nOfSteps, nOfSubs);

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
        steps.remove(steps.size()-1);
    }

    /** SET **/
    public void setSubPerc(int step, int sub, float perc){
        steps.get(step).setSubPerc(sub, perc);
    }
    public void setSubVol(int step, int sub, float vol){
        steps.get(step).setSubVol(sub, vol);
    }
    public void setSubPitch(int step, int sub, float pitch){
        steps.get(step).setSubPitch(sub, pitch);
    }
    public void setPresetCategory(String presetCategory) {
        this.presetCategory = presetCategory;
    }

    public void setRandomizeFx(boolean randomizeFx) {
        this.randomizeFx = randomizeFx;
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

    /** GET **/
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

    /** STEP CLASS **/
    public class Step{
        ArrayList<Sub> subs;
        private float pan;

        private Step(int nOfSubs){
            pan = 0;
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

        public void setSubVol(int sub, float vol) {
            subs.get(sub).setVol(vol);
        }

        public void setSubPitch(int sub, float pitch) {
            subs.get(sub).setPitch(pitch);
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

        public float getSubPan(){
            return pan;
        }

        public int getNofSubs(){
            return subs.size();
        }
    }

    /** SUB CLASS **/
    public class Sub{
        private float perc, vol;
        private float pitch;

        public Sub(){
            Random r = new Random();
            perc = r.nextFloat();
            vol = r.nextFloat();
            pitch = NmbrUtils.getRndmizer(0f, 1f);
        }

        /** SET **/
        public void setPerc(float perc) {
            this.perc = perc;
        }

        public void setVol(float vol) {
            this.vol = vol;
        }

        public void setPitch(float pitch) {
            this.pitch = pitch;
        }

        /** GET **/
        public float getPerc(){
            return perc;
        }

        public float getVol() {
            return vol;
        }

        public float getPitch() {
            return pitch;
        }
    }
}
