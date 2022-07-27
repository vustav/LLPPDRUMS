package com.kiefer.machine.sequence.track.Stackables;

import com.kiefer.machine.sequence.track.Stackables.fx.FxManager;

import java.util.ArrayList;

public abstract class StackableManager {
    protected Stacker stacker;

    public abstract void randomizeAll(boolean allowZero);
    public abstract ArrayList<Stackable> getStackables();
    public abstract int getSelectedStackableIndex();
    public abstract void createRandomStackable(boolean automation);
    public abstract Stackable getSelectedStackable();
    public abstract void turnSelectedStackableOn(Boolean on);
    public abstract void changeSelectedStackable(int newStackableIndex);
    public abstract void setSelectedStackable(int index, int sender);
    public abstract void moveStackable(final int from, final int to);
    public abstract void removeStackable(int n);
    public abstract int getListPopupImgId();
    public abstract int getBgImgId();
    public abstract String getName();


    public abstract ArrayList<StackableInfo> getStackableInfos();

    public StackableManager(Stacker stacker){
        this.stacker = stacker;
    }

    public Stacker getStacker() {
        return stacker;
    }

    /** INNER CLASS **/
    public static class StackableInfo {
        private final String name;
        private final int color;

        public StackableInfo(String name, int color){
            this.name = name;
            this.color = color;
        }

        public String getName(){
            return name;
        }

        public int getColor(){
            return color;
        }
    }
}
