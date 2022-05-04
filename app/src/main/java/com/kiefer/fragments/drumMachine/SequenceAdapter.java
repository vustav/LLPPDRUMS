package com.kiefer.fragments.drumMachine;

import android.content.Context;
import android.os.Vibrator;
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

import java.util.ArrayList;

public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.SequenceTabViewHolder> {
    private final LLPPDRUMS llppdrums;
    private final DrumMachineFragment drumMachineFragment;

    public SequenceAdapter(LLPPDRUMS llppdrums, DrumMachineFragment drumMachineFragment) {
        this.llppdrums = llppdrums;
        this.drumMachineFragment = drumMachineFragment;

        updateBorders(0);
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

        sequenceTabViewHolder.bg.setBackgroundColor(llppdrums.getDrumMachine().getSequences().get(sequenceTabViewHolder.getAdapterPosition()).getColor());
        sequenceTabViewHolder.tv.setText(llppdrums.getDrumMachine().getSequences().get(sequenceTabViewHolder.getAdapterPosition()).getName());

        sequenceTabViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator) llppdrums.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(llppdrums.getResources().getInteger(R.integer.vibrateInMs));

                updateBorders(sequenceTabViewHolder.getAdapterPosition());
                llppdrums.getDrumMachine().selectSequence(sequenceTabViewHolder.getAdapterPosition());
                drumMachineFragment.setColor();
            }
        });
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