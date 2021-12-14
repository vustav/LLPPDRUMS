package com.kiefer.graphics;

import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.kiefer.LLPPDRUMS;

import java.util.Random;

public class DrumTrackAutoBtnGraphics {
    private final int SIZE = 4, CELL_SIZE = 12, MARGIN = 3;
    private Random random = new Random();
    private LinearLayout layout;

    public DrumTrackAutoBtnGraphics(LLPPDRUMS llppdrums){
        layout = new LinearLayout(llppdrums);
        layout.setOrientation(LinearLayout.VERTICAL);

        for(int row = 0; row < SIZE; row++){
            LinearLayout linInner = new LinearLayout(llppdrums);
            linInner.setOrientation(LinearLayout.HORIZONTAL);
            linInner.setPadding(0, MARGIN, MARGIN, 0);
            for(int col = 0; col < SIZE; col++){
                FrameLayout fl = new FrameLayout(llppdrums);
                FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(CELL_SIZE, CELL_SIZE);
                flp.setMargins(MARGIN, 0, 0, MARGIN);
                fl.setLayoutParams(flp);

                if(random.nextInt(2) == 1){
                    fl.setBackgroundColor(Color.BLACK);
                }

                linInner.addView(fl);
            }
            layout.addView(linInner);
        }
    }

    public LinearLayout getLayout(){
        return layout;
    }
}
