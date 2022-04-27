package com.kiefer.ui.tabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.MotionEvent;
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

    private final boolean colorsOnOuter = false;

    public TabManagerPlay(LLPPDRUMS llppdrums) {
        super(llppdrums);
        icons = new ArrayList<>();
    }

    /*
    @Override
    public TabGroup getTabColumn(ArrayList<Tabable> tabables, OnTabClickedListener callback, int tier, final ViewGroup moduleBackground){
        return createTabs(tabables, callback, tier, moduleBackground, VERTICAL);
    }

     */

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

            if(tier == 0 && colorsOnOuter){
                background.setBackgroundColor(llppdrums.getDrumMachine().getSelectedSequence().getTabColor());
            }
            else {
                Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, t.getBitmapId(), i, tabables.size(), orientation);
                background.setBackground(new BitmapDrawable(llppdrums.getResources(), tabBitmap));
            }

            //set up the textView
            TextView textView = singleTabLayout.findViewById(R.id.tabTxt);
            textView.setText(tabName);

            //save the icon in the arrayList on tier 0
            if(tier == 0) {
                icons.add(singleTabLayout.findViewById(R.id.tabIcon));
            }

            //add the tab to the tabs-layout
            tabsLayout.addView(singleTabLayout);

            //store the tab to access data later
            //Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, t.getBitmapId(), i, tabables.size(), orientation);
            Tab tab;
            if(tier == 0 && colorsOnOuter){
                tab = new Tab(tabName, llppdrums.getDrumMachine().getSelectedSequence().getTabColor(), i, tier, background, border, textView);
            }
            else {
                tab = new Tab(tabName, t.getBitmapId(), i, tier, background, border, textView);
            }
            tabsArray.add(tab);

            //set a listener
            background.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {

                    if(llppdrums.getProjectOptionsManager().vibrateOnTouch()){
                        if(event.getAction() == MotionEvent.ACTION_DOWN) {
                            Vibrator v = (Vibrator) llppdrums.getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(llppdrums.getResources().getInteger(R.integer.vibrateInMs));
                        }
                    }

                    //set the drawable of the moduleBackground
                    //Bitmap bgBitmap = ImgUtils.getBgBitmap(llppdrums, t.getBitmapId(), orientation);
                    //moduleBackground.setBackground(new BitmapDrawable(llppdrums.getResources(), bgBitmap));

                    //set borders for all tabs in the View
                    setTabBorders(tabGroup, tab.getN(), orientation);

                    //call the listener
                    callback.onTabClicked(tab);

                    return true;
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
