package com.kiefer.ui.tabs.interfaces;

import android.graphics.Bitmap;

/** implemented by classes that will be represented by a tab in the UI **/
public interface Tabable {
    //int getColor();
    Bitmap getTabBitmap();
    Bitmap getBgBitmap();
    String getLabel();
}
