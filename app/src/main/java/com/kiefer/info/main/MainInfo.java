package com.kiefer.info.main;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.Info;
import com.kiefer.info.InfoHolder;
import com.kiefer.info.controller.ControllerInfo;
import com.kiefer.info.link.InfoLink;
import com.kiefer.info.sequenceManager.SequenceManagerInfo;
import com.kiefer.info.sequence.SequenceInfo;
import com.kiefer.utils.ColorUtils;

public class MainInfo extends InfoHolder implements Info {
    public static String key = "mainInfo";

    public MainInfo(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupInfos(){
        infos.add(new ProjectOptionsInfo(llppdrums));
    }

    @Override
    public String getName(){
        return "MAIN";
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
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_main_img));

        layout.addView(nodeLayout);

        /** INTRO **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_tv, null);

        TextView nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.mainIntroTxt1);

        //create the link and append it
        String label = llppdrums.getResources().getString(R.string.machineName);
        InfoLink link = new InfoLink(llppdrums, label, SequenceInfo.key, nodeTV);
        nodeTV.append(" ");
        nodeTV.append(link);
        nodeTV.append(" and ");
        label = llppdrums.getResources().getString(R.string.controllerName);
        link = new InfoLink(llppdrums, label, ControllerInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(". ");
        nodeTV.append(llppdrums.getResources().getString(R.string.mainIntroTxt2));
        nodeTV.append(llppdrums.getResources().getString(R.string.mainIntroTxt3));
        nodeTV.append(" ");
        label = llppdrums.getResources().getString(R.string.seqManagerLabel);
        link = new InfoLink(llppdrums, label, SequenceManagerInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(" ");
        nodeTV.append(llppdrums.getResources().getString(R.string.mainIntroTxt4));

        layout.addView(nodeLayout);

        /** TRANSPORT **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_main_transport));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.mainTransportTxt);

        layout.addView(nodeLayout);

        /** SEQ MANAGER **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_main_seqmanager));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //set the initial text
        nodeTV.setText(R.string.mainSeqManTxt1);
        nodeTV.append(" ");

        //create a link and append it
        label = llppdrums.getResources().getString(R.string.seqManagerLabel);
        link = new InfoLink(llppdrums, label, SequenceManagerInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(".");
        nodeTV.append(llppdrums.getResources().getString(R.string.mainSeqManTxt2));
        nodeTV.append(llppdrums.getResources().getString(R.string.mainSeqManTxt3));

        layout.addView(nodeLayout);

        /** OPTIONS **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_main_options));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //create the link and append it
        label = llppdrums.getResources().getString(R.string.projectOptionsLabel);
        link = new InfoLink(llppdrums, label, ProjectOptionsInfo.key, nodeTV);
        nodeTV.append(link);

        layout.addView(nodeLayout);

        /** FILES **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_main_file));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.mainFilesTxt);

        layout.addView(nodeLayout);

        /** TABS **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_main_tabs));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //create a link and append it
        label = llppdrums.getResources().getString(R.string.machineName);
        link = new InfoLink(llppdrums, label, SequenceInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(" or ");
        label = llppdrums.getResources().getString(R.string.controllerName);
        link = new InfoLink(llppdrums, label, ControllerInfo.key, nodeTV);
        nodeTV.append(link);

        layout.addView(nodeLayout);

        return layout;
    }
}
