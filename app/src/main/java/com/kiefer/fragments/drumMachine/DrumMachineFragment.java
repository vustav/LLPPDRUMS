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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kiefer.R;
import com.kiefer.fragments.TabFragment;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.DrumMachine;
import com.kiefer.popups.stackableManager.StackableManagerPopup;
import com.kiefer.popups.nameColor.NamePopup;
import com.kiefer.popups.sequencer.CopyFromPopup;
import com.kiefer.popups.sequencer.TempoPopup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.Tab;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

public class DrumMachineFragment extends TabFragment {
    //private static DrumMachineFragment drumMachineFragment;
    boolean startup = true; //only used once to set platIcon when switching from controller when the engine is playing

    //lockable UI (for disabling during playback)
    private ArrayList<View> lockableUI;

    //backgrounds for the buttons at the bottom to fill with gradients
    private LinearLayout tempoLayout, rndLayout, copyLayout, fxLayout;
    private FrameLayout divider1, divider2, divider3;

    //private ImageView playIcon;
    //private RelativeLayout pauseIcon;

    //name
    private TextView nameBtnTV;
    private RelativeLayout nameBtnGraphics;

    //tempo
    private CSpinnerButton tempoSpinnerBtn;

    private RelativeLayout sequenceBg;

    private Button removeSeqBtn;

    //subs
    //private CSpinnerButton subsSpinnerBtn;

    //ArrayList<FrameLayout> sequenceTabLayouts;

    //tabs
    //private ArrayList<TabGroup> tabGroupArray; //store the Tabs (a Tabs is a representation of the row/column)
    ArrayList<ViewGroup> tabBackgroundViews;

    private RecyclerView recyclerView;
    private SequenceAdapter adapter;
    private SequenceTouchHelper sequenceTouchHelper;

    public DrumMachineFragment() {
        // Required empty public constructor
        //drumMachineFragment = new DrumMachineFragment();
    }

    public static DrumMachineFragment newInstance() {
        //if(drumMachineFragment)
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

        tabBackgroundViews = new ArrayList<>();

        sequenceBg = rootView.findViewById(R.id.machineSequenceBg);

        //divider0 = rootView.findViewById(R.id.topFragmentDivider0);
        divider1 = rootView.findViewById(R.id.topFragmentDivider1);
        divider2 = rootView.findViewById(R.id.topFragmentDivider2);
        divider3 = rootView.findViewById(R.id.topFragmentDivider3);

        //setup the fx-recyclerView
        recyclerView = rootView.findViewById(R.id.machineSequenceTabsRecyclerView);

        // create a LinearLayoutManager and make it update borders and colors (this works both after drag and when adding/remocing viewHolders)
        recyclerView.setLayoutManager(new LinearLayoutManager(llppdrums, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutCompleted(final RecyclerView.State state) {
                super.onLayoutCompleted(state);
                if(!sequenceTouchHelper.isDragging()) {
                    adapter.updateBorders(llppdrums.getDrumMachine().getSelectedSequenceIndex());
                    adapter.updateTVColors(llppdrums.getDrumMachine().getSelectedSequenceIndex());
                }

                //only relevant when switching from controller if the engine is playing
                if(llppdrums.getEngineFacade().isPlaying() && startup) {
                    showPlayIcon(llppdrums.getDrumMachine().getPlayingSequenceIndex(), true);
                    startup = false;
                }
            }
        });

        //create an adapter
        adapter = new SequenceAdapter(llppdrums, this);

        //create and attach the ItemTouchHelper
        sequenceTouchHelper = new SequenceTouchHelper(llppdrums, adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(sequenceTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //add the adapter to the recyclerView
        recyclerView.setAdapter(adapter);

        //SEQUENCE MODULES
        FrameLayout sequenceModuleTabsLayout = rootView.findViewById(R.id.machineSequenceModuleTabsLayout);
        RelativeLayout sequenceModuleBg = rootView.findViewById(R.id.machineSequenceModuleBg);
        tabBackgroundViews.add(sequenceModuleBg);
        tabManager.createTabTier(callback.getTabs(0), callback, Tab.VERTICAL);
        sequenceModuleTabsLayout.addView(tabManager.getLinearLayout(0));

        //MODULE MODE
        FrameLayout moduleModeTabsLayout = rootView.findViewById(R.id.machineModuleModeTabsLayout);
        FrameLayout moduleModeBg = rootView.findViewById(R.id.machineModuleModeBg);
        tabBackgroundViews.add(moduleModeBg);
        tabManager.createTabTier(callback.getTabs(1), callback, Tab.VERTICAL);
        moduleModeTabsLayout.addView(tabManager.getLinearLayout(1));

        //sequencer
        //sequencer = new Sequencer(llppdrums);
        FrameLayout sequencerLayout = rootView.findViewById(R.id.machineSeqLayout);
        sequencerLayout.addView(llppdrums.getSequencerUI().getLayout());

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

        //fx
        Button fxBtn = rootView.findViewById(R.id.sequencerFxBtn);
        FrameLayout fxBtnGraphics = rootView.findViewById(R.id.sequencerFxBtnGraphics);
        LinearLayout fxGraphics = llppdrums.getDrumMachine().getSelectedSequence().getFxBtnGraphics();
        if (fxGraphics.getParent() != null) {
            ((ViewGroup) fxGraphics.getParent()).removeView(fxGraphics);
        }
        fxBtnGraphics.addView(fxGraphics);

        fxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StackableManagerPopup(llppdrums, llppdrums.getDrumMachine().getSelectedSequence().getStackableManager(), llppdrums.getDrumMachine().getSelectedSequence(), DrumMachine.FX_TYPE);
            }
        });
        fxLayout = rootView.findViewById(R.id.sequenceFxLayout);

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

