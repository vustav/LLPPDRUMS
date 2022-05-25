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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kiefer.R;
import com.kiefer.info.main.MainInfo;
import com.kiefer.options.projectOptions.ProjectOptionsManager;
import com.kiefer.popups.files.LoadPopup;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.popups.projectOptions.ProjectOptionsPopup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.Tab;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

public class TopFragment extends TabFragment {
    private ProjectOptionsManager projectOptionsManager;

    //private TabGroup tabGroup;

    private LinearLayout bg;
    private FrameLayout divider0, divider1;

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
        ImageView playIcon = rootView.findViewById(R.id.topPlayIcon);
        RelativeLayout pauseIcon = rootView.findViewById(R.id.topPauseIcon);

        Button playPauseBtn = rootView.findViewById(R.id.topPlayBtn);
        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llppdrums.getEngineFacade().isPlaying()){
                    llppdrums.getDrumMachine().pause();
                    playIcon.setVisibility(View.VISIBLE);
                    pauseIcon.setVisibility(View.INVISIBLE);
                }
                else {
                    llppdrums.getDrumMachine().play();
                    playIcon.setVisibility(View.INVISIBLE);
                    pauseIcon.setVisibility(View.VISIBLE);
                }
            }
        });

        //set up the stop btn
        Button stopBtn = rootView.findViewById(R.id.topStopBtn);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().stop();
                playIcon.setVisibility(View.VISIBLE);
                pauseIcon.setVisibility(View.INVISIBLE);
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
        tabManager.createTabTier(callback.getTabs(0), callback, Tab.HORIZONTAL);
        tabsLayout.addView(tabManager.getLinearLayout(0));


        //set DrumMachine as default
        callback.onTabClicked(callback.getTabs(0).get(0));

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

        //set the gradient
        bg.setBackground(gd);
    }

    /** GET/SET **/

    @Override
    public void setTabAppearances(int tier, ArrayList<Tab> tabs, int selectedTabNo){

        try {
            tabManager.setTabBorders(tier, tabs, selectedTabNo);
        }
        catch(Exception e){
            Log.e("TopFragment", e.getMessage());
            String message = "AJAJAJ";
            Toast toast = Toast.makeText(llppdrums,
                    message, Toast.LENGTH_SHORT);
            toast.show();
        }

        for(int i = 0; i < tabs.size(); i++){

            Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, tabs.get(i).getBitmapId(), i, tabs.size(), tabs.get(i).getOrientation());
            //tabGroup.getLayout().getChildAt(i).findViewById(R.id.tabBg).setBackground(new BitmapDrawable(llppdrums.getResources(), tabBitmap));
            tabManager.getLinearLayout(0).getChildAt(i).findViewById(R.id.tabBg).setBackground(new BitmapDrawable(llppdrums.getResources(), tabBitmap));

            tabManager.getLinearLayout(0).getChildAt(i).findViewById(R.id.tabTxt).setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtBgColor));
            ((TextView)tabManager.getLinearLayout(0).getChildAt(i).findViewById(R.id.tabTxt)).setTextColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtColor));
        }

        //set active colors/full alpha to the selected tab
        tabManager.getLinearLayout(0).getChildAt(selectedTabNo).findViewById(R.id.tabTxt).setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtBgColor));
        ((TextView)tabManager.getLinearLayout(0).getChildAt(selectedTabNo).findViewById(R.id.tabTxt)).setTextColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtColor));

        //set the background for the module
        Bitmap bgBitmap = ImgUtils.getBgBitmap(llppdrums, tabs.get(selectedTabNo).getBitmapId(), Tab.HORIZONTAL);
        llppdrums.getBackground().setBackground(new BitmapDrawable(getResources(), bgBitmap));
    }
}
