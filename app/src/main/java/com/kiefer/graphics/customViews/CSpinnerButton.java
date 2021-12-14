package com.kiefer.graphics.customViews;

//just the button. Open a ListPopup in the listener.

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.kiefer.LLPPDRUMS;

public class CSpinnerButton extends RelativeLayout {
    private LLPPDRUMS llppdrums;
    protected final Button button;
    //private final ImageView iv;

    public CSpinnerButton(LLPPDRUMS llppdrums){
        super(llppdrums);

        this.llppdrums = llppdrums;

        LayoutParams rlp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(rlp);

        button = new Button(llppdrums);
        button.setId(View.generateViewId());
        rlp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        button.setLayoutParams(rlp);


        addView(button);
    }

    public void setWidth(int width){
        LayoutParams rlp = new LayoutParams(width, getLayoutParams().height);
        setLayoutParams(rlp);
    }

    public void setHeight(int height){
        LayoutParams rlp = new LayoutParams(getLayoutParams().width, height);
        setLayoutParams(rlp);
    }

    public void setSelection(String s){
        String string = s+"▼";
        button.setText(string);
    }

    public void setTextSize(float size){
        button.setTextSize(size);
    }

    public Button getButton(){
        return button;
    }

    public void seButtonWidth(int width){
        button.setWidth(width);
    }

    @Override
    public void setAlpha(final float alpha){
        llppdrums.runOnUiThread(new Runnable() {
            public void run() {
                button.setAlpha(alpha);
                //iv.setAlpha(alpha);
            }
        });
    }

    @Override
    public void setEnabled(final boolean enabled){
        llppdrums.runOnUiThread(new Runnable() {
            public void run() {
                button.setEnabled(enabled);
                //iv.setEnabled(enabled);
            }
        });
    }
}
