package com.kiefer.popups;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;

import java.util.Random;

public abstract class Popup {
    protected LLPPDRUMS llppdrums;
    protected View popupView;

    protected int DEF_LIST_WIDTH = 300;

    public Popup(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
    }

    public void show(PopupWindow popupWindow){
        show(popupWindow, Gravity.CENTER);
    }

    public static void dimBehind(PopupWindow popupWindow, float dimAmount) {
        View container = popupWindow.getContentView().getRootView();
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = dimAmount;
        wm.updateViewLayout(container, p);
    }

    /** SHOW **/
    protected void show(PopupWindow popupWindow, int gravity){
        Random r = new Random();
        int offset = llppdrums.getResources().getInteger(R.integer.popupOffset);
        int modX = (offset/2) - r.nextInt(offset);
        int modY = (offset/2) - r.nextInt(offset);
        popupWindow.showAtLocation(llppdrums.getLayout(), gravity, modX, modY);
        dimBehind(popupWindow, .8f);
    }

    protected void show(PopupWindow popupWindow, int gravity, float dimAmount, boolean offset){
        Random r = new Random();

        int modX, modY;
        if(offset) {
            int off = llppdrums.getResources().getInteger(R.integer.popupOffset);
            modX = (off / 2) - r.nextInt(off);
            modY = (off / 2) - r.nextInt(off);
        }
        else{
            modX = 0;
            modY = 0;
        }
        popupWindow.showAtLocation(llppdrums.getLayout(), gravity, modX, modY);
        dimBehind(popupWindow, dimAmount);
    }
}
