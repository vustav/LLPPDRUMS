package com.kiefer.automation;

import android.graphics.drawable.GradientDrawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.AutomationKeeper;
import com.kiefer.files.keepers.AutomationManagerKeeper;
import com.kiefer.machine.sequence.track.Stackables.Stacker;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Random;

public class AutomationManager {
    LLPPDRUMS llppdrums;
    private final Stacker stacker;
    private final Automatable automatable;
    private ArrayList<Automation> automations;
    private final Random random;

    public AutomationManager(LLPPDRUMS llppdrums, Stacker stacker, Automatable automatable){
        this.llppdrums = llppdrums;
        this.stacker = stacker;
        this.automatable = automatable;
        random = new Random();
        automations = new ArrayList<>();
    }

    /** CHANGE DATASET **/
    public Automation addAutomation(){
        Automation automation = new Automation(stacker.getNOfSteps());
        automations.add(automation);
        return automation;
    }

    public void moveAutomation(int from, int to){
        Automation automation = automations.remove(from);
        automations.add(to, automation);
    }

    public void removeAutomation(int index){
        automations.get(index).reset();
        automations.remove(index);
    }

    /** STEPS **/
    public void addStep(){
        for(Automation a : automations){
            a.addStep();
        }
    }

    public void removeStep(){
        for(Automation a : automations){
            a.removeStep();
        }
    }

    /**SET **/
    public void setOn(int index, boolean on){
        automations.get(index).setOn(on);
    }

    public void setStepOn(int index, int step, boolean on){
        automations.get(index).setStepOn(step, on);
    }

    public void setParam(int index, int param){
        automations.get(index).setParam(getParams().get(param));
    }

    public void setValue(int index, float value){
        automations.get(index).setValue(value);
    }

    /** GET **/
    public ArrayList<Automation> getAutomations() {
        return automations;
    }

    public String getParam(int index){
        return automations.get(index).getParam();
    }

    public ArrayList<String> getParams(){
        return automatable.getParams();
    }

    public float getValue(int index){
        return automations.get(index).getValue();
    }

    public GradientDrawable getAutomationBgGradient(int index){
        return automations.get(index).getBgGradient();
    }

    public GradientDrawable getAutomationListGradient(int index){
        return automations.get(index).getListGradient();
    }

    public boolean getStepOn(int index, int step){
        return automations.get(index).getStepOn(step);
    }

    /** TRANSPORT **/
    public void play(){

    }

    public void stop(){
        for(Automation a  : automations){
            a.stop();
        }
    }

    /** AUTOMATION **/
    public void automate(int step, boolean popupShowing){
        for(Automation a : automations){

            //if the automation is ON or the fx is ON do it, otherwise don't
            if(a.param.equals(llppdrums.getResources().getString(R.string.fxOn)) || automatable.isOn()){
                a.automate(step, popupShowing);
            }
        }
    }
    public void resetAutomation(){
        for(Automation a : automations){
            a.reset();
        }
    }

    //FXs call this every time a parameter is changed. Without this the change won't stick id done
    //when an automation is active since it'll return to the saved old value when it ends
    public void changeInBaseValue(String param, float value){
        for(Automation a : automations){
            if(a.getParam().equals(param)){
                a.changeInBaseValue(value);
            }
        }
    }

    /** RESTORATION **/
    public void restore(AutomationManagerKeeper keeper){

        automations = new ArrayList<>(); //since automations is just numbers there's no need for a destroy()-method

        for(AutomationKeeper ak : keeper.automationKeepers){
            Automation automation = addAutomation();
            automation.restore(ak);
        }
    }

    public AutomationManagerKeeper getKeeper(){
        AutomationManagerKeeper keeper = new AutomationManagerKeeper();

        keeper.automationKeepers = new ArrayList<>();
        for(Automation a : automations){
            keeper.automationKeepers.add(a.getKeeper());
        }

        return keeper;
    }

