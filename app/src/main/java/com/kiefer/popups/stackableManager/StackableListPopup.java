package com.kiefer.popups.stackableManager;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.track.Stackables.StackableManager;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ColorUtils;

public class StackableListPopup extends Popup {
    private final CSpinnerButton btn;

    public StackableListPopup(LLPPDRUMS llppdrums, final StackableManager stackableManager, final StackableManagerPopup stackableManagerPopup, final CSpinnerButton btn){
        super(llppdrums);
        this.btn = btn;

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_list, null);
        popupView.findViewById(R.id.listBgIV).setBackground(ContextCompat.getDrawable(llppdrums, stackableManager.getListPopupImgId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        LinearLayout listLayout = popupView.findViewById(R.id.listLayout);

        for(int i = 0; i < stackableManager.getStackableInfos().size(); i++){
            TextView tv = new TextView(llppdrums);
            tv.setText(stackableManager.getStackableInfos().get(i).getName());

            int color = stackableManager.getStackableInfos().get(i).getColor();
            tv.setBackgroundColor(color);
            tv.setTextColor(ColorUtils.getContrastColor(color));
            tv.setTextSize((int) llppdrums.getResources().getDimension(R.dimen.defaultListTxtSize));
            tv.setTextColor(ColorUtils.getContrastColor(color));

            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stackableManagerPopup.changeSelected(finalI);
                    popupWindow.dismiss();
                }
            });
            listLayout.addView(tv);
        }
        //popupWindow.showAsDropDown(btn, 0, -100);
        show(popupWindow);
    }
}
