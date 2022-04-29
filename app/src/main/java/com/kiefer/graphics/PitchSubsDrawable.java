package com.kiefer.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.sequenceModules.Pitch;
import com.kiefer.machine.sequence.track.Step;

public class PitchSubsDrawable extends Drawable {
    private final LLPPDRUMS llppdrums;
    private final Pitch pitch;
    private final Step step;
    private final Paint shapePaint;
    private final Paint bgPaint;

    public PitchSubsDrawable(LLPPDRUMS llppdrums, Pitch ptich, Step step) {
        this.llppdrums = llppdrums;
        this.pitch = ptich;
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

        //draw the background
        canvas.drawRect(0, 0, cellWidth, height, bgPaint);

        for(int i = 0; i < subs; i++){

            //set the color
            if(step.isOn() && step.isSubOn(i)) {
                shapePaint.setColor(llppdrums.getResources().getColor(R.color.popupBarColor));
            }
            else{
                shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
            }

            int lineSize = 10;

            //draw a line in the middle
            canvas.drawRect(offset, height-(height * step.getPitchModifier(i)) - lineSize / 2f, drawableWidth + offset, height-(height * step.getPitchModifier(i)) + lineSize / 2f, shapePaint);

            offset += drawableWidth;
        }

        if(step.isOn() && step.pitchAutomationActive()) {
            pitch.addAutoIndication(step, canvas);
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