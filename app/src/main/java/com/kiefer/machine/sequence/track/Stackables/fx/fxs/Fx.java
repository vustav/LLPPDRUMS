package com.kiefer.machine.sequence.track.Stackables.fx.fxs;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.automation.AutomationManager;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.machine.DrumMachine;
import com.kiefer.machine.sequence.track.Stackables.Stackable;
import com.kiefer.machine.sequence.track.Stackables.fx.FxManager;
import com.kiefer.machine.sequence.track.Stackables.fx.Fxer;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

import nl.igorski.mwengine.core.BaseProcessor;

public abstract class Fx implements Stackable  {
    protected LLPPDRUMS llppdrums;
    protected FxManager fxManager;
    protected Fxer fxer;
    protected BaseProcessor fx;
    //protected View layout;
    protected int floatMultiplier;
    protected AutomationManager automationManager;

    protected int gradientColorTwo;

    private boolean on;
    private final int fxNo; //this is the number in fxInfos in FxManager, not its position in the chain

    private final int automationBgId;

    //PARAMS
    protected ArrayList<String> paramNames; //has to be populated by children!

    public Fx(LLPPDRUMS llppdrums, FxManager fxManager, Fxer fxer, int fxNo, boolean automation){
        this.fxManager = fxManager;
        this.fxer = fxer;
        this.llppdrums = llppdrums;
        this.fxNo = fxNo;

        gradientColorTwo = ColorUtils.getRandomColor();

        paramNames = new ArrayList<>();
        paramNames.add(llppdrums.getResources().getString(R.string.fxOn)); //every fx has this one

        floatMultiplier = llppdrums.getResources().getInteger(R.integer.floatMultiplier);

        automationBgId = ImgUtils.getRandomImageId();

        setupParamNames();
        automationManager = new AutomationManager(llppdrums, fxer, this);
        if(automation){
            randomizeAutomation();
        }

        on = true;
    }

    //getters
    public abstract View getLayout();
    public abstract GradientDrawable getBgGradient();
    public abstract GradientDrawable getTabGradient();
    public abstract String getName();
    public abstract String getInfoKey();

    //restoration
    public abstract FxKeeper getKeeper();
    public abstract void restore(FxKeeper k);

    //have to do this here since automations are created before fxs gets their view it can't be done then
    public abstract void setupParamNames();

    //used when an fx is selected (also called when the popup is created) to update the UI
    //public abstract void select();

    public void destroy() {
        //fx.delete(); //.delete() might be dangerous, should be collected...
        //fx.setDeletable(true);
        llppdrums.getDeleter().addFx(fx);
        fx = null;
    }

    /** RND **/
    private void randomizeAutomation(){
        Random random = new Random();
        final int MAX_N_RND_FXS = 2;
        int nOfAutos = random.nextInt(MAX_N_RND_FXS + 1);

        for(int i = 0; i < nOfAutos; i++){
            automationManager.addAutomation();
        }
    }

    /** ACTIVATION **/
    //override if you need to
    public void activate(){

    }

    public void deactivate(){

    }

    /** STEPS **/
    public void addStep(){
        automationManager.addStep();
    }

    public void removeStep(){
        automationManager.removeStep();
    }

    /** GET **/

    @Override
    public ArrayList<String> getParams(){
        return paramNames;
    }

    public boolean isOn(){
        //Log.e("Fx", "isOn: "+on);
        return on;
    }

    public AutomationManager getAutomationManager() {
        return automationManager;
    }

    public int getAutomationBgId() {
        return automationBgId;
    }

    public BaseProcessor getBaseProcessor(){
        return fx;
    }

    public int getFxNo() {
        return fxNo;
    }

    /** SET **/
    public void setOn(boolean on){
        this.on = on;
    }

    /** TRANSPORT **/
    public void play(){

    }

    public void stop(){
        automationManager.stop();
    }

    /** AUTOMATE **/
    public void automate(int step, boolean popupShowing){
        automationManager.automate(step, popupShowing);
    }

    public void resetAutomation(){
        automationManager.resetAutomation();
    }

    /** THIS IS JUST ON/OFF, OVERRIDE IN CHILDREN FOR ALL OTHER CASES **/
    @Override
    public float turnOnAutoValue(String param, float autoValue, boolean popupShowing) {

        if (param.equals(paramNames.get(0))) { //on

            boolean ogOn = isOn();

            if(popupShowing && fxManager.getSelectedStackable() == this) {
                fxer.getStackableManagerPopup(DrumMachine.FX_TYPE).getOnCb().setChecked(!ogOn); //doesn't seem to be triggering the listener here...
            }
            fxManager.turnFxOn(this, !ogOn);

            //use 1 and 0 instead of booleans here
            if (ogOn) {
                return 1;
            } else {
                return 0;
            }
        }
        return -1;
    }

    @Override
    public void turnOffAutoValue(String param, float oldValue, boolean popupShowing) {

        if (param.equals(paramNames.get(0))) { //on

            boolean ogOn = oldValue == 1;

            if (popupShowing && fxManager.getSelectedStackable() == this) {
                fxer.getStackableManagerPopup(DrumMachine.FX_TYPE).getOnCb().setChecked(ogOn); //doesn't seem to be triggering the listener here...
            }
            fxManager.turnFxOn(this, ogOn);
        }
    }
}
