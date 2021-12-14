package com.kiefer.info.sequence.trackMenu.fxManager;

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

public class FxManagerInfo extends InfoHolder implements Info {
    public static String key = "fxManagerInfo";

    public FxManagerInfo(LLPPDRUMS llppdrums) {
        super(llppdrums);
    }

    @Override
    public void setupInfos(){
        infos.add(new FlangerInfo(llppdrums));
        infos.add(new DelayInfo(llppdrums));
        infos.add(new ReverbInfo(llppdrums));
        infos.add(new BitCrusherInfo(llppdrums));
        infos.add(new DecimatorInfo(llppdrums));
    }

    @Override
    public String getName() {
        return llppdrums.getResources().getString(R.string.fxManagerName);
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

        /** INTRO **/
        FrameLayout nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_tv, null);

        TextView nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.fxManagerIntro);

        layout.addView(nodeLayout);

        /** RND **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        ImageView nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_fx_manager_rnd));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.fxManagerRnd);

        layout.addView(nodeLayout);

        /** LIST **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_fx_manager_list));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.fxManagerList);

        layout.addView(nodeLayout);

        /** ADD **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_fx_manager_add));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.fxManagerAdd);

        layout.addView(nodeLayout);

        /** FXS **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_tv, null);

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.fxManagerFxs);

        //create the link and append it
        String label = llppdrums.getResources().getString(R.string.fxFlangerName);
        nodeTV.append(" ");
        InfoLink link = new InfoLink(llppdrums, label, FlangerInfo.key, nodeTV);
        nodeTV.append(link);

        label = llppdrums.getResources().getString(R.string.fxDelayName);
        nodeTV.append(", ");
        link = new InfoLink(llppdrums, label, DelayInfo.key, nodeTV);
        nodeTV.append(link);

        label = llppdrums.getResources().getString(R.string.fxReverbName);
        nodeTV.append(", ");
        link = new InfoLink(llppdrums, label, ReverbInfo.key, nodeTV);
        nodeTV.append(link);

        label = llppdrums.getResources().getString(R.string.fxBitCrusherName);
        nodeTV.append(", ");
        link = new InfoLink(llppdrums, label, BitCrusherInfo.key, nodeTV);
        nodeTV.append(link);

        label = llppdrums.getResources().getString(R.string.fxDecimatorName);
        nodeTV.append(", ");
        link = new InfoLink(llppdrums, label, DecimatorInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(". !!MORE WILL BE ADDED!!");

        layout.addView(nodeLayout);

        /** AUTO LIST **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_hor, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_fx_manager_auto_list));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.fxManagerAutoList);

        layout.addView(nodeLayout);

        /** AUTO SINGLE **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_fx_manager_auto_single));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.fxManagerAutoSingle);

        layout.addView(nodeLayout);

        return layout;
    }
}