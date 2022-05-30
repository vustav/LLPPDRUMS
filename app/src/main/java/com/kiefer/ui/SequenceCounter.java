package com.kiefer.ui;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.SequenceManager;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;

public class SequenceCounter extends Counter {
    private LinearLayout controllerLayout;
    private final SequenceManager sequenceManager;

    public SequenceCounter(LLPPDRUMS llppdrums, SequenceManager sequenceManager, int steps, int txtSize){
        super(llppdrums, steps);
        this.sequenceManager = sequenceManager;

        createControllerLayout(steps, txtSize);

        listeners = new ArrayList<>();
    }

    //ImageView playIv;
    @Override
    public void addStep(){

        RelativeLayout stepLayout = (RelativeLayout) llppdrums.getLayoutInflater().inflate(R.layout.counter_cell_sequence, null);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(width, height);
        stepLayout.setLayoutParams(llp);

        //tv
        TextView tv = stepLayout.findViewById(R.id.counterCellTv);
        tv.setText(Integer.toString(layout.getChildCount() + 1));
        tv.setTextSize(llppdrums.getResources().getDimension(R.dimen.sequencerCounterTxtSize));

        layout.addView(stepLayout);

        reset();
    }

    private void createControllerLayout(int steps, int txtSize){
        controllerLayout = new LinearLayout(llppdrums);
        controllerLayout.setWeightSum(steps);

        for(int step = 0; step < steps; step++){
            RelativeLayout stepLayout = (RelativeLayout) llppdrums.getLayoutInflater().inflate(R.layout.counter_cell_sequence_border, null);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            llp.setMarginStart(2);
            llp.setMarginEnd(2);
            stepLayout.setLayoutParams(llp);

            //tv
            TextView tv = stepLayout.findViewById(R.id.counterCellTv);
            tv.setText(Integer.toString(layout.getChildCount() + 1));
            tv.setTextSize(txtSize);

            //stepLayout.addView(tv);
            controllerLayout.addView(stepLayout);
        }
    }

    /** SET **/
    @Override
    public void activateStep(int step){
        //llppdrums.runOnUiThread(() -> {
            layout.getChildAt(step).findViewById(R.id.counterCellPlayIv).setVisibility(View.VISIBLE);

            if(controllerLayout != null) {
                controllerLayout.getChildAt(step).findViewById(R.id.counterCellPlayIv).setVisibility(View.VISIBLE);
            }
        //});
    }

    public void queueStep(int step){
        //llppdrums.runOnUiThread(() -> {
            layout.getChildAt(step).findViewById(R.id.counterCellQueueIv).setVisibility(View.VISIBLE);

            if(controllerLayout != null) {
                controllerLayout.getChildAt(step).findViewById(R.id.counterCellQueueIv).setVisibility(View.VISIBLE);
            }
        //});
    }

    @Override
    public void reset(){
        super.reset();
        for(int i = 0; i < layout.getChildCount(); i++){
            reset(i);
        }
    }

    public void reset(int i){
        layout.getChildAt(i).findViewById(R.id.counterCellPlayIv).setVisibility(View.INVISIBLE);
        layout.getChildAt(i).findViewById(R.id.counterCellQueueIv).setVisibility(View.INVISIBLE);

        if(controllerLayout != null) {
            if (controllerLayout.getChildAt(i).isEnabled()) {
                setStepColor(i, sequenceManager.getSelectedSequences().get(i).getColor());
                controllerLayout.getChildAt(i).findViewById(R.id.counterCellPlayIv).setVisibility(View.INVISIBLE);
                controllerLayout.getChildAt(i).findViewById(R.id.counterCellQueueIv).setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void setStepColor(final int step, final int color){
        super.setStepColor(step, color);
        if (controllerLayout != null) {
            //controllerLayout.getChildAt(step).findViewById(R.id.counterCellBorder).setBackgroundColor(color);
            controllerLayout.getChildAt(step).findViewById(R.id.counterCellBg).setBackgroundColor(color);
            ((TextView)controllerLayout.getChildAt(step).findViewById(R.id.counterCellTv)).setTextColor(ColorUtils.getContrastColor(color));
        }
    }

    @Override
    public void setStepText(int step, String text){
        super.setStepText(step, text);
        ((TextView)controllerLayout.getChildAt(step).findViewById(R.id.counterCellTv)).setText(text);
    }

    private ArrayList<View.OnClickListener> listeners;
    @Override
    public void setStepListener(int step, View.OnClickListener listener){
        super.setStepListener(step, listener);
        controllerLayout.getChildAt(step).setOnClickListener(listener);
        listeners.add(listener);
    }

    public void setSelectedSeqBox(int step){
        int color;
        for(int i = 0; i < layout.getChildCount(); i++){
            color = sequenceManager.getSelectedSequences().get(i).getColor();
            controllerLayout.getChildAt(i).findViewById(R.id.counterCellBorder).setBackgroundColor(color);
        }

        color = ColorUtils.getContrastColor(sequenceManager.getSelectedSequences().get(step).getColor());
        controllerLayout.getChildAt(step).findViewById(R.id.counterCellBorder).setBackgroundColor(color);
    }

    @Override
    public void enableStep(int step, boolean enabled){
        super.enableStep(step, enabled);
        controllerLayout.getChildAt(step).setEnabled(enabled);

        if(enabled){
            //setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));
            setStepColor(step, sequenceManager.getSelectedSequences().get(step).getColor());
            controllerLayout.getChildAt(step).setAlpha(1f);
        }
        else{
            setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterDisabledBgColor));
            controllerLayout.getChildAt(step).setAlpha(.5f);
        }
    }

    /** GET **/

    public View.OnClickListener getListener(int index){
        return listeners.get(index);
    }

    public LinearLayout getControllerLayout(){
        return controllerLayout;
    }
}
