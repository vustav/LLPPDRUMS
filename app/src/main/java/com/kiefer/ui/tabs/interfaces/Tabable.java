package com.kiefer.ui.tabs.interfaces;

import android.graphics.Bitmap;

/** implemented by classes that will be represented by a tab in the UI **/
public interface Tabable {
    int getBitmapId();
    String getLabel();
    int getOrientation();
}
