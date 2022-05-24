package com.kiefer.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.sequenceModules.Pan;
import com.kiefer.machine.sequence.track.Step;

public class PanSubsDrawable extends Drawable {
    private final LLPPDRUMS llppdrums;
    private final Pan pan;
    private final Step step;
    private final Paint shapePaint;
    private final Paint bgPaint;

    public PanSubsDrawable(LLPPDRUMS llppdrums, Pan pan, Step step) {
        this.llppdrums = llppdrums;
        this.pan = pan;
        this.step = step;

        //create the paints
        shapePaint = new Paint();

        bgPaint = new Paint();
        bgPaint.setColor(llppdrums.getResources().getColor(R.color.popupBarBg));
    }

    @Override
    public void draw(Canvas canvas) {
        float panValue = (step.getPan() + 1) / 2;

        // Get the drawable's bounds
        int width = getBounds().width();
        int height = getBounds().height();

        if(step.isOn()) {
            shapePaint.setColor(llppdrums.getResources().getColor(R.color.popupBarColor));
        }
        else{
            shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
        }

        int lineSize = 10;

        //draw the background
        canvas.drawRect(0, 0, width, height, bgPaint);

        //draw a line in the middle
        canvas.drawRect(width * panValue, 0, width * panValue + lineSize, height, shapePaint);

        if(step.isOn() && step.panAutomationActive()) {
            pan.addAutoIndication(canvas);
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