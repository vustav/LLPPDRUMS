package com.kiefer.ui.tabs;

import com.kiefer.LLPPDRUMS;
import com.kiefer.fragments.TabFragment;

public class TabManagerFragment extends TabManager{
    protected TabFragment tabFragment;

    public TabManagerFragment(LLPPDRUMS llppdrums, TabFragment tabFragment){
        super(llppdrums);
        this.tabFragment = tabFragment;
    }
}
