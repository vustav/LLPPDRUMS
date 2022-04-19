package com.kiefer.ui.tabs.interfaces;

import android.graphics.Bitmap;

import java.util.ArrayList;

/** For UI-classes with tabs **/

public interface TabUIHoldable {
    void setTabAppearances(int tier, ArrayList<Tabable> tabables, int selectedTabNo);
}
