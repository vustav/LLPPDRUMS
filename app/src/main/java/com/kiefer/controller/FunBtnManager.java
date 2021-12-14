package com.kiefer.controller;

import com.kiefer.files.keepers.controller.FunBtnManagerKeeper;

public class FunBtnManager {
    private final Controller controller;
    private boolean momentary;

    public FunBtnManager(Controller controller, FunBtnManagerKeeper keeper){
        this.controller = controller;

        if(keeper != null) {
            momentary = keeper.momentary;
        }
        else{
            momentary = true;
        }
    }

    /** SET **/
    public void setMomentary(boolean momentary) {
        this.momentary = momentary;
    }

    /** GET **/
    public boolean isMomentary() {
        return momentary;
    }

    /** KEEPING **/
    public FunBtnManagerKeeper getKeeper(){
        FunBtnManagerKeeper k = new FunBtnManagerKeeper();
        k.momentary = momentary;
        return k;
    }

    public void load(FunBtnManagerKeeper k){
        setMomentary(k.momentary);
    }
}
