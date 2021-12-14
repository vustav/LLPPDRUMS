package com.kiefer.ui.tabs;

//small class just for storing the tab data

import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;

public class Tab{
    private String name;
    private Bitmap bitmap;
    private int n, tier;
    private View background, border;
    private TextView tv;

    public Tab(String name, Bitmap bitmap, int n, int tier, View background, View border, TextView textView){
        this.name = name;
        this.bitmap = bitmap;
        this.n = n;
        this.tier = tier;
        this.background = background;
        this.border = border;
        this.tv = textView;
    }

    public String getName() {
        return name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getTier() {
        return tier;
    }

    public View getBackground() {
        return background;
    }

    public int getN() {
        return n;
    }

    public View getBorder() {
        return border;
    }

    public TextView getTextView() {
        return tv;
    }
}
