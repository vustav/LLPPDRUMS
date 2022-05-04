package com.kiefer.ui;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.SequenceManager;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;

public class SequenceCounter extends Counter {
    private final LinearLayout controllerLayout;
    private final SequenceManager sequenceManager;

    public SequenceCounter(LLPPDRUMS llppdrums, SequenceManager sequenceManager, int steps, int width, int height, int txtSize){
        super(llppdrums, steps);
        this.sequenceManager = sequenceManager;

        controllerLayout = new LinearLayout(llppdrums);
        createControllerLayout(steps, width, height, txtSize);

        listeners = new ArrayList<>();
    }

    //ImageView playIv;
    @Override
    public void addStep(){

        FrameLayout stepLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.counter_cell_sequence, null);

        //RelativeLayout stepLayout = new RelativeLayout(llppdrums);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(width, height);
        stepLayout.setLayoutParams(llp);

        //setStepColor(i, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));

        //tv
        //TextView tv = new TextView(llppdrums);
        TextView tv = stepLayout.findViewById(R.id.counterCellTv);
        tv.setText(Integer.toString(layout.getChildCount() + 1));
        tv.setTextSize(llppdrums.getResources().getDimension(R.dimen.sequencerCounterTxtSize));

        //ImageView iv = stepLayout.findViewById(R.id.counterCellIv);
        //iv.setVisibility(View.INVISIBLE);
        //showPlayIcon(false);

        //stepLayout.addView(tv);
        layout.addView(stepLayout);

        reset();
    }

    private void createControllerLayout(int steps, int width, int height, int txtSize){
        for(int step = 0; step < steps; step++){
            FrameLayout stepLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.counter_cell_sequence, null);
            //RelativeLayout stepLayout = new RelativeLayout(llppdrums);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(width, height);
            stepLayout.setLayoutParams(llp);

            //tv
            TextView tv = stepLayout.findViewById(R.id.counterCellTv);
            tv.setText(Integer.toString(layout.getChildCount() + 1));
            tv.setTextSize(txtSize);

            //stepLayout.addView(tv);
            controllerLayout.addView(stepLayout);
        }

        //reset(); //sets colors to base
    }

    /** SET **/
    @Override
    public void activateStep(int step){
        /*
        super.activateStep(step);
        setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterActiveBgColor));

         */
        //((ViewGroup)layout.getChildAt(step)).getChildAt(2).setVisibility(View.VISIBLE);
        //((ViewGroup)controllerLayout.getChildAt(step)).getChildAt(2).setVisibility(View.VISIBLE);
        layout.getChildAt(step).findViewById(R.id.counterCellIv).setVisibility(View.VISIBLE);
        controllerLayout.getChildAt(step).findViewById(R.id.counterCellIv).setVisibility(View.VISIBLE);
    }

    @Override
    public void reset(){
        super.reset();
        for(int i = 0; i < layout.getChildCount(); i++){

            //((ViewGroup)layout.getChildAt(i)).getChildAt(2).setVisibility(View.INVISIBLE);
            layout.getChildAt(i).findViewById(R.id.counterCellIv).setVisibility(View.INVISIBLE);

            if(controllerLayout != null) {
                if (controllerLayout.getChildAt(i).isEnabled()) {
                    setStepColor(i, sequenceManager.getSelectedSequences().get(i).getColor());
                    controllerLayout.getChildAt(i).findViewById(R.id.counterCellIv).setVisibility(View.INVISIBLE);
                    //controllerLayout.findViewById(R.id.counterCellIv).setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void setStepColor(final int step, final int color){
        //if(controllerLayout.getChildAt(step).isEnabled()) {
        super.setStepColor(step, color);
        if (controllerLayout != null) {
            //llppdrums.runOnUiThread(new Runnable() {
            //public void run() {
            //if(controllerLayout.getChildAt(step).isEnabled()) {
            //RelativeLayout bg = (RelativeLayout) controllerLayout.getChildAt(step);
            //TextView tv = (TextView) ((RelativeLayout) controllerLayout.getChildAt(step)).getChildAt(1);
            controllerLayout.getChildAt(step).findViewById(R.id.counterCellBg).setBackgroundColor(color);
            ((TextView)controllerLayout.getChildAt(step).findViewById(R.id.counterCellTv)).setTextColor(ColorUtils.getContrastColor(color));
            //}
            //}
            //});
        }
        //}
    }

    @Override
    public void setStepText(int step, String text){
        super.setStepText(step, text);
        //TextView tv = (TextView) ((RelativeLayout) controllerLayout.getChildAt(step)).getChildAt(1);
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
        for(int i = 0; i < layout.getChildCount(); i++){
            //TextView tv = controllerLayout.getChildAt(i).findViewById(R.id.counterCellTv);
            controllerLayout.getChildAt(i).findViewById(R.id.counterCellTv).setBackgroundColor(0x00000000);
        }

        int color = ContextCompat.getColor(llppdrums, R.color.selectedSeqColor);
        //TextView selectedTv = (TextView) ((RelativeLayout) controllerLayout.getChildAt(step)).getChildAt(1);
        //controllerLayout.findViewById(R.id.counterCellTv).setBackgroundColor(color);
        controllerLayout.getChildAt(step).findViewById(R.id.counterCellTv).setBackgroundColor(color);
/*
        ((ViewGroup)layout.getChildAt(step)).getChildAt(1).setVisibility(View.VISIBLE);
        ((ViewGroup)controllerLayout.getChildAt(step)).getChildAt(1).setVisibility(View.VISIBLE);

 */

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
