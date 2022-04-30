package com.kiefer.ui.tabs;

import android.content.Context;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.fragments.TabFragment;
import com.kiefer.ui.tabs.interfaces.TabHoldable;
import com.kiefer.ui.tabs.interfaces.Tabable;

import java.util.ArrayList;

/** Only one set of tabs exist no matter how many Tabables they represent. Each Tabable holds a color(img?) that they recolor the tab representing them with when needed. **/

public class TabManager {

    public static final int VERTICAL = 0, HORIZONTAL = 1;

    //boolean tabClicked = false; //used to avoid spamming certain functions

    //Used for communication.
    protected LLPPDRUMS llppdrums;
    protected TabFragment tabFragment;


    private ArrayList<LinearLayout> tabLayouts;

    //See TABS INTERFACE at the bottom for an explanation. Could be solved by using the reference to LLPPDRUMS,
    //but I implemented this before I had that and it is nice to have
    //protected OnTabClickedListener callback;

    public TabManager(LLPPDRUMS llppdrums, TabFragment tabFragment){
        this.llppdrums = llppdrums;
        this.tabFragment = tabFragment;
        tabLayouts = new ArrayList<>();
    }

    /** TABS **/
    public void createTabRow(ArrayList<Tabable> tabables, OnTabClickedListener callback, int tier, final ViewGroup moduleBackground){
        createTabTier(tabables, callback, tier, moduleBackground, HORIZONTAL);
    }

    public void createTabColumn(ArrayList<Tabable> tabables, OnTabClickedListener callback, int tier, final ViewGroup moduleBackground){
        createTabTier(tabables, callback, tier, moduleBackground, VERTICAL);
    }

    public void createTabTier(ArrayList<Tabable> tabables, final OnTabClickedListener callback, int tier, final View moduleBackground, final int orientation){

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

        //final TabGroup tabGroup = new TabGroup(tabsLayout, tabsArray);

        for(int i = 0; i < tabables.size(); i++){
            Tabable t = tabables.get(i);
            final String tabName = t.getName();

            //inflate the right layout for the tab
            ViewGroup singleTabLayout;
            if(orientation == VERTICAL){
                singleTabLayout = (ViewGroup) llppdrums.getLayoutInflater().inflate(R.layout.tab_single_vertical, null);
            }
            else{
                singleTabLayout = (ViewGroup) llppdrums.getLayoutInflater().inflate(R.layout.tab_single_horizontal, null);
            }

            //setup params
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            singleTabLayout.setLayoutParams(llp);

            //Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, t.getBitmapId(), i, tabables.size(), orientation);
            //background.setBackground(new BitmapDrawable(llppdrums.getResources(), tabBitmap));

            //set up the textView
            TextView textView = singleTabLayout.findViewById(R.id.tabTxt);
            textView.setText(tabName);

            //store the tab to access data later
            //final Tab tab = new Tab(tabName, t.getBitmapId(), i, tier, orientation);
            //tabsArray.add(tab);

            //add the tab to the tabs-layout
            tabsLayout.addView(singleTabLayout);

            int finalI = i;

            //set a listener
            singleTabLayout.setOnTouchListener(new View.OnTouchListener() {
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
                    setTabBorders(tier, tabables, finalI);

                    //call the listener
                    callback.onTabClicked(tabables.get(finalI));

                    return true;
                }
            });
        }

        //return tabGroup;
        tabLayouts.add(tabsLayout);
    }

    public LinearLayout getTabsLayout(int tier){
        return tabLayouts.get(tier);
    }

    public void setTabBorders(int tier, ArrayList<Tabable> tabables, int selectedTab){

        //set up border sizes
        int largeBorderSize = (int) llppdrums.getResources().getDimension(R.dimen.tabsBorderLarge);
        int smallBorderSize = (int) llppdrums.getResources().getDimension(R.dimen.tabsBorderSmall);

        for(int i = 0; i < tabables.size(); i++) {

            //set padding on the selected tab
            if (selectedTab == i) {

                if(tabables.get(selectedTab).getOrientation() == VERTICAL) {
                    int topBorderSize;

                    //the first vertical tab always has a small topPadding, the others have a large
                    if (selectedTab == 0) {
                        topBorderSize = smallBorderSize;
                    } else {
                        topBorderSize = largeBorderSize;
                    }

                    //the bottom padding on selected verticals is always small. Since the top for
                    // default unselected tabs are small as well they will "blend" and look large.
                    tabLayouts.get(tier).getChildAt(selectedTab).findViewById(R.id.tabBorder).setPadding(largeBorderSize, topBorderSize, 0, smallBorderSize);

                }
                else{
                    int leftBorderSize;
                    if (selectedTab == 0) {
                        leftBorderSize = 0;
                    } else {
                        leftBorderSize = smallBorderSize;
                    }

                    int rightBorderSize;
                    if (selectedTab == tabables.size() - 1) {
                        rightBorderSize = 0;
                    } else {
                        rightBorderSize = smallBorderSize;
                    }
                    tabLayouts.get(tier).getChildAt(selectedTab).findViewById(R.id.tabBorder).setPadding(leftBorderSize, largeBorderSize, rightBorderSize, 0);
                }
            }
            //set the padding on all the non-selected tabs
            else {
                if(tabables.get(selectedTab).getOrientation() == VERTICAL) {
                    //the last tab gets a small bottom, the others get 0. This means that only the
                    // top will divide unselected tabs. For selected they have a small bottom that will "blend" with this top to show as a large.
                    if(i == tabables.size() - 1){
                        tabLayouts.get(tier).getChildAt(i).findViewById(R.id.tabBorder).setPadding(smallBorderSize, smallBorderSize, largeBorderSize, smallBorderSize);
                    }
                    else{
                        tabLayouts.get(tier).getChildAt(i).findViewById(R.id.tabBorder).setPadding(smallBorderSize, smallBorderSize, largeBorderSize, 0);
                    }
                }
                else{
                    if(i == 0){
                        tabLayouts.get(tier).getChildAt(i).findViewById(R.id.tabBorder).setPadding(0, smallBorderSize, smallBorderSize, largeBorderSize);
                    }
                    else if(i == tabables.size() - 1){
                        tabLayouts.get(tier).getChildAt(i).findViewById(R.id.tabBorder).setPadding(smallBorderSize, smallBorderSize, 0, largeBorderSize);
                    }
                    else{
                        tabLayouts.get(tier).getChildAt(i).findViewById(R.id.tabBorder).setPadding(smallBorderSize, smallBorderSize, smallBorderSize, largeBorderSize);
                    }
                }
            }
        }
    }

    /** LISTENER INTERFACE **/
    public interface OnTabClickedListener extends TabHoldable {
        void onTabClicked(Tabable tabable);
    }
}
