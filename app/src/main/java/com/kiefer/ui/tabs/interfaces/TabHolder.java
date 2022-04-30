package com.kiefer.ui.tabs.interfaces;

import java.util.ArrayList;

/** implemented by classes that will have tabs in their UI (not the UI class, see TabUIHoldable). All tabs are reused and recolored when needed. **/
public interface TabHolder {
    ArrayList<Tab> getTabs(int tierNo);
}
