package com.kiefer.files.keepers;

import java.util.ArrayList;

public class AutomationKeeper implements Keeper {
    public boolean on;
    public String param;
    public String value;
    public ArrayList<Boolean> steps;

    public AutomationKeeper(){
    }
}
