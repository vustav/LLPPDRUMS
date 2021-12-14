package com.kiefer.graphics.customViews;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;

public class CSpinnerButtonTrack extends RelativeLayout {
    private final Button button;
    private final TextView label;

    public CSpinnerButtonTrack(LLPPDRUMS llppdrums){
        super(llppdrums);

        LayoutParams rlp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(rlp);

        button = new Button(llppdrums);
        button.setId(View.generateViewId());
        rlp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        button.setLayoutParams(rlp);

        TextView arrows = new TextView(llppdrums);
        arrows.setTextColor(Color.BLACK);
        arrows.setText(">>");
        arrows.setId(View.generateViewId());
        arrows.setSingleLine();
        rlp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp.addRule(CENTER_VERTICAL);
        rlp.addRule(ALIGN_PARENT_END);
        rlp.setMargins(0, 0, 20, 10);
        arrows.setLayoutParams(rlp);

        label = new TextView(llppdrums);
        label.setTextColor(Color.BLACK);
        label.setSingleLine();
        rlp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rlp.addRule(START_OF, arrows.getId());
        rlp.addRule(ALIGN_PARENT_START);
        rlp.addRule(CENTER_VERTICAL);
        rlp.setMargins(20, 0, 0, 10);
        label.setLayoutParams(rlp);


        addView(button);
        addView(arrows);
        addView(label);
    }

    public void setSelection(String selection){
        label.setText(selection);
    }

    public Button getButton(){
        return button;
    }

    public String getSelection(){
        return label.getText().toString();
    }
}
