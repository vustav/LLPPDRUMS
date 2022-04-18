package com.kiefer.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.popups.sequencer.CopyFromPopup;
import com.kiefer.popups.sequencer.TempoPopup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.TabGroup;
import com.kiefer.ui.tabs.TabManagerPlay;
import com.kiefer.ui.tabs.interfaces.Tabable;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;

public class DrumMachineFragment extends TabFragment {
    private TabManagerPlay tabManagerPlay;

    //lockable UI (for disabling during playback)
    private ArrayList<View> lockableUI;

    //backgrounds for the buttons at the bottom to fill with gradients
    private LinearLayout tempoLayout, rndLayout, copyLayout;

    //tempo
    private CSpinnerButton tempoSpinnerBtn;

    //subs
    //private CSpinnerButton subsSpinnerBtn;

    //tabs
    private ArrayList<TabGroup> tabGroupArray; //store the Tabs (a Tabs is a representation of the row/column)
    ArrayList<ViewGroup> backgroundViews;

    //gfx
    private FrameLayout mixerBtnGraphics, fxBtnGraphics;

    //a little timer to prevent spamming with causes crashing
    //boolean randomizationRunning = false;

    public DrumMachineFragment() {
        // Required empty public constructor
    }

    public static DrumMachineFragment newInstance() {
        return new DrumMachineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanseState)
    {
        View view = provideView(inflater, container, savedInstanseState);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View provideView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lockableUI = new ArrayList<>();
        tabManagerPlay = new TabManagerPlay(llppdrums);

        View rootView = inflater.inflate(R.layout.fragment_drum_machine, container,false);

        tabGroupArray = new ArrayList<>();
        backgroundViews = new ArrayList<>();

        //set up the sequence tabs
        FrameLayout sequencerTabsLayout = rootView.findViewById(R.id.machineSequenceTabsLayout);
        RelativeLayout sequenceBg = rootView.findViewById(R.id.machineSequenceBg);
        backgroundViews.add(sequenceBg);
        TabGroup sequencerTabGroup = tabManagerPlay.getTabColumn(callback.getTabables(0), callback, 0, sequenceBg);
        tabGroupArray.add(sequencerTabGroup);
        sequencerTabsLayout.addView(sequencerTabGroup.getLayout());

        //set up the sequence module tabs
        FrameLayout sequenceModuleTabsLayout = rootView.findViewById(R.id.machineSequenceModuleTabsLayout);
        RelativeLayout sequenceModuleBg = rootView.findViewById(R.id.machineSequenceModuleBg);
        backgroundViews.add(sequenceModuleBg);
        TabGroup sequencerModuleTabGroup = tabManagerPlay.getTabColumn(callback.getTabables(1), callback, 1, sequenceModuleBg);
        tabGroupArray.add(sequencerModuleTabGroup);
        sequenceModuleTabsLayout.addView(sequencerModuleTabGroup.getLayout());

        //set up the module mode tabs
        FrameLayout moduleModeTabsLayout = rootView.findViewById(R.id.machineModuleModeTabsLayout);
        FrameLayout moduleModeBg = rootView.findViewById(R.id.machineModuleModeBg);
        backgroundViews.add(moduleModeBg);
        TabGroup moduleModeTabGroup = tabManagerPlay.getTabColumn(callback.getTabables(2), callback, 2, moduleModeBg);
        tabGroupArray.add(moduleModeTabGroup);
        moduleModeTabsLayout.addView(moduleModeTabGroup.getLayout());

        //sequencer
        //sequencer = new Sequencer(llppdrums);
        FrameLayout sequencerLayout = rootView.findViewById(R.id.machineSeqLayout);
        sequencerLayout.addView(llppdrums.getSequencer().getLayout());

        //stepsLayout = rootView.findViewById(R.id.sequencerRemoveAddStepLayout);

        //tempo
        tempoSpinnerBtn = new CSpinnerButton(llppdrums);
        tempoSpinnerBtn.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!uiLocked) {
                    new TempoPopup(llppdrums, tempoSpinnerBtn, llppdrums.getDrumMachine().getSelectedSequence());
                }
            }
        });
        lockableUI.add(tempoSpinnerBtn);

        //lower tempo
        Button lowerTempoBtn = rootView.findViewById(R.id.sequenceLowerTempoBtn);
        lowerTempoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().getSelectedSequence().lowerTempo();
            }
        });
        lockableUI.add(lowerTempoBtn);

        //raise tempo
        Button raiseTempoBtn = rootView.findViewById(R.id.sequenceRaiseTempoBtn);
        raiseTempoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().getSelectedSequence().raiseTempo();
            }
        });
        lockableUI.add(raiseTempoBtn);

        tempoLayout = rootView.findViewById(R.id.sequenceTempoLayout);

        FrameLayout tempoContainer = rootView.findViewById(R.id.sequenceTempoContainer);
        tempoContainer.addView(tempoSpinnerBtn);
        setTempo(llppdrums.getDrumMachine().getSelectedSequence().getTempo());

        //subs
        /*
        subsSpinnerBtn = new CSpinnerButton(llppdrums);
        subsSpinnerBtn.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!uiLocked) {
                    new SubsPopup(llppdrums, subsSpinnerBtn, llppdrums.getDrumMachine().getSelectedSequence());
                }
            }
        });
        lockableUI.add(subsSpinnerBtn);

        subsLayout = rootView.findViewById(R.id.sequenceSubsLayout);

        FrameLayout subsContainer = rootView.findViewById(R.id.sequenceSubsContainer);
        subsContainer.addView(subsSpinnerBtn);
        setSubs(llppdrums.getDrumMachine().getSelectedSequence().getSubs());

         */

        //set up the random options btn
        Button randomOptionsBtn = rootView.findViewById(R.id.sequenceRandomOptionsBtnBtn);
        randomOptionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llppdrums.getDrumMachine().openRandomOptionsPopup();
            }
        });

        //set up the randomize btn
        Button randomizeBtn = rootView.findViewById(R.id.sequenceRandomizeBtn);
        randomizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("DMFrag", "rndBtn()");
                /*
                if(!randomizationRunning) {
                    randomizationRunning = true;
                    new CountDownTimer(llppdrums.getResources().getInteger(R.integer.randomSwitchTimer), llppdrums.getResources().getInteger(R.integer.randomSwitchTimer)) {
                        public void onTick(long millisUntilFinished) {
                            //
                        }

                        public void onFinish() {
                            randomizationRunning = false;
                        }
                    }.start();

                    llppdrums.getDrumMachine().randomizeSelectedSequence();
                }

                 */
                llppdrums.getDrumMachine().randomizeSelectedSequence();
            }
        });
        rndLayout = rootView.findViewById(R.id.sequenceRndLayout);
        lockableUI.add(randomizeBtn);

        //set up the animation
        FrameLayout layout = rootView.findViewById(R.id.sequenceRandomizeBtnBg);
        int colorFrom = ColorUtils.getRandomColor();
        int colorTo = ColorUtils.getContrastColor(colorFrom);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                layout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();

        //copy spinner
        final CSpinnerButton copySpinnerButton = new CSpinnerButton(llppdrums);
        copySpinnerButton.seButtonWidth((int) llppdrums.getResources().getDimension(R.dimen.btnHeightSmall));
        copySpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!uiLocked) {
                    new CopyFromPopup(llppdrums, copySpinnerButton);
                }
            }
        });
        copySpinnerButton.setSelection(""); //will just add the arrow

        FrameLayout spinnerContainer = rootView.findViewById(R.id.sequenceCopyFromContainer);
        //cSpinnerButton.setSelection("COPY FROM");
        spinnerContainer.addView(copySpinnerButton);
        lockableUI.add(copySpinnerButton);

        copyLayout = rootView.findViewById(R.id.sequenceCopyLayout);

        //set selected tabs
        Integer[] selectedIndexes = llppdrums.getDrumMachine().getSelectedTabIndexes();

        callback.onTabClicked(sequencerTabGroup.getTabs().get(selectedIndexes[0]));
        callback.onTabClicked(sequencerModuleTabGroup.getTabs().get(selectedIndexes[1]));
        callback.onTabClicked(moduleModeTabGroup.getTabs().get(selectedIndexes[2]));

        return rootView;
    }

    private boolean uiLocked = false; //used to prevent spinner from opening when the UI is locked
    public void lockUI(){
        uiLocked = true;
        for(View v : lockableUI){
            //v.setAlpha(.7f);
            v.setEnabled(false);
        }
    }

    public void unlockUI(){
        uiLocked = false;
        for(View v : lockableUI){
            final View finalView = v;
            //llppdrums.runOnUiThread(new Runnable() {
                //public void run() {
                    //finalView.setAlpha(1f);
                    finalView.setEnabled(true);
                //}
            //});
        }
    }

    /** GET **/
    public ArrayList<TabGroup> getTabGroups(){
        return tabGroupArray;
    }

    public TabManagerPlay getTabManager(){
        return tabManagerPlay;
    }

    public void setCopyGradient(GradientDrawable gradientDrawable) {
        copyLayout.setBackgroundDrawable(gradientDrawable);
    }

    public void setRndGradient(GradientDrawable gradientDrawable) {
        rndLayout.setBackgroundDrawable(gradientDrawable);
    }

    public void setTempoGradient(GradientDrawable gradientDrawable) {
        tempoLayout.setBackground(gradientDrawable);
    }

    /*
    public void setSubsGradient(GradientDrawable gradientDrawable) {
        subsLayout.setBackground(gradientDrawable);
    }

     */

    /*
    public void setFxBtn(LinearLayout fxGraphics){
        fxBtnGraphics.addView(fxGraphics);
    }

    public void setMixerBtn(LinearLayout mixerGraphics){
        mixerBtnGraphics.addView(mixerGraphics);
    }

     */

    /** SET **/
    public void setTempo(int tempo){
        if(tempoSpinnerBtn != null) {
            tempoSpinnerBtn.setSelection(Integer.toString(tempo));
        }
    }

    /*
    public void setSubs(int subs){
        if(tempoSpinnerBtn != null) {
            subsSpinnerBtn.setSelection(Integer.toString(subs));
        }
    }

     */

    @Override
    public void setTabAppearances(int tier, ArrayList<Tabable> tabables, int selectedTabNo, Bitmap tabBitmap, Bitmap bgBitmap){

        for (int i = 0; i < tabables.size(); i++) {
            //set bitmap
            tabGroupArray.get(tier).getTabs().get(i).getBackground().setBackground(new BitmapDrawable(llppdrums.getResources(), tabables.get(i).getTabBitmap()));

            //set the inactive colors on the textView and lower alpha
            tabGroupArray.get(tier).getTabs().get(i).getTextView().setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtBgColor));
            tabGroupArray.get(tier).getTabs().get(i).getTextView().setTextColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtColor));
            tabGroupArray.get(tier).getTabs().get(i).getBackground().setAlpha(Float.parseFloat(llppdrums.getString(R.string.inactiveTabAlpha)));
        }

        //set the borders
        tabManager.setTabBorders(tabGroupArray.get(tier), selectedTabNo, TabManager.VERTICAL);

        //set the background for the module
        backgroundViews.get(tier).setBackground(new BitmapDrawable(llppdrums.getResources(), bgBitmap));

        //set active colors/full alpha to the selected tab
        tabGroupArray.get(tier).getTabs().get(selectedTabNo).getTextView().setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtBgColor));
        tabGroupArray.get(tier).getTabs().get(selectedTabNo).getTextView().setTextColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtColor));
        tabGroupArray.get(tier).getTabs().get(selectedTabNo).getBackground().setAlpha(1);
    }

    public ViewGroup getLastModuleBg(){
        return backgroundViews.get(backgroundViews.size() - 1);
    }
}
