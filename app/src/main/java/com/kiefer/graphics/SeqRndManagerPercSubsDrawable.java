package com.kiefer.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;

public class SeqRndManagerPercSubsDrawable extends Drawable {
    private final LLPPDRUMS llppdrums;
    private RndSeqPresetTrack.Step step;
    private final Paint shapePaint;
    private final Paint bgPaint;

    private float modifier = 0;
    private boolean on = true;

    public SeqRndManagerPercSubsDrawable(LLPPDRUMS llppdrums, RndSeqPresetTrack.Step step) {
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

        // Get the drawable's bounds
        //int width = getBounds().width();
        //int height = getBounds().height();

        shapePaint.setColor(llppdrums.getResources().getColor(R.color.rndSeqPercColor));

        for(int i = 0; i < subs; i++){

            //set the color
            //if(step.isOn() && step.isSubOn(i)) {
            //}
            //else{
                //shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
            //}

            //draw it
            canvas.drawRect(offset, 0, drawableWidth + offset, height-(height * step.getSubPerc(i)), bgPaint);
            canvas.drawRect(offset, height-(height * step.getSubPerc(i)), offset + drawableWidth, height, shapePaint);
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