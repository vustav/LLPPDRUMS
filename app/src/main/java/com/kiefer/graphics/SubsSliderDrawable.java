package com.kiefer.graphics;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.utils.ColorUtils;

public class SubsSliderDrawable extends Drawable {

    /** Suddenly passing actual colors here stopped working and they became transparent so having another color than green requires adding them here **/
    public final static int BLUE = 0, GREEN = 1;

    private final LLPPDRUMS llppdrums;
    private final int color;
    private final Paint shapePaint;
    private final Paint bgPaint;

    private Boolean[] subsOn;
    private Float[] subsValue;

    public SubsSliderDrawable(LLPPDRUMS llppdrums, boolean on, float value) {
        this(llppdrums, new Boolean[]{on}, new Float[]{value}, GREEN);
    }

    public SubsSliderDrawable(LLPPDRUMS llppdrums, boolean on, float value, int color) {
        this(llppdrums, new Boolean[]{on}, new Float[]{value}, color);
    }

    public SubsSliderDrawable(LLPPDRUMS llppdrums, Boolean[] subsOn, Float[] subsValue) {
        this(llppdrums, subsOn, subsValue, GREEN);
    }
    public SubsSliderDrawable(LLPPDRUMS llppdrums, Boolean[] subsOn, Float[] subsValue, int color) {
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
                //shapePaint.setColor(color);
                if(color == GREEN) {
                    shapePaint.setColor(llppdrums.getResources().getColor(R.color.volPopupBarColorLow));
                }
                else if (color == BLUE) {
                    shapePaint.setColor(llppdrums.getResources().getColor(R.color.rndSeqPercColor));
                }
            }
            else{
                shapePaint.setColor(llppdrums.getResources().getColor(R.color.sequencerInactiveStepColor));
            }
            //

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