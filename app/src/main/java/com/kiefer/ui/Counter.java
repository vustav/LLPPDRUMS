package com.kiefer.ui;

import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.utils.ColorUtils;

public class Counter {
    protected final LLPPDRUMS llppdrums;
    protected final LinearLayout layout;

    protected int width, height;

    protected Drawable drawable;

    public Counter(LLPPDRUMS llppdrums, int steps){
        this(llppdrums, steps, null);
    }

    public Counter(LLPPDRUMS llppdrums, int steps, Drawable drawable){
        this.llppdrums = llppdrums;
        this.width = (int) llppdrums.getResources().getDimension(R.dimen.sequencerStepWidth);
        this.height = (int) llppdrums.getResources().getDimension(R.dimen.counterCellHeight);
        this.drawable = drawable;

        layout = new LinearLayout(llppdrums);

        //create a layout with a tv in it for every step
        for(int step = 0; step<steps; step++){
            addStep();
        }
        reset(); //sets colors to base
    }

    public void setSize(int width, int height){
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(width, height);
        for(int i = 0; i < layout.getChildCount(); i++){
            layout.getChildAt(i).setLayoutParams(llp);
        }
    }

    public void addStep(){

        RelativeLayout stepLayout = (RelativeLayout) llppdrums.getLayoutInflater().inflate(R.layout.counter_cell, null);

        //stepLayout.setPadding(0, -5, 0, 0);
        //RelativeLayout stepLayout = new RelativeLayout(llppdrums);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(width, height);
        stepLayout.setLayoutParams(llp);

        //setStepColor(i, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));

        //tv
        //TextView tv = new TextView(llppdrums);
        TextView tv = stepLayout.findViewById(R.id.counterCellTv);
        //tv.setPadding(0, -5, 0, 0);
        //RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //rlp.setMargins(0, -5, 0, 0);
        //tv.setLayoutParams(rlp);
        tv.setText(Integer.toString(layout.getChildCount() + 1));
        tv.setTextSize(llppdrums.getResources().getDimension(R.dimen.sequencerCounterTxtSize));

        //stepLayout.addView(tv);
        layout.addView(stepLayout);

        reset();
    }

    //remove last step
    public void removeStep(){
        layout.removeView(layout.getChildAt(layout.getChildCount() - 1));
    }


    public void enableStep(int step, boolean enabled){
        layout.getChildAt(step).setEnabled(enabled);

        if(enabled){
            layout.getChildAt(step).setAlpha(1f);
            setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));
        }
        else{
            layout.getChildAt(step).setAlpha(.5f);
            setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterDisabledBgColor));
        }
    }

    /** SET **/
    public void activateStep(int step){
        setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterActiveBgColor));
    }

    public void reset(){
        for(int i = 0; i < layout.getChildCount(); i++){
            //if(layout != null) {
                if (layout.getChildAt(i).isEnabled()) {
                    setStepColor(i, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));
                }
            //}
        }
    }

    protected void setStepColor(final int step, final int color){
        llppdrums.runOnUiThread(() -> {
            layout.getChildAt(step).findViewById(R.id.counterCellBg).setBackgroundColor(color);
            ((TextView) layout.getChildAt(step).findViewById(R.id.counterCellTv)).setTextColor(ColorUtils.getContrastColor(color));
        });
    }

    public void setStepText(int step, String text){
        ((TextView)layout.getChildAt(step).findViewById(R.id.counterCellTv)).setText(text);
    }

    public void setStepListener(int step, View.OnClickListener listener){
        layout.getChildAt(step).setOnClickListener(listener);
    }

    /** GET **/
    public LinearLayout getLayout() {
        return layout;
    }
/*
    public String getStepTxt(int step){
        return ((TextView) ((FrameLayout) layout.getChildAt(step)).getChildAt(1)).getText().toString();
    }

 */

    public int getSize(){
        return layout.getChildCount();
    }
}