    /** INNER CLASSES **/
    public class Automation{
        private boolean on = true;
        private final GradientDrawable bgGradient, listGradient;
        private String param;
        private float value;
        private ArrayList<Boolean> steps;

        private Automation(int nOfSteps){
            bgGradient = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
            listGradient = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());

            param = automatable.getParams().get(random.nextInt(automatable.getParams().size()));

            value = random.nextFloat();


            steps = new ArrayList<>();
            for(int step = 0; step < nOfSteps; step++){
                steps.add(random.nextInt(2) == 0);
            }
        }

        /** STEPS **/
        public void addStep(){
            steps.add(false);
            //Log.e("AutoMan", "steps: "+steps.size());
        }

        public void removeStep(){
            steps.remove(steps.size()-1);
        }

        /** SET **/
        public void setOn(boolean on){
            this.on = on;
            if(!on){
                automatable.turnOffAutoValue(param, originalValue, true);
            }
        }

        public void setStepOn(int step, boolean on){
            steps.set(step, on);
        }

        public void setParam(String param){
            if(hasAutomated){
                automatable.turnOffAutoValue(this.param, originalValue, true);
                hasAutomated = false;
            }
            this.param = param;
        }

        public void setValue(float value){
            this.value = value;
        }

        /** GET **/
        public boolean isOn(){ //is the automation on, not a step
            return on;
        }

        public boolean isStepOn(int step){
            return steps.get(step);
        }

        public GradientDrawable getBgGradient() {
            return bgGradient;
        }

        public GradientDrawable getListGradient() {
            return listGradient;
        }

        public String getParam() {
            return param;
        }

        public float getValue() {
            return value;
        }

        public int getNOfSteps(){
            return steps.size();
        }

        public boolean getStepOn(int index){
            return steps.get(index);
        }


        /** TRANSPORT **/
        private boolean hasAutomated = false; //this is used to avoid resetting an automation that hasn't occurred
        private boolean popupShowing = false;
        float originalValue = -1;

        public void play(){

        }

        //called on stop and deselection. Turn off values and reset hasAutomated.
        public void stop(){
            if(hasAutomated){
                automatable.turnOffAutoValue(param, originalValue, popupShowing);
            }
            hasAutomated = false;
        }

        /** RESET **/
        private void reset(){
            if(hasAutomated){
                automatable.turnOffAutoValue(param, originalValue, popupShowing);
            }
        }

        //FXs call this every time a parameter is changed. Without this the change won't stick id done
        //when an automation is active since it'll return to the saved old value when it ends
        public void changeInBaseValue(float value){
            if(hasAutomated) {
                originalValue = value;
            }
        }

        /** AUTOMATION **/
        private void automate(int step, boolean popupShowing){
            this.popupShowing = popupShowing;
            if(on) {

                //get the step before this to see if a change is needed
                int lastStep = step - 1;
                if (lastStep == -1) {
                    lastStep = getNOfSteps() - 1;
                }

                //if this step is automated and the last wasn't, make a change
                if (steps.get(step) && !steps.get(lastStep)) {
                    hasAutomated = true;
                    originalValue = automatable.turnOnAutoValue(param, value, popupShowing);
                }
                //..or if turn off is the other way around. Make sure it automated in the first place, otherwise originalValue has no value.
                else if ((!steps.get(step) && steps.get(lastStep)) && hasAutomated) {
                    hasAutomated = false;
                    automatable.turnOffAutoValue(param, originalValue, popupShowing);
                }
            }
        }

        /** RESTORATION **/
        public void restore(AutomationKeeper keeper){
            on = keeper.on;
            param = keeper.param;
            value = Float.parseFloat(keeper.value);
            steps = keeper.steps;
        }

        public AutomationKeeper getKeeper(){
            AutomationKeeper keeper = new AutomationKeeper();
            keeper.on = on;
            keeper.param = param;
            keeper.value = Float.toString(value);
            keeper.steps = steps;
            return keeper;
        }
    }
}
