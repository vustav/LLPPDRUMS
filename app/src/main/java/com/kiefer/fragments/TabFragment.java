package com.kiefer.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiefer.LLPPDRUMS;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.Tab;

import java.util.ArrayList;

public abstract class TabFragment extends Fragment {

    public abstract void setTabAppearances(int tier, ArrayList<Tab> tabs, int selectedTabNo);

    //Used for communication.
    protected LLPPDRUMS llppdrums;

    protected TabManager tabManager;

    //See TABS INTERFACE at the bottom for an explanation. Could be solved by using the reference to LLPPDRUMS,
    //but I implemented this before I had that and it is nice to have
    protected TabManager.OnTabClickedListener callback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get a reference to LLPPDRUMS for communication purposes
        llppdrums = (LLPPDRUMS) getActivity();
        tabManager = new TabManager(llppdrums, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanseState)
    {
        View view = provideView(inflater, container, savedInstanseState);
        return view;
    }

    /** ABSTRACT METHODS **/
    //children inflates and sets up their views here. This is the only way I could get inheritance to work on fragments
    public abstract View provideView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    /** TabUtils.OnTabClickedListener **/
    public void setTabSelectedListener(TabManager.OnTabClickedListener callback) {
        this.callback = callback;
    }
}
