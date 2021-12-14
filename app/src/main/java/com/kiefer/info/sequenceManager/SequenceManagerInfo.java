package com.kiefer.info.sequenceManager;

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

public class SequenceManagerInfo extends InfoHolder implements Info {
    public static String key = "seqManInfo";

    public SequenceManagerInfo(LLPPDRUMS llppdrums){
        super(llppdrums);
    }

    @Override
    public void setupInfos(){
        infos.add(new SequenceManagerRandomOptionsInfo(llppdrums));
    }

    @Override
    public String getName(){
        return llppdrums.getResources().getString(R.string.seqManagerLabel);
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

        /** ORDER **/
        FrameLayout nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        ImageView nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_man_order));

        TextView nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.seqManOrderTxt);

        layout.addView(nodeLayout);

        /** BOXES **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_seq_man_boxes));

        //get a ref to the tv
        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);

        //set the initial text
        nodeTV.setText(R.string.seqManBoxesTxt1);
        nodeTV.append(" ");

        //create a link and append it
        String label = llppdrums.getResources().getString(R.string.seqManagerEditRndLabel);
        InfoLink link = new InfoLink(llppdrums, label, SequenceManagerRandomOptionsInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(".");
        nodeTV.append(llppdrums.getResources().getString(R.string.seqManBoxesTxt2));
        nodeTV.append(llppdrums.getResources().getString(R.string.seqManBoxesTxt3));

        layout.addView(nodeLayout);

        return layout;
    }
}
