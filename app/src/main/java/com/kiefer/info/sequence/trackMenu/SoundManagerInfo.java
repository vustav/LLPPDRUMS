package com.kiefer.info.sequence.trackMenu;

import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.info.Info;
import com.kiefer.info.link.InfoLink;
import com.kiefer.utils.ColorUtils;

public class SoundManagerInfo implements Info {
    private final LLPPDRUMS llppdrums;
    public static String key = "soundInfo";

    public SoundManagerInfo(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
    }

    @Override
    public String getName() {
        return llppdrums.getResources().getString(R.string.soundManagerName);
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
        tv.setText(getName());
        int textClr = ColorUtils.getRandomColor();
        tv.setTextColor(textClr);
        tv.setBackgroundColor(ColorUtils.getContrastColor(textClr));

        /** INTRO **/
        FrameLayout nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_iv_tv_vert, null);

        ImageView nodeIV = nodeLayout.findViewById(R.id.infoNodeIV);
        nodeIV.setImageDrawable(llppdrums.getResources().getDrawable(R.drawable.icon_info_trk_menu_sound));

        //get a ref to the tv
        TextView nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.soundManagerIntro);
        nodeTV.append(" ");

        //create the link and append it
        String label = llppdrums.getResources().getString(R.string.trackMenuLabel);
        InfoLink link = new InfoLink(llppdrums, label, TrackMenuInfo.key, nodeTV);
        nodeTV.append(link);
        nodeTV.append(".");

        layout.addView(nodeLayout);

        /** TXT **/
        nodeLayout = (FrameLayout) llppdrums.getLayoutInflater().inflate(R.layout.info_node_tv, null);

        nodeTV = nodeLayout.findViewById(R.id.infoNodeTV);
        nodeTV.setText(R.string.soundManagerTxt);

        layout.addView(nodeLayout);

        return layout;
    }
}
