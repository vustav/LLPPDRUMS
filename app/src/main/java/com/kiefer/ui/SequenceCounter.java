package com.kiefer.ui;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.utils.ColorUtils;

import java.util.ArrayList;

public class SequenceCounter extends Counter {
    private final LinearLayout controllerLayout;

    public SequenceCounter(LLPPDRUMS llppdrums, int steps, int width, int height, int txtSize){
        super(llppdrums, steps);

        controllerLayout = new LinearLayout(llppdrums);
        createControllerLayout(steps, width, height, txtSize);

        listeners = new ArrayList<>();
    }

    private void createControllerLayout(int steps, int width, int height, int txtSize){
        for(int step = 0; step < steps; step++){
            RelativeLayout stepLayout = new RelativeLayout(llppdrums);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(width, height);
            stepLayout.setLayoutParams(llp);

            //tv
            TextView tv = new TextView(llppdrums);
            tv.setText(Integer.toString(layout.getChildCount() + 1));
            tv.setTextSize(txtSize);

            stepLayout.addView(tv);
            controllerLayout.addView(stepLayout);
        }

        reset(); //sets colors to base
    }

    /** SET **/
    @Override
    public void activateStep(int step){
        super.activateStep(step);
        setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterActiveBgColor));
    }

    @Override
    public void reset(){
        super.reset();
        for(int i = 0; i < layout.getChildCount(); i++){
            if(controllerLayout != null) {
                if (controllerLayout.getChildAt(i).isEnabled()) {
                    setStepColor(i, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));
                }
            }
        }
    }

    @Override
    public void setStepColor(final int step, final int color){
        //if(controllerLayout.getChildAt(step).isEnabled()) {
            super.setStepColor(step, color);
            if (controllerLayout != null) {
                llppdrums.runOnUiThread(new Runnable() {
                    public void run() {
                        //if(controllerLayout.getChildAt(step).isEnabled()) {
                        RelativeLayout bg = (RelativeLayout) controllerLayout.getChildAt(step);
                        TextView tv = (TextView) ((RelativeLayout) controllerLayout.getChildAt(step)).getChildAt(0);
                        bg.setBackgroundColor(color);
                        tv.setTextColor(ColorUtils.getContrastColor(color));
                        //}
                    }
                });
            }
        //}
    }

    @Override
    public void setStepText(int step, String text){
        super.setStepText(step, text);
        TextView tv = (TextView) ((RelativeLayout) controllerLayout.getChildAt(step)).getChildAt(0);
        tv.setText(text);
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
            TextView tv = (TextView) ((RelativeLayout) controllerLayout.getChildAt(i)).getChildAt(0);
            tv.setBackgroundColor(0x00000000);
        }

        int color = ContextCompat.getColor(llppdrums, R.color.selectedSeqColor);
        TextView selectedTv = (TextView) ((RelativeLayout) controllerLayout.getChildAt(step)).getChildAt(0);
        selectedTv.setBackgroundColor(color);
    }

    @Override
    public void enableStep(int step, boolean enabled){
        super.enableStep(step, enabled);
        controllerLayout.getChildAt(step).setEnabled(enabled);

        if(enabled){
            setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));
        }
        else{
            setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterDisabledBgColor));
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
