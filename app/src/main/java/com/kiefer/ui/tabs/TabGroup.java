package com.kiefer.ui.tabs;

import android.widget.LinearLayout;

import java.util.ArrayList;

/** small class to store the layout and the array of tabs **/

public class TabGroup {
    private LinearLayout layout;
    private ArrayList<Tab> tabs;

    public TabGroup(LinearLayout layout, ArrayList<Tab> tabs){
        this.layout = layout;
        this.tabs = tabs;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public ArrayList<Tab> getTabs() {
        return tabs;
    }
}
