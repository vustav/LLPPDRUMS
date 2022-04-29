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
import com.kiefer.ui.tabs.interfaces.TabHoldable;
import com.kiefer.ui.tabs.interfaces.Tabable;

import java.util.ArrayList;

/** Only one set of tabs exist no matter how many Tabables they represent. Each Tabable holds a color(img?) that they recolor the tab representing them with when needed. **/

public class TabManager {

    public static final int VERTICAL = 0, HORIZONTAL = 1;

    //boolean tabClicked = false; //used to avoid spamming certain functions

    //Used for communication.
    protected LLPPDRUMS llppdrums;

    //See TABS INTERFACE at the bottom for an explanation. Could be solved by using the reference to LLPPDRUMS,
    //but I implemented this before I had that and it is nice to have
    //protected OnTabClickedListener callback;

    public TabManager(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
    }

    /** TABS **/
    public TabGroup getTabRow(ArrayList<Tabable> tabables, OnTabClickedListener callback, int tier, final ViewGroup moduleBackground){
        return createTabs(tabables, callback, tier, moduleBackground, HORIZONTAL);
    }

    public TabGroup getTabColumn(ArrayList<Tabable> tabables, OnTabClickedListener callback, int tier, final ViewGroup moduleBackground){
        return createTabs(tabables, callback, tier, moduleBackground, VERTICAL);
    }

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
            final Tab tab = new Tab(llppdrums, tabName, t.getBitmapId(), i, tier, orientation);
            tabsArray.add(tab);

            //add the tab to the tabs-layout
            tabsLayout.addView(singleTabLayout);

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
                    setTabBorders(tabGroup, tab.getN());

                    //call the listener
                    callback.onTabClicked(tab);

                    return true;
                }
            });
        }

        return tabGroup;
    }

    public void setTabBorders(TabGroup tabGroup, int selectedTab){
        //Log.e("TabManager", "setTabBorders()");

        //set up border sizes
        int largeBorderSize = (int) llppdrums.getResources().getDimension(R.dimen.tabsBorderLarge);
        int smallBorderSize = (int) llppdrums.getResources().getDimension(R.dimen.tabsBorderSmall);

        for(int i = 0; i < tabGroup.getTabs().size(); i++) {

            //set padding on the selected tab
            if (selectedTab == i) {

                if(tabGroup.getTabs().get(selectedTab).getOrientation() == VERTICAL) {
                    int topBorderSize;

                    //the first vertical tab always has a small topPadding, the others have a large
                    if (selectedTab == 0) {
                        topBorderSize = smallBorderSize;
                    } else {
                        topBorderSize = largeBorderSize;
                    }

                    //the bottom padding on selected verticals is always small. Since the top for
                    // default unselected tabs are small as well they will "blend" and look large.
                    //tabGroup.getTabs().get(selectedTab).getBorder().setPadding(largeBorderSize, topBorderSize, 0, smallBorderSize);
                    tabGroup.getLayout().getChildAt(selectedTab).findViewById(R.id.tabBorder).setPadding(largeBorderSize, topBorderSize, 0, smallBorderSize);

                }
                else{
                    int leftBorderSize;
                    if (selectedTab == 0) {
                        leftBorderSize = 0;
                    } else {
                        leftBorderSize = smallBorderSize;
                    }

                    int rightBorderSize;
                    if (selectedTab == tabGroup.getTabs().size() - 1) {
                        rightBorderSize = 0;
                    } else {
                        rightBorderSize = smallBorderSize;
                    }
                    //tabGroup.getTabs().get(selectedTab).getBorder().setPadding(leftBorderSize, largeBorderSize, rightBorderSize, 0);
                    tabGroup.getLayout().getChildAt(selectedTab).findViewById(R.id.tabBorder).setPadding(leftBorderSize, largeBorderSize, rightBorderSize, 0);
                }
            }
            //set the padding on all the non-selected tabs
            else {
                if(tabGroup.getTabs().get(selectedTab).getOrientation() == VERTICAL) {
                    //the last tab gets a small bottom, the others get 0. This means that only the
                    // top will divide unselected tabs. For selected they have a small bottom that will "blend" with this top to show as a large.
                    if(i == tabGroup.getTabs().size() - 1){
                        //tabGroup.getTabs().get(i).getBorder().setPadding(smallBorderSize, smallBorderSize, largeBorderSize, smallBorderSize);
                        tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabBorder).setPadding(smallBorderSize, smallBorderSize, largeBorderSize, smallBorderSize);
                    }
                    else{
                        //tabGroup.getTabs().get(i).getBorder().setPadding(smallBorderSize, smallBorderSize, largeBorderSize, 0);
                        tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabBorder).setPadding(smallBorderSize, smallBorderSize, largeBorderSize, 0);
                    }
                }
                else{
                    if(i == 0){
                        //tabGroup.getTabs().get(i).getBorder().setPadding(0, smallBorderSize, smallBorderSize, largeBorderSize);
                        tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabBorder).setPadding(0, smallBorderSize, smallBorderSize, largeBorderSize);
                    }
                    else if(i == tabGroup.getTabs().size() - 1){
                        //tabGroup.getTabs().get(i).getBorder().setPadding(smallBorderSize, smallBorderSize, 0, largeBorderSize);
                        tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabBorder).setPadding(smallBorderSize, smallBorderSize, 0, largeBorderSize);
                    }
                    else{
                        //tabGroup.getTabs().get(i).getBorder().setPadding(smallBorderSize, smallBorderSize, smallBorderSize, largeBorderSize);
                        tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabBorder).setPadding(smallBorderSize, smallBorderSize, smallBorderSize, largeBorderSize);
                    }
                }
            }
        }
    }
/*
    public void removeTopBorder(TabGroup tabGroup){
        for(int i = 0; i < tabGroup.getTabs().size(); i++) {
            int paddingLeft = tabGroup.getTabs().get(i).getBorder().getPaddingLeft();
            int paddingRight = tabGroup.getTabs().get(i).getBorder().getPaddingRight();
            int paddingBottom = tabGroup.getTabs().get(i).getBorder().getPaddingBottom();
            tabGroup.getTabs().get(i).getBorder().setPadding(paddingLeft, 0, paddingRight, paddingBottom);
        }

    }

 */

    /** LISTENER INTERFACE **/
    public interface OnTabClickedListener extends TabHoldable {
        void onTabClicked(Tab tab);
    }
}
