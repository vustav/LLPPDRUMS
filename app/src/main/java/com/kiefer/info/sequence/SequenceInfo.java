package com.kiefer.info.sequence;

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
import com.kiefer.info.sequence.trackMenu.TrackMenuInfo;
import com.kiefer.info.sequenceManager.SequenceManagerInfo;
import com.kiefer.utils.ColorUtils;

public class SequenceInfo extends InfoHolder implements Info {
    public static String key = "mainSeqInfo";

    public SequenceInfo(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupInfos(){
        infos.add(new TrackMenuInfo(llppdrums));
        infos.add(new AutoRandomInfo(llppdrums));
        infos.add(new RndSeqManagerInfo(llppdrums));
    }

    @Override
    public String getName(){
        return llppdrums.getResources().getString(R.string.seqName);
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
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_img));

        layout.addView(nodeLayout);

        /** INTRO **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert_bottom, null);

        TextView nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqIntroTxt1);

        //create a to SequenceManager
        nodeTV.append(" ");
        String label = llppdrums.getResources().getString(R.string.seqManagerLabel);
        InfoLink link = new InfoLink(llppdrums, label, SequenceManagerInfo.key, nodeTV);
        nodeTV.append(link);

        //controller
        nodeTV.append(llppdrums.getResources().getString(R.string.seqIntroTxt2));
        label = llppdrums.getResources().getString(R.string.controllerName);
        link = new InfoLink(llppdrums, label, ControllerInfo.key, nodeTV);
        nodeTV.append(" ");
        nodeTV.append(link);
        nodeTV.append(" ");
        nodeTV.append(llppdrums.getResources().getString(R.string.seqIntroTxt3));

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_main_seqmanager));

        layout.addView(nodeLayout);

        /** SEQUENCE **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_hor, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //set the initial text
        nodeTV.setText(R.string.seqSequenceTxt);

        layout.addView(nodeLayout);

        /** SEQ MODS **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_hor, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_module));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqModuleTxt);

        layout.addView(nodeLayout);

        /** MODE **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_hor, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_mode));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqModeTxt);
        nodeTV.append(" ");

        //create a link and append it
        label = llppdrums.getResources().getString(R.string.seqModRandomModeName);
        link = new InfoLink(llppdrums, label, AutoRandomInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(".");

        layout.addView(nodeLayout);

        /** TRACK **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_track));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqTrackTxt1);
        nodeTV.append(" ");

        //create a link and append it
        label = llppdrums.getResources().getString(R.string.trackMenuLabel);
        link = new InfoLink(llppdrums, label, TrackMenuInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(". "+llppdrums.getResources().getString(R.string.seqTrackTxt2)+" ");

        //create a link and append it
        label = llppdrums.getResources().getString(R.string.trackMenuLabel);
        link = new InfoLink(llppdrums, label, TrackMenuInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(" "+llppdrums.getResources().getString(R.string.seqTrackTxt3));

        nodeTV.append(llppdrums.getResources().getString(R.string.seqAddRemoveStepTxt));
        nodeTV.append(llppdrums.getResources().getString(R.string.seqAddTrackTxt));

        layout.addView(nodeLayout);

        /*
        // ADD/REMOVE STEPS
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_add_remove_step));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqAddRemoveStepTxt);

        layout.addView(nodeLayout);

        // ADD TRACK
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_add_track));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqAddTrackTxt);

        layout.addView(nodeLayout);
        */

        /** NAME **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_name));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqNameTxt);

        layout.addView(nodeLayout);

        /** TMP **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_tempo));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqTmpTxt);

        layout.addView(nodeLayout);

        /** RND **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_rnd));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqRndTxt);
        nodeTV.append(" ");

        //create a link and append it
        label = "RANDOMIZE SEQUENCE OPTIONS";
        link = new InfoLink(llppdrums, label, RndSeqManagerInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(".");

        layout.addView(nodeLayout);

        /** CPY **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_cpy));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqCpyTxt);

        layout.addView(nodeLayout);

        /** REMOVE **/
        nodeTV.append(llppdrums.getResources().getString(R.string.seqRemoveTxt));
        /*
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_remove));

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqRemoveTxt);

        layout.addView(nodeLayout);

         */

        return layout;
    }
}
