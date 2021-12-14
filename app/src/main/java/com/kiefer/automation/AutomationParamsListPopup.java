package com.kiefer.automation;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

public class AutomationParamsListPopup extends Popup {
    private final CSpinnerButton btn;

    public AutomationParamsListPopup(LLPPDRUMS llppdrums, final AutomationManager automationManager, final AutomationManagerAdapter automationManagerAdapter, final int index, final CSpinnerButton btn){
        super(llppdrums);
        this.btn = btn;


        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        //popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, automationManager.getAutomationListGradient(index)));
        popupView.findViewById(R.id.listBgIV).setBackground(automationManager.getAutomationListGradient(index));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        for(int i = 0; i < automationManager.getParams().size(); i++){
            TextView tv = new TextView(llppdrums);
            tv.setText(automationManager.getParams().get(i));

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

            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    automationManager.setParam(index, finalI);
                    btn.setSelection(automationManager.getParam(index));
                    automationManagerAdapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }
        popupWindow.showAsDropDown(btn, 0, -100);
    }
}
