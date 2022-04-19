package com.kiefer.ui.tabs;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.ui.tabs.interfaces.Tabable;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

public class TabManagerPlay extends TabManager {
    private ArrayList<ImageView> icons;

    public TabManagerPlay(LLPPDRUMS llppdrums) {
        super(llppdrums);
        icons = new ArrayList<>();
    }

    @Override
    public TabGroup getTabColumn(ArrayList<Tabable> tabables, OnTabClickedListener callback, int tier, final ViewGroup moduleBackground){
        return createTabs(tabables, callback, tier, moduleBackground, VERTICAL);
    }

    @Override
    public TabGroup createTabs(ArrayList<Tabable> tabables, final OnTabClickedListener callback, int tier, final View moduleBackground, final int orientation){

        final ArrayList<Tab> tabsArray = new ArrayList<>();

        //inflate the right layout to put the tabs in
        LinearLayout tabsLayout;
        if(orientation == VERTICAL){
            tabsLayout = (LinearLayout) llppdrums.getLayoutInflater().inflate(R.layout.tab_column, null);
        }
        else{
            tabsLayout = (LinearLayout) llppdrums.getLayoutInflater().inflate(R.layout.tab_row, null);
        }

        tabsLayout.setWeightSum(tabables.size());
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tabsLayout.setLayoutParams(flp);

        final TabGroup tabGroup = new TabGroup(tabsLayout, tabsArray);

        for(int i = 0; i<tabables.size(); i++){
            Tabable t = tabables.get(i);
            final String tabName = t.getLabel();

            //inflate the right layout for the tab
            ViewGroup singleTabLayout;
            if(orientation == VERTICAL){
                singleTabLayout = (ViewGroup) llppdrums.getLayoutInflater().inflate(R.layout.tab_single_vertical_play_icon, null);
            }
            else{
                singleTabLayout = (ViewGroup) llppdrums.getLayoutInflater().inflate(R.layout.tab_single_horizontal_play_icon, null);
            }

            //setup params
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            singleTabLayout.setLayoutParams(llp);

            //get the tho views that Tab needs
            FrameLayout border = singleTabLayout.findViewById(R.id.tabBorder);
            RelativeLayout background = singleTabLayout.findViewById(R.id.tabBg);

            Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, t.getBitmapId(), i, tabables.size(), orientation);
            background.setBackground(new BitmapDrawable(llppdrums.getResources(), tabBitmap));

            //set up the textView
            TextView textView = singleTabLayout.findViewById(R.id.tabTxt);
            textView.setText(tabName);

            //save the icon in the arrayList on tier 0
            if(tier == 0) {
                icons.add((ImageView) singleTabLayout.findViewById(R.id.tabIcon));
            }

            //add the tab to the tabs-layout
            tabsLayout.addView(singleTabLayout);

            //store the tab to access data later
            //Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, t.getBitmapId(), i, tabables.size(), orientation);
            final Tab tab = new Tab(tabName, t.getBitmapId(), i, tier, background, border, textView);
            tabsArray.add(tab);

            //set a listener
            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //start with a little timer to prevent spamming
                    /*
                    if(!tabClicked) {
                        tabClicked = true;
                        new CountDownTimer(llppdrums.getResources().getInteger(R.integer.tabSwitchTimer), llppdrums.getResources().getInteger(R.integer.tabSwitchTimer)) {
                            public void onTick(long millisUntilFinished) {
                                //
                            }

                            public void onFinish() {
                                tabClicked = false;
                            }
                        }.start();

                        //set the drawable of the moduleBackground
                        moduleBackground.setBackground(new BitmapDrawable(llppdrums.getResources(), tab.getBitmap()));

                        //set borders for all tabs in the View
                        setTabBorders(tabGroup, tab.getN(), orientation);

                        //call the listener
                        callback.onTabClicked(tab);
                    }

                     */

                    //set the drawable of the moduleBackground
                    Bitmap bgBitmap = ImgUtils.getBgBitmap(llppdrums, t.getBitmapId(), orientation);
                    moduleBackground.setBackground(new BitmapDrawable(llppdrums.getResources(), bgBitmap));

                    //set borders for all tabs in the View
                    setTabBorders(tabGroup, tab.getN(), orientation);

                    //call the listener
                    callback.onTabClicked(tab);
                }
            });
        }

        return tabGroup;
    }

    public void showIcon(final int i, final boolean show){
        icons.get(i).post(new Runnable() { //modify the View in the UI thread
            public void run() {
                if(show) {
                    icons.get(i).setVisibility(View.VISIBLE);
                }
                else{
                    icons.get(i).setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
