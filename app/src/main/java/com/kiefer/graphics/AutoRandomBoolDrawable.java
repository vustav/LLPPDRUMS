package com.kiefer.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;

public class AutoRandomBoolDrawable extends Drawable {
    private final LLPPDRUMS llppdrums;
    //private final Drum drum;
    private final boolean paramOn; //since all modules use this they will have different checks for autorandom and we can't just ask the drum
    private final boolean active;
    private final Paint shapePaint;
    private final Paint bgPaint;

    public AutoRandomBoolDrawable(LLPPDRUMS llppdrums, boolean active, boolean paramOn) {
        this.llppdrums = llppdrums;
        //this.drum = drum;
        this.active = active;
        this.paramOn = paramOn;

        //create the paints
        shapePaint = new Paint();

        bgPaint = new Paint();
        bgPaint.setColor(llppdrums.getResources().getColor(R.color.popupBarBg));
    }

    @Override
    public void draw(Canvas canvas) {

        // Get the drawable's bounds
        float width = getBounds().width();
        float height = getBounds().height();
        float radius = Math.min(width, height) / 2;

        //start by drawing the background
        canvas.drawRect(0, 0, width, height, bgPaint);

        // Draw a red circle in the center if autoRandom is on
        if(paramOn) {

            //set colors on the paint for the shape
            shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerActiveStepColor));
            if(!active) {
                shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
            }

            //draw it
            canvas.drawCircle(width / 2, height / 2, radius, shapePaint);
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