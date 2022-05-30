package com.kiefer.info.controller;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.Info;
import com.kiefer.info.InfoHolder;
import com.kiefer.info.link.InfoLink;
import com.kiefer.utils.ColorUtils;

public class ControllerInfo extends InfoHolder implements Info {
    public static String key = "contrInfo";

    public ControllerInfo(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupInfos(){
        infos.add(new SeqBtnOptionsInfo(llppdrums));
        infos.add(new FunBtnOptionsInfo(llppdrums));
    }

    @Override
    public String getName(){
        return "CONTROLLER";
    }

    @Override
    public String getKey(){
        return key;
    }

    @Override
    public ViewGroup getLayout(){
        LinearLayout layout = (LinearLayout) llppdrums.getLayoutInflater().inflate(R.layout.layout_info, null);
        layout.setBackground(ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor()));

        TextView tv = layout.findViewById(R.id.infoLabelTV);
        tv.setText(getName());
        int textClr = ColorUtils.getRandomColor();
        tv.setTextColor(textClr);
        tv.setBackgroundColor(ColorUtils.getContrastColor(textClr));

        /** IMG **/
        FrameLayout nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv, null);

        ImageView nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_contr_img));

        layout.addView(nodeLayout);

        /** INTRO **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_tv, null);

        TextView nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.controllerIntroTxt);

        layout.addView(nodeLayout);

        /** COUNTER **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_contr_counter));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.controllerCounterTxt);

        layout.addView(nodeLayout);

        /** ARROWS **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_hor, null);

        //add the first image
        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_contr_left));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.controllerArrowsTxt);

        layout.addView(nodeLayout);

        /** SEQ **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_hor, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_contr_seq));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //set the initial text
        nodeTV.setText(R.string.controllerSeqTxt1);
        nodeTV.append(" ");

        //create a link and append it
        String label = llppdrums.getResources().getString(R.string.seqBtnEditLabel);
        InfoLink link = new InfoLink(llppdrums, label, SeqBtnOptionsInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(llppdrums.getResources().getString(R.string.controllerSeqTxt2));

        layout.addView(nodeLayout);

        /** FUN **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_hor, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_contr_fun));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //set the initial text
        nodeTV.setText(R.string.controllerFunTxt1);
        nodeTV.append(" ");

        //create a link and append it
        label = llppdrums.getResources().getString(R.string.funBtnEditLabel);
        link = new InfoLink(llppdrums, label, FunBtnOptionsInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(llppdrums.getResources().getString(R.string.controllerFunTxt2));

        layout.addView(nodeLayout);

        return layout;
    }
}

