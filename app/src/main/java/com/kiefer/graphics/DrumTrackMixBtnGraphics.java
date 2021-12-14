package com.kiefer.graphics;

import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.kiefer.LLPPDRUMS;

import java.util.Random;

public class DrumTrackMixBtnGraphics {
    private final int HEIGHT = 15, WIDTH = 3, CELL_WIDTH = 16, CELL_HEIGHT = CELL_WIDTH / 10, MARGIN = 3;
    private Random random = new Random();
    private LinearLayout layout;

    public DrumTrackMixBtnGraphics(LLPPDRUMS llppdrums){
        layout = new LinearLayout(llppdrums);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        for(int col = 0; col < WIDTH; col++){
            int fill = random.nextInt(HEIGHT);
            LinearLayout linInner = new LinearLayout(llppdrums);
            linInner.setOrientation(LinearLayout.VERTICAL);
            linInner.setPadding(0, 0, MARGIN, 0);
            for(int row = 0; row < HEIGHT; row++){
                FrameLayout fl = new FrameLayout(llppdrums);
                FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(CELL_WIDTH, 4);
                flp.setMargins(MARGIN, 0, 0, 0);
                fl.setLayoutParams(flp);

                if(row >= fill){
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
