package com.kiefer.popups.trackMenu;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSeekBar;
import com.kiefer.popups.Popup;
import com.kiefer.popups.trackMenu.TrackNamePopup;
import com.kiefer.utils.ColorUtils;

public class PalettePopup extends Popup {
    private CSeekBar seekBar;
    private View.OnTouchListener onTouchListener;

    public PalettePopup(final LLPPDRUMS llppdrums, final TrackNamePopup trackNamePopup, View anchor){
        super(llppdrums);

        //inflate the View
        final FrameLayout popupView = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.popup_palette, null);

        //create the popupWindow
        int width = 1000;
        int height = 1000;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout rowLayout = new LinearLayout(llppdrums);
        rowLayout.setOrientation(LinearLayout.VERTICAL);

        int rows = 10, cols = 10;

        for(int row = 0; row < rows; row++){
            LinearLayout colLayout = new LinearLayout(llppdrums);
            colLayout.setOrientation(LinearLayout.HORIZONTAL);
            for(int col = 0; col < cols; col++){
                final FrameLayout cell = new FrameLayout(llppdrums);
                final int color = ColorUtils.getRandomColor();
                FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(width/cols, height/rows);
                cell.setLayoutParams(flp);
                cell.setBackgroundColor(color);
                colLayout.addView(cell);

                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        trackNamePopup.setColor(color);
                        popupWindow.dismiss();
                    }
                });
            }
            rowLayout.addView(colLayout);
        }
        popupView.addView(rowLayout);

        //popupWindow.showAsDropDown(anchor, -width/2, -height/2);
        show(popupWindow);
    }
}
