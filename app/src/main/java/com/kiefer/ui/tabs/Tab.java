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
    private final String name;
    private final int bitmapId, n, tier, orientation;

    public Tab(String name, int bitmapId, int n, int tier, int orientation){
        this.name = name;
        this.bitmapId = bitmapId;
        this.n = n;
        this.tier = tier;
        this.orientation = orientation;
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

    public int getIndex() {
        return n;
    }

    public int getOrientation() {
        return orientation;
    }
}
