package com.kiefer.ui;

import androidx.core.content.ContextCompat;
import android.view.View;
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

    public Counter(LLPPDRUMS llppdrums, int steps){
        this(llppdrums, steps, (int) llppdrums.getResources().getDimension(R.dimen.sequencerStepWidth), (int) llppdrums.getResources().getDimension(R.dimen.counterCellHeight));
    }

    public Counter(LLPPDRUMS llppdrums, int steps, int width, int height){
        this.llppdrums = llppdrums;
        this.width = width;
        this.height = height;

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
        RelativeLayout stepLayout = new RelativeLayout(llppdrums);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(width, height);
        stepLayout.setLayoutParams(llp);

        //setStepColor(i, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));

        //tv
        TextView tv = new TextView(llppdrums);
        tv.setText(Integer.toString(layout.getChildCount() + 1));
        tv.setTextSize(llppdrums.getResources().getDimension(R.dimen.sequencerCounterTxtSize));

        stepLayout.addView(tv);
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
            setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));
        }
        else{
            setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterDisabledBgColor));
        }
    }

    /** SET **/
    public void activateStep(int step){
        setStepColor(step, ContextCompat.getColor(llppdrums, R.color.counterActiveBgColor));
    }

    public void reset(){
        for(int i = 0; i < layout.getChildCount(); i++){
            if(layout != null) {
                if (layout.getChildAt(i).isEnabled()) {
                    setStepColor(i, ContextCompat.getColor(llppdrums, R.color.counterInactiveBgColor));
                }
            }
        }
    }

    protected void setStepColor(final int step, final int color){
        //llppdrums.runOnUiThread(new Runnable() {
            //public void run() {
                RelativeLayout bg = (RelativeLayout) layout.getChildAt(step);
                TextView tv = (TextView) ((RelativeLayout) layout.getChildAt(step)).getChildAt(0);
                bg.setBackgroundColor(color);
                tv.setTextColor(ColorUtils.getContrastColor(color));
            //}
        //});
    }

    public void setStepText(int step, String text){
        TextView tv = (TextView) ((RelativeLayout) layout.getChildAt(step)).getChildAt(0);
        tv.setText(text);
    }

    public void setStepListener(int step, View.OnClickListener listener){
        layout.getChildAt(step).setOnClickListener(listener);
    }

    /** GET **/
    public LinearLayout getLayout() {
        return layout;
    }

    public String getStepTxt(int step){
        return ((TextView) ((RelativeLayout) layout.getChildAt(step)).getChildAt(0)).getText().toString();
    }

    public int getSize(){
        return layout.getChildCount();
    }
}
