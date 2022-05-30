package com.kiefer.fragments.drumMachine;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.ui.tabs.interfaces.Tab;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.SequenceTabViewHolder> {
    private final LLPPDRUMS llppdrums;
    private final DrumMachineFragment drumMachineFragment;

    public SequenceAdapter(LLPPDRUMS llppdrums, DrumMachineFragment drumMachineFragment) {
        this.llppdrums = llppdrums;
        this.drumMachineFragment = drumMachineFragment;
    }

    // Create new viewHolder
    @Override
    public SequenceAdapter.SequenceTabViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        FrameLayout trackView = (FrameLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tab_single_vertical_play_icon, viewGroup, false);
        return new SequenceAdapter.SequenceTabViewHolder(trackView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final SequenceAdapter.SequenceTabViewHolder sequenceTabViewHolder, final int position) {
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
        sequenceTabViewHolder.view.setLayoutParams(flp);

        int bgColor = llppdrums.getDrumMachine().getSequences().get(sequenceTabViewHolder.getAdapterPosition()).getColor();

        sequenceTabViewHolder.bg.setBackgroundColor(bgColor);
        sequenceTabViewHolder.tv.setText(llppdrums.getDrumMachine().getSequences().get(sequenceTabViewHolder.getAdapterPosition()).getName());

        sequenceTabViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) llppdrums.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(llppdrums.getResources().getInteger(R.integer.vibrateInMs));

                updateTVColors(sequenceTabViewHolder.getAdapterPosition());
                updateBorders(sequenceTabViewHolder.getAdapterPosition());

                llppdrums.getDrumMachine().selectSequence(sequenceTabViewHolder.getAdapterPosition());
                drumMachineFragment.setColor(true);
            }
        });

        //when removing a seq we sometimes get a playIcon on the wrong tab since we set the new playingSeq before removing the old. This fixes that but fix it properly some time.
        if(llppdrums.getDrumMachine().getPlayingSequence().getTabIndex() != sequenceTabViewHolder.getAdapterPosition()) {
            sequenceTabViewHolder.playIcon.setVisibility(View.INVISIBLE);
        }
    }

    public void updateTVColors(int selectedTabIndex){
        for(int i = 0; i < drumMachineFragment.getRecyclerView().getChildCount(); i++){
            ((SequenceTabViewHolder) drumMachineFragment.getRecyclerView().getChildViewHolder(drumMachineFragment.getRecyclerView().getChildAt(i))).tv.setTextColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtColor));
            ((SequenceTabViewHolder) drumMachineFragment.getRecyclerView().getChildViewHolder(drumMachineFragment.getRecyclerView().getChildAt(i))).tv.setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsInactiveTxtBgColor));
            ((SequenceTabViewHolder) drumMachineFragment.getRecyclerView().getChildViewHolder(drumMachineFragment.getRecyclerView().getChildAt(i))).tv.setAlpha(Float.parseFloat(llppdrums.getString(R.string.inactiveTabAlpha)));
        }
        ((SequenceTabViewHolder) drumMachineFragment.getRecyclerView().getChildViewHolder(drumMachineFragment.getRecyclerView().getChildAt(selectedTabIndex))).tv.setBackgroundColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtBgColor));
        ((SequenceTabViewHolder) drumMachineFragment.getRecyclerView().getChildViewHolder(drumMachineFragment.getRecyclerView().getChildAt(selectedTabIndex))).tv.setTextColor(llppdrums.getResources().getColor(R.color.tabsActiveTxtColor));
        ((SequenceTabViewHolder) drumMachineFragment.getRecyclerView().getChildViewHolder(drumMachineFragment.getRecyclerView().getChildAt(selectedTabIndex))).tv.setAlpha(1);
    }

    public void updateBorders(int selectedTabIndex){

        ArrayList<FrameLayout> tabLayouts = new ArrayList<>();
        RecyclerView rv = drumMachineFragment.getRecyclerView();
        for(int i = 0; i < rv.getChildCount(); i++){
            tabLayouts.add(((SequenceTabViewHolder)rv.getChildViewHolder(rv.getChildAt(i))).view);
        }

        drumMachineFragment.getTabManager().setTabBorders(tabLayouts, selectedTabIndex, Tab.VERTICAL);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return llppdrums.getDrumMachine().getNOfSequences();
    }

    /** VIEWHOLDER **/
    public static class SequenceTabViewHolder extends RecyclerView.ViewHolder {
        final FrameLayout view;

        final FrameLayout border;
        final RelativeLayout bg;
        final TextView tv;
        final ImageView playIcon;

        public SequenceTabViewHolder(FrameLayout v) {
            super(v);
            view = v;
            border = v.findViewById(R.id.tabBorder);
            bg = v.findViewById(R.id.tabBg);
            tv = v.findViewById(R.id.tabTxt);
            playIcon = v.findViewById(R.id.tabIcon);
        }
    }
}