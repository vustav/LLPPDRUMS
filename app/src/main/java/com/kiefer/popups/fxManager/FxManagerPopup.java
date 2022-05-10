package com.kiefer.popups.fxManager;

import android.graphics.Color;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

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
import com.kiefer.info.sequence.trackMenu.fxManager.FxManagerInfo;
import com.kiefer.machine.fx.FxManager;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.Popup;
import com.kiefer.popups.fxManager.auto.FxAutomationManagerAdapter;
import com.kiefer.automation.AutomationManagerTouchHelper;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.utils.ColorUtils;

public class FxManagerPopup extends Popup {
    final private FxManager fxManager;
    boolean start = true;

    //fx-recyclerView
    final private RecyclerView fxRecyclerView;
    final private FxManagerAdapter fxAdapter;

    //automation-recyclerView
    final private RecyclerView automationRecyclerView;
    final private FxAutomationManagerAdapter automationAdapter;
    private FxManagerTouchHelper fxTouchHelper;

    final private FrameLayout fxListBorder;

    //edit area
    final private FrameLayout editAreaBorder;
    final private FrameLayout editArea; //the layout where the fxs add their UI
    final private LinearLayout editAreaBg; //the layout with the editArea and shared UI, the reference is for coloring

    //automation
    FrameLayout automationLayout;

    //shared UI
    private final CSpinnerButton cSpinnerButton;
    private final CheckBox onCb;
    private final ImageView fxInfoBtn;

    public FxManagerPopup(final LLPPDRUMS llppdrums, final DrumTrack drumTrack){
        super(llppdrums);
        this.fxManager = drumTrack.getFxManager();

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_fx_manager, null);
        popupView.setBackground(ContextCompat.getDrawable(llppdrums, drumTrack.getFxManager().getFxManagerImgId()));

        //create the popupWindow
        int width = (int) llppdrums.getResources().getDimension(R.dimen.defaultSeekBarWidth) + (int) llppdrums.getResources().getDimension(R.dimen.autoViewHolderWidth) + (int) llppdrums.getResources().getDimension(R.dimen.fxViewHolderWidth) + (int) llppdrums.getResources().getDimension(R.dimen.marginLarge) * 5;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //setup the TV
        String name = llppdrums.getResources().getString(R.string.fxManagerLabel) + drumTrack.getTrackNo();
        TextView label = popupView.findViewById(R.id.fxManagerLabel);
        label.setText(name);
        int bgColor = drumTrack.getColor();
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

        //rnd
        Button rndBtn = popupView.findViewById(R.id.fxManagerRndBtn);
        rndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fxManager.randomizeAll();
                fxAdapter.notifyDataSetChanged();
                editArea.removeAllViews();

