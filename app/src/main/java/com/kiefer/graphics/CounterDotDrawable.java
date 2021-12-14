package com.kiefer.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;

public class CounterDotDrawable extends Drawable {
    private final LLPPDRUMS llppdrums;
    private final Paint shapePaint;
    //private final Paint bgPaint;

    public CounterDotDrawable(LLPPDRUMS llppdrums) {
        this.llppdrums = llppdrums;

        //create the paints
        shapePaint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        Log.e("Dot", "drawing");

        // Get the drawable's bounds
        float width = getBounds().width();
        float height = getBounds().height();
        float radius = Math.min(width, height) / 5;

        //start by drawing the background
        //canvas.drawRect(0, 0, width, height, bgPaint);

        //set colors on the paint for the shape
        //if(drum.isOn()) {
            shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerActiveStepColor));

            //draw it
            canvas.drawCircle(width / 2, height - radius, radius, shapePaint);
        //}
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