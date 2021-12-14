package com.kiefer.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;

public class AutoRndRtrnSubsDrawable extends Drawable {
    private final LLPPDRUMS llppdrums;
    private final Paint shapePaint;
    private final Paint bgPaint;

    private Boolean[] subsOn;
    private Boolean[] subsValue;

    public AutoRndRtrnSubsDrawable(LLPPDRUMS llppdrums, Boolean[] subsOn, Boolean[] subsValue) {
        this.llppdrums = llppdrums;
        this.subsOn = subsOn;
        this.subsValue = subsValue;

        //create the paints
        shapePaint = new Paint();

        bgPaint = new Paint();
        bgPaint.setColor(llppdrums.getResources().getColor(R.color.popupBarBg));
    }

    @Override
    public void draw(Canvas canvas) {
        int subs = subsOn.length;

        // Get the drawable's size
        int cellWidth = getBounds().width();
        int height = getBounds().height();
        int subWidth = cellWidth / subs;

        //draw the background
        canvas.drawRect(0, 0, cellWidth, height, bgPaint);

        float x = (float)subWidth / 2;
        float radius = x - 1f;
        float y = (float)height / 2;
        int offset = 0;


        for(int i = 0; i < subs; i++){
            shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
            shapePaint.setStyle(Paint.Style.FILL);

            //autoRnd and return ON
            if(subsOn[i] && subsValue[i]) {
                shapePaint.setColor(llppdrums.getResources().getColor(R.color.autoRndRtrnColor));
                shapePaint.setStyle(Paint.Style.FILL);
            }
            //autoRnd and return OFF
            else if(!subsOn[i] && !subsValue[i]){
                shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
                shapePaint.setStyle(Paint.Style.STROKE);
                shapePaint.setStrokeWidth(2);
            }
            //one ON and one OFF
            else{
                shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
                shapePaint.setStyle(Paint.Style.FILL);
            }
            canvas.drawCircle(x + offset, y, radius, shapePaint);

            //draw it
            canvas.drawCircle(x + offset, y, radius, shapePaint);
            offset += subWidth;
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