                if(fxManager.getFxs().size() > 0) {
                    selectFx(0);
                }
                setSharedUI();
            }
        });

        //setup the fx-recyclerView
        fxRecyclerView = popupView.findViewById(R.id.fxManagerFxsRecyclerView);

        //create an adapter
        fxAdapter = new FxManagerAdapter(llppdrums, this, fxRecyclerView);

        // create a LinearLayoutManager and make it update borders and colors (this works both after drag and when adding/remocing viewHolders)
        fxRecyclerView.setLayoutManager(new LinearLayoutManager(llppdrums, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutCompleted(final RecyclerView.State state) {
                super.onLayoutCompleted(state);
                if(!fxTouchHelper.isDragging()) {
                    if(fxManager.getFxs().size() > 0 && start) {
                        fxAdapter.updateBorders(fxManager.getSelectedFxIndex());
                    }
                }
            }
        });

        //create and attach the ItemTouchHelper
        fxTouchHelper = new FxManagerTouchHelper(fxAdapter);
        ItemTouchHelper fxHelper = new ItemTouchHelper(fxTouchHelper);
        fxHelper.attachToRecyclerView(fxRecyclerView);

        //add the adapter to the recyclerView
        fxRecyclerView.setAdapter(fxAdapter);

        //setup the add fx button
        Button addFxBtn = popupView.findViewById(R.id.fxManagerAddFxBtn);
        addFxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fxManager.createRandomFx(false);
                editArea.removeAllViews();
                editArea.addView(fxManager.getSelectedFx().getLayout());
                fxAdapter.notifyDataSetChanged();
                setSharedUI();

                //setAutomationVisibility();
            }
        });

        //automation and its recyclerView
        automationLayout = popupView.findViewById(R.id.fxManagerAutomationLayout);
        setAutomationVisibility();

        automationRecyclerView = popupView.findViewById(R.id.fxManagerAutomationRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        LinearLayoutManager automationLayoutManager = new LinearLayoutManager(llppdrums);
        automationLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        automationRecyclerView.setLayoutManager(automationLayoutManager);

        //create an adapter
        //automationAdapter = new FxAutomationManagerAdapter(llppdrums, this, fxManager.getSelectedFx().getAutomationManager());
        automationAdapter = new FxAutomationManagerAdapter(llppdrums, this);

        //create and attach the ItemTouchHelper
        ItemTouchHelper.Callback automationCallback = new AutomationManagerTouchHelper(automationAdapter);
        ItemTouchHelper automationHelper = new ItemTouchHelper(automationCallback);
        automationHelper.attachToRecyclerView(automationRecyclerView);

        //add the adapter to the recyclerView
        automationRecyclerView.setAdapter(automationAdapter);

        fxListBorder = popupView.findViewById(R.id.fxManagerListBorder);

        //setup the edit area
        editAreaBorder = popupView.findViewById(R.id.fxManagerSharedBorder);
        editArea = popupView.findViewById(R.id.fxManagerFxEditArea);
        editAreaBg = popupView.findViewById(R.id.fxManagerEditBg);

        //setup the add automation button
        Button addAutomationBtn = popupView.findViewById(R.id.fxManagerAddAutomationBtn);
        addAutomationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fxManager.getSelectedFx().getAutomationManager().addAutomation();
                automationAdapter.notifyDataSetChanged();
            }
        });

        //checkBox
        onCb = popupView.findViewById(R.id.fxManagerCheck);
        onCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fxManager.turnSelectedFxOn(onCb.isChecked());
            }
        });

        //set up the infoBtn
        fxInfoBtn = popupView.findViewById(R.id.fxManagerFxInfoBtn);
        fxInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, fxManager.getSelectedFx().getInfoKey());
            }
        });

        cSpinnerButton = new CSpinnerButton(llppdrums);
        cSpinnerButton.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FxListPopup(llppdrums, fxManager, FxManagerPopup.this, cSpinnerButton);
            }
        });
        FrameLayout spinnerContainer = popupView.findViewById(R.id.fxManagerCSpinnerContainer);
        spinnerContainer.addView(cSpinnerButton);

        //set a dismiss listener
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //fxManager.releaseFxViews();
            }
        });

        if(fxManager.getFxs().size() > 0) {
            selectFx(fxManager.getFxs().indexOf(fxManager.getSelectedFx()));
        }
        else{
            setSharedUI();
        }

        //set up the infoBtn
        ImageView infoBtn = popupView.findViewById(R.id.fxManagerInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, FxManagerInfo.key);
            }
        });

        //show the popup with a little offset
        show(popupWindow);

        //let drumTrack know that a window is being shown (and that it's not when dismissed)
        drumTrack.setFxManagerPopup(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                drumTrack.setFxManagerPopup(null);
            }
        });
    }

    private void setAutomationVisibility(){
        if(fxManager.getFxs().size() > 0){
            automationLayout.setVisibility(View.VISIBLE);
        }
        else{
            automationLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void setSharedUI(){
        if(fxManager.getFxs().size() > 0) {
            showSharedUI(true);
            onCb.setChecked(fxManager.getSelectedFx().isOn());
            cSpinnerButton.setSelection(fxManager.getSelectedFx().getName());
            automationAdapter.notifyDataSetChanged();
        }
        else{
            showSharedUI(false);
        }
        setAutomationVisibility();
    }

    public void showSharedUI(boolean show){
        if(show){
            fxListBorder.setVisibility(View.VISIBLE);
            editAreaBorder.setVisibility(View.VISIBLE);
            fxInfoBtn.setVisibility(View.VISIBLE);
            onCb.setVisibility(View.VISIBLE);
            cSpinnerButton.setVisibility(View.VISIBLE);
            //editAreaBg.setBackgroundColor(fxManager.getSelectedFx().getBgGradient());
            editAreaBg.setBackgroundDrawable(fxManager.getSelectedFx().getBgGradient());
            automationLayout.setBackground(ContextCompat.getDrawable(llppdrums, fxManager.getSelectedFx().getAutomationBgId()));
        }
        else{
            fxListBorder.setVisibility(View.INVISIBLE);
            editAreaBorder.setVisibility(View.INVISIBLE);
            fxInfoBtn.setVisibility(View.INVISIBLE);
            onCb.setVisibility(View.INVISIBLE);
            cSpinnerButton.setVisibility(View.INVISIBLE);
            editAreaBg.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void changeSelectedFx(int i){
        fxManager.changeSelectedFx(i);
        editArea.removeAllViews();
        editArea.addView(fxManager.getSelectedFx().getLayout());
        setSharedUI();
        fxAdapter.notifyDataSetChanged();
    }

    public void selectFx(int fxNo){
        editArea.removeAllViews();

        //select the next fx if possible, otherwise select the last. If none exists none will be selected.
        if(fxNo < fxManager.getFxs().size() - 1) {
            fxManager.setSelectedFx(fxNo);
        }
        else if(fxManager.getFxs().size() > 0){
            fxManager.setSelectedFx(fxManager.getFxs().size() - 1);
        }

        if(fxManager.getSelectedFx() != null) {
            if(fxManager.getSelectedFx().getLayout().getParent() != null){
                ((ViewGroup)fxManager.getSelectedFx().getLayout().getParent()).removeView(fxManager.getSelectedFx().getLayout());
            }
            editArea.addView(fxManager.getSelectedFx().getLayout());
            //fxManager.getSelectedFx().select();
        }

        setSharedUI();
    }

    public void moveFx(int from, int to){
        fxManager.moveFx(from, to);
    }

    public void removeFx(int fxNo){
        fxManager.removeFx(fxNo);
        fxAdapter.notifyDataSetChanged();
        selectFx(fxNo);
        setAutomationVisibility();
    }

    /** GET **/
    public FxManager getFxManager(){
        return fxManager;
    }

    public CheckBox getOnCb() {
        return onCb;
    }
}
