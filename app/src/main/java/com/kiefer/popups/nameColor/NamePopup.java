package com.kiefer.popups.nameColor;

import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.popups.Popup;
import com.kiefer.utils.ImgUtils;

public class NamePopup extends Popup {
    private final NamerColorizer namerColorizer;
    private final FrameLayout colorPicker;

    public NamePopup(final LLPPDRUMS llppdrums, final NamerColorizer namerColorizer){
        super(llppdrums);
        this.namerColorizer = namerColorizer;

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_track_name, null);
        //popupView.setBackground(ContextCompat.getDrawable(llppdrums, drumTrack.getRndPopupBgId()));
        popupView.findViewById(R.id.trackNameBgIV).setBackground(ContextCompat.getDrawable(llppdrums, ImgUtils.getRandomImageId()));

        //create the popupWindow
        //int width = (int) llppdrums.getResources().getDimension(R.dimen.autoStepsPopupWidth);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //color
        colorPicker = popupView.findViewById(R.id.trackColorPicker);
        colorPicker.setBackgroundColor(namerColorizer.getColor());
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PalettePopup(llppdrums, NamePopup.this, colorPicker);
            }
        });

        //editText
        EditText editText = popupView.findViewById(R.id.trackNameEditText);
        editText.setText(namerColorizer.getName());
        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                namerColorizer.setName(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        //show(popupWindow, Gravity.START, 0);
        show(popupWindow);
    }

    public void setColor(int color){
        colorPicker.setBackgroundColor(color);
        namerColorizer.setColor(color);
    }
}
