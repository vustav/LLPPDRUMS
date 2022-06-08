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
import com.kiefer.info.sequence.SequenceInfo;
import com.kiefer.info.sequence.trackMenu.TrackMenuInfo;
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
        infos.add(new TremoloInfo(llppdrums));
        infos.add(new FFInfo(llppdrums));
        infos.add(new WSInfo(llppdrums));
        infos.add(new HPLPInfo(llppdrums));
        infos.add(new LimiterInfo(llppdrums));
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
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_fx_manager_img));

        layout.addView(nodeLayout);

        /** INTRO **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_trk_menu_fx));

        //get a ref to the tv
        TextView nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.fxManagerIntro);
        nodeTV.append(" ");

        //create the link and append it
        String label = llppdrums.getResources().getString(R.string.trackMenuLabel);
        InfoLink link = new InfoLink(llppdrums, label, TrackMenuInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(" or ");
        label = llppdrums.getResources().getString(R.string.machineName);
        link = new InfoLink(llppdrums, label, SequenceInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(".");

        layout.addView(nodeLayout);

        /** INTRO2 **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_tv, null);

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.fxManagerIntro2);

        layout.addView(nodeLayout);

        /** RND **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
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
        nodeTV.append(llppdrums.getResources().getString(R.string.fxManagerAdd));

        /** FXS **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_tv, null);

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.fxManagerFxs);

        //create the link and append it
        label = llppdrums.getResources().getString(R.string.fxFlangerName);
        nodeTV.append(" ");
        link = new InfoLink(llppdrums, label, FlangerInfo.key, nodeTV);
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

        label = llppdrums.getResources().getString(R.string.fxTremoloName);
        nodeTV.append(", ");
        link = new InfoLink(llppdrums, label, TremoloInfo.key, nodeTV);
        nodeTV.append(link);

        label = llppdrums.getResources().getString(R.string.fxFFName);
        nodeTV.append(", ");
        link = new InfoLink(llppdrums, label, FFInfo.key, nodeTV);
        nodeTV.append(link);

        label = llppdrums.getResources().getString(R.string.fxWSName);
        nodeTV.append(", ");
        link = new InfoLink(llppdrums, label, WSInfo.key, nodeTV);
        nodeTV.append(link);

        label = llppdrums.getResources().getString(R.string.fxLPName);
        nodeTV.append(", ");
        link = new InfoLink(llppdrums, label, HPLPInfo.key, nodeTV);
        nodeTV.append(link);

        label = llppdrums.getResources().getString(R.string.fxLimiterName);
        nodeTV.append(", ");
        link = new InfoLink(llppdrums, label, LimiterInfo.key, nodeTV);
        nodeTV.append(link);

        nodeTV.append(".");

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