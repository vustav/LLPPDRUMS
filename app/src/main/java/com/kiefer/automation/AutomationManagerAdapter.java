package com.kiefer.automation;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;

public abstract class AutomationManagerAdapter extends RecyclerView.Adapter<AutomationManagerAdapter.AutomationViewHolder> {
    protected LLPPDRUMS llppdrums;

    public AutomationManagerAdapter(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
    }

    // Create new viewHolder
    @Override
    public AutomationViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View autoView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_holder_automation, viewGroup, false);
        return new AutomationViewHolder(autoView);
    }

    /** VIEWHOLDER **/
    public static class AutomationViewHolder extends RecyclerView.ViewHolder {
        public final ImageView bgIV;
        public final CheckBox checkBox;
        public final FrameLayout spinnerLayout;
        public final SeekBar slider;
        public final LinearLayout stepsLayout;
        public final Button removeBtn;

        public AutomationViewHolder(View v) {
            super(v);
            bgIV = v.findViewById(R.id.automationBgIV);;
            checkBox = v.findViewById(R.id.automationCheck);
            spinnerLayout = v.findViewById(R.id.automationCSpinnerContainer);
            slider = v.findViewById(R.id.automationSlider);
            stepsLayout = v.findViewById(R.id.automationStepsLayout);
            removeBtn = v.findViewById(R.id.automationRemoveBtn);
        }
    }
}
