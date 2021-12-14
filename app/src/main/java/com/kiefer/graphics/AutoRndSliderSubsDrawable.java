package com.kiefer.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;

public class AutoRndSliderSubsDrawable extends Drawable {
    private final LLPPDRUMS llppdrums;
    private final int color;
    private final Paint shapePaint;
    private final Paint bgPaint;

    private Boolean[] subsOn;
    private Float[] subsValue;

    public AutoRndSliderSubsDrawable(LLPPDRUMS llppdrums, boolean on, float value) {
        this(llppdrums, new Boolean[]{on}, new Float[]{value}, R.color.volPopupBarColorLow);
    }

    public AutoRndSliderSubsDrawable(LLPPDRUMS llppdrums, boolean on, float value, int color) {
        this(llppdrums, new Boolean[]{on}, new Float[]{value}, color);
    }

    public AutoRndSliderSubsDrawable(LLPPDRUMS llppdrums, Boolean[] subsOn, Float[] subsValue) {
        this(llppdrums, subsOn, subsValue, R.color.volPopupBarColorLow);
    }
    public AutoRndSliderSubsDrawable(LLPPDRUMS llppdrums, Boolean[] subsOn, Float[] subsValue, int color) {
        this.llppdrums = llppdrums;
        this.color = color;
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
        int offset = 0;

        // Get the drawable's size
        int cellWidth = getBounds().width();
        int height = getBounds().height();
        int drawableWidth = cellWidth / subs;

        for(int i = 0; i < subs; i++){

            if(subsOn[i]) {
                shapePaint.setColor(color);
            }
            else{
                shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
            }

            //draw it
            canvas.drawRect(offset, 0, drawableWidth + offset, height-(height * subsValue[i]), bgPaint);
            canvas.drawRect(offset, height-(height * subsValue[i]), offset + drawableWidth, height, shapePaint);
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