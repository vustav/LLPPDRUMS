package com.kiefer.popups.stackableManager;

import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.info.sequence.trackMenu.fxManager.StackableManagerInfo;
import com.kiefer.machine.sequence.track.Stackables.StackableManager;
import com.kiefer.machine.sequence.track.Stackables.Stacker;
import com.kiefer.popups.Popup;
import com.kiefer.popups.stackableManager.auto.StackableAutomationManagerAdapter;
import com.kiefer.automation.AutomationManagerTouchHelper;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

public class StackableManagerPopup extends Popup {
    final private StackableManager stackableManager;
    boolean start = true;

    //fx-recyclerView
    final private RecyclerView stackableReclerView;
    final private StackableManagerAdapter stackableAdapter;

    //automation-recyclerView
    final private RecyclerView automationRecyclerView;
    final private StackableAutomationManagerAdapter automationAdapter;
    private StackableManagerTouchHelper stackableTouchHelper;

    final private FrameLayout stackableListBorder;

    //edit area
    final private FrameLayout editAreaBorder;
    final private FrameLayout editArea; //the layout where the fxs add their UI
    final private LinearLayout editAreaBg; //the layout with the editArea and shared UI, the reference is for coloring

    //automation
    FrameLayout automationLayout;

    //UI
    protected LinearLayout customArea;

    private final CSpinnerButton cSpinnerButton;
    private final CheckBox onCb;
    private final ImageView fxInfoBtn;

    public StackableManagerPopup(final LLPPDRUMS llppdrums, final StackableManager stackableManager, Stacker stacker, int type){
        super(llppdrums);
        this.stackableManager = stackableManager;

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_stackable_manager, null);
        popupView.setBackground(ContextCompat.getDrawable(llppdrums, stackableManager.getBgImgId()));

        customArea = popupView.findViewById(R.id.stackableManagerCustomArea);

        //create the popupWindow
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //set a margin on the window since we want to max its size
        Display display = llppdrums.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        popupWindow.setWidth(size.x-size.x/13);
        popupWindow.setHeight(size.y-size.y/20);
        popupWindow.setFocusable(true);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //setup the TV
        String name = stackableManager.getName() + stacker.getName();
        TextView label = popupView.findViewById(R.id.stackableManagerLabel);
        label.setText(name);
        label.setId(View.generateViewId());
        int bgColor = stacker.getColor();
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

        //rnd
        Button rndBtn = popupView.findViewById(R.id.stackableManagerRndBtn);
        rndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackableManager.randomizeAll(false);
                stackableAdapter.notifyDataSetChanged();
                editArea.removeAllViews();

