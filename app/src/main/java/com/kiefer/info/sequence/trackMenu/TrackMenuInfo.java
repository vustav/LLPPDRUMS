package com.kiefer.info.sequence.trackMenu;

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
import com.kiefer.info.sequence.trackMenu.fxManager.FxManagerInfo;
import com.kiefer.utils.ColorUtils;

public class TrackMenuInfo extends InfoHolder implements Info {
    public static String key = "trackMenuInfo";

    public TrackMenuInfo(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupInfos(){
        infos.add(new SoundManagerInfo(llppdrums));
        infos.add(new OrganizeTrackInfo(llppdrums));
        infos.add(new RandomizeTrackInfo(llppdrums));
        infos.add(new FxManagerInfo(llppdrums));
        infos.add(new MixerInfo(llppdrums));
    }

    @Override
    public String getName(){
        return llppdrums.getResources().getString(R.string.trackMenuLabel);
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

        /** NAME **/
        FrameLayout nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        ImageView nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_trk_menu_name));

        TextView nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.trkMenuName);

        layout.addView(nodeLayout);

        /** OSC **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_trk_menu_osc));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //create the link and append it
        String label = llppdrums.getResources().getString(R.string.soundManagerName);
        InfoLink link = new InfoLink(llppdrums, label, SoundManagerInfo.key, nodeTV);
        nodeTV.append(link);

        layout.addView(nodeLayout);

        /** SUBS **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_trk_menu_subs));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.nOfSubsName);

        layout.addView(nodeLayout);

        /** AUTO **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_trk_menu_auto));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //create the link and append it
        label = llppdrums.getResources().getString(R.string.organizeStepsName);
        link = new InfoLink(llppdrums, label, OrganizeTrackInfo.key, nodeTV);
        nodeTV.append(link);

        layout.addView(nodeLayout);

        /** RND **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_trk_menu_rnd));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //create the link and append it
        label = llppdrums.getResources().getString(R.string.rndTrackName);
        link = new InfoLink(llppdrums, label, RandomizeTrackInfo.key, nodeTV);
        nodeTV.append(link);

        layout.addView(nodeLayout);

        /** FX **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_trk_menu_fx));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //create the link and append it
        label = llppdrums.getResources().getString(R.string.fxManagerName);
        link = new InfoLink(llppdrums, label, FxManagerInfo.key, nodeTV);
        nodeTV.append(link);

        layout.addView(nodeLayout);

        /** MIXER **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_trk_menu_mixer));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //create the link and append it
        label = llppdrums.getResources().getString(R.string.mixerName);
        link = new InfoLink(llppdrums, label, MixerInfo.key, nodeTV);
        nodeTV.append(link);

        layout.addView(nodeLayout);

        /** REMOVE **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_trk_menu_remove));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.trkMenuRemove);

        layout.addView(nodeLayout);

        return layout;
    }
}
