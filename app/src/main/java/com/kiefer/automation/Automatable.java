package com.kiefer.automation;

import java.util.ArrayList;

public interface Automatable {
    public boolean isOn();
    float turnOnAutoValue(String param, float autoValue, boolean popupShowing); //return the original value
    void turnOffAutoValue(String param, float oldValue, boolean popupShowing);
    ArrayList<String> getParams();
}
