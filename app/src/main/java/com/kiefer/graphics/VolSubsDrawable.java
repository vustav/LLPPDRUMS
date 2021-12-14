package com.kiefer.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.Step;

public class VolSubsDrawable extends Drawable {
    private final LLPPDRUMS llppdrums;
    private final Step step;
    private final Paint shapePaint;
    private final Paint bgPaint;

    private float modifier = 0;
    private boolean on = true;

    public VolSubsDrawable(LLPPDRUMS llppdrums, Step step) {
        this.llppdrums = llppdrums;
        this.step = step;

        //create the paints
        shapePaint = new Paint();

        bgPaint = new Paint();
        bgPaint.setColor(llppdrums.getResources().getColor(R.color.popupBarBg));
    }

    @Override
    public void draw(Canvas canvas) {
        int subs = step.getNofSubs();
        int offset = 0;

        // Get the drawable's size
        int cellWidth = getBounds().width();
        int height = getBounds().height();
        int drawableWidth = cellWidth / subs;

        for(int i = 0; i < subs; i++){

            //set the color
            if(step.isOn() && step.isSubOn(i)) {
                if(step.getVolumeModifier(i) > .9){
                    shapePaint.setColor(llppdrums.getResources().getColor(R.color.volPopupBarColorHigh));
                }
                else if(step.getVolumeModifier(i) > .7){
                    shapePaint.setColor(llppdrums.getResources().getColor(R.color.volPopupBarColorMidHigh));
                }
                else if(step.getVolumeModifier(i) > .4){
                    shapePaint.setColor(llppdrums.getResources().getColor(R.color.volPopupBarColorMid));
                }
                else if(step.getVolumeModifier(i) > .2){
                    shapePaint.setColor(llppdrums.getResources().getColor(R.color.volPopupBarColorMidLow));
                }
                else{
                    shapePaint.setColor(llppdrums.getResources().getColor(R.color.volPopupBarColorLow));
                }
            }
            else{
                shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
            }

            //draw it
            canvas.drawRect(offset, 0, drawableWidth + offset, height-(height * step.getVolumeModifier(i)), bgPaint);
            canvas.drawRect(offset, height-(height * step.getVolumeModifier(i)), offset + drawableWidth, height, shapePaint);
            offset += drawableWidth;
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // This method is required
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // This method is required
    }

    @Override
    public int getOpacity() {
        // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
        return PixelFormat.OPAQUE;
    }
}