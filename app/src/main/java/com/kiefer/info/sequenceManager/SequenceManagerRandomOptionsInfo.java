package com.kiefer.info.sequenceManager;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.Info;
import com.kiefer.utils.ColorUtils;

public class SequenceManagerRandomOptionsInfo implements Info {
    private final LLPPDRUMS llppdrums;
    public static String key = "seqManEditRandomInfo";

    public SequenceManagerRandomOptionsInfo(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
    }

    @Override
    public String getName() {
        return llppdrums.getResources().getString(R.string.seqManagerEditRndLabelShort);

    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public ViewGroup getLayout(){
        LinearLayout layout = (LinearLayout) llppdrums.getLayoutInflater().inflate(R.layout.layout_info, null);
        layout.setBackground(ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor()));

        TextView tv = layout.findViewById(R.id.infoLabelTV);
        tv.setText(llppdrums.getResources().getString(R.string.seqManagerEditRndLabel));
        int textClr = ColorUtils.getRandomColor();
        tv.setTextColor(textClr);
        tv.setBackgroundColor(ColorUtils.getContrastColor(textClr));

        /** WIP **/
        FrameLayout nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        ImageView nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_wip));

        TextView nodeTV1 = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV1.setText(R.string.infoWip);

        layout.addView(nodeLayout);

        return layout;
    }
}
