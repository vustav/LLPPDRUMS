package com.kiefer.fragments.drumMachine;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kiefer.R;
import com.kiefer.fragments.TabFragment;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.popups.nameColor.NamePopup;
import com.kiefer.popups.sequencer.CopyFromPopup;
import com.kiefer.popups.sequencer.TempoPopup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.TabGroup;
import com.kiefer.ui.tabs.interfaces.Tabable;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

public class DrumMachineFragment extends TabFragment {
    //private TabManagerPlay tabManagerPlay;

    //lockable UI (for disabling during playback)
    private ArrayList<View> lockableUI;

    //backgrounds for the buttons at the bottom to fill with gradients
    private LinearLayout tempoLayout, rndLayout, copyLayout;

    //name
    private TextView nameBtnTV;
    private RelativeLayout nameBtnGraphics;

    //tempo
    private CSpinnerButton tempoSpinnerBtn;

    //subs
    //private CSpinnerButton subsSpinnerBtn;

    //tabs
    private ArrayList<TabGroup> tabGroupArray; //store the Tabs (a Tabs is a representation of the row/column)
    ArrayList<ViewGroup> backgroundViews;

    private RecyclerView recyclerView;
    private SequenceAdapter adapter;

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
        //tabManagerPlay = new TabManager(llppdrums);

        View rootView = inflater.inflate(R.layout.fragment_drum_machine, container,false);

        tabGroupArray = new ArrayList<>();
        backgroundViews = new ArrayList<>();

        //set up the sequence tabs
/*
        //setup the fx-recyclerView
        recyclerView = rootView.findViewById(R.id.machineSequenceTabsRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(llppdrums);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        final ArrayList<Tab> tabsArray = new ArrayList<>();
        //final Tab tab = new Tab("0", ImgUtils.getRandomImageId(), 0, 0, background, border, textView);
        //tabsArray.add(tab);
        //final Tab tab = new Tab(tabName, t.getBitmapId(), i, tier, background, border, textView);
        final Tab tab = new Tab(llppdrums, "0", ImgUtils.getRandomImageId(), 0, 0, TabManager.VERTICAL);
        tabsArray.add(tab);
        final Tab tab1 = new Tab(llppdrums, "1", ImgUtils.getRandomImageId(), 1, 0, TabManager.VERTICAL);
        tabsArray.add(tab1);

        //create the tabs
        RelativeLayout sequenceBg = rootView.findViewById(R.id.machineSequenceBg);
        backgroundViews.add(sequenceBg);
        //TabGroup sequencerTabGroup = tabManager.getTabColumn(callback.getTabables(0), callback, 0, sequenceBg);
        //tabGroupArray.add(sequencerTabGroup);

        //create an adapter
        adapter = new SequenceAdapter(llppdrums, tabsArray);

        //add the adapter to the recyclerView
        recyclerView.setAdapter(adapter);

 */

        FrameLayout sequencerTabsLayout = rootView.findViewById(R.id.machineSequenceTabsLayout);
        RelativeLayout sequenceBg = rootView.findViewById(R.id.machineSequenceBg);
        backgroundViews.add(sequenceBg);
        TabGroup sequencerTabGroup = tabManager.getTabColumn(callback.getTabables(0), callback, 0, sequenceBg);
        tabGroupArray.add(sequencerTabGroup);
        sequencerTabsLayout.addView(sequencerTabGroup.getLayout());

        //add playIcons to the first tier
        addPlayIcons();

        //set up the sequence module tabs
        FrameLayout sequenceModuleTabsLayout = rootView.findViewById(R.id.machineSequenceModuleTabsLayout);
        RelativeLayout sequenceModuleBg = rootView.findViewById(R.id.machineSequenceModuleBg);
        backgroundViews.add(sequenceModuleBg);
        TabGroup sequencerModuleTabGroup = tabManager.getTabColumn(callback.getTabables(1), callback, 1, sequenceModuleBg);
        tabGroupArray.add(sequencerModuleTabGroup);
        sequenceModuleTabsLayout.addView(sequencerModuleTabGroup.getLayout());

        //set up the module mode tabs
        FrameLayout moduleModeTabsLayout = rootView.findViewById(R.id.machineModuleModeTabsLayout);
        FrameLayout moduleModeBg = rootView.findViewById(R.id.machineModuleModeBg);
        backgroundViews.add(moduleModeBg);
        TabGroup moduleModeTabGroup = tabManager.getTabColumn(callback.getTabables(2), callback, 2, moduleModeBg);
        tabGroupArray.add(moduleModeTabGroup);
        moduleModeTabsLayout.addView(moduleModeTabGroup.getLayout());

        //sequencer
        //sequencer = new Sequencer(llppdrums);
        FrameLayout sequencerLayout = rootView.findViewById(R.id.machineSeqLayout);
        sequencerLayout.addView(llppdrums.getSequencer().getLayout());

        //stepsLayout = rootView.findViewById(R.id.sequencerRemoveAddStepLayout);


