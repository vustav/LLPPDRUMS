package com.kiefer.popups.projectOptions;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.engine.EngineFacade;
import com.kiefer.options.projectOptions.ProjectOptionsManager;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

public class DriverPopup extends Popup {

    public DriverPopup(LLPPDRUMS llppdrums, int imgId, final CSpinnerButton btn, ProjectOptionsManager projectOptionsManager){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.setBackground(ContextCompat.getDrawable(llppdrums, imgId));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        final EngineFacade engineFacade = llppdrums.getEngineFacade();
        for(int i = 0; i < engineFacade.getDrivers().length; i++){
            TextView tv = new TextView(llppdrums);
            tv.setText(engineFacade.getDrivers()[i]);

            int color;
            if(i % 2 == 0){
                color = ContextCompat.getColor(llppdrums, R.color.wavesBgEven);
            }
            else{
                color = ContextCompat.getColor(llppdrums, R.color.wavesBgUneven);
            }
            tv.setBackgroundColor(color);
            tv.setTextSize((int) llppdrums.getResources().getDimension(R.dimen.defaultListTxtSize));
            tv.setTextColor(ColorUtils.getContrastColor(color));

            //final int finalI = i;
            final String driver = engineFacade.getDrivers()[i];
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    projectOptionsManager.setDriver(driver);
                    btn.setSelection(driver);
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }
        popupWindow.showAsDropDown(btn);
    }
}
