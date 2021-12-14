package com.kiefer.graphics.customViews;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;

public class CSpinnerButtonRndMode extends CSpinnerButton {
    private final TextView tv;

    public CSpinnerButtonRndMode(LLPPDRUMS llppdrums){
        super(llppdrums);

        tv = new TextView(llppdrums);
        tv.setTextColor(Color.BLACK);
        LayoutParams rlp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp.setMarginStart(14);
        tv.setLayoutParams(rlp);
        addView(tv);
    }

    @Override
    public void setSelection(String s){
        String spaces = "";
        for(int i = 0; i < s.length(); i++){
            spaces += "      ";
        }
        button.setText(spaces);
        tv.setText(s);
    }
}
