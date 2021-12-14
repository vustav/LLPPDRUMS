package com.kiefer.info.sequence.trackMenu;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.Info;
import com.kiefer.utils.ColorUtils;

public class RandomizeTrackInfo implements Info {
    private final LLPPDRUMS llppdrums;
    public static String key = "rndTrackInfo";

    public RandomizeTrackInfo(LLPPDRUMS llppdrums) {
        this.llppdrums = llppdrums;
    }

    @Override
    public String getName() {
        return llppdrums.getResources().getString(R.string.rndTrackName);
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

        /** WIP **/
        FrameLayout nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_tv, null);

        TextView nodeTV1 = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV1.setText(R.string.rndTrackIntro);

        layout.addView(nodeLayout);

        return layout;
    }
}
