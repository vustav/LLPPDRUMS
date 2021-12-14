package com.kiefer.popups.fxManager.auto;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.automation.AutomationManagerAdapter;
import com.kiefer.automation.AutomationParamsListPopup;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.automation.AutomationManager;
import com.kiefer.automation.AutomationStepsPopup;
import com.kiefer.popups.fxManager.FxManagerPopup;

/**
 * Provide views to RecyclerView with data from  a dataSet.
 */
public class FxAutomationManagerAdapter extends AutomationManagerAdapter{
    private final FxManagerPopup fxManagerPopup;
    private final int floatMultiplier;
    private CSpinnerButton paramBtn;

    public FxAutomationManagerAdapter(LLPPDRUMS llppdrums, FxManagerPopup fxManagerPopup) {
        super(llppdrums);
        this.fxManagerPopup = fxManagerPopup;
        floatMultiplier = llppdrums.getResources().getInteger(R.integer.floatMultiplier);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final AutomationViewHolder autoViewHolder, final int position) {
        final AutomationManager automationManager = fxManagerPopup.getFxManager().getSelectedFx().getAutomationManager();
        final int automationIndex = autoViewHolder.getAdapterPosition();

        //set the bgColor
        //autoViewHolder.bgIV.setBackground(ContextCompat.getDrawable(llppdrums, automationManager.getAutomationBgGradient(automationIndex)));
        autoViewHolder.bgIV.setBackgroundDrawable(automationManager.getAutomationBgGradient(automationIndex));

        //setup the checkbox
        autoViewHolder.checkBox.setOnClickListener(null); //set to null to not trigger the old listener when setting checked
        autoViewHolder.checkBox.setChecked(automationManager.getAutomations().get(automationIndex).isOn());
        autoViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                automationManager.setOn(automationIndex, autoViewHolder.checkBox.isChecked());
            }
        });

        //remove the old spinner
        autoViewHolder.spinnerLayout.removeAllViews();

        //and add a new
        /*
        final CSpinnerButton paramBtn;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AutomationParamsListPopup(llppdrums, automationManager, FxAutomationManagerAdapter.this, automationIndex, paramBtn);
            }
        };

         */
        paramBtn = new CSpinnerButton(llppdrums);
        paramBtn.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AutomationParamsListPopup(llppdrums, automationManager, FxAutomationManagerAdapter.this, automationIndex, paramBtn);
            }
        });
        paramBtn.setSelection(automationManager.getParam(automationIndex));
        /*
        paramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AutomationParamsListPopup(llppdrums, automationManager, FxAutomationManagerAdapter.this, automationIndex, paramBtn);
            }
        });

         */
        autoViewHolder.spinnerLayout.addView(paramBtn);

        //set up the slider
        if(automationManager.getParam(automationIndex).equals(llppdrums.getResources().getString(R.string.fxOn))){ //hide the slider if ON is selected
            autoViewHolder.slider.setVisibility(View.INVISIBLE);
        }
        else {
            autoViewHolder.slider.setVisibility(View.VISIBLE);
            autoViewHolder.slider.setOnSeekBarChangeListener(null); //set it to null to not trigger the old listener when setting progress
            autoViewHolder.slider.setProgress((int) (automationManager.getValue(automationIndex) * floatMultiplier));
            autoViewHolder.slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    automationManager.setValue(automationIndex, (float) autoViewHolder.slider.getProgress() / floatMultiplier);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        //set up the steps-layout
        autoViewHolder.stepsLayout.removeAllViews();
        autoViewHolder.stepsLayout.setWeightSum(automationManager.getAutomations().get(automationIndex).getNOfSteps());

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, 40, 1);
        llp.setMarginStart(2);

        for(int step = 0; step < automationManager.getAutomations().get(automationIndex).getNOfSteps(); step++){
            FrameLayout stepLayout = new FrameLayout(llppdrums);
            stepLayout.setLayoutParams(llp);
            if(automationManager.getAutomations().get(automationIndex).isStepOn(step)){
                stepLayout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.automationStepOn));
            }
            else{
                stepLayout.setBackgroundColor(ContextCompat.getColor(llppdrums, R.color.automationStepOff));
            }
            autoViewHolder.stepsLayout.addView(stepLayout);
        }

        autoViewHolder.stepsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AutomationStepsPopup(llppdrums, automationManager, FxAutomationManagerAdapter.this, automationIndex, autoViewHolder.stepsLayout);
            }
        });

        //and the removeBtn
        autoViewHolder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                automationManager.removeAutomation(automationIndex);
                notifyDataSetChanged();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(fxManagerPopup.getFxManager().getSelectedFx() != null) {
            return fxManagerPopup.getFxManager().getSelectedFx().getAutomationManager().getAutomations().size();
        }
        else{
            return 0;
        }
    }

    public void moveAutomation(int from, int to){
        fxManagerPopup.getFxManager().getSelectedFx().getAutomationManager().moveAutomation(from, to);
    }
}