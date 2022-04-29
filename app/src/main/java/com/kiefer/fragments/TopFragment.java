package com.kiefer.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.R;
import com.kiefer.info.main.MainInfo;
import com.kiefer.options.projectOptions.ProjectOptionsManager;
import com.kiefer.popups.files.LoadPopup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.popups.projectOptions.ProjectOptionsPopup;
import com.kiefer.ui.tabs.Tab;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.TabGroup;
import com.kiefer.ui.tabs.interfaces.Tabable;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

public class TopFragment extends TabFragment {
    private ProjectOptionsManager projectOptionsManager;

    private TabGroup tabGroup;

    private LinearLayout bg;
    private FrameLayout divider0, divider1, divider2;

    //private boolean changeSeqRunning = false; //used to avoid spamming certain functions

    public TopFragment() {
        // Required empty public constructor
    }

    public static TopFragment newInstance() {
        return new TopFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View provideView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_top, container, false);

        //set the gradient as the bg
        bg = rootView.findViewById(R.id.topFragmentBg);
        divider0 = rootView.findViewById(R.id.topFragmentDivider0);
        divider1 = rootView.findViewById(R.id.topFragmentDivider1);
        divider2 = rootView.findViewById(R.id.topFragmentDivider2);
        randomizeBgGradient();

        //set up the infoBtn
        ImageView infoBtn = rootView.findViewById(R.id.topInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, MainInfo.key);
            }
        });

        //set up the play btn
        Button playBtn = rootView.findViewById(R.id.topPlayBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().play();
            }
        });

        //set up the pause btn
        Button pauseBtn = rootView.findViewById(R.id.topPauseBtn);
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().pause();
            }
        });

        //set up the stop btn
        Button stopBtn = rootView.findViewById(R.id.topStopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().stop();
            }
        });

        //options
        Button optionsBtn = rootView.findViewById(R.id.topOptionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ProjectOptionsPopup(llppdrums, projectOptionsManager);
            }
        });

        //load
        Button loadBtn = rootView.findViewById(R.id.topLoadBtn);
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoadPopup(llppdrums);
            }
        });
        if(llppdrums.disableLoad){
            loadBtn.setEnabled(false);
        }

        //save
        Button saveBtn = rootView.findViewById(R.id.topSaveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("TopFragment", "save btn");
                llppdrums.getKeeperFileHandler().writePromptName(llppdrums.getKeeper(), llppdrums.getSavedProjectsFolderPath());
            }
        });
        if(llppdrums.disableLoad){
            saveBtn.setEnabled(false);
        }

        //clear
        Button clearBtn = rootView.findViewById(R.id.topClearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().getSelectedSequence().reset(true);
            }
        });

        //sequence manager
        FrameLayout seqManagerLayout = rootView.findViewById(R.id.topFragmentSequenceManagerLayout);
        seqManagerLayout.addView(llppdrums.getDrumMachine().getSequenceManager().getView());

        //set up the tabs
        FrameLayout tabsLayout = rootView.findViewById(R.id.topTabsLayout);
        FrameLayout mainBg = llppdrums.findViewById(R.id.mainBg);
        tabGroup = tabManager.getTabRow(callback.getTabables(0), callback, 0, mainBg);
        tabsLayout.addView(tabGroup.getLayout());


        //set DrumMachine as default
        callback.onTabClicked(tabGroup.getTabs().get(0));

        return rootView;
    }

    public void setProjectOptionsManager(ProjectOptionsManager projectOptionsManager){
        this.projectOptionsManager = projectOptionsManager;
    }

    public void randomizeBgGradient(){

        //create a gradient for the bg. Save the first color and use it for the dividers.
        int gdColor1 = ColorUtils.getRandomColor();
        GradientDrawable gd = ColorUtils.getRandomGradientDrawable(gdColor1, ColorUtils.getRandomColor());

        //set the color of the dividers
        int dividerColor = ColorUtils.getContrastColor(gdColor1);
        divider0.setBackgroundColor(dividerColor);
        divider1.setBackgroundColor(dividerColor);
        divider2.setBackgroundColor(dividerColor);

        //set the gradient
        bg.setBackground(gd);
    }

    /*
    private void changeSequence(int sequence){
        //start with a little timer to prevent spamming
        if(!changeSeqRunning) {
            changeSeqRunning = true;
            new CountDownTimer(llppdrums.getResources().getInteger(R.integer.sequenceSwitchTimer), llppdrums.getResources().getInteger(R.integer.sequenceSwitchTimer)) {
                public void onTick(long millisUntilFinished) {
                    //
                }

                public void onFinish() {
                    changeSeqRunning = false;
                }
            }.start();
            llppdrums.getDrumMachine().changePlayingSequence(sequence);
        }
    }

     */

    /** GET/SET **/

    @Override
    public void setTabAppearances(int tier, ArrayList<Tabable> tabables, int selectedTabNo){
        Log.e("TopFragment", "setTabAppearances()");
        tabManager.setTabBorders(tabGroup, selectedTabNo);

        //Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, tabGroup.getTabs().get(selectedTabNo).getBitmapId(), selectedTabNo, tabables.size(), tabables.get(selectedTabNo).getOrientation());
        //tabGroup.getLayout().getChildAt(selectedTabNo).findViewById(R.id.tabBg).setBackground(new BitmapDrawable(llppdrums.getResources(), tabBitmap));
/*
        for(Tab t : tabGroup.getTabs()){

            //set the inactive colors on the textView and lower alpha
            t.getTextView().setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtBgColor));
            t.getTextView().setTextColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtColor));
            t.getBackground().setAlpha(Float.parseFloat(llppdrums.getString(R.string.inactiveTabAlpha)));
        }

 */
        for(int i = 0; i < tabGroup.getLayout().getChildCount(); i++){

            Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, tabGroup.getTabs().get(i).getBitmapId(), i, tabables.size(), tabables.get(i).getOrientation());
            tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabBg).setBackground(new BitmapDrawable(llppdrums.getResources(), tabBitmap));

            tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabTxt).setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtBgColor));
            ((TextView)tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabTxt)).setTextColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtColor));
            //tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabBg).setAlpha(Float.parseFloat(llppdrums.getString(R.string.inactiveTabAlpha)));
            //tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabBg).setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtBgColor));
        }

        //set active colors/full alpha to the selected tab
        tabGroup.getLayout().getChildAt(selectedTabNo).findViewById(R.id.tabTxt).setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtBgColor));
        ((TextView)tabGroup.getLayout().getChildAt(selectedTabNo).findViewById(R.id.tabTxt)).setTextColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtColor));
        //tabGroup.getLayout().getChildAt(selectedTabNo).findViewById(R.id.tabBg).getBackground().setAlpha(1);
        //tabGroup.getLayout().getChildAt(selectedTabNo).findViewById(R.id.tabBg).setBackgroundColor(llppdrums.getResources().getColor(R.color.defaultArrowColor));

        //set the background for the module
        Bitmap bgBitmap = ImgUtils.getBgBitmap(llppdrums, tabGroup.getTabs().get(selectedTabNo).getBitmapId(), TabManager.HORIZONTAL);
        llppdrums.getBackground().setBackground(new BitmapDrawable(getResources(), bgBitmap));
    }
}
