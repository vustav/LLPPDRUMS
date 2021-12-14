package com.kiefer.utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import java.util.Random;

public class ColorUtils {

    public static int getRandomColor(){
        Random random = new Random();
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public static int getContrastColor(int color) {
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        int newR = 255 - r;
        int newG = 255 - g;
        int newB = 255 - b;

        return Color.rgb(newR, newG, newB);
    }

    public static GradientDrawable getRandomGradientDrawable(int colorOne, int colorTwo){
        Random random = new Random();
        //GradientDrawable gd;
        if(random.nextInt(2) == 1){
            //gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{colorOne, colorTwo});
            return getGradientDrawable(colorOne, colorTwo, VERTICAL);
        }
        else{
            //gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{colorOne, colorTwo});
            return getGradientDrawable(colorOne, colorTwo, HORIZONTAL);
        }
        //gd.setCornerRadius(0f);
        //return gd;
    }

    public static final int VERTICAL = 0, HORIZONTAL = 1;
    public static GradientDrawable getGradientDrawable(int colorOne, int colorTwo, int orientation){
        if(orientation == VERTICAL){
            return new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{colorOne, colorTwo});
        }

        if(orientation == HORIZONTAL){
            return new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{colorOne, colorTwo});
        }
        return null;
    }
}