        //name/color
        nameBtnTV = rootView.findViewById(R.id.machineSequenceNameBtnTV);
        nameBtnGraphics = rootView.findViewById(R.id.machineSequenceNameBtnGraphics);
        rootView.findViewById(R.id.machineSequenceNameBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new NamePopup(llppdrums, llppdrums.getDrumMachine().getSelectedSequence());
                //popupWindow.dismiss();
            }
        });

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
        //setTempo(llppdrums.getDrumMachine().getSelectedSequence().getTempo());

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

    /** PLAY **/
    private void addPlayIcons(){
        /*
        for(Tab t : tabGroupArray.get(0).getTabs()){
            ImageView iv = new ImageView(llppdrums);
            iv.setImageDrawable(ContextCompat.getDrawable(llppdrums, R.drawable.drawable_triangle_red));

            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(flp);

            iv.setVisibility(View.INVISIBLE);

            t.getBackground().addView(iv);
        }

         */
    }

    public void showPlayIcon(final int i, final boolean show){
        /*
        //playIcons.get(i).post(new Runnable() { //modify the View in the UI thread
            //public void run() {
                if(show) {
                    tabGroupArray.get(0).getTabs().get(i).getBackground().getChildAt(tabGroupArray.get(0).getTabs().get(i).getBackground().getChildCount()-1).setVisibility(View.VISIBLE);
                    //playIcons.get(i).setVisibility(View.VISIBLE);
                }
                else{
                    //playIcons.get(i).setVisibility(View.INVISIBLE);
                    tabGroupArray.get(0).getTabs().get(i).getBackground().getChildAt(tabGroupArray.get(0).getTabs().get(i).getBackground().getChildCount()-1).setVisibility(View.INVISIBLE);
                }
            //}
        //});

         */
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

    /** SET **/

    public TabManager getTabManager(){
        return tabManager;
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

    public void setTempo(){
        if(tempoSpinnerBtn != null) {
            tempoSpinnerBtn.setSelection(Integer.toString(llppdrums.getDrumMachine().getSelectedSequence().getTempo()));
        }
    }

    public void setName(){
        nameBtnTV.setText(llppdrums.getDrumMachine().getSelectedSequence().getName());
        ((TextView)tabGroupArray.get(0).getLayout().getChildAt(llppdrums.getDrumMachine().getSelectedSequenceIndex()).findViewById(R.id.tabTxt)).setText(llppdrums.getDrumMachine().getSelectedSequence().getName());

    }

    public void setColor(){
        nameBtnTV.setTextColor(ColorUtils.getContrastColor(llppdrums.getDrumMachine().getSelectedSequence().getColor()));
        nameBtnGraphics.setBackgroundColor(llppdrums.getDrumMachine().getSelectedSequence().getColor());
        tabGroupArray.get(0).getLayout().getChildAt(llppdrums.getDrumMachine().getSelectedSequenceIndex()).findViewById(R.id.tabBg).setBackgroundColor(llppdrums.getDrumMachine().getSelectedSequence().getColor());
        backgroundViews.get(0).setBackground(llppdrums.getDrumMachine().getSelectedSequence().getBackgroundGradient());
    }

    public void update(){
        setTempo();
        setName();
        setColor();
    }

    @Override
    public void setTabAppearances(int tier, ArrayList<Tabable> tabables, int selectedTabNo){

        for (int i = 0; i < tabables.size(); i++) {
            //set tab appearance
            if(tier == 0){
                tabGroupArray.get(tier).getLayout().getChildAt(i).findViewById(R.id.tabBg).setBackgroundColor(llppdrums.getDrumMachine().getSequences().get(i).getColor());
            }
            else {
                Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, tabables.get(i).getBitmapId(), i, tabables.size(), tabables.get(i).getOrientation());
                tabGroupArray.get(tier).getLayout().getChildAt(i).findViewById(R.id.tabBg).setBackground(new BitmapDrawable(llppdrums.getResources(), tabBitmap));
            }

            //set the inactive colors on the textView and lower alpha
            tabGroupArray.get(tier).getLayout().getChildAt(i).findViewById(R.id.tabTxt).setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtBgColor));
            ((TextView)tabGroupArray.get(tier).getLayout().getChildAt(i).findViewById(R.id.tabTxt)).setTextColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtColor));
            tabGroupArray.get(tier).getLayout().getChildAt(i).findViewById(R.id.tabBg).setAlpha(Float.parseFloat(llppdrums.getString(R.string.inactiveTabAlpha)));
        }

        //set the borders
        tabManager.setTabBorders(tabGroupArray.get(tier), selectedTabNo);

        //set active colors/full alpha to the selected tab
        ((TextView)tabGroupArray.get(tier).getLayout().getChildAt(selectedTabNo).findViewById(R.id.tabTxt)).setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtBgColor));
        ((TextView)tabGroupArray.get(tier).getLayout().getChildAt(selectedTabNo).findViewById(R.id.tabTxt)).setTextColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtColor));
        tabGroupArray.get(tier).getLayout().getChildAt(selectedTabNo).findViewById(R.id.tabBg).setAlpha(1);

        //set the background for the module
        if(tier == 0) {
            backgroundViews.get(tier).setBackground(llppdrums.getDrumMachine().getSelectedSequence().getBackgroundGradient());
        }
        else{
            Bitmap bgBitmap = ImgUtils.getBgBitmap(llppdrums, llppdrums.getDrumMachine().getSelectedSequence().getBitmapId(tier), llppdrums.getDrumMachine().getSelectedSequence().getOrientation());
            backgroundViews.get(tier).setBackground(new BitmapDrawable(llppdrums.getResources(), bgBitmap));
        }
    }
}