package com.kiefer.popups.trackMenu;

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
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.Popup;

public class TrackNamePopup extends Popup {
    private final DrumTrack drumTrack;
    private final FrameLayout colorPicker;

    public TrackNamePopup(final LLPPDRUMS llppdrums, final DrumTrack drumTrack){
        super(llppdrums);
        this.drumTrack = drumTrack;

        //inflate the View
        final View popupView = llppdrums.getLayoutInflater().inflate(R.layout.popup_track_name, null);
        //popupView.setBackground(ContextCompat.getDrawable(llppdrums, drumTrack.getRndPopupBgId()));
        popupView.findViewById(R.id.trackNameBgIV).setBackground(ContextCompat.getDrawable(llppdrums, drumTrack.getNamePopupBgId()));

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
        colorPicker.setBackgroundColor(drumTrack.getColor());
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PalettePopup(llppdrums, TrackNamePopup.this, colorPicker);
            }
        });

        //editText
        EditText editText = popupView.findViewById(R.id.trackNameEditText);
        editText.setText(drumTrack.getName());
        editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                drumTrack.setName(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        //show(popupWindow, Gravity.START, 0);
        show(popupWindow);
    }

    public void setColor(int color){
        //label.setBackgroundColor(color);
        //label.setTextColor(ColorUtils.getContrastColor(color));
        colorPicker.setBackgroundColor(color);
        drumTrack.setColor(color);
    }
}
