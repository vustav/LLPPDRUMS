package com.kiefer.info.main;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.Info;
import com.kiefer.utils.ColorUtils;

public class ProjectOptionsInfo implements Info {
    private final LLPPDRUMS llppdrums;
    public static String key = "projOptionsInfo";

    public ProjectOptionsInfo(LLPPDRUMS llppdrums) {
        this.llppdrums = llppdrums;
    }

    @Override
    public String getName() {
        return "PROJECT OPTIONS";
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public ViewGroup getLayout() {
        LinearLayout layout = (LinearLayout) llppdrums.getLayoutInflater().inflate(R.layout.info, null);
        layout.setBackground(ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor()));

        TextView tv = layout.findViewById(R.id.infoLabelTV);
        tv.setText(getName());
        int textClr = ColorUtils.getRandomColor();
        tv.setTextColor(textClr);
        tv.setBackgroundColor(ColorUtils.getContrastColor(textClr));

        //1
        FrameLayout nodeLayout1 = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_tv, null);

        TextView nodeTV1 = nodeLayout1.findViewById(R.id.infoNodeTV);
        nodeTV1.setText(R.string.projOptnsDriverTxt);

        layout.addView(nodeLayout1);

        return layout;
    }
}