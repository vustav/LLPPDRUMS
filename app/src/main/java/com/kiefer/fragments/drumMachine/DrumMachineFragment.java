package com.kiefer.fragments.drumMachine;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiefer.R;
import com.kiefer.fragments.TabFragment;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.popups.fxManager.FxManagerTouchHelper;
import com.kiefer.popups.nameColor.NamePopup;
import com.kiefer.popups.sequencer.CopyFromPopup;
import com.kiefer.popups.sequencer.TempoPopup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.Tab;
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

    ArrayList<FrameLayout> sequenceTabLayouts;

    //tabs
    //private ArrayList<TabGroup> tabGroupArray; //store the Tabs (a Tabs is a representation of the row/column)
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

        View rootView = inflater.inflate(R.layout.fragment_drum_machine, container,false);

        backgroundViews = new ArrayList<>();

        //SEQUENCE
        FrameLayout sequencerTabsLayout = rootView.findViewById(R.id.machineSequenceTabsLayout);
        RelativeLayout sequenceBg = rootView.findViewById(R.id.machineSequenceBg);
        backgroundViews.add(sequenceBg);
        tabManager.createTabTier(callback.getTabs(0), callback, 0, Tab.VERTICAL);
        sequencerTabsLayout.addView(tabManager.getLinearLayout(0));

        //RECYCLER
        sequenceTabLayouts = new ArrayList<>();
        for(int i = 0; i < tabManager.getLinearLayout(0).getChildCount(); i++){
            sequenceTabLayouts.add((FrameLayout) tabManager.getLinearLayout(0).getChildAt(i));
        }

        //add playIcons to the first tier
        addPlayIcons();

        //setup the fx-recyclerView
        recyclerView = rootView.findViewById(R.id.machineSequenceTabsRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(llppdrums);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //create an adapter
        adapter = new SequenceAdapter(llppdrums);

        //create and attach the ItemTouchHelper
        ItemTouchHelper.Callback fxCallback = new SequenceTouchHelper(adapter);
        ItemTouchHelper fxHelper = new ItemTouchHelper(fxCallback);
        fxHelper.attachToRecyclerView(recyclerView);

        //add the adapter to the recyclerView
        recyclerView.setAdapter(adapter);

        //PLAY
        addPlayIcons();

        //SEQUENCE MODULES
        FrameLayout sequenceModuleTabsLayout = rootView.findViewById(R.id.machineSequenceModuleTabsLayout);
        RelativeLayout sequenceModuleBg = rootView.findViewById(R.id.machineSequenceModuleBg);
        backgroundViews.add(sequenceModuleBg);
        tabManager.createTabTier(callback.getTabs(1), callback, 1, Tab.VERTICAL);
        sequenceModuleTabsLayout.addView(tabManager.getLinearLayout(1));

        //MODULE MODE
        FrameLayout moduleModeTabsLayout = rootView.findViewById(R.id.machineModuleModeTabsLayout);
        FrameLayout moduleModeBg = rootView.findViewById(R.id.machineModuleModeBg);
        backgroundViews.add(moduleModeBg);
        tabManager.createTabTier(callback.getTabs(2), callback, 2, Tab.VERTICAL);
        moduleModeTabsLayout.addView(tabManager.getLinearLayout(2));

        //sequencer
        //sequencer = new Sequencer(llppdrums);
        FrameLayout sequencerLayout = rootView.findViewById(R.id.machineSeqLayout);
        sequencerLayout.addView(llppdrums.getSequencer().getLayout());

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
        spinnerContainer.addView(copySpinnerButton);
        lockableUI.add(copySpinnerButton);

        copyLayout = rootView.findViewById(R.id.sequenceCopyLayout);

        //set selected tabs
        callback.onTabClicked(callback.getTabs(0).get(0));
        callback.onTabClicked(callback.getTabs(1).get(0));
        callback.onTabClicked(callback.getTabs(2).get(0));

        return rootView;
    }

    /** PLAY **/
    private void addPlayIcons(){
        //for(int i = 0; i < tabManager.getLinearLayout(0).getChildCount(); i++){
        for(int i = 0; i < sequenceTabLayouts.size(); i++){

            ImageView iv = new ImageView(llppdrums);

            Drawable drawable = ContextCompat.getDrawable(llppdrums, R.drawable.drawable_triangle_red);
            iv.setImageDrawable(drawable);

            FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(flp);

            iv.setVisibility(View.INVISIBLE);

            sequenceTabLayouts.get(i).addView(iv);
        }
    }

    public void showPlayIcon(final int i, final boolean show){
        FrameLayout tabLayout = (FrameLayout)tabManager.getLinearLayout(0).getChildAt(i);
        if(show) {
            tabLayout.getChildAt(tabLayout.getChildCount()-1).setVisibility(View.VISIBLE);
        }
        else{
            tabLayout.getChildAt(tabLayout.getChildCount()-1).setVisibility(View.INVISIBLE);
        }
    }

    private boolean uiLocked = false; //used to prevent spinner from opening when the UI is locked
    public void lockUI(){
        uiLocked = true;
        for(View v : lockableUI){
            v.setEnabled(false);
        }
    }

    public void unlockUI(){
        uiLocked = false;
        for(View v : lockableUI){
            final View finalView = v;
            finalView.setEnabled(true);
        }
    }

    /** GET **/
    public ArrayList<FrameLayout> getSequenceTabLayouts() {
        return sequenceTabLayouts;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

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

    private void setName(){
        ((TextView)tabManager.getLinearLayout(0).getChildAt(llppdrums.getDrumMachine().getSelectedSequenceIndex()).findViewById(R.id.tabTxt)).setText(llppdrums.getDrumMachine().getSelectedSequence().getName());

        nameBtnTV.setText(llppdrums.getDrumMachine().getSelectedSequence().getName());
    }

    public void setBtnName(){
        nameBtnTV.setText(llppdrums.getDrumMachine().getSelectedSequence().getName());
        //((TextView)tabManager.getTabsLayout(0).getChildAt(llppdrums.getDrumMachine().getSelectedSequenceIndex()).findViewById(R.id.tabTxt)).setText(llppdrums.getDrumMachine().getSelectedSequence().getName());

    }

    public void setTabName(int tabIndex){
        //nameBtnTV.setText(llppdrums.getDrumMachine().getSelectedSequence().getName());
        ((TextView)tabManager.getLinearLayout(0).getChildAt(tabIndex).findViewById(R.id.tabTxt)).setText(llppdrums.getDrumMachine().getSequences().get(tabIndex).getName());

    }

    private void setColor(){
        tabManager.getLinearLayout(0).getChildAt(llppdrums.getDrumMachine().getSelectedSequenceIndex()).findViewById(R.id.tabBg).setBackgroundColor(llppdrums.getDrumMachine().getSelectedSequence().getColor());

        nameBtnTV.setTextColor(ColorUtils.getContrastColor(llppdrums.getDrumMachine().getSelectedSequence().getColor()));
        nameBtnGraphics.setBackgroundColor(llppdrums.getDrumMachine().getSelectedSequence().getColor());
        backgroundViews.get(0).setBackground(llppdrums.getDrumMachine().getSelectedSequence().getBackgroundGradient());

    }

    public void setBgColor(){
        nameBtnTV.setTextColor(ColorUtils.getContrastColor(llppdrums.getDrumMachine().getSelectedSequence().getColor()));
        nameBtnGraphics.setBackgroundColor(llppdrums.getDrumMachine().getSelectedSequence().getColor());
        backgroundViews.get(0).setBackground(llppdrums.getDrumMachine().getSelectedSequence().getBackgroundGradient());
    }

    public void setTabColor(int tabIndex){
        tabManager.getLinearLayout(0).getChildAt(tabIndex).findViewById(R.id.tabBg).setBackgroundColor(llppdrums.getDrumMachine().getSequences().get(tabIndex).getColor());
    }

    //updates colors with the selected tab
    public void update(){
        setTempo();
        setName();
        setColor();
    }

    @Override
    public void setTabAppearances(int tier, ArrayList<Tab> tabs, int selectedTabNo){
        //tier++;

        for (int i = 0; i < tabs.size(); i++) {
            //set tab appearance
            if(tier == 0){
                tabManager.getLinearLayout(tier).getChildAt(i).findViewById(R.id.tabBg).setBackgroundColor(llppdrums.getDrumMachine().getSequences().get(i).getColor());
            }
            else {
                Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, tabs.get(i).getBitmapId(), i, tabs.size(), tabs.get(i).getOrientation());
                tabManager.getLinearLayout(tier).getChildAt(i).findViewById(R.id.tabBg).setBackground(new BitmapDrawable(llppdrums.getResources(), tabBitmap));
            }

            //set the inactive colors on the textView and lower alpha
            tabManager.getLinearLayout(tier).getChildAt(i).findViewById(R.id.tabTxt).setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtBgColor));
            ((TextView)tabManager.getLinearLayout(tier).getChildAt(i).findViewById(R.id.tabTxt)).setTextColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtColor));
            tabManager.getLinearLayout(tier).getChildAt(i).findViewById(R.id.tabBg).setAlpha(Float.parseFloat(llppdrums.getString(R.string.inactiveTabAlpha)));
        }

        //set the borders
        tabManager.setTabBorders(tier, tabs, selectedTabNo);

        //set active colors/full alpha to the selected tab
        ((TextView)tabManager.getLinearLayout(tier).getChildAt(selectedTabNo).findViewById(R.id.tabTxt)).setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtBgColor));
        ((TextView)tabManager.getLinearLayout(tier).getChildAt(selectedTabNo).findViewById(R.id.tabTxt)).setTextColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtColor));
        tabManager.getLinearLayout(tier).getChildAt(selectedTabNo).findViewById(R.id.tabBg).setAlpha(1);

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