        //remove
        removeSeqBtn = rootView.findViewById(R.id.sequenceRemoveBtn);
        removeSeqBtn.setOnClickListener(view -> {
            llppdrums.getDrumMachine().removeSelectedSequence();
            adapter.notifyDataSetChanged();
        });

        //add
        Button addBtn = rootView.findViewById(R.id.sequenceAddBtn);
        addBtn.setOnClickListener(view -> {
            llppdrums.getDrumMachine().addSequence(null);
            adapter.notifyDataSetChanged();
        });

        //set selected tabs
        llppdrums.getDrumMachine().selectSequence(llppdrums.getDrumMachine().getSelectedSequenceIndex());
        callback.onTabClicked(callback.getTabs(0).get(llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModuleIndex()));
        callback.onTabClicked(callback.getTabs(1).get(llppdrums.getDrumMachine().getSelectedSequence().getSelectedSequenceModule().getSelectedModeIndex()));

        return rootView;
    }

    public void showPlayIcon(final int i, final boolean show){
        llppdrums.runOnUiThread(() -> {
            if(show) {
                ((SequenceAdapter.SequenceTabViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i))).playIcon.setVisibility(View.VISIBLE);
            }
            else{
                ((SequenceAdapter.SequenceTabViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i))).playIcon.setVisibility(View.INVISIBLE);
            }
        });
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

    public void enableSeqBtn(boolean enabled){
        removeSeqBtn.setEnabled(enabled);
    }

    /** GET **/

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

    public void setFxGradient(GradientDrawable gradientDrawable) {
        fxLayout.setBackground(gradientDrawable);
    }

    public void setTempo(){
        if(tempoSpinnerBtn != null) {
            tempoSpinnerBtn.setSelection(Integer.toString(llppdrums.getDrumMachine().getSelectedSequence().getTempo()));
        }
    }

    public void setName(boolean updateAdapter){
        nameBtnTV.setText(llppdrums.getDrumMachine().getSelectedSequence().getName());
        if(updateAdapter) {
            adapter.notifyDataSetChanged();
        }

    }

    public void setColor(boolean updateAdapter){

        sequenceBg.setBackground(llppdrums.getDrumMachine().getSelectedSequence().getBackgroundGradient());

        int seqCcolor = llppdrums.getDrumMachine().getSelectedSequence().getColor();
        //divider0.setBackgroundColor(ColorUtils.getContrastColor(seqCcolor));
        divider1.setBackgroundColor(ColorUtils.getContrastColor(seqCcolor));
        divider2.setBackgroundColor(ColorUtils.getContrastColor(seqCcolor));
        divider3.setBackgroundColor(ColorUtils.getContrastColor(seqCcolor));
        nameBtnGraphics.setBackgroundColor(seqCcolor);
        nameBtnTV.setTextColor(ColorUtils.getContrastColor(seqCcolor));

        if(updateAdapter) {
            adapter.notifyDataSetChanged();
        }
    }

    //updates colors with the selected tab
    public void update(){
        setTempo();
        setName(false);
        setColor(false);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void setTabAppearances(int tier, ArrayList<Tab> tabs, int selectedTabNo){
        //tier++;

        for (int i = 0; i < tabs.size(); i++) {
            //set tab appearance
            //if(tier == 0){
            //tabManager.getLinearLayout(tier).getChildAt(i).findViewById(R.id.tabBg).setBackgroundColor(llppdrums.getDrumMachine().getSequences().get(i).getColor());
            //}
            //else {
            Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, tabs.get(i).getBitmapId(), i, tabs.size(), tabs.get(i).getOrientation());
            tabManager.getLinearLayout(tier).getChildAt(i).findViewById(R.id.tabBg).setBackground(new BitmapDrawable(llppdrums.getResources(), tabBitmap));
            //}

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
        //if(tier == 0) {
        //backgroundViews.get(tier).setBackground(llppdrums.getDrumMachine().getSelectedSequence().getBackgroundGradient());
        //}
        //else{
        Bitmap bgBitmap = ImgUtils.getBgBitmap(llppdrums, llppdrums.getDrumMachine().getSelectedSequence().getBitmapId(tier), llppdrums.getDrumMachine().getSelectedSequence().getOrientation());
        tabBackgroundViews.get(tier).setBackground(new BitmapDrawable(llppdrums.getResources(), bgBitmap));
        //}
    }
}