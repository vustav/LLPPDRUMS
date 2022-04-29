package com.kiefer.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.sequenceModules.OnOff;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.track.Step;

public class OnOffSubsDrawable extends Drawable {
    private final LLPPDRUMS llppdrums;
    private final OnOff onOff;
    private final Step step;
    private final Paint shapePaint;
    private final Paint bgPaint;

    public OnOffSubsDrawable(LLPPDRUMS llppdrums, OnOff onOff, Step step) {
        this.llppdrums = llppdrums;
        this.onOff = onOff;
        this.step = step;

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

        //start by drawing the background
        canvas.drawRect(0, 0, width, height, bgPaint);

        //set colors on the paint for the shape
        if(step.isOn()) {

            int subs = step.getNofSubs();

            float x = width / 2 / subs;
            float radius = x - 1f;
            float y = height / 2;
            int offset = 0;

            for(int i = 0; i < subs; i++){

                if(step.isSubOn(i)) {
                    shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerActiveStepColor));
                }
                else{
                    shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
                }

                //draw it
                canvas.drawCircle(x + offset, y, radius, shapePaint);
                offset += width / subs;
            }
        }

        if(step.onAutomationActive()) {
            onOff.addAutoIndication(step, canvas);
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