                if(stackableManager.getStackables().size() > 0) {
                    selectStackable(0);
                }
                setSharedUI();
            }
        });

        //setup the fx-recyclerView
        stackableReclerView = popupView.findViewById(R.id.stackableManagerStackablesRecyclerView);

        //create an adapter
        stackableAdapter = new StackableManagerAdapter(llppdrums, this, stackableReclerView);

        // create a LinearLayoutManager and make it update borders and colors (this works both after drag and when adding/remocing viewHolders)
        stackableReclerView.setLayoutManager(new LinearLayoutManager(llppdrums, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutCompleted(final RecyclerView.State state) {
                super.onLayoutCompleted(state);
                if(!stackableTouchHelper.isDragging()) {
                    if(stackableManager.getStackables().size() > 0 && start) {
                        stackableAdapter.updateBorders(stackableManager.getSelectedStackableIndex());
                    }
                }
            }
        });

        //create and attach the ItemTouchHelper
        stackableTouchHelper = new StackableManagerTouchHelper(stackableAdapter);
        ItemTouchHelper stackableHelper = new ItemTouchHelper(stackableTouchHelper);
        stackableHelper.attachToRecyclerView(stackableReclerView);

        //add the adapter to the recyclerView
        stackableReclerView.setAdapter(stackableAdapter);

        //setup the add fx button
        Button addFxBtn = popupView.findViewById(R.id.stackableManagerAddStackableBtn);
        addFxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackableManager.createRandomStackable(false);
                editArea.removeAllViews();
                editArea.addView(stackableManager.getSelectedStackable().getLayout());
                stackableAdapter.notifyDataSetChanged();
                setSharedUI();

                //setAutomationVisibility();
            }
        });

        //automation and its recyclerView
        automationLayout = popupView.findViewById(R.id.stackableManagerAutomationLayout);
        setAutomationVisibility();

        automationRecyclerView = popupView.findViewById(R.id.stackableManagerAutomationRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        LinearLayoutManager automationLayoutManager = new LinearLayoutManager(llppdrums);
        automationLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        automationRecyclerView.setLayoutManager(automationLayoutManager);

        //create an adapter
        //automationAdapter = new FxAutomationManagerAdapter(llppdrums, this, fxManager.getSelectedFx().getAutomationManager());
        automationAdapter = new StackableAutomationManagerAdapter(llppdrums, this);

        //create and attach the ItemTouchHelper
        ItemTouchHelper.Callback automationCallback = new AutomationManagerTouchHelper(automationAdapter);
        ItemTouchHelper automationHelper = new ItemTouchHelper(automationCallback);
        automationHelper.attachToRecyclerView(automationRecyclerView);

        //add the adapter to the recyclerView
        automationRecyclerView.setAdapter(automationAdapter);

        stackableListBorder = popupView.findViewById(R.id.stackableManagerListBorder);

        //setup the edit area
        editAreaBorder = popupView.findViewById(R.id.stackableManagerSharedBorder);
        editArea = popupView.findViewById(R.id.stackableManagerStackableEditArea);
        editAreaBg = popupView.findViewById(R.id.stackableManagerEditBg);

        //setup the add automation button
        Button addAutomationBtn = popupView.findViewById(R.id.stackableManagerAddAutomationBtn);
        addAutomationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackableManager.getSelectedStackable().getAutomationManager().addAutomation();
                automationAdapter.notifyDataSetChanged();
            }
        });

        //checkBox
        onCb = popupView.findViewById(R.id.stackableManagerCheck);
        onCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stackableManager.turnSelectedStackableOn(onCb.isChecked());
            }
        });

        //set up the infoBtn
        fxInfoBtn = popupView.findViewById(R.id.stackableManagerStackableInfoBtn);
        fxInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, stackableManager.getSelectedStackable().getInfoKey());
            }
        });

        cSpinnerButton = new CSpinnerButton(llppdrums);
        cSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StackableListPopup(llppdrums, stackableManager, StackableManagerPopup.this, cSpinnerButton);
            }
        });
        FrameLayout spinnerContainer = popupView.findViewById(R.id.stackableManagerCSpinnerContainer);
        spinnerContainer.addView(cSpinnerButton);

        //set a dismiss listener
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        if(stackableManager.getStackables().size() > 0) {
            selectStackable(stackableManager.getStackables().indexOf(stackableManager.getSelectedStackable()));
        }
        else{
            setSharedUI();
        }

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.stackableManagerInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, StackableManagerInfo.key);
            }
        });

        //show the popup with a little offset
        show(popupWindow);

        //let drumTrack know that a window is being shown (and that it's not when dismissed)
        stacker.setStackableManagerPopup(this, type);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                stacker.setStackableManagerPopup(null, type);
            }
        });
    }

    private void setAutomationVisibility(){
        if(stackableManager.getStackables().size() > 0){
            automationLayout.setVisibility(View.VISIBLE);
        }
        else{
            automationLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setSharedUI(){
        if(stackableManager.getStackables().size() > 0) {
            showSharedUI(true);
            onCb.setChecked(stackableManager.getSelectedStackable().isOn());
            cSpinnerButton.setSelection(stackableManager.getSelectedStackable().getName());
            automationAdapter.notifyDataSetChanged();
        }
        else{
            showSharedUI(false);
        }
        setAutomationVisibility();
    }

    public void showSharedUI(boolean show){
        if(show){
            stackableListBorder.setVisibility(View.VISIBLE);
            editAreaBorder.setVisibility(View.VISIBLE);
            fxInfoBtn.setVisibility(View.VISIBLE);
            onCb.setVisibility(View.VISIBLE);
            cSpinnerButton.setVisibility(View.VISIBLE);
            //editAreaBg.setBackgroundColor(fxManager.getSelectedFx().getBgGradient());
            editAreaBg.setBackgroundDrawable(stackableManager.getSelectedStackable().getBgGradient());
            automationLayout.setBackground(ContextCompat.getDrawable(llppdrums, stackableManager.getSelectedStackable().getAutomationBgId()));
        }
        else{
            stackableListBorder.setVisibility(View.INVISIBLE);
            editAreaBorder.setVisibility(View.INVISIBLE);
            fxInfoBtn.setVisibility(View.INVISIBLE);
            onCb.setVisibility(View.INVISIBLE);
            cSpinnerButton.setVisibility(View.INVISIBLE);
            editAreaBg.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void changeSelected(int i){
        stackableManager.changeSelectedStackable(i);
        editArea.removeAllViews();
        editArea.addView(stackableManager.getSelectedStackable().getLayout());
        setSharedUI();
        stackableAdapter.notifyDataSetChanged();
    }

    public void selectStackable(int n){
        editArea.removeAllViews();

        //select the next fx if possible, otherwise select the last. If none exists none will be selected.
        if(n < stackableManager.getStackables().size() - 1) {
            stackableManager.setSelectedStackable(n, 2);
        }
        else if(stackableManager.getStackables().size() > 0){
            stackableManager.setSelectedStackable(stackableManager.getStackables().size() - 1, 3);
        }

        if(stackableManager.getSelectedStackable() != null) {
            if(stackableManager.getSelectedStackable().getLayout().getParent() != null){
                ((ViewGroup) stackableManager.getSelectedStackable().getLayout().getParent()).removeView(stackableManager.getSelectedStackable().getLayout());
            }
            editArea.addView(stackableManager.getSelectedStackable().getLayout());
            //fxManager.getSelectedFx().select();
        }

        setSharedUI();
    }

    public void moveStackable(int from, int to){
        stackableManager.moveStackable(from, to);
    }

    public void removeStackable(int no){
        stackableManager.removeStackable(no);
        stackableAdapter.notifyDataSetChanged();
        selectStackable(no);
        setAutomationVisibility();
    }

    public void updateAdapter(){
        stackableAdapter.notifyDataSetChanged();
    }

    /** GET **/
    public StackableManager getStackableManager(){
        return stackableManager;
    }

    public CheckBox getOnCb() {
        return onCb;
    }
}
