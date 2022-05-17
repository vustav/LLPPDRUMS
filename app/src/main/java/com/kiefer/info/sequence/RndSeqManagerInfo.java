package com.kiefer.info.sequence;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.Info;
import com.kiefer.utils.ColorUtils;

public class RndSeqManagerInfo implements Info {
    private final LLPPDRUMS llppdrums;
    public static String key = "seqRndOptBaseInfo";

    public RndSeqManagerInfo(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
    }

    @Override
    public String getName(){
        return llppdrums.getResources().getString(R.string.randomizeSeqLabel);
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

        /** INTRO **/
        FrameLayout nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        TextView nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqRndManagerIntro);

        ImageView nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_rnd));

        layout.addView(nodeLayout);

        /** INTRO2 **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_tv, null);

        TextView nodeTV1 = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV1.setText(R.string.seqRndManagerIntro2);

        layout.addView(nodeLayout);

        /** PRESET **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_rnd_preset));

        nodeTV1 = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV1.setText(R.string.seqRndManagerPreset);

        layout.addView(nodeLayout);

        /** TEMPO **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_rnd_tmp));

        nodeTV1 = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV1.setText(R.string.seqRndManagerTempo);

        layout.addView(nodeLayout);

        /** STEPS **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_rnd_stps));

        nodeTV1 = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV1.setText(R.string.seqRndManagerSteps);

        layout.addView(nodeLayout);

        /** TRACK **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_rnd_trk));

        nodeTV1 = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV1.setText(R.string.seqRndManagerTracks);

        layout.addView(nodeLayout);

        /** ADD **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_rnd_add));

        nodeTV1 = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV1.setText(R.string.seqRndManagerAdd);

        layout.addView(nodeLayout);

        return layout;
    }
}