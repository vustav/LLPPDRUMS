package com.kiefer.ui.tabs;

//small class just for storing the tab data

import static com.kiefer.ui.tabs.TabManager.VERTICAL;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;

public class Tab{
    private String name;
    private int bitmapId, n, tier, orientation;
    //private FrameLayout tabLayout, background, border;
    //private TextView tv;

    public Tab(LLPPDRUMS llppdrums, String name, int bitmapId, int n, int tier, int orientation){
        this.name = name;
        this.bitmapId = bitmapId;
        this.n = n;
        this.tier = tier;
        this.orientation = orientation;
        //this.tv = textView;
/*
        //ViewGroup singleTabLayout;
        if(orientation == VERTICAL){
            tabLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.tab_single_vertical, null);
        }
        else{
            tabLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.tab_single_horizontal, null);
        }

        //setup params
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        tabLayout.setLayoutParams(llp);

        //set up the textView
        tv = tabLayout.findViewById(R.id.tabTxt);
        tv.setText(name);


        //get the two views that Tab needs
        border = tabLayout.findViewById(R.id.tabBorder);
        background = tabLayout.findViewById(R.id.tabBg);

 */
    }

    public String getName() {
        return name;
    }

    public int getBitmapId() {
        return bitmapId;
    }

    public int getTier() {
        return tier;
    }

    /*
    public FrameLayout getBackground() {
        return tabLayout;
    }

     */

    public int getN() {
        return n;
    }
/*
    public FrameLayout getBorder() {
        return border;
    }

    public TextView getTextView() {
        return tv;
    }

 */

    public int getOrientation() {
        return orientation;
    }
}
