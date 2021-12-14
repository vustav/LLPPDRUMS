package com.kiefer.popups.files;

import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.KeeperFileHandler;
import com.kiefer.popups.Popup;

public class SavePopup extends Popup {

    public SavePopup(LLPPDRUMS llppdrums, final KeeperFileHandler keeperFileHandler, final String path, final Object keeper){
        super(llppdrums);

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_save, null);
        popupView.findViewById(R.id.saveBgIV).setBackground(ContextCompat.getDrawable(llppdrums, llppdrums.getDrumMachine().getSavePopupBgId()));

        //create the popupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //edit
        final EditText et = popupView.findViewById(R.id.saveEditText);

        //btn
        Button saveBtn = popupView.findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = checkName(et.getText().toString());
                keeperFileHandler.write(keeper, path, name, true);
                popupWindow.dismiss();
            }
        });

        //show the popup with a little offset
        show(popupWindow);
    }

    private String checkName(String name){
        //kolla dublett etc
        return name;
    }
}
