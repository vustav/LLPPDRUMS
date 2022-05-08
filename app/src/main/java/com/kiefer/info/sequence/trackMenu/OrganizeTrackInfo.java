package com.kiefer.info.sequence.trackMenu;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.Info;
import com.kiefer.utils.ColorUtils;

public class OrganizeTrackInfo implements Info {
    private final LLPPDRUMS llppdrums;
    public static String key = "organizeTrackInfo";

    public OrganizeTrackInfo(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
    }

    @Override
    public String getName(){
        return llppdrums.getResources().getString(R.string.organizeStepsName);
    }

    @Override
    public String getKey(){
        return key;
    }

    @Override
    public ViewGroup getLayout(){
        LinearLayout layout = (LinearLayout) llppdrums.getLayoutInflater().inflate(R.layout.info, null);
        layout.setBackground(ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor()));

        TextView tv = layout.findViewById(R.id.infoLabelTV);
        tv.setText(getName());
        int textClr = ColorUtils.getRandomColor();
        tv.setTextColor(textClr);
        tv.setBackgroundColor(ColorUtils.getContrastColor(textClr));

        /** JUMP **/
        FrameLayout nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        ImageView nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_auto_steps_jump));

        TextView nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.autoTrackJump);

        layout.addView(nodeLayout);

        /** STEPS **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_auto_steps_steps));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.autoTrackSteps);

        layout.addView(nodeLayout);

        return layout;
    }
}
