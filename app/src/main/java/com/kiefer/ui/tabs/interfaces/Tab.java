package com.kiefer.ui.tabs.interfaces;

/** implemented by classes that will be represented by a tab in the UI **/
public interface Tab {
    int getBitmapId();
    String getName();
    int getOrientation();
    int getTier();
    int getTabIndex();
}
