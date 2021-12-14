package com.kiefer.graphics;

import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.kiefer.LLPPDRUMS;
import com.kiefer.utils.ColorUtils;

import java.util.Random;

public class DrumTrackFxBtnGraphics {
    private final int WIDTH = 3, HEIGHT = 4, CELL_SIZE = 12, MARGIN = 3;
    private final Random random = new Random();
    private final LinearLayout layout;

    public DrumTrackFxBtnGraphics(LLPPDRUMS llppdrums){
        layout = new LinearLayout(llppdrums);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.BLACK);

        for(int row = 0; row < HEIGHT; row++){
            LinearLayout linInner = new LinearLayout(llppdrums);
            linInner.setOrientation(LinearLayout.HORIZONTAL);
            linInner.setPadding(0, MARGIN, MARGIN, 0);

            //if(row == 0 || row == HEIGHT-1) {
                for (int col = 0; col < WIDTH; col++) {
                    FrameLayout fl = new FrameLayout(llppdrums);
                    FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(CELL_SIZE, CELL_SIZE);
                    flp.setMargins(MARGIN, 0, 0, MARGIN);
                    fl.setLayoutParams(flp);

                    //if (random.nextInt(2) == 1) {
                        //if(row == HEIGHT-1 && col == WIDTH / 2 + 1){
                            //fl.setBackgroundColor(Color.GRAY);
                        //}
                        //else if(row == 0) {
                            fl.setBackgroundColor(ColorUtils.getRandomColor());
                        //}
                    //}

                    linInner.addView(fl);
                }
            //}
            layout.addView(linInner);
        }
    }

    public LinearLayout getLayout(){
        return layout;
    }
}
